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

package com.structed.utils;

import com.structed.constants.ErrorConstants;
import com.structed.data.entities.Vector;
import com.structed.data.Logger;

/**
 * Helper class for all the math operations
 */
public class MathHelpers {

    /**
     * multiple two double vectors
     * @param v1
     * @param v2
     * @return
     */
	public static double multipleVectors(Vector v1, Vector v2)
	{
		double result = 0;
        Double num;

		//validation
		if(v1.size() == 0 || v2.size() == 0) {
            Logger.error(ErrorConstants.MULTIPLE_VECTORS_EMPTY_ERROR);
			return 0;
		}

        if(v1.size() <= v2.size()) {
            //multiple the vectors
            for (Integer key : v1.keySet()) {
                num = v2.get(key);
                if (num != null)
                    result += (v1.get(key) * v2.get(key));
            }
        } else {
            //multiple the vectors
            for (Integer key : v2.keySet()) {
                num = v1.get(key);
                if (num != null)
                    result += (v1.get(key) * v2.get(key));
            }
        }

		return result;
	}

    /**
     * add scalar to a double vector
     * @param v1
     * @param scalar
     * @return
     */
	public static Vector addScalar2Vectors(Vector v1, double scalar)
	{
        Vector result = new Vector();

		if(v1.size() == 0) {
			Logger.error(ErrorConstants.MULTIPLE_VECTORS_EMPTY_ERROR);
			return null;
		}

		//multiple the vectors
		for(Integer key : v1.keySet())
			result.put(key, v1.get(key) + scalar);

		return result;
	}

    /**
     * add scalar to a double vector
     * @param v1
     * @param scalar
     * @return
     */
	public static Vector mulScalarWithVectors(Vector v1, double scalar)
	{
        Vector result = new Vector();

		if(v1.size() == 0) {
			Logger.error(ErrorConstants.MULTIPLE_VECTORS_EMPTY_ERROR);
			return null;
		}

		//multiple the vectors
        for(Integer key : v1.keySet())
            result.put(key, v1.get(key) * scalar);

		return result;
	}

    /**
     * add scalar to a double vector
     * @param v1
     * @param v2
     * @return
     */
	public static Vector subtract2Vectors(Vector v1, Vector v2)
	{
        Vector result = new Vector ();
        Double num;

		//validation
		if(v1.size() == 0 || v2.size() == 0) {
			Logger.error(ErrorConstants.MULTIPLE_VECTORS_EMPTY_ERROR);
			return null;
		}

		//subtract the vectors
		for(Integer key : v1.keySet()){
            num = v2.get(key);
            if(num != null)
			    result.put(key, v1.get(key)-v2.get(key));
            else
                result.put(key, v1.get(key));
        }

        for(Integer key : v2.keySet()){
            num = v1.get(key);
            if(num == null)
                result.put(key, (-1)*v2.get(key));
        }

		return result;
	}

    /**
     * add scalar to a double vector
     * @param v1
     * @param v2
     * @return
     */
	public static Vector add2Vectors(Vector v1, Vector v2)
	{
        Vector result = new Vector();
        Double num;
		//validation
        if(v1.size() == 0 || v2.size() == 0) {
            Logger.error(ErrorConstants.MULTIPLE_VECTORS_EMPTY_ERROR);
            return null;
        }

		//add the vectors
        for(Integer key : v1.keySet()){
            num = v2.get(key);
            if(num != null)
                result.put(key, v1.get(key)+v2.get(key));
            else
                result.put(key, v1.get(key));
        }

        for(Integer key : v2.keySet()){
            num = v1.get(key);
            if(num == null)
                result.put(key, v2.get(key));
        }

		return result;
	}

    /**
     * Preform sigmoid functions
     * @param x
     * @return
     */
    public static double sigmoid(double x) {
        return (1/( 1 + Math.pow(Math.E,(-1*x))));
    }

    public static double norm2(Vector v){
        double norm = 0.0;
        for(Integer key : v.keySet()){
            norm += v.get(key) * v.get(key);
        }
        return norm;
    }
}
