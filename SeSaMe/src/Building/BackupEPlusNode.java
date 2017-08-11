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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import A3JGroups.A3JGNode;

public class BackupEPlusNode extends A3JGNode {

	boolean status = true;
	Double temp;
	Double extRad;
	Double intLum; 
	int shadeVal;
	int slatAngle;
	String[] args;

	Socket socket = null;
	int port = -1;
	int verNo = 1;
	String host= null;

	public BackupEPlusNode(String obj, String[] argms) throws IOException {
		super(obj);
		shadeVal=0;
		slatAngle=45;
		args= argms;
		exchangeData();

	}




	public int getShadeVal() {
		return shadeVal;
	}




	public void setShadeVal(int shadeVal) {
		this.shadeVal = shadeVal;
	}




	public int getSlatAngle() {
		return slatAngle;
	}




	public void setSlatAngle(int slatAngle) {
		this.slatAngle = slatAngle;
	}




	public void exchangeData() throws IOException {
//
//		try {
//			Thread.sleep(1500);
//		} catch (InterruptedException e) {
//			Thread.currentThread().interrupt();
//		}
		System.out.println("In Data Exchange");
		ReadConfigFile();

		int flaWri = 0;
		int flaRea = 0;
		// Number of variables to be exchanged
		int nDblWri = 2;
		int nDblRea = 0;
		// Number of rooms
		int nRoo =2;
		// Arrays that contain the variables to be exchanged
		double[] dblValWri= new double[2];
		double[] dblValRea= new double[3];
		int i, iSte, retVal = 0;
		// set simulation time step
		double delTim;
		double endTim = Integer.parseInt(args[1])*24*3600;

		//////////////////////////////////////////////////////
		// Declare variables of the room model
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
			//			for(i=0; i < nDblWri; i++)
			//				dblValWri[i]=TRoo[i];
			dblValWri[0]=slatAngle;//45;
			dblValWri[1]= shadeVal;//6;

			/////////////////////////////////////////////////////////////
			// Exchange values

			final int nDbl=2;// = (dblValWri != null) ? dblValWri.length : 0;
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
			if (nDblRea != 3){
				System.out.println("Simulator received nDblRea = "+nDblRea+" from server, but expected 2. Exit simulation. ");
			}
			/////////////////////////////////////////////////////////////
			//			// No flags found that require the simulation to terminate.
			//			// Assign exchanged variables
			 temp = dblValRea[1];
			 extRad = dblValRea[0];
			 //intLum = dblValRea[2];
			System.out.println(temp+" "+extRad);

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


	public Double getTemp() {
		return temp;
	}




	public void setTemp(Double temp) {
		this.temp = temp;
	}




	public Double getExtRad() {
		return extRad;
	}




	public void setExtRad(Double extRad) {
		this.extRad = extRad;
	}




	public Double getIntLum() {
		return intLum;
	}




	public void setIntLum(Double intLum) {
		this.intLum = intLum;
	}




	public boolean getStatus() {
		return status;
	}




	public void setStatus(boolean status) {
		this.status = status;
	}
}