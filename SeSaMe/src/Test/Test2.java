package Test;

import java.util.logging.LogManager;


import A3JGroups.A3JGroup;
import A3JGroups.SSM.ComputationNode;
import A3JGroups.SSM.SSMGFollowerRole;
import A3JGroups.SSM.SSMJGSupervisorRole;
import A3JGroups.SSM.SSMNode;
import A3JGroups.SSM.TestFollowerRole;

public class Test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		A3JGroup ssmGroup = new A3JGroup(SSMJGSupervisorRole.class.getCanonicalName(),SSMGFollowerRole.class.getCanonicalName());
		
		
//		A3JGroup ssmGroup = new A3JGroup(
//				SSMJGSupervisorRole.class.getCanonicalName(),
//				new ArrayList<String>(){{add(SSMGFollowerRole.class.getCanonicalName()); add(TestFollowerRole.class.getCanonicalName());}});

//		A3JGroup ssmGroup = new A3JGroup(SSMJGSupervisorRole.class.getCanonicalName(),SSMGFollowerRole.class.getCanonicalName());
//		
//
//		LogManager.getLogManager().reset();
//		String configPath= "SSMConfig.xml";
		
//		SSMNode ssm = new SSMNode("ssm");
//		ssm.addGroupInfo("ssmGroup", ssmGroup);
//		ssm.addSupervisorRole(new SSMJGSupervisorRole(1,configPath,ssmGroup));
//		ssm.joinGroup("ssmGroup");
		

		LogManager.getLogManager().reset();
		String configPath= "SSMConfig.xml";
		
		SSMNode ssm = new SSMNode("ssm");
		ssm.addGroupInfo("ssmGroup", ssmGroup);
		ssm.addSupervisorRole(new SSMJGSupervisorRole(1,configPath,ssmGroup));
		ssm.joinGroup("ssmGroup");
		
		ComputationNode n1= new ComputationNode("1");
		n1.addGroupInfo("ssmGroup", ssmGroup);
		n1.addFollowerRole(new SSMGFollowerRole(1));
		n1.joinGroup("ssmGroup");
		
		ComputationNode n2= new ComputationNode("2");
		n2.addGroupInfo("ssmGroup", ssmGroup);
		n2.addFollowerRole(new TestFollowerRole(1));
		n2.joinGroup("ssmGroup");


	}

}
