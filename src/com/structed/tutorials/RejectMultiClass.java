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
import com.structed.models.algorithms.MultiClassReject;
import com.structed.models.algorithms.SVM;
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

        int epochNum = 100;
        int readerType = 0;
        int isAvg = 1;
        int numExamples2Display = 3;
        int numOfClasses = 10;
        int maxFeatures = 784;
        Vector W = new Vector();
        ArrayList<Double> arguments;
        StructEDModel mnist_model;
        Reader reader = getReader(readerType);
        // ================== //

        // load the data
        InstancesContainer mnistTrainInstances = reader.readData(trainPath, Consts.SPACE, Consts.COLON_SPLITTER);
        InstancesContainer mnistDevelopInstances = reader.readData(valPath, Consts.SPACE, Consts.COLON_SPLITTER);
        InstancesContainer mnistTestInstances = reader.readData(testPath, Consts.SPACE, Consts.COLON_SPLITTER);
        if (mnistTrainInstances.getSize() == 0) return;

//        // ======= SSVM ====== //
//        W = new Vector(){{put(0, 0.0);}}; // init the first weight vector
//        arguments = new ArrayList<Double>() {{add(0.1); add(0.1);}}; // model parameters
//        mnist_model = new StructEDModel(W, new SVM(), new TaskLossMultiClass(),
//                new InferenceMultiClass(numOfClasses), null, new FeatureFunctionsSparse(numOfClasses, maxFeatures), arguments); // create the model
//        mnist_model.train(mnistTrainInstances, null, null, epochNum, isAvg, true); // train
//        mnist_model.predict(mnistTestInstances, null, numExamples2Display, false); // predict
//        mnist_model.plotValidationError(false); // plot the error on the validation set

        final double beta = 1000000000.0;
        final double p = 0.3;
        double th = Math.log(beta*p/(1-p));

        // ======= MULTI-CLASS REJECT ====== //
        // init the first weight vector
        for (int i=0 ; i<numOfClasses*maxFeatures; i++)
            W.put(i, 0.0);
        arguments = new ArrayList<Double>() {{add(0.15); add(0.1); add(beta); add(p);}}; // model parameters

        mnist_model = new StructEDModel(W, new MultiClassReject(), new TaskLossMultiClass(),
                new InferenceMultiClass(numOfClasses), null, new FeatureFunctionsSparse(numOfClasses, maxFeatures), arguments); // create the model
        mnist_model.train(mnistTrainInstances, null, null, epochNum, isAvg, true); // train
        ArrayList<PredictedLabels> labels  = mnist_model.predict(mnistTestInstances, null, numExamples2Display, false); // predict
        mnist_model.plotValidationError(false); // plot the error on the validation set

//        Logger.info("Th: " + th);
//        for(int i=0 ; i<mnistTestInstances.getSize() ; i++){
//            String output = "Y = "+mnistTestInstances.getInstance(i).getLabel();
//            output += ", Y_HAT = "+labels.get(i).firstKey();
//            output += ", Confidence = "+labels.get(i).firstEntry().getValue();
//            if(labels.get(i).firstEntry().getValue() < th)
//                output += ", Reject.";
//            Logger.info(output);
//        }
//        Logger.info("");
        //==================================================================== //
    }
}
