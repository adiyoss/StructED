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
import com.structed.data.entities.Example;
import com.structed.data.entities.PredictedLabels;
import com.structed.data.entities.Vector;
import com.structed.data.Logger;
import com.structed.utils.comperators.MapValueComparatorAscending;
import com.structed.utils.MathHelpers;

import java.util.TreeMap;

public class InferenceRanking implements IInference {

    public PredictedLabels predictForTrain(Example vector, Vector W, String realClass, ClassifierData classifierData, double epsilonArgMax)
    {
        try{
            PredictedLabels tree = new PredictedLabels();
            String min_label_2 = "";
            double min_score_2 = 0;

            String max_label_1 = "";
            double max_score_1 = 0;

            String min_label_1 = "";
            double min_score_1 = 0;

            String max_label_0 = "";
            double max_score_0 = 0;


            boolean isFirst_2 = true;
            boolean isFirst_1 = true;
            boolean isFirst_0 = true;

            for(int i=0 ; i<vector.getFeatures2D().size() ; i++){
                double tmp;
                switch (vector.getLabels2D().get(i)){
                    case 2:
                        if(isFirst_2){
                            min_label_2 = String.valueOf(i);
                            min_score_2 = MathHelpers.multipleVectors(W,vector.getFeatures2D().get(i));
                            isFirst_2 = false;
                        } else {
                            tmp = MathHelpers.multipleVectors(W,vector.getFeatures2D().get(i));
                            if(tmp < min_score_2){
                                min_label_2 = String.valueOf(i);
                                min_score_2 = tmp;
                            }
                        }
                        break;
                    case 1:
                        if(isFirst_1){
                            max_label_1 = min_label_1 = String.valueOf(i);
                            max_score_1 = min_score_1 = MathHelpers.multipleVectors(W,vector.getFeatures2D().get(i));
                            isFirst_1 = false;
                        } else {
                            tmp = MathHelpers.multipleVectors(W,vector.getFeatures2D().get(i));

                            if(tmp > max_score_1){
                                max_label_1 = String.valueOf(i);
                                max_score_1 = tmp;
                            }

                            if(tmp < min_score_1){
                                min_label_1 = String.valueOf(i);
                                min_score_1 = tmp;
                            }
                        }
                        break;
                    case 0:
                        if(isFirst_0){
                            max_label_0 = String.valueOf(i);
                            max_score_0 = MathHelpers.multipleVectors(W,vector.getFeatures2D().get(i));
                            isFirst_0 = false;
                        } else {
                            tmp = MathHelpers.multipleVectors(W,vector.getFeatures2D().get(i));
                            if(tmp > max_score_0){
                                max_label_0 = String.valueOf(i);
                                max_score_0 = tmp;
                            }
                        }
                        break;
                    default:
                        break;
                }
            }

            double loss_1 = classifierData.taskLoss.computeTaskLoss(String.valueOf(min_score_2),String.valueOf(max_score_1),classifierData.arguments);
            double loss_2 = classifierData.taskLoss.computeTaskLoss(String.valueOf(min_score_1),String.valueOf(max_score_0),classifierData.arguments);
//            double loss_3 = classifierData.ITaskLoss.computeTaskLoss(String.valueOf(min_score_2),String.valueOf(max_score_0),0);

            Logger.info("Before: " + loss_1);
            Logger.info("Before: "+loss_2);
            if(loss_1 > 0)
                tree.put(min_label_2+Consts.CLASSIFICATION_SPLITTER+max_label_1, -2.0);

            if(loss_2 > 0)
                tree.put(min_label_1+Consts.CLASSIFICATION_SPLITTER+max_label_0, -1.0);

//            if(loss_3 > 0)
//                tree.put(min_label_2+Consts.CLASSIFICATION_SPLITTER+max_label_0, 0.0);

            MapValueComparatorAscending vc = new MapValueComparatorAscending(tree);
            PredictedLabels result = new PredictedLabels(vc);
            result.putAll(tree);

            return result;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public PredictedLabels predictForTest(Example vector, Vector W, String realClass, ClassifierData classifierData, int returnAll)
    {
        try{
            TreeMap<String,Double> tree = new TreeMap<String, Double>();

            for(int i=0 ; i<vector.getFeatures2D().size() ; i++){
                double rankValue = MathHelpers.multipleVectors(W,vector.getFeatures2D().get(i));
                tree.put(String.valueOf(i),rankValue);
            }

            MapValueComparatorAscending vc = new MapValueComparatorAscending(tree);
            PredictedLabels result = new PredictedLabels(vc);
            result.putAll(tree);

            return result;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}