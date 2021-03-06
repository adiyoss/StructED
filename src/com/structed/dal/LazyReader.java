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

package com.structed.dal;

import com.structed.constants.Consts;
import com.structed.data.entities.Example;
import com.structed.data.Factory;
import com.structed.data.InstancesContainer;
import com.structed.data.entities.Vector;
import com.structed.data.Logger;
import com.structed.utils.ConverterHelplers;

import java.util.ArrayList;

/**
 * This class reads the examples on demand
 * It gets as input a file with the paths to all the data and reads each example by the algorithm's demand
 */
public class LazyReader extends StandardReader {

    /**
     * a Lazy reader, it reads only the paths to the data and not the data itself
     * this reader will read the actual data on demand
     * @param path the path to the data
     * @param dataSpliter the splitter between the data values
     * @param valueSpliter the separator between the index of the feature to the feature value
     * @return an InstanceContainer object which contains all the data
     */
    @Override
    public InstancesContainer readData(String path, String dataSpliter, String valueSpliter)
    {
        ArrayList<ArrayList<String>> data = readFile(path, dataSpliter);
        InstancesContainer instances = Factory.getInstanceContainer(1);
        instances.setPaths(data);
        return instances;
    }

    /**
     * read only one example and it's labels
     * @param paths the paths to the data and to the label files
     * @return an Example object contains the data
     * @throws Exception
     */
    public Example readExample(ArrayList<String> paths) throws Exception{

        ArrayList<ArrayList<String>> features = readFile(paths.get(0), Consts.SPACE);
        ArrayList<ArrayList<String>> labels = readFile(paths.get(1), Consts.SPACE);

        Example example = Factory.getExample(1);

        //extract the labels
        String label = labels.get(1).get(0)+Consts.CLASSIFICATION_SPLITTER+labels.get(1).get(1);
        example.setLabel(label);

        //extract the features
        for (int i=0 ; i<features.size() ; i++){

            Vector vector = new Vector();
            for(int j=0 ; j<features.get(i).size() ; j++){

                if(features.get(i).get(j).equalsIgnoreCase("nan")) {
                    vector.put(j, 0.0);
                    continue;
                }

                if(!ConverterHelplers.tryParseDouble(features.get(i).get(j))){
                    Logger.error("Error converting example.");
                    return null;
                }
                double num = Double.parseDouble(features.get(i).get(j));
                vector.put(j,num);
            }
            example.getFeatures2D().add(vector);
        }

        example.sizeOfVector = example.getFeatures2D().size();
        return example;
    }
}
