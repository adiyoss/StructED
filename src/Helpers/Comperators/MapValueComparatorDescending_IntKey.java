package Helpers.Comperators;

import java.util.Comparator;
import java.util.Map;

public class MapValueComparatorDescending_IntKey implements Comparator<Integer> {

    Map<Integer, Double> base;

    public MapValueComparatorDescending_IntKey(Map<Integer, Double> base) {
        this.base = base;
    }

    @Override
    public int compare(Integer a, Integer b) {
        if (base.get(a) <= base.get(b)) {
            return 1;
        } else {
            return -1;
        }
    }
}
