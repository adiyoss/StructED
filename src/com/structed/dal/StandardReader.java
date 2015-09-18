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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.structed.constants.ErrorConstants;
import com.structed.data.entities.Example;
import com.structed.data.Factory;
import com.structed.data.InstancesContainer;
import com.structed.data.entities.Vector;
import com.structed.data.Logger;

/**
 * A standard reader
 * It reads the data in the following format:
 * label index_of_feature_i:value_of_feature_i (this holds also for sparse data, when many values are zeros, see mnist data for example)
 */
public class StandardReader implements Reader{

    /**
     * default Constructor
     */
	public StandardReader() {
	}

    /**
     * reads the data and returns an InstanceContainer object
     * @param path the path to the data
     * @param dataSpliter the splitter between the data values
     * @param valueSpliter the separator between the index of the feature to the feature value
     * @return an InstanceContainer object which contains all the data
     */
    @Override
    public InstancesContainer readData(String path, String dataSpliter, String valueSpliter)
    {
        ArrayList<ArrayList<String>> data = readFile(path, dataSpliter);
        ArrayList<Example> vectors = new ArrayList<Example>();

        for(int i=0 ; i<data.size() ; i++)
        {
            Example vector = Factory.getExample(0);
            Vector tmp = new Vector();
            for(int j=1 ; j<data.get(i).size() ; j++){
                String[] values = data.get(i).get(j).split(valueSpliter);
                tmp.put(Integer.parseInt(values[0]),Double.parseDouble(values[1]));
            }

            vector.setFeatures(tmp);
            vector.setLabel(data.get(i).get(0));
            vector.sizeOfVector = data.get(i).size()-1;
            vectors.add(vector);
            vector.path = path;
        }

        InstancesContainer instances = Factory.getInstanceContainer(0);
        instances.setInstances(vectors);
        return instances;
    }

    /**
     * read the file
     * each attribute in each row should be splited by spliter
     * @param path the path to the input file
     * @param spliter the note to separate the values by
     * @return a 2-D array of strings
     */
    @Override
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

    /**
     * this functions reads the paths from an input file of paths to the data
     * @param path: the path to the input file
     * @return an array list of string
     */
    public ArrayList<String> readPathes(String path)
    {
        //result object
        ArrayList<String> data = new ArrayList<String>();

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
                data.add(strLine);
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

