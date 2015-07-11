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

package com.structed.tests.models.loss;

import com.structed.models.loss.TaskLossDummyData;
import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by yossiadi on 7/6/15.
 */
public class TaskLossDummyDataTest extends TestCase {
    
    public void testComputeTaskLoss() throws Exception {
        String values[] = {"0-10", "10-16", "20-22", "45-52", "100-112", "12-10"};
        ArrayList<Double> params = new ArrayList<Double>(){{add(5.0);}};
        double lossValue;
        TaskLossDummyData loss = new TaskLossDummyData();

        // #Test 1
        lossValue = loss.computeTaskLoss(values[0], values[1], params);
        assertEquals("The loss should be: 0", 0.0, lossValue);

        // #Test 2
        lossValue = loss.computeTaskLoss(values[0], values[2], params);
        assertEquals("The loss should be: 3", 3.0, lossValue);

        // #Test 3
        lossValue = loss.computeTaskLoss(values[2], values[3], params);
        assertEquals("The loss should be: 0", 0.0, lossValue);

        // #Test 4
        lossValue = loss.computeTaskLoss(values[0], values[4], params);
        assertEquals("The loss should be: 0", 0.0, lossValue);

        // #Test 5
        lossValue = loss.computeTaskLoss(values[2], values[4], params);
        assertEquals("The loss should be: 5", 5.0, lossValue);

        // #Test 6
        lossValue = loss.computeTaskLoss(values[2], values[5], params);
        assertEquals("The loss should be: 0", 0.0, lossValue);

        // #Test 7
        lossValue = loss.computeTaskLoss(values[4], values[5], params);
        assertEquals("The loss should be: 9", 9.0, lossValue);
    }
}