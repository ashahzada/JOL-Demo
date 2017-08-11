package Building;


import java.util.ArrayList;
import java.util.Collections;

import org.jgroups.Address;

import A3JGroups.A3JGMessage;
import A3JGroups.A3JGSupervisorRole;
import A3JGroups.SSM.utils.Status;
import A3JGroups.SSM.utils.Update;

public  class FloorMgr extends A3JGSupervisorRole {

	ArrayList<Double> temp;
	ArrayList<Double> extRad;
	ArrayList<Double> intLum; 
	ArrayList<Double> slatAngle;
	int shadeVal;
	
	public FloorMgr(int resourceCost) {
		super(resourceCost);
		temp= new ArrayList<Double>(Collections.nCopies(12,0.0));
		extRad= new ArrayList<Double>(Collections.nCopies(12,0.0));
		intLum= new ArrayList<Double>(Collections.nCopies(12,0.0));
		slatAngle= new ArrayList<Double>(Collections.nCopies(12,25.0));

	}



	@Override
	public void run() {

		while (this.active) {
//			vista = this.node.getChannels("SV").getView();
//			try {
//				Thread.sleep(2000);
//
//			} catch (Exception e) {
//				e.printStackTrace();
			}

	}

	@SuppressWarnings("serial")
	@Override
	public void messageFromFollower(A3JGMessage msg) {

		System.out.println(this.getNode().getID()+"  has recived message from: "+((Status)msg.getContent()).getNodeID());
		Status st = ((Status)msg.getContent());
		temp= st.getIntTemp();
		intLum = st.getIntLum();
		extRad= st.getExtRadiation();
		
		for(int i=0; i<12;i++){ 
			
			if( temp.get(i) <23 && intLum.get(i)<500 && slatAngle.get(i)<85 && extRad.get(i) > 5){
				slatAngle.set(i, slatAngle.get(i)+5);
				shadeVal = 0;
			}
			else if (intLum.get(i) >500 && slatAngle.get(i)>5)
				slatAngle.set(i, slatAngle.get(i)-5);
	
			else if (temp.get(i)>23 && extRad.get(i) >300 && slatAngle.get(i)>5)
				slatAngle.set(i, slatAngle.get(i)-5);
		}
		
		Update update = new Update();
		update.setAddress(getChan().getAddress());
		update.setNodeId(getNode().getID());
		update.setAngle(slatAngle);
		update.setShadeVal(shadeVal);
		final Address addrs = st.getNodeAdd();
		sendMessageToFollower(new A3JGMessage("update",update) , new ArrayList<Address>(){{add(addrs);}});
		//System.out.println(this.getNode().getID()+"  has recived message from: "+((Update)msg.getContent()).getAddress());
	}

	@Override
	public void updateFromFollower(A3JGMessage msg) {

		System.out.println(this.getNode().getID()+"  has recived update from: "+((Status)msg.getContent()).getNodeID());
		Status st = ((Status)msg.getContent());
		temp= st.getIntTemp();
		intLum = st.getIntLum();
		extRad= st.getExtRadiation();
		
		for(int i=0; i<12;i++){ 
			
			if( temp.get(i) <23 && intLum.get(i)<500 && slatAngle.get(i)<85 && extRad.get(i) > 5){
				slatAngle.set(i, slatAngle.get(i)+5);
				shadeVal = 0;
			}
			else if (intLum.get(i) >500 && slatAngle.get(i)>5)
				slatAngle.set(i, slatAngle.get(i)-5);
	
			else if (temp.get(i)>23 && extRad.get(i) >300 && slatAngle.get(i)>5)
				slatAngle.set(i, slatAngle.get(i)-5);
		}
		
		Update update = new Update();
		update.setAddress(getChan().getAddress());
		update.setNodeId(getNode().getID());
		update.setAngle(slatAngle);
		update.setShadeVal(shadeVal);
		final Address addrs = st.getNodeAdd();
		sendMessageToFollower(new A3JGMessage("update",update) , new ArrayList<Address>(){{add(addrs);}});
	}



	@Override
	public int fitnessFunc() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	
}
