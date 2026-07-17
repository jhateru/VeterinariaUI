package society.dao;

import java.util.List;
import com.google.gson.reflect.TypeToken;

import society.modell.configuracion.Configuracion;

public class ConfiguracionDao extends JsonDao<Configuracion> {

    public ConfiguracionDao() {
        super("configuracion.json", new TypeToken<List<Configuracion>>(){}.getType());
    }

    

    
}
