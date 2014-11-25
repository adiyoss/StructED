package BL.Algorithms;

import java.util.ArrayList;

import BL.ClassifierData;
import Constants.ConfigParameters;
import Constants.ErrorConstants;
import Data.Entities.Example;
import Data.Entities.Vector;
import Data.Logger;
import Helpers.MathHelpers;

public class PassiveAggressive implements AlgorithmUpdateRule {

    //Singleton
    private static PassiveAggressive ourInstance = new PassiveAggressive();
    public static PassiveAggressive getInstance(ArrayList<Double> arguments) {
        if(arguments.size() != ConfigParameters.PA_PARAMS_SIZE){
            Logger.error(ErrorConstants.UPDATE_ARGUMENTS_ERROR);
            return null;
        }
        //initialize the parameters
        ourInstance.cValue = arguments.get(0);

        return ourInstance;
    }

    private PassiveAggressive() {
    }

    //Data members
    double cValue;

    @Override
	//the first cell of the arguments attribute would be the C value
	public Vector update(Vector currentWeights, Example vector, ClassifierData classifierData) {
		
		try{
			//get the prediction
			String prediction = classifierData.predict.predictForTrain(vector, currentWeights, vector.getLabel(), classifierData,1).firstKey();

            Example phiRealLabel = classifierData.phi.convert(vector, vector.getLabel(), classifierData.kernel);
            Example phiPrediction = classifierData.phi.convert(vector, prediction, classifierData.kernel);
			
			//compute the phi difference
            Vector phiDifference = MathHelpers.subtract2Vectors(phiRealLabel.getFeatures(), phiPrediction.getFeatures());
			
			double taskLossValue = classifierData.taskLoss.computeTaskLoss(prediction, vector.getLabel(), classifierData.arguments);
			
			double multipleVectors = MathHelpers.multipleVectors(currentWeights , phiDifference);
			double denominator = MathHelpers.multipleVectors(phiDifference, phiDifference);

			double tau = 0;
			if(denominator != 0){
				tau = (taskLossValue - multipleVectors)/denominator;
			}

			if(tau > cValue)
				tau = cValue;

            if(tau < 0)
                Logger.error("Loss < 0!, prediction search can't get to the target label.");

            Vector updateVector = MathHelpers.mulScalarWithVectors(phiDifference, tau);
            Vector result = MathHelpers.add2Vectors(currentWeights, updateVector);

			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}


