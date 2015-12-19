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

package com.structed.models;

import com.structed.models.algorithms.IUpdateRule;
import com.structed.models.inference.IInference;
import com.structed.models.kernels.IKernel;
import com.structed.models.loss.ITaskLoss;
import com.structed.data.entities.Example;
import com.structed.data.entities.PredictedLabels;
import com.structed.data.entities.Vector;
import com.structed.data.featurefunctions.IFeatureFunctions;
import com.structed.data.InstancesContainer;
import com.structed.data.Logger;
import com.structed.utils.ModelHandler;
import com.structed.view.Graph;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by yossiadi on 6/28/15.
 *
 */

public class StructEDModel implements Serializable{

    // data members
    private Classifier classifier;
    private Vector W;
    private double cumulative_loss = 0;
    private boolean isShuffle;

    // C'tor
    public StructEDModel(Vector init_weights, IUpdateRule updateRule, ITaskLoss taskLoss, IInference inference,
                         IKernel kernel, IFeatureFunctions phi, ArrayList<Double> args){
        classifier = new Classifier();
        classifier.classifierData = new ClassifierData();
        updateRule.init(args);
        classifier.classifierData.updateRule = updateRule;
        classifier.classifierData.taskLoss = taskLoss;
        classifier.classifierData.inference = inference;
        classifier.classifierData.kernel = kernel;
        classifier.classifierData.phi = phi;
        W = init_weights;
        this.isShuffle = true;
    }
    // override constructor to support enable/disable shuffling
    public StructEDModel(Vector init_weights, IUpdateRule updateRule, ITaskLoss taskLoss, IInference inference,
                         IKernel kernel, IFeatureFunctions phi, ArrayList<Double> args, boolean isShuffle){
        classifier = new Classifier();
        classifier.classifierData = new ClassifierData();
        updateRule.init(args);
        classifier.classifierData.updateRule = updateRule;
        classifier.classifierData.taskLoss = taskLoss;
        classifier.classifierData.inference = inference;
        classifier.classifierData.kernel = kernel;
        classifier.classifierData.phi = phi;
        W = init_weights;
        this.isShuffle = isShuffle;
    }

    /**
     * Train the model on the train instances
     * @param epoch - the number of epochs to run over the data
     * @param trainInstances - the training instances(train set)
     * @param task_loss_params - the task loss parameters, if there are no parameters set this to null
     * @param developInstances - the develop instances(dev set), set this to null if there is no validation set
     * @param isAvg - an indicator whether or not to average the weights
     * @throws Exception
     */
    public void train(InstancesContainer trainInstances, ArrayList<Double> task_loss_params, InstancesContainer developInstances, int epoch, int isAvg, boolean verbose) throws Exception {
        //#############################################################################//
        //================================TRAIN========================================//
        //#############################################################################//
        //===================================//
        //print the start time of the program//
        Logger.infoTime("Start Training:");
        Logger.time();
        Logger.info("");
        long startTime = System.currentTimeMillis();
        //===================================//

        //loop over the training set epoch time
        for(int size = 0 ; size<epoch ; size++)
        {
            //===================================//
            //print the start time of the program//
            Logger.info("");
            Logger.info("==================================");
            Logger.timeExampleStandard("Epoch: ", (size + 1));
            Logger.info("==================================");
            Logger.info("");
            //===================================//
            if(isShuffle)
                //preform random shuffle for the next epoch
                trainInstances = ModelHandler.randomShuffle(trainInstances);

            //train the algorithm
            if (classifier == null) throw new AssertionError();
            W = classifier.train(W, trainInstances, task_loss_params, developInstances, isAvg, verbose);
            if(W == null)
                return;
        }

        if(isAvg == 1)
            this.W = classifier.getAvgWeights();
        //===================================//
        //print the start time of the program//
        Logger.time();
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(totalTime);
        Logger.info("Running time in seconds: " + seconds);
        //===================================//
    }

    /**
     * predict based on the model and return the scores of the best matches
     * @param instances - the test set
     * @param task_loss_params - the parameters for the task loss function, set to null if there are no task loss parameters
     * @param numPredictions2Return - the number of examples to return in the scores
     * @return ArrayList<PredictedLabels> - return the predicted labels as array
     */
    public ArrayList<PredictedLabels> predict(InstancesContainer instances, ArrayList<Double> task_loss_params, int numPredictions2Return, boolean verbose){
        Logger.info("");
        Logger.infoTime("Start Predicting:");
        Logger.time();
        Logger.info("");
        cumulative_loss = 0;
        ArrayList<PredictedLabels> scores = new ArrayList<PredictedLabels>();
        for(int i=0 ; i<instances.getSize() ; i++) {
            Example x = instances.getInstance(i);
            if (x == null)
                continue;
            PredictedLabels score = classifier.test(W, x, numPredictions2Return);

            String y_hat = score.firstKey();
            String y = x.getLabel();
            cumulative_loss += classifier.classifierData.taskLoss.computeTaskLoss(y, y_hat, task_loss_params);
            scores.add(score);
            if(verbose)
                Logger.info("file = "+x.path+", Y = "+y+", Y_Hat = "+y_hat);
        }
        cumulative_loss /= (double) (instances.getSize());
        Logger.info("Cumulative task loss: " + (cumulative_loss));
        Logger.info("");
        return scores;
    }

    /**
     * Plotting the error graph for the validation set
     * @param save flag which indicates whether to write the image or not
     */
    public void plotValidationError(boolean save){
        if(classifier.validationCumulativeLoss != null && classifier.validationCumulativeLoss.size() != 0) {
            Graph graph = new Graph();
            graph.drawGraph(classifier.validationCumulativeLoss, save);
        }
    }

    /**
     * Plotting the values that were stored in the plot_array inside the ClassifierData object
     */
    public void plotValues(){
        if(classifier.classifierData.plot_array != null && classifier.classifierData.plot_array.size() != 0) {
            Graph graph = new Graph();
            graph.drawGraph(classifier.classifierData.plot_array, false);
        }
    }

    /**
     * save the model, notice: we save only the weight vector, the rest of the configuration should be predefined
     * @param path - the path to save the model
     * @throws IOException
     */
    public void saveModel(String path) throws IOException {
        FileOutputStream fout = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(W);
        oos.close();
    }

    /**
     * load the model, notice: we load only the weight vector, the rest of the configuration should be predefined
     * @param path - the model's path
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void loadModel(String path) throws IOException, ClassNotFoundException {
        FileInputStream fin = new FileInputStream(path);
        ObjectInputStream ois = new ObjectInputStream(fin);
        W = (Vector) ois.readObject();
        ois.close();
    }

    // Setters and Getters
    public Vector getWeights(){
        return this.W;
    }
    public double getCumulative_loss() {return cumulative_loss;}
    public Classifier getClassifier() {return this.classifier;}

    public void setUpdateRule(IUpdateRule updateRule){
        this.classifier.classifierData.updateRule = updateRule;
    }
    public void setTaskLoss(ITaskLoss taskLoss){
        this.classifier.classifierData.taskLoss = taskLoss;
    }
    public void setKernel(IKernel kernel){
        this.classifier.classifierData.kernel = kernel;
    }
    public void setInference(IInference inference){
        this.classifier.classifierData.inference = inference;
    }
    public void setFeatureFunctions(IFeatureFunctions phi){ this.classifier.classifierData.phi = phi; }
    public void setReShuffle(boolean isShuffle) { this.isShuffle = isShuffle; }
}
