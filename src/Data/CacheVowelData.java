package Data;

import Data.Entities.Example;

import java.util.ArrayList;
import java.util.HashMap;

public class CacheVowelData {

    private static HashMap<Integer, ArrayList<ArrayList<Double>>> cache = new HashMap<Integer, ArrayList<ArrayList<Double>>>();

    //get the cumulative value of example of feature
    public static double getCumulativeValue(Example example, int frameIndex, int featureNumber)
    {
        double res = 0;
        if(frameIndex <= 0)
            res = cache.get(example.hashCode()).get(frameIndex).get(0);
        else if(frameIndex >= cache.get(example.hashCode()).size())
            res = cache.get(example.hashCode()).get(cache.get(example.hashCode()).size()-1).get(featureNumber);
        else if(cache.containsKey(example.hashCode()))
            res = cache.get(example.hashCode()).get(frameIndex).get(featureNumber);

        return res;
    }

    //update the cache
    public static void updateCache(Example example)
    {
        if(!cache.containsKey(example.hashCode()))
        {
            ArrayList<ArrayList<Double>> cumulativeValues = new ArrayList<ArrayList<Double>>();
            ArrayList<Double> tmp = new ArrayList<Double>();

            int size = example.getFeatures2D().get(0).size();
            for(int i=0 ; i<size ; i++)
                tmp.add(example.getFeatures2D().get(0).get(i));

            cumulativeValues.add(tmp);

            for(int i=1 ; i<example.sizeOfVector ; i++)
            {
                ArrayList<Double> vector = new ArrayList<Double>();
                for(int j=0 ; j<size ; j++)
                    vector.add(cumulativeValues.get(i-1).get(j) + example.getFeatures2D().get(i).get(j));

                cumulativeValues.add(vector);
            }

            cache.put(example.hashCode(),cumulativeValues);
        }
    }

    public static void clearCacheValues(){
        cache.clear();
    }
}
