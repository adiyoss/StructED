package BL.Algorithms;

import BL.ClassifierData;
import Data.Entities.Example;
import Data.Entities.Vector;

//this interface enable us to set many update versions and compare between them
public interface AlgorithmUpdateRule {
	//the arguments would be different from update to update
	public Vector update(Vector currentWeights, Example vector, ClassifierData classifierData);
}
