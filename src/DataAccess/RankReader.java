package DataAccess;

import Constants.ErrorConstants;
import Data.Entities.Example;
import Data.Factory;
import Data.InstancesContainer;
import Data.Entities.Vector;
import Data.Logger;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RankReader implements Reader{

    public InstancesContainer readData(String path, String dataSpliter, String valueSpliter)
    {
        ArrayList<ArrayList<String>> data = readFile(path,dataSpliter);
        ArrayList<Example> vectors = new ArrayList<Example>();
        int i=0;
        String prevQid = "";

        while(i<data.size())
        {
            Example vector = Factory.getExample(1);
            ArrayList<Vector> totalQuery = new ArrayList<Vector>();
            ArrayList<Integer> labels = new ArrayList<Integer>();
            String[] qidValues = data.get(i).get(1).split(valueSpliter);
            do{

                Vector tmp = new Vector();
                labels.add(Integer.parseInt(data.get(i).get(0)));

                for(int j=2 ; j<data.get(i).size()-3 ; j++){
                    String[] values = data.get(i).get(j).split(valueSpliter);
                    tmp.put(Integer.parseInt(values[0])-1,Double.parseDouble(values[1]));
                }

                totalQuery.add(tmp);
                prevQid = qidValues[1];
                i++;

                if(i>=data.size())
                    break;

                qidValues = data.get(i).get(1).split(valueSpliter);

            } while (prevQid.equalsIgnoreCase(qidValues[1]));

            vector.setFeatures2D(totalQuery);
            vector.setLabels2D(labels);
            vector.sizeOfVector = data.get(i-1).size()-1;
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
}
