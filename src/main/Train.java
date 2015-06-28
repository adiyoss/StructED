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

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import BL.Classifier;
import Constants.ConfigParameters;
import Constants.Consts;
import Constants.ErrorConstants;
import Constants.Paths;
import Data.*;
import Data.Entities.Vector;
import DataAccess.Reader;
import DataAccess.Writer;
import Helpers.ModelHandler;
import view.Graph;

import static Constants.Paths.getInstance;
import static Data.Factory.getClassifier;
import static Data.Factory.getReader;

public class Train {
    //parameters
    private final static int TYPE = 0;
    private final static int TASK = 1;
    private final static int EPOCH = 2;
    private final static int TASK_PARAM = 3;
    private final static int KERNEL = 4;
    private final static int IS_W = 5;
    private final static int PHI = 6;
    private final static int PREDICTION = 7;
    private final static int READER = 8;
    private final static int WRITER = 9;
    private final static int IS_AVERAGE = 10;

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
            getInstance().CONFIG_PATH_TRAIN = args[0];

			ConfigFileGetter configGetter = Factory.getConfigGetter(0);
			ArrayList<String> arguments = configGetter.getConfigDataTrain(getReader(0));
			if(arguments == null){
                Logger.error(ErrorConstants.CONFIG_ERROR);
				return;
			}

            //=============PARSING THE CONFIG FILES===========//
            //config parameters
            int type;
            int task;
            int epoch;
            int kernel;
            int isW;
            int phi;
            int prediction;
            int readerType;
            int writerType;
            int isAvg;
            ArrayList<Double> task_param = new ArrayList<Double>();
            try {
                type = Integer.valueOf(arguments.get(TYPE));
                task = Integer.valueOf(arguments.get(TASK));
                epoch = Integer.valueOf(arguments.get(EPOCH));
                String[] tmp_param = arguments.get(TASK_PARAM).split(Consts.PARAMS_SPLITER);
                for(int idx=0 ; idx < tmp_param.length ; idx++)
                    task_param.add(Double.parseDouble(tmp_param[idx]));
                kernel = Integer.valueOf(arguments.get(KERNEL));
                isW = Integer.valueOf(arguments.get(IS_W));
                phi = Integer.valueOf(arguments.get(PHI));
                prediction = Integer.valueOf(arguments.get(PREDICTION));
                readerType = Integer.valueOf(arguments.get(READER));
                writerType = Integer.valueOf(arguments.get(WRITER));
                isAvg = Integer.valueOf(arguments.get(IS_AVERAGE));
            } catch (Exception e){
                Logger.error("Wrong config file parameters.");
                return;
            }
            //===============================================//
            //===============VALIDATIONS================//
            if(epoch < 0){
                Logger.error(ErrorConstants.EPOCH_ERROR);
                return;
            }
            //==========================================//

            //creating the new set of arguments for the specific algorithm
            ArrayList<Double> algorithmParameters = new ArrayList<Double>();
            for(int i = ConfigParameters.GENERAL_PARAMS_SIZE_TRAIN_VALIDATION ; i<arguments.size() ; i++)
                algorithmParameters.add(Double.parseDouble(arguments.get(i)));
            //=============================================================================//


            //#############################################################################//
            //==================LOADING THE TRAIN DATA AND THE MODEL=======================//
            //#############################################################################//
            //init the weights by the user choice and the best_W parameter, existing ones or just 0's
            Vector W = ModelHandler.setWeights(isW, getInstance().INIT_WEIGHTS_PATH);
            Logger.infoTime("Loading train data...");
            Logger.infoTime("Train file: " + getInstance().TRAIN_PATH+". ");
            Logger.infoTime("Validation file: " + getInstance().VALIDATION_PATH+". ");

            //read the data and parse it
            Reader reader = getReader(readerType);
            InstancesContainer instances;
            instances = reader.readData(getInstance().TRAIN_PATH, Consts.SPACE, Consts.COLON_SPLITTER);
            InstancesContainer developInstances;
            developInstances = reader.readData(Paths.getInstance().VALIDATION_PATH, Consts.SPACE, Consts.COLON_SPLITTER);
            if ( instances.getSize() == 0 ) return;

            //=============================================================================//

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

            //create the classifier
            Classifier classifier = getClassifier(task, type, prediction, kernel, phi, algorithmParameters);

            //loop over the training set epoch time
            for(int size = 0 ; size<epoch ; size++)
            {
                //===================================//
                //print the start time of the program//
                Logger.info("");
                Logger.info("==================================");
                Logger.progressMessage("Epoch: " + (size + 1));
                Logger.info("==================================");
                Logger.info("");
                //===================================//

                //preform random shuffle for the next epoch
                instances = ModelHandler.randomShuffle(instances);

                //train the algorithm
                if (classifier == null) throw new AssertionError();
                W = classifier.train(W, instances, task_param, developInstances, isAvg);
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
            //=============================================================================//

            //#############################################################################//
            //==========================WRITE THE MODEL====================================//
            //#############################################################################//
            //write the final weights to output file
            Writer writer = Factory.getWriter(writerType);
            if(isAvg == 1)
                writer.writeHashMap2File(getInstance().OUTPUT_WEIGHTS_PATH, classifier.getAvgWeights());
            else
                writer.writeHashMap2File(getInstance().OUTPUT_WEIGHTS_PATH, W);
            //=============================================================================//

            //#############################################################################//
            //============== DRAW THE CUMULATIVE LOSS OF THE VALIDATION SET ===============//
            //#############################################################################//
            if (classifier.validationCumulativeLoss.size() != 0) {
                Graph graph = new Graph();
                graph.drawGraph(classifier.validationCumulativeLoss, true);
            }

            //=============================================================================//
		} catch (Exception e) {
            Logger.error(e.getMessage());
            e.printStackTrace();
		}
	}
}
