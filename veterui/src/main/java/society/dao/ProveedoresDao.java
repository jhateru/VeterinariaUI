package society.dao;

import java.util.List;
import com.google.gson.reflect.TypeToken;

import society.modell.administracion.Proveedores;

public class ProveedoresDao extends JsonDao<Proveedores> {

    public ProveedoresDao() {
        super("proveedores.json", new TypeToken<List<Proveedores>>(){}.getType());
    }

    

    
}
