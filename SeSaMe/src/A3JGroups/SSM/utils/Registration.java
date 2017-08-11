package A3JGroups.SSM.utils;

import java.io.Serializable;

import org.jgroups.Address;

@SuppressWarnings("serial")
public class Registration implements Serializable {

	private String nodeID;
	private Address src;
	private Boolean status;

	public Registration(String nodeID, Address src, Boolean status) {
		this.nodeID = nodeID;
		this.src = src;
		this.status = status;
	}

	public String getNodeID() {
		return nodeID;
	}

	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}

	public Address getSrc() {
		return src;
	}

	public void setSrc(Address src) {
		this.src = src;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}
