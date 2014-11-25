package BL.Prediction;

import BL.ClassifierData;
import Constants.Consts;
import Constants.ErrorConstants;
import Data.Entities.Example;
import Data.Entities.PredictedLabels;
import Data.Entities.Vector;
import Data.Logger;
import Helpers.Comperators.MapValueComparatorDescending_IntKey;
import Helpers.MathHelpers;
import Helpers.ModelHandler;

import java.util.TreeMap;

public class PredictionVowelDurationData implements Prediction{

    //predict function
    //argmax(yS,yE) (W*Phi(Xi,yS,yE)) + Task Loss
    //this function assumes that the argument vector has already been converted to phi vector
    //return null on error
    public PredictedLabels predictForTrain(Example vector, Vector W, String realClass, ClassifierData classifierData, double epsilonArgMax)
    {
        try{
            double maxVal = 0;
            String maxLabel = "";
            boolean isFirst = true;

            //validation
            if(vector.sizeOfVector<=0)
            {
                Logger.error(ErrorConstants.PHI_VECTOR_DATA);
                return null;
            }

            //loop over all the classifications of this specific example
            for(int i=Consts.MIN_GAP_START ; i<vector.sizeOfVector-(Consts.MIN_GAP_END) ; i++)
            {
                for(int j=i+Consts.MIN_VOWEL ; j<i+Consts.MAX_VOWEL ; j++)
                {
                    if(j>vector.sizeOfVector-(Consts.MIN_GAP_END))
                        break;

                    Example phiData = classifierData.phi.convert(vector,(i+1)+Consts.CLASSIFICATION_SPLITTER+(j+1),classifierData.kernel);
                    //multiple the vectors
                    double tmp = MathHelpers.multipleVectors(W, phiData.getFeatures());

                    if(epsilonArgMax != 0){
                        //add the task loss
                        tmp += epsilonArgMax*classifierData.taskLoss.computeTaskLoss((i+1)+Consts.CLASSIFICATION_SPLITTER+(j+1), realClass, classifierData.arguments);
                    }

                    if(isFirst) {
                        maxLabel = (i + 1) + Consts.CLASSIFICATION_SPLITTER + (j + 1);
                        maxVal = tmp;
                        isFirst = false;
                    }
                    else if(tmp > maxVal) {
                        maxLabel = (i + 1) + Consts.CLASSIFICATION_SPLITTER + (j + 1);
                        maxVal = tmp;
                    }
                }
            }

            PredictedLabels result = new PredictedLabels();
            result.put(maxLabel, maxVal);

            return result;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public PredictedLabels predictForTest(Example vector, Vector W, String realClass, ClassifierData classifierData, int returnAll)
    {
        return predictForTrain(vector, W, realClass, classifierData ,0);
    }
}
