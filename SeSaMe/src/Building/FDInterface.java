package Building;



import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import A3JGroups.A3JGFollowerRole;
import A3JGroups.A3JGMessage;


public  class FDInterface extends A3JGFollowerRole   {


	ServerSocket socket = null;
	Socket cliSoc;
	int port = 1234;
	int verNo = 1;
	String host= "Adnans-MacBook-Pro-2.local";
	ArrayList<Boolean> deskStatusRead;
	public boolean status = false;
	private Map<String,LampNode> lights = new HashMap<String,LampNode>();
	private ArrayList<Boolean> lightsStatusToSend;// = new ArrayList<Boolean>();


	public FDInterface(int resourceCost) {
		super(resourceCost);
		lightsStatusToSend= new ArrayList<Boolean>(Collections.nCopies(6,false));
	}



	@Override
	public void run() {

		try {
		//	sendMessageToSupervisor(new A3JGMessage("status","Hello"));

			exchangeData();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while(active){
		
		}}




	public void exchangeData() throws IOException {
		System.out.println("In Data Exchange");
		int flaWri = 0;
		int flaRea = 0;
		// Number of variables to be exchanged
		int nblWri = 6;
		int nblRea = 6;

		// Arrays that contain the variables to be exchanged
		boolean[] blValWri= new boolean[nblWri];
		boolean[] blValRea= new boolean[nblWri];
		int i, iSte, retVal = 0;
		// set simulation time step
		double delTim;
		double endTim = 36000;//Integer.parseInt(args[1])*24*3600;

		//////////////////////////////////////////////////////
		// Declare variables of model
		double simTimWri = 0;
		double simTimRea = 0;
		int nSte;

		delTim = 60; //Integer.parseInt(args[0]);
		System.out.println("Simulation model has time step   "+ delTim);
		System.out.println("Simulation model has end time   "+endTim);

		if (delTim <= 0){
			System.out.println("Error: End time must be bigger than zero.");
			System.out.println("Usage: %s simulation_timestep_in_seconds end_time_in_seconds "+ delTim);
			return;
		}

		nSte = (int) Math.floor((endTim/delTim) + 0.5); // added by TNouidui. nearbyint not defined on Windows.
		System.out.println(" Number of time steps: "+ nSte);
		if (Math.abs(nSte*delTim-endTim) > 1E-10*endTim){
			System.out.println("Error: End time divided by time step must be an integer.");
			System.out.println("       Number of time steps is. "+ nSte);
			System.out.println("Usage:  simulation_timestep_in_seconds end_time_in_seconds "+ delTim);
			return;
		}

		/////////////////////////////////////////////////////////////
		// Establish the client socket
		boolean conn = false;
		try {
			socket = new ServerSocket(port);

			conn = true;
			System.out.println("Socket InetAddress: "+socket.getInetAddress());
			System.out.println("Socket Local Port: "+socket.getLocalPort());



		} catch (UnknownHostException e) {
			System.err.println("Can't find: " + delTim);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("IO error..");
			exchangeData();
			//System.exit(1);
		}


		if(conn){
			/////////////////////////////////////////////////////////////
			// Simulation loop
			
			cliSoc = socket.accept();
			
			for(iSte=0; ; iSte++){
			//	System.out.println("In simulation Loop");
				// Set simulation time
				simTimWri = (double)iSte*delTim; // set simulation time
				/////////////////////////////////////////////////////////////
				
				ArrayList<String> keys = new ArrayList<String>(lights.keySet());
				// assign values to be exchanged
				for(i=0; i < nblWri; i++)
				{
					if(iSte%2== 0)
						status = true;
						else status =false;
					blValWri[i]=lightsStatusToSend.get(i);
				}



				/////////////////////////////////////////////////////////////
				// Exchange values

				final int nBool= (blValWri != null) ? blValWri.length : 0;
				StringBuffer strBuf = new StringBuffer(Integer.toString(verNo));
				strBuf.append(" " + Integer.toString(flaWri)); // the communication flag
				strBuf.append(" " + Integer.toString(nBool)); // then number of booleans
				strBuf.append(" 0 0 "); // the number of integers and doubles
				strBuf.append(simTimWri); // the current simulation time
				strBuf.append(" ");
				for (int k = 0; k < nBool; k++) {
					strBuf.append(String.valueOf(blValWri[k]));
					strBuf.append(" ");
				}
				// add line termination for parsing in client
				strBuf.append("\n");

				BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(cliSoc.getOutputStream()));
				wr.write(new String(strBuf));
				wr.flush();

				final InputStreamReader inpStrRea = new InputStreamReader(cliSoc.getInputStream());
				final BufferedReader d = new BufferedReader(inpStrRea);
				final String line = d.readLine();
				//System.out.println("From Server "+line);
				String[] ele = line.split(" ");
				Integer.parseInt(ele[0]);
				flaRea = Integer.parseInt(ele[1]); // the communication flag
				if (flaRea == 0) { // read further if flag is nonzero
					nblRea = Integer.parseInt(ele[2]);
					Integer.parseInt(ele[3]);
					Integer.parseInt(ele[4]);
					simTimRea = Double.parseDouble(ele[5]);
					blValRea = new boolean[nblRea];
					// check sufficient array lenght
					if (nblRea != blValRea.length) {
						throw new IOException("Received " + nblRea
								+ " booleans, but expected " + blValRea.length
								+ " elements.");
					}
					for (int j = 0; j < nblRea; j++) {
						blValRea[j] = Boolean.getBoolean(ele[6 + j]);
						
					}
				}

//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					Thread.currentThread().interrupt();
//				}
			}}
		}


	@SuppressWarnings("unchecked")
	@Override
	public void messageFromSupervisor(A3JGMessage msg) {
		ArrayList<Boolean> tempStatus= ((ArrayList<Boolean>)msg.getContent());
		
		lightsStatusToSend.set(0, tempStatus.get(0));
		lightsStatusToSend.set(1, tempStatus.get(1));
		lightsStatusToSend.set(2, tempStatus.get(2));
		lightsStatusToSend.set(3, tempStatus.get(3));
		lightsStatusToSend.set(4, tempStatus.get(4));
		lightsStatusToSend.set(5, tempStatus.get(5));
		
		System.out.println("Sending these values to Freedomotic: ");
		for(int i=0;i<6;i++)
			System.out.println(lightsStatusToSend.get(i));
			
		
	//	lightsStatusToSend = ((ArrayList<Boolean>)msg.getContent());

		
	}



}