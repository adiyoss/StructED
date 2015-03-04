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
 * Created by adiyoss on 3/4/15.
 */
public class Perceptron implements AlgorithmUpdateRule{

    //Singleton
    private static Perceptron ourInstance = new Perceptron();
    public static Perceptron getInstance(ArrayList<Double> arguments) {
        if(arguments.size() != ConfigParameters.P_PARAMS_SIZE){
            Logger.error(ErrorConstants.UPDATE_ARGUMENTS_ERROR);
            return null;
        }

        return ourInstance;
    }
    private Perceptron() {
    }

    @Override
    // Structured Perceptron has no attributes
    public Vector update(Vector currentWeights, Example example, ClassifierData classifierData) {
        try{
            String prediction;
            //if there's a problem with the predict return the previous weights
            try{
                prediction = classifierData.predict.predictForTrain(example,currentWeights,example.getLabel(),classifierData,1).firstKey();
            } catch (Exception e){
                return currentWeights;
            }

            Example phiRealLabel = classifierData.phi.convert(example,example.getLabel(), classifierData.kernel);
            Example phiPrediction = classifierData.phi.convert(example,prediction, classifierData.kernel);

            //compute the phi difference
            Vector phiDifference = MathHelpers.subtract2Vectors(phiRealLabel.getFeatures(), phiPrediction.getFeatures());
            //update the weights vector
            Vector result = MathHelpers.add2Vectors(currentWeights, phiDifference);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

