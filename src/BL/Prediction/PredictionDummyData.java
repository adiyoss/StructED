package BL.Prediction;

import BL.ClassifierData;
import Constants.Consts;
import Constants.ErrorConstants;
import Data.Entities.Example;
import Data.Entities.PredictedLabels;
import Data.Entities.Vector;
import Helpers.Comperators.MapValueComparatorDescending;
import Helpers.MathHelpers;

public class PredictionDummyData implements Prediction {

    //predict function
    //argmax(yS,yE) (W*Phi(Xi,yS,yE)) + Task Loss
    //this function assumes that the argument vector has already been converted to phi vector
    //return null on error
    public PredictedLabels predictForTrain(Example vector, Vector W, String realClass, ClassifierData classifierData, double epsilonArgMax)
    {
        try{
            PredictedLabels tree = new PredictedLabels();

            //validation
            if(vector.sizeOfVector<=0)
            {
                System.err.println(ErrorConstants.PHI_VECTOR_DATA);
                return null;
            }

            //loop over all the classifications of this specific example
            for(int i=Consts.MIN_GAP_START_DUMMY-1 ; i<vector.sizeOfVector-Consts.MIN_GAP_END_DUMMY ; i++)
            {
                for(int j=i+1 ; j<vector.sizeOfVector- Consts.MIN_GAP_END_DUMMY ; j++)
                {
                    Example phiData = classifierData.phi.convert(vector,(i+1)+ Consts.CLASSIFICATION_SPLITTER+(j+1),classifierData.kernel);
                    //multiple the vectors
                    double tmp = MathHelpers.multipleVectors(W, phiData.getFeatures());

                    if(epsilonArgMax != 0){
                        //add the task loss
                        tmp += epsilonArgMax*classifierData.taskLoss.computeTaskLoss((i+1)+ Consts.CLASSIFICATION_SPLITTER+(j+1), realClass, classifierData.arguments);
                    }

                    //get the max value for the max classification
                    tree.put((i+1)+ Consts.CLASSIFICATION_SPLITTER+(j+1),tmp);
                }
            }

            MapValueComparatorDescending vc = new MapValueComparatorDescending(tree);
            PredictedLabels result = new PredictedLabels(vc);
            result.putAll(tree);

            return result;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public PredictedLabels predictForTest(Example vector, Vector W, String realClass, ClassifierData classifierData, int returnAll)
    {
        return predictForTrain(vector,W,realClass,classifierData,0);
    }
}
