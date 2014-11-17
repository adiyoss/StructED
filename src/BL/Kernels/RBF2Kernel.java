package BL.Kernels;

import Constants.ConfigParameters;
import Data.Entities.Vector;

import java.util.HashSet;
import java.util.Map;

public class RBF2Kernel implements Kernel {

    private double sigma = ConfigParameters.getInstance().SIGMA;//default value

    public Vector convertVector(Vector vector, int vectorSize)
    {
        Vector newVector = new Vector();
        HashSet<Integer> cache = new HashSet<Integer>(); // used for indexing the feature values
        double x_norm2 = 0.0;
        double sqrSigma = sigma*sigma;

        // exp(-||x||^2/(2*sigma2))
        for (Integer key : vector.keySet())
            x_norm2+=vector.get(key)*vector.get(key);

        double exp2_coef = Math.exp(-x_norm2/(2.0*sqrSigma));

        // exp(-<x,z>) expansion
        newVector.put(0,exp2_coef);

        double tmp_coef = 1/sigma;
        double tmp_coef_2 = 1.0/(Math.sqrt(2.0)*sqrSigma);

        for (Map.Entry<Integer, Double> entry : vector.entrySet()) {
            newVector.put(1+entry.getKey(), entry.getValue() * tmp_coef * exp2_coef);
            newVector.put(1+vectorSize+entry.getKey(), entry.getValue() * entry.getValue() * tmp_coef_2 * exp2_coef);
        }

        tmp_coef = 1.0/(sqrSigma);

        for (Map.Entry<Integer, Double> firstEntry : vector.entrySet()) {
            cache.add(firstEntry.getKey());
            for (Map.Entry<Integer, Double> secondEntry : vector.entrySet()) {
                if(!cache.contains(secondEntry.getKey()))
                    newVector.put(1+2*vectorSize+(firstEntry.getKey()*secondEntry.getKey())/2, firstEntry.getValue() * secondEntry.getValue() * tmp_coef * exp2_coef);
            }
        }
        return newVector;
    }

    public void setSigma(double sigma){this.sigma = sigma;}
}