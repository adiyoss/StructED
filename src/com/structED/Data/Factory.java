/*
 * The MIT License (MIT)
 *
 * StructED - Machine Learning Package for Structured Prediction
 *
 * Copyright (c) 2015 Yossi Adi, E-Mail: yossiadidrum@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.structed.data;


import com.structed.dal.*;
import com.structed.models.algorithms.*;
import com.structed.models.Classifier;
import com.structed.models.ClassifierData;
import com.structed.models.inference.InferenceDummyData;
import com.structed.models.inference.InferenceMultiClass;
import com.structed.models.inference.InferenceRanking;
import com.structed.models.inference.InferenceVowelDurationData;
import com.structed.models.kernels.Poly2Kernel;
import com.structed.models.kernels.RBF2Kernel;
import com.structed.models.kernels.RBF3Kernel;

import com.structed.data.entities.Example;
import com.structed.data.entities.Example1D;
import com.structed.data.entities.Example2D;
import com.structed.data.featurefunctions.FeatureFunctionsDummy;
import com.structed.data.featurefunctions.FeatureFunctionsRank;
import com.structed.data.featurefunctions.FeatureFunctionsSparse;
import com.structed.data.featurefunctions.FeatureFunctionsVowelDuration;
import com.structed.models.loss.TaskLossDummyData;
import com.structed.models.loss.TaskLossMultiClass;
import com.structed.models.loss.TaskLossRank;
import com.structed.models.loss.TaskLossVowelDuration;

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
                return new LazyReader();
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

//    //=======================================//
//	//===============CLASSIFIER==============//
//	//taskLossType = 0 - for Single Difference Task Loss
//	//taskLossType = 1 - for Multi class Task Loss
//	//updateType = 0 - for Passive Aggressive update
//	//updateType = 1 - for SVM Pegasos update
//	//updateType = 2 - use Direct Loss update
//	//updateType = 3 - use CRF - Conditional Random Fields
//	//updateType = 4 - use Ramp Loss update
//	//updateType = 5 - use Probit Loss update
//	public static Classifier getClassifier(int taskLossType, int updateType, int predictType, int kernelType, int phi, ArrayList<Double> arguments){
//
//		Classifier classifier = new Classifier();
//        classifier.classifierData = new ClassifierData();
//        classifier.classifierData.arguments = new ArrayList<Double>();
//
//		switch (taskLossType) {
//			case 0:
//                classifier.classifierData.taskLoss = new TaskLossVowelDuration();
//				break;
//			case 1:
//                classifier.classifierData.taskLoss = new TaskLossMultiClass();
//				break;
//            case 2:
//                classifier.classifierData.taskLoss = new TaskLossRank();
//                break;
//            case 3:
//                classifier.classifierData.taskLoss = new TaskLossDummyData();
//                break;
//			default:
//				return null;
//		}
//
//        switch (predictType) {
//            case 0:
//                classifier.classifierData.inference = new InferenceVowelDurationData();
//                break;
//            case 1:
//                classifier.classifierData.inference = new InferenceMultiClass();
//                break;
//            case 2:
//                classifier.classifierData.inference = new InferenceRanking();
//                break;
//            case 3:
//                classifier.classifierData.inference = new InferenceDummyData();
//                break;
//            default:
//                return null;
//        }
//
//        switch (phi) {
//            case 0:
//                classifier.classifierData.phi = new FeatureFunctionsVowelDuration();
//                break;
//            case 1:
//                classifier.classifierData.phi = new FeatureFunctionsSparse();
//                break;
//            case 2:
//                classifier.classifierData.phi = new FeatureFunctionsRank();
//                break;
//            case 3:
//                classifier.classifierData.phi = new FeatureFunctionsDummy();
//                break;
//            default:
//                return null;
//        }
//
//        switch (kernelType) {
//            case 0:
//                classifier.classifierData.kernel = new Poly2Kernel();
//                break;
//            case 1:
//                classifier.classifierData.kernel = new RBF2Kernel();
//                break;
//            case 2:
//                classifier.classifierData.kernel = new RBF3Kernel();
//                break;
//            default:
//                classifier.classifierData.kernel = null;
//        }
//
//        IUpdateRule cls;
//		switch (updateType) {
//			case 0:
//                cls = new PassiveAggressive();
//				break;
//			case 1:
//                cls = new SVM();
//				break;
//			case 2:
//                cls = new DirectLoss();
//				break;
//			case 3:
//                cls = new CRF();
//				break;
//			case 4:
//                cls = new RampLoss();
//				break;
//			case 5:
//                cls = new ProbitLoss();
//				break;
//            case 6:
//                cls = new Perceptron();
//                break;
//            case 7:
//                cls = new OrbitLoss();
//                break;
//            case 8:
//                cls = new RankSVM();
//                break;
//			default:
//                cls = null;
//		}
//
//        classifier.classifierData.updateRule = cls;
//        if (cls == null) throw new AssertionError();
//        cls.init(arguments);
//		return classifier;
//	}

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
