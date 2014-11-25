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

import Data.Entities.Example;

import java.util.ArrayList;

public class InstancesContainer {

    ArrayList<Example> instances;
    ArrayList<ArrayList<String>> paths;
    int size;

    //C'tor
    public InstancesContainer() {
        instances = new ArrayList<Example>();
        paths = new ArrayList<ArrayList<String>>();
        size = 0;
    }

    //get the requested example
    public Example getInstance(int index)
    {
        try{
            return instances.get(index);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //======Setters and getters=====//
    public ArrayList<Example> getInstances() {
        return instances;
    }

    public void setInstances(ArrayList<Example> instances) {
        this.instances = instances;
        this.size = instances.size();
    }

    public ArrayList<ArrayList<String>> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<ArrayList<String>> paths) {
        this.paths = paths;
        this.size = paths.size();
    }

    public int getSize() {
        return size;
    }
}
