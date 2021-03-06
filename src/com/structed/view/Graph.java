/*
 * The MIT License (MIT)
 *
 * StructED - Machine Learning Package for Structured Prediction
 *
 * Copyright (c) 2015 Yossi Adi, E-Mail: yossiadidrum@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.structed.view;


import com.structed.data.Logger;
import com.structed.data.entities.Vector;
import com.structed.models.StructEDModel;
import com.xeiam.xchart.BitmapEncoder;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.SwingWrapper;
import org.tc33.jheatchart.HeatChart;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by yossiadi on 5/11/15.
 * Graph object
 */
public class Graph {

    final String TITLE = "Validation Error";
    final String Y_TITLE = "X";
    final String X_TITLE = "Y";
    final String SERIES_NAME = "y(x)";
    final String IMG_PATH = "img/validation_error";
    final String HIT_MAP_PATH = "img/heat_map.png";
    final String IMG_DIR = "img/";


    /**
     * Draw a graph of the loss values as a functions of the iterations
     * @param scores an array list of the loss values
     * @param save a flag indicates whether or not to save the img
     */
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

                File imgDir = new File(IMG_DIR);
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
                BitmapEncoder.saveBitmapWithDPI(chart, IMG_PATH, BitmapEncoder.BitmapFormat.PNG.PNG, 300);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * Draw heat map - used in the OCR tutorial
     * This function saves the heat map under: img/heat_map.png
     * @param model - the trained model
     * @param start - the index from where to start and build the heat map
     * @param jumps - the row size of the matrix
     * @throws IOException
     */
    public void drawHeatMap(StructEDModel model, int start, int jumps) throws IOException {

        Vector w = model.getWeights();
        double[][] data = new double[jumps][jumps];
        Character[] eng_chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

        for(int i = 0; i<jumps ; i++){
            for(int j = 0; j<jumps ; j++){
                data[i][j] = w.containsKey(start + i*jumps + j) ? w.get(start + i*jumps + j) : 0.0;
            }
        }
        
        HeatChart map = new HeatChart(data);

        map.setTitle("Characters Transition Parameters");
        map.setXValues(eng_chars);
        map.setYValues(eng_chars);
        map.getChartImage();

        File imgDir = new File(IMG_DIR);
        boolean result = true;
        // if the directory does not exist, create it
        if (!imgDir.exists()) {
            Logger.info("creating directory: img/");
            try {
                result = imgDir.mkdir();
            } catch (SecurityException se) {
                Logger.error("There is no permission to write the error image.");
            }
        }
        if (result)
            map.saveToFile(new File(HIT_MAP_PATH));
    }
}