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

package Constants;

import java.util.HashMap;

public class ConfigParameters {

    //SINGLETON
    private static ConfigParameters instance = new ConfigParameters();
    public static ConfigParameters getInstance(){
        return instance;
    }
    private ConfigParameters(){}

    //GENERAL PARAMETERS
    public static final String COMMENT = "#";
    public static final String TRAIN_PATH = "train_path";
    public static final String TEST_PATH = "test_path";
    public static final String VALIDATION_PATH = "validation_path";
    public static final String OUTPUT_FILE_WEIGHTS = "w_output";
    public static final String TYPE = "type";
    public static final String TASK = "task";
    public static final String EPOCH = "epoch";
    public static final String TASK_PARAM = "task_param";
    public static final String KERNEL = "IKernel";
    public static final String W_INIT = "init_w";
    public static final String PHI = "phi";
    public static final String PREDICTION = "prediction";
    public static final String READER = "reader";
    public static final String WRITER = "writer";
    public static final String IS_AVG = "isAvg";
    public static final String SIZE_OF_VECTOR = "size_of_vector";

    public static final String C = "c"; //USED 4 PA
    public static final String LAMBDA = "lambda"; //USED 4 SVM/CRF/Ramp Loss/Probit Loss
    public static final String ETA = "eta"; //USED 4 SVM/Direct Loss/CRF/Ramp Loss/Probit Loss
    public static final String TRAIN_SIZE = "train_size"; //USED 4 SVM
    public static final String EPSILON = "epsilon"; //USED 4 Direct Loss
    public static final String NUM_OF_ITERATION = "num_of_iteration"; //USED 4 Probit Loss
    public static final String NOISE_ALL_VECTOR = "noise_all_vector"; //USED 4 Probit Loss
    public static final String MEAN = "noise_mean"; //USED 4 Probit Loss
    public static final String STD_DEV = "noise_std"; //USED 4 Probit Loss

    //GENERAL PARAMETERS SIZES TRAIN
    public static final int GENERAL_PARAMS_SIZE_TRAIN = 15; //The number of general parameters in the train config file
    public static final int GENERAL_PARAMS_SIZE_TRAIN_MINIMUM = 10; //The number of general parameters in the train config file
    public static final int GENERAL_PARAMS_SIZE_TRAIN_VALIDATION = 11; //The number of general parameters in the train config file
    public static final int PA_PARAMS_SIZE = 1; //The number params 4 PA
    public static final int SVM_PARAMS_SIZE = 2; //The number params 4 SVM
    public static final int DL_PARAMS_SIZE = 2; //The number params 4 Direct Loss
    public static final int CRF_PARAMS_SIZE = 2; //The number params 4 CRF
    public static final int RL_PARAMS_SIZE = 2; //The number params 4 Ramp Loss
    public static final int PL_PARAMS_SIZE = 6; //The number params 4 Probit Loss
    public static final int P_PARAMS_SIZE = 0; //The number params 0 Perceptron

    //GENERAL PARAMETERS SIZES TEST
    public static final int GENERAL_PARAMS_SIZE_TEST = 12; //The number of must have params in the test config file
    public static final int GENERAL_PARAMS_SIZE_TEST_VALIDATION = 10; //The number of must have params in the test config file
    public static final String W_PATH = "w_path";
    public static final String OUTPUT_FILE = "output_file";
    public static final String EXAMPLES_2_DISPLAY = "examples_2_display";

    //RBF_PARAMETERS
    public static double SIGMA = 1; //The number of must have params in the test config file
    public static int VECTOR_SIZE = 1;
}
