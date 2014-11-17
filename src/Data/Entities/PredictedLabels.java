package Data.Entities;

import Helpers.Comperators.MapKeyComparatorAscending;
import Helpers.Comperators.MapValueComparatorAscending;
import Helpers.Comperators.MapValueComparatorDescending;

import java.util.TreeMap;

//this class is used for name alias
public class PredictedLabels extends TreeMap<String,Double>{

    //default c'tor
    public PredictedLabels(){
    }

    public PredictedLabels(MapValueComparatorAscending vc){
        super(vc);
    }

    public PredictedLabels(MapValueComparatorDescending vc){
        super(vc);
    }

    public PredictedLabels(MapKeyComparatorAscending vc){
        super(vc);
    }
}
