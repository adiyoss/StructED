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

import com.structed.utils.ConverterHelplers;
import junit.framework.TestCase;

import java.util.Random;

/**
 * Created by yossiadi on 7/7/15.
 */
public class ConverterHelplersTest extends TestCase {
    
    public void testTryParseInt() throws Exception {
        Random random = new Random();
        for(int i=0 ; i<10 ; i++) {
            Double num = random.nextDouble();
            assertEquals("Should be double", true, ConverterHelplers.tryParseDouble(num.toString()));
            assertEquals("Should not be double", false, ConverterHelplers.tryParseDouble(num.toString()+"_"));
        }
    }

    public void testTryParseDouble() throws Exception {
        Random random = new Random();
        for(int i=0 ; i<10 ; i++) {
            Integer num = random.nextInt();
            assertEquals("Should be double", true, ConverterHelplers.tryParseInt(num.toString()));
            assertEquals("Should not be double", false, ConverterHelplers.tryParseInt(num.toString() + "_"));
        }
    }
}