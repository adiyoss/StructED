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
import com.structed.utils.MathHelpers;
import com.structed.utils.comperators.MapValueComparatorAscending;
import com.structed.utils.comperators.MapValueComparatorDescending;

import java.util.TreeMap;

/**
 * Vowel Duration inference example
 */
public class InferenceVowelDurationData implements IInference {

    //predict function
    //argmax(yS,yE) (W*Phi(Xi,yS,yE)) + Task Loss
    //this function assumes that the argument vector has already been converted to phi vector
    //return null on error
    public PredictedLabels predictForTrain(Example vector, Vector W, String realClass, ClassifierData classifierData, double epsilonArgMax)
    {
        try{
            double maxVal = 0;
            String maxLabel = "";
            boolean isFirst = true;

            //validation
            if(vector.sizeOfVector<=0)
            {
                Logger.error(ErrorConstants.PHI_VECTOR_DATA);
                return null;
            }

            //loop over all the classifications of this specific example
            for(int i=Consts.MIN_GAP_START ; i<vector.sizeOfVector-(Consts.MIN_GAP_END) ; i++)
            {
                for(int j=i+Consts.MIN_VOWEL ; j<i+Consts.MAX_VOWEL ; j++)
                {
                    if(j>vector.sizeOfVector-(Consts.MIN_GAP_END))
                        break;

                    Example phiData = classifierData.phi.convert(vector,(i+1)+Consts.CLASSIFICATION_SPLITTER+(j+1),classifierData.kernel);
                    //multiple the vectors
                    double tmp = MathHelpers.multipleVectors(W, phiData.getFeatures());

                    if(epsilonArgMax != 0){
                        //add the task loss
                        tmp += epsilonArgMax*classifierData.taskLoss.computeTaskLoss((i+1)+Consts.CLASSIFICATION_SPLITTER+(j+1), realClass, classifierData.arguments);
                    }

                    if(isFirst) {
                        maxLabel = (i + 1) + Consts.CLASSIFICATION_SPLITTER + (j + 1);
                        maxVal = tmp;
                        isFirst = false;
                    }
                    else if(tmp > maxVal) {
                        maxLabel = (i + 1) + Consts.CLASSIFICATION_SPLITTER + (j + 1);
                        maxVal = tmp;
                    }
                }
            }

            PredictedLabels result = new PredictedLabels();
            result.put(maxLabel, maxVal);

            return result;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public PredictedLabels predictForTest(Example vector, Vector W, String realClass, ClassifierData classifierData, int returnAll) {
        switch (returnAll) {
            case 0:
                return predictForTrain(vector, W, realClass, classifierData, 0.0);
            case 1:
                try {
                    TreeMap<String, Double> tree = new TreeMap<String, Double>();

                    //validation
                    if (vector.sizeOfVector <= 0) {
                        Logger.error(ErrorConstants.PHI_VECTOR_DATA);
                        return null;
                    }

                    //loop over all the classifications of this specific example
                    for (int i = Consts.MIN_GAP_START; i < vector.sizeOfVector - (Consts.MIN_GAP_END); i++) {
                        for (int j = i + Consts.MIN_VOWEL; j < i + Consts.MAX_VOWEL; j++) {
                            if (j > vector.sizeOfVector - (Consts.MIN_GAP_END))
                                break;

                            Example phiData = classifierData.phi.convert(vector, (i + 1) + Consts.CLASSIFICATION_SPLITTER + (j + 1), classifierData.kernel);
                            //multiple the vectors
                            double tmp = MathHelpers.multipleVectors(W, phiData.getFeatures());
                            tree.put((i + 1) + Consts.CLASSIFICATION_SPLITTER + (j + 1), tmp);
                        }
                    }

                    MapValueComparatorDescending vc = new MapValueComparatorDescending(tree);
                    PredictedLabels result = new PredictedLabels(vc);
                    result.putAll(tree);

                    return result;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            default:
                return predictForTrain(vector, W, realClass, classifierData, 0.0);
        }
    }
}
