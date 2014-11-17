package BL.TaskLoss;

import java.util.List;

//Task loss 4 the OCR
public class TaskLossMultiClass implements TaskLoss {

	@Override
	public double computeTaskLoss(String predictClass, String actualClass, List<Double> params) {
		if(predictClass.equalsIgnoreCase(actualClass))
			return 0;
		return 1;
	}
}
