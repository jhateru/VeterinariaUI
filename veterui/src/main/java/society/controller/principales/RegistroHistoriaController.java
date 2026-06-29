package society.controller.principales;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import society.dao.HistoriaClinicaDao;
import society.modell.areamedica.HistoriaClinica;
import society.modell.recepcion.Paciente;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class RegistroHistoriaController {

    @FXML private ImageView patientImageView;
    @FXML private Label nameLabel;
    @FXML private Label statusLabel;
    @FXML private Label breedAgeLabel;
    @FXML private Label weightLabel;
    @FXML private Label tempLabel;

    @FXML private ComboBox<String> tipoConsultaCombo;
    @FXML private TextField nombreConsultaField;
    @FXML private TextField fechaHoraField;
    @FXML private TextField veterinarioField;

    @FXML private TextArea obsSubjetivasArea;
    @FXML private TextArea obsObjetivasArea;

    @FXML private TextField diagnosticoField;

    @FXML private TextField medicacionField;
    @FXML private TextField dosisField;
    @FXML private TextArea recomendacionesArea;
    @FXML private DatePicker fechaControlPicker;

    private Paciente paciente;
    private HistoriaClinicaDao historiaDao;
    private HistoriaClinica historiaExistente;
    private boolean isEditMode = false;

    public void setDatos(Paciente paciente, HistoriaClinicaDao dao, boolean isEditMode) {
        this.paciente = paciente;
        this.historiaDao = dao;
        this.isEditMode = isEditMode;

        // Cargar info de cabecera
        nameLabel.setText(paciente.getNombre());
        breedAgeLabel.setText(paciente.getRaza() + " • " + paciente.getSexo() + " • " + paciente.getEdadAproximada());
        weightLabel.setText("⚖ " + paciente.getPeso() + " kg");
        
        switch (paciente.getEstado()) {
            case EN_CLINICA: statusLabel.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-padding: 2 8; -fx-background-radius: 4; -fx-font-size: 10px;"); statusLabel.setText("EN CLÍNICA"); break;
            case ALTA: statusLabel.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 2 8; -fx-background-radius: 4; -fx-font-size: 10px;"); statusLabel.setText("ALTA"); break;
            case SEGUIMIENTO: statusLabel.setStyle("-fx-background-color: #f1c40f; -fx-text-fill: white; -fx-padding: 2 8; -fx-background-radius: 4; -fx-font-size: 10px;"); statusLabel.setText("SEGUIMIENTO"); break;
        }

        try {
            String imgPath = paciente.getEspecie() == Paciente.EspecieAnimal.GATO ? "/society/images/cat.png" : "/society/images/dog.png";
            patientImageView.setImage(new Image(getClass().getResourceAsStream(imgPath)));
        } catch (Exception e) { e.printStackTrace(); }

        // Opciones del combo
        tipoConsultaCombo.getItems().addAll("Consulta de Rutina", "Urgencia", "Seguimiento", "Vacunación", "Especialidad");
        tipoConsultaCombo.setValue("Consulta de Rutina");

        Optional<HistoriaClinica> opt = historiaDao.findById(paciente.getId());
        if (opt.isPresent()) {
            this.historiaExistente = opt.get();
        }

        if (isEditMode && historiaExistente != null) {
            cargarDatosParaEditar();
        } else {
            // Valores por defecto
            fechaHoraField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ", 10:00 a.m.");
            veterinarioField.setText("Dr. Alejandro Méndez");
        }
    }

    private void cargarDatosParaEditar() {
        // En una aplicación real, se parsearían los campos estructurados desde evoluciones y tratamientos.
        // Dado que el CSV guarda esto como un String largo, haremos una simulación para la UI:
        String evol = historiaExistente.getEvoluciones();
        if (evol != null && !evol.isEmpty()) {
            String[] partes = evol.split("\\|", -1);
            if (partes.length > 0) {
                String lastEvol = partes[partes.length - 1]; // Tomar la última consulta
                String[] datos = lastEvol.split(";", -1);
                if (datos.length >= 3) {
                    if (datos[0].contains(" - ")) {
                        String[] titleParts = datos[0].split(" - ", 2);
                        tipoConsultaCombo.setValue(titleParts[0]);
                        nombreConsultaField.setText(titleParts[1]);
                    } else {
                        tipoConsultaCombo.setValue(datos[0]);
                        nombreConsultaField.setText("");
                    }
                    fechaHoraField.setText(datos[1]);
                    obsSubjetivasArea.setText(datos[2]);
                    if (datos.length >= 4) obsObjetivasArea.setText(datos[3]);
                    if (datos.length >= 5) diagnosticoField.setText(datos[4]);
                }
            }
        }

        String trat = historiaExistente.getTratamientos();
        if (trat != null && !trat.isEmpty()) {
            String[] partesT = trat.split("\\|", -1);
            if (partesT.length > 0) {
                String lastTrat = partesT[partesT.length - 1];
                String[] datosT = lastTrat.split(";", -1);
                if (datosT.length >= 2) {
                    medicacionField.setText(datosT[0]);
                    dosisField.setText(datosT[1]);
                    if (datosT.length >= 3) recomendacionesArea.setText(datosT[2]);
                }
            }
        }
    }

    @FXML
    private void handleDescartar(ActionEvent event) {
        cerrarModal();
    }

    @FXML
    private void handleGuardar(ActionEvent event) {
        String tipoConsulta = tipoConsultaCombo.getValue() != null ? tipoConsultaCombo.getValue() : "Consulta General";
        if (nombreConsultaField.getText() != null && !nombreConsultaField.getText().trim().isEmpty()) {
            tipoConsulta += " - " + nombreConsultaField.getText().trim().replace(";", ",").replace("|", ",");
        }
        String fechaHora = fechaHoraField.getText();
        String obsS = obsSubjetivasArea.getText() != null ? obsSubjetivasArea.getText().replace(";", ",").replace("|", ",") : "";
        String obsO = obsObjetivasArea.getText() != null ? obsObjetivasArea.getText().replace(";", ",").replace("|", ",") : "";
        String diag = diagnosticoField.getText() != null ? diagnosticoField.getText().replace(";", ",").replace("|", ",") : "";
        
        String nuevaEvolucion = tipoConsulta + ";" + fechaHora + ";" + obsS + ";" + obsO + ";" + diag;

        String med = medicacionField.getText() != null ? medicacionField.getText().replace(";", ",").replace("|", ",") : "";
        String dosis = dosisField.getText() != null ? dosisField.getText().replace(";", ",").replace("|", ",") : "";
        String recom = recomendacionesArea.getText() != null ? recomendacionesArea.getText().replace(";", ",").replace("|", ",") : "";

        String nuevoTratamiento = med + ";" + dosis + ";" + recom;

        if (historiaExistente == null) {
            // Crear nueva
            int newId = historiaDao.getAll().size() + 1;
            HistoriaClinica hc = new HistoriaClinica(newId, paciente, LocalDate.now(), "Ninguna", "Ninguno", paciente.getPeso(), nuevaEvolucion, nuevoTratamiento);
            historiaDao.save(hc);
        } else {
            // Actualizar existente
            String evolActual = historiaExistente.getEvoluciones();
            if (isEditMode) {
                // En modo edición, para la demostración, reemplazamos la última o simplemente sobreescribimos
                // Si quisiéramos hacerlo perfecto, reemplazaríamos solo la última parte separada por '|'
                // Para simplificar: lo agregamos como una nueva entrada o sobrescribimos si estaba vacío.
                if (evolActual == null || evolActual.isEmpty()) {
                    historiaExistente.setEvoluciones(nuevaEvolucion);
                } else {
                    int lastPipe = evolActual.lastIndexOf("|");
                    if (lastPipe != -1) {
                        historiaExistente.setEvoluciones(evolActual.substring(0, lastPipe) + "|" + nuevaEvolucion);
                    } else {
                        historiaExistente.setEvoluciones(nuevaEvolucion);
                    }
                }
                
                String tratActual = historiaExistente.getTratamientos();
                if (tratActual == null || tratActual.isEmpty()) {
                    historiaExistente.setTratamientos(nuevoTratamiento);
                } else {
                    int lastPipeT = tratActual.lastIndexOf("|");
                    if (lastPipeT != -1) {
                        historiaExistente.setTratamientos(tratActual.substring(0, lastPipeT) + "|" + nuevoTratamiento);
                    } else {
                        historiaExistente.setTratamientos(nuevoTratamiento);
                    }
                }
            } else {
                // Modo registro: anexar
                historiaExistente.setEvoluciones((evolActual == null || evolActual.isEmpty() ? "" : evolActual + "|") + nuevaEvolucion);
                String tratActual = historiaExistente.getTratamientos();
                historiaExistente.setTratamientos((tratActual == null || tratActual.isEmpty() ? "" : tratActual + "|") + nuevoTratamiento);
            }
            java.util.List<HistoriaClinica> todas = historiaDao.getAll();
            for (int i = 0; i < todas.size(); i++) {
                if (todas.get(i).getId() == historiaExistente.getId()) {
                    todas.set(i, historiaExistente);
                    break;
                }
            }
            historiaDao.saveAll(todas);
        }

        cerrarModal();
    }

    private void cerrarModal() {
        Stage stage = (Stage) nameLabel.getScene().getWindow();
        stage.close();
    }
}
