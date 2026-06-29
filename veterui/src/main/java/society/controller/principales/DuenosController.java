package society.controller.principales;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Circle;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import society.dao.DuenoDao;
import society.modell.recepcion.Dueno;
import society.modell.recepcion.Dueno.EstadoDueno;

public class DuenosController {

    @FXML private FlowPane duenosContainer;
    @FXML private Button btnTodos;
    @FXML private Button btnFrecuentes;
    @FXML private Button btnNuevos;

    private DuenoDao duenoDao = new DuenoDao();
    private List<Dueno> todosLosDuenos;
    private EstadoDueno filtroActual = null; // null means "Todos"

    @FXML
    public void initialize() {
        cargarDuenos(null);
    }

    public void cargarDuenos(EstadoDueno filtro) {
        todosLosDuenos = duenoDao.getAll();
        actualizarContadores();
        aplicarFiltro(filtro);
    }

    private void actualizarContadores() {
        int total = todosLosDuenos.size();
        btnTodos.setText("Todos (" + total + ")");
    }

    private void aplicarFiltro(EstadoDueno filtro) {
        this.filtroActual = filtro;
        
        // Reset styles
        String baseStyle = "-fx-background-color: white; -fx-border-color: #e4e2e2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 6 15; -fx-text-fill: #6e797a; -fx-cursor: hand;";
        String activeStyle = "-fx-background-color: #008080; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 6 15; -fx-font-weight: bold; -fx-cursor: hand;";
        
        btnTodos.setStyle(baseStyle);
        btnFrecuentes.setStyle(baseStyle);
        btnNuevos.setStyle(baseStyle);

        List<Dueno> filtrados;
        if (filtro == null) {
            btnTodos.setStyle(activeStyle);
            filtrados = todosLosDuenos;
        } else if (filtro == EstadoDueno.FRECUENTE) {
            btnFrecuentes.setStyle(activeStyle);
            filtrados = todosLosDuenos.stream().filter(d -> d.getEstado() == EstadoDueno.FRECUENTE).collect(Collectors.toList());
        } else {
            btnNuevos.setStyle(activeStyle);
            filtrados = todosLosDuenos.stream().filter(d -> d.getEstado() == EstadoDueno.NUEVO).collect(Collectors.toList());
        }

        renderizarTarjetas(filtrados);
    }

    @FXML
    public void onFilterTodos() {
        aplicarFiltro(null);
    }

    @FXML
    public void onFilterFrecuentes() {
        aplicarFiltro(EstadoDueno.FRECUENTE);
    }

    @FXML
    public void onFilterNuevos() {
        aplicarFiltro(EstadoDueno.NUEVO);
    }

    private void renderizarTarjetas(List<Dueno> lista) {
        duenosContainer.getChildren().clear();
        for (Dueno d : lista) {
            duenosContainer.getChildren().add(crearCardDueno(d));
        }
    }

    private VBox crearCardDueno(Dueno d) {
        VBox card = new VBox(15);
        card.setStyle("-fx-background-color: white; -fx-border-color: #e4e2e2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 20;");
        card.setPrefWidth(350);
        card.setMinWidth(350);

        // Header: Avatar, Name, ID, Tag, Menu icon
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        
        // Pseudo-Avatar using initials if no image
        Label avatar = new Label(d.getNombre().substring(0, 1).toUpperCase());
        avatar.setStyle("-fx-background-color: #d1bda2; -fx-text-fill: #1b1c1c; -fx-font-weight: bold; -fx-font-size: 20px; -fx-alignment: center; -fx-background-radius: 10;");
        avatar.setPrefSize(50, 50);
        avatar.setMinSize(50, 50);

        VBox nameIdTag = new VBox(3);
        Label name = new Label(d.getNombre() + (d.getApellidos() != null ? " " + d.getApellidos() : ""));
        name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1b1c1c;");
        Label idLbl = new Label("ID: " + (d.getDni() != null ? d.getDni() : d.getId()));
        idLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #6e797a;");
        
        Label tag = new Label();
        tag.setStyle("-fx-font-size: 11px; -fx-padding: 2 10; -fx-background-radius: 10;");
        if (d.getEstado() == EstadoDueno.FRECUENTE) {
            tag.setText("Frecuente");
            tag.setStyle(tag.getStyle() + "-fx-background-color: #e0e7ff; -fx-text-fill: #3730a3;");
        } else if (d.getEstado() == EstadoDueno.DEUDA_PENDIENTE) {
            tag.setText("Deuda Pendiente");
            tag.setStyle(tag.getStyle() + "-fx-background-color: #ffe4e6; -fx-text-fill: #9f1239;");
        } else {
            tag.setText(d.getEstado().name());
            tag.setStyle(tag.getStyle() + "-fx-background-color: #f3f4f6; -fx-text-fill: #4b5563;");
        }
        
        nameIdTag.getChildren().addAll(name, idLbl, tag);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label menuIcon = new Label("⋮");
        menuIcon.setStyle("-fx-font-weight: bold; -fx-text-fill: #6e797a; -fx-font-size: 18px;");

        header.getChildren().addAll(avatar, nameIdTag, spacer, menuIcon);

        // Contact info
        VBox contactInfo = new VBox(8);
        Label phone = new Label("📞 " + (d.getTelefono() != null ? d.getTelefono() : "Sin teléfono"));
        phone.setStyle("-fx-text-fill: #4b5563; -fx-font-size: 13px;");
        Label email = new Label("✉ " + (d.getEmail() != null ? d.getEmail() : "Sin email"));
        email.setStyle("-fx-text-fill: #4b5563; -fx-font-size: 13px;");
        contactInfo.getChildren().addAll(phone, email);

        // Separator
        Region line = new Region();
        line.setStyle("-fx-background-color: #e4e2e2; -fx-pref-height: 1; -fx-min-height: 1;");

        // Mascotas Registradas
        VBox mascotasInfo = new VBox(8);
        Label mascotasLbl = new Label("MASCOTAS REGISTRADAS");
        mascotasLbl.setStyle("-fx-font-size: 11px; -fx-text-fill: #6e797a; -fx-font-weight: bold;");
        
        HBox mascotasPills = new HBox(8);
        String mn = d.getMascotasNombres();
        if (mn != null && !mn.isEmpty()) {
            for (String m : mn.split(",")) {
                Label pill = new Label("🐾 " + m.trim());
                pill.setStyle("-fx-background-color: #f3f4f6; -fx-text-fill: #4b5563; -fx-padding: 4 10; -fx-background-radius: 4; -fx-font-size: 12px;");
                mascotasPills.getChildren().add(pill);
            }
        }
        mascotasInfo.getChildren().addAll(mascotasLbl, mascotasPills);

        // Footer buttons
        HBox footer = new HBox(10);
        Button btnVerHistorial = new Button("Ver Historial");
        btnVerHistorial.setStyle("-fx-background-color: white; -fx-border-color: #006067; -fx-text-fill: #006067; -fx-border-radius: 4; -fx-font-weight: bold; -fx-padding: 8 0; -fx-cursor: hand;");
        btnVerHistorial.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnVerHistorial, Priority.ALWAYS);
        
        Button btnAction = new Button("💬");
        btnAction.setStyle("-fx-background-color: #a5f3fc; -fx-text-fill: #006067; -fx-background-radius: 4; -fx-padding: 8 12; -fx-cursor: hand;");

        footer.getChildren().addAll(btnVerHistorial, btnAction);

        card.getChildren().addAll(header, contactInfo, line, mascotasInfo, footer);
        return card;
    }

    @FXML
    public void onNuevoDueno() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/society/NuevoDuenoModal.fxml"));
            Parent root = fxmlLoader.load();
            
            NuevoDuenoController controller = fxmlLoader.getController();
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Agendar Nuevo Dueño");
            stage.setScene(new Scene(root, 400, 400));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
