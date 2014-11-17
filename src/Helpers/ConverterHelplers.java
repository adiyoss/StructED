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
