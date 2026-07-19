package society.dao;

import java.lang.reflect.Type;
import java.util.List;

public abstract class JsonDao<T> extends MasterJsonDao<T> {

    public JsonDao(String fileName, Type listType) {
        super(fileName.replace(".json", ""), listType);
    }
}
