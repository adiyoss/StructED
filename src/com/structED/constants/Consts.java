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
 * this class holds all the program constants
 */
public class Consts {

    // GENERALS
	public static final String SPACE = " ";
    public static final String COMMA_NOTE = ",";
	public static final String CLASSIFICATION_SPLITTER = "-";
	public static final String COLON_SPLITTER = ":";
    public static final String PARAMS_SPLITTER = ";";
	public static final String NEW_LINE = "line.separator";
    public static final int ERROR_NUMBER = -1;

    // DUMMY
    public static final int MIN_GAP_START_DUMMY = 3;
    public static final int MIN_GAP_END_DUMMY = 3;

    // VOWEL DURATION
    public static final int MIN_VOWEL = 9;  //min training set: 45 frames
	public static final int MAX_VOWEL = 92; //max training set: 459 frames
    public static final int MIN_GAP_START = 10;
    public static final int MIN_GAP_END = 10;

    public static final double MEAN_VOWEL_LENGTH = 41.787; //pre calculated from the training set
    public static final double MAX_VOWEL_GAMMA = 38; //pre calculated from the training set
    public static final double STD_VOWEL_LENGTH = 12.918; //pre calculated from the training set

    // MODELS NUMBER OF PARAMETERS
    public static final int PA_PARAMS_SIZE = 1; //The number params 4 PA
    public static final int SVM_PARAMS_SIZE = 2; //The number params 4 SVM
    public static final int DL_PARAMS_SIZE = 2; //The number params 4 Direct Loss
    public static final int CRF_PARAMS_SIZE = 2; //The number params 4 CRF
    public static final int RL_PARAMS_SIZE = 2; //The number params 4 Ramp Loss
    public static final int PL_PARAMS_SIZE = 6; //The number params 4 Probit Loss
    public static final int SP_PARAMS_SIZE = 0; //The number params 0 Perceptron
    public static final int ORBIT_PARAMS_SIZE = 2; //The number params 4 SVM

    // KERNEL DEFAULT PARAMETERS
    public static double SIGMA = 1;
}