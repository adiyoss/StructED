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

package Data;

import BL.*;
import BL.Algorithms.*;
import BL.Kernels.Poly2Kernel;
import BL.Kernels.RBF2Kernel;
import BL.Kernels.RBF3Kernel;
import BL.Inference.*;
import BL.TaskLoss.*;
import Data.Entities.*;
import Data.FeatureFunctions.*;
import DataAccess.*;

import java.util.ArrayList;

public class Factory {
	
	//==============DATA ACCESS==============//
	//type is not supported
	//StandardReader getter object
	public static Reader getReader(int type){
        switch(type){
            case 0:
		        return new StandardReader();
            case 1:
                return new RankReader();
            case 2:
                return new VowelDurationReader();
            default:
                return new StandardReader();
        }
	}
	//type is not supported
	//Writer getter object
	public static Writer getWriter(int type){
        switch (type){
            case 0:
		        return new StandardWriter();
            case 1:
                return new RankWriter();
            default:
                return new StandardWriter();
        }
	}
	
	//type is not supported
	//Config File getter object
	public static ConfigFileGetter getConfigGetter(int type){
		return new ConfigFileGetter();
	}
	//=======================================//
	//===============DATA TYPES==============//
	//type is not supported
	//data vector getter object
	public static Example getExample(int type){
        switch(type){
            case 0:
                return new Example1D();

            case 1:
                return new Example2D();

            default:
                return new Example1D();
        }
	}

    //=======================================//
	//===============CLASSIFIER==============//
	//taskLossType = 0 - for Single Difference Task Loss
	//taskLossType = 1 - for Multi class Task Loss
	//updateType = 0 - for Passive Aggressive update
	//updateType = 1 - for SVM Pegasos update
	//updateType = 2 - use Direct Loss update
	//updateType = 3 - use CRF - Conditional Random Fields
	//updateType = 4 - use Ramp Loss update
	//updateType = 5 - use Probit Loss update
	public static Classifier getClassifier(int taskLossType, int updateType, int predictType, int kernelType, int phi, ArrayList<Double> arguments){
		
		Classifier classifier = new Classifier();
        classifier.classifierData = new ClassifierData();
        classifier.classifierData.arguments = new ArrayList<Double>();
		
		switch (taskLossType) {
			case 0:
                classifier.classifierData.taskLoss = new TaskLossVowelDuration();
				break;
			case 1:
                classifier.classifierData.taskLoss = new TaskLossMultiClass();
				break;
            case 2:
                classifier.classifierData.taskLoss = new TaskLossRank();
                break;
            case 3:
                classifier.classifierData.taskLoss = new TaskLossDummyData();
                break;
			default:
				return null;
		}

        switch (predictType) {
            case 0:
                classifier.classifierData.inference = new InferenceVowelDurationData();
                break;
            case 1:
                classifier.classifierData.inference = new InferenceMultiClass();
                break;
            case 2:
                classifier.classifierData.inference = new InferenceRanking();
                break;
            case 3:
                classifier.classifierData.inference = new InferenceDummyData();
                break;
            default:
                return null;
        }

        switch (phi) {
            case 0:
                classifier.classifierData.phi = new FeatureFunctionsVowelDuration();
                break;
            case 1:
                classifier.classifierData.phi = new FeatureFunctionsSparse();
                break;
            case 2:
                classifier.classifierData.phi = new FeatureFunctionsRank();
                break;
            case 3:
                classifier.classifierData.phi = new FeatureFunctionsDummy();
                break;
            default:
                return null;
        }

        switch (kernelType) {
            case 0:
                classifier.classifierData.kernel = new Poly2Kernel();
                break;
            case 1:
                classifier.classifierData.kernel = new RBF2Kernel();
                break;
            case 2:
                classifier.classifierData.kernel = new RBF3Kernel();
                break;
            default:
                classifier.classifierData.kernel = null;
        }

        IUpdateRule cls;
		switch (updateType) {
			case 0:
                cls = new PassiveAggressive();
				break;
			case 1:
                cls = new SVM();
				break;
			case 2:
                cls = new DirectLoss();
				break;
			case 3:
                cls = new CRF();
				break;
			case 4:
                cls = new RampLoss();
				break;
			case 5:
                cls = new ProbitLoss();
				break;
            case 6:
                cls = new Perceptron();
                break;
            case 7:
                cls = new OrbitLoss();
                break;
            case 8:
                cls = new RankSVM();
                break;
			default:
                cls = null;
		}

        classifier.classifierData.updateRule = cls;
        if (cls == null) throw new AssertionError();
        cls.init(arguments);
		return classifier;
	}

    public static InstancesContainer getInstanceContainer(int type){
        switch(type) {
            case 0:
                return new InstancesContainer();
            case 1:
                return new LazyInstancesContainer();
            default:
                return new InstancesContainer();
        }
    }
}
