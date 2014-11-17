package DataAccess;

import java.util.ArrayList;
import java.util.Map;

public interface Writer {
    public void writeData2File(String path, ArrayList<String> data, boolean isNewLine);
    public void writeHashMap2File(String path, Map<Integer, Double> data);
    public void writeScoresFile(String exampleName ,String path, Map<String, Double> data, int maxElements2Display);
    public void clearPrevResult(String path);
}
