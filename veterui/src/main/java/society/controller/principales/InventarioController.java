package society.controller.principales;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import society.dao.InventarioDao;
import society.modell.inventario.Inventario;

import java.util.List;
import java.util.stream.Collectors;

public class InventarioController {
    
    @FXML private Label totalItemsLabel;
    @FXML private Label stockBajoLabel;
    @FXML private Label proximosVencerLabel;
    @FXML private Label inversionLabel;

    @FXML private VBox medicamentosContainer;
    @FXML private VBox equipoContainer;
    @FXML private VBox alertasContainer;

    private InventarioDao inventarioDao;
    private ObservableList<Inventario> inventarioList;

    @FXML
    public void initialize() {
        inventarioDao = new InventarioDao();
        List<Inventario> items = inventarioDao.getAll();
        
        // Carga datos mock si el CSV está vacío para que se vea como en la imagen
        if (items.isEmpty()) {
            items.add(new Inventario("1", "Amoxicillin 250mg", "Antibiotico Broad Spectrum", 1240, "Capsulas", "ESTABLE", "12 Oct 2024", "Medicamento"));
            items.add(new Inventario("2", "Rabies Vaccine (3yr)", "Immunization - Zoetis", 14, "Viales", "STOCK BAJO", "18 Jul 2024", "Medicamento"));
            items.add(new Inventario("3", "Prednisolone 5mg", "Anti-Inflamatorio Corticosteroide", 0, "Tabletas", "AGOTADO", "-", "Medicamento"));
            items.add(new Inventario("4", "Carprofen 100mg", "NSAID Analgesico", 42, "Tabletas masticables", "SUFICIENTE", "05 Feb 2025", "Medicamento"));
            
            items.add(new Inventario("5", "Sterile Gloves (Size 7)", "Latex - Pack x 50", 128, "Cajas", "ESTABLE", "-", "Equipo"));
            items.add(new Inventario("6", "Disposable Scalpels #10", "Acero Inoxidable", 8, "Cajas", "BAJO", "-", "Equipo"));
            items.add(new Inventario("7", "Gauze Sponges 4x4", "12-ply Sterile Pack", 450, "Paquetes", "ESTABLE", "-", "Equipo"));
            
            inventarioDao.saveAll(items);
        }
        
        inventarioList = FXCollections.observableArrayList(items);
        
        cargarResumen(items);
        cargarMedicamentos(items);
        cargarEquipoQuirurgico(items);
        cargarAlertas();
    }

    private void cargarResumen(List<Inventario> items) {
        totalItemsLabel.setText(String.valueOf(items.size()));
        long stockBajo = items.stream()
            .filter(i -> "BAJO".equals(i.getEstado()) || "STOCK BAJO".equals(i.getEstado()) || "AGOTADO".equals(i.getEstado()))
            .count();
        stockBajoLabel.setText(String.valueOf(stockBajo));
    }

    private void cargarMedicamentos(List<Inventario> items) {
        medicamentosContainer.getChildren().clear();
        
        List<Inventario> medicamentos = items.stream()
            .filter(i -> "Medicamento".equals(i.getCategoria()))
            .collect(Collectors.toList());
            
        for (Inventario med : medicamentos) {
            HBox row = new HBox();
            row.setStyle("-fx-padding: 15 20; -fx-border-color: #e4e2e2; -fx-border-width: 0 0 1 0;");
            row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            
            // Producto
            VBox prodBox = new VBox(2);
            prodBox.setPrefWidth(160);
            Label lblProd = new Label(med.getProducto());
            lblProd.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #1b1c1c;");
            Label lblDesc = new Label(med.getDescripcion());
            lblDesc.setStyle("-fx-font-size: 11px; -fx-text-fill: #6e797a; -fx-wrap-text: true;");
            prodBox.getChildren().addAll(lblProd, lblDesc);
            
            // Stock
            Label lblStock = new Label(String.valueOf(med.getStock()));
            lblStock.setPrefWidth(80);
            if (med.getStock() == 0 || med.getEstado().contains("BAJO") || med.getEstado().contains("AGOTADO")) {
                lblStock.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #d32f2f;");
            } else {
                lblStock.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #1b1c1c;");
            }
            
            // Unidad
            Label lblUnidad = new Label(med.getUnidad());
            lblUnidad.setPrefWidth(120);
            lblUnidad.setStyle("-fx-font-size: 13px; -fx-text-fill: #1b1c1c;");
            
            // Estado (Badge)
            HBox estadoBox = new HBox();
            estadoBox.setPrefWidth(120);
            estadoBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            
            Label lblEstado = new Label(med.getEstado());
            String colorFondo = "#e0e0e0";
            String colorTexto = "#6e797a";
            
            if ("ESTABLE".equals(med.getEstado())) {
                colorFondo = "#ccf0f0";
                colorTexto = "#006067";
            } else if ("STOCK BAJO".equals(med.getEstado())) {
                colorFondo = "#facdcd";
                colorTexto = "#d32f2f";
            } else if ("BAJO".equals(med.getEstado())) {
                colorFondo = "#facdcd";
                colorTexto = "#d32f2f";
                lblEstado.setText("STOCK BAJO");
            } else if ("AGOTADO".equals(med.getEstado())) {
                colorFondo = "#e4e2e2";
                colorTexto = "#1b1c1c";
            } else if ("SUFICIENTE".equals(med.getEstado())) {
                colorFondo = "#d2e3fc";
                colorTexto = "#1967d2";
            }
            
            lblEstado.setStyle("-fx-background-color: " + colorFondo + "; -fx-text-fill: " + colorTexto + "; -fx-padding: 3 8; -fx-background-radius: 10; -fx-font-size: 10px; -fx-font-weight: bold;");
            estadoBox.getChildren().add(lblEstado);
            
            // Fefo
            VBox fefoBox = new VBox(2);
            fefoBox.setPrefWidth(100);
            if (!"-".equals(med.getFefo())) {
                String[] parts = med.getFefo().split(" ");
                if (parts.length >= 3) {
                    Label lblDayMonth = new Label(parts[0] + " " + parts[1]);
                    lblDayMonth.setStyle("-fx-font-size: 12px; -fx-text-fill: #1b1c1c;");
                    Label lblYear = new Label(parts[2]);
                    lblYear.setStyle("-fx-font-size: 11px; -fx-text-fill: #6e797a;");
                    fefoBox.getChildren().addAll(lblDayMonth, lblYear);
                } else {
                    Label lblFefo = new Label(med.getFefo());
                    fefoBox.getChildren().add(lblFefo);
                }
            } else {
                Label lblDash = new Label("-");
                fefoBox.getChildren().add(lblDash);
            }
            
            row.getChildren().addAll(prodBox, lblStock, lblUnidad, estadoBox, fefoBox);
            medicamentosContainer.getChildren().add(row);
        }
    }

    private void cargarEquipoQuirurgico(List<Inventario> items) {
        equipoContainer.getChildren().clear();
        
        List<Inventario> equipos = items.stream()
            .filter(i -> "Equipo".equals(i.getCategoria()))
            .collect(Collectors.toList());
        
        for (Inventario eq : equipos) {
            boolean alert = eq.getStock() < 15;
            
            HBox card = new HBox(15);
            card.setStyle("-fx-background-color: white; -fx-border-color: #e4e2e2; -fx-border-radius: 8; -fx-padding: 15;");
            card.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            
            // Icon placeholder
            Region iconBg = new Region();
            iconBg.setMinWidth(40);
            iconBg.setMinHeight(40);
            iconBg.setPrefWidth(40);
            iconBg.setPrefHeight(40);
            iconBg.setStyle("-fx-background-color: #f1f3f4; -fx-background-radius: 8;");
            
            VBox infoBox = new VBox(2);
            Label lblTitle = new Label(eq.getProducto());
            lblTitle.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #1b1c1c;");
            Label lblDesc = new Label(eq.getDescripcion());
            lblDesc.setStyle("-fx-font-size: 11px; -fx-text-fill: #6e797a;");
            infoBox.getChildren().addAll(lblTitle, lblDesc);
            
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            
            VBox stockBox = new VBox(2);
            stockBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
            Label lblStock = new Label(String.valueOf(eq.getStock()));
            lblStock.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + (alert ? "#d32f2f" : "#1b1c1c") + ";");
            String subStockText = alert ? "Min: 15" : "En Stock";
            Label lblSubStock = new Label(subStockText);
            lblSubStock.setStyle("-fx-font-size: 10px; -fx-text-fill: " + (alert ? "#d32f2f" : "#6e797a") + ";");
            stockBox.getChildren().addAll(lblStock, lblSubStock);
            
            card.getChildren().addAll(iconBg, infoBox, spacer, stockBox);
            equipoContainer.getChildren().add(card);
        }
    }

    private void cargarAlertas() {
        alertasContainer.getChildren().clear();
        
        Label alerta1 = new Label("Amoxicillin Lote #AX-902\nVence en 48 horas. Priorizar uso o descartar.");
        alerta1.setStyle("-fx-font-size: 12px; -fx-text-fill: #d32f2f; -fx-wrap-text: true;");
        
        Label alerta2 = new Label("Sutura Nylon 3-0\nInventario agotado en Sala A. Reponer de Almacen.");
        alerta2.setStyle("-fx-font-size: 12px; -fx-text-fill: #d32f2f; -fx-wrap-text: true;");
        
        alertasContainer.getChildren().addAll(alerta1, alerta2);
    }
}
