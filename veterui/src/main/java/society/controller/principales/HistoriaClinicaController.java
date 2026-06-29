package society.controller.principales;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import society.dao.HistoriaClinicaDao;
import society.dao.PacienteDao;
import society.modell.areamedica.HistoriaClinica;
import society.modell.recepcion.Paciente;

import java.util.List;
import java.util.Optional;

public class HistoriaClinicaController {

    @FXML private ListView<Paciente> pacientesListView;
    @FXML private ImageView patientImageView;
    @FXML private Label nombreLabel;
    @FXML private Label idLabel;
    @FXML private Label detallesLabel;
    @FXML private Label pesoLabel;
    @FXML private Label generoLabel;
    
    @FXML private VBox evolucionesWrapper;
    @FXML private VBox emptyStateWrapper;
    @FXML private VBox evolucionesContainer;
    
    @FXML private VBox rightColumnContent;
    @FXML private Button mainActionButton;

    @FXML private BarChart<String, Number> pesoChart;
    @FXML private VBox tratamientosContainer;
    @FXML private VBox citasContainer;

    private HistoriaClinicaDao historiaDao;
    private PacienteDao pacienteDao;
    
    private List<Paciente> pacientes;
    private Paciente pacienteActual;

    @FXML
    public void initialize() {
        historiaDao = new HistoriaClinicaDao();
        pacienteDao = new PacienteDao();
        pacientes = pacienteDao.getAll();

        cargarListaPacientes();

        pacientesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                seleccionarPaciente(newValue);
            }
        });

        if (!pacientes.isEmpty()) {
            pacientesListView.getSelectionModel().select(0);
        }
    }

    private void cargarListaPacientes() {
        pacientesListView.getItems().clear();
        pacientesListView.getItems().addAll(pacientes);
        
        pacientesListView.setCellFactory(lv -> new javafx.scene.control.ListCell<Paciente>() {
            private javafx.scene.layout.HBox root = new javafx.scene.layout.HBox(15);
            private ImageView imageView = new ImageView();
            private javafx.scene.layout.VBox dataBox = new javafx.scene.layout.VBox(5);
            
            private javafx.scene.layout.HBox topBox = new javafx.scene.layout.HBox();
            private Label nameLabel = new Label();
            private javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
            private javafx.scene.shape.Circle statusDot = new javafx.scene.shape.Circle(4);
            
            private Label breedAgeLabel = new Label();
            private Label ownerLabel = new Label();
            
            private javafx.scene.layout.HBox bottomBox = new javafx.scene.layout.HBox();
            private Label dateLabel = new Label();
            private javafx.scene.layout.Region bottomSpacer = new javafx.scene.layout.Region();
            private Label idLabel = new Label();
            
            {
                root.setPadding(new javafx.geometry.Insets(15, 10, 15, 10));
                root.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                root.setStyle("-fx-border-color: transparent transparent #eaeaea transparent; -fx-border-width: 1px;");
                
                imageView.setFitWidth(55);
                imageView.setFitHeight(55);
                
                // Add a drop shadow or rounded clip to the image container
                javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(55, 55);
                clip.setArcWidth(15);
                clip.setArcHeight(15);
                imageView.setClip(clip);
                
                javafx.scene.layout.HBox.setHgrow(topBox, javafx.scene.layout.Priority.ALWAYS);
                javafx.scene.layout.HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
                javafx.scene.layout.HBox.setHgrow(bottomSpacer, javafx.scene.layout.Priority.ALWAYS);
                
                nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #00768e;");
                breedAgeLabel.setStyle("-fx-text-fill: #6e797a; -fx-font-size: 11px;");
                ownerLabel.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 11px; -fx-font-style: italic;");
                
                dateLabel.setStyle("-fx-background-color: #f7e6d9; -fx-text-fill: #b38363; -fx-padding: 3 8; -fx-background-radius: 6; -fx-font-size: 10px;");
                idLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #00768e; -fx-font-size: 11px;");
                
                topBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                topBox.getChildren().addAll(nameLabel, spacer, statusDot);
                
                bottomBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                bottomBox.getChildren().addAll(dateLabel, bottomSpacer, idLabel);
                
                javafx.scene.layout.HBox.setHgrow(dataBox, javafx.scene.layout.Priority.ALWAYS);
                dataBox.getChildren().addAll(topBox, breedAgeLabel, ownerLabel, bottomBox);
                
                root.getChildren().addAll(imageView, dataBox);
            }
            
            @Override
            protected void updateItem(Paciente paciente, boolean empty) {
                super.updateItem(paciente, empty);
                if (empty || paciente == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    nameLabel.setText(paciente.getNombre());
                    breedAgeLabel.setText(paciente.getRaza() + " • " + paciente.getEdadAproximada());
                    ownerLabel.setText("Dueño: " + paciente.getNombreDueno());
                    
                    String ultVisita = paciente.getUltimaVisita();
                    if(ultVisita == null || ultVisita.isEmpty()) {
                        dateLabel.setText("Última: Desconocida");
                        dateLabel.setVisible(false);
                    } else {
                        dateLabel.setText("Última: " + ultVisita);
                        dateLabel.setVisible(true);
                    }
                    
                    idLabel.setText("ID: " + paciente.getId() + "-A");
                    
                    switch (paciente.getEstado()) {
                        case EN_CLINICA: statusDot.setFill(javafx.scene.paint.Color.web("#2ecc71")); break;
                        case ALTA: statusDot.setFill(javafx.scene.paint.Color.web("#e74c3c")); break;
                        default: statusDot.setFill(javafx.scene.paint.Color.web("#95a5a6")); break;
                    }
                    
                    try {
                        String imgPath = paciente.getEspecie() == Paciente.EspecieAnimal.GATO ? "/society/images/cat.png" : "/society/images/dog.png";
                        imageView.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream(imgPath)));
                    } catch (Exception e) {}
                    
                    setGraphic(root);
                }
            }
        });
    }

    private void seleccionarPaciente(Paciente paciente) {
        this.pacienteActual = paciente;
        
        // Cargar datos básicos del perfil (siempre visibles)
        nombreLabel.setText(paciente.getNombre());
        idLabel.setText("#" + paciente.getId() + "-A");
        detallesLabel.setText(paciente.getEspecie().toString() + " • " + paciente.getRaza() + " • " + paciente.getEdadAproximada());
        pesoLabel.setText(String.valueOf(paciente.getPeso()));
        generoLabel.setText(paciente.getSexo());

        // Buscar si tiene historial
        Optional<HistoriaClinica> optHistoria = historiaDao.findById(paciente.getId());
        
        if (optHistoria.isPresent()) {
            mostrarEstadoConHistorial(optHistoria.get());
        } else {
            mostrarEstadoVacio();
        }
    }

    private void mostrarEstadoVacio() {
        evolucionesWrapper.setVisible(false);
        evolucionesWrapper.setManaged(false);
        emptyStateWrapper.setVisible(true);
        emptyStateWrapper.setManaged(true);
        
        rightColumnContent.setVisible(false);
        rightColumnContent.setManaged(false);
        
        mainActionButton.setText("Registrar Historial Clínico");
    }

    private void mostrarEstadoConHistorial(HistoriaClinica hc) {
        evolucionesWrapper.setVisible(true);
        evolucionesWrapper.setManaged(true);
        emptyStateWrapper.setVisible(false);
        emptyStateWrapper.setManaged(false);
        
        rightColumnContent.setVisible(true);
        rightColumnContent.setManaged(true);
        
        mainActionButton.setText("Editar Historial Clínico");

        pesoLabel.setText(String.valueOf(hc.getPesoActual()));

        // Evoluciones
        evolucionesContainer.getChildren().clear();
        String evs = hc.getEvoluciones();
        if (evs != null && !evs.isEmpty()) {
            for (String ev : evs.split("\\|")) {
                String[] parts = ev.split(";", -1); // Use -1 to keep empty trailing strings
                if (parts.length >= 2) {
                    VBox card = new VBox(5);
                    card.getStyleClass().add("timeline-card");
                    Label title = new Label(parts[0]);
                    title.getStyleClass().add("timeline-title");
                    Label date = new Label(parts[1]);
                    date.getStyleClass().add("timeline-date");
                    
                    VBox detailsBox = new VBox(2);
                    
                    if (parts.length > 2 && !parts[2].isEmpty()) {
                        Label descS = new Label("Subjetivas: " + parts[2]);
                        descS.getStyleClass().add("timeline-desc");
                        descS.setWrapText(true);
                        detailsBox.getChildren().add(descS);
                    }
                    if (parts.length > 3 && !parts[3].isEmpty()) {
                        Label descO = new Label("Objetivas: " + parts[3]);
                        descO.getStyleClass().add("timeline-desc");
                        descO.setWrapText(true);
                        detailsBox.getChildren().add(descO);
                    }
                    if (parts.length > 4 && !parts[4].isEmpty()) {
                        Label diag = new Label("Diagnóstico: " + parts[4]);
                        diag.setStyle("-fx-font-weight: bold; -fx-text-fill: #e74c3c; -fx-font-size: 11px;");
                        diag.setWrapText(true);
                        detailsBox.getChildren().add(diag);
                    }
                    
                    card.getChildren().addAll(title, date, detailsBox);
                    evolucionesContainer.getChildren().add(card);
                }
            }
        }

        // Tratamientos
        tratamientosContainer.getChildren().clear();
        String trats = hc.getTratamientos();
        if (trats != null && !trats.isEmpty() && !trats.equals("Ninguno;N/A")) {
            for (String t : trats.split("\\|")) {
                String[] parts = t.split(";", -1);
                if (parts.length >= 2 && (!parts[0].isEmpty() || !parts[1].isEmpty())) {
                    VBox card = new VBox(5);
                    card.getStyleClass().add("treatment-card");
                    Label title = new Label(parts[0]);
                    title.getStyleClass().add("treatment-name");
                    Label desc = new Label(parts[1]);
                    desc.getStyleClass().add("treatment-desc");
                    
                    card.getChildren().addAll(title, desc);
                    
                    if (parts.length > 2 && !parts[2].isEmpty()) {
                        Label rec = new Label("Rec.: " + parts[2]);
                        rec.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 10px; -fx-font-style: italic;");
                        rec.setWrapText(true);
                        card.getChildren().add(rec);
                    }
                    
                    tratamientosContainer.getChildren().add(card);
                }
            }
        }

        // Gráfico de Peso
        pesoChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("MAY", hc.getPesoActual() - 2.0));
        series.getData().add(new XYChart.Data<>("JUN", hc.getPesoActual() - 1.5));
        series.getData().add(new XYChart.Data<>("JUL", hc.getPesoActual() - 1.0));
        series.getData().add(new XYChart.Data<>("AGO", hc.getPesoActual() - 0.5));
        series.getData().add(new XYChart.Data<>("SEP", hc.getPesoActual() - 0.2));
        series.getData().add(new XYChart.Data<>("OCT", hc.getPesoActual()));
        pesoChart.getData().add(series);

        // Citas Dummy
        citasContainer.getChildren().clear();
        VBox citaCard = new VBox(2);
        citaCard.getStyleClass().add("treatment-card");
        Label citaTitle = new Label("Control General");
        citaTitle.getStyleClass().add("treatment-name");
        Label citaDesc = new Label("Próxima visita");
        citaDesc.getStyleClass().add("treatment-desc");
        citaCard.getChildren().addAll(citaTitle, citaDesc);
        citasContainer.getChildren().add(citaCard);
    }

    @FXML
    private void handleMainAction() {
        if (pacienteActual == null) {
            System.out.println("No hay paciente seleccionado.");
            return;
        }
        
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/society/RegistroHistoriaModal.fxml"));
            javafx.scene.Parent root = loader.load();
            
            RegistroHistoriaController controller = loader.getController();
            
            // Check if history exists
            java.util.Optional<society.modell.areamedica.HistoriaClinica> opt = historiaDao.findById(pacienteActual.getId());
            boolean isEditMode = opt.isPresent();
            
            controller.setDatos(pacienteActual, historiaDao, isEditMode);
            
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle(isEditMode ? "Editar Historial Clínico" : "Registrar Historial Clínico");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            
            javafx.scene.Scene scene = new javafx.scene.Scene(root, 900, 750);
            // Optionally, add stylesheet
            // scene.getStylesheets().add(getClass().getResource("/society/Styles.css").toExternalForm());
            stage.setScene(scene);
            
            stage.showAndWait();
            
            // Refrescar UI al cerrar el modal
            seleccionarPaciente(pacienteActual);
            
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
