package A3JGroups.SSM.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import org.jgroups.Address;

@SuppressWarnings("serial")
public class Status implements Serializable {

	private String nodeID;
	ArrayList<Double> temp;
	ArrayList<Double> extRad;
	ArrayList<Double> intLum; 
	private Address nodeAdd;

	public Address getNodeAdd() {
		return nodeAdd;
	}

	public void setNodeAdd(Address nodeAdd) {
		this.nodeAdd = nodeAdd;
	}

	public Status(String nodeID, ArrayList<Double> rad, ArrayList<Double> lum, ArrayList<Double> tempr, Address add) {
		this.nodeID = nodeID;
		this.extRad = rad;
		this.intLum = lum;
		this.temp= tempr;
	}

	public Status() {
		temp= new ArrayList<Double>(Collections.nCopies(12,0.0));
		extRad= new ArrayList<Double>(Collections.nCopies(12,0.0));
		intLum= new ArrayList<Double>(Collections.nCopies(12,0.0));
		// TODO Auto-generated constructor stub
	}

	public String getNodeID() {
		return nodeID;
	}

	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}

	public ArrayList<Double> getExtRadiation() {
		return extRad;
	}

	public void setExtRadiation(ArrayList<Double> extRadiation) {
		this.extRad = extRadiation;
	}

	public ArrayList<Double> getIntLum() {
		return intLum;
	}

	public void setIntLum(ArrayList<Double> intLum) {
		this.intLum = intLum;
	}

	public ArrayList<Double> getIntTemp() {
		return temp;
	}

	public void setIntTemp(ArrayList<Double> intTemp) {
		this.temp = intTemp;
	}

	

}
