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

import java.util.List;

/**
 * Computing Character Error Rate loss function
 * Created by yossiadi on 7/18/15.
 */
public class TaskLossCER implements ITaskLoss {

    @Override
    public double computeTaskLoss(String predictClass, String actualClass, List<Double> params) {

        // get the minimum length
        int len = ((predictClass.length() < actualClass.length()) ? predictClass.length() : actualClass.length()) - 2;
        double loss = 0.0;

        // compute the character error rate
        for(int i=0 ; i<len ; i++){
            char ch_predict = predictClass.charAt(i+1);
            char ch_actual = actualClass.charAt(i+1);

            if(ch_actual != ch_predict)
                loss ++;
        }
        return loss/len;
    }
}
