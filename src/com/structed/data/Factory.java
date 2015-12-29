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


import com.structed.dal.*;

import com.structed.data.entities.Example;
import com.structed.data.entities.Example1D;
import com.structed.data.entities.Example2D;

/**
 * Factory class
 */
public class Factory {

    /**
     * Get specific reader
     * This function can be used also for adding new reader types
     * @param type the reader type
     * @return the reader
     */
	//==============DATA ACCESS==============//
	//type is not supported
	//StandardReader getter object
	public static Reader getReader(int type){
        switch(type){
            case 0:
		        return new StandardReader();
            case 1:
                return new RankReader();
            case 2:
                return new LazyReader();
            case 3:
                return new OcrReader();
            default:
                return new StandardReader();
        }
	}

    /**
     * Get specific writer
     * This function can be used also for adding new writer types
     * @param type the writer type
     * @return the writer
     */
	//type is not supported
	//Writer getter object
	public static Writer getWriter(int type){
        switch (type){
            case 0:
		        return new StandardWriter();
            case 1:
                return new RankWriter();
            default:
                return new StandardWriter();
        }
	}

    /**
     * Get specific example
     * This function can be used also for adding new example types
     * @param type the example type
     * @return the example
     */
	//=======================================//
	//===============DATA TYPES==============//
	//type is not supported
	//data vector getter object
	public static Example getExample(int type){
        switch(type){
            case 0:
                return new Example1D();
            case 1:
                return new Example2D();
            default:
                return new Example1D();
        }
	}

    /**
     * Get specific container
     * This function can be used also for adding new containers types
     * @param type the container type
     * @return the container
     */
    public static InstancesContainer getInstanceContainer(int type){
        switch(type) {
            case 0:
                return new InstancesContainer();
            case 1:
                return new LazyInstancesContainer();
            default:
                return new InstancesContainer();
        }
    }
}
