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

package com.structed.tests.utils;

import com.structed.constants.Consts;
import com.structed.dal.Reader;
import com.structed.data1.Factory;
import com.structed.data1.InstancesContainer;
import com.structed.data1.entities.Vector;
import com.structed.utils.ModelHandler;
import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by yossiadi on 7/7/15.
 */
public class ModelHandlerTest extends TestCase {


    public void testConvert2Weights() throws Exception {
        double[] values = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.1};
        ArrayList<String> w = new ArrayList<String>();
        for(int i=0 ; i<values.length ; i++)
            w.add(i+":"+values[i]);
        ArrayList<ArrayList<String>> new_w = new ArrayList<ArrayList<String>>();
        new_w.add(w);

        Vector double_w = ModelHandler.convert2Weights(new_w);
        for(int i=0 ; i<double_w.size() ; i++) {
            assertEquals("Values must be equal", values[i], double_w.get(i));
        }
    }

    public void testRandomShuffle() throws Exception {
        InstancesContainer instances;
        Reader reader =  Factory.getReader(2);

        instances = reader.readData("data1/tests/db/test2.txt", Consts.SPACE, Consts.COLON_SPLITTER);
        InstancesContainer randInstances = ModelHandler.randomShuffle(reader.readData("data1/tests/db/test2.txt", Consts.SPACE, Consts.COLON_SPLITTER));

        // Test 1
        assertEquals("Container sizes must be equal", instances.getSize(), randInstances.getSize());

        // Test 2
        boolean flag = false;
        for(int i=0 ; i<instances.getSize() ; i++)
            if (instances.getPaths().get(i).get(0) != randInstances.getPaths().get(i).get(0)) {
                flag = true;
                break;
            }
        assertEquals("Check for randomness", flag, true);
    }
}