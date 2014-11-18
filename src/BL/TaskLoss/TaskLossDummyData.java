package BL.TaskLoss;

import Constants.Consts;

import java.util.List;

public class TaskLossDummyData implements TaskLoss {

	@Override
	//max{0,|ys-ye - y's-y'e|-epsilon}
	public double computeTaskLoss(String predictClass, String actualClass, List<Double> params) {

        double epsilon = params.get(0);
		String predictValues[] = predictClass.split(Consts.CLASSIFICATION_SPLITTER);
		String actualClassValues[] = actualClass.split(Consts.CLASSIFICATION_SPLITTER);
	
		//calculate difference of each classification
		double predictRes = Double.parseDouble(predictValues[0]) - Double.parseDouble(predictValues[1]);
		double actualRes = Double.parseDouble(actualClassValues[0]) - Double.parseDouble(actualClassValues[1]);
		
		//subtract the epsilon		
		double absRes = Math.abs(predictRes-actualRes) - epsilon;
		
		//get the max from the absolute result minus epsilon and 0
		if(absRes > 0)
			return absRes;
		return 0;
	}
}
