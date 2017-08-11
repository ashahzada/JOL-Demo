package A3JGroups.SSM;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import A3JGroups.A3JGMessage;
import A3JGroups.A3JGNode;
import A3JGroups.A3JGSupervisorRole;
import A3JGroups.A3JGroup;
import A3JGroups.SSM.utils.FormationCriteria;
import A3JGroups.SSM.utils.Location;
import A3JGroups.SSM.utils.Update;

public  class SSMJGSupervisorRole extends A3JGSupervisorRole {

	private Map<String,String> roleGroupsMap;
	private Map<String, String> supervisorFollowerMap;
	private Map<String, A3JGroup> listOfGroups;
	private Map<String, Location> locationMap;
	private Map<String, String> funcLocMapping;
	private String rulesFilePath;

	
	
	public SSMJGSupervisorRole(int resourceCost, String rulesFilename, A3JGroup group) {
		super(resourceCost);
		roleGroupsMap = new HashMap<String, String>();
		supervisorFollowerMap = new HashMap<String, String>();
		listOfGroups = new HashMap<String, A3JGroup>();
		locationMap = new HashMap<String, Location>();
		funcLocMapping = new HashMap<String, String>();
		rulesFilePath= rulesFilename;
		readSSMConfigFile();
	}



	@Override
	public void run() {

		while (this.active) {
//			vista = this.node.getChannels("SV").getView();
//			try {
//				Thread.sleep(2000);
//
//			} catch (Exception e) {
//				e.printStackTrace();
			}

	}

	@Override
	public void messageFromFollower(A3JGMessage msg) {

		System.out.println(this.getNode().getID()+"  has recived message from: "+((Update)msg.getContent()).getNodeId());
	}

	@Override
	public void updateFromFollower(A3JGMessage msg) {
	}

	
	public void manageNewNode(A3JGNode node)
	{
		String role = null;
		if (node.getAllSupervisorRoles().size() != 0){
			role = node.getAllSupervisorRoles().get(0).getClass().getCanonicalName();
			A3JGroup grp = listOfGroups.get((roleGroupsMap.get(role)));
			String grpName = grp.getGroupDescriptor();
			if(grp.getCriteria().getCriteria().equalsIgnoreCase("Location")){
				if(grp.getCriteria().getAttribute().equalsIgnoreCase("functional"))
					if(grp.getCriteria().getType().equalsIgnoreCase("thermal"))
						grpName = funcLocMapping.get(node.getNodeLoc().getName());//+grpName;
						else
							grpName = node.getNodeLoc().getName()+grpName;
			}
			
			//System.out.println(grpName);	
			
			try {
					node.addGroupInfo(grpName, grp);
					node.joinGroup(grpName);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		if (node.getAllFollowerRoles().size() != 0){
			role = node.getAllFollowerRoles().get(0).getClass().getCanonicalName();
			A3JGroup grp = listOfGroups.get((roleGroupsMap.get(role)));
			String grpName = grp.getGroupDescriptor();
			if(grp.getCriteria().getCriteria().equalsIgnoreCase("Location")){
				if(grp.getCriteria().getAttribute().equalsIgnoreCase("functional"))
					if(grp.getCriteria().getType().equalsIgnoreCase("thermal"))
						grpName = funcLocMapping.get(node.getNodeLoc().getName());//+grpName;
						else
							grpName = node.getNodeLoc().getName()+grpName;
			}
			
			//System.out.println(grpName);	
			
			try {
					node.addGroupInfo(grpName, grp);
					node.joinGroup(grpName);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
	
	
	public void updateSVFollowerMap(String follower, String sv){
		
		supervisorFollowerMap.put(follower, sv);
		
	}
	
	public void updateGroupMap(String sv, A3JGroup group){
		
		listOfGroups.put(sv, group);

	}
	
	public String getGroupByFol(String fol){
		String svMapping = supervisorFollowerMap.get(fol);
		return listOfGroups.get(svMapping).getGroupDescriptor();	
	}
	
	public String getGroupBySV(String sv){
		return (listOfGroups.get(sv)).getGroupDescriptor();

	}
	
	public  void readSSMConfigFile() {
		
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream is = cl.getResourceAsStream(rulesFilePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);		
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("location");
		        setLocationMap(nList);
		        
		    nList = doc.getElementsByTagName("group");
		        setGroupsInfo(nList);
		    
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public  void setLocationMap(NodeList nList) {
        for (int i = 0; i < nList.getLength(); ++i) {
    		Node nNode = nList.item(i);
    		 
    		System.out.println("\nCurrent Element :" + nNode.getNodeName());
     
    		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
    			  Location loc = new Location();
    			Element eElement = (Element) nNode;
     
    			System.out.println("Name : " + eElement.getElementsByTagName("name").item(0).getTextContent());
    			loc.setName(eElement.getElementsByTagName("name").item(0).getTextContent());
    			System.out.println("Attribute : " + eElement.getElementsByTagName("attribute").item(0).getTextContent());
    			loc.setAttribute(eElement.getElementsByTagName("attribute").item(0).getTextContent());
    			System.out.println("Type : " + eElement.getElementsByTagName("type").item(0).getTextContent());
    			loc.setType(eElement.getElementsByTagName("type").item(0).getTextContent());
    			System.out.println("Topology : " + eElement.getElementsByTagName("topology").item(0).getTextContent());
    			loc.setTopology(eElement.getElementsByTagName("topology").item(0).getTextContent());
    			
    			NodeList relatedLocs = eElement.getElementsByTagName("relatedlocation");
    			System.out.println("Total Related Locations : " + eElement.getElementsByTagName("relatedlocation").getLength());
    			int x = 0,y=0;
    			for (int j = 0; j < relatedLocs.getLength(); ++j) {
    				Element rl = (Element) relatedLocs.item(j);
    				if (rl.getElementsByTagName("relationship").item(0).getTextContent().equalsIgnoreCase("parent")){
    					loc.getParentLocs().put(x++, rl.getElementsByTagName("locationname").item(0).getTextContent());
    				}
    				else if(rl.getElementsByTagName("relationship").item(0).getTextContent().equalsIgnoreCase("contains")){
    					loc.getChildLocs().put(y++, rl.getElementsByTagName("locationname").item(0).getTextContent());
    				}
    				if(loc.getAttribute().equalsIgnoreCase("functional") && loc.getType().equalsIgnoreCase("thermal"))
    					funcLocMapping.put(rl.getElementsByTagName("locationname").item(0).getTextContent(), loc.getName());
    			}
    			
    			
    			locationMap.put(loc.getName(),loc);     
    		}
            

        }
    }

	public  void setGroupsInfo(NodeList nList) {
        for (int i = 0; i < nList.getLength(); ++i) {
    		Node nNode = nList.item(i);
    		 
    		System.out.println("\nCurrent Element :" + nNode.getNodeName());
     
    		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
    			  A3JGroup grp = new A3JGroup();
    			Element eElement = (Element) nNode;

     
    			System.out.println("Name : " + eElement.getElementsByTagName("name").item(0).getTextContent());
    			grp.setGroupDescriptor(eElement.getElementsByTagName("name").item(0).getTextContent());
    			System.out.println("Type : " + eElement.getElementsByTagName("type").item(0).getTextContent());
    			grp.setGroupType(eElement.getElementsByTagName("type").item(0).getTextContent());
    			System.out.println("SVRole : " + eElement.getElementsByTagName("SVRole").item(0).getTextContent());
    			grp.addSupervisor(0,eElement.getElementsByTagName("SVRole").item(0).getTextContent());
    			roleGroupsMap.put(eElement.getElementsByTagName("SVRole").item(0).getTextContent(), grp.getGroupDescriptor());
    			
    			NodeList folRoles = eElement.getElementsByTagName("FollowerRole");
    			System.out.println("Total Follower Roles : " + eElement.getElementsByTagName("FollowerRole").getLength());
    			for (int j = 0; j < folRoles.getLength(); ++j) {
    				grp.addFollower(j, eElement.getElementsByTagName("FollowerRole").item(j).getTextContent());    			
    				roleGroupsMap.put(eElement.getElementsByTagName("FollowerRole").item(j).getTextContent(), grp.getGroupDescriptor());
    			}
    			
    			Element criteria = (Element) eElement.getElementsByTagName("FormationCriteria").item(0);
    			FormationCriteria fc = new FormationCriteria();
    			fc.setCriteria(criteria.getElementsByTagName("criteria").item(0).getTextContent());
    			fc.setAttribute(criteria.getElementsByTagName("attribute").item(0).getTextContent());
    			fc.setType(criteria.getElementsByTagName("type").item(0).getTextContent());
    		    			
    			System.out.println("Criteria : " + fc.getCriteria()+ " "+fc.getAttribute()+" "+fc.getType());
    			grp.setCriteria(fc);
    			listOfGroups.put(grp.getGroupDescriptor(), grp);
    		}
            

        }
    }

	@Override
	public int fitnessFunc() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
