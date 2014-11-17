package BL.Prediction;

import BL.ClassifierData;
import Constants.Consts;
import Constants.ErrorConstants;
import Data.Entities.Example;
import Data.Entities.PredictedLabels;
import Data.Entities.Vector;
import Data.Logger;
import Helpers.Comperators.MapValueComparatorDescending;
import Helpers.MathHelpers;

public class PredictionMultiClass implements Prediction{

    int numOfClass = 10;
    //predict function
    //argmax(yS,yE) (W*Phi(Xi,yS,yE)) + Task Loss
    //this function assumes that the argument vector has already been converted to phi vector
    //return null on error
    public PredictedLabels predictForTrain(Example vector, Vector W, String realClass, ClassifierData classifierData, double epsilon, double epsilonArgMax)
    {
        try{
            PredictedLabels tree = new PredictedLabels();

            //validation
            if(vector.sizeOfVector<=0) {
                Logger.error(ErrorConstants.PHI_VECTOR_DATA);
                return null;
            }

            for(int i=0 ; i<numOfClass ; i++){

                Example phiData = classifierData.phi.convert(vector,String.valueOf(i),classifierData.kernel);
                //multiple the vectors
                double tmp = MathHelpers.multipleVectors(W, phiData.getFeatures());

                if(epsilonArgMax != 0){
                    //add the task loss
                    tmp += epsilonArgMax*classifierData.taskLoss.computeTaskLoss(String.valueOf(i), realClass, classifierData.arguments);
                }

                //get the max value for the max classification
                tree.put(String.valueOf(i),tmp);
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

    public PredictedLabels predictForTest(Example vector, Vector W, String realClass, ClassifierData classifierData, int returnAll){

        if(returnAll != Consts.ERROR_NUMBER) {
            try {
                PredictedLabels tree = new PredictedLabels();

                //validation
                if (vector.sizeOfVector <= 0) {
                    Logger.error(ErrorConstants.PHI_VECTOR_DATA);
                    return null;
                }

                for (int i = 0; i < numOfClass; i++) {
                    Example phiData = classifierData.phi.convert(vector, String.valueOf(i), classifierData.kernel);
                    //multiple the vectors
                    double tmp = MathHelpers.multipleVectors(W, phiData.getFeatures());

                    //get the max value for the max classification
                    tree.put(String.valueOf(i), tmp);
                }

                MapValueComparatorDescending vc = new MapValueComparatorDescending(tree);
                PredictedLabels result = new PredictedLabels(vc);
                result.putAll(tree);

                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else
            return predictForTrain(vector, W, realClass, classifierData ,0 ,0);
    }
}
