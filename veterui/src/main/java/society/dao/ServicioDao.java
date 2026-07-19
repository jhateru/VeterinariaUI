package society.dao;

import java.util.List;
import com.google.gson.reflect.TypeToken;

import society.modell.administracion.Servicio;

public class ServicioDao extends MasterJsonDao<Servicio> {
    
    public ServicioDao() {
        super("servicios", new TypeToken<List<Servicio>>(){}.getType());
    }

    public void create(Servicio s) {
        save(s);
    }

    public void update(Servicio s) {
        List<Servicio> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == s.getId()) {
                all.set(i, s);
                break;
            }
        }
        saveAll(all);
    }

    public void delete(int id) {
        List<Servicio> all = getAll();
        all.removeIf(s -> s.getId() == id);
        saveAll(all);
    }
}
