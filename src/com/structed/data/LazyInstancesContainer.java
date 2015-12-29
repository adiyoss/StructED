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
import com.structed.dal.LazyReader;

import java.io.File;

/**
 * Lazy instance container that loads the examples by demand
 * The standard container loads all the data, this container gets as input all the paths to the raw data and loads it when needed
 *
 */
public class LazyInstancesContainer extends InstancesContainer{

    LazyReader reader = new LazyReader();

    //C'tor
    public LazyInstancesContainer(){
        super();
    }

    @Override
    //get the requested example
    public Example getInstance(int index) {
        try {
            File file = new File(paths.get(index).get(0));
            if(file.exists()) {
                Example example = reader.readExample(paths.get(index));
                CacheVowelData.updateCache(example);
                example.path = paths.get(index).get(0);
                return example;
            }
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
