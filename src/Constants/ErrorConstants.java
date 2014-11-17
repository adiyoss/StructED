package Constants;

//this class stores all the types of errors 
public class ErrorConstants {

	public static final String GENERAL_ERROR = "Error: ";
	public static final String RAW_DATA_EMPTY_ERROR = "Raw data is empty";
	public static final String VECTOR_CONVERT_ERROR = "Error converting vector: ";
	public static final String PARSE_CLASSIFICATION_ERROR = "Error with the classification of example: ";
	public static final String PHI_VECTOR_DATA = "Phi vector data empty";
	
	public static final String MULTIPLE_VECTORS_SIZE_ERROR = "The vectors doesn't have the same size";
	public static final String MULTIPLE_VECTORS_EMPTY_ERROR = "The vectors are empty";
	
	public static final String UPDATE_ARGUMENTS_ERROR = "Size of arguments is not valid";
	public static final String UPDATE_DIVIDE_ZERO_ERROR = "Phi difference multiple return 0";
	
	public static final String PHI_DATA_ERROR = "Phi data is null";
	public static final String EPOCH_ERROR = "Epoch is a negative number";
	
	public static final String SIZE_OF_TRAIN_ERROR = "Train size is 0";
	public static final String GAP_START_SIZE_ERROR = "Gap start can not be 0";
	public static final String GAP_END_SIZE_ERROR = "Gap end can not be 0";
	public static final String ZERO_DIVIDING = "Can not divide by zero";
	
	public static final String CONFIG_ERROR = "Can not convert config file";
	public static final String CONFIG_ARGUMENTS_ERROR = "Invalid number of arguments";
    public static final String CONFIG_ARGUMENTS_TYPE_ERROR = "Invalid type of arguments";
	
	public static final String WRONG_TEST_VALUES = "Invalid test values";
	public static final String WRONG_TRAIN_VALUES = "Invalid train values";

    public static final String WEIGHTS_VALUES = "Error converting weights file.";
    public static final String SORT_ERROR = "Error, sorting the scores map.";
}
