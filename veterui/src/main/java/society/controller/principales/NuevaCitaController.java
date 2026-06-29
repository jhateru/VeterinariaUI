package society.controller.principales;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import society.dao.CitaDao;
import society.modell.recepcion.Cita;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class NuevaCitaController {
    
    @FXML private TextField pacienteField;
    @FXML private TextField duenoField;
    @FXML private ComboBox<String> veterinarioCombo;
    @FXML private DatePicker fechaPicker;
    @FXML private ComboBox<String> horaCombo;
    @FXML private ComboBox<String> minutoCombo;
    @FXML private TextField motivoField;
    
    private CitaDao citaDao = new CitaDao();
    
    @FXML
    public void initialize() {
        veterinarioCombo.getItems().addAll("Dr. Ramírez", "Dra. Valencia", "Todos los Doctores");
        
        for (int i = 8; i <= 18; i++) {
            horaCombo.getItems().add(String.format("%02d", i));
        }
        minutoCombo.getItems().addAll("00", "15", "30", "45");
    }
    
    @FXML
    public void onGuardar() {
        if (fechaPicker.getValue() != null && horaCombo.getValue() != null && minutoCombo.getValue() != null) {
            LocalTime time = LocalTime.of(Integer.parseInt(horaCombo.getValue()), Integer.parseInt(minutoCombo.getValue()));
            LocalDateTime ldt = LocalDateTime.of(fechaPicker.getValue(), time);
            
            Cita nuevaCita = new Cita(
                (int)(System.currentTimeMillis() % 100000), 
                ldt, 
                Cita.EstadoCita.PENDIENTE, 
                pacienteField.getText(), 
                veterinarioCombo.getValue(), 
                motivoField.getText()
            );
            
            citaDao.save(nuevaCita);
            System.out.println("Cita Guardada en CSV: " + nuevaCita.getPacienteNombre());
        }
        cerrarModal();
    }
    
    @FXML
    public void onCancelar() {
        cerrarModal();
    }
    
    private void cerrarModal() {
        Stage stage = (Stage) pacienteField.getScene().getWindow();
        stage.close();
    }
}
