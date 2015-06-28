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

package Data.FeatureFunctions;

import BL.Kernels.IKernel;
import Data.Entities.Example;
import Data.Factory;
import Data.Entities.Vector;

public class FeatureFunctionsRank implements IFeatureFunctions {

    public Example convert(Example vector, String label, IKernel kernel)
    {
        try{
            //initialize result object
            Example phiData = Factory.getExample(0);

            int intLabel = Integer.parseInt(label);
            phiData.setLabel(label);
            Vector features;
            features = vector.getFeatures2D().get(intLabel);

            if(kernel != null)
                features = kernel.convertVector(vector.getFeatures2D().get(intLabel), vector.getFeatures2D().get(intLabel).size());

            phiData.setFeatures(features);
            return phiData;

        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
