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

import Data.Entities.Vector;
import Helpers.MathHelpers;
import junit.framework.TestCase;

/*
    Unit tests for the MathHelpers Reader class
 */
public class MathHelpersTest extends TestCase {

    private double delta = 0.0001;

    public void testMultipleVectors() throws Exception {
        Vector v1 = new Vector();
        Vector v2 = new Vector();

        v1.put(1,1.0);
        v1.put(2,2.0);
        v1.put(3,5.5);
        v1.put(4,8.1);

        v2.put(1,5.5);
        v2.put(2,0.0);
        v2.put(3,-3.1);
        v2.put(4,7.2);

        assertEquals("v1*v2 must be 46.77", 46.77, MathHelpers.multipleVectors(v1, v2),delta);
    }

    public void testAddScalar2Vectors() throws Exception {
        Vector v1 = new Vector();
        double s_1 = 5.5;
        double s_2 = -3.1;

        v1.put(1,1.0);
        v1.put(2,2.0);
        v1.put(3,5.5);
        v1.put(4,-8.1);

        Vector v3 = MathHelpers.addScalar2Vectors(v1, s_1);
        Vector v4 = MathHelpers.addScalar2Vectors(v1, s_2);

        assertEquals("1 + 5.5 must be 5.5", 6.5, v3.get(1),delta);
        assertEquals("2 + 5.5 must be 7.5", 7.5, v3.get(2),delta);
        assertEquals("5.5 + 5.5 must be 11.0", 11.0, v3.get(3),delta);
        assertEquals("-8.1 + 5.5 must be -2.6", -2.6, v3.get(4),delta);

        assertEquals("1 + -3.1 must be -2.1", -2.1, v4.get(1),delta);
        assertEquals("2 + -3.1 must be -1.1", -1.1, v4.get(2),delta);
        assertEquals("5.5 + -3.1 must be 2.4", 2.4, v4.get(3),delta);
        assertEquals("-8.1 + -3.1 must be -11.2", -11.2, v4.get(4),delta);
    }


    public void testMulScalarWithVectors() throws Exception {
        Vector v1 = new Vector();
        double s_1 = 5.5;
        double s_2 = -3.1;

        v1.put(1,1.0);
        v1.put(2,2.0);
        v1.put(3,5.5);
        v1.put(4,-8.1);

        Vector v3 = MathHelpers.mulScalarWithVectors(v1,s_1);
        Vector v4 = MathHelpers.mulScalarWithVectors(v1,s_2);

        assertEquals("1 x 5.5 must be 5.5", 5.5, v3.get(1),delta);
        assertEquals("2 x 5.5 must be 11", 11.0, v3.get(2),delta);
        assertEquals("5.5 x 5.5 must be 30.25", 30.25, v3.get(3),delta);
        assertEquals("-8.1 x 5.5 must be -44.55", -44.55, v3.get(4),delta);

        assertEquals("1 x -3.1 must be 5.5", -3.1, v4.get(1),delta);
        assertEquals("2 x -3.1 must be 11", -6.2, v4.get(2),delta);
        assertEquals("5.5 x -3.1 must be 30.25", -17.05, v4.get(3),delta);
        assertEquals("-8.1 x -3.1 must be 25.11", 25.11, v4.get(4),delta);
    }

    public void testSubtract2Vectors() throws Exception {
        Vector v1 = new Vector();
        Vector v2 = new Vector();

        v1.put(1,1.0);
        v1.put(2,2.0);
        v1.put(3,5.5);
        v1.put(4,8.1);

        v2.put(1,5.5);
        v2.put(2,0.0);
        v2.put(3,-3.1);
        v2.put(4,7.2);

        Vector v3 = MathHelpers.subtract2Vectors(v1, v2);

        assertEquals("1.0 - 5.5 must be -4.5", -4.5, v3.get(1),delta);
        assertEquals("2.0 - 0.0 must be 2.0", 2.0, v3.get(2),delta);
        assertEquals("5.5 - -3.1 must be 8.6", 8.6, v3.get(3),delta);
        assertEquals("8.1 - 7.2 must be 0.9", 0.9, v3.get(4),delta);
    }

    public void testAdd2Vectors() throws Exception {
        Vector v1 = new Vector();
        Vector v2 = new Vector();

        v1.put(1,1.0);
        v1.put(2,2.0);
        v1.put(3,5.5);
        v1.put(4,8.1);

        v2.put(1,5.5);
        v2.put(2,0.0);
        v2.put(3,-3.1);
        v2.put(4,7.2);

        Vector v3 = MathHelpers.add2Vectors(v1, v2);

        assertEquals("1.0 + 5.5 must be 6.5", 6.5, v3.get(1),delta);
        assertEquals("2.0 + 0.0 must be 2.0", 2.0, v3.get(2),delta);
        assertEquals("5.5 + -3.1 must be 2.4", 2.4, v3.get(3),delta);
        assertEquals("8.1 + 7.2 must be 0", 15.3, v3.get(4),delta);
    }

    public void testSigmoid() throws Exception {
        assertEquals("g(1.0) = 0.731058", 0.731058, MathHelpers.sigmoid(1.0),delta);
        assertEquals("g(8.2) = 0.99972", 0.99972, MathHelpers.sigmoid(8.2),delta);
        assertEquals("g(-5.1) = 0.00605", 0.00605, MathHelpers.sigmoid(-5.1),delta);
        assertEquals("g(3.22) = 0.96158", 0.96158, MathHelpers.sigmoid(3.22),delta);
        assertEquals("g(0.002) = 0.5005", 0.5005, MathHelpers.sigmoid(0.002),delta);
        assertEquals("g(-0.123) = 0.4692", 0.4692, MathHelpers.sigmoid(-0.123),delta);
    }
}