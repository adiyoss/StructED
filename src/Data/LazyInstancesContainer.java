package Data;

import Data.Entities.Example;
import DataAccess.VowelDurationReader;

import java.io.File;

public class LazyInstancesContainer extends InstancesContainer{

    VowelDurationReader reader = new VowelDurationReader();

    //C'tor
    public LazyInstancesContainer(){
        super();
    }

    @Override
    //get the requested example
    public Example getInstance(int index) {
        try {
            File file = new File(paths.get(index).get(0));
            if(file.exists()) {
                Example example = reader.readExample(paths.get(index));
                CacheVowelData.updateCache(example);
                example.path = paths.get(index).get(0);
                return example;
            }
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
