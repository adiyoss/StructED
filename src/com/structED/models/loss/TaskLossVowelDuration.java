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

package com.structed.models.loss;

import com.structed.constants.Consts;

import java.util.List;

/**
 * Vowel Duration Task Loss example
 */
public class TaskLossVowelDuration implements ITaskLoss {

	@Override
    //max{0, |ys - ys'| - epsilon} + max{0, |ye - ye'| - epsilon}
	public double computeTaskLoss(String predictClass, String actualClass, List<Double> params) {
        try {
            Double epsilon_onset = params.get(0);
            Double epsilon_offset = params.get(1);

            String predictValues[] = predictClass.split(Consts.CLASSIFICATION_SPLITTER);
            String actualClassValues[] = actualClass.split(Consts.CLASSIFICATION_SPLITTER);

            double predictResStart = Double.parseDouble(predictValues[0]);
            double actualResStart = Double.parseDouble(actualClassValues[0]);

            double predictResEnd = Double.parseDouble(predictValues[1]);
            double actualResEnd = Double.parseDouble(actualClassValues[1]);

            double diffStart = Math.abs(predictResStart - actualResStart);
            double diffEnd = Math.abs(predictResEnd - actualResEnd);

            //subtract the epsilon
            double absRes = 0;
            if(diffStart >=  epsilon_onset)
                absRes += diffStart;
            if(diffEnd >= epsilon_offset)
                absRes += diffEnd;

            //get the max from the absolute result minus epsilon and 0
            return absRes;

        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
	}
}
