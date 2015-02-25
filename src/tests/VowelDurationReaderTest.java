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

import Constants.Consts;
import Data.Factory;
import Data.InstancesContainer;
import Data.LazyInstancesContainer;
import DataAccess.Reader;
import junit.framework.TestCase;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;

/*
    Unit tests for the Vowel Duration Reader class
 */
public class VowelDurationReaderTest extends TestCase {

    public void testReadData() throws Exception {
        InstancesContainer instances_train;
        InstancesContainer instances_test;
        Reader reader =  Factory.getReader(2);
        instances_train = reader.readData("data/test_data/db/train_vowel.txt", Consts.SPACE, Consts.COLON_SPLITTER);
        instances_test = reader.readData("data/test_data/db/test_vowel.txt", Consts.SPACE, Consts.COLON_SPLITTER);

        assertEquals("Number of examples must be: 36",36,instances_train.getSize());
        assertEquals("Number of examples must be: 15",15,instances_test.getSize());
        assertThat(instances_test, instanceOf(LazyInstancesContainer.class));
        assertThat(instances_train, instanceOf(LazyInstancesContainer.class));
    }


    public void testReadFile() throws Exception {
        Reader reader =  Factory.getReader(2);
        assertEquals("length must be: 11", 11, reader.readFile("data/test_data/conf_train/config_CRF.txt", Consts.COLON_SPLITTER).size());
        assertEquals("length must be: 13", 13, reader.readFile("data/test_data/conf_train/config_DL.txt", Consts.COLON_SPLITTER).size());
        assertEquals("length must be: 12", 12, reader.readFile("data/test_data/conf_train/config_PA.txt", Consts.COLON_SPLITTER).size());
        assertEquals("length must be: 19", 19, reader.readFile("data/test_data/conf_train/config_Probit.txt", Consts.COLON_SPLITTER).size());
        assertEquals("length must be: 13", 12, reader.readFile("data/test_data/conf_train/config_Ramp.txt", Consts.COLON_SPLITTER).size());
        assertEquals("length must be: 14", 14, reader.readFile("data/test_data/conf_train/config_SVM.txt", Consts.COLON_SPLITTER).size());
    }
}