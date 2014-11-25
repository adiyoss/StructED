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

package BL.Prediction;

import BL.ClassifierData;
import Constants.Consts;
import Constants.ErrorConstants;
import Data.Entities.Example;
import Data.Entities.PredictedLabels;
import Data.Entities.Vector;
import Helpers.Comperators.MapValueComparatorDescending;
import Helpers.MathHelpers;

public class PredictionDummyData implements Prediction {

    //predict function
    //argmax(yS,yE) (W*Phi(Xi,yS,yE)) + Task Loss
    //this function assumes that the argument vector has already been converted to phi vector
    //return null on error
    public PredictedLabels predictForTrain(Example vector, Vector W, String realClass, ClassifierData classifierData, double epsilonArgMax)
    {
        try{
            PredictedLabels tree = new PredictedLabels();

            //validation
            if(vector.sizeOfVector<=0)
            {
                System.err.println(ErrorConstants.PHI_VECTOR_DATA);
                return null;
            }

            //loop over all the classifications of this specific example
            for(int i=Consts.MIN_GAP_START_DUMMY-1 ; i<vector.sizeOfVector-Consts.MIN_GAP_END_DUMMY ; i++)
            {
                for(int j=i+1 ; j<vector.sizeOfVector- Consts.MIN_GAP_END_DUMMY ; j++)
                {
                    Example phiData = classifierData.phi.convert(vector,(i+1)+ Consts.CLASSIFICATION_SPLITTER+(j+1),classifierData.kernel);
                    //multiple the vectors
                    double tmp = MathHelpers.multipleVectors(W, phiData.getFeatures());

                    if(epsilonArgMax != 0){
                        //add the task loss
                        tmp += epsilonArgMax*classifierData.taskLoss.computeTaskLoss((i+1)+ Consts.CLASSIFICATION_SPLITTER+(j+1), realClass, classifierData.arguments);
                    }

                    //get the max value for the max classification
                    tree.put((i+1)+ Consts.CLASSIFICATION_SPLITTER+(j+1),tmp);
                }
            }

            MapValueComparatorDescending vc = new MapValueComparatorDescending(tree);
            PredictedLabels result = new PredictedLabels(vc);
            result.putAll(tree);

            return result;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public PredictedLabels predictForTest(Example vector, Vector W, String realClass, ClassifierData classifierData, int returnAll)
    {
        return predictForTrain(vector,W,realClass,classifierData,0);
    }
}
