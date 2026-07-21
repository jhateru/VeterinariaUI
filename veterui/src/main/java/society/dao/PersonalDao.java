package society.dao;

import java.util.List;
import com.google.gson.reflect.TypeToken;

import society.modell.administracion.Personal;

public class PersonalDao extends MasterJsonDao<Personal> {

    public PersonalDao() {
        super("personal", new TypeToken<List<Personal>>(){}.getType());
    }

    public Personal getById(int id) {
        List<Personal> entities = getAll();
        for (Personal e : entities) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    public void update(Personal entity) {
        List<Personal> entities = getAll();
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).getId() == entity.getId()) {
                entities.set(i, entity);
                break;
            }
        }
        saveAll(entities);
    }

    public void delete(int id) {
        List<Personal> entities = getAll();
        entities.removeIf(e -> e.getId() == id);
        saveAll(entities);
    }

    public Personal authenticate(String username, String password) {
        if (username == null || password == null) return null;
        List<Personal> entities = getAll();
        for (Personal p : entities) {
            if (username.equals(p.getUsername()) && password.equals(p.getPassword())) {
                return p;
            }
        }
        return null;
    }
}
