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

import com.structed.constants.Consts;
import com.structed.models.ClassifierData;
import com.structed.constants.ErrorConstants;
import com.structed.data.entities.Example;
import com.structed.data.entities.Vector;
import com.structed.data.Logger;
import com.structed.utils.MathHelpers;

/**
 * Passive Aggressive algorithm
 * http://machinelearning.wustl.edu/mlpapers/paper_files/NIPS2003_LT21.pdf
 */
public class PassiveAggressive implements IUpdateRule {
    
    //data members
    double cValue;

    @Override
    public void init(ArrayList<Double> args){
        if(args.size() != Consts.PA_PARAMS_SIZE){
            Logger.error(ErrorConstants.UPDATE_ARGUMENTS_ERROR);
            return;
        }
        //initialize the parameters
        this.cValue = args.get(0);
    }

	/**
	 * Implementation of the update rule
	 * @param currentWeights - the current weights
	 * @param example - a single example
	 * @param classifierData - all the additional data that needed such as: loss function, inference, etc.
	 * @return the new set of weights
	 */
    @Override
	//the first cell of the arguments attribute would be the C value
	public Vector update(Vector currentWeights, Example example, ClassifierData classifierData) {
		
		try{
			//get the prediction
			String prediction = classifierData.inference.predictForTrain(example, currentWeights, example.getLabel(), classifierData,1).firstKey();

            Example phiRealLabel = classifierData.phi.convert(example, example.getLabel(), classifierData.kernel);
            Example phiPrediction = classifierData.phi.convert(example, prediction, classifierData.kernel);
			
			//compute the phi difference
            Vector phiDifference = MathHelpers.subtract2Vectors(phiRealLabel.getFeatures(), phiPrediction.getFeatures());
			
			double taskLossValue = classifierData.taskLoss.computeTaskLoss(prediction, example.getLabel(), classifierData.arguments);
			double multipleVectors = MathHelpers.multipleVectors(currentWeights , phiDifference);
			double denominator = MathHelpers.multipleVectors(phiDifference, phiDifference);

			double tau = 0;
			if(denominator != 0){
				tau = (taskLossValue - multipleVectors)/denominator;
			}

			if(tau > cValue)
				tau = cValue;

			classifierData.verbose = String.format(", Tau = %.3f, loss = %.3f, denominator = %.3f", tau, taskLossValue, denominator);

            if(tau < 0) {
				Logger.error("Y: "+example.getLabel()+" ,Y_hat: "+prediction);
				Logger.error("Loss < 0!, prediction search can't get to the target label.");
			}

            Vector updateVector = MathHelpers.mulScalarWithVectors(phiDifference, tau);
            Vector result = MathHelpers.add2Vectors(currentWeights, updateVector);

			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}


