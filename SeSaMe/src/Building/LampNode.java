package Building;

import A3JGroups.A3JGNode;

public class LampNode extends A3JGNode {

	
	public LampNode(String ID) {
		super(ID);
		// TODO Auto-generated constructor stub
	}

	String room;

	
	
	
	public String getRoom(){
		return room;
	}
	
	public void setRoom(String rm){
		 room = rm;
	}

	
}
