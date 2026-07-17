package society.view;

import society.modell.recepcion.Paciente;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class RegistroPacienteBasic extends JDialog {

    private JTextField txtNombre;
    private JComboBox<Paciente.EspecieAnimal> cbEspecie;
    private JTextField txtRaza;
    private JTextField txtFechaNac; // o edad
    private JRadioButton rbMacho;
    private JRadioButton rbHembra;
    private JTextField txtPeso;
    
    private JTextField txtNombreDueno;
    private JTextField txtDni;
    private JTextField txtTelDueno;
    private JTextField txtCorreo;
    private JTextField txtDireccion;
    
    private JTextArea txtAlergias;
    private JTextArea txtMotivo;
    
    private boolean saved = false;
    private Paciente paciente;

    public RegistroPacienteBasic(Frame parent) {
        super(parent, "Nuevo Paciente", true);
        setSize(900, 800);
        setLocationRelativeTo(parent);
        
        // Fondo general
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 248, 248)); // Gris muy claro
        
        // --- HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel lblTitle = new JLabel("Nuevo Paciente");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0, 80, 100));
        headerPanel.add(lblTitle, BorderLayout.WEST);
        
        // --- CONTENIDO CENTRAL (Scrollable) ---
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30));

        // -- SECCION 1: FOTO (izq) y MASCOTA (der) --
        JPanel topSection = new JPanel(new BorderLayout(20, 0));
        topSection.setOpaque(false);
        
        // Panel Foto
        JPanel panelFoto = createCardPanel();
        panelFoto.setPreferredSize(new Dimension(280, 280));
        panelFoto.setLayout(new GridBagLayout());
        
        JPanel dropArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(200, 200, 200));
                Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
                g2.setStroke(dashed);
                g2.drawOval(10, 10, getWidth() - 20, getHeight() - 20);
                g2.dispose();
            }
        };
        dropArea.setPreferredSize(new Dimension(160, 160));
        dropArea.setOpaque(false);
        dropArea.setLayout(new GridBagLayout());
        
        JLabel lblIconFoto = new JLabel("📷"); // Placeholder de icono
        lblIconFoto.setFont(new Font("SansSerif", Font.PLAIN, 40));
        lblIconFoto.setForeground(Color.GRAY);
        
        JLabel lblSubir = new JLabel("Subir foto del paciente");
        lblSubir.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblSubir.setForeground(Color.GRAY);
        
        GridBagConstraints gbcFoto = new GridBagConstraints();
        gbcFoto.gridy = 0; dropArea.add(lblIconFoto, gbcFoto);
        gbcFoto.gridy = 1; dropArea.add(lblSubir, gbcFoto);
        
        GridBagConstraints gbcCard1 = new GridBagConstraints();
        gbcCard1.gridy = 0; gbcCard1.insets = new Insets(0,0,10,0);
        panelFoto.add(dropArea, gbcCard1);
        
        JLabel lblFormatos = new JLabel("Formatos: JPG, PNG (Max 5MB)");
        lblFormatos.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblFormatos.setForeground(new Color(100, 120, 130));
        gbcCard1.gridy = 1; panelFoto.add(lblFormatos, gbcCard1);
        
        JLabel lblEliminar = new JLabel("Eliminar imagen");
        lblEliminar.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblEliminar.setForeground(new Color(0, 120, 140)); // Teal text
        lblEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbcCard1.gridy = 2; panelFoto.add(lblEliminar, gbcCard1);
        
        topSection.add(panelFoto, BorderLayout.WEST);
        
        // Panel Mascota
        JPanel panelMascota = createCardPanel();
        panelMascota.setLayout(new BorderLayout());
        
        JLabel titleMascota = new JLabel("🐾 Información de la Mascota");
        titleMascota.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleMascota.setForeground(new Color(0, 80, 100));
        titleMascota.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JPanel gridMascota = new JPanel(new GridLayout(3, 2, 20, 20));
        gridMascota.setOpaque(false);
        
        txtNombre = createUnderlineTextField();
        cbEspecie = new JComboBox<>(Paciente.EspecieAnimal.values());
        cbEspecie.setBackground(Color.WHITE);
        
        txtRaza = createUnderlineTextField();
        txtFechaNac = createUnderlineTextField();
        
        JPanel panelSexo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelSexo.setOpaque(false);
        rbMacho = new JRadioButton("Macho");
        rbHembra = new JRadioButton("Hembra");
        rbMacho.setOpaque(false); rbHembra.setOpaque(false);
        ButtonGroup bgSexo = new ButtonGroup();
        bgSexo.add(rbMacho); bgSexo.add(rbHembra);
        panelSexo.add(rbMacho); panelSexo.add(rbHembra);
        
        txtPeso = createUnderlineTextField();
        
        gridMascota.add(wrapField("NOMBRE DE LA MASCOTA", txtNombre));
        gridMascota.add(wrapField("ESPECIE", cbEspecie));
        gridMascota.add(wrapField("RAZA", txtRaza));
        gridMascota.add(wrapField("FECHA DE NACIMIENTO / EDAD", txtFechaNac));
        gridMascota.add(wrapField("SEXO", panelSexo));
        gridMascota.add(wrapField("PESO (KG)", txtPeso));
        
        panelMascota.add(titleMascota, BorderLayout.NORTH);
        panelMascota.add(gridMascota, BorderLayout.CENTER);
        
        topSection.add(panelMascota, BorderLayout.CENTER);
        contentPanel.add(topSection);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // -- SECCION 2: DUEÑO --
        JPanel panelDueno = createCardPanel();
        panelDueno.setLayout(new BorderLayout());
        
        JPanel headerDueno = new JPanel(new BorderLayout());
        headerDueno.setOpaque(false);
        JLabel titleDueno = new JLabel("👤 Información del Dueño");
        titleDueno.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleDueno.setForeground(new Color(0, 80, 100));
        titleDueno.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        headerDueno.add(titleDueno, BorderLayout.WEST);
        
        // Search bar (simulada)
        JTextField txtBuscar = new JTextField();
        txtBuscar.setPreferredSize(new Dimension(250, 30));
        txtBuscar.setBackground(new Color(240, 240, 240));
        txtBuscar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        searchPanel.add(txtBuscar);
        headerDueno.add(searchPanel, BorderLayout.EAST);
        
        JPanel gridDueno = new JPanel(new GridLayout(2, 3, 20, 20));
        gridDueno.setOpaque(false);
        
        txtNombreDueno = createUnderlineTextField();
        txtDni = createUnderlineTextField();
        txtTelDueno = createUnderlineTextField();
        txtCorreo = createUnderlineTextField();
        txtDireccion = createUnderlineTextField();
        
        gridDueno.add(wrapField("NOMBRE COMPLETO", txtNombreDueno));
        gridDueno.add(wrapField("DNI / ID", txtDni));
        gridDueno.add(wrapField("TELÉFONO", txtTelDueno));
        gridDueno.add(wrapField("CORREO ELECTRÓNICO", txtCorreo));
        
        // La dirección ocupa las dos columnas restantes
        JPanel panelDir = wrapField("DIRECCIÓN", txtDireccion);
        GridBagConstraints gbcDir = new GridBagConstraints();
        gbcDir.gridwidth = 2; gbcDir.fill = GridBagConstraints.HORIZONTAL;
        JPanel dirContainer = new JPanel(new GridBagLayout());
        dirContainer.setOpaque(false);
        gbcDir.weightx = 1.0;
        dirContainer.add(panelDir, gbcDir);
        gridDueno.add(dirContainer);
        
        panelDueno.add(headerDueno, BorderLayout.NORTH);
        panelDueno.add(gridDueno, BorderLayout.CENTER);
        
        contentPanel.add(panelDueno);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // -- SECCION 3: DETALLES MEDICOS --
        JPanel panelMedico = createCardPanel();
        panelMedico.setLayout(new BorderLayout());
        
        JLabel titleMedico = new JLabel("📋 Detalles Médicos Iniciales");
        titleMedico.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleMedico.setForeground(new Color(0, 80, 100));
        titleMedico.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JPanel gridMedico = new JPanel(new GridLayout(1, 2, 20, 20));
        gridMedico.setOpaque(false);
        
        txtAlergias = new JTextArea();
        txtAlergias.setForeground(Color.BLACK);
        txtAlergias.setLineWrap(true); txtAlergias.setWrapStyleWord(true);
        txtAlergias.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JScrollPane scrollAlergias = new JScrollPane(txtAlergias);
        scrollAlergias.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));
        scrollAlergias.setPreferredSize(new Dimension(300, 80));
        
        txtMotivo = new JTextArea();
        txtMotivo.setForeground(Color.BLACK);
        txtMotivo.setLineWrap(true); txtMotivo.setWrapStyleWord(true);
        txtMotivo.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JScrollPane scrollMotivo = new JScrollPane(txtMotivo);
        scrollMotivo.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));
        
        JPanel alergiasWrapper = wrapField("ALERGIAS CONOCIDAS", scrollAlergias);
        JLabel infoAlergias = new JLabel("ⓘ Dejar en blanco si no se conocen.");
        infoAlergias.setFont(new Font("SansSerif", Font.PLAIN, 9));
        infoAlergias.setForeground(Color.GRAY);
        alergiasWrapper.add(infoAlergias, BorderLayout.SOUTH);
        
        gridMedico.add(alergiasWrapper);
        gridMedico.add(wrapField("MOTIVO DE LA PRIMERA CONSULTA", scrollMotivo));
        
        panelMedico.add(titleMedico, BorderLayout.NORTH);
        panelMedico.add(gridMedico, BorderLayout.CENTER);
        
        contentPanel.add(panelMedico);
        
        JScrollPane mainScroll = new JScrollPane(contentPanel);
        mainScroll.setBorder(null);
        mainScroll.getViewport().setOpaque(false);
        mainScroll.setOpaque(false);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(mainScroll, BorderLayout.CENTER);
        
        // --- FOOTER ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));
        
        JButton btnCancelar = new JButton("X Cancelar");
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setForeground(new Color(0, 80, 100));
        btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCancelar.setFocusPainted(false);
        // Borde punteado
        btnCancelar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createDashedBorder(new Color(150, 180, 255), 2.0f, 4.0f, 2.0f, true),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        
        JButton btnGuardar = new JButton("💾 Guardar Registro");
        btnGuardar.setBackground(new Color(0, 100, 100)); // Teal
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 100, 100), 1, true),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        
        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> guardar());
        
        footerPanel.add(btnCancelar);
        footerPanel.add(btnGuardar);
        
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createCardPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                g2.setColor(new Color(220, 220, 220)); // Borde
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }
    
    private JTextField createUnderlineTextField() {
        JTextField tf = new JTextField();
        tf.setForeground(Color.BLACK);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 0, 5, 0)
        ));
        tf.setOpaque(false);
        tf.setFont(new Font("SansSerif", Font.BOLD, 14));
        return tf;
    }
    
    private JPanel wrapField(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        lbl.setForeground(new Color(120, 120, 120));
        p.add(lbl, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private void guardar() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre del paciente es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (paciente == null) {
            paciente = new Paciente();
            paciente.setId(new Random().nextInt(10000));
            paciente.setUltimaVisita("Hoy, " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
            paciente.setEstado(Paciente.EstadoPaciente.EN_CLINICA); // Por defecto
        }

        paciente.setNombre(txtNombre.getText().trim());
        paciente.setEspecie((Paciente.EspecieAnimal) cbEspecie.getSelectedItem());
        
        paciente.setRaza(txtRaza.getText().trim());
        paciente.setEdadAproximada(txtFechaNac.getText().trim());
        
        if (rbMacho.isSelected()) paciente.setSexo("Macho");
        else if (rbHembra.isSelected()) paciente.setSexo("Hembra");
        else paciente.setSexo("Desconocido");
        
        paciente.setNombreDueno(txtNombreDueno.getText().trim());
        paciente.setTelefonoDueno(txtTelDueno.getText().trim());

        saved = true;
        dispose();
    }

    public void setPacienteToEdit(Paciente p) {
        this.paciente = p;
        setTitle("Editar Paciente");
        
        txtNombre.setText(p.getNombre());
        txtNombre.setForeground(Color.BLACK);
        
        cbEspecie.setSelectedItem(p.getEspecie());
        
        if (p.getRaza() != null) {
            txtRaza.setText(p.getRaza());
        }
        
        if (p.getEdadAproximada() != null) {
            txtFechaNac.setText(p.getEdadAproximada());
        }
        
        if ("Macho".equals(p.getSexo())) rbMacho.setSelected(true);
        else if ("Hembra".equals(p.getSexo())) rbHembra.setSelected(true);
        
        if (p.getNombreDueno() != null) {
            txtNombreDueno.setText(p.getNombreDueno());
        }
        
        if (p.getTelefonoDueno() != null) {
            txtTelDueno.setText(p.getTelefonoDueno());
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public Paciente getPaciente() {
        return paciente;
    }
}
