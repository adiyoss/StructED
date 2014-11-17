package Data.Entities;

import java.util.ArrayList;

//this class will store all the possible class offsets and the real small_offset
public abstract class Example {

    public String path;
    private String label;
    public int sizeOfVector;

	//C'tor
	public Example() {
        label = "";
        sizeOfVector = 0;
	}

	//===========GETTERS AND SETTERS=========//
	public Vector getFeatures(){return null;}
	public void setFeatures(Vector features){}

    public ArrayList<Vector> getFeatures2D(){return null;}
    public void setFeatures2D(ArrayList<Vector> features){}
    public ArrayList<Integer> getLabels2D(){return null;}
    public void setLabels2D(ArrayList<Integer> rankLabels){}

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
}
