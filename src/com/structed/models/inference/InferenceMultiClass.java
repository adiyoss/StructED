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

import com.structed.constants.Consts;
import com.structed.constants.ErrorConstants;
import com.structed.data.Logger;
import com.structed.data.entities.Example;
import com.structed.data.entities.PredictedLabels;
import com.structed.data.entities.Vector;
import com.structed.models.ClassifierData;
import com.structed.utils.MathHelpers;
import com.structed.utils.comperators.MapValueComparatorDescending;

/**
 * Multi class inference
 * Created by yossiadi on 7/17/15.
 */
public class InferenceMultiClass implements IInference {

    private int numOfClass = 10; //default for MNIST

    /**
     * Constructor
     * @param numOfClasses - the number of classes
     */
    public InferenceMultiClass(int numOfClasses){
        this.numOfClass = numOfClasses;
    }

    @Override
    public PredictedLabels predictForTrain(Example vector, Vector W, String realClass, ClassifierData classifierData, double epsilonArgMax) {
        try{
            //validation
            if(vector.sizeOfVector<=0) {
                Logger.error(ErrorConstants.PHI_VECTOR_DATA);
                return null;
            }

            if(this.numOfClass > 0) {
                int maxFeatures = classifierData.phi.getSizeOfVector() / this.numOfClass;
                double[] scores = new double[this.numOfClass];
                for(Integer key : vector.getFeatures().keySet()){
                    for(int i=0 ; i<this.numOfClass ; i++){
                        Double wVal = W.get(key + i * maxFeatures);
                        if(wVal != null)
                            scores[i] += wVal * vector.getFeatures().get(key);
                    }
                }

                String maxLabel = "0";
                double maxScore = scores[0];
                if (epsilonArgMax != 0)
                    maxScore += epsilonArgMax * classifierData.taskLoss.computeTaskLoss(String.valueOf("0"), realClass, classifierData.arguments);

                for(int i=1 ; i<this.numOfClass ; i++) {
                    double tmp = scores[i];

                    if (epsilonArgMax != 0)
                        //add the task loss
                        tmp += epsilonArgMax * classifierData.taskLoss.computeTaskLoss(String.valueOf(i), realClass, classifierData.arguments);

                    if (tmp > maxScore) {
                        maxScore = tmp;
                        maxLabel = String.valueOf(i);
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
    public PredictedLabels predictForTest(Example vector, Vector W, String realClass, ClassifierData classifierData, int returnAll) {
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
