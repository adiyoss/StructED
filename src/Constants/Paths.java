package Constants;

public class Paths {
    //SINGLETON
    private static Paths instance = new Paths();
    public static Paths getInstance(){
        return instance;
    }
    private Paths(){}

    //Train Path
    public String TRAIN_PATH = "";
    public String VALIDATION_PATH = "";

    //Weights
	public String OUTPUT_WEIGHTS_PATH = "";
    public String INIT_WEIGHTS_PATH = "";

    //Config Files
	public String CONFIG_PATH_TRAIN = "";
    public String CONFIG_PATH_TEST = "";
}

