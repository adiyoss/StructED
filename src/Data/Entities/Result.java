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

