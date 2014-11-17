package BL.TaskLoss;

import java.util.List;

//this interface enables us to create multiple task loss functions and compare between them
public interface TaskLoss {
	double computeTaskLoss(String predictClass, String actualClass, List<Double> params);
}
