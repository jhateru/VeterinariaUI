package society.controller.principales;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import society.dao.CitaDao;
import society.modell.recepcion.Cita;

public class CitasController {

    @FXML private GridPane headerGrid;
    @FXML private GridPane calendarGrid;
    @FXML private VBox proximasCitasContainer;
    
    @FXML private Label hoyLabel;
    @FXML private Label countCitas;
    @FXML private Label countAtendidos;
    @FXML private Label countPendientes;
    
    @FXML private Button btnSemana;
    @FXML private Button btnMes;
    @FXML private Button btnDia;
    
    private CitaDao CitaDao = new CitaDao();
    private LocalDate fechaReferencia = LocalDate.now();
    private enum VistaTipo { DIA, SEMANA, MES }
    private VistaTipo vistaActual = VistaTipo.SEMANA;

    @FXML
    public void initialize() {
        actualizarSidebar();
        renderizarCalendario();
    }
    
    @FXML
    public void onSemanaView() {
        vistaActual = VistaTipo.SEMANA;
        resetBtnStyles();
        btnSemana.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 8; -fx-font-weight: bold; -fx-text-fill: #006067;");
        renderizarCalendario();
    }
    
    @FXML
    public void onMesView() {
        vistaActual = VistaTipo.MES;
        resetBtnStyles();
        btnMes.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 8; -fx-font-weight: bold; -fx-text-fill: #006067;");
        renderizarCalendario();
    }
    
    @FXML
    public void onDiaView() {
        vistaActual = VistaTipo.DIA;
        resetBtnStyles();
        btnDia.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 8; -fx-font-weight: bold; -fx-text-fill: #006067;");
        renderizarCalendario();
    }
    
    private void resetBtnStyles() {
        String base = "-fx-background-color: transparent; -fx-text-fill: #6e797a;";
        btnSemana.setStyle(base);
        btnMes.setStyle(base);
        btnDia.setStyle(base);
    }
    
    public void renderizarCalendario() {
        headerGrid.getChildren().clear();
        headerGrid.getColumnConstraints().clear();
        headerGrid.getRowConstraints().clear();
        calendarGrid.getChildren().clear();
        calendarGrid.getColumnConstraints().clear();
        calendarGrid.getRowConstraints().clear();
        
        if (vistaActual == VistaTipo.SEMANA) {
            renderizarSemana();
        } else if (vistaActual == VistaTipo.DIA) {
            renderizarDia();
        } else {
            renderizarMes();
        }
        
        cargarTodasLasCitasEnSidebar();
    }
    
    private void renderizarSemana() {
        LocalDate inicioSemana = fechaReferencia.with(DayOfWeek.MONDAY);
        
        ColumnConstraints timeCol = new ColumnConstraints();
        timeCol.setPrefWidth(60);
        timeCol.setMinWidth(60);
        headerGrid.getColumnConstraints().add(timeCol);
        calendarGrid.getColumnConstraints().add(timeCol);
        
        headerGrid.add(new Region(), 0, 0);
        
        DateTimeFormatter df = DateTimeFormatter.ofPattern("EEE", new Locale("es", "ES"));
        for (int i = 0; i < 7; i++) {
            LocalDate dia = inicioSemana.plusDays(i);
            
            ColumnConstraints dayCol = new ColumnConstraints();
            dayCol.setHgrow(Priority.ALWAYS);
            dayCol.setMinWidth(100);
            headerGrid.getColumnConstraints().add(dayCol);
            calendarGrid.getColumnConstraints().add(dayCol);
            
            VBox headerCell = new VBox();
            headerCell.setAlignment(Pos.CENTER);
            headerCell.getStyleClass().add("calendar-header");
            headerCell.setStyle("-fx-border-color: #e4e2e2; -fx-border-width: 0 0 1 1; -fx-padding: 8;");
            
            Label lblDayName = new Label(dia.format(df).toUpperCase());
            lblDayName.setStyle("-fx-font-size: 12px; -fx-text-fill: #6e797a; -fx-font-weight: bold;");
            Label lblDayNum = new Label(String.valueOf(dia.getDayOfMonth()));
            lblDayNum.getStyleClass().add("headline-md");
            
            headerCell.getChildren().addAll(lblDayName, lblDayNum);
            headerGrid.add(headerCell, i + 1, 0);
        }
        
        dibujarFilasYCeldaHoras(7);
        cargarCitasGridSemanaODia(inicioSemana, 7);
    }
    
    private void renderizarDia() {
        ColumnConstraints timeCol = new ColumnConstraints();
        timeCol.setPrefWidth(60);
        timeCol.setMinWidth(60);
        headerGrid.getColumnConstraints().add(timeCol);
        calendarGrid.getColumnConstraints().add(timeCol);
        headerGrid.add(new Region(), 0, 0);
        
        ColumnConstraints dayCol = new ColumnConstraints();
        dayCol.setHgrow(Priority.ALWAYS);
        headerGrid.getColumnConstraints().add(dayCol);
        calendarGrid.getColumnConstraints().add(dayCol);
        
        VBox headerCell = new VBox();
        headerCell.setAlignment(Pos.CENTER);
        headerCell.getStyleClass().add("calendar-header");
        headerCell.setStyle("-fx-border-color: #e4e2e2; -fx-border-width: 0 0 1 1; -fx-padding: 8;");
        
        DateTimeFormatter df = DateTimeFormatter.ofPattern("EEEE", new Locale("es", "ES"));
        Label lblDayName = new Label(fechaReferencia.format(df).toUpperCase());
        lblDayName.setStyle("-fx-font-size: 12px; -fx-text-fill: #6e797a; -fx-font-weight: bold;");
        Label lblDayNum = new Label(String.valueOf(fechaReferencia.getDayOfMonth()));
        lblDayNum.getStyleClass().add("headline-md");
        
        headerCell.getChildren().addAll(lblDayName, lblDayNum);
        headerGrid.add(headerCell, 1, 0);
        
        dibujarFilasYCeldaHoras(1);
        cargarCitasGridSemanaODia(fechaReferencia, 1);
    }
    
    private void renderizarMes() {
        LocalDate inicioMes = fechaReferencia.withDayOfMonth(1);
        int diasEnMes = inicioMes.lengthOfMonth();
        int offset = inicioMes.getDayOfWeek().getValue() - 1; // 0 = Monday, 6 = Sunday
        
        DateTimeFormatter df = DateTimeFormatter.ofPattern("EEE", new Locale("es", "ES"));
        LocalDate temp = inicioMes.with(DayOfWeek.MONDAY);
        for (int i = 0; i < 7; i++) {
            ColumnConstraints dayCol = new ColumnConstraints();
            dayCol.setHgrow(Priority.ALWAYS);
            dayCol.setMinWidth(100);
            headerGrid.getColumnConstraints().add(dayCol);
            calendarGrid.getColumnConstraints().add(dayCol);
            
            VBox headerCell = new VBox();
            headerCell.setAlignment(Pos.CENTER);
            headerCell.getStyleClass().add("calendar-header");
            headerCell.setStyle("-fx-border-color: #e4e2e2; -fx-border-width: 0 0 1 1; -fx-padding: 8;");
            Label lblDayName = new Label(temp.plusDays(i).format(df).toUpperCase());
            lblDayName.setStyle("-fx-font-size: 12px; -fx-text-fill: #6e797a; -fx-font-weight: bold;");
            headerCell.getChildren().add(lblDayName);
            headerGrid.add(headerCell, i, 0);
        }
        
        int filas = (int) Math.ceil((diasEnMes + offset) / 7.0);
        for (int row = 0; row < filas; row++) {
            RowConstraints rc = new RowConstraints();
            rc.setMinHeight(120);
            rc.setVgrow(Priority.ALWAYS);
            calendarGrid.getRowConstraints().add(rc);
            
            for (int col = 0; col < 7; col++) {
                int diaIndex = row * 7 + col - offset + 1;
                VBox cell = new VBox(4);
                cell.setStyle("-fx-border-color: #e4e2e2; -fx-border-width: 0 1 1 0; -fx-padding: 4; -fx-background-color: #ffffff;");
                
                if (diaIndex > 0 && diaIndex <= diasEnMes) {
                    Label num = new Label(String.valueOf(diaIndex));
                    num.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1b1c1c;");
                    cell.getChildren().add(num);
                    
                    // Inyectar mini tarjetas de citas
                    LocalDate currentDate = inicioMes.withDayOfMonth(diaIndex);
                    List<Cita> citas = CitaDao.getAll();
                    for(Cita c : citas) {
                        if(c.getFechaHora() != null && c.getFechaHora().toLocalDate().equals(currentDate)) {
                            Label miniEvent = new Label(c.getFechaHora().getHour() + ":00 " + c.getPacienteNombre());
                            miniEvent.setStyle("-fx-background-color: #e9f0fc; -fx-text-fill: #006067; -fx-font-size: 10px; -fx-padding: 2 4; -fx-background-radius: 4;");
                            miniEvent.setMaxWidth(Double.MAX_VALUE);
                            if(c.getEstado() == Cita.EstadoCita.URGENCIA) {
                                miniEvent.setStyle("-fx-background-color: #ffdad6; -fx-text-fill: #93000a; -fx-font-size: 10px; -fx-padding: 2 4; -fx-background-radius: 4;");
                            }
                            cell.getChildren().add(miniEvent);
                        }
                    }
                } else {
                    cell.setStyle("-fx-border-color: #e4e2e2; -fx-border-width: 0 1 1 0; -fx-background-color: #fbf9f8;");
                }
                calendarGrid.add(cell, col, row);
            }
        }
    }
    
    private void dibujarFilasYCeldaHoras(int dias) {
        for (int i = 0; i < 12; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setMinHeight(80);
            calendarGrid.getRowConstraints().add(rc);
            
            String hora = String.format("%02d:00", i + 8);
            Label timeLabel = new Label(hora);
            timeLabel.getStyleClass().add("calendar-time");
            timeLabel.setMaxWidth(Double.MAX_VALUE);
            timeLabel.setMaxHeight(Double.MAX_VALUE);
            timeLabel.setAlignment(Pos.TOP_RIGHT);
            timeLabel.setStyle("-fx-padding: 8;");
            calendarGrid.add(timeLabel, 0, i);
            
            for (int j = 1; j <= dias; j++) {
                VBox cell = new VBox();
                cell.getStyleClass().add("calendar-cell");
                calendarGrid.add(cell, j, i);
            }
        }
    }
    
    private void cargarCitasGridSemanaODia(LocalDate fechaInicio, int diasRevisar) {
        List<Cita> citas = CitaDao.getAll();
        LocalDate fechaFin = fechaInicio.plusDays(diasRevisar - 1);
        
        for (Cita c : citas) {
            if (c.getFechaHora() == null) continue;
            LocalDate citaDate = c.getFechaHora().toLocalDate();
            
            if (!citaDate.isBefore(fechaInicio) && !citaDate.isAfter(fechaFin)) {
                int col = (int) java.time.temporal.ChronoUnit.DAYS.between(fechaInicio, citaDate) + 1;
                if (vistaActual == VistaTipo.DIA) {
                    col = 1;
                }
                int row = c.getFechaHora().getHour() - 8;
                
                if (row >= 0 && row < 12) {
                    String cardStyle = "event-blue";
                    String textStyle = "event-blue-text";
                    if (c.getEstado() == Cita.EstadoCita.URGENCIA) {
                        cardStyle = "event-red";
                        textStyle = "event-red-text";
                    }
                    
                    VBox card = crearEventoCalendario("🐾 " + c.getPacienteNombre(), c.getMotivo(), cardStyle, textStyle);
                    calendarGrid.add(card, col, row);
                }
            }
        }
    }
    
    private void cargarTodasLasCitasEnSidebar() {
        proximasCitasContainer.getChildren().clear();
        List<Cita> citas = CitaDao.getAll();
        
        int countTotal = citas.size();
        int countCompletadas = 0;
        
        for(Cita c : citas) {
            if(c.getEstado() == Cita.EstadoCita.COMPLETADA) {
                countCompletadas++;
            }
            if(c.getFechaHora() != null) {
                String timeStr = c.getFechaHora().format(DateTimeFormatter.ofPattern("dd MMM HH:mm", new Locale("es", "ES")));
                proximasCitasContainer.getChildren().add(crearCardSidebarCuadrada("🐾", c.getPacienteNombre(), c.getMotivo(), timeStr, c.getEstado().name()));
            }
        }
        
        countCitas.setText(String.valueOf(countTotal));
        countAtendidos.setText(String.valueOf(countCompletadas));
        countPendientes.setText(String.valueOf(countTotal - countCompletadas));
    }
    
    private void actualizarSidebar() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d MMM yyyy", new Locale("es", "ES"));
        hoyLabel.setText("Fecha Actual: " + LocalDate.now().format(dtf));
    }
    
    private VBox crearEventoCalendario(String title, String desc, String cardStyle, String textStyle) {
        VBox card = new VBox(4);
        card.getStyleClass().addAll("event-card", cardStyle);
        card.setMaxWidth(Double.MAX_VALUE);
        Label titleLbl = new Label(title);
        titleLbl.getStyleClass().add(textStyle);
        Label descLbl = new Label(desc != null ? desc : "");
        descLbl.setStyle("-fx-font-size: 10px; -fx-text-fill: #6e797a; -fx-wrap-text: true;");
        card.getChildren().addAll(titleLbl, descLbl);
        VBox container = new VBox(card);
        container.setStyle("-fx-padding: 4;");
        VBox.setVgrow(card, Priority.ALWAYS); // Hacer que la tarjeta crezca si hay espacio
        return container;
    }
    
    private VBox crearCardSidebarCuadrada(String iconStr, String name, String motif, String time, String status) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");
        card.setStyle("-fx-background-color: #ffffff; -fx-padding: 16; -fx-border-color: #e4e2e2; -fx-border-radius: 8; -fx-background-radius: 8;");
        
        HBox topBar = new HBox();
        Label timeLbl = new Label(time);
        timeLbl.setStyle("-fx-background-color: #f5f3f3; -fx-padding: 4 8; -fx-background-radius: 4; -fx-font-weight: bold; -fx-font-size: 11px;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label statusLbl = new Label("● " + status);
        statusLbl.setStyle("-fx-text-fill: #006067; -fx-font-size: 11px; -fx-font-weight: bold;");
        if(status.equals("URGENCIA")) {
            statusLbl.setStyle("-fx-text-fill: #93000a; -fx-font-size: 11px; -fx-font-weight: bold;");
        }
        topBar.getChildren().addAll(timeLbl, spacer, statusLbl);
        
        Label icon = new Label(iconStr);
        icon.setStyle("-fx-font-size: 24px; -fx-alignment: center;");
        Label nameLbl = new Label(name != null ? name : "");
        nameLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-alignment: center;");
        Label motifLbl = new Label(motif != null ? motif : "");
        motifLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #6e797a; -fx-alignment: center; -fx-wrap-text: true;");
        
        VBox centerContent = new VBox(4, icon, nameLbl, motifLbl);
        centerContent.setAlignment(Pos.CENTER);
        
        card.getChildren().addAll(topBar, centerContent);
        return card;
    }
    
    @FXML
    public void onAgendarCita() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/society/NuevaCitaModal.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Agendar Nueva Cita");
            stage.setScene(new Scene(root, 400, 500));
            stage.showAndWait();
            
            renderizarCalendario();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
