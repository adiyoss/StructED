/*
 * StructED - Machine Learning, Structured Prediction Package written in Java.
 * Copyright (C) 2015 Yossi Adi, E-Mail: yossiadidrum@gmail.com
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

import BL.ClassifierData;
import Constants.ConfigParameters;
import Constants.ErrorConstants;
import Data.Entities.Example;
import Data.Entities.Vector;
import Data.Logger;
import Helpers.MathHelpers;

import java.util.ArrayList;

/**
 * Created by yossiadi on 5/4/15.
 */
public class OrbitLoss implements AlgorithmUpdateRule{

    //Singleton
    private static OrbitLoss ourInstance = new OrbitLoss();
    public static OrbitLoss getInstance(ArrayList<Double> arguments) {
        if(arguments.size() != ConfigParameters.SVM_PARAMS_SIZE){
            Logger.error(ErrorConstants.UPDATE_ARGUMENTS_ERROR);
            return null;
        }
        //initialize the parameters
        ourInstance.lambda = arguments.get(0);
        ourInstance.eta = arguments.get(1);

        return ourInstance;
    }

    private OrbitLoss() {
    }

    //Data members
    double lambda;
    double eta;

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
                prediction = classifierData.predict.predictForTrain(example,currentWeights,example.getLabel(),classifierData,1).firstKey();
            } catch (Exception e){
                return currentWeights;
            }

            // feature functions
            Example phiRealLabel = classifierData.phi.convert(example,example.getLabel(), classifierData.kernel);
            Example phiPrediction = classifierData.phi.convert(example, prediction, classifierData.kernel);

            //compute the phi difference
            Vector phiDifference = MathHelpers.subtract2Vectors(phiRealLabel.getFeatures(), phiPrediction.getFeatures());

            // get the loss value and update the learning rate
            double loss = classifierData.taskLoss.computeTaskLoss(prediction, example.getLabel(), classifierData.arguments);
            double newEta = eta;
            double coefficientFirstArgument = (1-(lambda*newEta));

            // update the weights
            Vector firstArgument = MathHelpers.mulScalarWithVectors(currentWeights, coefficientFirstArgument);
            Vector secondArgument = MathHelpers.mulScalarWithVectors(phiDifference, newEta*loss);
            Vector result = MathHelpers.add2Vectors(firstArgument, secondArgument);

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
