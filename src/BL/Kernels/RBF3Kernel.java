package BL.Kernels;

import Constants.ConfigParameters;
import Data.Entities.Vector;

import java.util.HashSet;
import java.util.Map;

public class RBF3Kernel implements Kernel {

    private double sigma = ConfigParameters.getInstance().SIGMA;//default value

    //TODO check this thing one more time
    public Vector convertVector(Vector vector, int vectorSize)
    {
        Vector newVector = new Vector();
        HashSet<Integer> cache = new HashSet<Integer>(); // used for indexing the feature values

        double sqrSigma = sigma*sigma;
        double threeSigma = sigma*sigma*sigma;

        // exp(-||x||^2/sigma2)
        double x_norm2 = 0;
        for (Integer key : vector.keySet())
            x_norm2 += vector.get(key)*vector.get(key);

        double exp2_coef = Math.exp(-x_norm2/(2.0*sqrSigma));

        // exp(-x) expansion
        int location = 0;
        double tmp_coef;
        newVector.put(0,exp2_coef);
        location++;

        //============================================//
        tmp_coef = 1/sigma;
        double tmp_coef_2 = 1/(Math.sqrt(2)*sqrSigma);
        double tmp_coef_3 = 1.0/(Math.sqrt(6.0)*threeSigma);
        for (Map.Entry<Integer, Double> entry : vector.entrySet()){
            newVector.put(1+entry.getKey(),entry.getValue()*tmp_coef*exp2_coef);

            newVector.put(1+vectorSize+entry.getKey(),entry.getValue()*entry.getValue()*tmp_coef_2*exp2_coef);

            newVector.put(1+(2*vectorSize)+(vectorSize*(vectorSize-1))/2+entry.getKey(),entry.getValue()*entry.getValue()*entry.getValue()*tmp_coef_3*exp2_coef);
        }
        //============================================//

        //============================================//
        //TODO ask yossi about this
        tmp_coef = 1.0/(sqrSigma);
        tmp_coef_2 = Math.sqrt(3.0)/(Math.sqrt(6.0)*threeSigma);

        for (Map.Entry<Integer, Double> firstEntry : vector.entrySet()){
            cache.add(firstEntry.getKey());
            for (Map.Entry<Integer, Double> secondEntry : vector.entrySet()){
                if(!cache.contains(secondEntry.getKey())) {
                    newVector.put(1+(2*vectorSize)+(firstEntry.getKey()*secondEntry.getKey())/2, firstEntry.getValue() * secondEntry.getValue() * tmp_coef * exp2_coef);
                    newVector.put(1+(3*vectorSize)+(vectorSize*(vectorSize-1))/2+(firstEntry.getKey()*secondEntry.getKey())/2, firstEntry.getValue() * firstEntry.getValue() * secondEntry.getValue() * tmp_coef_2 * exp2_coef);
                    newVector.put(1+(3*vectorSize)+(vectorSize*(vectorSize-1))+(firstEntry.getKey()*secondEntry.getKey())/2, firstEntry.getValue() * secondEntry.getValue() * secondEntry.getValue() * tmp_coef_2 * exp2_coef);
                }
            }
        }
        //============================================//

        cache.clear();
        HashSet<Integer> helperCache = new HashSet<Integer>(); // used for indexing the feature values


        //============================================//
        tmp_coef = 1.0/(threeSigma);
        for (Map.Entry<Integer, Double> firstEntry : vector.entrySet()) {
            cache.add(firstEntry.getKey());
            for (Map.Entry<Integer, Double> secondEntry : vector.entrySet()) {
                helperCache.add(secondEntry.getKey());
                if (!cache.contains(secondEntry.getKey())) {
                    for (Map.Entry<Integer, Double> thirdEntry : vector.entrySet()) {
                        if (!cache.contains(thirdEntry.getKey()) && !helperCache.contains(thirdEntry.getKey()))
                            newVector.put(1+3*vectorSize+3*(vectorSize*(vectorSize-1))/2 + (firstEntry.getKey()*(secondEntry.getKey()-1)*(thirdEntry.getKey()-2))/6, firstEntry.getValue() * secondEntry.getValue() * thirdEntry.getValue() * tmp_coef * exp2_coef);

                        location++;
                    }
                }
            }
        }
        //============================================//
        return newVector;

    }

    public void setSigma(double sigma){this.sigma = sigma;}
}
