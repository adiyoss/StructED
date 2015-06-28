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

package BL.TaskLoss;

import Constants.Consts;

import java.util.List;

public class TaskLossDummyData implements ITaskLoss {

	@Override
	//max{0,|ys-ye - y's-y'e|-epsilon}
	public double computeTaskLoss(String predictClass, String actualClass, List<Double> params) {

        double epsilon = params.get(0);
		String predictValues[] = predictClass.split(Consts.CLASSIFICATION_SPLITTER);
		String actualClassValues[] = actualClass.split(Consts.CLASSIFICATION_SPLITTER);
	
		//calculate difference of each classification
		double predictRes = Double.parseDouble(predictValues[0]) - Double.parseDouble(predictValues[1]);
		double actualRes = Double.parseDouble(actualClassValues[0]) - Double.parseDouble(actualClassValues[1]);
		
		//subtract the epsilon		
		double absRes = Math.abs(predictRes-actualRes) - epsilon;
		
		//get the max from the absolute result minus epsilon and 0
		if(absRes > 0)
			return absRes;
		return 0;
	}
}
