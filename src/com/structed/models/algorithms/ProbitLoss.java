/*
 * The MIT License (MIT)
 *
 * StructED - Machine Learning Package for Structured Prediction
 *
 * Copyright (c) 2015 Yossi Adi, E-Mail: yossiadidrum@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.structed.models.algorithms;

import java.util.ArrayList;
import java.util.Random;

import com.structed.constants.Consts;
import com.structed.models.ClassifierData;
import com.structed.constants.ErrorConstants;
import com.structed.data.entities.Example;
import com.structed.data.entities.Vector;
import com.structed.data.Logger;
import com.structed.utils.MathHelpers;

/**
 * Probit Loss Algorithm
 * http://cs.haifa.ac.il/~tamir/papers/KeshetMcHa10.pdf
 */
public class ProbitLoss implements IUpdateRule {

    //data members
    double lambda;
    double eta;
    double numOfIteration; //indicates the number of iterations
    double noiseAllVector; //indicates whether to noise all the weights vector or just one random element from it
    double mean; //indicates the mean to the noise to be generated
    double stDev; //indicates the standard deviation to the noise to be generated
    
    @Override
    public void init(ArrayList<Double> args) {
        if(args.size() != Consts.PL_PARAMS_SIZE){
            Logger.error(ErrorConstants.UPDATE_ARGUMENTS_ERROR);
            return;
        }
        //initialize the parameters
        this.eta = args.get(0);
        this.lambda = args.get(1);
        this.numOfIteration = args.get(2);
        this.noiseAllVector = args.get(3);
        this.mean = args.get(4);
        this.stDev = args.get(5);
    }

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
                    for(int index=0 ; index < classifierData.phi.getSizeOfVector(); index++)
                        epsilonVector.put(index, (stDev * random.nextGaussian() + mean));
                } else {
                    //only one location
                    int location = random.nextInt(example.sizeOfVector);
                    epsilonVector.put(location, (stDev * random.nextGaussian() + mean));
                }

				//W+EpsilonVector
                Vector U = MathHelpers.add2Vectors(currentWeights, epsilonVector);
                //get the noisy prediction
				String noisyPrediction = classifierData.inference.predictForTrain(example, U, example.getLabel(), classifierData, 1).firstKey();
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
            return W_Next;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}


