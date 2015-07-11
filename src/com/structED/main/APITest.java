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

package com.structed.main;

import com.structed.models.algorithms1.CRF;
import com.structed.models.algorithms1.PassiveAggressive;
import com.structed.models.algorithms1.SVM;
import com.structed.models.inference1.InferenceDummyData;
import com.structed.models.inference1.InferenceMultiClass;
import com.structed.models.StructEDModel;
import com.structed.models.loss.TaskLossDummyData;
import com.structed.models.loss.TaskLossMultiClass;
import com.structed.constants.Consts;
import com.structed.data.entities.PredictedLabels;
import com.structed.data.entities.Vector;
import com.structed.data.featurefunctions.FeatureFunctionsDummy;
import com.structed.data.featurefunctions.FeatureFunctionsSparse;
import com.structed.dal.Reader;
import com.structed.data.InstancesContainer;
import com.structed.data.Logger;

import java.util.ArrayList;

import static com.structed.data.Factory.getReader;

/**
 * Created by yossiadi on 6/28/15.
 * Another example of some usage functions of StructED
 */
public class APITest {

    public static void main(String[] args) throws Exception {

        // ============================ DUMMY DATA ============================ //
        Logger.info("Dummy data example.");
        int readerType = 0;
        int epochNum = 3;
        int isAvg = 1;
        int numExamples2Display = 3;
        String trainPath = "data/db/dummy/train.txt";
        String testPath = "data/db/dummy/test.txt";

        // load the data
        Reader reader = getReader(readerType);
        InstancesContainer dummyTrainInstances = reader.readData(trainPath, Consts.SPACE, Consts.COLON_SPLITTER);
        InstancesContainer dummyTestInstances = reader.readData(testPath, Consts.SPACE, Consts.COLON_SPLITTER);
        if ( dummyTrainInstances.getSize() == 0 ) return;

        // ======= PASSIVE AGGRESSIVE ====== //
        Vector W = new Vector() {{put(0, 0.0);}}; // init the first weight vector
        ArrayList<Double> arguments = new ArrayList<Double>(){{add(3.0);}}; // model parameters
        ArrayList<Double> task_loss_params = new ArrayList<Double>(){{add(1.0);}}; // task loss parameters

        StructEDModel dummy_model = new StructEDModel(W, new PassiveAggressive(), new TaskLossDummyData(),
                new InferenceDummyData(), null, new FeatureFunctionsDummy(), arguments); // create the model
        dummy_model.train(dummyTrainInstances, task_loss_params, null, epochNum, isAvg); // train
        ArrayList<PredictedLabels> labels = dummy_model.predict(dummyTestInstances, task_loss_params, numExamples2Display); // predict

        // print the prediction
        for(int i=0 ; i<dummyTestInstances.getSize() ; i++)
            Logger.info("Y = "+dummyTestInstances.getInstance(i).getLabel()+", Y_HAT = "+labels.get(i).firstKey());
        Logger.info("");

        // ======= CRF ====== //
        W = new Vector() {{put(0, 0.0);}}; // init the first weight vector
        arguments = new ArrayList<Double>(){{add(0.1);add(0.1);}}; // model parameters

        StructEDModel dummy_model_crf = new StructEDModel(W, new CRF(), new TaskLossDummyData(),
                new InferenceDummyData(), null, new FeatureFunctionsDummy(), arguments); // create the model
        dummy_model_crf.train(dummyTrainInstances, task_loss_params, null, epochNum, isAvg); // train
        labels = dummy_model_crf.predict(dummyTestInstances, task_loss_params, numExamples2Display); // predict

        // print the prediction
        for(int i=0 ; i<dummyTestInstances.getSize() ; i++)
            Logger.info("Y = "+dummyTestInstances.getInstance(i).getLabel()+", Y_HAT = "+labels.get(i).firstKey());
        Logger.info("");
        // ==================================================================== //


        // ============================ MNIST DATA ============================ //
        Logger.info("MNIST data example.");
        trainPath = "data/db/MNIST/train.txt";
        testPath = "data/db/MNIST/test.data.txt";
        String valPath = "data/db/MNIST/val.data.txt";
        epochNum = 1;
        int numOfClasses = 10;
        int maxFeatures = 784;

        // load the data
        InstancesContainer mnistTrainInstances = reader.readData(trainPath, Consts.SPACE, Consts.COLON_SPLITTER);
        InstancesContainer mnistDevelopInstances = reader.readData(valPath, Consts.SPACE, Consts.COLON_SPLITTER);
        InstancesContainer mnistTestInstances = reader.readData(testPath, Consts.SPACE, Consts.COLON_SPLITTER);
        if ( mnistTrainInstances.getSize() == 0 ) return;

        // ======= SVM ====== //
        W = new Vector() {{put(0, 0.0);}}; // init the first weight vector
        arguments = new ArrayList<Double>(){{add(0.1); add(0.1);}}; // model parameters

        StructEDModel mnist_model = new StructEDModel(W, new SVM(), new TaskLossMultiClass(),
                new InferenceMultiClass(numOfClasses), null, new FeatureFunctionsSparse(numOfClasses, maxFeatures), arguments); // create the model
        mnist_model.train(mnistTrainInstances, task_loss_params, mnistDevelopInstances, epochNum, isAvg); // train
        mnist_model.predict(mnistTestInstances, null, numExamples2Display); // predict
        mnist_model.plotValidationError(false); // plot the error on the validation set
        // ==================================================================== //


        // ============================ DUMMY DATA ============================ //
        // ======================= LOAD & SAVE THE MODEL ====================== //
        Logger.info("Save and Load Dummy model.");

        dummy_model.saveModel("dummy.model"); // save the pa model of the dummy data
        arguments = new ArrayList<Double>(){{add(3.0);}}; // model parameters

        StructEDModel loaded_model = new StructEDModel(null, new PassiveAggressive(), new TaskLossDummyData(),
                new InferenceDummyData(), null, new FeatureFunctionsDummy(), arguments); // create the model
        loaded_model.loadModel("dummy.model"); // load the saved model
        loaded_model.predict(dummyTestInstances, task_loss_params, numExamples2Display); // predict

        // print the prediction
        for(int i=0 ; i<dummyTestInstances.getSize() ; i++)
            Logger.info("Y = "+dummyTestInstances.getInstance(i).getLabel()+", Y_HAT = "+labels.get(i).firstKey());
        Logger.info("");
        // ==================================================================== //
    }
}
