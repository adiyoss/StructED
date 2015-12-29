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

import com.structed.constants.Char2Idx;
import com.structed.constants.Consts;
import com.structed.dal.OcrReader;
import com.structed.data.Factory;
import com.structed.data.InstancesContainer;
import com.structed.data.Logger;
import com.structed.data.entities.Example;
import com.structed.data.entities.Vector;
import com.structed.data.featurefunctions.FeatureFunctionsOCR;
import com.structed.data.featurefunctions.FeatureFunctionsSparse;
import com.structed.models.StructEDModel;
import com.structed.models.algorithms.PassiveAggressive;
import com.structed.models.inference.InferenceMultiClass;
import com.structed.models.inference.InferenceOCR;
import com.structed.models.loss.TaskLossCER;
import com.structed.models.loss.TaskLossMultiClass;
import com.structed.view.Graph;
import java.util.ArrayList;

import static com.structed.data.Factory.getReader;

/**
 * Created by yossiadi on 19/12/2015.
 * Tutorial that demonstrate the strength of using structured data.
 * For that we use the OCR data set from http://ai.stanford.edu/~btaskar/ocr/
 */
public class OCRTutorial {
    public static void main(String[] args) throws Exception {
        // ============================ OCR DATA ============================ //
        Logger.info("OCR data example.");

        // === PARAMETERS === //
        String dataPath = "tutorials-code/ocr/data/all.letter.data";

        int epochNum = 5;
        int readerType = 3;
        int isAvg = 1;
        int maxFeatures = 128;
        int start_transition_characters = 3354; // 26*128 + 26
        int nam_of_characters = Char2Idx.char2id.size() - 1;


        OcrReader reader = (OcrReader)getReader(readerType);
        Vector W;
        ArrayList<Double> arguments;
        // ================== //

//        // ===================== MULTI CLASS ===================== //
//        Logger.info("================= Multi-class version =================");
//        Logger.info("Loading data...");
//        // === LOADING DATA === //
//        InstancesContainer ocrAllInstancesMulti = reader.readDataMultiClass(dataPath, Consts.TAB, Consts.COLON_SPLITTER);
//        InstancesContainer ocrTrainInstancesMulti = getFold(ocrAllInstancesMulti, 1, true);
//        InstancesContainer ocrTestInstancesMulti = getFold(ocrAllInstancesMulti, 1, false);
//        if (ocrTrainInstancesMulti.getSize() == 0) return;
//        // ==================== //
//
//        W = new Vector() {{put(0, 0.0);}}; // init the first weight vector
//        arguments = new ArrayList<Double>() {{add(500.0);}}; // model parameters
//
//        // ======= PA MULTI-CLASS MODEL ====== //
//        StructEDModel ocr_model_multi_class = new StructEDModel(W, new PassiveAggressive(), new TaskLossMultiClass(),
//                new InferenceMultiClass(Char2Idx.char2id.size()-1), null, new FeatureFunctionsSparse(Char2Idx.char2id.size()-1, maxFeatures), arguments); // create the model
//        ocr_model_multi_class.train(ocrTrainInstancesMulti, null, null, epochNum, isAvg, true); // train
//        ocr_model_multi_class.predict(ocrTestInstancesMulti, null, 1, false); // predict
//        // ======================================================= //

        // ====================== STRUCTURED ===================== //
        Logger.info("================= Structured version =================");
        // === LOADING DATA === //
        InstancesContainer ocrAllInstances = reader.readData(dataPath, Consts.TAB, Consts.COLON_SPLITTER);
        InstancesContainer ocrTrainInstancesStruct = getFold(ocrAllInstances, 0, false);
        InstancesContainer ocrTestInstancesStruct = getFold(ocrAllInstances, 0, true);
        if (ocrTrainInstancesStruct.getSize() == 0) return;
        // ==================== //

        // ======= PA STRUCTURED MODEL ====== //
        W = new Vector() {{put(0, 0.0);}}; // init the first weight vector
        arguments = new ArrayList<Double>() {{add(15.0);}}; // model parameters

        StructEDModel ocr_model = new StructEDModel(W, new PassiveAggressive(), new TaskLossCER(),
                new InferenceOCR(), null, new FeatureFunctionsOCR(maxFeatures), arguments); // create the model
        ocr_model.train(ocrTrainInstancesStruct, null, null, epochNum, isAvg, true); // train
        ocr_model.predict(ocrTestInstancesStruct, null, 1, false); // predict
        ocr_model.saveModel("models/pa4danny.model");
        Graph g = new Graph();
        g.drawHeatMap(ocr_model, start_transition_characters, nam_of_characters, true);
        // ======================================================= //
    }

    /**
     * Get a given fold from the whole dataset
     * @param container all the ocr data from the all.letter.data file
     * @param fold the requested fold
     * @param isEqual if this boolean parameter is true only the examples from the requested fold will be returned,
     *                otherwise all the examples besides the examples from the given fold number will be selected
     * @return A container with the requested examples
     */
    public static InstancesContainer getFold(InstancesContainer container, int fold, boolean isEqual){
        InstancesContainer new_container = Factory.getInstanceContainer(0);

        for(int i=0 ; i<container.getSize() ; i++){
            Example e = container.getInstances().get(i);
            if(isEqual) {
                if (e.getFold() == fold) {
                    new_container.getInstances().add(e);
                }
            } else {
                if (e.getFold() != fold) {
                    new_container.getInstances().add(e);
                }
            }
        }
        return new_container;
    }
}
