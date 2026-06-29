package society.controller.principales;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import society.dao.ReportesDao;
import society.modell.reportes.Reportes;

import java.util.ArrayList;
import java.util.List;

public class ReportesController {

    @FXML private ComboBox<String> filtroTiempo;
    @FXML private Label ingresosLabel;
    @FXML private Label nuevosPacientesLabel;
    @FXML private Label consultasLabel;
    @FXML private Label tasaOcupacionLabel;
    @FXML private LineChart<String, Number> tendenciasChart;
    @FXML private PieChart especiesChart;
    @FXML private VBox desempenoContainer;

    private ReportesDao reportesDao;

    @FXML
    public void initialize() {
        filtroTiempo.setItems(FXCollections.observableArrayList("Últimos 30 días", "Este Mes", "Mes Anterior", "Este Año"));
        filtroTiempo.getSelectionModel().selectFirst();

        reportesDao = new ReportesDao();
        List<Reportes> reportesList = reportesDao.getAll();

        if (reportesList.isEmpty()) {
            reportesList.add(new Reportes("1", "Dr. Alejandro Pérez", 245, 4.9, "Alta"));
            reportesList.add(new Reportes("2", "Dra. Maria García", 198, 4.8, "Media"));
            reportesList.add(new Reportes("3", "Dr. Roberto Sánchez", 164, 4.7, "Baja"));
            reportesDao.saveAll(reportesList);
        }

        cargarDesempeno(reportesList);
        configurarGraficos();
    }

    private void cargarDesempeno(List<Reportes> reportesList) {
        desempenoContainer.getChildren().clear();

        for (Reportes r : reportesList) {
            HBox row = new HBox();
            row.setStyle("-fx-padding: 15 20; -fx-border-color: #e4e2e2; -fx-border-width: 0 0 1 0;");
            row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

            // Veterinario
            HBox vetBox = new HBox(10);
            vetBox.setPrefWidth(200);
            vetBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            Label avatar = new Label("👨‍⚕️");
            avatar.setStyle("-fx-font-size: 20px; -fx-background-color: #f1f3f4; -fx-background-radius: 20; -fx-padding: 5;");
            Label nombre = new Label(r.getVeterinarioNombre());
            nombre.setStyle("-fx-font-size: 14px; -fx-text-fill: #1b1c1c;");
            vetBox.getChildren().addAll(avatar, nombre);

            // Consultas
            Label lblConsultas = new Label(String.valueOf(r.getConsultas()));
            lblConsultas.setPrefWidth(100);
            lblConsultas.setStyle("-fx-font-size: 13px; -fx-text-fill: #6e797a;");

            // Calificacion
            Label lblCalificacion = new Label(r.getCalificacion() + " ★");
            lblCalificacion.setPrefWidth(120);
            lblCalificacion.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #f39c12;");

            // Carga
            HBox cargaBox = new HBox(10);
            cargaBox.setPrefWidth(100);
            cargaBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
            
            Region barraCarga = new Region();
            barraCarga.setPrefHeight(6);
            HBox.setHgrow(barraCarga, Priority.ALWAYS);
            
            Label lblCarga = new Label(r.getCarga());
            lblCarga.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");

            if ("Alta".equals(r.getCarga())) {
                barraCarga.setStyle("-fx-background-color: #d32f2f; -fx-background-radius: 3;");
                lblCarga.setStyle(lblCarga.getStyle() + " -fx-text-fill: #d32f2f;");
            } else if ("Media".equals(r.getCarga())) {
                barraCarga.setStyle("-fx-background-color: #006067; -fx-background-radius: 3;");
                lblCarga.setStyle(lblCarga.getStyle() + " -fx-text-fill: #006067;");
            } else {
                barraCarga.setStyle("-fx-background-color: #4a90e2; -fx-background-radius: 3; -fx-max-width: 30;");
                lblCarga.setStyle(lblCarga.getStyle() + " -fx-text-fill: #4a90e2;");
            }

            cargaBox.getChildren().addAll(barraCarga, lblCarga);

            row.getChildren().addAll(vetBox, lblConsultas, lblCalificacion, cargaBox);
            desempenoContainer.getChildren().add(row);
        }
    }

    private void configurarGraficos() {
        // Grafico de líneas (Tendencias)
        XYChart.Series<String, Number> seriesCitas = new XYChart.Series<>();
        seriesCitas.setName("Citas");
        seriesCitas.getData().add(new XYChart.Data<>("Semana 1", 30));
        seriesCitas.getData().add(new XYChart.Data<>("Semana 2", 80));
        seriesCitas.getData().add(new XYChart.Data<>("Semana 3", 40));
        seriesCitas.getData().add(new XYChart.Data<>("Semana 4", 150));

        XYChart.Series<String, Number> seriesHistorias = new XYChart.Series<>();
        seriesHistorias.setName("Historias Médicas");
        seriesHistorias.getData().add(new XYChart.Data<>("Semana 1", 20));
        seriesHistorias.getData().add(new XYChart.Data<>("Semana 2", 40));
        seriesHistorias.getData().add(new XYChart.Data<>("Semana 3", 30));
        seriesHistorias.getData().add(new XYChart.Data<>("Semana 4", 80));

        tendenciasChart.getData().addAll(seriesCitas, seriesHistorias);

        // Estilizar lineas
        // Se aplicaría mejor por CSS: 
        // .default-color0.chart-series-line { -fx-stroke: #006067; -fx-stroke-width: 3px; }
        // .default-color1.chart-series-line { -fx-stroke: #a3c2ff; -fx-stroke-width: 3px; }

        // Gráfico de Pastel (Distribución por especie)
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Caninos", 58),
                new PieChart.Data("Felinos", 32),
                new PieChart.Data("Exóticos", 10)
        );
        especiesChart.setData(pieData);
    }
}
