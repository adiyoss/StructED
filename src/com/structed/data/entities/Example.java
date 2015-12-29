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
 */
public abstract class Example {

    public String path;
    private String label;
    private int fold;
    public int sizeOfVector;

	//C'tor
	public Example() {
        label = "";
        sizeOfVector = 0;
	}

	//===========GETTERS AND SETTERS=========//
	public Vector getFeatures(){return null;}
	public void setFeatures(Vector features){}

    public ArrayList<Vector> getFeatures2D(){return null;}
    public void setFeatures2D(ArrayList<Vector> features){}
    public ArrayList<Integer> getLabels2D(){return null;}
    public void setLabels2D(ArrayList<Integer> rankLabels){}

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    public int getFold() { return fold; }
    public void setFold(int fold) { this.fold = fold; }
}
