package it.telecomitalia.scube.a3;

import it.freedomotic.model.object.BooleanBehavior;
import it.freedomotic.model.object.EnvObject;
import it.freedomotic.model.object.RangedIntBehavior;
import it.freedomotic.reactions.Command;

public class LightNode {
	
	protected String location;
	protected String ID;
	protected EnvObject obj;
	boolean status;
	private Command cmdPwr ;

	public LightNode(EnvObject obj) {
		super();
		this.obj = obj;
		setID(obj.getName());
		status = false;
		defineCommand();
		System.out.println("Light is mapped..");
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public EnvObject getObj() {
		return obj;
	}

	public void setObj(EnvObject obj) {
		this.obj = obj;
	}
	
	public boolean getPower() {
		BooleanBehavior b = (BooleanBehavior)obj.getBehavior("powered");
		return b.getValue();
	}

	public int getBrightness() {
		RangedIntBehavior b = (RangedIntBehavior)obj.getBehavior("brightness");
		return b.getValue();
	}
	public Command getCmdPwr() {
		return cmdPwr;
	}

	public void setCmdPwr(Command cmdPwr) {
		this.cmdPwr = cmdPwr;
	}
	
	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public void defineCommand(){
		setCmdPwr(new Command());
		//cmdPwr .setName("Turn on " + obj.getName());
		
		getCmdPwr() .setReceiver("app.events.sensors.behavior.request.objects");
		getCmdPwr() .setProperty("behavior", "powered");
		getCmdPwr() .setProperty("value", "true");
		getCmdPwr() .setProperty("object", this.getID());
	
}

}
