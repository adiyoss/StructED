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

package com.structed.tests.models.algorithms;

import com.structed.constants.Consts;
import com.structed.dal.Reader;
import com.structed.data.InstancesContainer;
import com.structed.data.Logger;
import com.structed.data.entities.Vector;
import com.structed.data.featurefunctions.FeatureFunctionsDummy;
import com.structed.models.StructEDModel;
import com.structed.models.algorithms.*;
import com.structed.models.inference.InferenceDummyData;
import com.structed.models.loss.TaskLossDummyData;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Random;

import static com.structed.data.Factory.getReader;

/**
 * Created by yossiadi on 6/29/15.
 */
public class AlgorithmsTests extends TestCase{

    public void testAlgorithms() throws Exception {
        // ============================ DUMMY DATA ============================ //
        Logger.info("Dummy data example.");
        int readerType = 0;
        int epochNum = 5;
        int isAvg = 1;
        int numExamples2Display = 3;
        String trainPath = "src/com/structed/tests/tests_data/dummy/train.txt";
        String testPath = "src/com/structed/tests/tests_data/dummy/test.txt";

        // load the data
        Reader reader = getReader(readerType);
        InstancesContainer dummyTrainInstances = reader.readData(trainPath, Consts.SPACE, Consts.COLON_SPLITTER);
        InstancesContainer dummyTestInstances = reader.readData(testPath, Consts.SPACE, Consts.COLON_SPLITTER);
        if ( dummyTrainInstances.getSize() == 0 ) return;
        Vector W;
        ArrayList<Double> arguments;
        ArrayList<Double> task_loss_params;

        // ======= PASSIVE AGGRESSIVE ====== //
        W = new Vector() {{put(0, 0.0);}}; // init the first weight vector
        arguments = new ArrayList<Double>(){{add(3.0);}}; // model parameters
        task_loss_params = new ArrayList<Double>(){{add(0.0);}}; // task loss parameters
        StructEDModel pa_model = new StructEDModel(W, new PassiveAggressive(), new TaskLossDummyData(),
                new InferenceDummyData(), null, new FeatureFunctionsDummy(), arguments); // create the model
        pa_model.train(dummyTrainInstances, task_loss_params, null, epochNum, isAvg, false); // train
        pa_model.predict(dummyTestInstances, task_loss_params, numExamples2Display, false); // predict
        assertEquals("PA test, Loss should be 0", 0.0, pa_model.getCumulative_loss());
        // =================================== //

        // ======= SVM ====== //
        W = new Vector() {{put(0, 0.0);}}; // init the first weight vector
        arguments = new ArrayList<Double>(){{add(0.1);add(0.1);}}; // model parameters

        StructEDModel svm_model = new StructEDModel(W, new SVM(), new TaskLossDummyData(),
                new InferenceDummyData(), null, new FeatureFunctionsDummy(), arguments); // create the model
        svm_model.train(dummyTrainInstances, task_loss_params, null, epochNum, isAvg, false); // train
        svm_model.predict(dummyTestInstances, task_loss_params, numExamples2Display, false); // predict
        assertEquals("SVM test, Loss should be 0", 0.0, svm_model.getCumulative_loss());
        // ==================================================================== //

        // ======= DL ====== //
        W = new Vector() {{put(0, 0.0);}}; // init the first weight vector
        arguments = new ArrayList<Double>(){{add(0.5);add(-1.6);}}; // model parameters
        StructEDModel dl_model = new StructEDModel(W, new DirectLoss(), new TaskLossDummyData(),
                new InferenceDummyData(), null, new FeatureFunctionsDummy(), arguments); // create the model
        dl_model.train(dummyTrainInstances, task_loss_params, null, epochNum, isAvg, false); // train
        dl_model.predict(dummyTestInstances, task_loss_params, numExamples2Display, false); // predict
        assertEquals("DL  test, Loss should be 0", 0.0, dl_model.getCumulative_loss());
        // ==================================================================== //

        // ======= PL ====== //
        epochNum = 5;
        int numOfFeatureMaps = 6;
        W = new Vector();
        Random rand = new Random();
        rand.setSeed(1234);
        // init the first weight vector
        for(int i=0 ; i<numOfFeatureMaps ; i++)
            W.put(i, rand.nextDouble());

        arguments = new ArrayList<Double>(){{add(0.1);add(0.8);add(800.0);add(1.0);add(0.0);add(0.2);}}; // model parameters
        StructEDModel pl_model = new StructEDModel(W, new ProbitLoss(), new TaskLossDummyData(),
                new InferenceDummyData(), null, new FeatureFunctionsDummy(), arguments); // create the model
        pl_model.train(dummyTrainInstances, task_loss_params, null, epochNum, isAvg, false); // train
        pl_model.predict(dummyTestInstances, task_loss_params, numExamples2Display, false); // predict
        assertTrue("PL  test, Loss should be less then 2.0", pl_model.getCumulative_loss() <= 2.0);
        // ==================================================================== //

        // ======= OL ====== //
        W = new Vector() {{put(0, 0.0);}}; // init the first weight vector
        arguments = new ArrayList<Double>(){{add(0.1);add(0.01);}}; // model parameters
        StructEDModel ol_model = new StructEDModel(W, new OrbitLoss(), new TaskLossDummyData(),
                new InferenceDummyData(), null, new FeatureFunctionsDummy(), arguments); // create the model
        ol_model.train(dummyTrainInstances, task_loss_params, null, epochNum, isAvg, false); // train
        ol_model.predict(dummyTestInstances, task_loss_params, numExamples2Display, false); // predict
        assertEquals("OL  test, Loss should be 0", 0.0, ol_model.getCumulative_loss());
        // ==================================================================== //

        // ======= CRF ====== //
        W = new Vector() {{put(0, 0.0);}}; // init the first weight vector
        arguments = new ArrayList<Double>(){{add(0.1);add(0.1);}}; // model parameters
        StructEDModel crf_model = new StructEDModel(W, new CRF(), new TaskLossDummyData(),
                new InferenceDummyData(), null, new FeatureFunctionsDummy(), arguments); // create the model
        crf_model.train(dummyTrainInstances, task_loss_params, null, epochNum, isAvg, false); // train
        crf_model.predict(dummyTestInstances, task_loss_params, numExamples2Display, false); // predict
        assertEquals("CRF test, Loss should be 0", 0.0, crf_model.getCumulative_loss());
        // ==================================================================== //
    }
}
