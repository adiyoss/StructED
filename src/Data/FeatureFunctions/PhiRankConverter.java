package Data.FeatureFunctions;

import BL.Kernels.Kernel;
import Data.Entities.Example;
import Data.Factory;
import Data.Entities.Vector;

public class PhiRankConverter implements PhiConverter{

    public Example convert(Example vector, String label, Kernel kernel)
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
