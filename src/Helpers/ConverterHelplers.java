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

package Helpers;

import java.util.ArrayList;

public class ConverterHelplers {

	//parser validation for integer type
	//true if s is integer otherwise false
	public static boolean tryParseInt(String s)
	{
		try{
			Integer.parseInt(s);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	
	//parser validation for integer type
	//true if s is double otherwise false
	public static boolean tryParseDouble(String s)
	{
		try{
			Double.parseDouble(s);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	
	//calculate the offset for the chunks reading 
	public static int calculateOffset(int offset, ArrayList<ArrayList<String>> tmp)
	{
		//get the offset
		for(int i=0 ; i<tmp.size() ; i++)	
		{
			for(int j=0 ; j<tmp.get(i).size()-1 ; j++)
				offset += tmp.get(i).get(j).length()+1;
			offset += tmp.get(i).get(tmp.get(i).size()-1).length()+1;
		}
		
		return offset;
	}
}
