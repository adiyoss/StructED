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

package com.structed.models.inference;

import com.structed.models.ClassifierData;
import com.structed.constants.Consts;
import com.structed.constants.ErrorConstants;
import com.structed.data.entities.Example;
import com.structed.data.entities.PredictedLabels;
import com.structed.data.entities.Vector;
import com.structed.data.Logger;
import com.structed.utils.comperators.MapValueComparatorDescending;
import com.structed.utils.MathHelpers;

/**
 * Multi class inference
 */
public class InferenceMultiClassOld implements IInference {

    private int numOfClass = 10; //default MNIST

    /**
     * Constructor
     * @param numOfClasses - the number of classes
     */
    public InferenceMultiClassOld(int numOfClasses){
        this.numOfClass = numOfClasses;
    }

    @Override
    //predict function
    //argmax(yS,yE) (W*Phi(Xi,yS,yE)) + Task Loss
    //this function assumes that the argument vector has already been converted to phi vector
    //return null on error
    public PredictedLabels predictForTrain(Example vector, Vector W, String realClass, ClassifierData classifierData, double epsilonArgMax)
    {
        try{
            //validation
            if(vector.sizeOfVector<=0) {
                Logger.error(ErrorConstants.PHI_VECTOR_DATA);
                return null;
            }

            if(this.numOfClass > 0) {
                // get score for the first label
                String maxLabel = "0";
                Example phiData = classifierData.phi.convert(vector, String.valueOf(0), classifierData.kernel);
                double maxScore = MathHelpers.multipleVectors(W, phiData.getFeatures());

                if (epsilonArgMax != 0) {
                    //add the task loss
                    maxScore += epsilonArgMax * classifierData.taskLoss.computeTaskLoss(String.valueOf(0), realClass, classifierData.arguments);
                }

                for (int i = 1; i < numOfClass; i++) {
                    phiData = classifierData.phi.convert(vector, String.valueOf(i), classifierData.kernel);
                    //multiple the vectors
                    double tmp = MathHelpers.multipleVectors(W, phiData.getFeatures());

                    if (epsilonArgMax != 0) {
                        //add the task loss
                        tmp += epsilonArgMax * classifierData.taskLoss.computeTaskLoss(String.valueOf(i), realClass, classifierData.arguments);
                    }

                    // updates the max score and max label
                    if (tmp > maxScore){
                        maxLabel = String.valueOf(i);
                        maxScore = tmp;
                    }
                }

                PredictedLabels result = new PredictedLabels();
                result.put(maxLabel, maxScore);

                return result;

            }
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PredictedLabels predictForTest(Example vector, Vector W, String realClass, ClassifierData classifierData, int returnAll){

        if(returnAll != Consts.ERROR_NUMBER) {
            try {
                PredictedLabels tree = new PredictedLabels();

                //validation
                if (vector.sizeOfVector <= 0) {
                    Logger.error(ErrorConstants.PHI_VECTOR_DATA);
                    return null;
                }

                for (int i = 0; i < numOfClass; i++) {
                    Example phiData = classifierData.phi.convert(vector, String.valueOf(i), classifierData.kernel);

                    //multiple the vectors
                    double tmp = MathHelpers.multipleVectors(W, phiData.getFeatures());

                    //get the max value for the max classification
                    tree.put(String.valueOf(i), tmp);
                }

                MapValueComparatorDescending vc = new MapValueComparatorDescending(tree);
                PredictedLabels result = new PredictedLabels(vc);
                result.putAll(tree);

                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else
            return predictForTrain(vector, W, realClass, classifierData ,0);
    }

}
