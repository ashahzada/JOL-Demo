package A3JGroups;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import A3JGroups.SSM.utils.FormationCriteria;


/**
 * A3JGroup is used to define the information relating to a group of A3. For each 
 * group, you must create an instance of A3JGroup, with the respective information.
 * 
 * @author bett.marco88@gmail.com
 *
 */
public class A3JGroup {
	
	private Map<Integer, String> supervisor = new HashMap<Integer, String>();
	private Map<Integer, String> followers = new HashMap<Integer, String>();
	private String groupConnection;
	private String groupDescriptor;
	private String groupType;
	private FormationCriteria criteria;
	
	
	
	/**
	 * THe constructor has as input the class name of the JGSupervisorRole and 
	 * JGFollowerRole used as default roles of the group.
	 * 
	 * @param supervisorDef
	 * 			JGSupervisorRole default class name.
	 * @param followerDef
	 * 			JGFollowerRole default class name.
	 */
	public A3JGroup() {
		super();
		criteria = new FormationCriteria();
	}
	public A3JGroup(String supervisorDef, String followerDef) {
		super();
		supervisor.put(0, supervisorDef);
		followers.put(0, followerDef);
		criteria = new FormationCriteria();
	}
	public A3JGroup(String supervisorDef, ArrayList<String> followersDef) {
		super();
		supervisor.put(0, supervisorDef);
		for(int i=0; i<followersDef.size();i++)
			followers.put(i, followersDef.get(i));
		criteria = new FormationCriteria();

	}

	public Map<Integer, String> getSupervisor() {
		return supervisor;
	}
	
	public Map<Integer, String> getFollower() {
		return followers;
	}
	
	public void addSupervisor(int index, String className){
		supervisor.put(index, className);
	}
	
	public void addFollower(int index, String className){
		followers.put(index, className);
	}

	public void addGroupConnection(String pathConfig){
		groupConnection = pathConfig;
	}
	
	public String getGroupConnection(){
		return groupConnection;
	}
	
	public String getGroupDescriptor() {
		return groupDescriptor;
	}

	public void setGroupDescriptor(String groupDescriptor) {
		this.groupDescriptor = groupDescriptor;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public FormationCriteria getCriteria() {
		return criteria;
	}
	public void setCriteria(FormationCriteria criteria) {
		this.criteria = criteria;
	}
		
}
