package society.dao;

import com.google.gson.*;
import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class JsonDao<T> implements Dao<T> {
    protected final String filePath;
    protected final Gson gson;
    protected final Type listType;

    public JsonDao(String fileName, Type listType) {
        this.filePath = "data/" + fileName;
        
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        
        // Add adapter for LocalDateTime
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
        
        this.gson = builder.create();
        this.listType = listType;

        File file = new File(this.filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }

    @Override
    public List<T> getAll() {
        File file = new File(filePath);
        if (!file.exists()) return new ArrayList<>();

        try (Reader reader = new FileReader(file)) {
            List<T> entities = gson.fromJson(reader, listType);
            return entities != null ? entities : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error al leer el archivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void save(T entity) {
        List<T> entities = getAll();
        entities.add(entity);
        saveAll(entities);
    }

    @Override
    public void saveAll(List<T> entities) {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(entities, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
