package Building;


import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import A3JGroups.A3JGNode;
import java.net.ServerSocket;


public class FDNode extends A3JGNode {

	
	ServerSocket socket = null;
	Socket cliSoc;
	int port = 1235;
	int verNo = 1;
	String host= "Adnans-MacBook-Pro-2.local";



	public FDNode(String obj) {
		super(obj);
		
		setTakeAction(false);
		setStateChanged(false);
		//	state = new A3JGroups.SSM.utils.Status(getID(),totLights,0);
		
		
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

	

	private Boolean takeAction;
	private Boolean stateChanged;




	}
