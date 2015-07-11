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
import com.structed.data1.entities.Example;
import com.structed.data1.entities.Vector;
import com.structed.data1.Logger;
import com.structed.utils.MathHelpers;

/**
 * Ramp Loss Minimization
 * http://papers.nips.cc/paper/4268-generalization-bounds-and-consistency-for-latent-structural-probit-and-ramp-loss.pdf
 */
public class RampLoss implements IUpdateRule {
    
    //data1 members
    double lambda;
    double eta;

    @Override
    public void init(ArrayList<Double> args) {
        if(args.size() != Consts.RL_PARAMS_SIZE){
            Logger.error(ErrorConstants.UPDATE_ARGUMENTS_ERROR);
            return;
        }
        //initialize the parameters
        this.eta = args.get(0);
        this.lambda = args.get(1);
    }

	@Override
	//the eta variable in is the first cell
	//the lambda variable s in the second cell
	public Vector update(Vector currentWeights, Example example, ClassifierData classifierData) {
		
		try{
            double algorithmIteration = classifierData.iteration;
            double newEta = eta/Math.sqrt(algorithmIteration);

			//get the prediction
			String prediction = classifierData.inference.predictForTrain(example, currentWeights, example.getLabel(), classifierData, 0).firstKey();
			//get the prediction with the task loss
			String predictionLoss = classifierData.inference.predictForTrain(example, currentWeights, example.getLabel(), classifierData, 1).firstKey();

            Example phiPredictionNoLoss = classifierData.phi.convert(example,prediction,classifierData.kernel);
            Example phiPredictionWithLoss= classifierData.phi.convert(example,predictionLoss,classifierData.kernel);

			
			//compute the ramp loss
            Vector rampLoss = MathHelpers.subtract2Vectors(phiPredictionNoLoss.getFeatures(), phiPredictionWithLoss.getFeatures());

            //perform the update
            Vector lossArg = MathHelpers.mulScalarWithVectors(rampLoss, newEta);
            Vector newWeights = MathHelpers.mulScalarWithVectors(currentWeights, 1-lambda*newEta);
            Vector W_Next = MathHelpers.add2Vectors(newWeights, lossArg);
			
			return W_Next;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
