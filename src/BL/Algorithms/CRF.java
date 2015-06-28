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
import java.util.Map;

import BL.ClassifierData;
import Constants.ConfigParameters;
import Constants.ErrorConstants;
import Data.Entities.Example;
import Data.Entities.PredictedLabels;
import Data.Entities.Vector;
import Data.Logger;
import Helpers.MathHelpers;

//Conditional Random Fields
public class CRF implements IUpdateRule {

    //Data members
    double eta;
    double lambda;

	@Override
	public void init(ArrayList<Double> args) {
		if(args.size() != ConfigParameters.CRF_PARAMS_SIZE){
            Logger.error(ErrorConstants.UPDATE_ARGUMENTS_ERROR);
            return;
        }
        //initialize the parameters
        this.eta = args.get(0);
        this.lambda = args.get(1);
	}

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
