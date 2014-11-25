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

package DataAccess;

import Constants.Consts;
import Constants.ErrorConstants;
import Data.Logger;
import Helpers.Comperators.MapKeyComparatorAscending;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class RankWriter implements Writer {

    public void writeHashMap2File(String path, Map<Integer, Double> data) {
        try {
            // Create file
            FileWriter fstream = new FileWriter(path);
            BufferedWriter out = new BufferedWriter(fstream);

            //write the data
            for (Integer key : data.keySet())
                out.write(key + Consts.COLON_SPLITTER + data.get(key) + Consts.SPACE);
            out.write(System.getProperty(Consts.NEW_LINE));

            //Close the output stream
            out.close();
        } catch (Exception e) {//Catch exception if any
            Logger.error(ErrorConstants.GENERAL_ERROR);
            e.printStackTrace();
        }
    }

    public void writeScoresFile(String exampleName, String path, Map<String, Double> data, int maxElements2Display) {
        try {
            MapKeyComparatorAscending vc = new MapKeyComparatorAscending(data);
            TreeMap<String, Double> result = new TreeMap<String, Double>(vc);
            result.putAll(data);

            FileWriter fstream = new FileWriter(path, true);
            BufferedWriter out = new BufferedWriter(fstream);

            out.write(exampleName+Consts.SPACE);
            int iteration = 0;
            //write the data
            for (Map.Entry entry : result.entrySet()) {
                iteration++;
                if (iteration > maxElements2Display)
                    break;
                out.write(entry.getValue() + System.getProperty(Consts.NEW_LINE));
            }

            //Close the output stream
            out.close();
            fstream.close();
        } catch (Exception e) {//Catch exception if any
            Logger.error(ErrorConstants.GENERAL_ERROR);
            e.printStackTrace();
        }
    }

    public void writeData2File(String path, ArrayList<String> data, boolean isNewLine) {
        try {
            // Create file
            FileWriter fstream = new FileWriter(path, true);
            BufferedWriter out = new BufferedWriter(fstream);

            //write the data
            for (int i = 0; i < data.size() - 1; i++) {
                if (!isNewLine)
                    out.write(data.get(i) + Consts.SPACE);
                else
                    out.write(data.get(i) + System.getProperty(Consts.NEW_LINE));
            }

            out.write(data.get(data.size() - 1));
            out.write(System.getProperty(Consts.NEW_LINE));

            //Close the output stream
            out.close();
        } catch (Exception e) {//Catch exception if any
            Logger.error(ErrorConstants.GENERAL_ERROR);
            e.printStackTrace();
        }
    }

    public void clearPrevResult(String path) {
        File file = new File(path);
        if(file.exists() && !file.isDirectory())
            file.delete();
    }
}
