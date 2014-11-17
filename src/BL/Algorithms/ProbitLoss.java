package BL.Algorithms;

import java.util.ArrayList;
import java.util.Random;

import BL.ClassifierData;
import Constants.ConfigParameters;
import Constants.Consts;
import Constants.ErrorConstants;
import Data.Entities.Example;
import Data.Entities.Vector;
import Data.Logger;
import Helpers.MathHelpers;

public class ProbitLoss implements AlgorithmUpdateRule {

    //Singleton
    private static ProbitLoss ourInstance = new ProbitLoss();
    public static ProbitLoss getInstance(ArrayList<Double> arguments) {
        if(arguments.size() != ConfigParameters.PL_PARAMS_SIZE){
            Logger.error(ErrorConstants.UPDATE_ARGUMENTS_ERROR);
            return null;
        }
        //initialize the parameters
        ourInstance.eta = arguments.get(0);
        ourInstance.lambda = arguments.get(1);
        ourInstance.numOfIteration = arguments.get(2);

        return ourInstance;
    }
    private ProbitLoss(){
    }

    //Data members
    double lambda;
    double eta;
    double numOfIteration; //indicates the number of iterations
    double rangeMin = -1;
    double rangeMax = 1;

	@Override
	//eta would be at the first cell
	//lambda would be at the second cell
	//the number of iteration that we'll go and generate epsilon 
	public Vector update(Vector currentWeights, Example vector, ClassifierData classifierData) {

        try{
            double algorithmIteration = classifierData.iteration;
            double epsilon = classifierData.arguments.get(0);

            double newEta = eta/Math.sqrt(algorithmIteration);
            Vector expectation = new Vector();
            Random random = new Random();

            //get the prediction
            String prediction = classifierData.predict.predictForTrain(vector, currentWeights, vector.getLabel(), classifierData, 0, 0).firstKey();

            //Initialize the expectation
			for(Integer key : currentWeights.keySet())
				expectation.put(key,0.0);
			
			//calculate the expectation 
			for(int i=0 ; i<numOfIteration ; i++)
			{
                Vector epsilonVector = new Vector();

                //only one location
                int location = random.nextInt(ConfigParameters.getInstance().VECTOR_SIZE);
                epsilonVector.put(location,0.001*(random.nextDouble()*(rangeMax - rangeMin)+rangeMin)); //TODO get the normalization factor as parameter

//				//generating the epsilon vector
//				for(Integer key : currentWeights.keySet())
//					epsilonVector.put(key,0.000001*(random.nextDouble()*(rangeMax - rangeMin)+rangeMin)); //TODO get the normalization number from the config file
				
				//W+EpsilonVector
                Vector U = MathHelpers.add2Vectors(currentWeights, epsilonVector);
                //get the noisy prediction
				String noisyPrediction = classifierData.predict.predictForTrain(vector, U, vector.getLabel(), classifierData, epsilon, 1).firstKey();
				//calculate loss
                double taskLossValue = classifierData.taskLoss.computeTaskLoss(vector.getLabel(), noisyPrediction, classifierData.arguments);
				//update the expectation
                epsilonVector = MathHelpers.mulScalarWithVectors(epsilonVector, taskLossValue);
				expectation = MathHelpers.add2Vectors(expectation, epsilonVector);
			}

            expectation = MathHelpers.mulScalarWithVectors(expectation, ((-1)/numOfIteration));

            int iteration = 0;
            //loop few times until we improve with the task value
            do{
                //perform the normalization on the current weight
                Vector oldWeights = MathHelpers.mulScalarWithVectors(currentWeights, (1-lambda*newEta));
                Vector updateArgument = MathHelpers.mulScalarWithVectors(expectation, newEta);
                //perform the update
                Vector W_Next = MathHelpers.add2Vectors(oldWeights, updateArgument);

                //get the new prediction
                String newPrediction = classifierData.predict.predictForTrain(vector, W_Next, vector.getLabel(), classifierData, 0, 0).firstKey();

                //check if we've improved
                double t1 = classifierData.taskLoss.computeTaskLoss(prediction, vector.getLabel(), classifierData.arguments);
                double t2 = classifierData.taskLoss.computeTaskLoss(newPrediction, vector.getLabel(), classifierData.arguments);

                if(t2<t1 || t2 == 1) //TODO remove this, only for multi class problems
                    return W_Next;

                //decrease the eta for the next iteration
                newEta = newEta/2;
                iteration++;

            }while(iteration<Consts.NUM_OF_IMPROVE_ITERATIONS);

            return currentWeights;

		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}


