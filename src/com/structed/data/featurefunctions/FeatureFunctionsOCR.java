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

import com.structed.data.Factory;
import com.structed.data.entities.Example;
import com.structed.data.entities.Vector;
import com.structed.models.kernels.IKernel;

/**
 * This class implements the feature functions for the OCR task
 * Created by yossiadi on 8/2/15.
 */
public class FeatureFunctionsOCR implements IFeatureFunctions {

    // 16*8*26 (image size for all characters) + 26*26 (all the english characters pairs)
    private int sizeOfVector;
    final int numOfCharacters = 27;
    // default value image size
    private int maxFeatures = 128;

    public FeatureFunctionsOCR(int maxNumFeatures){
        this.maxFeatures = maxNumFeatures;
        // 16*8*26 (image size for all characters) + 26*26 (all the english characters pairs)
        this.sizeOfVector = this.maxFeatures*this.numOfCharacters + (this.numOfCharacters*this.numOfCharacters);
    }

    @Override
    public Example convert(Example vector, String label, IKernel kernel) {
        try{
            //parse the label
            char prevChar = label.charAt(0);
            char currChar = label.charAt(1);
            int currCharIdx = currChar - '0';

            Example newVector = Factory.getExample(0);
            newVector.sizeOfVector = sizeOfVector;
            Vector tmpVector = new Vector();

            //run the phi function
            for(Integer feature : vector.getFeatures().keySet())
                tmpVector.put(feature+currCharIdx*maxFeatures,vector.getFeatures().get(feature));

            // expand the vector size of needed
            if(kernel !=null)
                tmpVector = kernel.convertVector(tmpVector, vector.sizeOfVector);
            // populate the new example with the feature functions
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
