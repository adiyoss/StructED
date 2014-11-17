package Data.Entities;

import java.util.HashMap;

//this class is used for name alias
public class Vector extends HashMap<Integer, Double> {

    public Vector(){}
    public Vector(Vector v){
        super(v);
    }
}
