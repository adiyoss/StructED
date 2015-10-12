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

package com.structed.tutorials;

import com.structed.constants.Consts;
import com.structed.dal.Reader;
import com.structed.data.InstancesContainer;
import com.structed.data.Logger;
import com.structed.data.entities.PredictedLabels;
import com.structed.data.entities.Vector;
import com.structed.data.featurefunctions.FeatureFunctionsSparse;
import com.structed.models.StructEDModel;
import com.structed.models.algorithms.MulticlassRegect;
import com.structed.models.inference.InferenceMultiClass;
import com.structed.models.loss.TaskLossMultiClass;

import java.util.ArrayList;

import static com.structed.data.Factory.getReader;

/**
 * Created by yossiadi on 10/12/15.
 *
 */
public class RejectMultiClass {
    public static void main(String[] args) throws Exception {
        // ============================ MNIST DATA ============================ //
        Logger.info("MNIST data example.");
        // === PARAMETERS === //
        String trainPath = "tutorials-code/multiclass/data/MNIST/train.txt";
        String testPath = "tutorials-code/multiclass/data/MNIST/test.data.txt";
        String valPath = "tutorials-code/multiclass/data/MNIST/val.data.txt";

        int epochNum = 1;
        int readerType = 0;
        int isAvg = 1;
        int numExamples2Display = 3;
        int numOfClasses = 10;
        int maxFeatures = 784;
        Reader reader = getReader(readerType);
        // ================== //

        // load the data
//        InstancesContainer mnistTrainInstances = reader.readData(trainPath, Consts.SPACE, Consts.COLON_SPLITTER);
//        InstancesContainer mnistDevelopInstances = reader.readData(valPath, Consts.SPACE, Consts.COLON_SPLITTER);
//        InstancesContainer mnistTestInstances = reader.readData(testPath, Consts.SPACE, Consts.COLON_SPLITTER);
//        if (mnistTrainInstances.getSize() == 0) return;

//        // ======= SVM ====== //
        Vector W = new Vector() {{put(0, 0.0);}}; // init the first weight vector
        ArrayList<Double> arguments = new ArrayList<Double>() {{add(1.0);}}; // model parameters
//
//        StructEDModel mnist_model = new StructEDModel(W, new PassiveAggressive(), new TaskLossMultiClass(),
//                new InferenceMultiClass(numOfClasses), null, new FeatureFunctionsSparse(numOfClasses, maxFeatures), arguments); // create the model
//        mnist_model.train(mnistTrainInstances, null, mnistDevelopInstances, epochNum, isAvg, true); // train
//        mnist_model.predict(mnistTestInstances, null, numExamples2Display, false); // predict
//        mnist_model.plotValidationError(false); // plot the error on the validation set
//        //==================================================================== //

        // ============================ IRIS DATA ============================= //
        // === PARAMETERS === //
        trainPath = "tutorials-code/multiclass/data/iris/iris.train.txt";
        testPath = "tutorials-code/multiclass/data/iris/iris.test.txt";
        epochNum = 1;
        isAvg = 1;
        numExamples2Display = 3;
        numOfClasses = 3;
        maxFeatures = 4;
        // ================== //

        // load the data
        InstancesContainer irisTrainInstances = reader.readData(trainPath, Consts.COMMA_NOTE, Consts.COLON_SPLITTER);
        InstancesContainer irisTestInstances = reader.readData(testPath, Consts.COMMA_NOTE, Consts.COLON_SPLITTER);
        // ======= SVM ====== //
        W = new Vector() {{put(0, 0.0);}}; // init the first weight vector to be zeros
        double beta = 2.0;
        double p = 0.4;
        arguments = new ArrayList<Double>() {{add(0.1); add(0.1); add(2.0); add(0.4);}}; // model parameters
        double th = Math.log(beta*p/(1-p));
        StructEDModel iris_model = new StructEDModel(W, new MulticlassRegect(), new TaskLossMultiClass(),
                new InferenceMultiClass(numOfClasses), null, new FeatureFunctionsSparse(numOfClasses, maxFeatures), arguments); // create the model
        iris_model.train(irisTrainInstances, null, null, epochNum, isAvg, true); // train
        ArrayList<PredictedLabels> labels = iris_model.predict(irisTestInstances, null, numExamples2Display, true); // predict

        Logger.info("Th: "+th);
        for(int i=0 ; i<irisTestInstances.getSize() ; i++){
            Logger.info("Y = "+irisTestInstances.getInstance(i).getLabel());
            Logger.info("Y_HAT = "+labels.get(i).firstKey());
            Logger.info("Confidence = "+labels.get(i).firstEntry().getValue());
//            if(labels.get(i).firstEntry().getValue() < th)
//                Logger.info("Reject.");
        }
        Logger.info("");
        // ==================================================================== //
    }
}
