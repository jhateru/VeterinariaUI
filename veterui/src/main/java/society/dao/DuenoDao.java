package society.dao;

import java.util.List;
import com.google.gson.reflect.TypeToken;

import society.modell.recepcion.Dueno;

public class DuenoDao extends JsonDao<Dueno> {

    public DuenoDao() {
        super("duenos.json", new TypeToken<List<Dueno>>(){}.getType());
    }

    

    
}
