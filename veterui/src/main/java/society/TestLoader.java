package society;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;

public class TestLoader extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Starting FXML validation test...");
        File dir = new File("c:/Users/user/Java/VeterinariaUI/veterui/src/main/resources/society");
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".fxml"));
            if (files != null) {
                for (File file : files) {
                    System.out.println("Testing " + file.getName() + "...");
                    try {
                        FXMLLoader loader = new FXMLLoader(file.toURI().toURL());
                        loader.load();
                        System.out.println("SUCCESS: " + file.getName());
                    } catch (Exception e) {
                        System.err.println("ERROR loading " + file.getName() + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.err.println("Directory not found.");
        }
        System.exit(0);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
