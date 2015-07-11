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
 * Structured SVM
 * http://link.springer.com/article/10.1007/s10107-010-0420-4#page-1
 */
public class SVM implements IUpdateRule {

    //data members
    double lambda;
    double eta;

    @Override
    public void init(ArrayList<Double> args) {
        if(args.size() != Consts.SVM_PARAMS_SIZE){
            Logger.error(ErrorConstants.UPDATE_ARGUMENTS_ERROR);
            return;
        }
        //initialize the parameters
        this.eta = args.get(0);
        this.lambda = args.get(1);
    }

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
			    prediction = classifierData.inference.predictForTrain(example,currentWeights,example.getLabel(),classifierData,1).firstKey();
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
