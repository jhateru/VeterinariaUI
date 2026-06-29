package society.dao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class CsvDao<T> implements Dao<T> {
    protected final String filePath;

    public CsvDao(String fileName) {
        this.filePath = "data" + File.separator + fileName;
        
        File file = new File(this.filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }

    @Override
    public List<T> getAll() {
        List<T> entities = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return entities;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                T entity = fromCsv(line);
                if (entity != null) {
                    entities.add(entity);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entities;
    }

    @Override
    public void save(T entity) {
        List<T> entities = getAll();
        entities.add(entity);
        saveAll(entities);
    }

    @Override
    public void saveAll(List<T> entities) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (T entity : entities) {
                bw.write(toCsv(entity));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract T fromCsv(String csvLine);
    protected abstract String toCsv(T entity);
}
