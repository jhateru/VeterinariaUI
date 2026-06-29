package society.controller.principales;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.geometry.Pos;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class RecepcionController {

    @FXML private Label fechaLabel;
    @FXML private Label citasHoyValue;
    @FXML private Label esperaValue;
    @FXML private Label atendidosValue;
    
    @FXML private Label caninosLabel;
    @FXML private Label felinosLabel;
    
    @FXML private VBox salaEsperaContainer;
    @FXML private BarChart<String, Number> ingresosChart;
    @FXML private VBox proximasCitasContainer;
    @FXML private VBox tareasContainer;

    @FXML
    public void initialize() {
        cargarMetricas();
        cargarSalaEspera();
        configurarGrafico();
        cargarProximasCitas();
        cargarTareas();
    }

    private void cargarMetricas() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM • hh:mm a", new Locale("es", "ES"));
        fechaLabel.setText(LocalDateTime.now().format(formatter));
        
        citasHoyValue.setText("24");
        esperaValue.setText("6");
        atendidosValue.setText("18");
        
        caninosLabel.setText("Caninos: 4");
        felinosLabel.setText("Felinos: 2");
    }

    private void cargarSalaEspera() {
        salaEsperaContainer.getChildren().clear();
        
        salaEsperaContainer.getChildren().add(crearFilaEspera("Bruno", "Labrador", "Carlos Méndez", "09:15 AM", "Vacunación Anual", false));
        salaEsperaContainer.getChildren().add(crearFilaEspera("Luna", "Persa", "Ana Sofia P.", "09:30 AM", "Control Post-Cirugía", true));
        salaEsperaContainer.getChildren().add(crearFilaEspera("Thor", "Golden R.", "Roberto Gómez", "09:38 AM", "Consulta General", false));
    }
    
    private HBox crearFilaEspera(String mascota, String raza, String dueno, String llegada, String motivo, boolean showIngresar) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-border-color: #e4e2e2; -fx-border-width: 0 0 1 0; -fx-padding: 10 0;");
        
        Label icon = new Label("🐾");
        icon.setStyle("-fx-font-size: 24px; -fx-background-color: #f5f3f3; -fx-padding: 10; -fx-background-radius: 8;");
        
        VBox infoMascota = new VBox(2);
        HBox nameBox = new HBox(5);
        Label nameLabel = new Label(mascota);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label razaLabel = new Label(raza);
        razaLabel.getStyleClass().add("chip");
        nameBox.getChildren().addAll(nameLabel, razaLabel);
        Label duenoLabel = new Label("Dueño: " + dueno);
        duenoLabel.setStyle("-fx-text-fill: #6e797a; -fx-font-size: 12px;");
        infoMascota.getChildren().addAll(nameBox, duenoLabel);
        infoMascota.setPrefWidth(150);
        
        VBox infoLlegada = new VBox(2);
        infoLlegada.setAlignment(Pos.CENTER);
        Label llegadaTitle = new Label("LLEGADA");
        llegadaTitle.setStyle("-fx-text-fill: #6e797a; -fx-font-size: 10px; -fx-font-weight: bold;");
        Label llegadaTime = new Label(llegada);
        llegadaTime.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        infoLlegada.getChildren().addAll(llegadaTitle, llegadaTime);
        infoLlegada.setPrefWidth(80);
        
        VBox infoMotivo = new VBox(2);
        infoMotivo.setAlignment(Pos.CENTER);
        Label motivoTitle = new Label("MOTIVO");
        motivoTitle.setStyle("-fx-text-fill: #6e797a; -fx-font-size: 10px; -fx-font-weight: bold;");
        Label motivoText = new Label(motivo);
        motivoText.setStyle("-fx-text-fill: #315ea2; -fx-font-size: 12px;");
        infoMotivo.getChildren().addAll(motivoTitle, motivoText);
        infoMotivo.setPrefWidth(120);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        row.getChildren().addAll(icon, infoMascota, infoLlegada, infoMotivo, spacer);
        
        if (showIngresar) {
            Button btnIngresar = new Button("Ingresar");
            btnIngresar.getStyleClass().add("btn-primary");
            row.getChildren().add(btnIngresar);
        }
        
        return row;
    }

    private void configurarGrafico() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("LUN 20", 32));
        series.getData().add(new XYChart.Data<>("MAR 21", 41));
        series.getData().add(new XYChart.Data<>("MIE 22", 24));
        series.getData().add(new XYChart.Data<>("JUE 23", 28));
        series.getData().add(new XYChart.Data<>("VIE 24", 22));
        ingresosChart.getData().add(series);
    }

    private void cargarProximasCitas() {
        proximasCitasContainer.getChildren().clear();
        proximasCitasContainer.getChildren().add(crearCita("10:00", "AM", "Kira (Canino)", "Ecografía Abdominal", "Dr. Ramírez"));
        proximasCitasContainer.getChildren().add(crearCita("10:30", "AM", "Simba (Felino)", "Chequeo General", "Dra. Valencia"));
        proximasCitasContainer.getChildren().add(crearCita("11:00", "AM", "Gofre (Conejo)", "Corte de Uñas", ""));
    }
    
    private HBox crearCita(String time, String ampm, String paciente, String motivo, String doc) {
        HBox card = new HBox(15);
        card.setStyle("-fx-border-color: #e4e2e2; -fx-border-radius: 8; -fx-padding: 12;");
        card.setAlignment(Pos.CENTER_LEFT);
        
        VBox timeBox = new VBox(0);
        timeBox.setAlignment(Pos.CENTER);
        Label lblTime = new Label(time);
        lblTime.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        Label lblAmpm = new Label(ampm);
        lblAmpm.setStyle("-fx-text-fill: #a0a0a0; -fx-font-size: 10px;");
        timeBox.getChildren().addAll(lblTime, lblAmpm);
        timeBox.setPrefWidth(40);
        
        VBox info = new VBox(2);
        Label lblPac = new Label(paciente);
        lblPac.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label lblMot = new Label(motivo);
        lblMot.setStyle("-fx-text-fill: #6e797a; -fx-font-size: 12px;");
        info.getChildren().addAll(lblPac, lblMot);
        
        if (!doc.isEmpty()) {
            Label lblDoc = new Label(doc);
            lblDoc.setStyle("-fx-text-fill: #006067; -fx-font-size: 10px; -fx-font-weight: bold;");
            info.getChildren().add(lblDoc);
        }
        
        card.getChildren().addAll(timeBox, info);
        return card;
    }

    private void cargarTareas() {
        tareasContainer.getChildren().clear();
        CheckBox t1 = new CheckBox("Llamar a dueño de Toby (Resultados)");
        CheckBox t2 = new CheckBox("Confirmar cirugías de mañana");
        t2.setSelected(true);
        // We cannot apply pseudo-classes easily here, so we fake strike-through via CSS inline. 
        // Wait, CheckBox text strikethrough in JavaFX can't easily be done directly on CheckBox unless we use a Label graphic or custom CSS.
        // We will just change the color to gray.
        t2.setStyle("-fx-text-fill: #a0a0a0;");
        CheckBox t3 = new CheckBox("Reponer carnets de vacunación");
        
        tareasContainer.getChildren().addAll(t1, t2, t3);
    }

    @FXML
    public void onNuevoDueno() {
        System.out.println("Acción: Nuevo Dueño");
    }

    @FXML
    public void onNuevaCita() {
        System.out.println("Acción: Agendar Cita");
    }
}
