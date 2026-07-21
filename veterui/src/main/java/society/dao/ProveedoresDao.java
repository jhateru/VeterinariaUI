package society.dao;

import java.util.List;
import com.google.gson.reflect.TypeToken;

import society.modell.administracion.Proveedores;

public class ProveedoresDao extends MasterJsonDao<Proveedores> {

    public ProveedoresDao() {
        super("proveedores", new TypeToken<List<Proveedores>>(){}.getType());
    }

    

    
}
