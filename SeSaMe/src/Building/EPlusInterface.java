package Building;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import A3JGroups.A3JGFollowerRole;
import A3JGroups.A3JGMessage;
import A3JGroups.SSM.utils.Status;
import A3JGroups.SSM.utils.Update;

public  class EPlusInterface extends A3JGFollowerRole   {
	
	Status state;
	boolean status = true;
	Double extTemp;
	ArrayList<Double> temp;
	ArrayList<Double> extRad;
	ArrayList<Double> intLum; 
	ArrayList<Double> slatAngle;
	String[] args;

	Socket socket = null;
	int port = -1;
	int verNo = 1;
	String host= null;

	
	public EPlusInterface(int resourceCost, String[] argmts ) {
		super(resourceCost);
		temp= new ArrayList<Double>(Collections.nCopies(12,0.0));
		extRad= new ArrayList<Double>(Collections.nCopies(12,0.0));
		intLum= new ArrayList<Double>(Collections.nCopies(12,0.0));
		slatAngle= new ArrayList<Double>(Collections.nCopies(12,25.0));
		args = argmts;
	}

	

	@Override
	public void run() {
		
		try {
			exchangeData();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
			while(active){
//
//				try {
//					Thread.sleep(500);
//				} catch (InterruptedException e) {
//					Thread.currentThread().interrupt();
//				}

//				state = new Status();
//				state.setExtRadiation(((EPlusNode)getNode()).getExtRad());
//				state.setIntLum(((EPlusNode)getNode()).getIntLum());
//				state.setIntTemp(((EPlusNode)getNode()).getTemp());
//				state.setNodeAdd(getChan().getAddress());
//				state.setNodeID(getNode().getID());
				
		}}

	@Override
	public void messageFromSupervisor(A3JGMessage msg) {
			System.out.println("Message from SV");
			Update updt = ((Update)msg.getContent());
			for(int i=0; i<slatAngle.size();i++)
				slatAngle = updt.getAngle();
		
		}

	
	public void exchangeData() throws IOException {

				System.out.println("In Data Exchange");
				ReadConfigFile();

				int flaWri = 0;
				int flaRea = 0;
				// Number of variables to be exchanged
				int nDblWri = 12;
				int nDblRea = 0;

				// Arrays that contain the variables to be exchanged
				double[] dblValWri= new double[12];
				double[] dblValRea= new double[37];
				int i, iSte, retVal = 0;
				// set simulation time step
				double delTim;
				double endTim = Integer.parseInt(args[1])*24*3600;

				//////////////////////////////////////////////////////
				// Declare variables of model
				double simTimWri = 0;
				double simTimRea = 0;
				int nSte;

				delTim = Integer.parseInt(args[0]);
				System.out.println("Simulation model has time step   "+ delTim);
				System.out.println("Simulation model has end time   "+endTim);

				if (delTim <= 0){
					System.out.println("Error: End time must be bigger than zero.");
					System.out.println("Usage: %s simulation_timestep_in_seconds end_time_in_seconds "+ args[0]);
					return;
				}

				nSte = (int) Math.floor((endTim/delTim) + 0.5); // added by TNouidui. nearbyint not defined on Windows.
				System.out.println(" Number of time steps: "+ nSte);
				if (Math.abs(nSte*delTim-endTim) > 1E-10*endTim){
					System.out.println("Error: End time divided by time step must be an integer.");
					System.out.println("       Number of time steps is. "+ nSte);
					System.out.println("Usage:  simulation_timestep_in_seconds end_time_in_seconds "+ args[0]);
					return;
				}

				/////////////////////////////////////////////////////////////
				// Establish the client socket
				boolean conn = false;
				try {
					socket = new Socket(host, port);
					conn = true;
					System.out.println("Socket InetAddress: "+socket.getInetAddress());
					System.out.println("Socket Local Port: "+socket.getLocalPort());
					System.out.println("Socket Remote Port: "+socket.getPort());


				} catch (UnknownHostException e) {
					System.err.println("Can't find: " + args[0]);
					System.exit(1);
				} catch (IOException e) {
					System.err.println("IO error..");
					exchangeData();
					//System.exit(1);
				}


				if(conn){
				/////////////////////////////////////////////////////////////
				// Simulation loop
				for(iSte=0; iSte <= nSte+1; iSte++){
					System.out.println("In simulation Loop");
					// Set simulation time
					simTimWri = (double)iSte*delTim; // set simulation time
					/////////////////////////////////////////////////////////////
					// assign values to be exchanged
								for(i=0; i < nDblWri; i++)
									dblValWri[i]=slatAngle.get(i);

					

					/////////////////////////////////////////////////////////////
					// Exchange values

					final int nDbl= (dblValWri != null) ? dblValWri.length : 0;
					StringBuffer strBuf = new StringBuffer(Integer.toString(verNo));
					strBuf.append(" " + Integer.toString(flaWri)); // the communication flag
					strBuf.append(" " + Integer.toString(nDbl)); // then number of doubles
					strBuf.append(" 0 0 "); // the number of integers and booleans
					strBuf.append(simTimWri); // the current simulation time
					strBuf.append(" ");
					for (int k = 0; k < nDbl; k++) {
						strBuf.append(String.valueOf(dblValWri[k]));
						strBuf.append(" ");
					}
					// add line termination for parsing in client
					strBuf.append("\n");

					BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					wr.write(new String(strBuf));
					wr.flush();

					final InputStreamReader inpStrRea = new InputStreamReader(socket.getInputStream());
					final BufferedReader d = new BufferedReader(inpStrRea);
					final String line = d.readLine();
					System.out.println("From Server "+line);
					String[] ele = line.split(" ");
					Integer.parseInt(ele[0]);
					flaRea = Integer.parseInt(ele[1]); // the communication flag
					if (flaRea == 0) { // read further if flag is nonzero
						nDblRea = Integer.parseInt(ele[2]);
						Integer.parseInt(ele[3]);
						Integer.parseInt(ele[4]);
						simTimRea = Double.parseDouble(ele[5]);
						dblValRea = new double[nDblRea];
						// check sufficient array lenght
						if (nDblRea != dblValRea.length) {
							throw new IOException("Received " + nDblRea
									+ " doubles, but expected " + dblValRea.length
									+ " elements.");
						}
						for (int j = 0; j < nDblRea; j++) {
							dblValRea[j] = Double.parseDouble(ele[6 + j]);
							if(iSte==0 || iSte%100 == 0)
								System.out.println("Parameter "+j+" "+dblValRea[j]);
						}
					}

					/////////////////////////////////////////////////////////////

					// Check flags
					if (retVal < 0){
						System.out.println("Simulator received value when reading from socket. Exit simulation. " + retVal);
					}

					if (flaRea == 1){
						System.out.println("Simulator received end of simulation signal from server. Exit simulation.");
					}

					if (flaRea != 0){
						System.out.println("Simulator received flag  from server. Exit simulation. "+ flaRea);
					}
					/////////////////////////////////////////////////////////////
					// Check for the correct number of double values
					if (nDblRea != 37){
						System.out.println("Simulator received nDblRea = "+nDblRea+" from server, but expected 37... Exit simulation. ");
					}
	
					//			// No flags found that require the simulation to terminate.
					//			// Assign exchanged variables
					 extTemp =dblValRea[0];
					 for (int k =0; k < 12;k++){
						 temp.set(k,dblValRea[k+1]);
						 extRad.set(k,dblValRea[k +13]);
						 intLum.set(k,dblValRea[k+25]);
					 }
					
					state = new Status();
					state.setExtRadiation(extRad);
					state.setIntLum(intLum);
					state.setIntTemp(temp);
					state.setNodeAdd(getChan().getAddress());
					state.setNodeID(getNode().getID());
					
					sendUpdateToSupervisor(new A3JGMessage("update",state));

				}
				
					status = false;
				}
			}


			public void ReadConfigFile() throws FileNotFoundException{

				FileInputStream is = new FileInputStream("SeSaMe/socket.cfg");
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder;
				try {
					dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(is);		
					doc.getDocumentElement().normalize();

					NodeList nList = doc.getElementsByTagName("socket");
					port = Integer.parseInt(nList.item(0).getAttributes().item(1).getNodeValue());
					host = nList.item(0).getAttributes().item(0).getNodeValue();
					System.out.println("Port No. "+port);
					System.out.println("Host: "+host);


				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	

	
}