package Building;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jgroups.Address;

import A3JGroups.A3JGMessage;
import A3JGroups.A3JGSupervisorRole;
import A3JGroups.SSM.utils.Status;
import A3JGroups.SSM.utils.Update;

public  class LabMgr extends A3JGSupervisorRole {

	private ArrayList<Boolean> deskStatus; 
	private ArrayList<Boolean> lightsStatus; 

	public LabMgr(int resourceCost) {
		super(resourceCost);
		deskStatus= new ArrayList<Boolean>(Collections.nCopies(10,false));
		lightsStatus= new ArrayList<Boolean>(Collections.nCopies(6,false));
	}



	@Override
	public void run() {

		while (this.active) {

			}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void messageFromFollower(A3JGMessage msg) {

	//	System.out.println(this.getNode().getID()+"  has recived message from: ");//+((Status)msg.getContent()).getNodeID());
		deskStatus = ((ArrayList<Boolean>) msg.getContent());
		
		//demo area
		if(deskStatus.get(0))
			lightsStatus.set(0, true);
		else
			lightsStatus.set(0, false);
		
		//Meeting Room
		if(deskStatus.get(2))
			lightsStatus.set(1, true);
		else
			lightsStatus.set(1, false);
		
		//Living room
		if(deskStatus.get(1))
			lightsStatus.set(2, true);
		else
			lightsStatus.set(2, false);
		
		//Emergency door
			lightsStatus.set(3, false);
		
		
		//Kitchen
				if(deskStatus.get(1) )
					lightsStatus.set(4, true);
				else
					lightsStatus.set(4, false);
		
		//Entrance
		
		if(deskStatus.get(9) || deskStatus.get(11) || deskStatus.get(10) || deskStatus.get(12) )
			lightsStatus.set(5, true);
		else
			lightsStatus.set(5, false);
		
		
		sendMessageToFollower(new A3JGMessage("update",lightsStatus),null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateFromFollower(A3JGMessage msg) {

		System.out.println(this.getNode().getID()+"  has recived update");//+((Status)msg.getContent()).getNodeID());
		
		deskStatus = ((ArrayList<Boolean>) msg.getContent());
		
		if(deskStatus.get(1) || deskStatus.get(2)  || deskStatus.get(3)  || deskStatus.get(4) )
			lightsStatus.set(0, true);
		else
			lightsStatus.set(0, false);
		
		if(deskStatus.get(5) || deskStatus.get(6) )
			lightsStatus.set(1, true);
		else
			lightsStatus.set(1, false);
		
		if(deskStatus.get(7) )
			lightsStatus.set(2, true);
		else
			lightsStatus.set(2, false);
		
		if(deskStatus.get(8) || deskStatus.get(9))
			lightsStatus.set(3, true);
		else
			lightsStatus.set(3, false);
		
		if(deskStatus.get(0) )
			lightsStatus.set(4, true);
		else
			lightsStatus.set(4, false);
		sendMessageToFollower(new A3JGMessage("update",msg.getContent()),null);
	}



	@Override
	public int fitnessFunc() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	
}
