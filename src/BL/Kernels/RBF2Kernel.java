package BL.Kernels;

import Constants.ConfigParameters;
import Data.Entities.Vector;

public class RBF2Kernel implements Kernel {

    private double sigma = ConfigParameters.getInstance().SIGMA;//default value

    //new dimension = 1 + 2.0*d + d*(d-1)/2.0
    public Vector convertVector(Vector vector, int vectorSize)
    {
        Vector expansionVector = new Vector();
        double x_norm2 = 0.0;
        double sigmaSqr = sigma*sigma;

        // exp(-||x||^2/(2*sigma2))
        for (int j=0 ; j<vectorSize ; j++)  {
            if(vector.containsKey(j))
                x_norm2 += vector.get(j)*vector.get(j);
        }
        double exp2_coef = Math.exp(-x_norm2/(2.0*sigmaSqr));

        // exp(-<x,z>) expansion
        int loc = 0;
        double tmp_coef;
        expansionVector.put(loc, exp2_coef);
        loc++;

        //============================================//
        tmp_coef = 1/sigma;
        for (int j=0 ; j<vectorSize ; j++){
            if(vector.containsKey(j))
                expansionVector.put(loc,vector.get(j)*tmp_coef*exp2_coef);
            loc++;
        }

        //============================================//
        tmp_coef = 1.0/(Math.sqrt(2.0)*sigmaSqr);
        for (int j=0 ; j<vectorSize ; j++)  {
            if(vector.containsKey(j))
                expansionVector.put(loc,vector.get(j)*vector.get(j)*tmp_coef*exp2_coef);
            loc++;
        }

        //============================================//
        tmp_coef = 1.0/(sigmaSqr);
        for (int j=0 ; j<vectorSize-1 ; j++) {
            for (int k=j+1 ; k<vectorSize ; k++) {
                if(vector.containsKey(j) && vector.containsKey(k))
                    expansionVector.put(loc,vector.get(j)*vector.get(k)*tmp_coef*exp2_coef);
                loc++;
            }
        }

        return expansionVector;
    }

    public void setSigma(double sigma){this.sigma = sigma;}
}