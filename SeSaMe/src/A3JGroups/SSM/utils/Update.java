package A3JGroups.SSM.utils;

import java.io.Serializable;
import java.util.ArrayList;

import org.jgroups.Address;

@SuppressWarnings("serial")
public class Update implements Serializable {

	private Address address;
	private String nodeId;
	ArrayList<Double> angle;
	private int shadeVal;

	public ArrayList<Double> getAngle() {
		return angle;
	}

	public void setAngle(ArrayList<Double> angle) {
		this.angle = angle;
	}

	public int getShadeVal() {
		return shadeVal;
	}

	public void setShadeVal(int shadeVal) {
		this.shadeVal = shadeVal;
	}

	public Update(Address address, String id) {
		this.address = address;
		this.nodeId = id;
	}

	public Update() {
		// TODO Auto-generated constructor stub
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String id) {
		this.nodeId = id;
	}

}
