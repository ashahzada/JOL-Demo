package Test;

import java.util.ArrayList;

import A3JGroups.A3JGroup;
import Building.FDInterface;
import Building.FDNode;
import Building.LabMgr;
import Building.LabMgrNode;
import Building.SiafuInterface;
import Building.SiafuUserInfoNode;



public class SiafuSocketTest {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) throws Exception {

		ArrayList<String> folRoles= new ArrayList<String>(2);
		folRoles.add(SiafuInterface.class.getCanonicalName());
		folRoles.add(FDInterface.class.getCanonicalName());
		
		A3JGroup LabGroup = new A3JGroup(LabMgr.class.getCanonicalName(),folRoles);
		
		LabMgrNode node = new LabMgrNode("LabMgr",0);
		node.addSupervisorRole(new LabMgr(1));
		node.addGroupInfo("LabGroup", LabGroup);
		node.joinGroup("LabGroup");
		
		SiafuUserInfoNode siafu = new SiafuUserInfoNode("Siafu");
		siafu.addFollowerRole(new SiafuInterface(1) );
		siafu.addGroupInfo("LabGroup", LabGroup);
		siafu.joinGroup("LabGroup");
		
		FDNode fd = new FDNode("Freedomotic");
		fd.addFollowerRole(new FDInterface(1) );
		fd.addGroupInfo("LabGroup", LabGroup);
		fd.joinGroup("LabGroup");
		
		
	}
}




