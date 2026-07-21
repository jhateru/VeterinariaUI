package society.view;

import society.modell.recepcion.Paciente;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class RegistroPacienteBasic extends JDialog {

    private JTextField txtNombre;
    private JComboBox<String> cbEspecie;
    private JTextField txtRaza;
    private JDateChooser dcFechaNac;
    private JComboBox<String> cbSexo;
    private JTextField txtPeso;
    
    private JComboBox<DuenoItem> cbDueno;
    private java.util.List<society.modell.recepcion.Dueno> duenosList;
    
    class DuenoItem {
        society.modell.recepcion.Dueno d;
        DuenoItem(society.modell.recepcion.Dueno d) { this.d = d; }
        @Override public String toString() { 
            return d.getId() + " - " + d.getNombre() + " " + (d.getApellidos() != null ? d.getApellidos() : ""); 
        }
    }
    
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

        // -- SECCION 1: MASCOTA --
        JPanel topSection = new JPanel(new BorderLayout(20, 0));
        topSection.setOpaque(false);
        
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
        cbEspecie = new JComboBox<>(new String[]{"PERRO", "GATO", "EXOTICO"});
        cbEspecie.setBackground(Color.WHITE);
        
        txtRaza = createUnderlineTextField();
        dcFechaNac = new JDateChooser();
        dcFechaNac.setDateFormatString("dd/MM/yyyy");
        
        cbSexo = new JComboBox<>(new String[]{"Seleccionar...", "Macho", "Hembra", "Desconocido"});
        cbSexo.setBackground(Color.WHITE);
        
        txtPeso = createUnderlineTextField();
        
        gridMascota.add(wrapField("NOMBRE DE LA MASCOTA", txtNombre));
        gridMascota.add(wrapField("ESPECIE", cbEspecie));
        gridMascota.add(wrapField("RAZA", txtRaza));
        gridMascota.add(wrapField("FECHA DE NACIMIENTO", dcFechaNac));
        gridMascota.add(wrapField("SEXO", cbSexo));
        gridMascota.add(wrapField("PESO (KG)", txtPeso));
        
        panelMascota.add(titleMascota, BorderLayout.NORTH);
        panelMascota.add(gridMascota, BorderLayout.CENTER);
        
        topSection.add(panelMascota, BorderLayout.CENTER);
        contentPanel.add(topSection);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // -- SECCION 2: DUEÑO --
        JPanel panelDueno = createCardPanel();
        panelDueno.setLayout(new BorderLayout());
        
        JLabel titleDueno = new JLabel("👤 Selección del Dueño");
        titleDueno.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleDueno.setForeground(new Color(0, 80, 100));
        titleDueno.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JPanel gridDueno = new JPanel(new GridLayout(1, 1, 20, 20));
        gridDueno.setOpaque(false);
        
        cbDueno = new JComboBox<>();
        cbDueno.setBackground(Color.WHITE);
        
        society.dao.DuenoDao dDao = new society.dao.DuenoDao();
        duenosList = dDao.getAll();
        if (duenosList != null) {
            for(society.modell.recepcion.Dueno d : duenosList) {
                cbDueno.addItem(new DuenoItem(d));
            }
        }
        
        gridDueno.add(wrapField("SELECCIONAR DUEÑO", cbDueno));
        
        panelDueno.add(titleDueno, BorderLayout.NORTH);
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
        paciente.setEspecie(Paciente.EspecieAnimal.valueOf(cbEspecie.getSelectedItem().toString()));
        try {
            if (!txtPeso.getText().trim().isEmpty()) {
                paciente.setPeso(Double.parseDouble(txtPeso.getText().trim()));
            }
        } catch(Exception ex) {}
        
        paciente.setRaza(txtRaza.getText().trim());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        paciente.setEdadAproximada(dcFechaNac.getDate() != null ? sdf.format(dcFechaNac.getDate()) : "");
        
        paciente.setSexo(cbSexo.getSelectedItem().toString());
        
        if (cbDueno.getSelectedItem() != null) {
            DuenoItem sel = (DuenoItem) cbDueno.getSelectedItem();
            paciente.setDuenoId(sel.d.getId());
        }

        saved = true;
        dispose();
    }

    public void setPacienteToEdit(Paciente p) {
        this.paciente = p;
        setTitle("Editar Paciente");
        
        txtNombre.setText(p.getNombre());
        txtNombre.setForeground(Color.BLACK);
        
        if (p.getEspecie() != null) {
            cbEspecie.setSelectedItem(p.getEspecie().name());
        }
        if (p.getPeso() > 0) {
            txtPeso.setText(String.valueOf(p.getPeso()));
        }
        
        if (p.getRaza() != null) {
            txtRaza.setText(p.getRaza());
        }
        
        if (p.getSexo() != null) {
            cbSexo.setSelectedItem(p.getSexo());
        }
        
        if (p.getDuenoId() > 0) {
            for(int i = 0; i < cbDueno.getItemCount(); i++) {
                if (cbDueno.getItemAt(i).d.getId() == p.getDuenoId()) {
                    cbDueno.setSelectedIndex(i);
                    break;
                }
            }
        }
    }
    
    public void preseleccionarDueno(society.modell.recepcion.Dueno d) {
        if (d != null) {
            for(int i = 0; i < cbDueno.getItemCount(); i++) {
                if (cbDueno.getItemAt(i).d.getId() == d.getId()) {
                    cbDueno.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public Paciente getPaciente() {
        return paciente;
    }
}
