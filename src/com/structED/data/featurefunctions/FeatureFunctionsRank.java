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

public class FeatureFunctionsRank implements IFeatureFunctions {

    public Example convert(Example vector, String label, IKernel kernel)
    {
        try{
            //initialize result object
            Example phiData = Factory.getExample(0);

            int intLabel = Integer.parseInt(label);
            phiData.setLabel(label);
            Vector features;
            features = vector.getFeatures2D().get(intLabel);

            if(kernel != null)
                features = kernel.convertVector(vector.getFeatures2D().get(intLabel), vector.getFeatures2D().get(intLabel).size());

            phiData.setFeatures(features);
            return phiData;

        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getSizeOfVector() {
        return 0;
    }
}
