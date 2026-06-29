package society.controller.reutilizables;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import society.dao.HospitalizacionDao;
import society.dao.PacienteDao;
import society.modell.areamedica.Hospitalizacion;
import society.modell.recepcion.Paciente;

import java.time.LocalDateTime;
import java.util.Optional;

public class RegistroHospitalizacionController {

    @FXML private TextField searchPacienteField;
    @FXML private HBox pacienteCard;
    @FXML private ImageView pacienteImageView;
    @FXML private Label nombrePacienteLabel;
    @FXML private Label detallesPacienteLabel;
    @FXML private Label propietarioLabel;

    @FXML private ComboBox<String> motivoIngresoCombo;
    @FXML private ComboBox<String> areaCanilCombo;
    
    @FXML private ToggleButton btnCritico;
    @FXML private ToggleButton btnEstable;
    @FXML private ToggleButton btnReservado;
    private ToggleGroup estadoGroup;

    @FXML private ComboBox<String> frecuenciaMonitoreoCombo;

    @FXML private TextArea dietaArea;
    @FXML private TextArea medicacionArea;
    @FXML private TextArea alertasArea;

    @FXML private TextField tempField;
    @FXML private TextField fcField;
    @FXML private TextField frField;

    private PacienteDao pacienteDao;
    private HospitalizacionDao hospitalizacionDao;
    private Paciente pacienteSeleccionado;
    
    // Callback to refresh the main view
    private Runnable onGuardadoExitoso;

    @FXML
    public void initialize() {
        pacienteDao = new PacienteDao();
        hospitalizacionDao = new HospitalizacionDao();

        // Opciones
        motivoIngresoCombo.getItems().addAll("Post-Operación", "Neumonía Monitoring", "Diagnostic Testing", "Observation");
        motivoIngresoCombo.getSelectionModel().selectFirst();

        areaCanilCombo.getItems().addAll("UCI-01 (Cuidados Intensivos)", "CAGE A-04", "CAGE B-12", "CAGE C-02");
        areaCanilCombo.getSelectionModel().selectFirst();

        frecuenciaMonitoreoCombo.getItems().addAll("Cada 2 horas", "Cada 4 horas", "Cada 8 horas", "Solo emergencias");
        frecuenciaMonitoreoCombo.getSelectionModel().select(1);

        // Grupo de botones para estado
        estadoGroup = new ToggleGroup();
        btnCritico.setToggleGroup(estadoGroup);
        btnEstable.setToggleGroup(estadoGroup);
        btnReservado.setToggleGroup(estadoGroup);

        // Búsqueda simple
        searchPacienteField.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarPaciente(newValue);
        });
        
        // Cargar dummy inicial (Max)
        buscarPaciente("Max");
    }

    private void buscarPaciente(String query) {
        if (query == null || query.trim().isEmpty()) {
            pacienteCard.setVisible(false);
            pacienteSeleccionado = null;
            return;
        }
        
        // Simular búsqueda encontrando el primero que coincida, o simplemente cargar uno de prueba
        Optional<Paciente> opt = pacienteDao.getAll().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(query.toLowerCase()) || 
                             p.getNombreDueno().toLowerCase().contains(query.toLowerCase()))
                .findFirst();

        if (opt.isPresent()) {
            pacienteSeleccionado = opt.get();
            nombrePacienteLabel.setText(pacienteSeleccionado.getNombre());
            detallesPacienteLabel.setText(pacienteSeleccionado.getRaza() + " • " + pacienteSeleccionado.getSexo() + " • " + pacienteSeleccionado.getEdadAproximada());
            propietarioLabel.setText("Propietario: " + pacienteSeleccionado.getNombreDueno());
            
            try {
                String imgPath = pacienteSeleccionado.getEspecie() == Paciente.EspecieAnimal.GATO ? "/society/images/cat.png" : "/society/images/dog.png";
                pacienteImageView.setImage(new Image(getClass().getResourceAsStream(imgPath)));
            } catch (Exception e) { e.printStackTrace(); }
            
            pacienteCard.setVisible(true);
        } else {
            pacienteCard.setVisible(false);
            pacienteSeleccionado = null;
        }
    }

    public void setOnGuardadoExitoso(Runnable callback) {
        this.onGuardadoExitoso = callback;
    }

    @FXML
    private void handleConfirmar(ActionEvent event) {
        if (pacienteSeleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Debe seleccionar un paciente.");
            alert.showAndWait();
            return;
        }

        Hospitalizacion h = new Hospitalizacion();
        h.setPaciente(pacienteSeleccionado);
        h.setFechaIngreso(LocalDateTime.now());
        h.setEstado(Hospitalizacion.EstadoHospitalizacion.INGRESADO);
        
        h.setMotivo(motivoIngresoCombo.getValue());
        h.setJaulaAsignada(areaCanilCombo.getValue());
        
        ToggleButton tb = (ToggleButton) estadoGroup.getSelectedToggle();
        h.setEstadoMedicoInicial(tb != null ? tb.getText() : "ESTABLE");
        
        h.setFrecuenciaMonitoreo(frecuenciaMonitoreoCombo.getValue());
        h.setDietaEspecial(dietaArea.getText());
        h.setCronogramaMedicacion(medicacionArea.getText());
        h.setAlertasSignos(alertasArea.getText());
        
        h.setTemperaturaIngreso(tempField.getText());
        h.setFrecCardiacaIngreso(fcField.getText());
        h.setFrecRespiratoriaIngreso(frField.getText());

        hospitalizacionDao.save(h);

        if (onGuardadoExitoso != null) {
            onGuardadoExitoso.run();
        }

        cerrarVentana();
    }

    @FXML
    private void handleCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnEstable.getScene().getWindow();
        stage.close();
    }
}
