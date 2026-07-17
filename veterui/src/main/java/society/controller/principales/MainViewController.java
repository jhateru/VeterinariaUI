package society.controller.principales;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class MainViewController {

    private static MainViewController instance;

    @FXML
    private StackPane contentArea;
    
    // Caché para almacenar las vistas y evitar fugas de memoria
    private Map<String, Parent> viewCache = new HashMap<>();

    public MainViewController() {
        instance = this;
    }

    public static MainViewController getInstance() {
        return instance;
    }

    @FXML
    public void initialize() {
        // La vista inicial se carga desde el FXML
        javafx.application.Platform.runLater(() -> cargarVista("FacturacionView"));
    }

    public void cargarVista(String fxml) {
        try {
            // Verificar si la vista ya está en caché
            if (viewCache.containsKey(fxml)) {
                contentArea.getChildren().setAll(viewCache.get(fxml));
                return;
            }
            
            // Si no está, cargarla, cachearla y mostrarla
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/society/" + fxml + ".fxml"));
            Parent view = loader.load();
            viewCache.put(fxml, view);
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista: " + fxml);
        }
    }
}