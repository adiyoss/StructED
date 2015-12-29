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

import com.structed.constants.Char2Idx;
import com.structed.constants.Consts;
import com.structed.constants.ErrorConstants;
import com.structed.data.Factory;
import com.structed.data.Logger;
import com.structed.data.entities.Example;
import com.structed.data.entities.PredictedLabels;
import com.structed.data.entities.Vector;
import com.structed.models.ClassifierData;
import com.structed.utils.MathHelpers;

import java.util.ArrayList;


/**
 * Created by yossiadi on 19/12/2015.
 *
 */
public class InferenceOCR implements IInference {

    private int numOfChars = Char2Idx.char2id.size(); //the number of characters in english plus a special note for word start and end
    private int maxFeature = 127; // TODO make this parameter the same as in the phi function
    //predict function
    //argmax(yS,yE) (W*Phi(Xi,yS,yE)) + Task Loss
    //this function assumes that the argument vector has already been converted to phi vector
    //return null on error
    @Override
    public PredictedLabels predictForTrain(Example vector, Vector W, String realClass, ClassifierData classifierData, double epsilonArgMax) {
        try{
            //validation
            if(vector.sizeOfVector<=0) {
                Logger.error(ErrorConstants.PHI_VECTOR_DATA);
                return null;
            }
            int label_size = vector.getLabel().length()-2;
            int num_of_eng_char = this.numOfChars-1;
            char prev_char;
            char curr_char;
            double[][] D_S = new double[label_size][num_of_eng_char]; // use for scores
            int[][] D_PI = new int[label_size][num_of_eng_char]; // use to save the prev char index

            // ================= get the first prediction ================= //
            final Vector vec_2_proc = vector.getFeatures2D().get(0);
            Example curr_e = Factory.getExample(1);
            ArrayList<Vector> l_vecs = new ArrayList<Vector>(){{add(vec_2_proc);}};
            curr_e.setFeatures2D(l_vecs);

            prev_char = Char2Idx.id2char.get(0);
            for (int i = 0; i < num_of_eng_char; i++) {
                curr_char = Char2Idx.id2char.get(i+1);
                String y_hat = (prev_char + "" + curr_char);
                Example phiData = classifierData.phi.convert(curr_e, y_hat, classifierData.kernel);
                double s = MathHelpers.multipleVectors(W, phiData.getFeatures());
                D_S[0][i] = s;
                D_PI[0][i] = 0;
            }
            // ============================================================ //

            // ========================= recursion ======================== //
            for (int i=1 ; i<label_size; i++) {
                final Vector f = vector.getFeatures2D().get(i);
                Example x = Factory.getExample(1);
                ArrayList<Vector> vs = new ArrayList<Vector>(){{add(f);}};
                x.setFeatures2D(vs);
                for (int j=0 ; j<num_of_eng_char ; j++) {
                    curr_char = Char2Idx.id2char.get(j+1);
                    double tmp_d;
                    double d_best = -1;
                    int i_best = -1;
                    for (int k=0 ; k<num_of_eng_char ; k++) {
                        prev_char = Char2Idx.id2char.get(k+1);
                        String target = (prev_char + "" + curr_char);
                        Example phiData = classifierData.phi.convert(x, target, classifierData.kernel);
                        tmp_d = D_S[i-1][k] + MathHelpers.multipleVectors(W, phiData.getFeatures());
                        if(d_best < tmp_d){
                            d_best = tmp_d;
                            i_best = k;
                        }
                    }
                    D_S[i][j] = d_best;
                    D_PI[i][j] = i_best;
                }
            }
            // ============================================================ //

            // ======================== back-track ======================== //
            int[] y_hat = new int[label_size];
            double d_best = -1;

            for(int i=0; i<num_of_eng_char ; i++){
                if(d_best < D_S[label_size-1][i]) {
                    y_hat[label_size - 1] = i;
                    d_best = D_S[label_size-1][i];
                }
            }
            for(int i=label_size-2; i>=0 ; i--) {
                y_hat[i] = D_PI[i + 1][y_hat[i + 1]];
            }
            // ============================================================ //

            // =================== convert idx to string ================== //
            char maxLabel[] = new char[vector.getLabel().length()];
            maxLabel[0] = Char2Idx.id2char.get(0);
            maxLabel[vector.getLabel().length()-1] = Char2Idx.id2char.get(0);
            Double maxScore = D_S[label_size-1][y_hat[label_size-1]];
            for (int i=0 ; i<y_hat.length ; i++)
                maxLabel[i+1] = Char2Idx.id2char.get(y_hat[i]+1);
            // ============================================================ //

            PredictedLabels result = new PredictedLabels();
            result.put(String.valueOf(maxLabel), maxScore);

            return result;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PredictedLabels predictForTest(Example vector, Vector W, java.lang.String realClass, ClassifierData classifierData, int returnAll) {
        return predictForTrain(vector, W, realClass, classifierData, 0);
    }
}

