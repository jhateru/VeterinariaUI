package society.view;

import society.modell.recepcion.Dueno;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Random;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistroDuenoBasic extends JDialog {

    private boolean saved = false;
    private Dueno dueno;

    // Fields
    private JTextField txtNombreCompleto;
    private JTextField txtDni;
    private JComboBox<String> cbGenero;
    private JDateChooser dcFechaNacimiento;

    private JTextField txtTelefonoPrincipal;
    private JTextField txtEmail;
    private JTextField txtTelefonoEmergencia;

    private JTextField txtDireccion;
    private JTextField txtCiudad;
    private JTextField txtCodigoPostal;

    public RegistroDuenoBasic(Frame parent) {
        super(parent, "Nuevo Registro", true);
        setSize(750, 750);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(Color.WHITE);

        // --- TOP HEADER ---
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblBreadcrumb = new JLabel("<html><span style='color:gray; font-size:10px;'>RECEPCIÓN / GESTIÓN DE DUEÑOS / <b style='color:#006464;'>NUEVO REGISTRO</b></span></html>");
        
        JLabel lblTitle = new JLabel("Información del Propietario");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setForeground(new Color(40, 50, 60));
        
        JLabel lblSubtitle = new JLabel("Complete los campos obligatorios para registrar un nuevo perfil en el sistema.");
        lblSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSubtitle.setForeground(Color.GRAY);

        topPanel.add(lblBreadcrumb);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(lblTitle);
        topPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        topPanel.add(lblSubtitle);
        
        add(topPanel, BorderLayout.NORTH);

        // --- MAIN SCROLL AREA ---
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(0, 25, 10, 25));

        // 1. Datos Personales
        JPanel pnlDatosPersonales = createSectionPanel("👤 Datos Personales");
        
        JPanel gridDP = new JPanel(new GridLayout(2, 2, 20, 15));
        gridDP.setBackground(Color.WHITE);
        
        txtNombreCompleto = createTextField();
        txtDni = createTextField();
        cbGenero = new JComboBox<>(new String[]{"Seleccionar...", "Masculino", "Femenino", "Otro"});
        cbGenero.setBackground(Color.WHITE);
        dcFechaNacimiento = new JDateChooser();
        dcFechaNacimiento.setDateFormatString("dd/MM/yyyy");
        
        gridDP.add(createFieldContainer("Nombre Completo *", txtNombreCompleto));
        gridDP.add(createFieldContainer("Documento de Identidad (DNI/Cédula) *", txtDni));
        gridDP.add(createFieldContainer("Género", cbGenero));
        gridDP.add(createFieldContainer("Fecha de Nacimiento", dcFechaNacimiento));
        
        pnlDatosPersonales.add(gridDP, BorderLayout.CENTER);
        mainPanel.add(pnlDatosPersonales);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 2. Información de Contacto
        JPanel pnlContacto = createSectionPanel("📇 Información de Contacto");
        
        JPanel gridContacto = new JPanel(new GridLayout(1, 3, 20, 15));
        gridContacto.setBackground(Color.WHITE);
        
        txtTelefonoPrincipal = createTextField();
        txtEmail = createTextField();
        txtTelefonoEmergencia = createTextField();
        
        gridContacto.add(createFieldContainer("Teléfono Principal *", txtTelefonoPrincipal));
        gridContacto.add(createFieldContainer("Correo Electrónico *", txtEmail));
        gridContacto.add(createFieldContainer("Teléfono de Emergencia", txtTelefonoEmergencia));
        
        pnlContacto.add(gridContacto, BorderLayout.CENTER);
        mainPanel.add(pnlContacto);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 3. Dirección de Residencia
        JPanel pnlDireccion = createSectionPanel("📍 Dirección de Residencia");
        
        JPanel gridDireccion = new JPanel(new GridLayout(1, 3, 20, 15));
        gridDireccion.setBackground(Color.WHITE);
        
        txtDireccion = createTextField();
        txtCiudad = createTextField();
        txtCodigoPostal = createTextField();
        
        gridDireccion.add(createFieldContainer("Dirección (Calle y Número)", txtDireccion));
        gridDireccion.add(createFieldContainer("Ciudad", txtCiudad));
        gridDireccion.add(createFieldContainer("Código Postal", txtCodigoPostal));
        
        JPanel dirCenter = new JPanel(new BorderLayout(0, 15));
        dirCenter.setBackground(Color.WHITE);
        dirCenter.add(gridDireccion, BorderLayout.CENTER);
        
        // Info Box
        JPanel infoBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoBox.setBackground(new Color(245, 245, 245));
        infoBox.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));
        JLabel lblInfo = new JLabel(" ℹ️ Esta información se utilizará para facturación y envío de recordatorios físicos si es necesario.");
        lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblInfo.setForeground(Color.DARK_GRAY);
        infoBox.add(lblInfo);
        
        dirCenter.add(infoBox, BorderLayout.SOUTH);
        pnlDireccion.add(dirCenter, BorderLayout.CENTER);
        mainPanel.add(pnlDireccion);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // --- FOOTER BUTTONS ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        
        JButton btnGuardar = new JButton("💾 Guardar Dueño");
        btnGuardar.setBackground(new Color(0, 100, 100)); // Dark Teal
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(150, 40));
        btnCancelar.setPreferredSize(new Dimension(100, 40));

        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> guardar());

        footerPanel.add(btnCancelar);
        footerPanel.add(btnGuardar);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(15, 20, 20, 20)
        ));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblTitle.setForeground(new Color(40, 60, 80));
        
        // Custom left border for title
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        JPanel leftBorder = new JPanel();
        leftBorder.setBackground(new Color(0, 120, 120));
        leftBorder.setPreferredSize(new Dimension(4, 20));
        titlePanel.add(leftBorder, BorderLayout.WEST);
        
        JLabel paddedTitle = new JLabel("  " + title);
        paddedTitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        titlePanel.add(paddedTitle, BorderLayout.CENTER);
        
        panel.add(titlePanel, BorderLayout.NORTH);
        return panel;
    }

    private JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            new EmptyBorder(5, 5, 5, 5)
        ));
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return tf;
    }

    private JPanel createFieldContainer(String labelText, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setBackground(Color.WHITE);
        
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(new Color(80, 80, 80));
        
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        
        // Hack for JDateChooser border
        if (field instanceof JDateChooser) {
            field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        }
        
        return p;
    }

    private void guardar() {
        if (txtNombreCompleto.getText().trim().isEmpty() || txtDni.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre y el DNI son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (dueno == null) {
            dueno = new Dueno();
            dueno.setId(new Random().nextInt(100000));
        }

        dueno.setNombre(txtNombreCompleto.getText().trim());
        dueno.setDni(txtDni.getText().trim());
        
        if (cbGenero.getSelectedIndex() > 0) {
            dueno.setGenero(cbGenero.getSelectedItem().toString());
        }
        
        if (dcFechaNacimiento.getDate() != null) {
            dueno.setFechaNacimiento(new SimpleDateFormat("dd/MM/yyyy").format(dcFechaNacimiento.getDate()));
        }
        
        dueno.setTelefono(txtTelefonoPrincipal.getText().trim());
        dueno.setEmail(txtEmail.getText().trim());
        dueno.setTelefonoEmergencia(txtTelefonoEmergencia.getText().trim());
        
        dueno.setDireccion(txtDireccion.getText().trim());
        dueno.setCiudad(txtCiudad.getText().trim());
        dueno.setCodigoPostal(txtCodigoPostal.getText().trim());

        saved = true;
        dispose();
    }

    public void setDuenoToEdit(Dueno d) {
        this.dueno = d;
        setTitle("Editar Propietario");

        txtNombreCompleto.setText(d.getNombre());
        txtDni.setText(d.getDni());
        
        if (d.getGenero() != null) {
            cbGenero.setSelectedItem(d.getGenero());
        }
        
        if (d.getFechaNacimiento() != null && !d.getFechaNacimiento().isEmpty()) {
            try {
                dcFechaNacimiento.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(d.getFechaNacimiento()));
            } catch (Exception e) {}
        }
        
        txtTelefonoPrincipal.setText(d.getTelefono());
        txtEmail.setText(d.getEmail());
        txtTelefonoEmergencia.setText(d.getTelefonoEmergencia());
        
        txtDireccion.setText(d.getDireccion());
        txtCiudad.setText(d.getCiudad());
        txtCodigoPostal.setText(d.getCodigoPostal());
    }

    public boolean isSaved() {
        return saved;
    }

    public Dueno getDueno() {
        return dueno;
    }
}
