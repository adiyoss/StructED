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

package BL;

import BL.Algorithms.IUpdateRule;
import BL.Inference.IInference;
import BL.Kernels.IKernel;
import BL.TaskLoss.ITaskLoss;
import Data.Entities.Example;
import Data.Entities.PredictedLabels;
import Data.Entities.Vector;
import Data.FeatureFunctions.IFeatureFunctions;
import Data.InstancesContainer;
import Data.Logger;
import Helpers.ModelHandler;
import view.Graph;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by yossiadi on 6/28/15.
 */
public class StructEDModel implements Serializable{

    // data members
    private Classifier classifier;
    private Vector W;

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
    public void train(InstancesContainer trainInstances, ArrayList<Double> task_loss_params, InstancesContainer developInstances, int epoch, int isAvg) throws Exception {
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

            //preform random shuffle for the next epoch
            trainInstances = ModelHandler.randomShuffle(trainInstances);

            //train the algorithm
            if (classifier == null) throw new AssertionError();
            W = classifier.train(W, trainInstances, task_loss_params, developInstances, isAvg);
            if(W == null)
                return;
        }

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
     * @return
     */
    public ArrayList<PredictedLabels> predict(InstancesContainer instances, ArrayList<Double> task_loss_params, int numPredictions2Return){
        double cumulative_loss = 0;
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
        }

        Logger.info("Cumulative task loss: " + (cumulative_loss / (double) (instances.getSize())));
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
    public void setFeatureFunctions(IFeatureFunctions phi){
        this.classifier.classifierData.phi = phi;
    }
}
