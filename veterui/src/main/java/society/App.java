package society;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static final String FXML_ROOT = "/society/";

    private static Scene scene;

    @Override
    public void start(Stage stage) {
        javafx.scene.layout.VBox root = new javafx.scene.layout.VBox(20);
        root.setAlignment(javafx.geometry.Pos.CENTER);
        
        javafx.scene.control.Label title = new javafx.scene.control.Label("Seleccione la versión a ejecutar");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        javafx.scene.control.Button btnAvanzada = new javafx.scene.control.Button("Versión Avanzada (FXML/CSS)");
        btnAvanzada.setPrefWidth(250);
        btnAvanzada.setPrefHeight(50);
        btnAvanzada.setOnAction(e -> {
            try {
                scene.setRoot(loadFXML("MainView"));
                stage.setWidth(1000);
                stage.setHeight(700);
                stage.centerOnScreen();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
        javafx.scene.control.Button btnBasica = new javafx.scene.control.Button("Versión Básica (Swing Puro)");
        btnBasica.setPrefWidth(250);
        btnBasica.setPrefHeight(50);
        btnBasica.setOnAction(e -> {
            javax.swing.SwingUtilities.invokeLater(() -> {
                javax.swing.JFrame frame = new javax.swing.JFrame("VeterinariaUI - Versión Básica");
                frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                
                // Asume que MainViewBasic es un JPanel. Si es JFrame, adaptar.
                java.awt.Component view = new society.view.MainViewBasic();
                if (view instanceof javax.swing.JFrame) {
                    ((javax.swing.JFrame)view).setVisible(true);
                } else {
                    frame.setContentPane((java.awt.Container) view);
                    frame.setSize(1000, 700);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                }
            });
            stage.close();
        });
        
        root.getChildren().addAll(title, btnAvanzada, btnBasica);
        scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.setTitle("VeterinariaUI - Launcher");
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    
    public static void setRootNode(Parent node) {
        scene.setRoot(node);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        var resource = App.class.getResource(FXML_ROOT + fxml + ".fxml");
        if (resource == null) {
            throw new IOException(
                "No se encontro el archivo FXML: " + FXML_ROOT + fxml + ".fxml"
            );
        }

        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
