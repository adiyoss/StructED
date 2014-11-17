package DataAccess;

import Data.InstancesContainer;

import java.util.ArrayList;

public interface Reader {
    public ArrayList<ArrayList<String>> readFile(String path, String spliter);
    public InstancesContainer readData(String path, String dataSpliter, String valueSpliter);
}
