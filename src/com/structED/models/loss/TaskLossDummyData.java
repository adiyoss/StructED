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
import com.structed.data.Logger;

import java.util.List;

public class TaskLossDummyData implements ITaskLoss {

	@Override
	//max{0,|ys-ye - y's-y'e|-epsilon}
	public double computeTaskLoss(String predictClass, String actualClass, List<Double> params) {
		try {
			double epsilon = params.get(0);
			String predictValues[] = predictClass.split(Consts.CLASSIFICATION_SPLITTER);
			String actualClassValues[] = actualClass.split(Consts.CLASSIFICATION_SPLITTER);

			//calculate difference of each classification
			double predictRes = Double.parseDouble(predictValues[0]) - Double.parseDouble(predictValues[1]);
			double actualRes = Double.parseDouble(actualClassValues[0]) - Double.parseDouble(actualClassValues[1]);

			//subtract the epsilon
			double absRes = Math.abs(predictRes - actualRes) - epsilon;

			//get the max from the absolute result minus epsilon and 0
			if (absRes > 0)
				return absRes;
			return 0;
		} catch (Exception e) {
			Logger.error("Problem with the loss function parameters.");
			return 0;
		}
	}
}
