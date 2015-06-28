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

package view;

/**
 * Created by yossiadi on 5/11/15.
 */

import Data.Logger;
import com.xeiam.xchart.BitmapEncoder;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.SwingWrapper;

import java.io.File;
import java.util.ArrayList;

public class Graph {

    final String TITLE = "Validation Error";
    final String Y_TITLE = "X";
    final String X_TITLE = "Y";
    final String SERIES_NAME = "y(x)";
    final String IMG_PATH = "img/validation_error";

    public void drawGraph(ArrayList<Double> scores, boolean save) {
        try {
            // setting the data fit for the plotting
            double[] xData = new double[scores.size()];
            double[] yData = new double[scores.size()];

            for(int i=0 ; i<scores.size() ; i++){
                yData[i] = scores.get(i);
                xData[i] = i+1;
            }

            // Create Chart
            Chart chart = QuickChart.getChart(TITLE, X_TITLE, Y_TITLE, SERIES_NAME, xData, yData);
            // Show it
            new SwingWrapper(chart).displayChart();
            if (save) {
                // or save it in high-res
                Logger.info("=========================");
                Logger.info("Saving image: " + IMG_PATH);
                Logger.info("=========================");

                File imgDir = new File("img/");
                // if the directory does not exist, create it
                if (!imgDir.exists()) {
                    Logger.info("creating directory: img/");
                    boolean result = false;
                    try{
                        result = imgDir.mkdir();
                    }
                    catch(SecurityException se){
                        Logger.error("There is no permission to write the error image.");
                    }
                    if(result) {
                        Logger.info("DIR created");
                        BitmapEncoder.saveBitmapWithDPI(chart, IMG_PATH, BitmapEncoder.BitmapFormat.PNG.PNG, 300);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}