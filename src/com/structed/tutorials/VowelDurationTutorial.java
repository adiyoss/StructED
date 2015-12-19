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
import com.structed.dal.Writer;
import com.structed.data.Factory;
import com.structed.data.InstancesContainer;
import com.structed.data.Logger;
import com.structed.data.entities.Vector;
import com.structed.data.featurefunctions.FeatureFunctionsVowelDuration;
import com.structed.models.StructEDModel;
import com.structed.models.algorithms.*;
import com.structed.models.inference.InferenceVowelDurationData;
import com.structed.models.loss.TaskLossVowelDuration;

import javax.security.auth.login.LoginException;
import javax.swing.text.html.parser.Entity;
import java.io.File;
import java.util.ArrayList;

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
        String trainPath = "tutorials-code/vowel/data/vowel/train.vowel.txt";
        String testPath = "tutorials-code/vowel/data/vowel/test.vowel.txt";

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
        Logger.info("===============================");
        Logger.info("============= ORBIT ============");
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

        ArrayList<String> data = new ArrayList<String>();
        for(Double val :vowel_model.getClassifier().classifierData.plot_array)
            data.add(String.valueOf(val));
        Writer wrt = Factory.getWriter(0);
        wrt.writeData2File("vals.txt", data, true);
        // ==================================================================== //
    }
}

