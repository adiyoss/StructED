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

package com.structed.tests.dal;

import com.structed.constants.Consts;
import com.structed.dal.Reader;
import com.structed.data.Factory;
import com.structed.data.InstancesContainer;
import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Test class for the OCR Reader
 * Created by yossiadi on 7/18/15.
 */
public class OcrReaderTest extends TestCase {
    
    public void testReadFile() throws Exception {
        Reader reader = Factory.getReader(3);
        ArrayList<ArrayList<String>> data = reader.readFile("src/com/structed/tests/tests_data/ocr/val.data", Consts.TAB);
        assertEquals("Data size should be: 999", 999, data.size());
        for(int i=0 ; i<data.size() ; i++)
            assertEquals((i+1)+"Example, size should be: 134", 134, data.get(i).size());
    }

    public void testReadData() throws Exception {
        Reader reater = Factory.getReader(3);
        // validation set
        InstancesContainer containerVal = reater.readData("src/com/structed/tests/tests_data/ocr/val.data", Consts.TAB, Consts.TAB);
        assertEquals("Should be 111 words", 111, containerVal.getSize());

        // test set
        InstancesContainer containerTest = reater.readData("src/com/structed/tests/tests_data/ocr/test.data", Consts.TAB, Consts.TAB);
        assertEquals("Should be 1,137 words", 1137, containerTest.getSize());
    }
}