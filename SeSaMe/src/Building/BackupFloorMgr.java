package Building;


import java.util.ArrayList;

import org.jgroups.Address;

import A3JGroups.A3JGMessage;
import A3JGroups.A3JGSupervisorRole;
import A3JGroups.SSM.utils.Status;
import A3JGroups.SSM.utils.Update;

public  class BackupFloorMgr extends A3JGSupervisorRole {

	Double temp;
	Double extRad;
	Double intLum; 
	int shadeVal;
	int slatAngle;
	
	public BackupFloorMgr(int resourceCost) {
		super(resourceCost);
		shadeVal=0;
		slatAngle=45;

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
//		temp= st.getIntTemp();
//		intLum = st.getIntLum();
//		extRad= st.getExtRadiation();
		
		if( temp<23 && intLum<500 && slatAngle<85 && extRad > 5){
			slatAngle+=5;
			shadeVal = 0;
		}
		else if (intLum >500 && slatAngle>5)
			slatAngle-=5;

		else if (temp>23 && extRad >300 && slatAngle>5)
			slatAngle-=5;
		
		Update update = new Update();
		update.setAddress(getChan().getAddress());
		update.setNodeId(getNode().getID());
//		update.setAngle(slatAngle);
		update.setShadeVal(shadeVal);
		final Address addrs = st.getNodeAdd();
		sendMessageToFollower(new A3JGMessage("update",update) , new ArrayList<Address>(){{add(addrs);}});
		//System.out.println(this.getNode().getID()+"  has recived message from: "+((Update)msg.getContent()).getAddress());
	}

	@Override
	public void updateFromFollower(A3JGMessage msg) {

		System.out.println(this.getNode().getID()+"  has recived message from: "+((Status)msg.getContent()).getNodeID());
		Status st = ((Status)msg.getContent());
//		temp= st.getIntTemp();
//		intLum = st.getIntLum();
//		extRad= st.getExtRadiation();
		
		if( temp<23 && intLum<500 && slatAngle<85 && extRad > 5){
			slatAngle+=5;
			shadeVal = 0;
		}
		else if (intLum >500 && slatAngle>5)
			slatAngle-=5;

		else if (temp>23 && extRad >300 && slatAngle>5)
			slatAngle-=5;
		
		Update update = new Update();
		update.setAddress(getChan().getAddress());
		update.setNodeId(getNode().getID());
//		update.setAngle(slatAngle);
		update.setShadeVal(shadeVal);
		final Address addrs = st.getNodeAdd();
		sendMessageToFollower(new A3JGMessage("update",update) , new ArrayList<Address>(){{add(addrs);}});
		//System.out.println(this.getNode().getID()+"  has recived message from: "+((Update)msg.getContent()).getAddress());
	}



	@Override
	public int fitnessFunc() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	
}
