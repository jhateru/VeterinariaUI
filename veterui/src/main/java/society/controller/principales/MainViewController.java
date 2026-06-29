package society.controller.principales;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class MainViewController {

    private static MainViewController instance;

    @FXML
    private StackPane contentArea;

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/society/" + fxml + ".fxml"));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista: " + fxml);
        }
    }
}