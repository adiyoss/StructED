package BL.Kernels;

import Data.Entities.Vector;

import java.util.HashSet;
import java.util.Map;

public class Poly2Kernel implements Kernel {

    public Vector convertVector(Vector vector, int vectorSize)
    {
        Vector newVector = new Vector();

        double sqrt_2 = Math.sqrt(2);

        for (Map.Entry<Integer, Double> entry : vector.entrySet())
            newVector.put(entry.getKey(), entry.getValue()*entry.getValue());

        HashSet<Integer> cache = new HashSet<Integer>();
        for (Map.Entry<Integer, Double> firstEntry : vector.entrySet()) {
            cache.add(firstEntry.getKey());
            for (Map.Entry<Integer, Double> secondEntry : vector.entrySet()) {
                if(!cache.contains(secondEntry.getKey()))
                    newVector.put(vectorSize+(firstEntry.getKey()*secondEntry.getKey())/2, sqrt_2*firstEntry.getValue()*secondEntry.getValue());
            }
        }

        return newVector;
    }
}