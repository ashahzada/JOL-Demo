package it.telecomitalia.scube.a3;

import it.freedomotic.app.Freedomotic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

public class ExchangeData extends Thread {
		   

	 A3 plugin; // reference to the plugin class
	 Socket socket;
	 boolean run =true;
	    
	 public ExchangeData(A3 plugin) {
	        this.plugin = plugin;
	        try {
				socket = new Socket(plugin.host, plugin.port);
				System.out.println("Socket InetAddress: "+socket.getInetAddress());
				System.out.println("Socket Local Port: "+socket.getLocalPort());
				System.out.println("Socket Remote Port: "+socket.getPort());


			} catch (UnknownHostException e) {
				System.err.println("Can't find host: " +e);
				System.exit(1);
			} catch (IOException e) {
				System.err.println("IO error..");
				//System.exit(1);
			}
	    }

	    @SuppressWarnings("deprecation")
		public void run() {
	        while (run) {
	            try {
	    
					exchangeData();

	                
	            } catch (Exception ex) {
	                System.out.println("Reading data exception " + ex);
	                run = false;
	    	        this.interrupt();
	                plugin.setPollingWait(-1);
	                //System.exit(1);
	            }
	        }
	    }

	  
	  public void exchangeData() throws IOException {
		//	System.out.println("In Data Exchange");
			int flaWri = 0;
			int flaRea = 0;
			// Number of variables to be exchanged
			int nblWri = plugin.lights.size();
			int nblRea = 5;

			// Arrays that contain the variables to be exchanged
			boolean[] blValWri= new boolean[nblWri];
			boolean[] blValRea= new boolean[nblWri];
			int i;
			// set simulation time step
			double delTim;
			double endTim = 36000;//Integer.parseInt(args[1])*24*3600;

			//////////////////////////////////////////////////////
			// Declare variables of model
			double simTimWri = 0;
			double simTimRea = 0;
			int nSte;

			
				/////////////////////////////////////////////////////////////
				// Simulation loop
//				for(iSte=0; ; iSte++){
//					//for(iSte=0; iSte <= nSte+1; iSte++){
//					System.out.println("In simulation Loop");
//					// Set simulation time
//					simTimWri = (double)iSte*delTim; // set simulation time
					/////////////////////////////////////////////////////////////
					// assign values to be exchanged
					ArrayList<String> keys = new ArrayList<String>(plugin.lights.keySet());
					for(i=0; i < nblWri; i++)
						blValWri[i]= plugin.lightsStatusToSend.get(keys.get(i));



					/////////////////////////////////////////////////////////////
					// Exchange values

					final int nBool= (blValWri != null) ? blValWri.length : 0;
					StringBuffer strBuf = new StringBuffer(Integer.toString(1));
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

					BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					wr.write(new String(strBuf));
					wr.flush();

					final InputStreamReader inpStrRea = new InputStreamReader(socket.getInputStream());
					final BufferedReader d = new BufferedReader(inpStrRea);
					final String line = d.readLine();
					
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
						
						keys = new ArrayList<String>(plugin.lights.keySet());
						for (int j = 0; j < nblRea; j++) {
						//	System.out.println("Parsing "+ele[6 + j]+ " to "+Boolean.parseBoolean(ele[6 + j]));
							blValRea[j] = Boolean.parseBoolean(ele[6 + j]);
							plugin.lightsStatusToRecv.put(plugin.configuration.getStringProperty(String.valueOf(j+1),String.valueOf(j+1)), blValRea[j]);
						//	System.out.println("Setting "+plugin.lights.get(keys.get(j)).getID()+ " to "+blValRea[j]);
						}
					}

					synchronize();
					
//					try {
//						Thread.sleep(5000);
//					} catch (InterruptedException e) {
//						Thread.currentThread().interrupt();
//					}
					
				}
			
		
		
		protected void synchronize(){
			boolean status = false;
			
			for (int i =1; i<= plugin.lights.size();i++){
				String key = plugin.configuration.getStringProperty(String.valueOf(i),String.valueOf(i));
//			for (Iterator<String> it = plugin.lights.keySet().iterator(); it.hasNext();) {
//				String key = it.next();
				LightNode light = plugin.lights.get(key);
				status = plugin.lightsStatusToRecv.get(key);

					
				if( status != light.getStatus()){
				light.getCmdPwr() .setProperty("value", String.valueOf(status));
				light.getCmdPwr() .setProperty("object", light.getID());
				Freedomotic.sendCommand(light.getCmdPwr());
				light.setStatus(status);
				System.out.println("Command sent and status changed !!" );

				//System.out.println("Asking "+light.getID()+ " to "+status);
				}

			}
			
			for (Iterator<String> it = plugin.lights.keySet().iterator(); it.hasNext();) {
				String key = it.next();
				LightNode light = plugin.lights.get(key);
				status = light.getPower();
				plugin.lightsStatusToSend.put(light.getID(),status);
			}

		}


}
