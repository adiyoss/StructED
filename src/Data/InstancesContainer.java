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
