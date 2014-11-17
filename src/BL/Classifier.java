package BL;

import Constants.Consts;
import Constants.ErrorConstants;
import Constants.Paths;
import Data.CacheVowelData;
import Data.Entities.Example;
import Data.InstancesContainer;
import Data.Entities.PredictedLabels;
import Data.Entities.Vector;
import Data.Logger;
import DataAccess.Reader;

import java.util.List;
import java.util.Map;

public class Classifier {

	public ClassifierData classifierData;
    private Vector avgWeights = new Vector();

	//train the algorithm and return the final weights
	public Vector train(Vector W, InstancesContainer data, List<Double> params, Reader reader) throws Exception
	{
        //counter - used for debug
        Vector best_W = new Vector(W);
        double bestCumulativeLoss = -1;

		//validation
		if(data == null){
            Logger.error(ErrorConstants.PHI_DATA_ERROR);
			return null;
		}

//        classifierData.arguments.clear();
        //TODO clone params
        classifierData.arguments = params;

        Logger.infoTime("Start Training...");
		//loop over all the training set
        for(int i=0 ; i<data.getSize() ; i++)
		{
            //************* PRINTINGS ****************//
            //print the start time of the program//
            classifierData.iteration++;
            Example x_train = data.getInstance(i);
            Logger.info("==================================");
            if(x_train.path != null)
                Logger.timeExample("Processing example: "+x_train.path+", Number: ",(i+1));
            else
                Logger.timeExample("Processing example number: ",(classifierData.iteration%data.getSize())+1);
            Logger.info("**********************************");
            //****************************************//

            if(x_train == null)
                continue;
			//update W
			W = classifierData.algorithmUpdateRule.update(W, x_train, classifierData);
			if(W == null)
				return null;

            //############################################################################################################//
            //######################################  UPDATE AVERAGE WEIGHTS  ############################################//
            if(avgWeights.size() != 0) {
                for (Map.Entry<Integer, Double> entry : avgWeights.entrySet())
                    avgWeights.put(entry.getKey(), (entry.getValue() * (classifierData.iteration - 1) + W.get(entry.getKey())) / classifierData.iteration);
            } else
                avgWeights = (Vector)W.clone();
            //############################################################################################################//

            //############################################################################################################//
            //##############################################  VALIDATION  ################################################//
            if(!Paths.getInstance().VALIDATION_PATH.equalsIgnoreCase("")) {
                InstancesContainer developInstances;
                developInstances = reader.readData(Paths.getInstance().VALIDATION_PATH, Consts.SPACE, Consts.COLON_SPLITTER);
                double cumulative_loss = 0;
                for (int idx = 0; idx < developInstances.getSize(); idx++) {
                    Example x_validate = developInstances.getInstance(idx);
                    PredictedLabels scores = test(W, x_validate, -1);
                    String y_hat = scores.firstKey();
                    String y = x_validate.getLabel();
                    cumulative_loss += classifierData.taskLoss.computeTaskLoss(y, y_hat, params);
                }
                cumulative_loss /= developInstances.getSize();
                if (bestCumulativeLoss == -1 || cumulative_loss < bestCumulativeLoss) {
                    bestCumulativeLoss = cumulative_loss;
                    best_W = (Vector) W.clone();
                }

                //############################################################################################################//

                //************* PRINTINGS ****************//
                Logger.info("Cumulative loss on validation set: " + cumulative_loss + ", Best cumulative loss on validation set: " + bestCumulativeLoss);
                Logger.info("==================================");
                Logger.info("");
            }
            //****************************************//

            CacheVowelData.clearCacheValues();
            //===================================//
		}

        if(!Paths.getInstance().VALIDATION_PATH.equalsIgnoreCase(""))
		    return best_W;
        return W;
	}

    public PredictedLabels test(Vector W, Example example, int returnAll) {
        return classifierData.predict.predictForTest(example, W, "", classifierData, returnAll);
    }

    public Vector getAvgWeights() {
        return avgWeights;
    }
}
