package Data.Entities;

import java.util.ArrayList;

public class Example2D extends Example {

    private ArrayList<Vector> features2D;
    private ArrayList<Integer> labels2D;


    public Example2D(){
        features2D = new ArrayList<Vector>();
        labels2D = new ArrayList<Integer>();

    }

    public ArrayList<Vector> getFeatures2D() {
        return features2D;
    }

    public void setFeatures2D(ArrayList<Vector> features) {
        this.features2D = features;
    }

    public ArrayList<Integer> getLabels2D() { return labels2D; }

    public void setLabels2D(ArrayList<Integer> labels2D) { this.labels2D = labels2D; }
}
