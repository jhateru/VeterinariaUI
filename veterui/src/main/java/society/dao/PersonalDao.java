package society.dao;

import java.util.List;
import com.google.gson.reflect.TypeToken;

import society.modell.administracion.Personal;

public class PersonalDao extends JsonDao<Personal> {

    public PersonalDao() {
        super("personal.json", new TypeToken<List<Personal>>(){}.getType());
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
}
