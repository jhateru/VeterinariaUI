package society.dao;

import java.util.List;
import com.google.gson.reflect.TypeToken;

import society.modell.configuracion.Configuracion;

public class ConfiguracionDao extends MasterJsonDao<Configuracion> {

    public ConfiguracionDao() {
        super("configuracion", new TypeToken<List<Configuracion>>(){}.getType());
    }

    

    
}
