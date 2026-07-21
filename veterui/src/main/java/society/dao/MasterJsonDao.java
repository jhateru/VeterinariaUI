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
    // Absolute path: resolves at JVM startup from the working directory (always 'veterui/')
    protected static final String MASTER_FILE_PATH;
    static {
        String base = System.getProperty("user.dir");
        // When running via 'mvn javafx:run' from veterui/, user.dir == .../veterui
        // The data file lives under src/main/java/society/data/
        String candidate = base + "/src/main/java/society/data/master.json";
        if (!new java.io.File(candidate).exists()) {
            // Fallback: maybe launched from the parent VeterinariaUI/ dir
            candidate = base + "/veterui/src/main/java/society/data/master.json";
        }
        MASTER_FILE_PATH = candidate;
        System.out.println("[MasterJsonDao] MASTER_FILE_PATH = " + MASTER_FILE_PATH);
    }
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
        File file = new File(MASTER_FILE_PATH);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        try (Writer writer = new java.io.OutputStreamWriter(
                new java.io.FileOutputStream(file), java.nio.charset.StandardCharsets.UTF_8)) {
            gson.toJson(map, writer);
        } catch (IOException e) {
            System.err.println("[MasterJsonDao] ERROR writing to " + MASTER_FILE_PATH);
            e.printStackTrace();
            // Show a user-visible Swing dialog if we are on EDT or after
            String msg = "Error al guardar datos: " + e.getMessage() + "\nRuta: " + MASTER_FILE_PATH;
            javax.swing.SwingUtilities.invokeLater(() ->
                javax.swing.JOptionPane.showMessageDialog(null, msg, "Error de Guardado",
                    javax.swing.JOptionPane.ERROR_MESSAGE));
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
            Map<String, JsonElement> map = readMasterFile();
            List<T> entities = map.containsKey(collectionKey)
                ? gson.fromJson(map.get(collectionKey), listType)
                : new ArrayList<>();
            if (entities == null) entities = new ArrayList<>();
            entities.add(entity);
            JsonElement newElem = gson.toJsonTree(entities, listType);
            map.put(collectionKey, newElem);
            writeMasterFile(map);
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

    /**
     * Generic update: replaces the element where idExtractor.apply(element) equals the given id.
     * If not found, appends as new. Thread-safe, single file-write.
     */
    public void update(T updated, java.util.function.ToIntFunction<T> idExtractor) {
        lock.lock();
        try {
            Map<String, JsonElement> map = readMasterFile();
            List<T> entities = map.containsKey(collectionKey)
                ? gson.fromJson(map.get(collectionKey), listType)
                : new ArrayList<>();
            if (entities == null) entities = new ArrayList<>();
            int targetId = idExtractor.applyAsInt(updated);
            boolean found = false;
            for (int i = 0; i < entities.size(); i++) {
                if (idExtractor.applyAsInt(entities.get(i)) == targetId) {
                    entities.set(i, updated);
                    found = true;
                    break;
                }
            }
            if (!found) {
                entities.add(updated);
            }
            JsonElement newElem = gson.toJsonTree(entities, listType);
            map.put(collectionKey, newElem);
            writeMasterFile(map);
        } finally {
            lock.unlock();
        }
    }
}
