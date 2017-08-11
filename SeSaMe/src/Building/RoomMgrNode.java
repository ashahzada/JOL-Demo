package Building;

import A3JGroups.A3JGNode;
import A3JGroups.SSM.utils.Status;


public class RoomMgrNode extends A3JGNode {

	
	int totLights;
	A3JGroups.SSM.utils.Status state;
	String lastTurnedOnLight;
	

	
	public RoomMgrNode(String obj) {
		super(obj);
		totLights = 0;
		setTakeAction(false);
		setStateChanged(false);
	//	state = new A3JGroups.SSM.utils.Status(getID(),totLights,0);
		lastTurnedOnLight = "";
		
		
		// TODO Auto-generated constructor stub
	}
	
	public int getTotalLightsLimit()
	{
		return totLights;
	}
	
	public void updateState(int onLgts){
	//	state.setTotalLights(totLights);
	//	state.setStatus(onLgts);
		setStateChanged(true);
	}

	public Boolean IsStateChanged() {
		return stateChanged;
	}

	public void setStateChanged(Boolean stateChanged) {
		this.stateChanged = stateChanged;
	}

	public Boolean IsActionRequired() {
		return takeAction;
	}

	public void setTakeAction(Boolean takeAction) {
		this.takeAction = takeAction;
	}
	
	public String getLastTurnedOnLight() {
		return lastTurnedOnLight;
	}

	public void setLastTurnedOnLight(String lastTurnedOnLight) {
		this.lastTurnedOnLight = lastTurnedOnLight;
	}

	public Status getState() {
		return state;
	}

	public void setState(Status state) {
		this.state = state;
	}

	private Boolean takeAction;
	private Boolean stateChanged;
	
	
}
