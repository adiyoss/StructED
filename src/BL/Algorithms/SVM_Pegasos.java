package BL.Algorithms;

import java.util.ArrayList;

import BL.ClassifierData;
import Constants.ConfigParameters;
import Constants.ErrorConstants;
import Data.Entities.Example;
import Data.Entities.Vector;
import Data.Logger;
import Helpers.MathHelpers;

public class SVM_Pegasos implements AlgorithmUpdateRule {

    //Singleton
    private static SVM_Pegasos ourInstance = new SVM_Pegasos();
    public static SVM_Pegasos getInstance(ArrayList<Double> arguments) {
        if(arguments.size() != ConfigParameters.SVM_PARAMS_SIZE){
            Logger.error(ErrorConstants.UPDATE_ARGUMENTS_ERROR);
            return null;
        }
        //initialize the parameters
        ourInstance.lambda = arguments.get(0);
        ourInstance.eta = arguments.get(1);

        return ourInstance;
    }

    private SVM_Pegasos() {
    }

    //Data members
    double lambda;
    double eta;

	@Override
	//in SVM the lambda value would be in the first cell of the arguments attribute
	//the second cell of the arguments attribute would be the eta 
	public Vector update(Vector currentWeights, Example example, ClassifierData classifierData) {
		
		try{
            double algorithmIteration = classifierData.iteration;

			//get the prediction
            String prediction;

            //if there's a problem with the predict return the previous weights
            try{
			    prediction = classifierData.predict.predictForTrain(example,currentWeights,example.getLabel(),classifierData,1).firstKey();
            } catch (Exception e){
                return currentWeights;
            }

            Example phiRealLabel = classifierData.phi.convert(example,example.getLabel(), classifierData.kernel);
            Example phiPrediction = classifierData.phi.convert(example,prediction, classifierData.kernel);

			//compute the phi difference
            Vector phiDifference = MathHelpers.subtract2Vectors(phiRealLabel.getFeatures(), phiPrediction.getFeatures());
			
            double newEta = eta/Math.sqrt(algorithmIteration);

			double coefficientFirstArgument = (1-(lambda*newEta));

            Vector firstArgument = MathHelpers.mulScalarWithVectors(currentWeights, coefficientFirstArgument);
            Vector secondArgument = MathHelpers.mulScalarWithVectors(phiDifference, newEta);

            Vector result = MathHelpers.add2Vectors(firstArgument, secondArgument);

			return result;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
