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
import com.structed.data.entities.Example2D;
import com.structed.data.Factory;
import com.structed.data.InstancesContainer;
import com.structed.data.LazyInstancesContainer;
import com.structed.dal.Reader;
import junit.framework.TestCase;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

/*
    Unit test for the LazyInstanceContainer class
 */
public class LazyInstancesContainerTest extends TestCase{

    public void testGetInstance() throws Exception {
        Reader reader =  Factory.getReader(2);
        InstancesContainer instances_train;
        instances_train = reader.readData("data/tests/db/test4.txt", Consts.SPACE, Consts.COLON_SPLITTER);
        assertThat(instances_train, instanceOf(LazyInstancesContainer.class));
        assertEquals("Must return null because there is no such files",instances_train.getInstance(0), null);

        InstancesContainer instances_test;
        instances_test = reader.readData("data/tests/db/test2.txt", Consts.SPACE, Consts.COLON_SPLITTER);
        assertThat(instances_test, instanceOf(LazyInstancesContainer.class));
        assertThat(instances_test.getInstance(0), instanceOf(Example2D.class));
    }
}