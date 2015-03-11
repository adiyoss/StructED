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

package Data;

import java.util.ArrayList;

import Constants.ConfigParameters;
import Constants.Consts;
import Constants.ErrorConstants;
import Constants.Paths;
import DataAccess.Reader;
import Helpers.ConverterHelplers;

public class ConfigFileGetter {

    //parse the train config file
	public ArrayList<String> getConfigDataTrain(Reader reader)
	{
        try{
            ArrayList<String> result = new ArrayList<String>();
            ArrayList<ArrayList<String>> data = reader.readFile(Paths.getInstance().CONFIG_PATH_TRAIN,Consts.COLON_SPLITTER);
            int index = 0;

            //validation
            if(data == null || data.size() == 0 || data.size() < ConfigParameters.GENERAL_PARAMS_SIZE_TRAIN_MINIMUM){
                Logger.error(ErrorConstants.CONFIG_ARGUMENTS_ERROR);
                return null;
            }

            //loop over the rows
            int i=-1;
            while(i<ConfigParameters.GENERAL_PARAMS_SIZE_TRAIN-1){
                if(data.get(index).get(0).startsWith(ConfigParameters.COMMENT) || data.get(index).get(0).equalsIgnoreCase("")) {
                    index++;
                    continue;
                }
                i++;
                //validate the default first four parameters
                switch (i){
                    case 0:
                        if(data.get(index).get(0).equalsIgnoreCase(ConfigParameters.TRAIN_PATH)){
                            Paths.getInstance().TRAIN_PATH = data.get(index).get(1);
                            index++;
                        } else {
                            Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR+", Train Path.");
                            return null;
                        }
                        continue;
                    case 1:
                        if(data.get(index).get(0).equalsIgnoreCase(ConfigParameters.VALIDATION_PATH)){
                            Paths.getInstance().VALIDATION_PATH = data.get(index).get(1);
                            index++;
                        }
                        continue;
                    case 2:
                        if(data.get(index).get(0).equalsIgnoreCase(ConfigParameters.OUTPUT_FILE_WEIGHTS)){
                            Paths.getInstance().OUTPUT_WEIGHTS_PATH = data.get(index).get(1);
                            index++;
                        } else {
                            Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                            return null;
                        }
                        continue;
                    case 3:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.TYPE)){
                            Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                            return null;
                        }
                        break;
                    case 4:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.TASK)){
                            Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                            return null;
                        }
                        break;
                    case 5:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.EPOCH)){
                            Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                            return null;
                        }
                        break;
                    case 6:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.TASK_PARAM)){
                            result.add(String.valueOf(Consts.ERROR_NUMBER)); //default value
                            continue;
                        }
                        break;
                    case 7:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.KERNEL)){
                            result.add(String.valueOf(Consts.ERROR_NUMBER)); //default value
                            continue;
                        }
                        break;
                    case 8:
                        if(data.get(index).get(0).equalsIgnoreCase(ConfigParameters.W_INIT)){
                            Paths.getInstance().INIT_WEIGHTS_PATH = data.get(index).get(1);
                            result.add(String.valueOf(1)); //default value
                            index++;
                        } else {
                            result.add(String.valueOf(0)); //default value
                        }
                        continue;
                    case 9:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.PHI)){
                            Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                            return null;
                        }
                        break;
                    case 10:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.PREDICTION)){
                            Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                            return null;
                        }
                        break;
                    case 11:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.READER)){
                            Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                            return null;
                        }
                        break;
                    case 12:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.WRITER)){
                            Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                            return null;
                        }
                        break;
                    case 13:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.IS_AVG)){
                            result.add(String.valueOf(Consts.ERROR_NUMBER)); //default value
                            continue;
                        }
                        break;
                    case 14:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.SIZE_OF_VECTOR)){
                            Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                            return null;
                        } else {
                            ConfigParameters.getInstance().VECTOR_SIZE = Integer.parseInt(data.get(index).get(1));
                            index++;
                            continue;
                        }
                    default:
                        break;
                }

                //support more then one parameter in line, for future use
                for(int j=1 ; j<data.get(index).size() ; j++)
                {
                    if(data.get(index).get(0).equalsIgnoreCase(ConfigParameters.KERNEL) && (j!=1)){
                        ConfigParameters.getInstance().SIGMA = Double.parseDouble(data.get(index).get(j));
                        continue;
                    }
                    result.add((data.get(index).get(j)));
                }

                index++;
            }

            //validate the rest of the parameters depends on the type
            switch (Integer.valueOf(result.get(0))){
                case 0:
                    if(!validatePA(data, index)) // PA
                        return null;
                    break;
                case 1:
                    if(!validateSVM(data, index)) // SVM
                        return null;
                    break;
                case 2:
                    if(!validateDL(data, index)) //DLM
                        return null;
                    break;
                case 3:
                    if(!validateCRF(data, index)) //CRF
                        return null;
                    break;
                case 4:
                    if(!validateRL(data, index)) //RL
                        return null;
                    break;
                case 5:
                    if(!validatePL(data, index)) //PL
                        return null;
                    break;
                case 6:
                    // structured perceptron does not need any special parameters
                    break;
                case 7:
                    if(!validateSVM(data, index)) // Rank-SVM
                        return null;
                    break;
                default:
                    break;
            }

            //insert the rest of the parameters
            for(int idx=index; idx<data.size(); idx++)
            {
                for(int j=1 ; j<data.get(idx).size() ; j++)
                {
                    if(!ConverterHelplers.tryParseDouble(data.get(idx).get(j))){
                        Logger.error(ErrorConstants.CONFIG_ERROR);
                        return null;
                    }

                    result.add((data.get(idx).get(j)));
                }
            }

            return result;

        } catch (Exception e) {
            Logger.error(ErrorConstants.CONFIG_ARGUMENTS_ERROR);
            e.printStackTrace();
            return null;
        }
	}

    //parse the test config file
    public ArrayList<String> getConfigDataTest(Reader reader)
    {
        try{
            ArrayList<String> result = new ArrayList<String>();
            ArrayList<ArrayList<String>> data = reader.readFile(Paths.getInstance().CONFIG_PATH_TEST,Consts.COLON_SPLITTER);
            int index = 0;

            //validation
            if(data == null || data.size() == 0 || data.size() < ConfigParameters.GENERAL_PARAMS_SIZE_TEST_VALIDATION)
            {
                Logger.error(ErrorConstants.CONFIG_ARGUMENTS_ERROR);
                return null;
            }

            //loop over the rows
            for(int i=0 ; i<ConfigParameters.GENERAL_PARAMS_SIZE_TEST; i++)
            {
                //validate the default first four parameters
                switch (i){
                    case 0:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.TEST_PATH)){
                            Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                            return null;
                        }
                        break;
                    case 1:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.OUTPUT_FILE)){
                            Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                            return null;
                        }
                        break;
                    case 2:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.W_PATH)){
                            Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                            return null;
                        }
                        break;
                    case 3:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.EXAMPLES_2_DISPLAY)){
                            result.add(String.valueOf(Consts.ERROR_NUMBER)); //default value
                            continue;
                        }
                        break;
                    case 4:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.TASK)){
                            Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                            return null;
                        }
                        break;
                    case 5:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.TASK_PARAM)){
                            result.add(String.valueOf(Consts.ERROR_NUMBER)); //default value
                            continue;
                        }
                        break;
                    case 6:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.KERNEL)){
                            result.add(String.valueOf(Consts.ERROR_NUMBER)); //default value
                            continue;
                        }
                        break;
                    case 7:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.PHI)){
                            Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                            return null;
                        }
                        break;
                    case 8:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.PREDICTION)){
                            Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                            return null;
                        }
                        break;
                    case 9:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.READER)){
                            Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                            return null;
                        }
                        break;
                    case 10:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.WRITER)){
                            Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                            return null;
                        }
                        break;
                    case 11:
                        if(!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.SIZE_OF_VECTOR)){
                            Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                            return null;
                        } else {
                            ConfigParameters.getInstance().VECTOR_SIZE = Integer.parseInt(data.get(index).get(1));
                            index++;
                            continue;
                        }
                    default:
                        break;
                }

                //support more then one parameter in line, for future use
                for(int j=1 ; j<data.get(index).size() ; j++){
                    if(data.get(index).get(0).equalsIgnoreCase(ConfigParameters.KERNEL) && (j!=1))
                        ConfigParameters.getInstance().SIGMA = Double.parseDouble(data.get(index).get(j));
                    else
                        result.add(data.get(index).get(j));
                }
                index++;
            }
            return result;

        }catch (Exception e){
            Logger.error(ErrorConstants.CONFIG_ARGUMENTS_ERROR);
            e.printStackTrace();
            return null;
        }
    }
    //==================================================================//
    //========================UPDATE VALIDATIONS========================//
    //==================================================================//
    public boolean validatePA(ArrayList<ArrayList<String>> data, int index)
    {
        try {
            if (!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.C)) {
                Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                return false;
            }
            return true;
        } catch (Exception e){
            Logger.error("Problem with algorithm parameters, PA");
            return false;
        }
    }

    public boolean validateSVM(ArrayList<ArrayList<String>> data, int index)
    {
        try {
            if (!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.LAMBDA)
                    || !data.get(index + 1).get(0).equalsIgnoreCase(ConfigParameters.ETA)) {
                Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                return false;
            }
            return true;
        } catch (Exception e){
            Logger.error("Problem with algorithm parameters, SVM");
            return false;
        }
    }

    public boolean validateDL(ArrayList<ArrayList<String>> data, int index)
    {
        try {
            if (!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.ETA)
                    || !data.get(index + 1).get(0).equalsIgnoreCase(ConfigParameters.EPSILON)) {
                Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                return false;
            }
            return true;
        } catch (Exception e){
        Logger.error("Problem with algorithm parameters, DL");
        return false;
    }
    }

    public boolean validateCRF(ArrayList<ArrayList<String>> data, int index)
    {
        try {
            if (!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.ETA)
                    || !data.get(index + 1).get(0).equalsIgnoreCase(ConfigParameters.LAMBDA)) {
                Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                return false;
            }
            return true;
        } catch (Exception e){
            Logger.error("Problem with algorithm parameters, CRF");
            return false;
        }
    }
    public boolean validateRL(ArrayList<ArrayList<String>> data, int index)
    {
        try {
            if (!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.ETA)
                    || !data.get(index + 1).get(0).equalsIgnoreCase(ConfigParameters.LAMBDA)) {
                Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                return false;
            }
            return true;
        } catch (Exception e){
            Logger.error("Problem with algorithm parameters, RL");
            return false;
        }
    }
    public boolean validatePL(ArrayList<ArrayList<String>> data, int index)
    {
        try {
            if (!data.get(index).get(0).equalsIgnoreCase(ConfigParameters.ETA)
                    || !data.get(index + 1).get(0).equalsIgnoreCase(ConfigParameters.LAMBDA)
                    || !data.get(index + 2).get(0).equalsIgnoreCase(ConfigParameters.NUM_OF_ITERATION)
                    || !data.get(index + 3).get(0).equalsIgnoreCase(ConfigParameters.NOISE_ALL_VECTOR)
                    || !data.get(index + 4).get(0).equalsIgnoreCase(ConfigParameters.MEAN)
                    || !data.get(index + 5).get(0).equalsIgnoreCase(ConfigParameters.STD_DEV)) {
                Logger.error(ErrorConstants.CONFIG_ARGUMENTS_TYPE_ERROR);
                return false;
            }
            return true;
        } catch (Exception e){
            Logger.error("Problem with algorithm parameters, Probit");
            return false;
        }
    }
}
