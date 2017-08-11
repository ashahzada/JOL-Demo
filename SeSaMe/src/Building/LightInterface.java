package Building;



import A3JGroups.A3JGFollowerRole;
import A3JGroups.A3JGMessage;
import A3JGroups.SSM.utils.Update;

public  class LightInterface extends A3JGFollowerRole   {
	
	public LightInterface(int resourceCost) {
		super(resourceCost);
		// TODO Auto-generated constructor stub
	}



	@Override
	public void run() {
			
		sendMessageToSupervisor(new A3JGMessage("Temperature2",
				new Update(getChan().getAddress(),
						getNode().getID())));
		
			while(active){
				sendUpdateToSupervisor(new A3JGMessage("Temperature2",
						new Update(getChan().getAddress(),
								getNode().getID())));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

		}}


	@Override
	public void messageFromSupervisor(A3JGMessage msg) {
			System.out.println(msg.toString());
		}

	

	
}