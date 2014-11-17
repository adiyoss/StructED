package BL.Algorithms;

import java.util.ArrayList;

import BL.ClassifierData;
import Constants.ConfigParameters;
import Constants.Consts;
import Constants.ErrorConstants;
import Data.Entities.Example;
import Data.Entities.Vector;
import Data.Logger;
import Helpers.MathHelpers;

public class RampLoss implements AlgorithmUpdateRule {

    //Singleton
    private static RampLoss ourInstance = new RampLoss();
    public static RampLoss getInstance(ArrayList<Double> arguments) {
        if(arguments.size() != ConfigParameters.RL_PARAMS_SIZE){
            Logger.error(ErrorConstants.UPDATE_ARGUMENTS_ERROR);
            return null;
        }
        //initialize the parameters
        ourInstance.eta = arguments.get(0);
        ourInstance.lambda = arguments.get(1);

        return ourInstance;
    }
    private RampLoss() {
    }

    //Data members
    double lambda;
    double eta;

	@Override
	//the eta variable in is the first cell
	//the lambda variable s in the second cell
	public Vector update(Vector currentWeights, Example vector, ClassifierData classifierData) {
		
		try{
            double algorithmIteration = classifierData.iteration;
            double epsilon = classifierData.arguments.get(0);

            double newEta = eta/Math.sqrt(algorithmIteration);

			//get the prediction
			String prediction = classifierData.predict.predictForTrain(vector, currentWeights, vector.getLabel(), classifierData, 0, 0).firstKey();
			//get the prediction with the task loss
			String predictionLoss = classifierData.predict.predictForTrain(vector, currentWeights, vector.getLabel(), classifierData, epsilon, 1).firstKey();

            Example phiPredictionNoLoss = classifierData.phi.convert(vector,prediction,classifierData.kernel);
            Example phiPredictionWithLoss= classifierData.phi.convert(vector,predictionLoss,classifierData.kernel);

			
			//compute the ramp loss
            Vector rampLoss = MathHelpers.subtract2Vectors(phiPredictionNoLoss.getFeatures(), phiPredictionWithLoss.getFeatures());

            int iteration = 0;
            //loop few times until we improve with the task value
            do{
                //perform the update
                Vector lossArg = MathHelpers.mulScalarWithVectors(rampLoss, newEta);
                Vector newWeights = MathHelpers.mulScalarWithVectors(currentWeights, 1-lambda*newEta);
                Vector W_Next = MathHelpers.add2Vectors(newWeights, lossArg);

                //get the new prediction
                String newPrediction = classifierData.predict.predictForTrain(vector, W_Next, vector.getLabel(), classifierData, 0, 0).firstKey();

                //check if we've improved
                double t1 = classifierData.taskLoss.computeTaskLoss(prediction, vector.getLabel(), classifierData.arguments);
                double t2 = classifierData.taskLoss.computeTaskLoss(newPrediction, vector.getLabel(), classifierData.arguments);

                if(t2<t1 || t2 == 1) //TODO remove this, only for multi class classifications
                    return W_Next;

                //decrease the eta for the next iteration
                newEta = newEta/2;
                iteration++;

            }while(iteration< Consts.NUM_OF_IMPROVE_ITERATIONS);
			
			return currentWeights;
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
