package Test;

import Building.FDLightMgrNode;



public class socketTest {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) throws Exception {

		FDLightMgrNode node = new FDLightMgrNode("lightMgr");
		node.exchangeData();

		
	}
}




