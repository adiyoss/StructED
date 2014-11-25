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

package BL;

import BL.Algorithms.AlgorithmUpdateRule;
import BL.Kernels.Kernel;
import BL.Prediction.Prediction;
import BL.TaskLoss.TaskLoss;
import Data.FeatureFunctions.PhiConverter;

import java.util.List;

//this class contains all the relevant data to the classifier
public class ClassifierData {

    public TaskLoss taskLoss;
    public AlgorithmUpdateRule algorithmUpdateRule;
    public Prediction predict;
    public Kernel kernel;
    public PhiConverter phi;
    public List<Double> arguments;
    public int iteration = 0;

}
