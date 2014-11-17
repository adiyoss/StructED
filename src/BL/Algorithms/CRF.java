package BL.Algorithms;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import BL.ClassifierData;
import Constants.ConfigParameters;
import Constants.ErrorConstants;
import Data.Entities.Example;
import Data.Entities.PredictedLabels;
import Data.Entities.Vector;
import Data.Logger;
import Helpers.MathHelpers;

//Conditional Random Fields
public class CRF implements AlgorithmUpdateRule {

    //Singleton
    private static CRF ourInstance = new CRF();
    public static CRF getInstance(ArrayList<Double> arguments) {
        if(arguments.size() != ConfigParameters.CRF_PARAMS_SIZE){
            Logger.error(ErrorConstants.UPDATE_ARGUMENTS_ERROR);
            return null;
        }
        //initialize the parameters
        ourInstance.eta = arguments.get(0);
        ourInstance.lambda = arguments.get(1);

        return ourInstance;
    }
    private CRF() {
    }

    //Data members
    double eta;
    double lambda;

	@Override
	//the first cell of the arguments attribute would be the mue value
	//the second cell of the arguments attribute would be the lambda
	public Vector update(Vector currentWeights, Example vector, ClassifierData classifierData) {

		try{
            double algorithmIteration = classifierData.iteration;

            double newEta = eta/Math.sqrt(algorithmIteration);

			double denominator = 0;

            //the parameter 1 at the end is used to get all the predicted labels and not only the max one
            //it can be anything but -1 - Consts.ERROR_NUMBER
            PredictedLabels predictMap = classifierData.predict.predictForTest(vector,currentWeights,vector.getLabel(),classifierData,1);

			for(Map.Entry<String, Double> entry : predictMap.entrySet())
				denominator += Math.pow(Math.E,(entry.getValue()));

            Vector probExpectation = new Vector();
			boolean isFirst = true;

			for(Map.Entry<String, Double> entry : predictMap.entrySet())
			{
				double prob = Math.pow(Math.E,(entry.getValue()));
				prob = prob/denominator;
				if(isFirst){
					isFirst = false;
					probExpectation = MathHelpers.mulScalarWithVectors(classifierData.phi.convert(vector, entry.getKey(), classifierData.kernel).getFeatures(), prob);
				} else
					probExpectation = MathHelpers.add2Vectors(probExpectation, MathHelpers.mulScalarWithVectors(classifierData.phi.convert(vector,entry.getKey(),classifierData.kernel).getFeatures(), prob));
			}

            Vector updateArg = MathHelpers.subtract2Vectors(classifierData.phi.convert(vector,vector.getLabel(),classifierData.kernel).getFeatures(), probExpectation);
			updateArg = MathHelpers.mulScalarWithVectors(updateArg, newEta);
            Vector result = MathHelpers.mulScalarWithVectors(currentWeights, 1-lambda*newEta);

			result = MathHelpers.add2Vectors(result, updateArg);

			return result;

		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
