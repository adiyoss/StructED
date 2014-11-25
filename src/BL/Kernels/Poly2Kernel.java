package BL.Kernels;

import Data.Entities.Vector;

public class Poly2Kernel implements Kernel {

    //new dimension = d + d*(d-1)/2.0
    public Vector convertVector(Vector vector, int vectorSize)
    {
        Vector expansionVector = new Vector();

        double sqrt_2 = Math.sqrt(2);
        int loc = 0;

        //============================================//
        for (int j=0 ; j<vectorSize ; j++) {
            if(vector.containsKey(j))
                expansionVector.put(loc, vector.get(j)*vector.get(j));
            loc++;
        }

        //============================================//
        for (int j = 0 ; j<vectorSize-1 ; j++) {
            for (int k=j+1 ; k<vectorSize ; k++) {
                if(vector.containsKey(j) && vector.containsKey(k))
                    expansionVector.put(loc, sqrt_2*vector.get(j)*vector.get(k));
                loc++;
            }
        }

        return expansionVector;
    }
}