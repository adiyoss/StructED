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
public class MulticlassIRIS {
    public static void main(String[] args) throws Exception {
        // ============================ MNIST DATA ============================ //
        Logger.info("Loading IRIS dataset.");

        // ============================ IRIS DATA ============================= //
        // === PARAMETERS === //
        String trainPath = "data/iris/iris.train.txt"; // <the path to the iris train data>
        String testPath = "data/iris/iris.test.txt"; // <the path to the iris test data>
        int epochNum = 10;
        int isAvg = 1;
        int numExamples2Display = 3;
        int numOfClasses = 3;
        int maxFeatures = 4;
        int readerType = 0;
        Reader reader = getReader(readerType);
        // ================== //

        // load the data
        InstancesContainer irisTrainInstances = reader.readData(trainPath, Consts.COMMA_NOTE, Consts.COLON_SPLITTER);
        InstancesContainer irisTestInstances = reader.readData(testPath, Consts.COMMA_NOTE, Consts.COLON_SPLITTER);
        // ======= PA ====== //
        Vector W = new Vector() {{put(0, 0.0);}}; // init the first weight vector to be zeros
        ArrayList<Double> arguments = new ArrayList<Double>() {{add(1.0);}}; // model parameters

        // build the model
        StructEDModel iris_model = new StructEDModel(W, new PassiveAggressive(), new TaskLossMultiClass(),
                new InferenceMultiClassOld(numOfClasses), null, new FeatureFunctionsSparse(numOfClasses, maxFeatures), arguments);
        // train
        iris_model.train(irisTrainInstances, null, null, epochNum, isAvg);
        // predict
        ArrayList<PredictedLabels> iris_labels = iris_model.predict(irisTestInstances, null, numExamples2Display);

        // printing the predictions
        for(int i=0 ; i<iris_labels.size() ; i++)
            Logger.info("Desire Label: "+irisTestInstances.getInstance(i).getLabel()+", Predicted Label: "+iris_labels.get(i).firstKey());
        // ==================================================================== //
    }
}
