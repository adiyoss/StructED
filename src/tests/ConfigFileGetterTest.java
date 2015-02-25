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

package tests;

import Constants.Paths;
import Data.ConfigFileGetter;
import Data.Factory;
import junit.framework.TestCase;

import java.util.ArrayList;

/*
    Unit tests for the Config File Getter class
 */
public class ConfigFileGetterTest extends TestCase {

    public void testGetConfigDataTrain() throws Exception {

        ConfigFileGetter configGetter = Factory.getConfigGetter(0);
        Paths.getInstance().CONFIG_PATH_TRAIN = "data/test_data/conf_train/config_CRF.txt";
        ArrayList<String> arguments_CRF = configGetter.getConfigDataTrain(Factory.getReader(0));

        Paths.getInstance().CONFIG_PATH_TRAIN = "data/test_data/conf_train/config_DL.txt";
        ArrayList<String> arguments_DL = configGetter.getConfigDataTrain(Factory.getReader(0));

        Paths.getInstance().CONFIG_PATH_TRAIN = "data/test_data/conf_train/config_PA.txt";
        ArrayList<String> arguments_PA = configGetter.getConfigDataTrain(Factory.getReader(0));

        Paths.getInstance().CONFIG_PATH_TRAIN = "data/test_data/conf_train/config_Probit.txt";
        ArrayList<String> arguments_Probit = configGetter.getConfigDataTrain(Factory.getReader(0));

        Paths.getInstance().CONFIG_PATH_TRAIN = "data/test_data/conf_train/config_Ramp.txt";
        ArrayList<String> arguments_Ramp = configGetter.getConfigDataTrain(Factory.getReader(0));

        Paths.getInstance().CONFIG_PATH_TRAIN = "data/test_data/conf_train/config_SVM.txt";
        ArrayList<String> arguments_SVM = configGetter.getConfigDataTrain(Factory.getReader(0));

        assertEquals("Wrong file format should be null, ", null, arguments_CRF);
        assertEquals("length must be: 13", 13, arguments_DL.size());
        assertEquals("length must be: 12", 12, arguments_PA.size());
        assertEquals("length must be: 17", 17, arguments_Probit.size());
        assertEquals("Wrong file format should be null", null, arguments_Ramp);
        assertEquals("length must be: 13", 13, arguments_SVM.size());
    }

    public void testGetConfigDataTest() throws Exception {

        ConfigFileGetter configGetter = Factory.getConfigGetter(0);
        Paths.getInstance().CONFIG_PATH_TEST = "data/test_data/conf_test/config_dummy.txt";
        ArrayList<String> arguments_dummy = configGetter.getConfigDataTest(Factory.getReader(0));

        Paths.getInstance().CONFIG_PATH_TEST = "data/test_data/conf_test/config_mnist.txt";
        ArrayList<String> arguments_mnist = configGetter.getConfigDataTest(Factory.getReader(0));

        Paths.getInstance().CONFIG_PATH_TEST = "data/test_data/conf_test/config_vowel.txt";
        ArrayList<String> arguments_vowel = configGetter.getConfigDataTest(Factory.getReader(0));

        Paths.getInstance().CONFIG_PATH_TEST = "data/test_data/conf_test/config_dummy_error.txt";
        ArrayList<String> arguments_dummy_error = configGetter.getConfigDataTest(Factory.getReader(0));

        Paths.getInstance().CONFIG_PATH_TEST = "data/test_data/conf_test/blabla.txt";
        ArrayList<String> arguments_blabla = configGetter.getConfigDataTest(Factory.getReader(0));

        assertEquals("Wrong file format should be null, ", null, arguments_dummy_error);
        assertEquals("Wrong file format should be null, ", null, arguments_blabla);
        assertEquals("length must be: 11", 11, arguments_mnist.size());
        assertEquals("length must be: 11", 11, arguments_vowel.size());
        assertEquals("length must be: 11", 11, arguments_dummy.size());
    }
}