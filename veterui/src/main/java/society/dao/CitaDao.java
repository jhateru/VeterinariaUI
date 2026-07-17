package society.dao;

import java.util.List;
import com.google.gson.reflect.TypeToken;

import society.modell.recepcion.Cita;
import java.time.LocalDateTime;

public class CitaDao extends JsonDao<Cita> {

    public CitaDao() {
        super("citas.json", new TypeToken<List<Cita>>(){}.getType());
    }
    public void update(Cita entity) {
        List<Cita> entities = getAll();
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).getId() == entity.getId()) {
                entities.set(i, entity);
                break;
            }
        }
        saveAll(entities);
    }

    public void delete(int id) {
        List<Cita> entities = getAll();
        entities.removeIf(e -> e.getId() == id);
        saveAll(entities);
    }
}
