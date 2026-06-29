package society.controller.principales;

import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import society.dao.ConfiguracionDao;
import society.modell.configuracion.Configuracion;

public class ConfiguracionController {

    @FXML
    private TextField txtNombreClinica;
    @FXML
    private TextField txtIdFiscal;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtCorreo;

    @FXML
    private CheckBox chk2FA;

    @FXML
    private ComboBox<String> cmbIdioma;
    @FXML
    private ComboBox<String> cmbMoneda;

    @FXML
    private CheckBox chkNotifCitas;
    @FXML
    private CheckBox chkNotifLab;
    @FXML
    private CheckBox chkNotifResumen;

    private ConfiguracionDao configuracionDao;
    private Configuracion configActual;

    @FXML
    public void initialize() {
        configuracionDao = new ConfiguracionDao();
        
        // Init ComboBoxes
        cmbIdioma.getItems().addAll("Español (Colombia)", "Español (España)", "English (US)");
        cmbMoneda.getItems().addAll("Peso Colombiano (COP)", "Euro (EUR)", "US Dollar (USD)");

        cargarConfiguracion();
    }

    private void cargarConfiguracion() {
        List<Configuracion> configs = configuracionDao.getAll();
        if (configs != null && !configs.isEmpty()) {
            configActual = configs.get(0);
        } else {
            // Default if empty
            configActual = new Configuracion(1, "", "", "", "", "", "Español (Colombia)", "Peso Colombiano (COP)", false, false, false, false);
        }

        txtNombreClinica.setText(configActual.getNombreClinica());
        txtIdFiscal.setText(configActual.getIdFiscal());
        txtDireccion.setText(configActual.getDireccion());
        txtTelefono.setText(configActual.getTelefono());
        txtCorreo.setText(configActual.getCorreo());

        chk2FA.setSelected(configActual.isDosFaActivo());

        cmbIdioma.setValue(configActual.getIdioma());
        cmbMoneda.setValue(configActual.getMoneda());

        chkNotifCitas.setSelected(configActual.isNotifRecordatorioCitas());
        chkNotifLab.setSelected(configActual.isNotifResultadosLab());
        chkNotifResumen.setSelected(configActual.isNotifResumenSemanal());
    }

    @FXML
    private void guardarCambios(ActionEvent event) {
        configActual.setNombreClinica(txtNombreClinica.getText());
        configActual.setIdFiscal(txtIdFiscal.getText());
        configActual.setDireccion(txtDireccion.getText());
        configActual.setTelefono(txtTelefono.getText());
        configActual.setCorreo(txtCorreo.getText());

        configActual.setDosFaActivo(chk2FA.isSelected());

        configActual.setIdioma(cmbIdioma.getValue());
        configActual.setMoneda(cmbMoneda.getValue());

        configActual.setNotifRecordatorioCitas(chkNotifCitas.isSelected());
        configActual.setNotifResultadosLab(chkNotifLab.isSelected());
        configActual.setNotifResumenSemanal(chkNotifResumen.isSelected());

        // Update in CSV
        java.util.List<Configuracion> all = java.util.Collections.singletonList(configActual);
        configuracionDao.saveAll(all);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText("Configuración guardada correctamente.");
        alert.showAndWait();
    }
}
