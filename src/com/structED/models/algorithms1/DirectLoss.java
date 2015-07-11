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

package com.structed.models.algorithms1;

import java.util.ArrayList;

import com.structed.constants.Consts;
import com.structed.models.ClassifierData;
import com.structed.constants.ErrorConstants;
import com.structed.data.entities.Example;
import com.structed.data.entities.Vector;
import com.structed.data.Logger;
import com.structed.utils.MathHelpers;

/**
 * Direct Loss Minimization
 * http://papers.nips.cc/paper/4069-direct-loss-minimization-for-structured-prediction.pdf
 */
public class DirectLoss implements IUpdateRule {

    //data members
    double eta;
    double epsilonArgMax;

    @Override
    public void init(ArrayList<Double> args) {
        if(args.size() != Consts.DL_PARAMS_SIZE){
            Logger.error(ErrorConstants.UPDATE_ARGUMENTS_ERROR);
            return;
        }
        //initialize the parameters
        this.eta = args.get(0);
        this.epsilonArgMax = args.get(1);
    }


	@Override
	//the first cell of the arguments attribute would be the eta value
	//the second cell of the arguments attribute would be the epsilon for the argmax
	public Vector update(Vector currentWeights, Example example, ClassifierData classifierData) {
		
		try{
            double algorithmIteration = classifierData.iteration;
            double newEta = eta/(Math.sqrt(algorithmIteration)*Math.abs(epsilonArgMax));

			//get the prediction
			String prediction = classifierData.inference.predictForTrain(example, currentWeights, example.getLabel(), classifierData, 0).firstKey();
			//get the direct prediction
			String predictionDirect = classifierData.inference.predictForTrain(example, currentWeights, example.getLabel(),classifierData, epsilonArgMax).firstKey();
            Vector directLoss;

            Example phiPredictionNoLoss = classifierData.phi.convert(example,prediction,classifierData.kernel);
            Example phiPredictionDirect = classifierData.phi.convert(example,predictionDirect,classifierData.kernel);

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
