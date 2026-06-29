package society.controller.principales;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import society.dao.LaboratorioDao;
import society.modell.areamedica.Laboratorio;
import society.modell.recepcion.Paciente;

import java.util.List;
import java.util.stream.Collectors;

public class LaboratorioController {

    @FXML private Label muestrasPendientesLabel;
    @FXML private Label muestrasUrgentesLabel;
    @FXML private Label resultadosListosLabel;
    @FXML private Label resultadosHoyLabel;
    @FXML private Label equiposActivosLabel;
    @FXML private ProgressBar equiposProgress;

    @FXML private ListView<Laboratorio> solicitudesListView;
    @FXML private ListView<Laboratorio> resultadosListView;

    private LaboratorioDao laboratorioDao;
    private ObservableList<Laboratorio> allLabs;

    @FXML
    public void initialize() {
        laboratorioDao = new LaboratorioDao();
        allLabs = FXCollections.observableArrayList();

        setupSolicitudesCellFactory();
        setupResultadosCellFactory();

        loadData();
    }

    private void loadData() {
        List<Laboratorio> labs = laboratorioDao.getAll();
        allLabs.setAll(labs);

        // Populate left list (Not ready)
        List<Laboratorio> solicitudes = labs.stream()
                .filter(l -> !"Resultado Listo".equalsIgnoreCase(l.getEstado()))
                .collect(Collectors.toList());
        solicitudesListView.setItems(FXCollections.observableArrayList(solicitudes));

        // Populate right list (Ready / Recent)
        List<Laboratorio> resultados = labs.stream()
                .filter(l -> "Resultado Listo".equalsIgnoreCase(l.getEstado()))
                .collect(Collectors.toList());
        resultadosListView.setItems(FXCollections.observableArrayList(resultados));

        // Update metrics
        int pendientes = solicitudes.size();
        long urgentes = solicitudes.stream().filter(l -> "URGENTE".equalsIgnoreCase(l.getPrioridad())).count();
        muestrasPendientesLabel.setText(String.valueOf(pendientes));
        muestrasUrgentesLabel.setText("! " + urgentes + " urgentes");

        int listos = resultados.size();
        resultadosListosLabel.setText(String.valueOf(listos));
        // Hardcoded "+5 hoy" simulation
        resultadosHoyLabel.setText("~ +5 hoy");

        // Hardcoded equipos activos
        equiposActivosLabel.setText("6/7");
        equiposProgress.setProgress(6.0 / 7.0);
    }

    private void setupSolicitudesCellFactory() {
        solicitudesListView.setCellFactory(param -> new ListCell<Laboratorio>() {
            @Override
            protected void updateItem(Laboratorio item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getPaciente() == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    Paciente p = item.getPaciente();
                    
                    HBox root = new HBox(10);
                    root.setStyle("-fx-padding: 10; -fx-border-color: transparent transparent #eee transparent; -fx-background-color: white;");
                    
                    // Mascota / Dueño Column
                    HBox patientCol = new HBox(10);
                    patientCol.setPrefWidth(120);
                    patientCol.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                    
                    Label initialBadge = new Label(p.getNombre().substring(0, 1).toUpperCase());
                    initialBadge.setStyle("-fx-background-color: #fce4ec; -fx-text-fill: #333; -fx-font-weight: bold; -fx-padding: 10 15; -fx-background-radius: 20;");
                    
                    VBox pDetails = new VBox(2);
                    Label pName = new Label(p.getNombre());
                    pName.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
                    Label pOwner = new Label(p.getNombreDueno());
                    pOwner.setStyle("-fx-font-size: 10px; -fx-text-fill: #888;");
                    pDetails.getChildren().addAll(pName, pOwner);
                    patientCol.getChildren().addAll(initialBadge, pDetails);

                    // Tipo Test Column
                    VBox testCol = new VBox(2);
                    testCol.setPrefWidth(120);
                    Label tLabel = new Label(item.getTipoExamen());
                    tLabel.setStyle("-fx-text-fill: #333;");
                    Label dLabel = new Label(item.getDoctorAsignado());
                    dLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #888;");
                    testCol.getChildren().addAll(tLabel, dLabel);

                    // Prioridad Column
                    HBox prioCol = new HBox();
                    prioCol.setPrefWidth(100);
                    prioCol.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                    Label prioLabel = new Label(item.getPrioridad());
                    if ("URGENTE".equalsIgnoreCase(item.getPrioridad())) {
                        prioLabel.setStyle("-fx-background-color: #fdebd0; -fx-text-fill: #c0392b; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 4 8; -fx-background-radius: 4;");
                        prioLabel.setText("⚡ " + item.getPrioridad());
                    } else {
                        prioLabel.setStyle("-fx-background-color: #eee; -fx-text-fill: #666; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 4 8; -fx-background-radius: 4;");
                    }
                    prioCol.getChildren().add(prioLabel);

                    // Estado Column
                    HBox statusCol = new HBox();
                    statusCol.setPrefWidth(120);
                    statusCol.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                    Label sLabel = new Label(item.getEstado());
                    sLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #005a5a;");
                    // Add dot logic here if necessary
                    statusCol.getChildren().add(sLabel);

                    root.getChildren().addAll(patientCol, testCol, prioCol, statusCol);
                    setGraphic(root);
                }
            }
        });
    }

    private void setupResultadosCellFactory() {
        resultadosListView.setCellFactory(param -> new ListCell<Laboratorio>() {
            @Override
            protected void updateItem(Laboratorio item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getPaciente() == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    VBox root = new VBox(8);
                    root.setStyle("-fx-padding: 15; -fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #e0ebeb; -fx-border-radius: 8; -fx-margin: 0 0 10 0;");
                    
                    HBox header = new HBox(10);
                    header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                    Label title = new Label(item.getPaciente().getNombre() + " - " + item.getTipoExamen());
                    title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #333;");
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
                    Label docIcon = new Label("📄"); // Simple doc icon
                    header.getChildren().addAll(title, spacer, docIcon);

                    Label timeLabel = new Label("Finalizado " + (item.getTiempoFinalizadoStr() != null ? item.getTiempoFinalizadoStr() : "recientemente"));
                    timeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #888;");

                    Label notes = new Label("\"" + (item.getResultadosCortos() != null ? item.getResultadosCortos() : "Sin notas.") + "\"");
                    notes.setWrapText(true);
                    notes.setStyle("-fx-font-size: 11px; -fx-text-fill: #666; -fx-font-style: italic;");

                    VBox badgeBox = new VBox(5);
                    if (item.isRequiereAtencion()) {
                        HBox alertsBox = new HBox(5);
                        alertsBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                        Label pBadge = new Label("POSITIVO");
                        pBadge.setStyle("-fx-background-color: #ffcccc; -fx-text-fill: #c0392b; -fx-padding: 2 6; -fx-font-size: 9px; -fx-font-weight: bold; -fx-background-radius: 4;");
                        Label pText = new Label("Requiere atención");
                        pText.setStyle("-fx-font-size: 11px; -fx-text-fill: #888;");
                        alertsBox.getChildren().addAll(pBadge, pText);
                        docIcon.setText("⚠️");
                        docIcon.setStyle("-fx-text-fill: #c0392b; -fx-font-size: 16px;");
                        badgeBox.getChildren().add(alertsBox);
                    }

                    Button btn = new Button("Ver Reporte Completo");
                    btn.setMaxWidth(Double.MAX_VALUE);
                    btn.setStyle("-fx-background-color: #f5f5f5; -fx-text-fill: #333; -fx-font-size: 11px; -fx-padding: 8; -fx-background-radius: 4; -fx-cursor: hand;");

                    root.getChildren().addAll(header, timeLabel, badgeBox, notes, btn);
                    setGraphic(root);
                }
            }
        });
    }

    @FXML
    private void handleNuevaSolicitud() {
        System.out.println("Nueva solicitud clickeada. TODO: Abrir modal.");
    }
}
