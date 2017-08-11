package it.telecomitalia.scube.a3;

import it.freedomotic.api.EventTemplate;
import it.freedomotic.api.Protocol;
import it.freedomotic.app.Freedomotic;
import it.freedomotic.environment.EnvironmentLogic;
import it.freedomotic.environment.EnvironmentPersistence;
import it.freedomotic.environment.ZoneLogic;
import it.freedomotic.events.ObjectHasChangedBehavior;
import it.freedomotic.events.ZoneHasChanged;
import it.freedomotic.exceptions.UnableToExecuteException;
import it.freedomotic.model.environment.Environment;
import it.freedomotic.model.environment.Zone;
import it.freedomotic.model.object.EnvObject;
import it.freedomotic.reactions.Command;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;




public class A3 extends Protocol
{

	private static int POLLING_TIME = 1000;
	Map<String,LightNode> lights = new HashMap<String,LightNode>();
	Map<String,Boolean> lightsStatusToSend = new HashMap<String,Boolean>();
	Map<String,Boolean> lightsStatusToRecv = new HashMap<String,Boolean>();

	ExchangeData excData = null;
	Socket socket = null;
	int port = 1234;
	int verNo = 1;
	//String host= "Adnans-MacBook-Air.local";
	String host= "Adnans-Mac";



	public A3()
	{
		super("A3", "/it.telecomitalia.a3/a3-manifest.xml");

		//        host = configuration.getProperty("host");
		//        port = Integer.valueOf(Integer.parseInt(configuration.getProperty("port")));
		//
	}

	protected void onShowGui()
	{
	}

	@SuppressWarnings("deprecation")
	public void onStart()
	{
		super.onStart();
		setPollingWait(POLLING_TIME);

		addEventListener("app.event.sensor.object.behavior.change");
		addEventListener("app.event.sensor.environment.zone.change");
		addEventListener("app.event.sensor.person.movement.detected");
		addEventListener("app.events.sensors.moving.person.*");
		addEventListener("app.events.sensors.person.zone");

		host= configuration.getStringProperty("hostName", "Adnans-Mac.homenet.telecomitalia.it");
		port= configuration.getIntProperty("portNo", 1234);

		setDescription((new StringBuilder()).append("Connected to A3").toString());
//		Environment env = EnvironmentPersistence.getEnvironments().get(0).getPojo();
//		ArrayList<Zone> zones = env.getZones();
		for (EnvironmentLogic env : EnvironmentPersistence.getEnvironments()) {
		       for (ZoneLogic z : env.getZones()) {
		//for (int i= 0; i < zones.size(); i++) {
			Freedomotic.logger.info("Entering zone: " + z.getPojo().getName());
			String zone = z.getPojo().getName();
			ArrayList<EnvObject> objs = z.getPojo().getObjects();
			for (int j=0; j < objs.size(); j++) {
				Freedomotic.logger.info("Found object: " + objs.get(j).getType());
				if (objs.get(j).getType().equalsIgnoreCase("EnvObject.ElectricDevice.Light"))
				{
					LightNode newLight = new LightNode(objs.get(j));
					newLight.setLocation(zone);
					//if(lights.get(newLight.getID()) == null)
					lights.put(newLight.getObj().getName(), newLight); 
					lightsStatusToSend.put(newLight.getObj().getName(), false); 
					lightsStatusToRecv.put(newLight.getObj().getName(), false);
					Freedomotic.logger.info("LightId "+objs.get(j).getName()+" Number of Lights: "+lights.size());
				}

			}
		}}

		System.out.println("Total Number of Lights: "+lights.size());
		startA3();

		
	}

	public void onStop()
	{
	   
	        super.onStop();
	        //release resources
	        excData.interrupt();
	        excData = null;
	        setPollingWait(-1); //disable polling
	        //display the default description
	        setDescription(configuration.getStringProperty("description", "A3  is stopped"));
	
	}

	public void onCommand(Command c)
			throws IOException, UnableToExecuteException
			{
			}

	public boolean canExecute(Command c)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	protected void onRun()
	{
		
	}

	protected void startA3(){
		System.out.println("STARTING ......");
	//	synchronize();

		excData = new ExchangeData(this);
		excData.start();
//		exchangeData();



	}

	

	protected void onEvent(EventTemplate event)
	{
		System.out.println("Event Capture is instance of "+ event.getClass()); 
		if (isRunning()) {
			System.out.println("isRunning()-Event Capture and is instance of "+ event.getClass()); 
			if (event instanceof ObjectHasChangedBehavior) {
				//ObjectHasChangedBehavior objEvent= (ObjectHasChangedBehavior) event;
				event.getProperty("object");
				System.out.println("------Event name: "+event.getEventName()+ "...Event object "+ event.getProperty("object.name"));

			} else {
				if (event instanceof ZoneHasChanged) {
					System.out.println("------Event name: "+event.getEventName()+ "...Event object "+ event.getProperty("sender"));

				} else {
					System.out.println("------Event name: "+event.getEventName()+ "...Event object "+ event.getProperty("sender"));

				}
			}
		}
	}



	
}
