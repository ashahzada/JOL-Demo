package Building;

import java.io.IOException;

import A3JGroups.A3JGNode;
import A3JGroups.SSM.utils.Status;

public class FloorMgrNode extends A3JGNode {

	boolean status = true;
	Status state;
	


	public FloorMgrNode(String obj, int limit) {

		super(obj);
		state = new Status();
		
		// TODO Auto-generated constructor stub
	}
	
	public boolean getStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
	
	
}
