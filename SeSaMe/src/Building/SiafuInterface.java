package Building;



import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;

import A3JGroups.A3JGFollowerRole;
import A3JGroups.A3JGMessage;


public  class SiafuInterface extends A3JGFollowerRole   {


	ServerSocket socket = null;
	Socket cliSoc;
	int port = 1235;
	int verNo = 1;
	String host= "Adnans-MacBook-Pro-2.local";
	ArrayList<Boolean> deskStatusRead;


	public SiafuInterface(int resourceCost) {
		super(resourceCost);
		
	}



	@Override
	public void run() {

		try {
			sendMessageToSupervisor(new A3JGMessage("status","Hello"));

			exchangeData();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while(active){
		
		}}




	public void exchangeData() throws IOException {
		System.out.println("In Data Exchange");
		int flaRea = 0;
		// Number of variables to be exchanged
		int nblRea = 0;

		// Arrays that contain the variables to be exchanged

		
		int i, iSte, retVal = 0;
		// set simulation time step
		double delTim;
		double endTim = 3600000;//Integer.parseInt(args[1])*24*3600;

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
			System.out.println("Socket Address: "+socket.getLocalSocketAddress());
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
			InputStreamReader inpStrRea ;
			BufferedReader d;

			for(iSte=0; ; iSte++){
				//System.out.println("In simulation Loop");
				// Set simulation time
				simTimWri = (double)iSte*delTim; // set simulation time

				/////////////////////////////////////////////////////////////
				// Exchange values


				inpStrRea = new InputStreamReader(cliSoc.getInputStream());
				d = new BufferedReader(inpStrRea);
				final String line = d.readLine();
				System.out.println("From Client "+line);
				String[] ele = line.split(" ");
				if(!(ele.length<27)){
				Integer.parseInt(ele[0]);
				flaRea = Integer.parseInt(ele[1]); // the communication flag
				if (flaRea == 0) { // read further if flag is nonzero
					nblRea = Integer.parseInt(ele[2]);
					Integer.parseInt(ele[3]);
					Integer.parseInt(ele[4]);
					simTimRea = Double.parseDouble(ele[5]);
					deskStatusRead= new ArrayList<Boolean>(Collections.nCopies(nblRea,false));
					//deskStatusRead = new boolean[nblRea];
					// check sufficient array lenght
					if (nblRea != deskStatusRead.size()) {
						throw new IOException("Received " + nblRea
								+ " booleans, but expected " + deskStatusRead.size()
								+ " elements.");
					}
					for (int j = 0; j < nblRea; j++) {
						deskStatusRead.set(j, Boolean.parseBoolean(ele[6 + j])) ;
						if(iSte==0 || iSte%5 == 0)
							System.out.println("Parameter "+j+" "+deskStatusRead.get(j));
					}
					
					sendMessageToSupervisor(new A3JGMessage("status",deskStatusRead));
					
				}}


			}}
	}



	public void close() throws IOException {
		if (cliSoc != null) {
			cliSoc.close();
		}
		if (socket != null) {
			socket.close();
		}
	}



	@Override
	public void messageFromSupervisor(A3JGMessage msg) {
		//System.out.println("Message from SV");

		
	}



}