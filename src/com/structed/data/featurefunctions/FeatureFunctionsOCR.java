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

import com.structed.constants.Char2Idx;
import com.structed.constants.Consts;
import com.structed.data.Factory;
import com.structed.data.entities.Example;
import com.structed.data.entities.Vector;
import com.structed.models.kernels.IKernel;
import com.sun.xml.internal.xsom.impl.Const;

/**
 * This class implements the feature functions for the OCR task
 * Created by yossiadi on 8/2/15.
 */
public class FeatureFunctionsOCR implements IFeatureFunctions {

    // 16*8*26 (image size for all characters) + 27*(27) (all the english characters pairs plus the start character)
    private int sizeOfVector;
    final int numOfCharacters = Char2Idx.char2id.size() - 1;
    // default value image size
    private int maxFeatures = 128; // 16*8 index - [0 - 127]
    private int startPrevWordIdx = this.maxFeatures*this.numOfCharacters;

    public FeatureFunctionsOCR(int maxNumFeatures){
        this.maxFeatures = maxNumFeatures;
        // 16*8*26 (image size for all characters) + 27*27 (all the english characters pairs)
        this.sizeOfVector = this.startPrevWordIdx + ((this.numOfCharacters+1)*(this.numOfCharacters+1));
    }

    @Override
    public Example convert(Example vector, String label, IKernel kernel) {
        try{
            double indicator_val = 0.01;
            Example newVector = Factory.getExample(0);
            newVector.sizeOfVector = sizeOfVector;
            Vector tmpVector = new Vector();

            // loop over the word characters
            for (int i=0 ; i<label.length()-1 ; i++) {
                //parse the label
                char prevChar = label.charAt(i);
                char currChar = label.charAt(i+1);
                // avoid the last character
                if (currChar == Char2Idx.id2char.get(0))
                    continue;

                int prevCharIdx = Char2Idx.char2id.get(prevChar) - 1; // avoid $ sign
                int currCharIdx = Char2Idx.char2id.get(currChar) - 1; // avoid $ sign

                //run the phi function - multi-class
                for (Integer feature : vector.getFeatures2D().get(i).keySet()) {
                    int key = feature + currCharIdx * maxFeatures;
                    if (!tmpVector.containsKey(feature + currCharIdx * maxFeatures))
                        tmpVector.put(key, vector.getFeatures2D().get(i).get(feature));
                    else
                        tmpVector.put(key, tmpVector.get(key) + vector.getFeatures2D().get(i).get(feature));
                }

                // indicator for the previous character
                int key = this.startPrevWordIdx + (prevCharIdx+1) * this.numOfCharacters + (currCharIdx);
                if (!tmpVector.containsKey(key))
                    tmpVector.put(key, indicator_val);
                else
                    tmpVector.put(key, tmpVector.get(key) + indicator_val);
            }

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
