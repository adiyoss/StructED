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

import com.structed.constants.ErrorConstants;
import com.structed.data1.entities.Example;
import com.structed.data1.Factory;
import com.structed.data1.InstancesContainer;
import com.structed.data1.entities.Vector;
import com.structed.data1.Logger;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RankReader implements Reader{

    public InstancesContainer readData(String path, String dataSpliter, String valueSpliter)
    {
        ArrayList<ArrayList<String>> data = readFile(path,dataSpliter);
        ArrayList<Example> vectors = new ArrayList<Example>();
        int i=0;
        String prevQid = "";

        while(i<data.size())
        {
            Example vector = Factory.getExample(1);
            ArrayList<Vector> totalQuery = new ArrayList<Vector>();
            ArrayList<Integer> labels = new ArrayList<Integer>();
            String[] qidValues = data.get(i).get(1).split(valueSpliter);
            do{

                Vector tmp = new Vector();
                labels.add(Integer.parseInt(data.get(i).get(0)));

                for(int j=2 ; j<data.get(i).size()-3 ; j++){
                    String[] values = data.get(i).get(j).split(valueSpliter);
                    tmp.put(Integer.parseInt(values[0])-1,Double.parseDouble(values[1]));
                }

                totalQuery.add(tmp);
                prevQid = qidValues[1];
                i++;

                if(i>=data.size())
                    break;

                qidValues = data.get(i).get(1).split(valueSpliter);

            } while (prevQid.equalsIgnoreCase(qidValues[1]));

            vector.setFeatures2D(totalQuery);
            vector.setLabels2D(labels);
            vector.sizeOfVector = data.get(i-1).size()-1;
            vectors.add(vector);
        }

        InstancesContainer instances = Factory.getInstanceContainer(0);
        instances.setInstances(vectors);
        return instances;
    }

    //read the file
    //each attribute in each row should be splited with ,
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
