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

package DataAccess;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import Constants.ErrorConstants;
import Data.Entities.Example;
import Data.Factory;
import Data.InstancesContainer;
import Data.Entities.Vector;
import Data.Logger;

public class StandardReader implements Reader{
	
	//default C'tor
	public StandardReader() {
	}

	//read the file
	//each attribute in each row should be splited with , 
	public ArrayList<ArrayList<String>> readFileInChunks(String path, String spliter, int offset, int chunckSize)
	{
		//result object
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		
		try{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream(path);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			br.skip(offset);
			
			String strLine;
			int j=0;
			//Read File Line By Line
			while ((strLine = br.readLine())!=null && j<chunckSize)   {				
				//skip on empty line
				if(strLine.equalsIgnoreCase("")){
					j++;
					continue;
				}
				
				String values[] = strLine.split(spliter);
				ArrayList<String> row = new ArrayList<String>();
				for(int i=0 ; i<values.length ; i++)
					//Print the content on the console
					row.add(values[i]);
				data.add(row);
				
				j++;
			}
			//Close the input stream
			br.close();
			in.close();
			fstream.close();
			
		} catch (Exception e) {//Catch exception if any
            Logger.error(ErrorConstants.GENERAL_ERROR);
			e.printStackTrace();
			return null;
		}
		
		return data;
	}

    public InstancesContainer readData(String path, String dataSpliter, String valueSpliter)
    {
        ArrayList<ArrayList<String>> data = readFile(path,dataSpliter);
        ArrayList<Example> vectors = new ArrayList<Example>();

        for(int i=0 ; i<data.size() ; i++)
        {
            Example vector = Factory.getExample(0);
            Vector tmp = new Vector();
            for(int j=1 ; j<data.get(i).size() ; j++){
                String[] values = data.get(i).get(j).split(valueSpliter);
                tmp.put(Integer.parseInt(values[0]),Double.parseDouble(values[1]));
            }

            vector.setFeatures(tmp);
            vector.setLabel(data.get(i).get(0));
            vector.sizeOfVector = data.get(i).size()-1;
            vectors.add(vector);
        }

        InstancesContainer instances = Factory.getInstanceContainer(0);
        instances.setInstances(vectors);
        return instances;
    }

    //read the file
    //each attribute in each row should be splited with ,
    public ArrayList<ArrayList<String>> readFile(String path, String spliter)
    {
        //result object
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

        try{
            // Open the file that is the first
            // command line parameter
            FileInputStream fstream = new FileInputStream(path);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;
            //Read File Line By Line
            while ((strLine = br.readLine())!=null)   {
                //skip on empty line
                if(strLine.equalsIgnoreCase("")){
                    continue;
                }

                String values[] = strLine.split(spliter);
                ArrayList<String> row = new ArrayList<String>();
                for(int i=0 ; i<values.length ; i++)
                    //Print the content on the console
                    row.add(values[i]);
                data.add(row);
            }
            //Close the input stream
            br.close();
            in.close();
            fstream.close();

        } catch (Exception e) {//Catch exception if any
            Logger.error(ErrorConstants.GENERAL_ERROR);
            e.printStackTrace();
            return null;
        }

        return data;
    }

    public ArrayList<String> readPathes(String path)
    {
        //result object
        ArrayList<String> data = new ArrayList<String>();

        try{
            // Open the file that is the first
            // command line parameter
            FileInputStream fstream = new FileInputStream(path);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;
            //Read File Line By Line
            while ((strLine = br.readLine())!=null)   {
                //skip on empty line
                if(strLine.equalsIgnoreCase("")){
                    continue;
                }
                data.add(strLine);
            }
            //Close the input stream
            br.close();
            in.close();
            fstream.close();

        } catch (Exception e) {//Catch exception if any
            Logger.error(ErrorConstants.GENERAL_ERROR);
            e.printStackTrace();
            return null;
        }

        return data;
    }
}

