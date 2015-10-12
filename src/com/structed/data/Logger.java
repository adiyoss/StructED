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

package com.structed.data;

import com.structed.constants.Consts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Gets all the printings and print them for the system
 */
public class Logger {

    private static final String directory = "log";
    private static final String log_file = "files.log";

    /**
     * Write the message into the standard output
     * @param message - the message to be written
     */
    public static void info(String message){
        System.out.print(System.getProperty(Consts.NEW_LINE) + message);
    }

    /**
     * Write the message into the standard output with the data and time attached
     * @param message - the message to be written
     */
    public static void infoTime(String message){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();
        if(message.isEmpty() || message.equals(""))
            message = "None,";
        System.out.print(System.getProperty(Consts.NEW_LINE) + message + " " + dateFormat.format(date));
    }

    /**
     * Writes only the data and time to the standard output
     */
    public static void time(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();
        System.out.print(System.getProperty(Consts.NEW_LINE)  + dateFormat.format(date));
    }

    /**
     * Write the message into the standard output with the data and time attached
     * @param msg - the message to be written
     * @param example - a running id
     */
    public static void timeExampleStandard(String msg, int example){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();
        System.out.print(System.getProperty(Consts.NEW_LINE) + msg+example+", "+dateFormat.format(date));
    }

    /**
     * Write the message into the standard output as a progress bar
     * @param msg - the message to be written
     */
    public static void progressMessage(String msg){
        System.out.print("\r" + msg + " ");
    }

    /**
     * Write the message into the standard error
     * @param message - the message to be written
     */
    public static void error(String message){
        System.err.println("Error: "+message);
    }



    /**
     * Write the message into the files.log in the lod directory(if doesn't exists the program creates the dir)
     * @param message - tha message to ve written
     * @throws IOException
     */
    public static void log2File(String message) throws IOException
    {
        // create the log directory if does noe exists
        File dir = new File(directory);
        if(!dir.exists())
            dir.mkdir();

        // Create file stream
        File file = new File(directory+"/"+log_file);
        FileWriter fstream = new FileWriter(file,true);
        BufferedWriter out = new BufferedWriter(fstream);
        //writes the message
        out.write(message);
        out.write(System.getProperty(Consts.NEW_LINE));
        //close all the file descriptors
        out.close();
        fstream.close();
    }

    /**
     * clean the log directory
     */
    public static void clean(){
        File dir = new File(directory);
        if(dir.exists() && dir.isDirectory()) {
            String[]entries = dir.list();
            for(String s: entries){
                File currentFile = new File(dir.getPath(),s);
                currentFile.delete();
            }
            dir.delete();
        }

        dir.mkdir();
    }
}
