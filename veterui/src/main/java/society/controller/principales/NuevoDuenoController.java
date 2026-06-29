package society.controller.principales;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import society.dao.DuenoDao;
import society.modell.recepcion.Dueno;
import society.modell.recepcion.Dueno.EstadoDueno;

public class NuevoDuenoController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtDni;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private ComboBox<EstadoDueno> cmbEstado;
    @FXML private TextField txtMascotas;

    private DuenoDao duenoDao = new DuenoDao();
    private DuenosController parentController;

    @FXML
    public void initialize() {
        cmbEstado.getItems().setAll(EstadoDueno.values());
        cmbEstado.setValue(EstadoDueno.NUEVO);
    }

    public void setParentController(DuenosController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void onGuardar() {
        Dueno d = new Dueno();
        d.setId((int) (System.currentTimeMillis() % 100000)); // Simple ID generator
        d.setNombre(txtNombre.getText());
        d.setApellidos(txtApellidos.getText());
        d.setDni(txtDni.getText());
        d.setTelefono(txtTelefono.getText());
        d.setEmail(txtEmail.getText());
        d.setEstado(cmbEstado.getValue());
        d.setMascotasNombres(txtMascotas.getText());

        duenoDao.save(d);

        if (parentController != null) {
            parentController.cargarDuenos(null); // Recargar todos
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
