package Building;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import A3JGroups.A3JGNode;
import A3JGroups.SSM.utils.Status;

import java.net.ServerSocket;


public class FDLightMgrNode extends A3JGNode {


	int totLights;
	A3JGroups.SSM.utils.Status state;
	String lastTurnedOnLight;

	private Map<String,LampNode> lights = new HashMap<String,LampNode>();
	private Map<String,Boolean> lightsStatusToSend = new HashMap<String,Boolean>();
	private Map<String,Boolean> lightsStatusToRecv = new HashMap<String,Boolean>();

	ServerSocket socket = null;
	Socket cliSoc;
	int port = 1234;
	int verNo = 1;
	String host= "Adnans-MacBook-Pro-2.local";



	public FDLightMgrNode(String obj) {
		super(obj);
		totLights = 0;
		setTakeAction(false);
		setStateChanged(false);
		//	state = new A3JGroups.SSM.utils.Status(getID(),totLights,0);
		lastTurnedOnLight = "";
		try {
			exchangeData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		// TODO Auto-generated constructor stub
	}

	public int getTotalLightsLimit()
	{
		return totLights;
	}

	public void updateState(int onLgts){
		//	state.setTotalLights(totLights);
		//	state.setStatus(onLgts);
		setStateChanged(true);
	}

	public Boolean IsStateChanged() {
		return stateChanged;
	}

	public void setStateChanged(Boolean stateChanged) {
		this.stateChanged = stateChanged;
	}

	public Boolean IsActionRequired() {
		return takeAction;
	}

	public void setTakeAction(Boolean takeAction) {
		this.takeAction = takeAction;
	}

	public String getLastTurnedOnLight() {
		return lastTurnedOnLight;
	}

	public void setLastTurnedOnLight(String lastTurnedOnLight) {
		this.lastTurnedOnLight = lastTurnedOnLight;
	}

	public Status getState() {
		return state;
	}

	public void setState(Status state) {
		this.state = state;
	}

	private Boolean takeAction;
	private Boolean stateChanged;


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
			
			for(iSte=0; iSte <= nSte+1; iSte++){
				System.out.println("In simulation Loop");
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
					blValWri[i]=status;
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
				System.out.println("From Server "+line);
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
						if(iSte==0 || iSte%100 == 0)
							System.out.println("Parameter "+j+" "+blValRea[j]);
					}
				}

				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}}
		}
		
		

	}
