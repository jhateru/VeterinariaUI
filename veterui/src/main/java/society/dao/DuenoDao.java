package society.dao;

import java.util.List;
import com.google.gson.reflect.TypeToken;

import society.modell.recepcion.Dueno;

public class DuenoDao extends MasterJsonDao<Dueno> {

    public DuenoDao() {
        super("duenos", new TypeToken<List<Dueno>>(){}.getType());
    }

    public Dueno getById(int id) {
        List<Dueno> duenos = getAll();
        for (Dueno d : duenos) {
            if (d.getId() == id) {
                return d;
            }
        }
        return null;
    }

    
}
