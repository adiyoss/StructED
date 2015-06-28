/*
 * StructED - Machine Learning, Structured Prediction Package written in Java.
 * Copyright (C) 2014 Yossi Adi, E-Mail: yossiadidrum@gmail.com
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

package main;

import BL.Classifier;
import BL.TaskLoss.ITaskLoss;
import BL.TaskLoss.TaskLossVowelDuration;
import Constants.Consts;
import Constants.ErrorConstants;
import Constants.Paths;
import Data.*;
import Data.Entities.Example;
import Data.Entities.PredictedLabels;
import Data.Entities.Vector;
import DataAccess.Reader;
import DataAccess.Writer;
import Helpers.ModelHandler;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Test {
    //parameters
    private final static int TEST_PATH = 0;
    private final static int OUTPUT_FILE = 1;
    private final static int W_PATH = 2;
    private final static int EXAMPLES_2_DISPLAY = 3;
    private final static int TASK = 4;
    private final static int TASK_PARAM = 5;
    private final static int KERNEL = 6;
    private final static int PHI = 7;
    private final static int PREDICTION = 8;
    private final static int READER = 9;
    private final static int WRITER = 10;

    public static void main(String[] args) {
        try{
            //==============COPYRIGHT============//
            Logger.info("This program comes with ABSOLUTELY NO WARRANTY");
            Logger.info("This is free software, and you are welcome to redistribute it under certain conditions, see LICENSE file for more information.");
            //===================================//

            //#############################################################################//
            //==========================LOADING CONFIG PARAMETERS==========================//
            //#############################################################################//
            //get the config path
            if(args.length != 1){
                Logger.error("Error with argument parameters.");
                return;
            }
            Paths.getInstance().CONFIG_PATH_TEST = args[0];

            ConfigFileGetter configGetter = Factory.getConfigGetter(0);
            ArrayList<String> arguments = configGetter.getConfigDataTest(Factory.getReader(0));
            if(arguments == null){
                Logger.error(ErrorConstants.CONFIG_ERROR);
                return;
            }
            //getting the config parameters
            String testPath;
            String outputFile;
            String w_path;
            int examples2Display;
            int task;
            ArrayList<Double> task_param = new ArrayList<Double>();
            int kernel;
            int phiValue;
            int predictValue;
            int readerType;
            int writerType;

            try{
                //getting the config parameters
                testPath = arguments.get(TEST_PATH);
                outputFile =  arguments.get(OUTPUT_FILE);
                w_path = arguments.get(W_PATH);
                examples2Display = Integer.parseInt(arguments.get(EXAMPLES_2_DISPLAY));
                task = Integer.parseInt(arguments.get(TASK));
                String[] tmp_params = arguments.get(TASK_PARAM).split(Consts.PARAMS_SPLITER);
                for(int idx=0 ; idx < tmp_params.length ; idx++)
                    task_param.add(Double.parseDouble(tmp_params[idx]));
                kernel = Integer.parseInt(arguments.get(KERNEL));
                phiValue = Integer.parseInt(arguments.get(PHI));
                predictValue = Integer.parseInt(arguments.get(PREDICTION));
                readerType = Integer.parseInt(arguments.get(READER));
                writerType = Integer.parseInt(arguments.get(WRITER));
            } catch (Exception e){
                Logger.error("Wrong config file parameters.");
                return;
            }
            //clean all the log files
            Logger.clean();

            //=============================================================================//

            //#############################################################################//
            //===================LOADING THE TEST DATA AND THE MODEL=======================//
            //#############################################################################//
            Classifier classifier = Factory.getClassifier(task,-1,predictValue,kernel,phiValue,null);

            //get the weights
            Reader reader = Factory.getReader(readerType);
            Writer writer = Factory.getWriter(writerType);
            ArrayList<ArrayList<String>> wData = reader.readFile(w_path, Consts.SPACE);

            // scores file
            Vector W = ModelHandler.convert2Weights(wData);
            if(W == null){
                Logger.error(ErrorConstants.WEIGHTS_VALUES);
                return;
            }

            Logger.infoTime("Loading test data...");
            Logger.infoTime("Test file: " + testPath+". ");
            Logger.info("");
            Logger.infoTime("StructEDModel: " + w_path+". ");
            Logger.info("==============================================================");

            // read the test data and parse it
            InstancesContainer instances;
            instances = reader.readData(testPath, Consts.SPACE, Consts.COLON_SPLITTER);
            if ( instances.getSize() == 0 )
                return;
            //=============================================================================//
            long startTime = System.currentTimeMillis();

            //#############################################################################//
            //=================================TEST========================================//
            //#############################################################################//
            // run over all test instances and predict
            double cumulative_loss = 0;

            // ================= //
            double cum_los_2_3 = 0;
            double cum_los_4_7 = 0;
            double cum_los_6_10 = 0;
            ArrayList<Double> params_2_3 = new ArrayList<Double>(){{add(2.0); add(3.0);}};
            ArrayList<Double> params_4_7 = new ArrayList<Double>(){{add(4.0); add(7.0);}};
            ArrayList<Double> params_6_10 = new ArrayList<Double>(){{add(6.0); add(10.0);}};
            ITaskLoss t_2_3 = new TaskLossVowelDuration();
            ITaskLoss t_4_7 = new TaskLossVowelDuration();
            ITaskLoss t_6_10 = new TaskLossVowelDuration();
            // ================= //

            writer.clearPrevResult(outputFile);

            for(int i=0 ; i<instances.getSize() ; i++){
                Example x = instances.getInstance(i);
                if(x == null)
                    continue;
                PredictedLabels scores = classifier.test(W, x, examples2Display);
                writer.writeScoresFile(x.path, outputFile, scores, examples2Display);

                String y_hat = scores.firstKey();
                if(y_hat == null)
                    break;

                String y = x.getLabel();
                String message = "Processing example number: "+(i+1)+", Real Label = "+y+", IInference: "+y_hat;

                Logger.info(message);
                Logger.log2File(message);
                cumulative_loss += classifier.classifierData.taskLoss.computeTaskLoss(y, y_hat, task_param);
                cum_los_2_3 += t_2_3.computeTaskLoss(y, y_hat, params_2_3);
                cum_los_4_7 += t_4_7.computeTaskLoss(y, y_hat, params_4_7);
                cum_los_6_10 += t_6_10.computeTaskLoss(y, y_hat, params_6_10);
                CacheVowelData.clearCacheValues();
            }

            //===================================//
            //print the start time of the program//
            Logger.info("==============================================================");

            Logger.info("==============================================================");
            Logger.time();
            long endTime   = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            long seconds = TimeUnit.MILLISECONDS.toSeconds(totalTime);
            Logger.info("Running time in seconds: " + seconds);
            Logger.info("==============================================================");
            //===================================//

            Logger.info("");
            Logger.info("==============================================================");
            Logger.info("Total files predicted: "+instances.getSize());
            Logger.info("Cumulative task loss 0,0: " + (cumulative_loss / (double) (instances.getSize())));
            Logger.info("==============================================================");
            Logger.info("");
            Logger.info("==============================================================");
            Logger.info("Total files predicted: "+instances.getSize());
            Logger.info("Cumulative task loss 10,15: " + (cum_los_2_3 / (double) (instances.getSize())));
            Logger.info("==============================================================");
            Logger.info("");
            Logger.info("==============================================================");
            Logger.info("Total files predicted: "+instances.getSize());
            Logger.info("Cumulative task loss 20,35: " + (cum_los_4_7 / (double) (instances.getSize())));
            Logger.info("==============================================================");
            Logger.info("");
            Logger.info("==============================================================");
            Logger.info("Total files predicted: "+instances.getSize());
            Logger.info("Cumulative task loss 30,50: " + (cum_los_6_10 / (double) (instances.getSize())));
            Logger.info("==============================================================");

            Logger.log2File("==============================================================");
            Logger.log2File("Total files predicted: "+instances.getSize());
            Logger.log2File("Cumulative task loss: " + (cumulative_loss / (double) (instances.getSize())));
            //=============================================================================//

        } catch (Exception e) {
            Logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
