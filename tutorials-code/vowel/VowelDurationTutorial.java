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
import com.structed.data.entities.Vector;
import com.structed.data.featurefunctions.FeatureFunctionsVowelDuration;
import com.structed.models.StructEDModel;
import com.structed.models.algorithms.*;
import com.structed.models.inference.InferenceVowelDurationData;
import com.structed.models.loss.TaskLossVowelDuration;

import java.io.File;
import java.util.ArrayList;

import static com.structed.data.Factory.getReader;


import static com.structed.data.Factory.getReader;

/**
 * Created by yossiadi on 6/29/15.
 * Tutorial about the vowel duration data
 *
 * Compering all the models on the vowel duration measurement task (using the same setup of training)
 * All the models will be saved to the models directory
 *
 * Results: ========================
 *         MODEL    COST    RUN-TIME
 *         SSVM     14.45   13s
 *         PA       13.8    12s
 *         CRF      14.05   31s
 *         DLM      12.7    24s
 *         RLM      12.9    25s
 *         OL       11.7    13s
 *         PL      ~12.9    617s
 */

public class VowelDurationTutorial {
    public static void main(String[] args) throws Exception {
        // ============================ VOWEL DURATION DATA ============================ //
        Logger.info("Vowel Duration data example.");
        String trainPath = "data/vowel/train.vowel.txt";
        String testPath = "data/vowel/test.vowel.txt";

        int epochNum = 1;
        int readerType = 2;
        int isAvg = 1;
        int numExamples2Display = 1;
        Reader reader = getReader(readerType);

        // load the data
        InstancesContainer vowelTrainInstances = reader.readData(trainPath, Consts.SPACE, Consts.COLON_SPLITTER);
        InstancesContainer vowelTestInstances = reader.readData(testPath, Consts.SPACE, Consts.COLON_SPLITTER);
        if (vowelTrainInstances.getSize() == 0) return;

        ArrayList<Double> arguments;
        StructEDModel vowel_model;
        Vector W;
        ArrayList<Double> task_loss_params = new ArrayList<Double>(){{add(1.0);add(2.0);}}; // task loss parameters

        // create models dir if doesn't exists
        File f = new File("models");
        if(!f.exists())
            f.mkdir();

        Logger.info("");
        Logger.info("=========================================");
        Logger.info("============= STRUCTURED SVM ============");
        Logger.info("");
        // ======= SSVM ====== //
        W = new Vector() {{put(0, 0.0);}}; // init the first weight vector
        arguments = new ArrayList<Double>() {{add(0.1);add(0.01);}}; // model parameters
        vowel_model = new StructEDModel(W, new SVM(), new TaskLossVowelDuration(),
                new InferenceVowelDurationData(), null, new FeatureFunctionsVowelDuration(), arguments); // create the model
        vowel_model.train(vowelTrainInstances, task_loss_params, null, epochNum, isAvg, true); // train
        vowel_model.predict(vowelTestInstances, task_loss_params, numExamples2Display, true); // predict
        vowel_model.saveModel("models/ssvm.vowel.model");

        Logger.info("");
        Logger.info("=============================================");
        Logger.info("============= PASSIVE AGGRESSIVE ============");
        Logger.info("");
        // ======= PA ====== //
        W = new Vector() {{put(0, 0.0);}}; // init the first weight vector
        arguments = new ArrayList<Double>() {{add(0.5);}}; // model parameters
        vowel_model = new StructEDModel(W, new PassiveAggressive(), new TaskLossVowelDuration(),
                new InferenceVowelDurationData(), null, new FeatureFunctionsVowelDuration(), arguments, false); // create the model
        vowel_model.train(vowelTrainInstances, task_loss_params, null, epochNum, isAvg, true); // train
        vowel_model.predict(vowelTestInstances, task_loss_params, numExamples2Display, true); // predict
        vowel_model.saveModel("models/pa.vowel.model");

        Logger.info("");
        Logger.info("==============================");
        Logger.info("============= CRF ============");
        Logger.info("");
        // ======= CRF ====== //
        W = new Vector() {{put(0, 0.0);}}; // init the first weight vector
        arguments = new ArrayList<Double>() {{add(0.1); add(0.1);}}; // model parameters
        vowel_model = new StructEDModel(W, new CRF(), new TaskLossVowelDuration(),
                new InferenceVowelDurationData(), null, new FeatureFunctionsVowelDuration(), arguments, false); // create the model
        vowel_model.train(vowelTrainInstances, task_loss_params, null, epochNum, isAvg, true); // train
        vowel_model.predict(vowelTestInstances, task_loss_params, numExamples2Display, true); // predict
        vowel_model.saveModel("models/crf.vowel.model");

        Logger.info("");
        Logger.info("===================================================");
        Logger.info("============= DIRECT LOSS MINIMIZATION ============");
        Logger.info("");
        // ======= DIRECT LOSS ====== //
        W = new Vector() {{put(0, 0.0);}}; // init the first weight vector
        arguments = new ArrayList<Double>() {{add(0.01);add(-2.45);}}; // model parameters
        vowel_model = new StructEDModel(W, new DirectLoss(), new TaskLossVowelDuration(),
                new InferenceVowelDurationData(), null, new FeatureFunctionsVowelDuration(), arguments, false); // create the model
        vowel_model.loadModel("models/pa.vowel.model");
        vowel_model.train(vowelTrainInstances, task_loss_params, null, epochNum, isAvg, true); // train
        vowel_model.predict(vowelTestInstances, task_loss_params, numExamples2Display, true); // predict
        vowel_model.saveModel("models/dlm.vowel.model");
        // ==================================================================== //

        Logger.info("");
        Logger.info("==================================================");
        Logger.info("============= RAMP  LOSS MINIMIZATION ============");
        Logger.info("");
        // ======= RAMP LOSS ====== //
        W = new Vector() {{put(0, 0.0);}}; // init the first weight vector
        arguments = new ArrayList<Double>() {{add(0.01);add(0.01);}}; // model parameters
        vowel_model = new StructEDModel(W, new RampLoss(), new TaskLossVowelDuration(),
                new InferenceVowelDurationData(), null, new FeatureFunctionsVowelDuration(), arguments, false); // create the model
        vowel_model.loadModel("models/pa.vowel.model");
        vowel_model.train(vowelTrainInstances, task_loss_params, null, epochNum, isAvg, true); // train
        vowel_model.predict(vowelTestInstances, task_loss_params, numExamples2Display, true); // predict
        vowel_model.saveModel("models/ramp.vowel.model");
        // ==================================================================== //

        Logger.info("");
        Logger.info("=================================================");
        Logger.info("============= ORBIT LOSS MINIMIZATION============");
        Logger.info("");
        // ======= ORBIT LOSS ====== //
        W = new Vector() {{put(0, 0.0);}}; // init the first weight vector
        arguments = new ArrayList<Double>() {{add(0.1);add(0.01);}}; // model parameters
        vowel_model = new StructEDModel(W, new OrbitLoss(), new TaskLossVowelDuration(),
                new InferenceVowelDurationData(), null, new FeatureFunctionsVowelDuration(), arguments, false); // create the model
        vowel_model.loadModel("models/pa.vowel.model");
        vowel_model.train(vowelTrainInstances, task_loss_params, null, epochNum, isAvg, true); // train
        vowel_model.predict(vowelTestInstances, task_loss_params, numExamples2Display, true); // predict
        vowel_model.saveModel("models/orbit.vowel.model");
        // ==================================================================== //
        
        Logger.info("");
        Logger.info("===============================");
        Logger.info("// ======= PROBIT LOSS ====== //");
        Logger.info("");
        // ======= PROBIT LOSS ====== //
        W = new Vector() {{put(0, 0.0);}}; // init the first weight vector
        arguments = new ArrayList<Double>() {{add(0.1);add(0.1);add(50.0);
            add(0.0);add(0.0);add(1.0);}}; // model parameters
        vowel_model = new StructEDModel(W, new ProbitLoss(), new TaskLossVowelDuration(),
                new InferenceVowelDurationData(), null, new FeatureFunctionsVowelDuration(), arguments); // create the model
        vowel_model.loadModel("models/pa.vowel.model");
        vowel_model.train(vowelTrainInstances, task_loss_params, null, epochNum, isAvg, true); // train
        vowel_model.predict(vowelTestInstances, task_loss_params, numExamples2Display, true); // predict
        vowel_model.saveModel("models/probit.vowel.model");
        // ==================================================================== //
    }
}

