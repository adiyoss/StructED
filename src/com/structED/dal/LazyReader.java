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
import com.structed.constants.ErrorConstants;
import com.structed.data1.entities.Example;
import com.structed.data1.Factory;
import com.structed.data1.InstancesContainer;
import com.structed.data1.entities.Vector;
import com.structed.data1.Logger;
import com.structed.utils.ConverterHelplers;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

//Paths reader
public class LazyReader implements Reader {

    /**
     * a Lazy reader, it reads only the paths to the data1 and not the data1 itself
     * this reader will read the actual data1 on demand
     * @param path the path to the data1
     * @param dataSpliter the splitter between the data1 values
     * @param valueSpliter the separator between the index of the feature to the feature value
     * @return an InstanceContainer object which contains all the data1
     */
    public InstancesContainer readData(String path, String dataSpliter, String valueSpliter)
    {
        ArrayList<ArrayList<String>> data = readFile(path,dataSpliter);
        InstancesContainer instances = Factory.getInstanceContainer(1);
        instances.setPaths(data);
        return instances;
    }

    /**
     * read only one example and it's labels
     * @param paths the paths to the data1 and to the label files
     * @return an Example object contains the data1
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

    /**
     * read the file
     * each attribute in each row should be splited by spliter
     * @param path the path to the input file
     * @param spliter the note to separate the values by
     * @return a 2-D array of strings
     */
    public ArrayList<ArrayList<String>> readFile(String path, String spliter)
    {
        //result object
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

        try{
            // Open the file that is the first
            // command line parameter
            FileInputStream fstream = new FileInputStream(path);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;
            //Read File Line By Line
            while ((strLine = br.readLine())!=null)   {
                //skip on empty line
                if(strLine.equalsIgnoreCase("")){
                    continue;
                }

                String values[] = strLine.split(spliter);
                ArrayList<String> row = new ArrayList<String>();
                for(int i=0 ; i<values.length ; i++)
                    //Print the content on the console
                    row.add(values[i]);
                data.add(row);
            }
            //Close the input stream
            br.close();
            in.close();
            fstream.close();

        } catch (Exception e) {//Catch exception if any
            Logger.error(ErrorConstants.GENERAL_ERROR);
            e.printStackTrace();
            return null;
        }

        return data;
    }
}
