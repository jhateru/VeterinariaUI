package society.controller.principales;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import society.controller.reutilizables.RegistroHospitalizacionController;
import society.dao.HospitalizacionDao;
import society.modell.areamedica.Hospitalizacion;
import society.modell.recepcion.Paciente;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class HospitalizacionController {

    @FXML private Label occupancyLabel;
    @FXML private ProgressBar occupancyProgress;
    @FXML private Label criticalLabel;
    @FXML private Label dischargesLabel;
    @FXML private Label staffLabel;

    @FXML private BarChart<String, Number> occupancyChart;
    @FXML private ListView<Hospitalizacion> admissionsListView;
    @FXML private Label showingLabel;

    private HospitalizacionDao hospitalizacionDao;
    private ObservableList<Hospitalizacion> hospitalizacionList;

    @FXML
    public void initialize() {
        hospitalizacionDao = new HospitalizacionDao();
        hospitalizacionList = FXCollections.observableArrayList();

        // Setup ListView Cell Factory
        admissionsListView.setCellFactory(param -> new ListCell<Hospitalizacion>() {
            @Override
            protected void updateItem(Hospitalizacion item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getPaciente() == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Paciente p = item.getPaciente();
                    
                    HBox root = new HBox(10);
                    root.setStyle("-fx-padding: 10 5; -fx-border-color: transparent transparent #eee transparent;");
                    
                    // Patient Column
                    HBox patientCol = new HBox(10);
                    patientCol.setPrefWidth(150);
                    ImageView imgView = new ImageView();
                    imgView.setFitWidth(40);
                    imgView.setFitHeight(40);
                    String imgPath = p.getEspecie() == Paciente.EspecieAnimal.GATO ? "/society/images/cat.png" : "/society/images/dog.png";
                    try {
                        imgView.setImage(new Image(getClass().getResourceAsStream(imgPath)));
                    } catch (Exception e) {}
                    
                    VBox pDetails = new VBox(2);
                    Label pName = new Label(p.getNombre());
                    pName.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
                    Label pInfo = new Label(p.getRaza() + " • " + p.getNombreDueno());
                    pInfo.setStyle("-fx-font-size: 10px; -fx-text-fill: #888;");
                    pInfo.setWrapText(true);
                    pDetails.getChildren().addAll(pName, pInfo);
                    patientCol.getChildren().addAll(imgView, pDetails);
                    
                    // Reason / Cage Column
                    VBox reasonCol = new VBox(2);
                    reasonCol.setPrefWidth(150);
                    Label rLabel = new Label(item.getMotivo() != null ? item.getMotivo() : "Observation");
                    rLabel.setStyle("-fx-text-fill: #333;");
                    Label cLabel = new Label(item.getJaulaAsignada() != null ? item.getJaulaAsignada() : "CAGE");
                    cLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #005a5a; -fx-font-weight: bold;");
                    reasonCol.getChildren().addAll(rLabel, cLabel);

                    // Date Column
                    VBox dateCol = new VBox(2);
                    dateCol.setPrefWidth(120);
                    String dateStr = item.getFechaIngreso() != null ? item.getFechaIngreso().format(DateTimeFormatter.ofPattern("MMM dd, HH:mm")) : "";
                    Label dLabel = new Label(dateStr);
                    dLabel.setStyle("-fx-text-fill: #333;");
                    Label dSub = new Label("Admitted"); // could calculate days
                    dSub.setStyle("-fx-font-size: 10px; -fx-text-fill: #888;");
                    dateCol.getChildren().addAll(dLabel, dSub);

                    // Status Column
                    HBox statusCol = new HBox();
                    statusCol.setPrefWidth(100);
                    Label sLabel = new Label(item.getEstadoMedicoInicial() != null ? item.getEstadoMedicoInicial() : "Stable");
                    String sColor = "#3498db"; // Default blue
                    String sBg = "#e3f2fd";
                    if ("CRÍTICO".equalsIgnoreCase(item.getEstadoMedicoInicial())) { sColor = "#c0392b"; sBg = "#ffcccc"; }
                    else if ("RESERVADO".equalsIgnoreCase(item.getEstadoMedicoInicial())) { sColor = "#f39c12"; sBg = "#fdebd0"; }
                    sLabel.setStyle("-fx-background-color: " + sBg + "; -fx-text-fill: " + sColor + "; -fx-padding: 4 10; -fx-background-radius: 12; -fx-font-size: 11px; -fx-font-weight: bold;");
                    statusCol.getChildren().add(sLabel);

                    // Actions
                    HBox actionsCol = new HBox();
                    actionsCol.setPrefWidth(50);
                    actionsCol.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
                    Label actionBtn = new Label("⋮");
                    actionBtn.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #888; -fx-cursor: hand;");
                    actionsCol.getChildren().add(actionBtn);

                    root.getChildren().addAll(patientCol, reasonCol, dateCol, statusCol, actionsCol);
                    setGraphic(root);
                }
            }
        });

        loadData();
        setupChart();
    }

    private void loadData() {
        List<Hospitalizacion> all = hospitalizacionDao.findAll();
        hospitalizacionList.setAll(all);
        admissionsListView.setItems(hospitalizacionList);

        // Update metrics
        int totalCages = 20;
        int occupied = all.size();
        occupancyLabel.setText(occupied + " / " + totalCages);
        occupancyProgress.setProgress((double) occupied / totalCages);

        long criticalCount = all.stream().filter(h -> "CRÍTICO".equalsIgnoreCase(h.getEstadoMedicoInicial())).count();
        criticalLabel.setText(String.format("%02d", criticalCount));
        
        showingLabel.setText("Showing " + occupied + " of " + occupied + " current patients");
    }

    private void setupChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Mon", 4));
        series.getData().add(new XYChart.Data<>("Tue", 6));
        series.getData().add(new XYChart.Data<>("Wed", 8));
        series.getData().add(new XYChart.Data<>("Thu", 10));
        series.getData().add(new XYChart.Data<>("Fri", 12));
        series.getData().add(new XYChart.Data<>("Today", hospitalizacionList.size()));
        occupancyChart.getData().add(series);
        // Style bars
        for (XYChart.Data<String, Number> data : series.getData()) {
            data.getNode().setStyle("-fx-bar-fill: #8db5b5;"); // light teal
        }
        if (!series.getData().isEmpty()) {
            series.getData().get(series.getData().size()-1).getNode().setStyle("-fx-bar-fill: #005a5a;"); // dark teal for today
        }
    }

    @FXML
    private void handleNewAdmission(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/society/RegistroHospitalizacionModal.fxml"));
            Parent root = loader.load();

            RegistroHospitalizacionController controller = loader.getController();
            controller.setOnGuardadoExitoso(() -> {
                loadData(); // Refresh table when saved
            });

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Nueva Hospitalización");
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al abrir el formulario: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
