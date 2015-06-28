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

//this class holds all the program constants
public class Consts {

    //GENERALS
	public static final String SPACE = " ";
	public static final String CLASSIFICATION_SPLITTER = "-";
	public static final String COLON_SPLITTER = ":";
    public static final String PARAMS_SPLITER = ";";
	public static final String NEW_LINE = "line.separator";
    public static final String SLASH = "/";
    public static final int ERROR_NUMBER = -1;

    //DUMMY
    public static final int MIN_GAP_START_DUMMY = 3;
    public static final int MIN_GAP_END_DUMMY = 3;

    //VOWEL DURATION
    public static final int MIN_VOWEL = 9;  //min training set: 45 frames
	public static final int MAX_VOWEL = 92; //max training set: 459 frames
    public static final int MIN_GAP_START = 10;
    public static final int MIN_GAP_END = 10;

//    public static final double MEAN_VOWEL_LENGTH = 36.7472; //pre calculated from the training set
//    public static final double MAX_VOWEL_LENGTH = 33; //pre calculated from the training set
//    public static final double STD_VOWEL_LENGTH = 11.0618; //pre calculated from the training set

    public static final double MEAN_VOWEL_LENGTH = 41.787; //pre calculated from the training set
    public static final double MAX_VOWEL_LENGTH = 38; //pre calculated from the training set
    public static final double STD_VOWEL_LENGTH = 12.918; //pre calculated from the training set
}