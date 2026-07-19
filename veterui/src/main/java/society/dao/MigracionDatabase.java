package society.dao;

import com.google.gson.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MigracionDatabase {
    
    public static void main(String[] args) {
        System.out.println("Iniciando migracion a master.json...");
        
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        Map<String, JsonElement> masterMap = new HashMap<>();
        
        // 1. Migrar inventario
        File inventarioFile = new File("data/inventario.json");
        if (inventarioFile.exists()) {
            System.out.println("Leyendo inventario.json...");
            try (Reader reader = new FileReader(inventarioFile)) {
                JsonArray inventarioArray = JsonParser.parseReader(reader).getAsJsonArray();
                masterMap.put("inventario", inventarioArray);
            } catch (Exception e) {
                System.err.println("Error migrando inventario: " + e.getMessage());
            }
        }
        
        // 2. Migrar servicios
        File serviciosFile = new File("data/servicios.json");
        if (serviciosFile.exists()) {
            System.out.println("Leyendo servicios.json...");
            try (Reader reader = new FileReader(serviciosFile)) {
                JsonArray serviciosArray = JsonParser.parseReader(reader).getAsJsonArray();
                masterMap.put("servicios", serviciosArray);
            } catch (Exception e) {
                System.err.println("Error migrando servicios: " + e.getMessage());
            }
        }
        
        // Escribir master.json
        File masterFile = new File("data/master.json");
        System.out.println("Escribiendo master.json...");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(masterFile)) {
            gson.toJson(masterMap, writer);
            System.out.println("master.json creado con exito!");
            
            // Renombrar backups
            if (inventarioFile.exists()) {
                inventarioFile.renameTo(new File("data/inventario.json.backup"));
            }
            if (serviciosFile.exists()) {
                serviciosFile.renameTo(new File("data/servicios.json.backup"));
            }
            System.out.println("Archivos viejos respaldados a .backup");
        } catch (IOException e) {
            System.err.println("Error escribiendo master.json: " + e.getMessage());
        }
    }
}
