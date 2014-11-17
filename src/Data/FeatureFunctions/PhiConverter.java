package Data.FeatureFunctions;

import BL.Kernels.Kernel;
import Data.Entities.Example;

public interface PhiConverter {
	public Example convert(Example vector, String label, Kernel kernel);
}
