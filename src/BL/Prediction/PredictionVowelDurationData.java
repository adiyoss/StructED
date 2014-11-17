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
    public PredictedLabels predictForTrain(Example vector, Vector W, String realClass, ClassifierData classifierData, double epsilon, double epsilonArgMax)
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

            Example phiData = classifierData.phi.convert(vector,maxLabel,classifierData.kernel);
            Example phiRealData = classifierData.phi.convert(vector,vector.getLabel(),classifierData.kernel);

            TreeMap<Integer, Double> tree = ModelHandler.sortMul(W, phiData.getFeatures());
            MapValueComparatorDescending_IntKey vc = new MapValueComparatorDescending_IntKey(tree);
            TreeMap<Integer, Double> result_pred = new  TreeMap<Integer, Double>(vc);
            result_pred.putAll(tree);
            Logger.info("Predict = "+result_pred.toString());

            TreeMap<Integer, Double> treeReal = ModelHandler.sortMul(W, phiRealData.getFeatures());
            MapValueComparatorDescending_IntKey vcReal = new MapValueComparatorDescending_IntKey(treeReal);
            TreeMap<Integer, Double> result_real = new  TreeMap<Integer, Double>(vcReal);
            result_real.putAll(treeReal);
            Logger.info("Real = "+result_real.toString());

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
//        if(returnAll != Consts.ERROR_NUMBER) {
//            try {
//                PredictedLabels tree = new PredictedLabels();
//                //validation
//                if (vector.sizeOfVector <= 0) {
//                    Logger.error(ErrorConstants.PHI_VECTOR_DATA);
//                    return null;
//                }
//
//                //loop over all the classifications of this specific example
//                for (int i = Consts.MIN_GAP_START; i < vector.sizeOfVector - (Consts.MAX_VOWEL + Consts.MIN_GAP_END); i++) {
//                    for (int j = i + Consts.MIN_VOWEL; j < i + Consts.MAX_VOWEL; j++) {
//                        Example phiData = classifierData.phi.convert(vector, (i + 1) + Consts.CLASSIFICATION_SPLITTER + (j + 1), classifierData.kernel);
//                        //multiple the vectors
//                        double tmp = MathHelpers.multipleVectors(W, phiData.getFeatures());
//
//                        //get the max value for the max classification
//                        tree.put((i + 1) + Consts.CLASSIFICATION_SPLITTER + (j + 1), tmp);
//                    }
//                }
//
//                MapValueComparatorDescending vc = new MapValueComparatorDescending(tree);
//                PredictedLabels result = new PredictedLabels(vc);
//                result.putAll(tree);
//
//                return result;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//        } else
            return predictForTrain(vector, W, realClass, classifierData ,0 ,0);
    }
}
