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

package com.structed.utils;

public class ConverterHelplers {

	/**
	 * parser validation for integer type
	 * true if s is integer otherwise false
	 * @param s
	 * @return
	 */
	public static boolean tryParseInt(String s)
	{
		try{
			Integer.parseInt(s);
			return true;
		}catch (Exception e) {
			return false;
		}
	}

	/**
	 * parser validation for integer type
	 * true if s is double otherwise false
	 * @param s
	 * @return
	 */
	public static boolean tryParseDouble(String s)
	{
		try{
			Double.parseDouble(s);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	
//	//calculate the offset for the chunks reading
//	public static int calculateOffset(int offset, ArrayList<ArrayList<String>> tmp)
//	{
//		//get the offset
//		for(int i=0 ; i<tmp.size() ; i++)
//		{
//			for(int j=0 ; j<tmp.get(i).size()-1 ; j++)
//				offset += tmp.get(i).get(j).length()+1;
//			offset += tmp.get(i).get(tmp.get(i).size()-1).length()+1;
//		}
//
//		return offset;
//	}
}
