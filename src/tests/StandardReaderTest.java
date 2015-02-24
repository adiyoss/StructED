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
import Constants.Paths;
import Data.Factory;
import Data.InstancesContainer;
import DataAccess.Reader;
import junit.framework.TestCase;

// Unit tests for the Standard Reader class
public class StandardReaderTest extends TestCase {

    public void testReadData() throws Exception {
        InstancesContainer instances_train;
        InstancesContainer instances_test;
        Reader reader =  Factory.getReader(0);
        instances_train = reader.readData("data/db/dummy/train.txt", Consts.SPACE, Consts.COLON_SPLITTER);
        instances_test = reader.readData("data/db/dummy/test.txt", Consts.SPACE, Consts.COLON_SPLITTER);

        assertEquals("Number of examples must be: 14",14,instances_train.getSize());
        assertEquals("Number of examples must be: 6",6,instances_test.getSize());
    }

    public void testReadFile() throws Exception {
        Reader reader =  Factory.getReader(0);
        assertEquals("length must be: 11", 11, reader.readFile("data/test_data/config_CRF.txt", Consts.COLON_SPLITTER).size());
        assertEquals("length must be: 13", 13, reader.readFile("data/test_data/config_DL.txt", Consts.COLON_SPLITTER).size());
        assertEquals("length must be: 12", 12, reader.readFile("data/test_data/config_PA.txt", Consts.COLON_SPLITTER).size());
        assertEquals("length must be: 19", 19, reader.readFile("data/test_data/config_Probit.txt", Consts.COLON_SPLITTER).size());
        assertEquals("length must be: 13", 13, reader.readFile("data/test_data/config_Ramp.txt", Consts.COLON_SPLITTER).size());
        assertEquals("length must be: 14", 14, reader.readFile("data/test_data/config_SVM.txt", Consts.COLON_SPLITTER).size());
    }
}