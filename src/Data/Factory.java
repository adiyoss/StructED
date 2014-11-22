package Data;

import BL.*;
import BL.Algorithms.*;
import BL.Kernels.Kernel;
import BL.Kernels.Poly2Kernel;
import BL.Kernels.RBF2Kernel;
import BL.Kernels.RBF3Kernel;
import BL.Prediction.*;
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
                return new ExampleStandard();

            case 1:
                return new Example2D();

            case 2:
                return new VowelExample();

            default:
                return new ExampleStandard();
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
                classifier.classifierData.predict = new PredictionVowelDurationData();
                break;
            case 1:
                classifier.classifierData.predict = new PredictionMultiClass();
                break;
            case 2:
                classifier.classifierData.predict = new PredictionRanking();
                break;
            case 3:
                classifier.classifierData.predict = new PredictionDummyData();
                break;
            default:
                return null;
        }

        switch (phi) {
            case 0:
                classifier.classifierData.phi = new PhiVowelDurationConverter();
                break;
            case 1:
                classifier.classifierData.phi = new PhiSparseConverter();
                break;
            case 2:
                classifier.classifierData.phi = new PhiRankConverter();
                break;
            case 3:
                classifier.classifierData.phi = new PhiDummyConverter();
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
		
		switch (updateType) {
			case 0:
                classifier.classifierData.algorithmUpdateRule = PassiveAggressive.getInstance(arguments);
				break;
			case 1:
                classifier.classifierData.algorithmUpdateRule = SVM_Pegasos.getInstance(arguments);
				break;
			case 2:
                classifier.classifierData.algorithmUpdateRule = DirectLoss.getInstance(arguments);
				break;
			case 3:
                classifier.classifierData.algorithmUpdateRule = CRF.getInstance(arguments);
				break;
			case 4:
                classifier.classifierData.algorithmUpdateRule = RampLoss.getInstance(arguments);
				break;
			case 5:
                classifier.classifierData.algorithmUpdateRule = ProbitLoss.getInstance(arguments);
				break;
            case 6:
                classifier.classifierData.algorithmUpdateRule = RankSVM.getInstance(arguments);
                break;
			default:
				classifier.classifierData.algorithmUpdateRule = null;
		}

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

    public static TaskLoss getTaskLoss(int task){

        switch (task) {
            case 0:
                return new TaskLossVowelDuration();
            case 1:
                return new TaskLossMultiClass();
            default:
                return null;
        }
    }
}
