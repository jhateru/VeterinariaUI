package society.controller.principales;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.TextAlignment;
import society.dao.FacturacionDao;
import society.modell.facturacion.CatalogoItem;
import society.modell.facturacion.DetalleFacturacion;
import society.modell.facturacion.Facturacion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FacturacionController {

    @FXML private ToggleButton btnTodo;
    @FXML private ToggleButton btnMedico;
    @FXML private ToggleButton btnEstetica;
    @FXML private ToggleButton btnAlimentos;
    @FXML private FlowPane catalogoGrid;
    
    @FXML private Label lblIdVenta;
    @FXML private VBox carritoContainer;
    
    @FXML private Label lblSubtotal;
    @FXML private Label lblDescuentos;
    @FXML private Label lblIva;
    @FXML private Label lblTotal;

    private List<CatalogoItem> catalogoCompleto;
    private Facturacion facturaActual;
    private FacturacionDao facturacionDao;

    @FXML
    public void initialize() {
        facturacionDao = new FacturacionDao();
        
        ToggleGroup group = new ToggleGroup();
        btnTodo.setToggleGroup(group);
        btnMedico.setToggleGroup(group);
        btnEstetica.setToggleGroup(group);
        btnAlimentos.setToggleGroup(group);
        
        cargarCatalogoDummy();
        iniciarNuevaFactura();
        renderCatalogo("Todos");
    }

    private void cargarCatalogoDummy() {
        catalogoCompleto = new ArrayList<>();
        catalogoCompleto.add(new CatalogoItem("S001", "Consulta General", "Evaluación veterinaria complet...", 35.00, "Médico", "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z", "#006067", "white"));
        catalogoCompleto.add(new CatalogoItem("S002", "Vacunación Anual", "Incluye Triple Felina o Sextuple...", 45.00, "Médico", "M3 13h2v-2H3v2zm0 4h2v-2H3v2zm0-8h2V7H3v2zm4 4h14v-2H7v2zm0 4h14v-2H7v2zM7 7v2h14V7H7z", "#73a5f5", "white"));
        catalogoCompleto.add(new CatalogoItem("E001", "Limpieza Dental", "Profilaxis ultrasónica profunda...", 120.00, "Estética", "M7 2v11h3v9l7-12h-4l4-8z", "#b59f84", "white"));
        catalogoCompleto.add(new CatalogoItem("A001", "Alimento Royal Canin", "Saco de 12kg para adultos...", 82.50, "Alimentos", "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z", "#00897b", "white"));
        catalogoCompleto.add(new CatalogoItem("S003", "Desparasitación", "Tableta palatable para control...", 18.00, "Médico", "M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2z", "#73a5f5", "white"));
        catalogoCompleto.add(new CatalogoItem("E002", "Grooming Completo", "Baño, corte de pelo, uñas...", 28.00, "Estética", "M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12z", "#8d6e63", "white"));
    }

    private void iniciarNuevaFactura() {
        facturaActual = new Facturacion();
        facturaActual.setId("POS-" + (int)(Math.random() * 100000));
        facturaActual.setClienteInfo("Max, Golden Retriever, Dueño: Carlos R.");
        facturaActual.setDescuentoPorcentaje(10); // Descuento de ejemplo aplicado a la venta global
        
        lblIdVenta.setText("ID: #" + facturaActual.getId());
        actualizarCarrito();
    }

    @FXML
    public void onFilterChange() {
        String categoria = "Todos";
        if (btnMedico.isSelected()) categoria = "Médico";
        if (btnEstetica.isSelected()) categoria = "Estética";
        if (btnAlimentos.isSelected()) categoria = "Alimentos";
        
        renderCatalogo(categoria);
        
        btnTodo.setStyle("-fx-background-color: " + (btnTodo.isSelected() ? "#006067" : "#f1f3f4") + "; -fx-text-fill: " + (btnTodo.isSelected() ? "white" : "#6e797a") + "; -fx-background-radius: 20; -fx-padding: 6 15; -fx-font-weight: bold;");
        btnMedico.setStyle("-fx-background-color: " + (btnMedico.isSelected() ? "#006067" : "#f1f3f4") + "; -fx-text-fill: " + (btnMedico.isSelected() ? "white" : "#6e797a") + "; -fx-background-radius: 20; -fx-padding: 6 15; -fx-font-weight: bold;");
        btnEstetica.setStyle("-fx-background-color: " + (btnEstetica.isSelected() ? "#006067" : "#f1f3f4") + "; -fx-text-fill: " + (btnEstetica.isSelected() ? "white" : "#6e797a") + "; -fx-background-radius: 20; -fx-padding: 6 15; -fx-font-weight: bold;");
        btnAlimentos.setStyle("-fx-background-color: " + (btnAlimentos.isSelected() ? "#006067" : "#f1f3f4") + "; -fx-text-fill: " + (btnAlimentos.isSelected() ? "white" : "#6e797a") + "; -fx-background-radius: 20; -fx-padding: 6 15; -fx-font-weight: bold;");
    }

    private void renderCatalogo(String categoria) {
        catalogoGrid.getChildren().clear();
        for (CatalogoItem item : catalogoCompleto) {
            if ("Todos".equals(categoria) || item.getCategoria().equals(categoria)) {
                catalogoGrid.getChildren().add(crearCardProducto(item));
            }
        }
    }

    private VBox crearCardProducto(CatalogoItem item) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-border-color: #e4e2e2; -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 15; -fx-cursor: hand;");
        card.setPrefWidth(220);
        card.setPrefHeight(250);
        
        // Icono
        StackPane iconPane = new StackPane();
        iconPane.setStyle("-fx-background-color: " + item.getColorFondoSVG() + "; -fx-background-radius: 8;");
        iconPane.setPrefSize(40, 40);
        iconPane.setMaxSize(40, 40);
        iconPane.setAlignment(Pos.CENTER_LEFT);
        
        SVGPath svg = new SVGPath();
        svg.setContent(item.getIconoSVG());
        svg.setFill(javafx.scene.paint.Color.web(item.getColorIconoSVG()));
        iconPane.getChildren().add(svg);
        
        VBox iconContainer = new VBox(iconPane);
        iconContainer.setAlignment(Pos.CENTER_LEFT);
        
        Label lblTitulo = new Label(item.getTitulo());
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-text-fill: #1b1c1c;");
        lblTitulo.setWrapText(true);
        lblTitulo.setMaxHeight(50);
        
        Label lblDesc = new Label(item.getDescripcion());
        lblDesc.setStyle("-fx-font-size: 12px; -fx-text-fill: #6e797a;");
        lblDesc.setWrapText(true);
        lblDesc.setPrefHeight(40);
        
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        HBox bottomRow = new HBox(10);
        bottomRow.setAlignment(Pos.CENTER_LEFT);
        Label lblPrecio = new Label(String.format("$%.2f", item.getPrecio()));
        lblPrecio.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #006067;");
        
        Label lblCategoria = new Label(item.getCategoria().toUpperCase());
        lblCategoria.setStyle("-fx-background-color: #f1f3f4; -fx-text-fill: #6e797a; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 2 6; -fx-background-radius: 4;");
        
        bottomRow.getChildren().addAll(lblPrecio, lblCategoria);
        
        card.getChildren().addAll(iconContainer, lblTitulo, lblDesc, spacer, bottomRow);
        
        card.setOnMouseClicked(e -> {
            agregarAlCarrito(item);
        });
        
        return card;
    }

    private void agregarAlCarrito(CatalogoItem item) {
        boolean encontrado = false;
        for (DetalleFacturacion df : facturaActual.getDetalles()) {
            if (df.getItem().getId().equals(item.getId())) {
                df.setCantidad(df.getCantidad() + 1);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            facturaActual.addDetalle(new DetalleFacturacion(item, 1, 0));
        }
        actualizarCarrito();
    }

    private void actualizarCarrito() {
        carritoContainer.getChildren().clear();
        for (DetalleFacturacion df : facturaActual.getDetalles()) {
            HBox row = new HBox(15);
            row.setAlignment(Pos.CENTER_LEFT);
            
            StackPane iconPane = new StackPane();
            iconPane.setStyle("-fx-background-color: " + df.getItem().getColorFondoSVG() + "; -fx-background-radius: 8;");
            iconPane.setPrefSize(40, 40);
            
            SVGPath svg = new SVGPath();
            svg.setContent(df.getItem().getIconoSVG());
            svg.setFill(javafx.scene.paint.Color.web(df.getItem().getColorIconoSVG()));
            iconPane.getChildren().add(svg);
            
            VBox textCol = new VBox(2);
            Label lblTitulo = new Label(df.getItem().getTitulo());
            lblTitulo.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #1b1c1c;");
            Label lblPrecio = new Label(String.format("Unidad: $%.2f", df.getItem().getPrecio()));
            lblPrecio.setStyle("-fx-font-size: 11px; -fx-text-fill: #6e797a;");
            textCol.getChildren().addAll(lblTitulo, lblPrecio);
            
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            
            VBox controlCol = new VBox(5);
            controlCol.setAlignment(Pos.CENTER_RIGHT);
            
            HBox qtyBox = new HBox(10);
            qtyBox.setAlignment(Pos.CENTER);
            qtyBox.setStyle("-fx-background-color: #f1f3f4; -fx-background-radius: 8; -fx-padding: 5 10;");
            Button btnMenos = new Button("-");
            btnMenos.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 0;");
            btnMenos.setOnAction(e -> {
                if (df.getCantidad() > 1) {
                    df.setCantidad(df.getCantidad() - 1);
                    actualizarCarrito();
                } else {
                    facturaActual.getDetalles().remove(df);
                    actualizarCarrito();
                }
            });
            Label lblQty = new Label(String.valueOf(df.getCantidad()));
            lblQty.setStyle("-fx-font-weight: bold;");
            Button btnMas = new Button("+");
            btnMas.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 0;");
            btnMas.setOnAction(e -> {
                df.setCantidad(df.getCantidad() + 1);
                actualizarCarrito();
            });
            qtyBox.getChildren().addAll(btnMenos, lblQty, btnMas);
            
            Label lblSub = new Label(String.format("$%.2f", df.getSubtotal()));
            lblSub.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #1b1c1c;");
            
            controlCol.getChildren().addAll(qtyBox, lblSub);
            
            row.getChildren().addAll(iconPane, textCol, spacer, controlCol);
            carritoContainer.getChildren().add(row);
        }
        
        if (facturaActual.getDescuentoPorcentaje() > 0 && !facturaActual.getDetalles().isEmpty()) {
            HBox descRow = new HBox(15);
            descRow.setAlignment(Pos.CENTER_LEFT);
            
            StackPane iconPane = new StackPane();
            iconPane.setStyle("-fx-background-color: #f5f6f7; -fx-background-radius: 8;");
            iconPane.setPrefSize(40, 40);
            SVGPath svg = new SVGPath();
            svg.setContent("M21.41 11.58l-9-9C12.05 2.22 11.55 2 11 2H4c-1.1 0-2 .9-2 2v7c0 .55.22 1.05.59 1.42l9 9c.36.36.86.58 1.41.58.55 0 1.05-.22 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.55-.23-1.06-.59-1.42zM5.5 7C4.67 7 4 6.33 4 5.5S4.67 4 5.5 4 7 4.67 7 5.5 6.33 7 5.5 7z");
            svg.setFill(javafx.scene.paint.Color.web("#d32f2f"));
            iconPane.getChildren().add(svg);
            
            VBox textCol = new VBox(2);
            Label lblTitulo = new Label("Descuento Cliente Frecuente");
            lblTitulo.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #6e797a;");
            Label lblDesc = new Label("-" + (int)facturaActual.getDescuentoPorcentaje() + "% sobre servicios");
            lblDesc.setStyle("-fx-font-size: 11px; -fx-text-fill: #d32f2f;");
            textCol.getChildren().addAll(lblTitulo, lblDesc);
            
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            
            Label lblTotalDesc = new Label(String.format("-$%.2f", facturaActual.getDescuentoTotal()));
            lblTotalDesc.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #d32f2f;");
            
            descRow.getChildren().addAll(iconPane, textCol, spacer, lblTotalDesc);
            carritoContainer.getChildren().add(descRow);
        }
        
        lblSubtotal.setText(String.format("$%.2f", facturaActual.getSubtotal()));
        lblDescuentos.setText(String.format("-$%.2f", facturaActual.getDescuentoTotal()));
        lblIva.setText(String.format("$%.2f", facturaActual.getIvaTotal()));
        lblTotal.setText(String.format("$%.2f", facturaActual.getTotal()));
    }

    @FXML
    public void onGuardarProforma() {
        guardarFactura(Facturacion.EstadoFactura.PROFORMA);
    }

    @FXML
    public void onProcesarPago() {
        guardarFactura(Facturacion.EstadoFactura.PAGADA);
    }

    private void guardarFactura(Facturacion.EstadoFactura estado) {
        if (facturaActual.getDetalles().isEmpty()) return;
        
        facturaActual.setEstado(estado);
        facturaActual.setFechaHora(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        facturacionDao.save(facturaActual);
        
        System.out.println("Factura " + estado.name() + " guardada en CSV con éxito.");
        
        iniciarNuevaFactura();
    }
}
