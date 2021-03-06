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
import java.util.Map;

import com.structed.constants.Consts;
import com.structed.models.ClassifierData;
import com.structed.constants.ErrorConstants;
import com.structed.data.entities.Example;
import com.structed.data.entities.PredictedLabels;
import com.structed.data.entities.Vector;
import com.structed.data.Logger;
import com.structed.utils.MathHelpers;

/**
 * Conditional Random Fields
 * http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.26.803&rep=rep1&type=pdf
 */
public class CRF implements IUpdateRule {

    //data members
    double eta;
    double lambda;

	@Override
	public void init(ArrayList<Double> args) {
		if(args.size() != Consts.CRF_PARAMS_SIZE){
            Logger.error(ErrorConstants.UPDATE_ARGUMENTS_ERROR);
            return;
        }
        //initialize the parameters
        this.eta = args.get(0);
        this.lambda = args.get(1);
	}

	/**
	 * Implementation of the update rule
	 * @param currentWeights - the current weights
	 * @param example - a single example
	 * @param classifierData - all the additional data that needed such as: loss function, inference, etc.
     * @return the new set of weights
     */
	@Override
	//the first cell of the arguments attribute would be the mue value
	//the second cell of the arguments attribute would be the lambda
	public Vector update(Vector currentWeights, Example example, ClassifierData classifierData) {

		try{
            double algorithmIteration = classifierData.iteration;

            double newEta = eta/Math.sqrt(algorithmIteration);

			double denominator = 0;

            //the parameter 1 at the end is used to get all the predicted labels and not only the max one
            //it can be anything but -1 = Consts.ERROR_NUMBER
            PredictedLabels predictMap = classifierData.inference.predictForTest(example,currentWeights,example.getLabel(),classifierData,1);
			int k = -10;
			double maxVal = predictMap.firstEntry().getValue();

			for(Map.Entry<String, Double> entry : predictMap.entrySet()) {
				// keep the computation numerically stable
				double z = entry.getValue() - maxVal;
				if (z >= k)
					denominator += Math.pow(Math.E, (z));
			}

            Vector probExpectation = new Vector();
			boolean isFirst = true;

			for(Map.Entry<String, Double> entry : predictMap.entrySet())
			{
				// keep the computation numerically stable
				double prob = (entry.getValue() - maxVal) < k ? 0 : Math.pow(Math.E,(entry.getValue() - maxVal));
				prob = prob/denominator;
				if(isFirst){
					isFirst = false;
					probExpectation = MathHelpers.mulScalarWithVectors(classifierData.phi.convert(example, entry.getKey(), classifierData.kernel).getFeatures(), prob);
				} else
					probExpectation = MathHelpers.add2Vectors(probExpectation, MathHelpers.mulScalarWithVectors(classifierData.phi.convert(example,entry.getKey(),classifierData.kernel).getFeatures(), prob));
			}

            Vector updateArg = MathHelpers.subtract2Vectors(classifierData.phi.convert(example,example.getLabel(),classifierData.kernel).getFeatures(), probExpectation);
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
