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

import com.structed.models.kernels.IKernel;
import com.structed.data.entities.Example;
import com.structed.data.Factory;
import com.structed.data.entities.Vector;

public class FeatureFunctionsSparse implements IFeatureFunctions {

    // data members
    private int maxFeatures;
    private int numOfClasses;
    private int sizeOfVector;

    /**
     * Constructor
     * @param numOfClasses - the number target of classes
     * @param maxNumFeatures - the number of features
     */
    public FeatureFunctionsSparse(int numOfClasses, int maxNumFeatures){
        this.numOfClasses = numOfClasses;
        this.maxFeatures = maxNumFeatures;
        this.sizeOfVector = this.maxFeatures*this.numOfClasses;
    }

	@Override
	public Example convert(Example vector, String label, IKernel kernel) {
        try{
            //parse the label
            int intLabel = Integer.parseInt(label);
            Example newVector = Factory.getExample(0);
            newVector.sizeOfVector = maxFeatures;
            Vector tmpVector = new Vector();

            //run the phi function
            for(Integer feature : vector.getFeatures().keySet())
                tmpVector.put(feature+intLabel*maxFeatures,vector.getFeatures().get(feature));

            if(kernel !=null)
                tmpVector = kernel.convertVector(tmpVector, vector.sizeOfVector);

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
