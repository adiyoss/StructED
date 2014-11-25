package BL.Algorithms;

import java.util.ArrayList;

import BL.ClassifierData;
import Constants.ConfigParameters;
import Constants.ErrorConstants;
import Data.Entities.Example;
import Data.Entities.Vector;
import Data.Logger;
import Helpers.MathHelpers;

public class DirectLoss implements AlgorithmUpdateRule {

    //Singleton
    private static DirectLoss ourInstance = new DirectLoss();
    public static DirectLoss getInstance(ArrayList<Double> arguments) {
        if(arguments.size() != ConfigParameters.DL_PARAMS_SIZE){
            Logger.error(ErrorConstants.UPDATE_ARGUMENTS_ERROR);
            return null;
        }
        //initialize the parameters
        ourInstance.eta = arguments.get(0);
        ourInstance.epsilonArgMax = arguments.get(1);
        return ourInstance;
    }
    private DirectLoss(){}

    //Data members
    double eta;
    double epsilonArgMax;

	@Override
	//the first cell of the arguments attribute would be the eta value
	//the second cell of the arguments attribute would be the epsilon for the argmax
	public Vector update(Vector currentWeights, Example vector, ClassifierData classifierData) {
		
		try{
            double algorithmIteration = classifierData.iteration;
            double newEta = eta/(Math.sqrt(algorithmIteration)*Math.abs(epsilonArgMax));

			//get the prediction
			String prediction = classifierData.predict.predictForTrain(vector, currentWeights, vector.getLabel(), classifierData, 0).firstKey();
			//get the direct prediction
			String predictionDirect = classifierData.predict.predictForTrain(vector, currentWeights, vector.getLabel(),classifierData, epsilonArgMax).firstKey();
            Vector directLoss;

            Example phiPredictionNoLoss = classifierData.phi.convert(vector,prediction,classifierData.kernel);
            Example phiPredictionDirect = classifierData.phi.convert(vector,predictionDirect,classifierData.kernel);

            //create the direct loss value by the epsilon sign
            if(epsilonArgMax < 0)
			    //compute the direct loss
                directLoss = MathHelpers.subtract2Vectors(phiPredictionDirect.getFeatures(), phiPredictionNoLoss.getFeatures());
            else
                directLoss = MathHelpers.subtract2Vectors(phiPredictionNoLoss.getFeatures(),phiPredictionDirect.getFeatures());

            //get the update argument for the direct loss
            Vector updateArg = MathHelpers.mulScalarWithVectors(directLoss, newEta);
            Vector W_Next = MathHelpers.add2Vectors(currentWeights, updateArg); //compute the new weights

			return W_Next;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
