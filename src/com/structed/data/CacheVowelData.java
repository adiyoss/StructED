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

package com.structed.data;

import com.structed.data.entities.Example;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * preforms caching for computing the feature functions
 * Can be used only at the vowel duration task at the moment
 */
public class CacheVowelData {

    private static HashMap<Integer, ArrayList<ArrayList<Double>>> cache = new HashMap<Integer, ArrayList<ArrayList<Double>>>();

    /**
     * get the cumulative value of example of feature
     * @param example
     * @param frameIndex
     * @param featureNumber
     * @return
     */
    public static double getCumulativeValue(Example example, int frameIndex, int featureNumber)
    {
        double res = 0;
        if(frameIndex <= 0)
            res = cache.get(example.hashCode()).get(0).get(featureNumber);
        else if(frameIndex >= cache.get(example.hashCode()).size())
            res = cache.get(example.hashCode()).get(cache.get(example.hashCode()).size()-1).get(featureNumber);
        else if(cache.containsKey(example.hashCode()))
            res = cache.get(example.hashCode()).get(frameIndex).get(featureNumber);

        return res;
    }

    /**
     * update the cache for the given example
     * @param example
     */
    public static void updateCache(Example example)
    {
        if(!cache.containsKey(example.hashCode()))
        {
            ArrayList<ArrayList<Double>> cumulativeValues = new ArrayList<ArrayList<Double>>();
            ArrayList<Double> tmp = new ArrayList<Double>();

            int size = example.getFeatures2D().get(0).size();
            for(int i=0 ; i<size ; i++)
                tmp.add(example.getFeatures2D().get(0).get(i));

            cumulativeValues.add(tmp);

            for(int i=1 ; i<example.sizeOfVector ; i++)
            {
                ArrayList<Double> vector = new ArrayList<Double>();
                for(int j=0 ; j<size ; j++)
                    vector.add(cumulativeValues.get(i-1).get(j) + example.getFeatures2D().get(i).get(j));

                cumulativeValues.add(vector);
            }

            cache.put(example.hashCode(),cumulativeValues);
        }
    }

    /**
     * Clears the cache values
     */
    public static void clearCacheValues(){
        cache.clear();
    }
}
