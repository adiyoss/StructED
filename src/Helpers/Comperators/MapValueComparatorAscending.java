package Helpers.Comperators;

import java.util.Comparator;
import java.util.Map;

public class MapValueComparatorAscending implements Comparator<String>{

    Map<String, Double> base;

    public MapValueComparatorAscending(Map<String, Double> base) {
        this.base = base;
    }

    @Override
    public int compare(String a, String b) {
        if (base.get(a) > base.get(b)) {
            return 1;
        } else {
            return -1;
        }
    }
}
