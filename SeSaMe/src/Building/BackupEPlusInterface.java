package Building;



import java.io.IOException;

import A3JGroups.A3JGFollowerRole;
import A3JGroups.A3JGMessage;
import A3JGroups.SSM.utils.Status;
import A3JGroups.SSM.utils.Update;

public  class BackupEPlusInterface extends A3JGFollowerRole   {
	
	Status state;
	
	public BackupEPlusInterface(int resourceCost) {
		super(resourceCost);
		// TODO Auto-generated constructor stub
	}

	public BackupEPlusNode getNode(){
	
		return ((BackupEPlusNode)this.getNode());
	}


	@Override
	public void run() {
		
			
//		sendMessageToSupervisor(new A3JGMessage("Temperature2",
//				new Update(getChan().getAddress(),
//						getNode().getID())));
	
			while(active){
				state = new Status();
//				state.setExtRadiation(getNode().getExtRad());
//				state.setIntLum(getNode().getIntLum());
//				state.setIntTemp(getNode().getTemp());
				state.setNodeAdd(getChan().getAddress());
				state.setNodeID(getNode().getID());
				
				sendUpdateToSupervisor(new A3JGMessage("update",state));
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

		}}


	@Override
	public void messageFromSupervisor(A3JGMessage msg) {
			System.out.println("Message from SV");
			Update updt = ((Update)msg.getContent());
			getNode().setShadeVal(updt.getShadeVal());
//			getNode().setSlatAngle(updt.getAngle());
		}

	

	
}