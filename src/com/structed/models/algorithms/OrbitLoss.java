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

import com.structed.constants.Consts;
import com.structed.models.ClassifierData;
import com.structed.constants.ErrorConstants;
import com.structed.data.entities.Example;
import com.structed.data.entities.Vector;
import com.structed.data.Logger;
import com.structed.utils.MathHelpers;

import java.util.ArrayList;

/**
 * Created by yossiadi on 5/4/15.
 * Orbit Loss algorithm
 */
public class OrbitLoss implements IUpdateRule {
    
    //data members
    double lambda;
    double eta;

    @Override
    public void init(ArrayList<Double> args) {
        if(args.size() != Consts.ORBIT_PARAMS_SIZE){
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
            //get the prediction
            String prediction;

            //if there's a problem with the predict return the previous weights
            try{
                prediction = classifierData.inference.predictForTrain(example,currentWeights,example.getLabel(),classifierData,1).firstKey();
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

            return MathHelpers.add2Vectors(firstArgument, secondArgument);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}