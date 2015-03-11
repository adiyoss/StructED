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

public class DirectLoss implements AlgorithmUpdateRule {

    //Singleton
    private static DirectLoss ourInstance = new DirectLoss();
    public static DirectLoss getInstance(ArrayList<Double> arguments) {
        if(arguments.size() != ConfigParameters.DL_PARAMS_SIZE){
            Logger.error(ErrorConstants.UPDATE_ARGUMENTS_ERROR);
            return null;
        }
        //initialize the parameters
        ourInstance.eta = arguments.get(0);
        ourInstance.epsilonArgMax = arguments.get(1);
        return ourInstance;
    }
    private DirectLoss(){}

    //Data members
    double eta;
    double epsilonArgMax;

	@Override
	//the first cell of the arguments attribute would be the eta value
	//the second cell of the arguments attribute would be the epsilon for the argmax
	public Vector update(Vector currentWeights, Example example, ClassifierData classifierData) {
		
		try{
            double algorithmIteration = classifierData.iteration;
            double newEta = eta/(Math.sqrt(algorithmIteration)*Math.abs(epsilonArgMax));

			//get the prediction
			String prediction = classifierData.predict.predictForTrain(example, currentWeights, example.getLabel(), classifierData, 0).firstKey();
			//get the direct prediction
			String predictionDirect = classifierData.predict.predictForTrain(example, currentWeights, example.getLabel(),classifierData, epsilonArgMax).firstKey();
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
