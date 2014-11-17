package Helpers.Comperators;

import java.util.Comparator;
import java.util.Map;

public class MapKeyComparatorAscending implements Comparator<String> {

    Map<String, Double> base;

    public MapKeyComparatorAscending(Map<String, Double> base) {
        this.base = base;
    }

    @Override
    public int compare(String a, String b) {
        if (a.compareToIgnoreCase(b)>0) {
            return 1;
        } else {
            return -1;
        }
    }
}
