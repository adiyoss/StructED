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

package com.structed.models.kernels;

import com.structed.constants.Consts;
import com.structed.data1.entities.Vector;

/**
 * RBF 3rd approximation
 */
public class RBF3Kernel implements IKernel {

    private double sigma = Consts.SIGMA;//default value

    //new dimension = 1 + 3.0*d + 3.0*d*(d-1)/2.0 + d*(d-1)*(d-2)/6.0
    public Vector convertVector(Vector vector, int vectorSize)
    {
        Vector expansionVector = new Vector();

        double sigmaSqr = sigma*sigma;
        double sigmaThrd = sigma*sigma*sigma;

        // exp(-||x||^2/sigma2)
        double x_norm2 = 0.0;
        for (int j=0 ; j<vectorSize ; j++) {
            if(vector.containsKey(j))
                x_norm2 += vector.get(j) * vector.get(j);
        }
        double exp2_coef = Math.exp(-x_norm2/(2.0*sigmaSqr));

        // exp(-x) expansion
        int loc = 0;
        double tmp_coef;
        expansionVector.put(loc,exp2_coef);
        loc++;

        //============================================//
        tmp_coef = 1/sigma;
        for (int j=0 ; j<vectorSize ; j++){
            if(vector.containsKey(j))
                expansionVector.put(loc,vector.get(j) * tmp_coef * exp2_coef);
            loc++;
        }

        //============================================//
        tmp_coef = 1.0/(Math.sqrt(2.0)*sigmaSqr);
        for (int j=0 ; j<vectorSize ; j++){
            if(vector.containsKey(j))
                expansionVector.put(loc,vector.get(j) * vector.get(j) * tmp_coef * exp2_coef);
            loc++;
        }

        //============================================//
        tmp_coef = 1.0/(sigmaSqr);
        for (int j=0 ; j<vectorSize-1 ; j++){
            for (int k=j+1 ; k<vectorSize ; k++){
                if(vector.containsKey(j) && vector.containsKey(k))
                    expansionVector.put(loc,vector.get(j) * vector.get(k) * tmp_coef * exp2_coef);
                loc++;
            }
        }

        //============================================//
        tmp_coef = 1.0/(Math.sqrt(6.0)*sigmaThrd);
        for (int j=0 ; j<vectorSize ; j++){
            if(vector.containsKey(j))
                expansionVector.put(loc,vector.get(j) * vector.get(j) * vector.get(j) * tmp_coef * exp2_coef);
            loc++;
        }

        //============================================//
        tmp_coef = Math.sqrt(3.0)/(Math.sqrt(6.0)*sigmaThrd);
        for (int j=0 ; j<vectorSize-1 ; j++){
            for (int k=j+1 ; k<vectorSize ; k++){
                if(vector.containsKey(j) && vector.containsKey(k)) {
                    expansionVector.put(loc, vector.get(j) * vector.get(j) * vector.get(k) * tmp_coef * exp2_coef);
                    loc++;
                    expansionVector.put(loc, vector.get(j) * vector.get(k) * vector.get(k) * tmp_coef * exp2_coef);
                    loc++;
                } else
                    loc+=2;
            }
        }

        //============================================//
        tmp_coef = 1.0/(sigmaThrd);
        for (int j=0 ; j<vectorSize-2 ; j++){
            for (int k=j+1 ; k<vectorSize-1 ; k++){
                for (int l=k+1 ; l<vectorSize ; l++){
                    if(vector.containsKey(j) && vector.containsKey(k) && vector.containsKey(l))
                        expansionVector.put(loc, vector.get(j) * vector.get(k) * vector.get(l) * tmp_coef * exp2_coef);
                    loc++;
                }
            }
        }

        return expansionVector;
    }

    public void setSigma(double sigma){this.sigma = sigma;}
}
