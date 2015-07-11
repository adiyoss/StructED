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

package com.structed.data.featurefunctions;

import com.structed.models.kernels1.IKernel;
import com.structed.constants.Consts;
import com.structed.constants.ErrorConstants;
import com.structed.data.entities.Example;
import com.structed.data.entities.Vector;
import com.structed.data.Factory;

public class FeatureFunctionsDummy implements IFeatureFunctions {

    int sizeOfVector = 6;

	@Override
	//return null on error
	public Example convert(Example vector, String label, IKernel kernel) {

        try{
            Example newVector = Factory.getExample(0);
            newVector.sizeOfVector = sizeOfVector;

            String labelValues[] = label.split(Consts.CLASSIFICATION_SPLITTER);
            int i = Integer.parseInt(labelValues[0]);
            int j = Integer.parseInt(labelValues[1]);

            //compute the difference/gradients near the start and end points
            double diff_1_Start = Math.abs(vector.getFeatures().get(i)-vector.getFeatures().get(i-1));
            double diff_1_End = Math.abs(vector.getFeatures().get(j)-vector.getFeatures().get(j+1));

            //compute the difference/gradients near the start and end points
            double diff_2_Start = Math.abs(vector.getFeatures().get(i)-vector.getFeatures().get(i-2));
            double diff_2_End = Math.abs(vector.getFeatures().get(j)-vector.getFeatures().get(j+2));

            //compute the avg of the signal from start to end
            double avg = 0;
            for(int k=i ; k<j ; k++)
                avg+=vector.getFeatures().get(k);

            if(j-i <= 0){
                System.err.println("Convert single vector: "+ ErrorConstants.GENERAL_ERROR+ ErrorConstants.ZERO_DIVIDING);
                return null;
            }
            avg /= (double)(j-i);

            //compute the avg MIN_GAP_END_DUMMY after end
            double gapEnd = 0;
            for(int k=j ; k<j+Consts.MIN_GAP_END_DUMMY ; k++)
                gapEnd+=vector.getFeatures().get(k);

            gapEnd/=(double)Consts.MIN_GAP_END_DUMMY;

            //compute the avg MIN_GAP_START_DUMMY before start
            double gapStart = 0;
            for(int k=i-Consts.MIN_GAP_START_DUMMY+1 ; k<=i ; k++)
                gapStart+=vector.getFeatures().get(k);
            gapStart/=(double)Consts.MIN_GAP_START_DUMMY;

            //adding the feature functions
            Vector tmpVector = new Vector();
            tmpVector.put(0, diff_1_Start);
            tmpVector.put(1, diff_1_End);
            tmpVector.put(2, diff_2_Start);
            tmpVector.put(3, diff_2_End);
            tmpVector.put(4, avg-gapStart);
            tmpVector.put(5,avg-gapEnd);

            if(kernel !=null)
                tmpVector = kernel.convertVector(tmpVector, sizeOfVector);

            newVector.setFeatures(tmpVector);

            return newVector;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
	}

    @Override
    public int getSizeOfVector() {
        return this.sizeOfVector;
    }
}
