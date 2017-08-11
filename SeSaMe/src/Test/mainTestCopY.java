package Test;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import A3JGroups.SSM.SSMNode;
import A3JGroups.SSM.ComputationNode;
import Building.HVACinterface;
import Building.LightInterface;
import Building.RoomLightMgr;
import Building.RoomLightReporter;
import Building.ThermalZoneMgr;
import Building.WindowInterface;

public class mainTestCopY {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		Socket socket = null;
		int port = -1;
		int verNo = 1;
		String host= null;
		int shadeVal=0;
		int slatAngle=45;
		
		
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
		double TIni   = 10;
		double tau    = 2*3600;
		double Q0Hea  = 100;
		double UA     = Q0Hea / 20;
		double TOut   = 5;
		double C[]    = {tau*UA, 2*tau*UA};
		double TRoo[] = {TIni, TIni};

		double y[]    = {0, 0};
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
		try {
			socket = new Socket(host, port);
			System.out.println("Socket InetAddress: "+socket.getInetAddress());
			System.out.println("Socket Local Port: "+socket.getLocalPort());
			System.out.println("Socket Remote Port: "+socket.getPort());


		} catch (UnknownHostException e) {
			System.err.println("Can't find: " + args[0]);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("IO error");
			System.exit(1);
		}



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
			if (nDblRea != 2){
				System.out.println("Simulator received nDblRea = "+nDblRea+" from server, but expected 2. Exit simulation. ");
			}
			/////////////////////////////////////////////////////////////
//			// No flags found that require the simulation to terminate.
//			// Assign exchanged variables
			 Double temp = dblValRea[1];
			 Double extRad = dblValRea[0];
			 Double intLum = dblValRea[2];
			 System.out.println(temp+" "+extRad);
			 
			 
			 if( temp<23 && intLum<500 && slatAngle<85 && extRad > 5){
				 slatAngle+=5;
				 shadeVal = 0;
				 }
			 else if (intLum >500 && slatAngle>5)
				 slatAngle-=5;
			 
			 else if (temp>23 && extRad >300 && slatAngle>5)
				 slatAngle-=5;
//			 
//			 else if (extRad < 5)
//				 shadeVal = 6;
			 
			 

		
//
//			/////////////////////////////////////////////////////////////
//			// Having obtained y_k, we compute the new state x_k+1 = f(y_k)
//			// This is the actual simulation of the client.
//			for(i=0; i < nRoo; i++)
//				TRoo[i] = TRoo[i] + delTim/C[i] * ( UA * (TOut-TRoo[i] ) + Q0Hea * y[i] );
		} // end of simulation loop
		/////////////////////////
		// Close socket at end of simulation
		//sendclientmessage(&sockfd, &stoFla);
		//closeipc(&sockfd);
		socket.close();

//		
//		File ipFile = new File("/Applications/bcvtb/examples/SeSaMe-actuator/simInput.txt");
//		 FileReader reader = new FileReader(ipFile);
//		 BufferedReader in = new BufferedReader(reader);
//		 String s =in.readLine();
		
//		 
//		 File opFile = new File("/Applications/bcvtb/examples/SeSaMe-actuator/output.txt");
//		  FileWriter fooWriter = new FileWriter(opFile, false); // true to append
//		  fooWriter.write(Integer.toString(val));
//		  fooWriter.close();	
		
//		SSMNode ssm = new SSMNode("ssm");
//
//		ComputationNode n5= new ComputationNode("ZoneMgr");
//		n5.getNodeLoc().setName("Library");
//		n5.addSupervisorRole(new ThermalZoneMgr(1));
//		ssm.connect(n5);
//		
//		ComputationNode n6= new ComputationNode("Window");
//		n6.getNodeLoc().setName("Library");
//		n6.addFollowerRole(new WindowInterface(1));
//		ssm.connect(n6);
//		
//		ComputationNode n7= new ComputationNode("HVAC");
//		n7.getNodeLoc().setName("Library");
//		n7.addFollowerRole(new HVACinterface(1));
//		ssm.connect(n7);
//		
//		ComputationNode n1= new ComputationNode("LibraryLightMgr");
//		n1.getNodeLoc().setName("Library");
//		n1.addSupervisorRole(new RoomLightMgr(1));
//		n1.addFollowerRole(new RoomLightReporter(1));
//		ssm.connect(n1);
//		
//		ComputationNode n2= new ComputationNode("LibraryLight");
//		n2.getNodeLoc().setName("Library");
//		n2.addFollowerRole(new LightInterface(1));
//		ssm.connect(n2);
//		
//		ComputationNode n3= new ComputationNode("DRLightMgr");
//		n3.getNodeLoc().setName("DiscussionRoom");
//		n3.addFollowerRole(new RoomLightReporter(1));
//		n3.addSupervisorRole(new RoomLightMgr(1));
//		ssm.connect(n3);
//		
//		ComputationNode n4= new ComputationNode("DRLight");
//		n4.getNodeLoc().setName("DiscussionRoom");
//		n4.addFollowerRole(new LightInterface(1));
//		ssm.connect(n4);
//		
		



	}

}
