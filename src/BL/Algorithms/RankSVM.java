package BL.Algorithms;

import BL.ClassifierData;
import Constants.ConfigParameters;
import Constants.Consts;
import Constants.ErrorConstants;
import Data.Entities.Example;
import Data.Entities.PredictedLabels;
import Data.Entities.Vector;
import Data.Logger;
import Helpers.MathHelpers;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.Map;

public class RankSVM implements AlgorithmUpdateRule{

    //Singleton
    private static RankSVM ourInstance = new RankSVM();
    public static RankSVM getInstance(ArrayList<Double> arguments) {
        if(arguments.size() != ConfigParameters.SVM_PARAMS_SIZE){
            Logger.error(ErrorConstants.UPDATE_ARGUMENTS_ERROR);
            return null;
        }
        //initialize the parameters
        ourInstance.lambda = arguments.get(0);
        ourInstance.eta = arguments.get(1);

        return ourInstance;
    }

    private RankSVM() {
    }

    //Data members
    double lambda;
    double eta;

    @Override
    //in SVM the lambda value would be in the first cell of the arguments attribute
    //the second cell of the arguments attribute would be the eta
    public Vector update(Vector currentWeights, Example vector, ClassifierData classifierData) {

        try{
            double algorithmIteration = classifierData.iteration;
            double epsilon = classifierData.arguments.get(0);

            //get the prediction
            PredictedLabels prediction;
            //if there's a problem with the predict return the previous weights
            try{
                prediction = classifierData.predict.predictForTrain(vector,currentWeights,vector.getLabel(),classifierData,epsilon,1);
            }catch (Exception e){
                return currentWeights;
            }
            Vector result = currentWeights;

            for(Map.Entry<String,Double> entry : prediction.entrySet()) {

                if(entry.getKey().equalsIgnoreCase(Consts.CLASSIFICATION_SPLITTER))
                    continue;
                String[] predictLabels = entry.getKey().split(Consts.CLASSIFICATION_SPLITTER);
                if(predictLabels.length != 2)
                    continue;
                if(predictLabels[0].equalsIgnoreCase("") || predictLabels[1].equalsIgnoreCase(""))
                    continue;

                Example phiRealLabel = classifierData.phi.convert(vector, predictLabels[0], classifierData.kernel);
                Example phiPrediction = classifierData.phi.convert(vector, predictLabels[1], classifierData.kernel);

                Vector phiDifference = MathHelpers.subtract2Vectors(phiRealLabel.getFeatures(), phiPrediction.getFeatures());

                double t1 = MathHelpers.multipleVectors(result,phiRealLabel.getFeatures());
                double t2 = MathHelpers.multipleVectors(result,phiPrediction.getFeatures());

                double taskLossValue = classifierData.taskLoss.computeTaskLoss(String.valueOf(t1),String.valueOf(t2),classifierData.arguments);

                //TODO figure out if this is necessary
                Logger.info("After: "+ taskLossValue);
                //compute the phi difference

                double newEta = eta/Math.sqrt(algorithmIteration);

                double coefficientFirstArgument = (1-(lambda*newEta));

                Vector firstArgument = MathHelpers.mulScalarWithVectors(currentWeights, coefficientFirstArgument);
                Vector secondArgument = MathHelpers.mulScalarWithVectors(phiDifference, newEta);

                result = MathHelpers.add2Vectors(firstArgument, secondArgument);
            }
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
