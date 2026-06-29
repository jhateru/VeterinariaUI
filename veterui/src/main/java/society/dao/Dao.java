package society.dao;

import java.util.List;

public interface Dao<T> {
    List<T> getAll();
    void save(T entity);
    void saveAll(List<T> entities);
}
