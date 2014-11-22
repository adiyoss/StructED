package BL.Prediction;

import BL.ClassifierData;
import Constants.Consts;
import Data.Entities.Example;
import Data.Entities.PredictedLabels;
import Data.Entities.Vector;
import Data.Logger;
import Helpers.Comperators.MapValueComparatorAscending;
import Helpers.MathHelpers;

import java.util.TreeMap;

public class PredictionRanking implements Prediction{

    public PredictedLabels predictForTrain(Example vector, Vector W, String realClass, ClassifierData classifierData, double epsilonArgMax)
    {
        try{
            PredictedLabels tree = new PredictedLabels();
            String min_label_2 = "";
            double min_score_2 = 0;

            String max_label_1 = "";
            double max_score_1 = 0;

            String min_label_1 = "";
            double min_score_1 = 0;

            String max_label_0 = "";
            double max_score_0 = 0;


            boolean isFirst_2 = true;
            boolean isFirst_1 = true;
            boolean isFirst_0 = true;

            for(int i=0 ; i<vector.getFeatures2D().size() ; i++){
                double tmp;
                switch (vector.getLabels2D().get(i)){
                    case 2:
                        if(isFirst_2){
                            min_label_2 = String.valueOf(i);
                            min_score_2 = MathHelpers.multipleVectors(W,vector.getFeatures2D().get(i));
                            isFirst_2 = false;
                        } else {
                            tmp = MathHelpers.multipleVectors(W,vector.getFeatures2D().get(i));
                            if(tmp < min_score_2){
                                min_label_2 = String.valueOf(i);
                                min_score_2 = tmp;
                            }
                        }
                        break;
                    case 1:
                        if(isFirst_1){
                            max_label_1 = min_label_1 = String.valueOf(i);
                            max_score_1 = min_score_1 = MathHelpers.multipleVectors(W,vector.getFeatures2D().get(i));
                            isFirst_1 = false;
                        } else {
                            tmp = MathHelpers.multipleVectors(W,vector.getFeatures2D().get(i));

                            if(tmp > max_score_1){
                                max_label_1 = String.valueOf(i);
                                max_score_1 = tmp;
                            }

                            if(tmp < min_score_1){
                                min_label_1 = String.valueOf(i);
                                min_score_1 = tmp;
                            }
                        }
                        break;
                    case 0:
                        if(isFirst_0){
                            max_label_0 = String.valueOf(i);
                            max_score_0 = MathHelpers.multipleVectors(W,vector.getFeatures2D().get(i));
                            isFirst_0 = false;
                        } else {
                            tmp = MathHelpers.multipleVectors(W,vector.getFeatures2D().get(i));
                            if(tmp > max_score_0){
                                max_label_0 = String.valueOf(i);
                                max_score_0 = tmp;
                            }
                        }
                        break;
                    default:
                        break;
                }
            }

            double loss_1 = classifierData.taskLoss.computeTaskLoss(String.valueOf(min_score_2),String.valueOf(max_score_1),classifierData.arguments);
            double loss_2 = classifierData.taskLoss.computeTaskLoss(String.valueOf(min_score_1),String.valueOf(max_score_0),classifierData.arguments);
//            double loss_3 = classifierData.taskLoss.computeTaskLoss(String.valueOf(min_score_2),String.valueOf(max_score_0),0);

            Logger.info("Before: " + loss_1);
            Logger.info("Before: "+loss_2);
            if(loss_1 > 0)
                tree.put(min_label_2+Consts.CLASSIFICATION_SPLITTER+max_label_1, -2.0);

            if(loss_2 > 0)
                tree.put(min_label_1+Consts.CLASSIFICATION_SPLITTER+max_label_0, -1.0);

//            if(loss_3 > 0)
//                tree.put(min_label_2+Consts.CLASSIFICATION_SPLITTER+max_label_0, 0.0);

            MapValueComparatorAscending vc = new MapValueComparatorAscending(tree);
            PredictedLabels result = new PredictedLabels(vc);
            result.putAll(tree);

            return result;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public PredictedLabels predictForTest(Example vector, Vector W, String realClass, ClassifierData classifierData, int returnAll)
    {
        try{
            TreeMap<String,Double> tree = new TreeMap<String, Double>();

            for(int i=0 ; i<vector.getFeatures2D().size() ; i++){
                double rankValue = MathHelpers.multipleVectors(W,vector.getFeatures2D().get(i));
                tree.put(String.valueOf(i),rankValue);
            }

            MapValueComparatorAscending vc = new MapValueComparatorAscending(tree);
            PredictedLabels result = new PredictedLabels(vc);
            result.putAll(tree);

            return result;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}