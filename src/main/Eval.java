package main;

import BL.TaskLoss.TaskLoss;
import Constants.Consts;
import Data.Factory;
import Data.Logger;
import DataAccess.Reader;

import java.util.ArrayList;

public class Eval {


    public static void main(String[] args){
        ArrayList<String> paths = new ArrayList<String>();
        paths.add("data/train/labels/");
        paths.add("data/train/second_labels/");

        EvaluatePrediction("data/res_first_subscriber.txt", paths);
    }
    // taggersPath is the path to the dir where all the labels are
    public static void EvaluatePrediction(String predictPath, ArrayList<String> taggersPath) {

        Reader reader = Factory.getReader(2);
        ArrayList<ArrayList<String>> predictedLabels = reader.readFile(predictPath, Consts.SPACE);
        ArrayList<Double> tast_params = new ArrayList<Double>();
        tast_params.add(25.0);
        TaskLoss loss = Factory.getTaskLoss(0);
        double[] losses = new double[taggersPath.size()];

        if(predictedLabels.size() <= 0)
            return;

        for(ArrayList<String> line : predictedLabels){
            String tmp = line.get(0).substring(1,line.get(0).length()-2);
            String[] pathValues = tmp.split(Consts.SLASH);
            String fileName = pathValues[pathValues.length-1];
            fileName = fileName.replace(".data","_to_fe.labels");

//            String[] labelValues = line.get(1).split(Consts.COLON_SPLITTER);
            String label = line.get(1).substring(1,line.get(1).length())+"-"+line.get(2).substring(0,line.get(2).length()-2);

            for(int i=0 ; i<taggersPath.size() ; i++) {
                ArrayList<ArrayList<String>> data = reader.readFile(taggersPath.get(i)+fileName, Consts.SPACE);
                String taggerLabel = data.get(1).get(0)+Consts.CLASSIFICATION_SPLITTER+data.get(1).get(1);
                losses[i] += loss.computeTaskLoss(label,taggerLabel,tast_params);
            }
        }

        for(int i=0 ; i<losses.length ; i++) {
            losses[i] /= predictedLabels.size();
            Logger.info("Cumulative loss " + (i + 1) + " tagger: " + losses[i]);
        }
    }
}
