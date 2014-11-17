package BL.Prediction;

import BL.ClassifierData;
import Data.Entities.Example;
import Data.Entities.PredictedLabels;
import Data.Entities.Vector;

public interface Prediction {
    public PredictedLabels predictForTrain(Example vector, Vector W, String realClass, ClassifierData classifierData, double epsilon, double epsilonArgMax);
    public PredictedLabels predictForTest(Example vector, Vector W, String realClass, ClassifierData classifierData, int returnAll);
}
