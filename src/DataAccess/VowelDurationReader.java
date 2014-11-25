package DataAccess;

import Constants.Consts;
import Constants.ErrorConstants;
import Data.Entities.Example;
import Data.Entities.Example2D;
import Data.Factory;
import Data.InstancesContainer;
import Data.Entities.Vector;
import Data.Logger;
import Helpers.ConverterHelplers;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

//Paths reader
public class VowelDurationReader implements Reader{

    public InstancesContainer readData(String path, String dataSpliter, String valueSpliter)
    {
        ArrayList<ArrayList<String>> data = readFile(path,dataSpliter);
        InstancesContainer instances = Factory.getInstanceContainer(1);
        instances.setPaths(data);
        return instances;
    }

    //read only one example and it's labels
    public Example readExample(ArrayList<String> paths) throws Exception{

        ArrayList<ArrayList<String>> features = readFile(paths.get(0), Consts.SPACE);
        ArrayList<ArrayList<String>> labels = readFile(paths.get(1), Consts.SPACE);

        Example example = Factory.getExample(2);

        //extract the labels
        String label = labels.get(1).get(0)+Consts.CLASSIFICATION_SPLITTER+labels.get(1).get(1);
        example.setLabel(label);

        //extract the features
        for (int i=0 ; i<features.size() ; i++){

            Vector vector = new Vector();
            for(int j=0 ; j<features.get(i).size() ; j++){

                if(features.get(i).get(j).equalsIgnoreCase("nan")) {
                    vector.put(j, 0.0);
                    continue;
                }

                if(!ConverterHelplers.tryParseDouble(features.get(i).get(j))){
                    Logger.error("Error converting example.");
                    return null;
                }
                double num = Double.parseDouble(features.get(i).get(j));
                vector.put(j,num);
            }
            example.getFeatures2D().add(vector);
        }

        example.sizeOfVector = example.getFeatures2D().size();
        return example;
    }

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
}
