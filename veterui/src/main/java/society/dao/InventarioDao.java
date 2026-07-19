package society.dao;

import java.util.List;
import com.google.gson.reflect.TypeToken;

import society.modell.inventario.Inventario;

public class InventarioDao extends MasterJsonDao<Inventario> {

    public InventarioDao() {
        super("inventario", new TypeToken<List<Inventario>>(){}.getType());
    }

    public void create(Inventario inv) {
        save(inv);
    }

    public void update(Inventario inv) {
        List<Inventario> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(inv.getId())) {
                all.set(i, inv);
                break;
            }
        }
        saveAll(all);
    }

    public void delete(String id) {
        List<Inventario> all = getAll();
        all.removeIf(i -> i.getId().equals(id));
        saveAll(all);
    }
}
