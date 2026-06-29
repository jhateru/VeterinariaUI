package society.controller.principales;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import society.dao.PacienteDao;
import society.modell.recepcion.Paciente;
import society.modell.recepcion.Paciente.EspecieAnimal;
import society.modell.recepcion.Paciente.EstadoPaciente;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NuevoPacienteController {

    @FXML private TextField txtNombre;
    @FXML private ComboBox<EspecieAnimal> cmbEspecie;
    @FXML private TextField txtRaza;
    @FXML private TextField txtEdad;
    @FXML private RadioButton rbMacho;
    @FXML private RadioButton rbHembra;
    @FXML private TextField txtPeso;
    
    @FXML private TextField txtDuenoNombre;
    @FXML private TextField txtDuenoDni;
    @FXML private TextField txtDuenoTelefono;
    @FXML private TextField txtDuenoEmail;
    @FXML private TextField txtDuenoDireccion;
    
    @FXML private TextArea txtAlergias;
    @FXML private TextArea txtMotivo;
    @FXML private ComboBox<EstadoPaciente> cmbEstado;

    private ToggleGroup sexoGroup;
    private PacienteDao pacienteDao = new PacienteDao();
    private PacientesController parentController;

    @FXML
    public void initialize() {
        cmbEspecie.getItems().setAll(EspecieAnimal.values());
        cmbEspecie.setValue(EspecieAnimal.PERRO);
        
        cmbEstado.getItems().setAll(EstadoPaciente.values());
        cmbEstado.setValue(EstadoPaciente.EN_CLINICA);
        
        sexoGroup = new ToggleGroup();
        rbMacho.setToggleGroup(sexoGroup);
        rbHembra.setToggleGroup(sexoGroup);
        rbMacho.setSelected(true);
    }

    public void setParentController(PacientesController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void onGuardar() {
        Paciente p = new Paciente();
        p.setId((int) (System.currentTimeMillis() % 100000));
        p.setNombre(txtNombre.getText());
        p.setEspecie(cmbEspecie.getValue());
        p.setRaza(txtRaza.getText());
        p.setEdadAproximada(txtEdad.getText());
        p.setSexo(rbMacho.isSelected() ? "Macho" : "Hembra");
        try {
            p.setPeso(Double.parseDouble(txtPeso.getText()));
        } catch (NumberFormatException e) {
            p.setPeso(0.0);
        }
        
        p.setNombreDueno(txtDuenoNombre.getText());
        p.setDniDueno(txtDuenoDni.getText());
        p.setTelefonoDueno(txtDuenoTelefono.getText());
        p.setEmailDueno(txtDuenoEmail.getText());
        p.setDireccionDueno(txtDuenoDireccion.getText());
        
        p.setAlergias(txtAlergias.getText());
        p.setMotivoPrimeraConsulta(txtMotivo.getText());
        p.setEstado(cmbEstado.getValue());
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM, yyyy");
        p.setUltimaVisita("Hoy, " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

        pacienteDao.save(p);

        if (parentController != null) {
            parentController.cargarPacientes();
        }
        
        cerrarModal();
    }

    @FXML
    public void onCancelar() {
        cerrarModal();
    }

    private void cerrarModal() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }
}
