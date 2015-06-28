/*
 * StructED - Machine Learning, Structured Prediction Package written in Java.
 * Copyright (C) 2014 Yossi Adi, E-Mail: yossiadidrum@gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package BL.Algorithms;

import java.util.ArrayList;
import java.util.Random;

import BL.ClassifierData;
import Constants.ConfigParameters;
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
        ourInstance.noiseAllVector = arguments.get(3);
        ourInstance.mean = arguments.get(4);
        ourInstance.stDev = arguments.get(5);

        return ourInstance;
    }
    private ProbitLoss(){}

    //Data members
    double lambda;
    double eta;
    double numOfIteration; //indicates the number of iterations
    double noiseAllVector; //indicates whether to noise all the weights vector or just one random element from it
    double mean; //indicates the mean to the noise to be generated
    double stDev; //indicates the standard deviation to the noise to be generated

	@Override
	//eta would be at the first cell
	//lambda would be at the second cell
	//the number of iteration that we'll go and generate epsilon 
	public Vector update(Vector currentWeights, Example example, ClassifierData classifierData) {

        try{
            double algorithmIteration = classifierData.iteration;
            double newEta = eta/Math.sqrt(algorithmIteration);
            Vector expectation = new Vector();
            Random random = new Random();

            //Initialize the expectation
			for(Integer key : currentWeights.keySet())
				expectation.put(key,0.0);
			
			//calculate the expectation 
			for(int i=0 ; i<numOfIteration ; i++)
			{
                Vector epsilonVector = new Vector();

                //generating noise to W
                if(noiseAllVector == 1) {
                    //generating the epsilon vector
                    for(Integer key : currentWeights.keySet())
                        epsilonVector.put(key, (stDev * random.nextGaussian() + mean));
                } else {
                    //only one location
                    int location = random.nextInt(ConfigParameters.getInstance().VECTOR_SIZE);
                    epsilonVector.put(location, (stDev * random.nextGaussian() + mean));
                }

				//W+EpsilonVector
                Vector U = MathHelpers.add2Vectors(currentWeights, epsilonVector);
                //get the noisy prediction
				String noisyPrediction = classifierData.predict.predictForTrain(example, U, example.getLabel(), classifierData, 1).firstKey();
				//calculate loss
                double taskLossValue = classifierData.taskLoss.computeTaskLoss(example.getLabel(), noisyPrediction, classifierData.arguments);
				//update the expectation
                epsilonVector = MathHelpers.mulScalarWithVectors(epsilonVector, taskLossValue);
				expectation = MathHelpers.add2Vectors(expectation, epsilonVector);
			}

            expectation = MathHelpers.mulScalarWithVectors(expectation, ((-1)/numOfIteration));
            //perform the normalization on the current weight
            Vector oldWeights = MathHelpers.mulScalarWithVectors(currentWeights, (1-lambda*newEta));
            Vector updateArgument = MathHelpers.mulScalarWithVectors(expectation, newEta);
            //perform the update
            Vector W_Next = MathHelpers.add2Vectors(oldWeights, updateArgument);
            Logger.info(W_Next.toString());
            return W_Next;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}


