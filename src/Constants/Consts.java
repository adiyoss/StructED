package Constants;

//this class holds all the program constants
public class Consts {
    //GERERALS
	public static final String SPACE = " ";
	public static final String CLASSIFICATION_SPLITTER = "-";
	public static final String COLON_SPLITTER = ":";
    public static final String PARAMS_SPLITER = ";";
	public static final String NEW_LINE = "line.separator";
    public static final String SLASH = "/";
    public static final int ERROR_NUMBER = -1;
    public static final int NUM_OF_IMPROVE_ITERATIONS = 5;

    //DUMMY
    public static final int MIN_GAP_START_DUMMY = 3;
    public static final int MIN_GAP_END_DUMMY = 3;


    //VOWEL DURATION
    public static final int MIN_VOWEL = 9;  //min training set: 45 frames
	public static final int MAX_VOWEL = 92; //max training set: 459 frames
    public static final int MIN_GAP_START = 10;
    public static final int MIN_GAP_END = 10;

//    public static final double MEAN_VOWEL_LENGTH = 36.7472; //pre calculated from the training set
//    public static final double MAX_VOWEL_LENGTH = 33; //pre calculated from the training set
//    public static final double STD_VOWEL_LENGTH = 11.0618; //pre calculated from the training set

    public static final double MEAN_VOWEL_LENGTH = 41.787; //pre calculated from the training set
    public static final double MAX_VOWEL_LENGTH = 38; //pre calculated from the training set
    public static final double STD_VOWEL_LENGTH = 12.918; //pre calculated from the training set
}
