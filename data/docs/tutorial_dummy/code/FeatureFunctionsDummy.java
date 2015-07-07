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

package com.structed.data.featurefunctions;

import com.structed.models.kernels.IKernel;
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
}
