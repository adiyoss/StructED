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

package com.structed.models.kernels1;

import com.structed.data.entities.Vector;

/**
 * Poly 2 kernel
 */
public class Poly2Kernel implements IKernel {

    //new dimension = d + d*(d-1)/2.0
    public Vector convertVector(Vector vector, int vectorSize)
    {
        Vector expansionVector = new Vector();

        double sqrt_2 = Math.sqrt(2);
        int loc = 0;

        //============================================//
        for (int j=0 ; j<vectorSize ; j++) {
            if(vector.containsKey(j))
                expansionVector.put(loc, vector.get(j)*vector.get(j));
            loc++;
        }

        //============================================//
        for (int j = 0 ; j<vectorSize-1 ; j++) {
            for (int k=j+1 ; k<vectorSize ; k++) {
                if(vector.containsKey(j) && vector.containsKey(k))
                    expansionVector.put(loc, sqrt_2*vector.get(j)*vector.get(k));
                loc++;
            }
        }
        return expansionVector;
    }
}