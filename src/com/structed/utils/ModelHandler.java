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

import com.structed.constants.Consts;
import com.structed.data.entities.Example;
import com.structed.data.Factory;
import com.structed.data.InstancesContainer;
import com.structed.data.entities.Vector;
import com.structed.dal.Reader;

import java.text.ParseException;
import java.util.*;

/**
 * Helper class
 */
public class ModelHandler {

    /**
     * Set weights for a model from a given file
     * This function either sets the new vector to be zeros or reads the data from a given path
     * @param type - if this parameter is set to be 0 than the returned vector will be zeros, if 1 than it will populate the vector with the file's data
     * @param path - the path to the file that contains the vecotr's data
     * @return the new vector
     */
    public static Vector setWeights(int type, String path) {

        Vector W = new Vector();
        try{
            Reader reader = Factory.getReader(0);
            if(type == 0)
                W.put(0, 0.0);
            else if(type == 1){
                ArrayList<ArrayList<String>> wData = reader.readFile(path, Consts.SPACE);
                W = convert2Weights(wData);
            }
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return W;
    }

    /**
     * Converts an array list of strings into a vector of double
     * this function can be used after reading a vector from a file
     * @param data the data 2D array but we treat tim as a 1D array
     * @return a vector that contains the model weights
     * @throws ParseException
     */
    public static Vector convert2Weights(ArrayList<ArrayList<String>> data) throws ParseException
    {
        Vector result = new Vector();
        for(int i=0 ; i<data.size() ; i++)
        {
            for(int j=0 ; j<data.get(i).size() ; j++){
                String values[] = data.get(i).get(j).split(Consts.COLON_SPLITTER);
                result.put(Integer.parseInt(values[0]), Double.parseDouble(values[1]));
            }
        }
        return result;
    }

    /**
     * preforming random shuffle on the data instances
     * @param instances
     * @return
     */
    public static InstancesContainer randomShuffle(InstancesContainer instances)
    {
        Random rnd = new Random();
        rnd.setSeed(1234);

        // random shuffle the paths
        if(instances.getPaths().size()!=0) {
            for (int i = instances.getPaths().size() - 1; i > 0; i--) {
                int index = rnd.nextInt(i + 1);
                // Simple swap
                ArrayList<String> tmp = instances.getPaths().get(index);
                instances.getPaths().set(index, instances.getPaths().get(i));
                instances.getPaths().set(i, tmp);
            }
        }

        // random shuffle the instances
        for (int i = instances.getInstances().size()-1 ; i>0 ; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            Example tmp = instances.getInstances().get(index);
            instances.getInstances().set(index, instances.getInstances().get(i));
            instances.getInstances().set(i, tmp);
        }
        return instances;
    }

    public static HashMap<Integer,Double> printMul(Vector v1, Vector v2)
    {
        HashMap<Integer,Double> result = new HashMap<Integer,Double>();
        for(Map.Entry<Integer,Double> entry : v1.entrySet()){
            if(v2.containsKey(entry.getKey())){
                result.put(entry.getKey(), entry.getValue()*v2.get(entry.getKey()));
            }
        }

        return result;
    }

    public static TreeMap<Integer,Double> sortMul(Vector v1, Vector v2)
    {
        TreeMap<Integer,Double> result = new TreeMap<Integer,Double>();
        for(Map.Entry<Integer,Double> entry : v1.entrySet()){
            if(v2.containsKey(entry.getKey())){
                result.put(entry.getKey(), entry.getValue()*v2.get(entry.getKey()));
            }
        }

        return result;
    }
}
