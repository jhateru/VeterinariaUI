package society.controller.principales;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import society.dao.PacienteDao;
import society.modell.recepcion.Paciente;
import society.modell.recepcion.Paciente.EspecieAnimal;
import society.modell.recepcion.Paciente.EstadoPaciente;

public class PacientesController {

    @FXML private VBox pacientesContainer;
    
    @FXML private ToggleButton btnTodo;
    @FXML private ToggleButton btnPerros;
    @FXML private ToggleButton btnGatos;
    @FXML private ToggleButton btnExoticos;
    
    @FXML private ToggleButton btnEnClinica;
    @FXML private ToggleButton btnAlta;
    @FXML private ToggleButton btnSeguimiento;
    
    @FXML private Label lblMostrando;
    
    @FXML private Label lblPacientesInternos;
    @FXML private ProgressBar progOcupacion;

    private PacienteDao pacienteDao = new PacienteDao();
    private List<Paciente> todosLosPacientes;

    @FXML
    public void initialize() {
        // Toggle groups logic isn't strictly necessary for custom styles, but we handle it manually in onFilterChange
        cargarPacientes();
    }

    public void cargarPacientes() {
        todosLosPacientes = pacienteDao.getAll();
        aplicarFiltros();
    }

    @FXML
    public void onFilterChange() {
        // Here we could handle mutual exclusivity manually if needed
        // For simplicity, we just trigger filter re-evaluation
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        List<Paciente> filtrados = todosLosPacientes;
        
        // Especie Filter
        if (btnPerros.isSelected()) {
            filtrados = filtrados.stream().filter(p -> p.getEspecie() == EspecieAnimal.PERRO).collect(Collectors.toList());
        } else if (btnGatos.isSelected()) {
            filtrados = filtrados.stream().filter(p -> p.getEspecie() == EspecieAnimal.GATO).collect(Collectors.toList());
        } else if (btnExoticos.isSelected()) {
            filtrados = filtrados.stream().filter(p -> p.getEspecie() == EspecieAnimal.EXOTICO).collect(Collectors.toList());
        }
        
        // Estado Filter
        if (btnEnClinica.isSelected()) {
            filtrados = filtrados.stream().filter(p -> p.getEstado() == EstadoPaciente.EN_CLINICA).collect(Collectors.toList());
        } else if (btnAlta.isSelected()) {
            filtrados = filtrados.stream().filter(p -> p.getEstado() == EstadoPaciente.ALTA).collect(Collectors.toList());
        } else if (btnSeguimiento.isSelected()) {
            filtrados = filtrados.stream().filter(p -> p.getEstado() == EstadoPaciente.SEGUIMIENTO).collect(Collectors.toList());
        }

        renderizarFilas(filtrados);
        actualizarMétricas();
    }

    private void renderizarFilas(List<Paciente> lista) {
        pacientesContainer.getChildren().clear();
        for (Paciente p : lista) {
            pacientesContainer.getChildren().add(crearFilaPaciente(p));
        }
        lblMostrando.setText("Mostrando " + lista.size() + " de " + todosLosPacientes.size() + " pacientes registrados");
    }
    
    private void actualizarMétricas() {
        long enClinica = todosLosPacientes.stream().filter(p -> p.getEstado() == EstadoPaciente.EN_CLINICA).count();
        lblPacientesInternos.setText(String.valueOf(enClinica));
        progOcupacion.setProgress(enClinica / 24.0); // Assuming 24 is max capacity
    }

    private HBox crearFilaPaciente(Paciente p) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-padding: 15; -fx-border-color: #e4e2e2; -fx-border-width: 0 0 1 0;");
        
        // PACIENTE (Avatar + Name + Sex/Age)
        HBox colPaciente = new HBox(10);
        colPaciente.setAlignment(Pos.CENTER_LEFT);
        colPaciente.setPrefWidth(250);
        
        String emoji = "🐾";
        if (p.getEspecie() == EspecieAnimal.PERRO) emoji = "🐶";
        else if (p.getEspecie() == EspecieAnimal.GATO) emoji = "🐱";
        else if (p.getEspecie() == EspecieAnimal.EXOTICO) emoji = "🦎";
        
        Label avatar = new Label(emoji);
        avatar.setStyle("-fx-background-color: #e4e2e2; -fx-font-size: 24px; -fx-padding: 8; -fx-background-radius: 25; -fx-alignment: center;");
        avatar.setMinSize(50, 50);
        
        VBox pacInfo = new VBox(2);
        Label pacName = new Label(p.getNombre());
        pacName.setStyle("-fx-font-weight: bold; -fx-text-fill: #1b1c1c; -fx-font-size: 14px;");
        Label pacDesc = new Label((p.getSexo() != null ? p.getSexo() : "") + ", " + (p.getEdadAproximada() != null ? p.getEdadAproximada() : ""));
        pacDesc.setStyle("-fx-text-fill: #6e797a; -fx-font-size: 11px;");
        pacInfo.getChildren().addAll(pacName, pacDesc);
        
        colPaciente.getChildren().addAll(avatar, pacInfo);
        
        // ESPECIE / RAZA
        VBox colEspecie = new VBox(2);
        colEspecie.setAlignment(Pos.CENTER_LEFT);
        colEspecie.setPrefWidth(200);
        Label spcName = new Label(p.getEspecie() != null ? p.getEspecie().name() : "");
        spcName.setStyle("-fx-text-fill: #1b1c1c; -fx-font-size: 13px;");
        Label brdName = new Label(p.getRaza() != null ? p.getRaza() : "");
        brdName.setStyle("-fx-text-fill: #6e797a; -fx-font-size: 11px;");
        colEspecie.getChildren().addAll(spcName, brdName);
        
        // DUEÑO
        VBox colDueno = new VBox(2);
        colDueno.setAlignment(Pos.CENTER_LEFT);
        colDueno.setPrefWidth(200);
        Label ownName = new Label(p.getNombreDueno() != null ? p.getNombreDueno() : "Sin dueño");
        ownName.setStyle("-fx-text-fill: #1b1c1c; -fx-font-size: 13px;");
        Label ownPhone = new Label(p.getTelefonoDueno() != null ? p.getTelefonoDueno() : "");
        ownPhone.setStyle("-fx-text-fill: #315ea2; -fx-font-size: 11px;");
        colDueno.getChildren().addAll(ownName, ownPhone);
        
        // PESO (KG)
        Label colPeso = new Label(String.valueOf(p.getPeso()));
        colPeso.setStyle("-fx-text-fill: #6e797a; -fx-font-size: 13px;");
        colPeso.setPrefWidth(100);

        // ALERGIAS
        Label colAlergias = new Label(p.getAlergias() != null && !p.getAlergias().isEmpty() ? p.getAlergias() : "Ninguna");
        colAlergias.setStyle("-fx-text-fill: #6e797a; -fx-font-size: 13px;");
        colAlergias.setPrefWidth(150);

        // MOTIVO CONSULTA
        Label colMotivo = new Label(p.getMotivoPrimeraConsulta() != null ? p.getMotivoPrimeraConsulta() : "");
        colMotivo.setStyle("-fx-text-fill: #6e797a; -fx-font-size: 13px;");
        colMotivo.setPrefWidth(200);
        
        // ÚLTIMA VISITA
        Label colVisita = new Label(p.getUltimaVisita() != null ? p.getUltimaVisita() : "");
        colVisita.setStyle("-fx-text-fill: #6e797a; -fx-font-size: 13px;");
        colVisita.setPrefWidth(150);
        
        // ESTADO
        HBox colEstado = new HBox();
        colEstado.setAlignment(Pos.CENTER_LEFT);
        colEstado.setPrefWidth(150);
        Label lblEstado = new Label();
        lblEstado.setStyle("-fx-font-size: 11px; -fx-padding: 4 12; -fx-background-radius: 12; -fx-font-weight: bold;");
        if (p.getEstado() == EstadoPaciente.EN_CLINICA) {
            lblEstado.setText("● En Clínica");
            lblEstado.setStyle(lblEstado.getStyle() + "-fx-background-color: #dcfce7; -fx-text-fill: #166534;");
        } else if (p.getEstado() == EstadoPaciente.SEGUIMIENTO) {
            lblEstado.setText("● Seguimiento");
            lblEstado.setStyle(lblEstado.getStyle() + "-fx-background-color: #fef08a; -fx-text-fill: #854d0e;");
        } else {
            lblEstado.setText("● Alta");
            lblEstado.setStyle(lblEstado.getStyle() + "-fx-background-color: #f3f4f6; -fx-text-fill: #6b7280;");
        }
        colEstado.getChildren().add(lblEstado);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // ACCIONES
        HBox colAcciones = new HBox(10);
        colAcciones.setAlignment(Pos.CENTER);
        colAcciones.setPrefWidth(100);
        Button btnVer = new Button("👁");
        btnVer.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 16px;");
        Button btnDoc = new Button("📋");
        btnDoc.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 16px;");
        colAcciones.getChildren().addAll(btnVer, btnDoc);
        
        row.getChildren().addAll(colPaciente, colEspecie, colDueno, colPeso, colAlergias, colMotivo, colVisita, colEstado, spacer, colAcciones);
        return row;
    }

    @FXML
    public void onNuevoPaciente() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/society/NuevoPacienteModal.fxml"));
            Parent root = fxmlLoader.load();
            
            NuevoPacienteController controller = fxmlLoader.getController();
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Registrar Nuevo Paciente");
            stage.setScene(new Scene(root, 700, 600));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
