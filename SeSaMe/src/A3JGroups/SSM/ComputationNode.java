package A3JGroups.SSM;

import A3JGroups.A3JGNode;

public class ComputationNode extends A3JGNode {

	// Sezione e sottosezione

	private int temperatureTreshold;

	private int counter = 0;
	private int[] array = { 20, 21, 22, 23, 26, 26, 25, 26, 27, 28, 29, 27, 26,
			27, 26, 23, 22, 21 };

	public ComputationNode(String ID) {
		super(ID);
	}

	public int getActualTemperature() {
		if (++counter > 9)
			counter = 0;
		return array[counter];
	}

	public int getTemperatureTreshold() {
		return temperatureTreshold;
	}

	public void setTemperatureTreshold(int temperatureTreshold) {
		this.temperatureTreshold = temperatureTreshold;
	}

}
