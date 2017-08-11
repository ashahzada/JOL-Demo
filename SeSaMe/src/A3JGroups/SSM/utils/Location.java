package A3JGroups.SSM.utils;

import java.util.HashMap;
import java.util.Map;

public class Location {
	
	String name;
	String attribute;
	String type;
	String topology;
	private Map<Integer,String> parentLocs;
	private Map<Integer,String> childLocs;

	
	
	public Location() {
		super();
		parentLocs = new HashMap<Integer,String>();
		childLocs = new HashMap<Integer,String>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTopology() {
		return topology;
	}
	public void setTopology(String topology) {
		this.topology = topology;
	}
	public Map<Integer, String> getParentLocs() {
		return parentLocs;
	}
	public void setParentLocs(Map<Integer, String> parentLocs) {
		this.parentLocs = parentLocs;
	}
	public Map<Integer, String> getChildLocs() {
		return childLocs;
	}
	public void setChildLocs(Map<Integer, String> childLocs) {
		this.childLocs = childLocs;
	}

}
	
	
	