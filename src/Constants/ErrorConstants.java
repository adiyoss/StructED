/*
 * StructED - Machine Learning, Structured Prediction Package written in Java.
 * Copyright (C) 2014 Yossi Adi, E-Mail: yossiadidrum@gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package Constants;

//this class stores all the types of errors 
public class ErrorConstants {

	public static final String GENERAL_ERROR = "Error: ";
	public static final String RAW_DATA_EMPTY_ERROR = "Raw data is empty";
	public static final String VECTOR_CONVERT_ERROR = "Error converting vector: ";
	public static final String PARSE_CLASSIFICATION_ERROR = "Error with the classification of example: ";
	public static final String PHI_VECTOR_DATA = "Phi vector data empty";
	
	public static final String MULTIPLE_VECTORS_SIZE_ERROR = "The vectors doesn't have the same size";
	public static final String MULTIPLE_VECTORS_EMPTY_ERROR = "The vectors are empty";
	
	public static final String UPDATE_ARGUMENTS_ERROR = "Size of arguments is not valid";
	public static final String UPDATE_DIVIDE_ZERO_ERROR = "Phi difference multiple return 0";
	
	public static final String PHI_DATA_ERROR = "Phi data is null";
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
