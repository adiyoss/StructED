package Data.Entities;

import Constants.ErrorConstants;
import Data.Logger;

import java.util.*;

public class Result {

    private HashMap<String,Double> scores;
    private ArrayList<HashMap<String,Double>> eval;

    private static Result ourInstance = new Result();
    public static Result getInstance() {
        return ourInstance;
    }
    private Result() {
        scores = new HashMap<String,Double>();
    }

    public HashMap<String,Double> getScores() {
        return scores;
    }

    public void setScores(HashMap<String,Double> scores) {
        this.scores = scores;
    }

    //sorting the scores
    public TreeMap<String,Double> sortScores()
    {
        try{

            ValueComparator vc = new ValueComparator(scores);
            TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(vc);

            sorted_map.putAll(scores);
            return sorted_map;

        } catch (Exception e){
            Logger.error(ErrorConstants.SORT_ERROR);
            e.printStackTrace();
            return null;
        }
    }

    //comparator class for sorting the hash map values
    class ValueComparator implements Comparator<String> {

        Map<String, Double> base;

        ValueComparator(Map<String, Double> base) {
            this.base = base;
        }

        @Override
        public int compare(String a, String b) {
            if (base.get(a) <= base.get(b)) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}

