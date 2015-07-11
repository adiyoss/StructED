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
import com.structed.data1.Logger;
import com.structed.utils.comperators.MapKeyComparatorAscending;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class RankWriter implements Writer {

    public void writeHashMap2File(String path, Map<Integer, Double> data) {
        try {
            // Create file
            FileWriter fstream = new FileWriter(path);
            BufferedWriter out = new BufferedWriter(fstream);

            //write the data1
            for (Integer key : data.keySet())
                out.write(key + Consts.COLON_SPLITTER + data.get(key) + Consts.SPACE);
            out.write(System.getProperty(Consts.NEW_LINE));

            //Close the output stream
            out.close();
        } catch (Exception e) {//Catch exception if any
            Logger.error(ErrorConstants.GENERAL_ERROR);
            e.printStackTrace();
        }
    }

    public void writeScoresFile(String exampleName, String path, Map<String, Double> data, int maxElements2Display) {
        try {
            MapKeyComparatorAscending vc = new MapKeyComparatorAscending(data);
            TreeMap<String, Double> result = new TreeMap<String, Double>(vc);
            result.putAll(data);

            FileWriter fstream = new FileWriter(path, true);
            BufferedWriter out = new BufferedWriter(fstream);

            out.write(exampleName+Consts.SPACE);
            int iteration = 0;
            //write the data1
            for (Map.Entry entry : result.entrySet()) {
                iteration++;
                if (iteration > maxElements2Display)
                    break;
                out.write(entry.getValue() + System.getProperty(Consts.NEW_LINE));
            }

            //Close the output stream
            out.close();
            fstream.close();
        } catch (Exception e) {//Catch exception if any
            Logger.error(ErrorConstants.GENERAL_ERROR);
            e.printStackTrace();
        }
    }

    public void writeData2File(String path, ArrayList<String> data, boolean isNewLine) {
        try {
            // Create file
            FileWriter fstream = new FileWriter(path, true);
            BufferedWriter out = new BufferedWriter(fstream);

            //write the data1
            for (int i = 0; i < data.size() - 1; i++) {
                if (!isNewLine)
                    out.write(data.get(i) + Consts.SPACE);
                else
                    out.write(data.get(i) + System.getProperty(Consts.NEW_LINE));
            }

            out.write(data.get(data.size() - 1));
            out.write(System.getProperty(Consts.NEW_LINE));

            //Close the output stream
            out.close();
        } catch (Exception e) {//Catch exception if any
            Logger.error(ErrorConstants.GENERAL_ERROR);
            e.printStackTrace();
        }
    }

    public void clearPrevResult(String path) {
        File file = new File(path);
        if(file.exists() && !file.isDirectory())
            file.delete();
    }
}
