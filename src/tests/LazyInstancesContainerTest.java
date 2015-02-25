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
import Data.Entities.Example2D;
import Data.Factory;
import Data.InstancesContainer;
import Data.LazyInstancesContainer;
import DataAccess.Reader;
import junit.framework.TestCase;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

/*
    Unit test for the LazyInstanceContainer class
 */
public class LazyInstancesContainerTest extends TestCase {

    public void testGetInstance() throws Exception {
        Reader reader =  Factory.getReader(2);
        InstancesContainer instances_train;
        instances_train = reader.readData("data/test_data/db/train_vowel.txt", Consts.SPACE, Consts.COLON_SPLITTER);
        assertThat(instances_train, instanceOf(LazyInstancesContainer.class));
        assertEquals("Must return null because there is no such files",instances_train.getInstance(0), null);

        InstancesContainer instances_test;
        instances_test = reader.readData("data/test_data/db/test_vowel.txt", Consts.SPACE, Consts.COLON_SPLITTER);
        assertThat(instances_test, instanceOf(LazyInstancesContainer.class));
        assertThat(instances_test.getInstance(0), instanceOf(Example2D.class));
    }
}