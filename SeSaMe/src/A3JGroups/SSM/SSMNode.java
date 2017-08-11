package A3JGroups.SSM;


import java.util.logging.LogManager;

import A3JGroups.A3JGNode;
import A3JGroups.A3JGroup;
import A3JGroups.SSM.SSMJGSupervisorRole;

public class SSMNode extends A3JGNode{

	A3JGroup groupInfo;

	
	public SSMNode(String name){
		super(name);
		A3JGroup ssmGroup = new A3JGroup(SSMJGSupervisorRole.class.getCanonicalName(),SSMGFollowerRole.class.getCanonicalName());
		LogManager.getLogManager().reset();
		String configPath= "A3JGroups/SSM/SSMConfig.xml";
		this.addGroupInfo("ssmGroup", ssmGroup);
		this.addSupervisorRole(new SSMJGSupervisorRole(1,configPath,ssmGroup));		
		try {
			this.joinGroup("ssmGroup");
		} 
		
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void listenGroupInfo()
	{


	
	}
	
	
	
	public void connect(A3JGNode node){
	
		SSMJGSupervisorRole role = (SSMJGSupervisorRole) this.getSupervisorRole("A3JGroups.SSM.SSMJGSupervisorRole");
		role.manageNewNode(node);
	}
	
	public void registerGroup(A3JGroup group ){
		
		((SSMJGSupervisorRole) this.getSupervisorRole("A3JGroups.SSM.SSMJGSupervisorRole")).updateSVFollowerMap(group.getFollower().get(0),group.getSupervisor().get(0));
		((SSMJGSupervisorRole) this.getSupervisorRole("A3JGroups.SSM.SSMJGSupervisorRole")).updateGroupMap(group.getSupervisor().get(0), group);
	}
	
	public String getGroupDescforFollower(String svRole){
		return ((SSMJGSupervisorRole) this.getSupervisorRole("A3JGroups.SSM.SSMJGSupervisorRole")).getGroupByFol(svRole);
	}
	
	public String getGroupDescforSupervisor(String svRole){
		return ((SSMJGSupervisorRole) this.getSupervisorRole("A3JGroups.SSM.SSMJGSupervisorRole")).getGroupBySV(svRole);
	}
	
}
