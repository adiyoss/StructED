package BL;

import BL.Algorithms.AlgorithmUpdateRule;
import BL.Kernels.Kernel;
import BL.Prediction.Prediction;
import BL.TaskLoss.TaskLoss;
import Data.FeatureFunctions.PhiConverter;

import java.util.List;

//this class contains all the relevant data to the classifier
public class ClassifierData {

    public TaskLoss taskLoss;
    public AlgorithmUpdateRule algorithmUpdateRule;
    public Prediction predict;
    public Kernel kernel;
    public PhiConverter phi;
    public List<Double> arguments;
    public int iteration = 0;

}
