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
import com.structed.data1.Factory;
import com.structed.data1.InstancesContainer;
import com.structed.dal.Reader;
import junit.framework.TestCase;

/*
    Unit tests for the Standard Reader class
 */
public class StandardReaderTest extends TestCase{

    public void testReadData() throws Exception {
        InstancesContainer instances;
        Reader reader =  Factory.getReader(0);

        // Test 1
        instances = reader.readData("data1/tests/db/test1.txt", Consts.SPACE, Consts.COLON_SPLITTER);
        assertEquals("Number of examples must be: 6", 6, instances.getSize());
        // Test 2
        instances = reader.readData("data1/tests/db/test3.txt", Consts.SPACE, Consts.COLON_SPLITTER);
        assertEquals("Number of examples must be: 14",14,instances.getSize());
    }
}