package Data.Entities;

public class ExampleStandard extends Example {

    private Vector features;

    public ExampleStandard(){
        features = new Vector();
    }

    //Current functions
    public Vector getFeatures() {
        return features;
    }
    public void setFeatures(Vector features) {
        this.features = features;
    }


}
