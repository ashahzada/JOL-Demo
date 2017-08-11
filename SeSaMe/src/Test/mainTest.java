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

import A3JGroups.A3JGroup;
import A3JGroups.SSM.SSMGFollowerRole;
import A3JGroups.SSM.SSMJGSupervisorRole;
import A3JGroups.SSM.SSMNode;
import A3JGroups.SSM.ComputationNode;
import Building.EPlusInterface;
import Building.EPlusNode;
import Building.FloorMgr;
import Building.FloorMgrNode;
import Building.HVACinterface;
import Building.LightInterface;
import Building.RoomLightMgr;
import Building.RoomLightReporter;
import Building.ThermalZoneMgr;
import Building.WindowInterface;

public class mainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		A3JGroup EEGroup = new A3JGroup(FloorMgr.class.getCanonicalName(),EPlusInterface.class.getCanonicalName());
		
		FloorMgrNode flrMgr = new FloorMgrNode("floor manager",1);
		flrMgr.addSupervisorRole(new FloorMgr(1));
		flrMgr.addGroupInfo("EEGroup", EEGroup);
		flrMgr.joinGroup("EEGroup");
		
		EPlusNode eplus = new EPlusNode("eplus", args);
		//eplus.addFollowerRole(new EPlusInterface(1));
		eplus.addGroupInfo("EEGroup", EEGroup);
		eplus.joinGroup("EEGroup");
	
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		
		while (eplus.getStatus())
		{
			;
		}
		
		
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
