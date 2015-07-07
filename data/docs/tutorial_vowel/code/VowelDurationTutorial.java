/*
 * StructED - Machine Learning, Structured Prediction Package written in Java.
 * Copyright (C) 2015 Yossi Adi, E-Mail: yossiadidrum@gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.structed.tutorials;

import com.structed.constants.Consts;
import com.structed.dal.Reader;
import com.structed.data.InstancesContainer;
import com.structed.data.Logger;
import com.structed.data.entities.Vector;
import com.structed.data.featurefunctions.FeatureFunctionsVowelDuration;
import com.structed.models.StructEDModel;
import com.structed.models.algorithms.DirectLoss;
import com.structed.models.inference.InferenceVowelDurationData;
import com.structed.models.loss.TaskLossVowelDuration;

import java.util.ArrayList;

import static com.structed.data.Factory.getReader;

/**
 * Created by yossiadi on 6/29/15.
 */
public class VowelDurationTutorial {
    public static void main(String[] args) throws Exception {
        // ============================ VOWEL DURATION DATA ============================ //
        Logger.info("Vowel Duration data example.");
        String trainPath = "data/db/vowel/small.train.vowel";
        String testPath = "data/db/vowel/test.results";

        int epochNum = 1;
        int readerType = 2;
        int isAvg = 1;
        int numExamples2Display = 1;
        Reader reader = getReader(readerType);

        // load the data
        InstancesContainer vowelTrainInstances = reader.readData(trainPath, Consts.SPACE, Consts.COLON_SPLITTER);
        InstancesContainer vowelTestInstances = reader.readData(testPath, Consts.SPACE, Consts.COLON_SPLITTER);
        if (vowelTrainInstances.getSize() == 0) return;

        // ======= DIRECT LOSS ====== //
        Vector W = new Vector() {{put(0, 0.0);}}; // init the first weight vector
        ArrayList<Double> arguments = new ArrayList<Double>() {{add(0.1);add(-1.51);}}; // model parameters
        ArrayList<Double> task_loss_params = new ArrayList<Double>(){{add(1.0);add(2.0);}}; // task loss parameters

        StructEDModel vowel_model = new StructEDModel(W, new DirectLoss(), new TaskLossVowelDuration(),
                new InferenceVowelDurationData(), null, new FeatureFunctionsVowelDuration(), arguments); // create the model
        vowel_model.train(vowelTrainInstances, task_loss_params, null, epochNum, isAvg); // train
        vowel_model.predict(vowelTestInstances, null, numExamples2Display); // predict
        vowel_model.plotValidationError(true); // plot the error on the validation set
        // ==================================================================== //
    }
}
