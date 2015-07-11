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

package com.structed.models;

import com.structed.constants.ErrorConstants;
import com.structed.data.CacheVowelData;
import com.structed.data.entities.Example;
import com.structed.data.InstancesContainer;
import com.structed.data.entities.PredictedLabels;
import com.structed.data.entities.Vector;
import com.structed.data.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Classifier class is responsible for the training the prediction
 */
public class Classifier{

    public ClassifierData classifierData;
    private Vector avgWeights = new Vector();
    public ArrayList<Double> validationCumulativeLoss = new ArrayList<Double>();

    /**
     * train the algorithm and return the final weights
     * @param W model weights
     * @param trainInstances training examples
     * @param params a given parameters for the model
     * @param developInstances development set, if set to null the model skip the validation phase
     * @param isAvg if this parameter set to 1 the function will average the model weights, otherwise it will return only the last one
     * @return the final model's weights
     * @throws Exception
     */
	public Vector train(Vector W, InstancesContainer trainInstances, List<Double> params, InstancesContainer developInstances, int isAvg) throws Exception
	{
        //validation
        if(trainInstances == null){
            Logger.error(ErrorConstants.PHI_DATA_ERROR);
            return null;
        }

        //counter - used for debug
        Vector best_W = new Vector(W);
        double bestCumulativeLoss = -1;
        classifierData.arguments = params;

        Logger.infoTime("Start Training...");
		//loop over all the training set
        for(int i=0 ; i<trainInstances.getSize() ; i++)
		{
            //************* PRINTINGS ****************//
            String output = "";
            classifierData.iteration++;
            Example x_train = trainInstances.getInstance(i);
            if(x_train == null) continue;
            if(x_train.path != null)
                output += "Processing example: " + x_train.path + ", Number: " + (i + 1)+" ";
            else
                output += "Processing example number: " + (i + 1)+" ";
            //****************************************//

            //############################################################################################################//
            //##############################################  VALIDATION  ################################################//
            if(developInstances != null) {
                double cumulative_loss = 0;
                for (int idx = 0; idx < developInstances.getSize(); idx++) {
                    Example x_validate = developInstances.getInstance(idx);
                    PredictedLabels scores = test(W, x_validate, -1);
                    String y_hat = scores.firstKey();
                    String y = x_validate.getLabel();
                    cumulative_loss += classifierData.taskLoss.computeTaskLoss(y, y_hat, params);
                }
                cumulative_loss /= developInstances.getSize();
                if (bestCumulativeLoss == -1 || cumulative_loss < bestCumulativeLoss) {
                    bestCumulativeLoss = cumulative_loss;
                    best_W = (Vector) W.clone();
                }
                //************* PRINTINGS ****************//
                validationCumulativeLoss.add(cumulative_loss);
                output += "Cumulative loss on validation set: " + (Math.floor(cumulative_loss * 100) / 100) + ", Best cumulative loss on validation set: " + (Math.floor(bestCumulativeLoss * 100) / 100);
            }
            //#########################################################################################################//

			//update W
			W = classifierData.updateRule.update(W, x_train, classifierData);
			if(W == null)
				return null;

            //############################################################################################################//
            //######################################  UPDATE AVERAGE WEIGHTS  ############################################//
            if(isAvg == 1) {
                if (avgWeights.size() != 0) {
                    for (Map.Entry<Integer, Double> entry : avgWeights.entrySet())
                        avgWeights.put(entry.getKey(), (entry.getValue() * (classifierData.iteration - 1) + W.get(entry.getKey())) / classifierData.iteration);
                    for (Map.Entry<Integer, Double> entry : W.entrySet()) {
                        if (!avgWeights.containsKey(entry.getKey()))
                            avgWeights.put(entry.getKey(), entry.getValue() / classifierData.iteration);
                    }
                } else
                    avgWeights = (Vector) W.clone();
            }
            //############################################################################################################//
            CacheVowelData.clearCacheValues();
            //===================================//

            Logger.progressMessage(output);
		}

        if(developInstances != null)
		    return best_W;
        return W;
	}

    /**
     * predict
     * @param W the model weights
     * @param example an example to predict
     * @param returnAll can be used by the inference1 to return the desired number of examples
     * @return PredictedLabels object containing the sorted predictions
     */
    public PredictedLabels test(Vector W, Example example, int returnAll) {
        return classifierData.inference.predictForTest(example, W, "", classifierData, returnAll);
    }

    /**
     * a getter for the averaged weights
     * @return
     */
    public Vector getAvgWeights() {
        return avgWeights;
    }
}
