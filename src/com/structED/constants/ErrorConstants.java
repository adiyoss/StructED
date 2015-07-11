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

package com.structed.constants;

/**
 * this class stores all the types of errors
 */
public class ErrorConstants {

	public static final String GENERAL_ERROR = "Error: ";
	public static final String RAW_DATA_EMPTY_ERROR = "Raw data1 is empty";
	public static final String VECTOR_CONVERT_ERROR = "Error converting vector: ";
	public static final String PARSE_CLASSIFICATION_ERROR = "Error with the classification of example: ";
	public static final String PHI_VECTOR_DATA = "Phi vector data1 empty";
	
	public static final String MULTIPLE_VECTORS_SIZE_ERROR = "The vectors doesn't have the same size";
	public static final String MULTIPLE_VECTORS_EMPTY_ERROR = "The vectors are empty";
	
	public static final String UPDATE_ARGUMENTS_ERROR = "Size of arguments is not valid";
	public static final String UPDATE_DIVIDE_ZERO_ERROR = "Phi difference multiple return 0";
	
	public static final String PHI_DATA_ERROR = "Phi data1 is null";
	public static final String EPOCH_ERROR = "Epoch is a negative number";
	
	public static final String SIZE_OF_TRAIN_ERROR = "Train size is 0";
	public static final String GAP_START_SIZE_ERROR = "Gap start can not be 0";
	public static final String GAP_END_SIZE_ERROR = "Gap end can not be 0";
	public static final String ZERO_DIVIDING = "Can not divide by zero";
	
	public static final String CONFIG_ERROR = "Can not convert config file";
	public static final String CONFIG_ARGUMENTS_ERROR = "Invalid number of arguments";
    public static final String CONFIG_ARGUMENTS_TYPE_ERROR = "Invalid type of arguments";
	
	public static final String WRONG_TEST_VALUES = "Invalid test values";
	public static final String WRONG_TRAIN_VALUES = "Invalid train values";

    public static final String WEIGHTS_VALUES = "Error converting weights file.";
    public static final String SORT_ERROR = "Error, sorting the scores map.";
}
