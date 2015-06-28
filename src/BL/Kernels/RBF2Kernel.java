/*
 * StructED - Machine Learning, Structured Prediction Package written in Java.
 * Copyright (C) 2014 Yossi Adi, E-Mail: yossiadidrum@gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package BL.Kernels;

import Constants.ConfigParameters;
import Data.Entities.Vector;

public class RBF2Kernel implements IKernel {

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