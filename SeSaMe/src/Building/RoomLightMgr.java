package Building;


import A3JGroups.A3JGMessage;
import A3JGroups.A3JGSupervisorRole;
import A3JGroups.SSM.utils.Update;

public  class RoomLightMgr extends A3JGSupervisorRole {

	
	
	public RoomLightMgr(int resourceCost) {
		super(resourceCost);

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

	@Override
	public void messageFromFollower(A3JGMessage msg) {

		System.out.println(this.getNode().getID()+"  has recived message from: "+((Update)msg.getContent()).getNodeId());
		//System.out.println(this.getNode().getID()+"  has recived message from: "+((Update)msg.getContent()).getAddress());
	}

	@Override
	public void updateFromFollower(A3JGMessage msg) {
	//	System.out.println(this.getNode().getID()+"  has recived update from someone");
	}



	@Override
	public int fitnessFunc() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	
}
