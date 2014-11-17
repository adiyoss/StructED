package BL.TaskLoss;

import Constants.Consts;

import java.util.List;

public class TaskLossVowelDuration implements TaskLoss {

	@Override
	//max{0,|ys-ye - y's-y'e|-epsilon}
    //max{0, |ys - ys'| - epsilon} + max{0, |ye - ye'| - epsilon}
	public double computeTaskLoss(String predictClass, String actualClass, List<Double> params) {
        try {
            Double epsilon_onset = params.get(0);
            Double epsilon_offset = params.get(1);

            String predictValues[] = predictClass.split(Consts.CLASSIFICATION_SPLITTER);
            String actualClassValues[] = actualClass.split(Consts.CLASSIFICATION_SPLITTER);

            double predictResStart = Double.parseDouble(predictValues[0]);
            double actualResStart = Double.parseDouble(actualClassValues[0]);

            double predictResEnd = Double.parseDouble(predictValues[1]);
            double actualResEnd = Double.parseDouble(actualClassValues[1]);

            double diffStart = Math.abs(predictResStart - actualResStart);
            double diffEnd = Math.abs(predictResEnd - actualResEnd);

            //subtract the epsilon
            double absRes = 0;
            if(diffStart >=  epsilon_onset)
                absRes += diffStart;
            if(diffEnd >= epsilon_offset)
                absRes += diffEnd;

            //get the max from the absolute result minus epsilon and 0
            return absRes;

        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
	}
}
