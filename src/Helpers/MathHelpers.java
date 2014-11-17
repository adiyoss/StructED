package Helpers;

import Constants.ErrorConstants;
import Data.Entities.Vector;
import Data.Logger;

public class MathHelpers {

	//multiple two double vectors
	public static double multipleVectors(Vector v1, Vector v2)
	{
		double result = 0;

		//validation
		if(v1.size() == 0 || v2.size() == 0) {
            Logger.error(ErrorConstants.MULTIPLE_VECTORS_EMPTY_ERROR);
			return 0;
		}

        if(v1.size() <= v2.size()) {
            //multiple the vectors
            for (Integer key : v1.keySet()) {
                if (v2.containsKey(key))
                    result += (v1.get(key) * v2.get(key));
            }
        } else {
            //multiple the vectors
            for (Integer key : v2.keySet()) {
                if (v1.containsKey(key))
                    result += (v1.get(key) * v2.get(key));
            }
        }

		return result;
	}

	//add scalar to a double vector
	public static Vector addScalar2Vectors(Vector v1, double scalar)
	{
        Vector result = new Vector();

		if(v1.size() == 0) {
			Logger.error(ErrorConstants.MULTIPLE_VECTORS_EMPTY_ERROR);
			return null;
		}

		//multiple the vectors
		for(Integer key : v1.keySet())
			result.put(key,v1.get(key) + scalar);

		return result;
	}

	//add scalar to a double vector
	public static Vector mulScalarWithVectors(Vector v1, double scalar)
	{
        Vector result = new Vector();

		if(v1.size() == 0) {
			Logger.error(ErrorConstants.MULTIPLE_VECTORS_EMPTY_ERROR);
			return null;
		}

		//multiple the vectors
        for(Integer key : v1.keySet())
            result.put(key,v1.get(key)*scalar);

		return result;
	}

	//add scalar to a double vector
	public static Vector subtract2Vectors(Vector v1, Vector v2)
	{
        Vector result = new Vector ();

		//validation
		if(v1.size() == 0 || v2.size() == 0) {
			Logger.error(ErrorConstants.MULTIPLE_VECTORS_EMPTY_ERROR);
			return null;
		}

		//subtract the vectors
		for(Integer key : v1.keySet()){
            if(v2.containsKey(key))
			    result.put(key, v1.get(key)-v2.get(key));
            else
                result.put(key, v1.get(key));
        }

        for(Integer key : v2.keySet()){
            if(!v1.containsKey(key))
                result.put(key, (-1)*v2.get(key));
        }

		return result;
	}

	//add scalar to a double vector
	public static Vector add2Vectors(Vector v1, Vector v2)
	{
        Vector result = new Vector();

		//validation
        if(v1.size() == 0 || v2.size() == 0) {
            Logger.error(ErrorConstants.MULTIPLE_VECTORS_EMPTY_ERROR);
            return null;
        }

		//add the vectors
        for(Integer key : v1.keySet()){
            if(v2.containsKey(key))
                result.put(key, v1.get(key)+v2.get(key));
            else
                result.put(key, v1.get(key));
        }

        for(Integer key : v2.keySet()){
            if(!v1.containsKey(key))
                result.put(key, v2.get(key));
        }

		return result;
	}

    public static double sigmoid(double x) {
        return (1/( 1 + Math.pow(Math.E,(-1*x))));
    }
}
