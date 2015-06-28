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

import Data.Entities.Vector;

public class Poly2Kernel implements IKernel {

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