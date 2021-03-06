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

package com.tutorials;

import com.structed.constants.Consts;
import com.structed.dal.Reader;
import com.structed.data.InstancesContainer;
import com.structed.data.Logger;
import com.structed.data.entities.PredictedLabels;
import com.structed.data.entities.Vector;
import com.structed.data.featurefunctions.FeatureFunctionsSparse;
import com.structed.models.StructEDModel;
import com.structed.models.algorithms.PassiveAggressive;
import com.structed.models.algorithms.SVM;
import com.structed.models.inference.InferenceMultiClassOld;
import com.structed.models.loss.TaskLossMultiClass;

import java.util.ArrayList;

import static com.structed.data.Factory.getReader;

/**
 * Created by yossiadi on 6/29/15.
 * Tutorial about the multiclass classifications using MNIST and Iris datasets
 */
public class MulticlassMNIST {
    public static void main(String[] args) throws Exception {
        // ============================ MNIST DATA ============================ //
        Logger.info("Loading MNIST dataset.");

        // === PARAMETERS === //
        String trainPath = "data/MNIST/train.txt"; // <the path to the mnist train data>
        String testPath = "data/MNIST/test.data.txt"; // <the path to the mnist test data>
        String valPath = "data/MNIST/val.data.txt"; // <the path to the mnist validation data>
        int epochNum = 1;
        int readerType = 0;
        int isAvg = 1;
        int numExamples2Display = 3;
        int numOfClasses = 10;
        int maxFeatures = 784;
        Reader reader = getReader(readerType);
        // ================== //

        // load the data
        InstancesContainer mnistTrainInstances = reader.readData(trainPath, Consts.SPACE, Consts.COLON_SPLITTER);
        InstancesContainer mnistDevelopInstances = reader.readData(valPath, Consts.SPACE, Consts.COLON_SPLITTER);
        InstancesContainer mnistTestInstances = reader.readData(testPath, Consts.SPACE, Consts.COLON_SPLITTER);
        if (mnistTrainInstances.getSize() == 0) return;

        // ======= SVM ====== //
        Vector W = new Vector() {{put(0, 0.0);}}; // init the first weight vector
        ArrayList<Double> arguments = new ArrayList<Double>() {{add(0.1);add(0.1);}}; // model parameters

        // build the model
        StructEDModel mnist_model = new StructEDModel(W, new SVM(), new TaskLossMultiClass(),
                new InferenceMultiClassOld(numOfClasses), null, new FeatureFunctionsSparse(numOfClasses, maxFeatures), arguments);
        // train
        mnist_model.train(mnistTrainInstances, null, mnistDevelopInstances, epochNum, isAvg, true);
        // predict
        mnist_model.predict(mnistTestInstances, null, numExamples2Display, true);
        // plot the error on the validation set
        // the true flag indicates that we saves the image to img folder in the project directory
        // if the img directory does not exists it will create it
        mnist_model.plotValidationError(true);
        // ==================================================================== //
    }
}
