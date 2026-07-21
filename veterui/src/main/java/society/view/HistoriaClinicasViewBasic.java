package society.view;

import society.dao.HistoriaClinicaDao;
import society.dao.PacienteDao;
import society.dao.CitaDao;
import society.dao.InventarioDao;
import society.dao.ServicioDao;
import society.modell.areamedica.HistoriaClinica;
import society.modell.recepcion.Cita;
import society.modell.recepcion.Paciente;
import society.modell.inventario.Inventario;
import society.modell.administracion.Servicio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HistoriaClinicasViewBasic extends JPanel {

    private Color brandBlue = new Color(0, 80, 100);
    private Color tealColor = new Color(0, 128, 128);
    private Color lightBg = new Color(248, 250, 250);
    private Color borderCol = new Color(220, 220, 220);

    private PacienteDao pacienteDao;
    private HistoriaClinicaDao historiaDao;
    private CitaDao citaDao;
    private InventarioDao inventarioDao;
    private ServicioDao servicioDao;

    private JPanel patientsListPanel;
    private JPanel mainContentPanel;
    
    // UI elements to update
    private JLabel lblNombre, lblInfo, lblPeso, lblTemp, lblVisita, lblEstado, lblAlergia;
    private JPanel alertasPanel;
    private JPanel visitasPanel;
    private JPanel evolucionesPanel;
    private JPanel tratamientosPanel;
    private JTable tablaExploraciones;
    private DefaultTableModel modeloExploraciones;
    private JScrollPane scrollExploraciones;
    private JTable tablaDiagnosticos;
    private DefaultTableModel modeloDiagnosticos;

    
    private Paciente pacienteSeleccionado;
    private HistoriaClinica historiaSeleccionada;

    public HistoriaClinicasViewBasic() {
        pacienteDao = new PacienteDao();
        historiaDao = new HistoriaClinicaDao();
        citaDao = new CitaDao();
        inventarioDao = new InventarioDao();
        servicioDao = new ServicioDao();

        setLayout(new BorderLayout(20, 0));
        setBackground(lightBg);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top Header
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setBackground(lightBg);
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(lightBg);
        
        JLabel title = new JLabel("Historial Clínico Digital");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(brandBlue);
        
        JLabel subtitle = new JLabel("Gestión centralizada de expedientes médicos y evoluciones.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        
        titlePanel.add(title);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitle);
        
        topHeader.add(titlePanel, BorderLayout.WEST);
        
        JPanel headerBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerBtns.setBackground(lightBg);
        
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(220, 53, 69));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(e -> {
            int row = getSelectedRowFromActiveTable();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un registro en las tablas de Exploración o Diagnóstico.");
                return;
            }
            int ans = JOptionPane.showConfirmDialog(this, "¿Eliminar este registro?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (ans == JOptionPane.YES_OPTION) eliminarRegistroActual(row);
        });
        
        JButton btnEditar = new JButton("Editar");
        btnEditar.setBackground(Color.WHITE);
        btnEditar.setForeground(brandBlue);
        btnEditar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnEditar.setFocusPainted(false);
        btnEditar.setBorder(BorderFactory.createLineBorder(brandBlue, 1));
        btnEditar.addActionListener(e -> {
            int row = getSelectedRowFromActiveTable();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un registro en las tablas de Exploración o Diagnóstico.");
                return;
            }
            showHistoriaDialog(row);
        });

        JButton btnNuevaHistoria = new JButton("+ Nueva Historia");
        btnNuevaHistoria.setBackground(brandBlue);
        btnNuevaHistoria.setForeground(Color.WHITE);
        btnNuevaHistoria.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnNuevaHistoria.setFocusPainted(false);
        btnNuevaHistoria.addActionListener(e -> showHistoriaDialog(-1));
        
        headerBtns.add(btnEditar);
        headerBtns.add(btnEliminar);
        headerBtns.add(btnNuevaHistoria);
        
        topHeader.add(headerBtns, BorderLayout.EAST);
        
        add(topHeader, BorderLayout.NORTH);

        // Center Split
        JPanel splitPanel = new JPanel(new BorderLayout(20, 0));
        splitPanel.setBackground(lightBg);
        splitPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        splitPanel.add(buildLeftSidebar(), BorderLayout.WEST);
        
        JScrollPane mainScroll = new JScrollPane(buildMainContent());
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        splitPanel.add(mainScroll, BorderLayout.CENTER);

        
        add(splitPanel, BorderLayout.CENTER);
        
        loadPatientsList();
    }

    private JPanel buildLeftSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout(0, 20));
        sidebar.setBackground(lightBg);
        sidebar.setPreferredSize(new Dimension(280, 0));
        
        // Pacientes
        JPanel pacientesCard = createCardPanel("Pacientes");
        
        patientsListPanel = new JPanel();
        patientsListPanel.setLayout(new BoxLayout(patientsListPanel, BoxLayout.Y_AXIS));
        patientsListPanel.setBackground(Color.WHITE);
        
        JScrollPane scroll = new JScrollPane(patientsListPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        pacientesCard.add(scroll, BorderLayout.CENTER);
        
        // Acciones Rápidas
        JPanel accionesCard = createCardPanel("ACCIONES RÁPIDAS");
        JPanel accionesList = new JPanel(new GridLayout(3, 1, 0, 10));
        accionesList.setBackground(Color.WHITE);
        
        accionesList.add(createActionButton("Nueva Evolución", "✏️", () -> showNuevaEvolucionDialog()));
        accionesList.add(createActionButton("Solicitar Laboratorio", "🔬", () -> JOptionPane.showMessageDialog(this, "Módulo de laboratorio en construcción.")));
        accionesList.add(createActionButton("Emitir Receta", "📋", () -> showEmitirRecetaDialog()));
        
        accionesCard.add(accionesList, BorderLayout.CENTER);
        
        sidebar.add(pacientesCard, BorderLayout.CENTER);
        sidebar.add(accionesCard, BorderLayout.SOUTH);
        
        return sidebar;
    }

    private JPanel buildMainContent() {
        mainContentPanel = new JPanel(new BorderLayout(0, 20));
        mainContentPanel.setBackground(lightBg);
        
        // Header Card (Profile)
        JPanel profileCard = createCardPanel("");
        profileCard.setLayout(new BorderLayout(20, 0));
        
        JLabel lblAvatar = new JLabel("🐾", SwingConstants.CENTER);
        lblAvatar.setFont(new Font("SansSerif", Font.PLAIN, 60));
        lblAvatar.setPreferredSize(new Dimension(100, 100));
        lblAvatar.setOpaque(true);
        lblAvatar.setBackground(new Color(240, 240, 240));
        lblAvatar.setBorder(new LineBorder(borderCol, 1, true));
        profileCard.add(lblAvatar, BorderLayout.WEST);
        
        JPanel infoCenter = new JPanel(new BorderLayout());
        infoCenter.setBackground(Color.WHITE);
        
        JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        nameRow.setBackground(Color.WHITE);
        lblNombre = new JLabel("Seleccione un paciente");
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 24));
        nameRow.add(lblNombre);
        
        lblInfo = new JLabel("");
        lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblInfo.setForeground(Color.GRAY);
        
        JPanel nameAndInfo = new JPanel();
        nameAndInfo.setLayout(new BoxLayout(nameAndInfo, BoxLayout.Y_AXIS));
        nameAndInfo.setBackground(Color.WHITE);
        nameAndInfo.add(nameRow);
        nameAndInfo.add(lblInfo);
        
        infoCenter.add(nameAndInfo, BorderLayout.NORTH);
        
        JPanel metricsRow = new JPanel(new GridLayout(1, 4, 10, 0));
        metricsRow.setBackground(Color.WHITE);
        metricsRow.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        lblPeso = new JLabel("-");
        lblTemp = new JLabel("38.5 °C"); // Fake static for now
        lblVisita = new JLabel("-");
        lblEstado = new JLabel("-");
        
        metricsRow.add(createMetric("PESO", lblPeso));
        metricsRow.add(createMetric("TEMPERATURA", lblTemp));
        metricsRow.add(createMetric("ÚLTIMA VISITA", lblVisita));
        metricsRow.add(createMetric("ESTADO", lblEstado));
        
        infoCenter.add(metricsRow, BorderLayout.CENTER);
        profileCard.add(infoCenter, BorderLayout.CENTER);
        
        JPanel alertPanelContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        alertPanelContainer.setBackground(Color.WHITE);
        alertasPanel = new JPanel(new BorderLayout());
        alertasPanel.setBackground(new Color(255, 230, 230));
        alertasPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 200, 200), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));
        lblAlergia = new JLabel("Alergia: Ninguna");
        lblAlergia.setForeground(new Color(180, 0, 0));
        lblAlergia.setFont(new Font("SansSerif", Font.BOLD, 12));
        alertasPanel.add(lblAlergia, BorderLayout.CENTER);
        alertasPanel.setVisible(false);
        alertPanelContainer.add(alertasPanel);
        profileCard.add(alertPanelContainer, BorderLayout.EAST);
        
        mainContentPanel.add(profileCard, BorderLayout.NORTH);
        
        // Body 3 Columns
        JPanel bodyCols = new JPanel(new GridLayout(1, 3, 20, 0));
        bodyCols.setBackground(lightBg);
        
        // Col 1: Historial de Visitas
        JPanel colVisitas = createCardPanel("🕒 Historial de Visitas");
        visitasPanel = new JPanel();
        visitasPanel.setLayout(new BoxLayout(visitasPanel, BoxLayout.Y_AXIS));
        visitasPanel.setBackground(Color.WHITE);
        JScrollPane scrollVis = new JScrollPane(visitasPanel);
        scrollVis.setBorder(null);
        colVisitas.add(scrollVis, BorderLayout.CENTER);
        
        // Col 2: Historial de Evolución
        JPanel colEvo = createCardPanel("📈 Historial de Evolución");
        evolucionesPanel = new JPanel();
        evolucionesPanel.setLayout(new BoxLayout(evolucionesPanel, BoxLayout.Y_AXIS));
        evolucionesPanel.setBackground(Color.WHITE);
        JScrollPane scrollEvo = new JScrollPane(evolucionesPanel);
        scrollEvo.setBorder(null);
        colEvo.add(scrollEvo, BorderLayout.CENTER);
        
        // Col 3: Tratamientos Activos + Peso
        JPanel colTratamientos = new JPanel(new GridLayout(2, 1, 0, 20));
        colTratamientos.setBackground(lightBg);
        
        JPanel tratCard = createCardPanel("💊 Tratamientos Activos");
        tratamientosPanel = new JPanel();
        tratamientosPanel.setLayout(new BoxLayout(tratamientosPanel, BoxLayout.Y_AXIS));
        tratamientosPanel.setBackground(Color.WHITE);
        JScrollPane scrollTrat = new JScrollPane(tratamientosPanel);
        scrollTrat.setBorder(null);
        tratCard.add(scrollTrat, BorderLayout.CENTER);
        
        JPanel pesoCard = createCardPanel("Tendencia de Peso");
        pesoCard.add(new TendenciaPesoChart(), BorderLayout.CENTER);
        
        colTratamientos.add(tratCard);
        colTratamientos.add(pesoCard);
        
        bodyCols.add(colVisitas);
        bodyCols.add(colEvo);
        bodyCols.add(colTratamientos);
        
        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setBackground(lightBg);
        
        bodyCols.setPreferredSize(new Dimension(800, 400));
        centerWrapper.add(bodyCols);
        centerWrapper.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // ── Sección 1: Exploración Física ─────────────────────────────────────
        JPanel colExplo = createCardPanel("🩺 Exploración Física");
        String[] colExploHeaders = {"FECHA", "MOTIVO", "PESO", "TEMP", "FC", "FR", "OBSERVACIONES"};
        modeloExploraciones = new DefaultTableModel(colExploHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaExploraciones = new JTable(modeloExploraciones);
        tablaExploraciones.setRowHeight(35);
        tablaExploraciones.setShowGrid(true);
        tablaExploraciones.setGridColor(new Color(230, 230, 230));
        tablaExploraciones.setIntercellSpacing(new Dimension(1, 1));
        tablaExploraciones.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tablaExploraciones.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        tablaExploraciones.getTableHeader().setBackground(new Color(245, 245, 245));
        tablaExploraciones.getTableHeader().setForeground(new Color(80, 80, 80));
        tablaExploraciones.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        tablaExploraciones.getColumnModel().getColumn(0).setPreferredWidth(90);
        tablaExploraciones.getColumnModel().getColumn(0).setMaxWidth(120);
        
        tablaExploraciones.getColumnModel().getColumn(1).setPreferredWidth(130);
        tablaExploraciones.getColumnModel().getColumn(1).setMaxWidth(200);
        
        tablaExploraciones.getColumnModel().getColumn(2).setPreferredWidth(60);
        tablaExploraciones.getColumnModel().getColumn(2).setMaxWidth(80);
        
        tablaExploraciones.getColumnModel().getColumn(3).setPreferredWidth(55);
        tablaExploraciones.getColumnModel().getColumn(3).setMaxWidth(70);
        
        tablaExploraciones.getColumnModel().getColumn(4).setPreferredWidth(45);
        tablaExploraciones.getColumnModel().getColumn(4).setMaxWidth(60);
        
        tablaExploraciones.getColumnModel().getColumn(5).setPreferredWidth(45);
        tablaExploraciones.getColumnModel().getColumn(5).setMaxWidth(60);
        
        tablaExploraciones.getColumnModel().getColumn(6).setPreferredWidth(300); // Observaciones takes the rest
        tablaExploraciones.setPreferredScrollableViewportSize(new Dimension(800, 160));
        
        scrollExploraciones = new JScrollPane(tablaExploraciones);
        scrollExploraciones.setBorder(BorderFactory.createEmptyBorder());
        scrollExploraciones.getViewport().setBackground(Color.WHITE);
        colExplo.add(scrollExploraciones, BorderLayout.CENTER);
        colExplo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200)); // Prevent excessive vertical stretch
        centerWrapper.add(colExplo);
        centerWrapper.add(Box.createRigidArea(new Dimension(0, 15)));

        // ── Sección 2: Diagnósticos Recientes ─────────────────────────────────
        JPanel colDiag = createCardPanel("🔬 Diagnósticos Recientes");
        String[] colDiagHeaders = {"FECHA", "NOMBRE DEL DIAGNÓSTICO", "DESCRIPCIÓN", "PRÓXIMO CONTROL"};
        modeloDiagnosticos = new DefaultTableModel(colDiagHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaDiagnosticos = new JTable(modeloDiagnosticos);
        tablaDiagnosticos.setRowHeight(35);
        tablaDiagnosticos.setShowGrid(true);
        tablaDiagnosticos.setGridColor(new Color(230, 230, 230));
        tablaDiagnosticos.setIntercellSpacing(new Dimension(1, 1));
        tablaDiagnosticos.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tablaDiagnosticos.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        tablaDiagnosticos.getTableHeader().setBackground(new Color(245, 245, 245));
        tablaDiagnosticos.getTableHeader().setForeground(new Color(80, 80, 80));
        tablaDiagnosticos.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        tablaDiagnosticos.getColumnModel().getColumn(0).setPreferredWidth(90);
        tablaDiagnosticos.getColumnModel().getColumn(0).setMaxWidth(120);
        
        tablaDiagnosticos.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaDiagnosticos.getColumnModel().getColumn(1).setMaxWidth(300);
        
        tablaDiagnosticos.getColumnModel().getColumn(2).setPreferredWidth(300); // Descripción takes the rest
        
        tablaDiagnosticos.getColumnModel().getColumn(3).setPreferredWidth(120);
        tablaDiagnosticos.getColumnModel().getColumn(3).setMaxWidth(150);
        
        tablaDiagnosticos.setPreferredScrollableViewportSize(new Dimension(800, 160));
        
        tablaExploraciones.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaExploraciones.getSelectedRow() != -1) {
                tablaDiagnosticos.clearSelection();
            }
        });
        tablaDiagnosticos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaDiagnosticos.getSelectedRow() != -1) {
                tablaExploraciones.clearSelection();
            }
        });
        
        JScrollPane scrollDiag = new JScrollPane(tablaDiagnosticos);
        scrollDiag.setBorder(BorderFactory.createEmptyBorder());
        scrollDiag.getViewport().setBackground(Color.WHITE);
        colDiag.add(scrollDiag, BorderLayout.CENTER);
        colDiag.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200)); // Prevent excessive vertical stretch
        centerWrapper.add(colDiag);
        
        // Add vertical glue to absorb all remaining empty space at the bottom, 
        // preventing the tables from being stretched downwards.
        centerWrapper.add(Box.createVerticalGlue());
        
        mainContentPanel.add(centerWrapper, BorderLayout.CENTER);
        
        return mainContentPanel;
    }

    private void loadPatientsList() {
        patientsListPanel.removeAll();
        List<Paciente> pacientes = pacienteDao.getAll();
        
        for (Paciente p : pacientes) {
            JPanel pItem = new JPanel(new BorderLayout(10, 0));
            pItem.setBackground(Color.WHITE);
            pItem.setBorder(new EmptyBorder(10, 10, 10, 10));
            pItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
            pItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            JLabel av = new JLabel("🐾");
            av.setFont(new Font("SansSerif", Font.PLAIN, 24));
            
            JPanel textP = new JPanel(new GridLayout(2, 1));
            textP.setBackground(Color.WHITE);
            JLabel name = new JLabel(p.getNombre());
            name.setFont(new Font("SansSerif", Font.BOLD, 14));
            
            JLabel sub = new JLabel(p.getRaza() + " • ID: " + p.getId());
            sub.setFont(new Font("SansSerif", Font.PLAIN, 11));
            sub.setForeground(Color.GRAY);
            
            textP.add(name);
            textP.add(sub);
            
            JLabel dot = new JLabel("●");
            dot.setForeground(p.getEstado() == Paciente.EstadoPaciente.ALTA ? new Color(0, 180, 0) : new Color(200, 150, 0));
            
            pItem.add(av, BorderLayout.WEST);
            pItem.add(textP, BorderLayout.CENTER);
            pItem.add(dot, BorderLayout.EAST);
            
            pItem.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectPatient(p);
                }
                @Override
                public void mouseEntered(MouseEvent e) { pItem.setBackground(new Color(240, 248, 250)); textP.setBackground(new Color(240, 248, 250)); }
                @Override
                public void mouseExited(MouseEvent e) { pItem.setBackground(Color.WHITE); textP.setBackground(Color.WHITE); }
            });
            
            patientsListPanel.add(pItem);
            patientsListPanel.add(new JSeparator());
        }
        
        patientsListPanel.revalidate();
        patientsListPanel.repaint();
    }

    private void selectPatient(Paciente p) {
        this.pacienteSeleccionado = p;
        Optional<HistoriaClinica> opt = historiaDao.findById(p.getId());

        if (opt.isPresent()) {
            this.historiaSeleccionada = opt.get();
        } else {
            // No existe: crear una nueva usando el ID del paciente como clave estable
            this.historiaSeleccionada = new HistoriaClinica();
            this.historiaSeleccionada.setId(p.getId()); // ID estable == ID del paciente
            this.historiaSeleccionada.setPaciente(p);
            this.historiaSeleccionada.setFechaApertura(LocalDate.now());
            this.historiaSeleccionada.setAlergias("");
            this.historiaSeleccionada.setAntecedentes("");
            this.historiaSeleccionada.setPesoActual(p.getPeso());
            this.historiaSeleccionada.setEvoluciones("");
            this.historiaSeleccionada.setTratamientos("");
            this.historiaSeleccionada.setHistorialPesos("");
            historiaDao.saveOrUpdate(this.historiaSeleccionada);
        }
        
        // Update header
        lblNombre.setText(p.getNombre());
        lblInfo.setText((p.getRaza()!=null?p.getRaza():"-") + " • " + (p.getSexo()!=null?p.getSexo():"-") + " • " + (p.getEdadAproximada()!=null?p.getEdadAproximada():"-"));
        lblPeso.setText(historiaSeleccionada.getPesoActual() + " kg");
        lblVisita.setText(p.getUltimaVisita() != null ? p.getUltimaVisita() : "-");
        lblEstado.setText(p.getEstado().toString());
        
        if (historiaSeleccionada.getAlergias() != null && !historiaSeleccionada.getAlergias().trim().isEmpty() && !historiaSeleccionada.getAlergias().equalsIgnoreCase("Ninguna")) {
            lblAlergia.setText("Alergia: " + historiaSeleccionada.getAlergias());
            alertasPanel.setVisible(true);
        } else {
            alertasPanel.setVisible(false);
        }
        
        // Update Visitas (From Citas)
        visitasPanel.removeAll();
        List<Cita> citasPaciente = citaDao.getAll().stream()
            .filter(c -> c.getPacienteId() == p.getId())
            .sorted((c1, c2) -> c2.getFechaHora().compareTo(c1.getFechaHora()))
            .collect(Collectors.toList());
            
        if (citasPaciente.isEmpty()) {
            visitasPanel.add(new JLabel("No hay visitas registradas."));
        } else {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
            for (Cita c : citasPaciente) {
                visitasPanel.add(createTimelineItem(c.getMotivo(), c.getFechaHora().format(df), "Atendido por el equipo veterinario. Estado: " + c.getEstado(), brandBlue));
            }
        }
        
        // Update Evoluciones
        evolucionesPanel.removeAll();
        String evoStr = historiaSeleccionada.getEvoluciones();
        if (evoStr == null || evoStr.trim().isEmpty()) {
            evolucionesPanel.add(new JLabel("No hay evoluciones."));
        } else {
            String[] evos = evoStr.split("\\|");
            for (String e : evos) {
                String[] parts = e.split(";");
                if (parts.length >= 3) {
                    evolucionesPanel.add(createTimelineItem(parts[0], parts[1], parts[2], tealColor));
                }
            }
        }
        
        // Update Tratamientos
        tratamientosPanel.removeAll();
        String tratStr = historiaSeleccionada.getTratamientos();
        if (tratStr == null || tratStr.trim().isEmpty()) {
            tratamientosPanel.add(new JLabel("No hay tratamientos activos."));
        } else {
            String[] trats = tratStr.split("\\|");
            for (String t : trats) {
                String[] parts = t.split(";");
                if (parts.length >= 2) {
                    tratamientosPanel.add(createTratamientoCard(parts[0], parts[1]));
                }
            }
        }
        
        // Update Peso Chart
        if (historiaSeleccionada != null) {
            mainContentPanel.repaint();
        }
        
        // Update Exploraciones y Diagnósticos (2 tablas separadas)
        // Formato de cada entrada: "FECHA~MOTIVO~EXPLORACION_RAW~DIAG_RAW~PROXCTRL~V"
        // Las entradas se separan por "~V|" (fin de registro + separador)
        // EXPLORACION_RAW:  "Peso: Xkg, Temp: X, FC: X, FR: X | Obs: texto"
        // DIAG_RAW:         "CODIGO | Nombre del diagnóstico"
        modeloExploraciones.setRowCount(0);
        modeloDiagnosticos.setRowCount(0);
        if (evoStr != null && !evoStr.trim().isEmpty()) {
            // Normalizar: el último registro termina en "~V" sin "|" al final
            String normalized = evoStr.trim();
            if (!normalized.endsWith("~V")) {
                // Ya tiene separadores internos — split seguro por el marcador de fin
                // cada registro completo es: FECHA~MOTIVO~EXPLO~DIAG~PROXCTRL~V
            }
            // Separar registros: cada uno termina en "~V" — split simple y robusto
            String[] partsRaw = normalized.split("~V");
            String[] registros = java.util.Arrays.stream(partsRaw)
                .map(s -> {
                    String clean = s.trim();
                    while (clean.startsWith("|")) {
                        clean = clean.substring(1).trim();
                    }
                    return clean;
                })
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
            for (String reg : registros) {
                String r = reg;
                if (r.isEmpty()) continue;
                String[] parts = r.split("~");
                if (parts.length < 3) continue;

                String fecha   = parts.length > 0 ? parts[0].trim() : "";
                String motivo  = parts.length > 1 ? parts[1].trim() : "";
                String exploRaw = parts.length > 2 ? parts[2].trim() : "";
                String diagRaw  = parts.length > 3 ? parts[3].trim() : "";
                String proxCtrl = parts.length > 4 ? parts[4].trim() : "";

                // Descomponer exploración: "Peso: Xkg, Temp: X, FC: X, FR: X | Obs: ..."
                String peso = ""; String temp = ""; String fc = ""; String fr = ""; String obs = "";
                if (exploRaw.contains(" | Obs:")) {
                    String[] expParts = exploRaw.split(" \\| Obs:", 2);
                    obs = expParts.length > 1 ? expParts[1].trim() : "";
                    String vitales = expParts[0].trim();
                    for (String seg : vitales.split(",")) {
                        seg = seg.trim();
                        if (seg.startsWith("Peso:"))  peso = seg.replace("Peso:", "").trim();
                        else if (seg.startsWith("Temp:")) temp = seg.replace("Temp:", "").trim();
                        else if (seg.startsWith("FC:"))   fc   = seg.replace("FC:", "").trim();
                        else if (seg.startsWith("FR:"))   fr   = seg.replace("FR:", "").trim();
                    }
                } else {
                    obs = exploRaw;
                }

                // Descomponer diagnóstico: "CODIGO | Nombre"
                String diagCod = ""; String diagNombre = "";
                if (diagRaw.contains(" | ")) {
                    String[] dp = diagRaw.split(" \\| ", 2);
                    diagCod    = dp[0].trim();
                    diagNombre = dp.length > 1 ? dp[1].trim() : "";
                } else {
                    diagNombre = diagRaw;
                }

                modeloExploraciones.addRow(new Object[]{fecha, motivo, peso, temp, fc, fr, obs});
                modeloDiagnosticos.addRow(new Object[]{fecha, diagNombre.isEmpty() ? diagCod : diagNombre,
                                                       diagCod, proxCtrl});
            }
        }
        
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private void showNuevaEvolucionDialog() {
        if (historiaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JTextField txtTitle = new JTextField();
        JTextArea txtDesc = new JTextArea(4, 20);
        Object[] message = {
            "Título de Evolución:", txtTitle,
            "Descripción Clínica:", new JScrollPane(txtDesc)
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Nueva Evolución", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String title = txtTitle.getText().replace(";", ",").replace("|", ",");
            String desc = txtDesc.getText().replace(";", ",").replace("|", ",");
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
            
            String current = historiaSeleccionada.getEvoluciones();
            String newEvo = title + ";" + date + ";" + desc;
            
            if (current == null || current.trim().isEmpty()) {
                historiaSeleccionada.setEvoluciones(newEvo);
            } else {
                historiaSeleccionada.setEvoluciones(newEvo + "|" + current);
            }
            
        // Guardar evolución (sin modificar el formato de exploraciones)
        try {
            historiaDao.saveOrUpdate(historiaSeleccionada);
            selectPatient(pacienteSeleccionado);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        }
    }
    
    private int getSelectedRowFromActiveTable() {
        if (tablaExploraciones != null && tablaExploraciones.getSelectedRow() != -1) {
            return tablaExploraciones.getSelectedRow();
        }
        if (tablaDiagnosticos != null && tablaDiagnosticos.getSelectedRow() != -1) {
            return tablaDiagnosticos.getSelectedRow();
        }
        return -1;
    }
    
    private void eliminarRegistroActual(int index) {
        if (historiaSeleccionada == null) return;
        
        String evoStr = historiaSeleccionada.getEvoluciones();
        if (evoStr == null || evoStr.trim().isEmpty()) return;
        
        String[] partsRaw = evoStr.trim().split("~V");
        java.util.List<String> registros = new java.util.ArrayList<>();
        for (String s : partsRaw) {
            String clean = s.trim();
            while (clean.startsWith("|")) {
                clean = clean.substring(1).trim();
            }
            if (!clean.isEmpty()) registros.add(clean);
        }
        
        if (index >= 0 && index < registros.size()) {
            registros.remove(index);
            
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < registros.size(); i++) {
                if (i > 0) sb.append(" | ");
                sb.append(registros.get(i)).append("~V");
            }
            
            historiaSeleccionada.setEvoluciones(sb.toString());
            try {
                historiaDao.saveOrUpdate(historiaSeleccionada);
                selectPatient(pacienteSeleccionado);
                JOptionPane.showMessageDialog(this, "Registro eliminado correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
            }
        }
    }
    
    private void showHistoriaDialog(int editIndex) {
        if (historiaSeleccionada == null || pacienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            editIndex >= 0 ? "Editar Entrada - " + pacienteSeleccionado.getNombre() : "Nueva Entrada - " + pacienteSeleccionado.getNombre(), 
            true);
        dialog.setSize(700, 650);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Header
        JLabel lblHeader = new JLabel(editIndex >= 0 ? "Editar Entrada" : "Nueva Entrada");
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblHeader.setForeground(brandBlue);
        panel.add(lblHeader, BorderLayout.NORTH);
        
        // Form Body
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        
        JTextArea txtMotivo = new JTextArea(3, 20);
        txtMotivo.setBorder(BorderFactory.createTitledBorder("Motivo de Consulta"));
        form.add(txtMotivo);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel pFisica = new JPanel(new GridLayout(2, 4, 10, 10));
        pFisica.setBackground(Color.WHITE);
        pFisica.setBorder(BorderFactory.createTitledBorder("Exploración Física"));
        JTextField txtPeso = new JTextField();
        JTextField txtTemp = new JTextField();
        JTextField txtFC = new JTextField();
        JTextField txtFR = new JTextField();
        pFisica.add(new JLabel("Peso (kg):")); pFisica.add(txtPeso);
        pFisica.add(new JLabel("Temp (°C):")); pFisica.add(txtTemp);
        pFisica.add(new JLabel("FC (LPM):")); pFisica.add(txtFC);
        pFisica.add(new JLabel("FR (RPM):")); pFisica.add(txtFR);
        
        JTextArea txtObs = new JTextArea(3, 20);
        txtObs.setBorder(BorderFactory.createTitledBorder("Observaciones Generales"));
        
        form.add(pFisica);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(txtObs);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel pDiag = new JPanel(new BorderLayout(5, 5));
        pDiag.setBackground(Color.WHITE);
        pDiag.setBorder(BorderFactory.createTitledBorder("Diagnóstico"));
        JTextField txtCIE = new JTextField();
        JTextArea txtDiagPre = new JTextArea(3, 20);
        txtDiagPre.setBorder(BorderFactory.createTitledBorder("Descripción del Diagnóstico"));
        pDiag.add(new JLabel("Nombre:"), BorderLayout.WEST);
        pDiag.add(txtCIE, BorderLayout.CENTER);
        pDiag.add(txtDiagPre, BorderLayout.SOUTH);
        form.add(pDiag);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel pControl = new JPanel(new BorderLayout());
        pControl.setBackground(Color.WHITE);
        pControl.setBorder(BorderFactory.createTitledBorder("Próximo Control"));
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd / MM / yyyy");
        pControl.add(dateChooser, BorderLayout.CENTER);
        form.add(pControl);
        
        // Cargar datos si es edición
        java.util.List<String> currentRegistros = new java.util.ArrayList<>();
        if (historiaSeleccionada != null && historiaSeleccionada.getEvoluciones() != null) {
            String[] partsRaw = historiaSeleccionada.getEvoluciones().trim().split("~V");
            for (String s : partsRaw) {
                String clean = s.trim();
                while (clean.startsWith("|")) {
                    clean = clean.substring(1).trim();
                }
                if (!clean.isEmpty()) currentRegistros.add(clean);
            }
        }
        
        String existingDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        if (editIndex >= 0 && editIndex < currentRegistros.size()) {
            String r = currentRegistros.get(editIndex);
            String[] parts = r.split("~");
            if (parts.length > 0) existingDate = parts[0];
            if (parts.length > 1) txtMotivo.setText(parts[1]);
            if (parts.length > 2) {
                String exploFisica = parts[2];
                String[] exParts = exploFisica.split("\\|");
                if (exParts.length > 0) {
                    String[] vTokens = exParts[0].trim().split(",");
                    for(String token : vTokens) {
                        if(token.contains("Peso:")) txtPeso.setText(token.replace("Peso:","").replace("kg","").trim());
                        if(token.contains("Temp:")) txtTemp.setText(token.replace("Temp:","").trim());
                        if(token.contains("FC:")) txtFC.setText(token.replace("FC:","").trim());
                        if(token.contains("FR:")) txtFR.setText(token.replace("FR:","").trim());
                    }
                }
                if (exParts.length > 1) {
                    txtObs.setText(exParts[1].replace("Obs:", "").trim());
                }
            }
            if (parts.length > 3) {
                String diag = parts[3];
                String[] dParts = diag.split("\\|");
                if (dParts.length > 0) txtCIE.setText(dParts[0].trim());
                if (dParts.length > 1) txtDiagPre.setText(dParts[1].trim());
            }
            if (parts.length > 4) {
                try {
                    dateChooser.setDate(new SimpleDateFormat("dd / MM / yyyy").parse(parts[4].trim()));
                } catch(Exception ex) {}
            }
        }
        final String finalDate = existingDate;

        JScrollPane scrollForm = new JScrollPane(form);
        scrollForm.setBorder(null);
        panel.add(scrollForm, BorderLayout.CENTER);
        
        // Buttons
        JPanel pBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pBtns.setBackground(Color.WHITE);
        JButton btnGuardar = new JButton(editIndex >= 0 ? "Actualizar Entrada" : "Guardar Entrada");
        btnGuardar.setBackground(brandBlue);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> {
            String proxControl = "";
            if (dateChooser.getDate() != null) {
                proxControl = new SimpleDateFormat("dd / MM / yyyy").format(dateChooser.getDate());
            }
            
            String exploFisica = "Peso: " + txtPeso.getText() + "kg, Temp: " + txtTemp.getText() + ", FC: " + txtFC.getText() + ", FR: " + txtFR.getText() + " | Obs: " + txtObs.getText().replace("~","").replace("|","");
            String diagnostico = txtCIE.getText() + " | " + txtDiagPre.getText().replace("~","").replace("|","");
            
            String newEntry = finalDate + "~" + txtMotivo.getText().replace("~","").replace("|","") + "~" +
                              exploFisica + "~" + diagnostico + "~" + proxControl + "~V";
            
            if (editIndex >= 0 && editIndex < currentRegistros.size()) {
                currentRegistros.set(editIndex, newEntry.replace("~V", ""));
            } else {
                currentRegistros.add(0, newEntry.replace("~V", ""));
            }
            
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < currentRegistros.size(); i++) {
                if (i > 0) sb.append(" | ");
                sb.append(currentRegistros.get(i)).append("~V");
            }
            
            historiaSeleccionada.setEvoluciones(sb.toString());
            
            try {
                if (!txtPeso.getText().trim().isEmpty()) {
                    double peso = Double.parseDouble(txtPeso.getText().trim());
                    historiaSeleccionada.setPesoActual(peso);
                    
                    String month = LocalDate.now().format(DateTimeFormatter.ofPattern("MMM")).toUpperCase();
                    String curPesos = historiaSeleccionada.getHistorialPesos();
                    String newPesoEntry = month + ";" + peso;
                    if (curPesos == null || curPesos.trim().isEmpty()) {
                        historiaSeleccionada.setHistorialPesos(newPesoEntry);
                    } else {
                        // avoid duplicates in same month ideally, but keep simple
                        historiaSeleccionada.setHistorialPesos(curPesos + "|" + newPesoEntry);
                    }
                }
            } catch (Exception ex) {}
            
            try {
                historiaDao.saveOrUpdate(historiaSeleccionada);
                selectPatient(pacienteSeleccionado);
                dialog.dispose();
                javax.swing.JOptionPane.showMessageDialog(null,
                    "Entrada guardada correctamente.", "Guardado",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(null,
                    "Error al guardar: " + ex.getMessage(), "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
        
        pBtns.add(btnGuardar);
        panel.add(pBtns, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void showEmitirRecetaDialog() {
        if (historiaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JComboBox<String> cbCatalog = new JComboBox<>();
        cbCatalog.addItem("Seleccione del catálogo (Opcional)...");
        
        List<Inventario> prods = inventarioDao.getAll();
        for(Inventario i : prods) {
            cbCatalog.addItem("[Producto] " + i.getProducto());
        }
        
        List<Servicio> servs = servicioDao.getAll();
        for(Servicio s : servs) {
            cbCatalog.addItem("[Servicio] " + s.getNombre());
        }
        
        JTextField txtMed = new JTextField();
        JTextArea txtDosis = new JTextArea(3, 20);
        txtDosis.setLineWrap(true);
        txtDosis.setWrapStyleWord(true);
        
        cbCatalog.addActionListener(e -> {
            if(cbCatalog.getSelectedIndex() > 0) {
                String sel = (String) cbCatalog.getSelectedItem();
                sel = sel.replace("[Producto] ", "").replace("[Servicio] ", "");
                txtMed.setText(sel);
            }
        });
        
        Object[] message = {
            "Catálogo:", cbCatalog,
            "Medicamento/Servicio (Manual):", txtMed,
            "Indicaciones y Dosis:", new JScrollPane(txtDosis)
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Emitir Receta / Tratamiento", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String med = txtMed.getText().replace(";", ",").replace("|", ",");
            String dosis = txtDosis.getText().replace(";", ",").replace("|", ",");
            
            if(med.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar o seleccionar un medicamento/servicio.");
                return;
            }
            
            String current = historiaSeleccionada.getTratamientos();
            String newTrat = med + ";" + dosis;
            
            if (current == null || current.trim().isEmpty()) {
                historiaSeleccionada.setTratamientos(newTrat);
            } else {
                historiaSeleccionada.setTratamientos(newTrat + "|" + current);
            }
            
            try {
                historiaDao.saveOrUpdate(historiaSeleccionada);
                selectPatient(pacienteSeleccionado);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private JPanel createCardPanel(String titleStr) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(borderCol, 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        if (titleStr != null && !titleStr.isEmpty()) {
            JLabel lblTitle = new JLabel(titleStr);
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
            lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
            p.add(lblTitle, BorderLayout.NORTH);
        }
        return p;
    }

    private JPanel createActionButton(String text, String icon, Runnable action) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new LineBorder(borderCol, 1, true));
        p.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel lblIcon = new JLabel(icon, SwingConstants.CENTER);
        lblIcon.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblIcon.setPreferredSize(new Dimension(40, 40));
        lblIcon.setForeground(tealColor);
        
        JLabel lblText = new JLabel(text);
        lblText.setFont(new Font("SansSerif", Font.BOLD, 13));
        
        JLabel lblArrow = new JLabel(">");
        lblArrow.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblArrow.setForeground(Color.LIGHT_GRAY);
        lblArrow.setBorder(new EmptyBorder(0, 0, 0, 10));
        
        p.add(lblIcon, BorderLayout.WEST);
        p.add(lblText, BorderLayout.CENTER);
        p.add(lblArrow, BorderLayout.EAST);
        
        p.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { action.run(); }
            @Override
            public void mouseEntered(MouseEvent e) { p.setBackground(new Color(248, 250, 250)); }
            @Override
            public void mouseExited(MouseEvent e) { p.setBackground(Color.WHITE); }
        });
        
        return p;
    }

    private JPanel createMetric(String label, JLabel valueLabel) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setBackground(Color.WHITE);
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        lbl.setForeground(Color.GRAY);
        
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        p.add(lbl);
        p.add(valueLabel);
        return p;
    }

    private JPanel createTimelineItem(String title, String date, String desc, Color dotColor) {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(5, 0, 15, 0));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        JLabel dot = new JLabel("●");
        dot.setFont(new Font("SansSerif", Font.PLAIN, 18));
        dot.setForeground(dotColor);
        dot.setVerticalAlignment(SwingConstants.TOP);
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        
        JLabel lblDate = new JLabel(date);
        lblDate.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblDate.setForeground(Color.GRAY);
        
        JTextArea txtDesc = new JTextArea(desc);
        txtDesc.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtDesc.setForeground(Color.DARK_GRAY);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        txtDesc.setBackground(Color.WHITE);
        txtDesc.setBorder(null);
        
        content.add(lblTitle);
        content.add(lblDate);
        content.add(Box.createRigidArea(new Dimension(0, 5)));
        content.add(txtDesc);
        
        p.add(dot, BorderLayout.WEST);
        p.add(content, BorderLayout.CENTER);
        
        return p;
    }
    
    private JPanel createTratamientoCard(String name, String desc) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(245, 250, 255));
        p.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 220, 240), 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        JLabel lblTitle = new JLabel(name);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTitle.setForeground(brandBlue);
        
        JTextArea txtDesc = new JTextArea(desc);
        txtDesc.setFont(new Font("SansSerif", Font.PLAIN, 11));
        txtDesc.setForeground(Color.DARK_GRAY);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        txtDesc.setOpaque(false);
        txtDesc.setBorder(null);
        
        p.add(lblTitle, BorderLayout.NORTH);
        p.add(txtDesc, BorderLayout.CENTER);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(p, BorderLayout.CENTER);
        wrapper.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        return wrapper;
    }

    // Fake Chart for Tendencia de Peso
    class TendenciaPesoChart extends JPanel {
        public TendenciaPesoChart() {
            setBackground(Color.WHITE);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int width = getWidth();
            int height = getHeight();
            int barWidth = 30;
            
            java.util.List<String> labelsList = new java.util.ArrayList<>();
            java.util.List<Double> valuesList = new java.util.ArrayList<>();
            
            if (historiaSeleccionada != null && historiaSeleccionada.getHistorialPesos() != null && !historiaSeleccionada.getHistorialPesos().trim().isEmpty()) {
                String[] entries = historiaSeleccionada.getHistorialPesos().split("\\|");
                // Take up to last 5 entries
                int start = Math.max(0, entries.length - 5);
                for (int i = start; i < entries.length; i++) {
                    String[] parts = entries[i].split(";");
                    if (parts.length >= 2) {
                        labelsList.add(parts[0]);
                        try {
                            valuesList.add(Double.parseDouble(parts[1]));
                        } catch (Exception ex) {
                            valuesList.add(0.0);
                        }
                    }
                }
            }
            
            if (valuesList.isEmpty()) {
                g2.setColor(Color.GRAY);
                g2.drawString("No hay historial de pesos", 10, height / 2);
                return;
            }
            
            int count = valuesList.size();
            int gap = (width - (count * barWidth)) / (count + 1);
            
            double maxVal = 0;
            for(Double v : valuesList) if (v > maxVal) maxVal = v;
            if (maxVal == 0) maxVal = 100; // prevent div by zero
            
            g2.setColor(new Color(180, 220, 220));
            int x = gap;
            for (int i = 0; i < count; i++) {
                int barHeight = (int) ((valuesList.get(i) / maxVal) * (height - 30));
                int y = height - 25 - barHeight;
                g2.fillRoundRect(x, y, barWidth, barHeight, 10, 10);
                
                g2.setColor(Color.GRAY);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g2.drawString(labelsList.get(i), x + 5, height - 5);
                
                g2.setColor(brandBlue);
                g2.drawString(String.format("%.1f", valuesList.get(i)), x, y - 5);
                
                g2.setColor(new Color(180, 220, 220));
                
                x += barWidth + gap;
            }
        }
    }
}
