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

import BL.ClassifierData;
import Constants.ConfigParameters;
import Constants.ErrorConstants;
import Data.Entities.Example;
import Data.Entities.Vector;
import Data.Logger;
import Helpers.MathHelpers;

public class PassiveAggressive implements IUpdateRule {
    
    //Data members
    double cValue;

    @Override
    public void init(ArrayList<Double> args){
        if(args.size() != ConfigParameters.PA_PARAMS_SIZE){
            Logger.error(ErrorConstants.UPDATE_ARGUMENTS_ERROR);
            return;
        }
        //initialize the parameters
        this.cValue = args.get(0);
    }

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

            if(tau < 0)
                Logger.error("Loss < 0!, prediction search can't get to the target label.");

            Vector updateVector = MathHelpers.mulScalarWithVectors(phiDifference, tau);
            Vector result = MathHelpers.add2Vectors(currentWeights, updateVector);

			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}


