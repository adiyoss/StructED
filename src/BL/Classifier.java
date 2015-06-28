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

package BL;

import Constants.ErrorConstants;
import Data.CacheVowelData;
import Data.Entities.Example;
import Data.InstancesContainer;
import Data.Entities.PredictedLabels;
import Data.Entities.Vector;
import Data.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Classifier{

    public ClassifierData classifierData;
    private Vector avgWeights = new Vector();
    public ArrayList<Double> validationCumulativeLoss = new ArrayList<Double>();

	//train the algorithm and return the final weights
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

    public PredictedLabels test(Vector W, Example example, int returnAll) {
        return classifierData.inference.predictForTest(example, W, "", classifierData, returnAll);
    }

    public Vector getAvgWeights() {
        return avgWeights;
    }
}
