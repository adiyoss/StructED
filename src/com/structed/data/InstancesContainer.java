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

package com.structed.data;

import com.structed.data.entities.Example;

import java.util.ArrayList;

/**
 * this class will store the raw data and the desired label
 * the raw data here will be presented as 2D array
 */
public class InstancesContainer {

    ArrayList<Example> instances;
    ArrayList<ArrayList<String>> paths;
    int size;

    //C'tor
    public InstancesContainer() {
        instances = new ArrayList<Example>();
        paths = new ArrayList<ArrayList<String>>();
        size = 0;
    }

    //get the requested example
    public Example getInstance(int index)
    {
        try{
            return instances.get(index);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //======Setters and getters=====//
    public ArrayList<Example> getInstances() {
        return instances;
    }

    public void setInstances(ArrayList<Example> instances) {
        this.instances = instances;
        this.size = instances.size();
    }

    public ArrayList<ArrayList<String>> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<ArrayList<String>> paths) {
        this.paths = paths;
        this.size = paths.size();
    }


    public int getSize() {
        if(this.size != 0)
            return this.size;
        this.size = (instances.size() != 0) ? instances.size() : paths.size();
        return this.size;
    }
}
