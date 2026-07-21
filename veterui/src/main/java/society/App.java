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

    private static society.modell.administracion.Personal usuarioLogueado;
    private static Stage primaryStage;
    
    public static society.modell.administracion.Personal getUsuarioLogueado() {
        return usuarioLogueado;
    }
    
    public static void logout() {
        usuarioLogueado = null;
        javafx.application.Platform.runLater(() -> {
            if (primaryStage != null) {
                primaryStage.show();
            }
        });
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        javafx.application.Platform.setImplicitExit(false);
        
        javafx.scene.layout.VBox root = new javafx.scene.layout.VBox(15);
        root.setAlignment(javafx.geometry.Pos.CENTER);
        
        javafx.scene.control.Label title = new javafx.scene.control.Label("Iniciar Sesión");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        javafx.scene.control.TextField txtUsername = new javafx.scene.control.TextField();
        txtUsername.setPromptText("Usuario (ej. @nombre)");
        txtUsername.setMaxWidth(250);
        
        javafx.scene.control.PasswordField txtPassword = new javafx.scene.control.PasswordField();
        txtPassword.setPromptText("Contraseña");
        txtPassword.setMaxWidth(250);
        
        javafx.scene.control.ComboBox<String> cmbVersion = new javafx.scene.control.ComboBox<>();
        cmbVersion.getItems().addAll("Versión Avanzada (FXML/CSS)", "Versión Básica (Swing Puro)");
        cmbVersion.setValue("Versión Avanzada (FXML/CSS)");
        cmbVersion.setMaxWidth(250);
        
        javafx.scene.control.Label lblError = new javafx.scene.control.Label("");
        lblError.setTextFill(javafx.scene.paint.Color.RED);
        
        javafx.scene.control.Button btnIngresar = new javafx.scene.control.Button("Ingresar");
        btnIngresar.setPrefWidth(250);
        btnIngresar.setPrefHeight(40);
        btnIngresar.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white; -fx-font-weight: bold;");
        
        btnIngresar.setOnAction(e -> {
            String user = txtUsername.getText();
            String pass = txtPassword.getText();
            society.dao.PersonalDao dao = new society.dao.PersonalDao();
            society.modell.administracion.Personal loggedIn = dao.authenticate(user, pass);
            
            if (loggedIn != null) {
                usuarioLogueado = loggedIn;
                String version = cmbVersion.getValue();
                if (version.contains("Avanzada")) {
                    try {
                        scene.setRoot(loadFXML("MainView"));
                        stage.setWidth(1000);
                        stage.setHeight(700);
                        stage.centerOnScreen();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        javax.swing.JFrame frame = new javax.swing.JFrame("VeterinariaUI - Versión Básica");
                        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
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
                }
            } else {
                lblError.setText("Credenciales inválidas.");
            }
        });
        
        root.getChildren().addAll(title, txtUsername, txtPassword, cmbVersion, lblError, btnIngresar);
        scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.setTitle("VeterinariaUI - Login");
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
