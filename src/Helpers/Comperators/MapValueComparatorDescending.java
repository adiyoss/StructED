package Helpers.Comperators;

import java.util.Comparator;
import java.util.Map;

public class MapValueComparatorDescending implements Comparator<String>{

    Map<String, Double> base;

    public MapValueComparatorDescending(Map<String, Double> base) {
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
