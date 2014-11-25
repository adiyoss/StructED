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

import BL.Kernels.Kernel;
import Constants.ConfigParameters;
import Data.Entities.Example;
import Data.Factory;
import Data.Entities.Vector;

public class PhiSparseConverter implements PhiConverter {

    int maxFeatures = 780;

	@Override
	public Example convert(Example vector, String label, Kernel kernel) {
        try{
            //parse the label
            int intLabel = Integer.parseInt(label);
            Example newVector = Factory.getExample(0);
            Vector tmpVector = new Vector();

            //run the phi function
            for(Integer feature : vector.getFeatures().keySet())
                tmpVector.put(feature+intLabel*maxFeatures,vector.getFeatures().get(feature));

            if(kernel!=null)
                tmpVector = kernel.convertVector(tmpVector, ConfigParameters.getInstance().VECTOR_SIZE);

            newVector.setFeatures(tmpVector);

            return newVector;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
	}

    //setter for the max feature parameter
    public void setMaxFeatures(int maxFeatures){
        this.maxFeatures = maxFeatures;
    }
}
