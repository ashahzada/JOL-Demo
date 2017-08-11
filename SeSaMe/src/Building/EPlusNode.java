package Building;


import java.io.IOException;
import java.util.ArrayList;
import A3JGroups.A3JGNode;

public class EPlusNode extends A3JGNode {

	boolean status = true;
	Double temp;
	Double extRad;
	Double intLum; 
	ArrayList<Double> slatAngle;
	String[] args;


	public EPlusNode(String obj, String[] argms) throws IOException {
		super(obj);

		args= argms;
	//	exchangeData();

	}



	public ArrayList<Double> getSlatAngle() {
		return slatAngle;
	}


	public void setSlatAngle(ArrayList<Double> slatAngle) {
		this.slatAngle = slatAngle;
	}




	
	public Double getTemp() {
		return temp;
	}




	public void setTemp(Double temp) {
		this.temp = temp;
	}




	public Double getExtRad() {
		return extRad;
	}




	public void setExtRad(Double extRad) {
		this.extRad = extRad;
	}




	public Double getIntLum() {
		return intLum;
	}




	public void setIntLum(Double intLum) {
		this.intLum = intLum;
	}




	public boolean getStatus() {
		return status;
	}




	public void setStatus(boolean status) {
		this.status = status;
	}
}