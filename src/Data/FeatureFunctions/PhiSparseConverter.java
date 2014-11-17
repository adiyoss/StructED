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
}
