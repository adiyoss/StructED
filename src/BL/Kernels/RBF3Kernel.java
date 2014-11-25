package BL.Kernels;

import Constants.ConfigParameters;
import Data.Entities.Vector;

public class RBF3Kernel implements Kernel {

    private double sigma = ConfigParameters.getInstance().SIGMA;//default value

    //new dimension = 1 + 3.0*d + 3.0*d*(d-1)/2.0 + d*(d-1)*(d-2)/6.0
    public Vector convertVector(Vector vector, int vectorSize)
    {
        Vector expansionVector = new Vector();

        double sigmaSqr = sigma*sigma;
        double sigmaThrd = sigma*sigma*sigma;

        // exp(-||x||^2/sigma2)
        double x_norm2 = 0.0;
        for (int j=0 ; j<vectorSize ; j++) {
            if(vector.containsKey(j))
                x_norm2 += vector.get(j) * vector.get(j);
        }
        double exp2_coef = Math.exp(-x_norm2/(2.0*sigmaSqr));

        // exp(-x) expansion
        int loc = 0;
        double tmp_coef;
        expansionVector.put(loc,exp2_coef);
        loc++;

        //============================================//
        tmp_coef = 1/sigma;
        for (int j=0 ; j<vectorSize ; j++){
            if(vector.containsKey(j))
                expansionVector.put(loc,vector.get(j) * tmp_coef * exp2_coef);
            loc++;
        }

        //============================================//
        tmp_coef = 1.0/(Math.sqrt(2.0)*sigmaSqr);
        for (int j=0 ; j<vectorSize ; j++){
            if(vector.containsKey(j))
                expansionVector.put(loc,vector.get(j) * vector.get(j) * tmp_coef * exp2_coef);
            loc++;
        }

        //============================================//
        tmp_coef = 1.0/(sigmaSqr);
        for (int j=0 ; j<vectorSize-1 ; j++){
            for (int k=j+1 ; k<vectorSize ; k++){
                if(vector.containsKey(j) && vector.containsKey(k))
                    expansionVector.put(loc,vector.get(j) * vector.get(k) * tmp_coef * exp2_coef);
                loc++;
            }
        }

        //============================================//
        tmp_coef = 1.0/(Math.sqrt(6.0)*sigmaThrd);
        for (int j=0 ; j<vectorSize ; j++){
            if(vector.containsKey(j))
                expansionVector.put(loc,vector.get(j) * vector.get(j) * vector.get(j) * tmp_coef * exp2_coef);
            loc++;
        }

        //============================================//
        tmp_coef = Math.sqrt(3.0)/(Math.sqrt(6.0)*sigmaThrd);
        for (int j=0 ; j<vectorSize-1 ; j++){
            for (int k=j+1 ; k<vectorSize ; k++){
                if(vector.containsKey(j) && vector.containsKey(k)) {
                    expansionVector.put(loc, vector.get(j) * vector.get(j) * vector.get(k) * tmp_coef * exp2_coef);
                    loc++;
                    expansionVector.put(loc, vector.get(j) * vector.get(k) * vector.get(k) * tmp_coef * exp2_coef);
                    loc++;
                } else
                    loc+=2;
            }
        }

        //============================================//
        tmp_coef = 1.0/(sigmaThrd);
        for (int j=0 ; j<vectorSize-2 ; j++){
            for (int k=j+1 ; k<vectorSize-1 ; k++){
                for (int l=k+1 ; l<vectorSize ; l++){
                    if(vector.containsKey(j) && vector.containsKey(k) && vector.containsKey(l))
                        expansionVector.put(loc, vector.get(j) * vector.get(k) * vector.get(l) * tmp_coef * exp2_coef);
                    loc++;
                }
            }
        }

        return expansionVector;
    }

    public void setSigma(double sigma){this.sigma = sigma;}
}
