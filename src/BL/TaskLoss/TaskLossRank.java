package BL.TaskLoss;

import java.util.List;

public class TaskLossRank implements TaskLoss {
    @Override
    public double computeTaskLoss(String predictClass, String actualClass, List<Double> params) {
        try {
            double r = Double.parseDouble(predictClass);
            double s = Double.parseDouble(actualClass);
            if(1 - r + s < 0)
                return 0;
            return (1 - r + s);
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }
}
