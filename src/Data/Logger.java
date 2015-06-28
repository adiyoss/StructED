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

package Data;

import Constants.Consts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static final String directory = "log";
    private static final String log_file = "files.log";

    public static void info(String message){
        System.out.println(message);
    }
    public static void infoTime(String message){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();
        if(message.isEmpty() || message.equals(""))
            message = "None,";
        System.out.println(message+" "+dateFormat.format(date));
    }

    public static void time(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();
        System.out.println(dateFormat.format(date));
    }

    public static void timeExampleStandard(String msg, int example){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();
        System.out.println(msg+example+", "+dateFormat.format(date));
    }

    public static void progressMessage(String msg){
        System.out.print("\r" + msg + " ");
    }

    public static void error(String message){
        System.err.println("Error: "+message);
    }

    //saves the file names to different folder
    public static void log2File(String message) throws IOException
    {
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

    //clean the log file
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
