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

import com.structed.models.ClassifierData;
import com.structed.constants.Consts;
import com.structed.constants.ErrorConstants;
import com.structed.data.entities.Example;
import com.structed.data.entities.PredictedLabels;
import com.structed.data.entities.Vector;
import com.structed.data.Logger;
import com.structed.utils.MathHelpers;

import java.util.ArrayList;
import java.util.Map;

public class RankSVM implements IUpdateRule {

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
        this.lambda = args.get(0);
        this.eta = args.get(1);
    }

    @Override
    //in SVM the lambda value would be in the first cell of the arguments attribute
    //the second cell of the arguments attribute would be the eta
    public Vector update(Vector currentWeights, Example example, ClassifierData classifierData) {

        try{
            double algorithmIteration = classifierData.iteration;

            //get the prediction
            PredictedLabels prediction;
            //if there's a problem with the predict return the previous weights
            try{
                prediction = classifierData.inference.predictForTrain(example,currentWeights,example.getLabel(),classifierData,1);
            }catch (Exception e){
                return currentWeights;
            }
            Vector result = currentWeights;

            for(Map.Entry<String,Double> entry : prediction.entrySet()) {

                if(entry.getKey().equalsIgnoreCase(Consts.CLASSIFICATION_SPLITTER))
                    continue;
                String[] predictLabels = entry.getKey().split(Consts.CLASSIFICATION_SPLITTER);
                if(predictLabels.length != 2)
                    continue;
                if(predictLabels[0].equalsIgnoreCase("") || predictLabels[1].equalsIgnoreCase(""))
                    continue;

                Example phiRealLabel = classifierData.phi.convert(example, predictLabels[0], classifierData.kernel);
                Example phiPrediction = classifierData.phi.convert(example, predictLabels[1], classifierData.kernel);
                Vector phiDifference = MathHelpers.subtract2Vectors(phiRealLabel.getFeatures(), phiPrediction.getFeatures());

                double newEta = eta/Math.sqrt(algorithmIteration);
                double coefficientFirstArgument = (1-(lambda*newEta));

                Vector firstArgument = MathHelpers.mulScalarWithVectors(currentWeights, coefficientFirstArgument);
                Vector secondArgument = MathHelpers.mulScalarWithVectors(phiDifference, newEta);

                result = MathHelpers.add2Vectors(firstArgument, secondArgument);
            }
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
