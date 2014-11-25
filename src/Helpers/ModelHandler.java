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

import Constants.Consts;
import Data.Entities.Example;
import Data.Factory;
import Data.InstancesContainer;
import Data.Entities.Vector;
import DataAccess.Reader;

import java.text.ParseException;
import java.util.*;

public class ModelHandler {

    public static Vector setWeights(int type, String path) {

        Vector W = new Vector();
        try{
            Reader reader = Factory.getReader(0);
            if(type == 0)
                W.put(0, 0.0);
            else if(type == 1){
                ArrayList<ArrayList<String>> wData = reader.readFile(path, Consts.SPACE);
                W = convert2Weights(wData);
            }
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return W;
    }

    public static Vector convert2Weights(ArrayList<ArrayList<String>> data) throws ParseException
    {
        Vector result = new Vector();
        for(int i=0 ; i<data.size() ; i++)
        {
            for(int j=0 ; j<data.get(i).size() ; j++){
                String values[] = data.get(i).get(j).split(Consts.COLON_SPLITTER);
                result.put(Integer.parseInt(values[0]), Double.parseDouble(values[1]));
            }
        }
        return result;
    }

    //preforming random shuffle on the data instances
    public static InstancesContainer randomShuffle(InstancesContainer instances)
    {
        Random rnd = new Random();

        //random shuffle the paths
        if(instances.getPaths().size()!=0) {
            for (int i = instances.getPaths().size() - 1; i > 0; i--) {
                int index = rnd.nextInt(i + 1);
                // Simple swap
                ArrayList<String> tmp = instances.getPaths().get(index);
                instances.getPaths().set(index, instances.getPaths().get(i));
                instances.getPaths().set(i, tmp);
            }
        }

        //random shuffle the instances
        for (int i = instances.getInstances().size()-1 ; i>0 ; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            Example tmp = instances.getInstances().get(index);
            instances.getInstances().set(index, instances.getInstances().get(i));
            instances.getInstances().set(i, tmp);
        }
        return instances;
    }

    public static HashMap<Integer,Double> printMul(Vector v1, Vector v2)
    {
        HashMap<Integer,Double> result = new HashMap<Integer,Double>();
        for(Map.Entry<Integer,Double> entry : v1.entrySet()){
            if(v2.containsKey(entry.getKey())){
                result.put(entry.getKey(), entry.getValue()*v2.get(entry.getKey()));
            }
        }

        return result;
    }

    public static TreeMap<Integer,Double> sortMul(Vector v1, Vector v2)
    {
        TreeMap<Integer,Double> result = new TreeMap<Integer,Double>();
        for(Map.Entry<Integer,Double> entry : v1.entrySet()){
            if(v2.containsKey(entry.getKey())){
                result.put(entry.getKey(), entry.getValue()*v2.get(entry.getKey()));
            }
        }

        return result;
    }
}
