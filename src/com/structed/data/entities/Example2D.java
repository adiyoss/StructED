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

package com.structed.data.entities;

import java.util.ArrayList;

/**
 * this class will store the raw data and the desired label
 * the raw data here will be presented as 2D array
 */
public class Example2D extends Example {

    private ArrayList<Vector> features2D;
    private ArrayList<Integer> labels2D;


    public Example2D(){
        features2D = new ArrayList<Vector>();
        labels2D = new ArrayList<Integer>();

    }

    public ArrayList<Vector> getFeatures2D() {
        return features2D;
    }

    public void setFeatures2D(ArrayList<Vector> features) {
        this.features2D = features;
    }

    public ArrayList<Integer> getLabels2D() { return labels2D; }

    public void setLabels2D(ArrayList<Integer> labels2D) { this.labels2D = labels2D; }
}
