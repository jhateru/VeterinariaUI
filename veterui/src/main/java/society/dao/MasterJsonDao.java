package society.dao;

import com.google.gson.*;
import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public abstract class MasterJsonDao<T> implements Dao<T> {
    protected static final String MASTER_FILE_PATH = "data/master.json";
    protected static final ReentrantLock lock = new ReentrantLock();
    protected static final Gson gson;
    
    protected final String collectionKey;
    protected final Type listType;

    static {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        builder.registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
        });
        builder.registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }
        });
        builder.registerTypeAdapter(java.time.LocalDate.class, new JsonSerializer<java.time.LocalDate>() {
            @Override
            public JsonElement serialize(java.time.LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
        });
        builder.registerTypeAdapter(java.time.LocalDate.class, new JsonDeserializer<java.time.LocalDate>() {
            @Override
            public java.time.LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return java.time.LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
            }
        });
        gson = builder.create();
    }

    public MasterJsonDao(String collectionKey, Type listType) {
        this.collectionKey = collectionKey;
        this.listType = listType;
        
        File file = new File(MASTER_FILE_PATH);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }
    
    private Map<String, JsonElement> readMasterFile() {
        File file = new File(MASTER_FILE_PATH);
        if (!file.exists()) return new HashMap<>();

        try (Reader reader = new FileReader(file)) {
            Type mapType = new com.google.gson.reflect.TypeToken<Map<String, JsonElement>>(){}.getType();
            Map<String, JsonElement> map = gson.fromJson(reader, mapType);
            return map != null ? map : new HashMap<>();
        } catch (Exception e) {
            System.err.println("Error reading master.json: " + e.getMessage());
            return new HashMap<>();
        }
    }
    
    private void writeMasterFile(Map<String, JsonElement> map) {
        try (Writer writer = new FileWriter(MASTER_FILE_PATH)) {
            gson.toJson(map, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<T> getAll() {
        lock.lock();
        try {
            Map<String, JsonElement> map = readMasterFile();
            if (map.containsKey(collectionKey)) {
                JsonElement collectionElement = map.get(collectionKey);
                List<T> entities = gson.fromJson(collectionElement, listType);
                return entities != null ? entities : new ArrayList<>();
            }
            return new ArrayList<>();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void save(T entity) {
        lock.lock();
        try {
            List<T> entities = getAll();
            entities.add(entity);
            saveAll(entities);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void saveAll(List<T> entities) {
        lock.lock();
        try {
            Map<String, JsonElement> map = readMasterFile();
            JsonElement newCollectionElement = gson.toJsonTree(entities, listType);
            map.put(collectionKey, newCollectionElement);
            writeMasterFile(map);
        } finally {
            lock.unlock();
        }
    }
}
