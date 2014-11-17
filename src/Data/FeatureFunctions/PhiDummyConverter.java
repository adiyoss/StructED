package Data.FeatureFunctions;

import BL.Kernels.Kernel;
import Constants.Consts;
import Constants.ErrorConstants;
import Data.Entities.Example;
import Data.Entities.Vector;
import Data.Factory;

public class PhiDummyConverter implements PhiConverter {

    int sizeOfVector = 4;

	@Override
	//return null on error
	public Example convert(Example vector, String label, Kernel kernel) {

        try{
            Example newVector = Factory.getExample(0);

            String labelValues[] = label.split(Consts.CLASSIFICATION_SPLITTER);
            int i = Integer.parseInt(labelValues[0]);
            int j = Integer.parseInt(labelValues[1]);

            //compute the difference/gradients near the start and end points
            double diff_1_Start = Math.abs(vector.getFeatures().get(i)-vector.getFeatures().get(i-1));
            double diff_1_End = Math.abs(vector.getFeatures().get(j)-vector.getFeatures().get(j+1));

            //compute the difference/gradients near the start and end points
            double diff_2_Start = Math.abs(vector.getFeatures().get(i)-vector.getFeatures().get(i-2));
            double diff_2_End = Math.abs(vector.getFeatures().get(j)-vector.getFeatures().get(j+2));

            double avg = 0;
            for(int k=i ; k<j ; k++)
                avg+=vector.getFeatures().get(k);

            if(j-i <= 0){
                System.err.println("Convert single vector: "+ ErrorConstants.GENERAL_ERROR+ ErrorConstants.ZERO_DIVIDING);
                return null;
            }
            avg /= (double)(j-i);

            double gapEnd = 0;
            for(int k=j ; k<j+Consts.MIN_GAP_END_DUMMY ; k++)
                gapEnd+=vector.getFeatures().get(k);

            gapEnd/=(double)Consts.MIN_GAP_END_DUMMY;

            double gapStart = 0;
            for(int k=i-Consts.MIN_GAP_START_DUMMY+1 ; k<=i ; k++)
                gapStart+=vector.getFeatures().get(k);

            gapStart/=(double)Consts.MIN_GAP_START_DUMMY;

            Vector tmpVector = new Vector();
            tmpVector.put(0, diff_1_Start);
            tmpVector.put(1, diff_1_End);
            tmpVector.put(2, diff_2_Start);
            tmpVector.put(3, diff_2_End);
            tmpVector.put(4, avg-gapStart);
            tmpVector.put(5,avg-gapEnd);

            if(kernel!=null)
                tmpVector = kernel.convertVector(tmpVector, sizeOfVector);

            newVector.setFeatures(tmpVector);

            return newVector;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
	}
}
