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
import jsc.distributions.Gamma;

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
    //#############################################################################//
    //==========================LOADING CONFIG PARAMETERS==========================//
    //#############################################################################//
            //get the config path
            if(args.length != 1){
                Logger.error("Error with argument parameters.");
                return;
            }
            Paths.getInstance().CONFIG_PATH_TRAIN = args[0];

			ConfigFileGetter configGetter = Factory.getConfigGetter(0);
			ArrayList<String> arguments = configGetter.getConfigDataTrain(Factory.getReader(0));
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
            Vector W = ModelHandler.setWeights(isW, Paths.getInstance().INIT_WEIGHTS_PATH);
            Logger.infoTime("Loading train data...");
            Logger.infoTime("Train file: " + Paths.getInstance().TRAIN_PATH+". ");
            Logger.infoTime("Validation file: " + Paths.getInstance().VALIDATION_PATH+". ");

            //read the data and parse it
            Reader reader = Factory.getReader(readerType);
            InstancesContainer instances;
            instances = reader.readData(Paths.getInstance().TRAIN_PATH, Consts.SPACE, Consts.COLON_SPLITTER);
            if ( instances.getSize() == 0 )
                return;
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
            Classifier classifier = Factory.getClassifier(task, type, prediction, kernel, phi, algorithmParameters);

            //loop over the training set epoch time
            for(int size = 0 ; size<epoch ; size++)
            {
                //preform random shuffle for the next epoch
                instances = ModelHandler.randomShuffle(instances);
                //train the algorithm
                W = classifier.train(W, instances, task_param, reader);
                if(W == null)
                    return;

                //===================================//
                //print the start time of the program//
                Logger.info("");
                Logger.info("==================================");
                Logger.timeExample("Epoch: ", size);
                Logger.info("==================================");
                Logger.info("");
                //===================================//
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
                writer.writeHashMap2File(Paths.getInstance().OUTPUT_WEIGHTS_PATH, classifier.getAvgWeights());
            else
                writer.writeHashMap2File(Paths.getInstance().OUTPUT_WEIGHTS_PATH, W);
    //=============================================================================//

		} catch (Exception e) {
            Logger.error(e.getMessage());
            e.printStackTrace();
		}
	}
}
