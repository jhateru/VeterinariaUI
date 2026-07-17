package society.view;

import society.modell.administracion.Personal;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class RegistroPersonalBasic extends JDialog {

    private boolean saved = false;
    private Personal nuevoPersonal;

    // Fields
    private JTextField txtNombre;
    private JTextField txtDni;
    private JTextField txtFechaNac;
    private JComboBox<String> cbGenero;
    private JTextField txtTelefono;
    private JTextField txtEmail;
    private JTextField txtDireccion;

    private JComboBox<String> cbRolPrincipal;
    private JTextField txtEspecialidad;
    private JTextField txtColegiado;
    private JTextField txtFechaContratacion;

    private JTextField txtUsername;
    private JComboBox<String> cbRolSistema;

    public RegistroPersonalBasic(Frame parent) {
        super(parent, "Registro de Nuevo Empleado", true);
        setSize(900, 750);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(15, 15));
        
        // --- HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel title = new JLabel("Registro de Nuevo Empleado");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(new JSeparator(), BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // --- MAIN CONTENT ---
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        // LEFT COLUMN
        JPanel leftCol = new JPanel();
        leftCol.setLayout(new BoxLayout(leftCol, BoxLayout.Y_AXIS));
        leftCol.setPreferredSize(new Dimension(300, 0));

        // Foto de Perfil
        JPanel fotoPanel = createSectionPanel("Foto de Perfil");
        JLabel lblFoto = new JLabel("[ 📷 ]", SwingConstants.CENTER);
        lblFoto.setPreferredSize(new Dimension(150, 150));
        lblFoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        lblFoto.setAlignmentX(Component.LEFT_ALIGNMENT);
        fotoPanel.add(lblFoto);
        JLabel lblHelp = new JLabel("<html><center>JPG, PNG o GIF. Máximo 2MB.<br>Recomendado 400x400px.</center></html>", SwingConstants.CENTER);
        lblHelp.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblHelp.setAlignmentX(Component.LEFT_ALIGNMENT);
        fotoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        fotoPanel.add(lblHelp);
        leftCol.add(fotoPanel);
        leftCol.add(Box.createRigidArea(new Dimension(0, 15)));

        // Configuración de Acceso
        JPanel configPanel = createSectionPanel("🔑 Configuración de Acceso");
        JLabel lblUser = new JLabel("NOMBRE DE USUARIO");
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        configPanel.add(lblUser);
        txtUsername = new JTextField();
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        configPanel.add(txtUsername);
        configPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel lblRol = new JLabel("ROL DE SISTEMA");
        lblRol.setAlignmentX(Component.LEFT_ALIGNMENT);
        configPanel.add(lblRol);
        cbRolSistema = new JComboBox<>(new String[]{"Seleccionar permiso...", "Administrador", "Clínico", "Recepción"});
        cbRolSistema.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        cbRolSistema.setAlignmentX(Component.LEFT_ALIGNMENT);
        configPanel.add(cbRolSistema);
        
        leftCol.add(configPanel);
        leftCol.add(Box.createRigidArea(new Dimension(0, 15)));

        // Horarios y Turnos 
        JPanel horPanel = createSectionPanel("🕒 Horarios y Turnos");
        JLabel lblJornada = new JLabel("JORNADA PREDETERMINADA");
        lblJornada.setAlignmentX(Component.LEFT_ALIGNMENT);
        horPanel.add(lblJornada);
        
        JPanel turnos = new JPanel(new GridLayout(1, 3, 5, 0));
        turnos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        turnos.setAlignmentX(Component.LEFT_ALIGNMENT);
        ButtonGroup bgTurnos = new ButtonGroup();
        JToggleButton btnMan = createJornadaBtn("Mañana", "08:00-<br>15:00");
        JToggleButton btnTar = createJornadaBtn("Tarde", "15:00-<br>22:00");
        JToggleButton btnNoc = createJornadaBtn("Noche", "22:00-<br>08:00");
        bgTurnos.add(btnMan); bgTurnos.add(btnTar); bgTurnos.add(btnNoc);
        turnos.add(btnMan);
        turnos.add(btnTar);
        turnos.add(btnNoc);
        horPanel.add(turnos);
        horPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel lblDias = new JLabel("DÍAS LABORALES");
        lblDias.setAlignmentX(Component.LEFT_ALIGNMENT);
        horPanel.add(lblDias);
        
        JPanel dias = new JPanel(new GridLayout(1, 7, 2, 0));
        dias.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        dias.setAlignmentX(Component.LEFT_ALIGNMENT);
        String[] nombresDias = {"L", "M", "X", "J", "V", "S", "D"};
        for (String d : nombresDias) {
            JToggleButton tb = new JToggleButton(d);
            tb.setMargin(new Insets(2, 2, 2, 2));
            dias.add(tb);
        }
        horPanel.add(dias);
        leftCol.add(horPanel);

        mainPanel.add(leftCol, BorderLayout.WEST);

        // RIGHT COLUMN
        JPanel rightCol = new JPanel();
        rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.Y_AXIS));

        // Datos Personales
        JPanel dpPanel = createSectionPanel("👤 Datos Personales");
        
        JLabel lblNombre = new JLabel("NOMBRE COMPLETO");
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        dpPanel.add(lblNombre);
        txtNombre = new JTextField();
        txtNombre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        dpPanel.add(txtNombre);
        dpPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel row2 = new JPanel(new GridLayout(1, 2, 15, 0));
        row2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        row2.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel pDni = createFieldPanel("DNI / ID IDENTIFICACIÓN", txtDni = new JTextField());
        JPanel pFec = createFieldPanel("FECHA DE NACIMIENTO", txtFechaNac = new JTextField("dd / mm / aaaa"));
        row2.add(pDni); row2.add(pFec);
        dpPanel.add(row2);
        dpPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel row3 = new JPanel(new GridLayout(1, 2, 15, 0));
        row3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        row3.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel pGen = createFieldPanel("GÉNERO", cbGenero = new JComboBox<>(new String[]{"Seleccionar...", "Masculino", "Femenino", "Otro"}));
        JPanel pTel = createFieldPanel("TELÉFONO DE CONTACTO", txtTelefono = new JTextField());
        row3.add(pGen); row3.add(pTel);
        dpPanel.add(row3);
        dpPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel lblEmail = new JLabel("CORREO ELECTRÓNICO");
        lblEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        dpPanel.add(lblEmail);
        txtEmail = new JTextField();
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        dpPanel.add(txtEmail);
        dpPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel lblDir = new JLabel("DIRECCIÓN DE RESIDENCIA");
        lblDir.setAlignmentX(Component.LEFT_ALIGNMENT);
        dpPanel.add(lblDir);
        txtDireccion = new JTextField();
        txtDireccion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtDireccion.setAlignmentX(Component.LEFT_ALIGNMENT);
        dpPanel.add(txtDireccion);

        rightCol.add(dpPanel);
        rightCol.add(Box.createRigidArea(new Dimension(0, 15)));

        // Información Profesional
        JPanel ipPanel = createSectionPanel("💼 Información Profesional");
        
        JPanel rowIP1 = new JPanel(new GridLayout(1, 2, 15, 0));
        rowIP1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        rowIP1.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel pRol = createFieldPanel("ROL PRINCIPAL", cbRolPrincipal = new JComboBox<>(new String[]{"Seleccionar rol...", "Veterinario", "Asistente", "Recepción"}));
        JPanel pEsp = createFieldPanel("ESPECIALIDAD (OPCIONAL)", txtEspecialidad = new JTextField());
        rowIP1.add(pRol); rowIP1.add(pEsp);
        ipPanel.add(rowIP1);
        ipPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel rowIP2 = new JPanel(new GridLayout(1, 2, 15, 0));
        rowIP2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        rowIP2.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel pCol = createFieldPanel("Nº DE COLEGIADO", txtColegiado = new JTextField());
        JPanel pFecc = createFieldPanel("FECHA DE CONTRATACIÓN", txtFechaContratacion = new JTextField("dd / mm / aaaa"));
        rowIP2.add(pCol); rowIP2.add(pFecc);
        ipPanel.add(rowIP2);
        
        rightCol.add(ipPanel);

        mainPanel.add(rightCol, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // --- FOOTER ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnGuardar = new JButton("💾 Guardar Empleado");
        
        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> guardar());
        
        footerPanel.add(btnCancelar);
        footerPanel.add(btnGuardar);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createSectionPanel(String title) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(titleLabel);
        p.add(Box.createRigidArea(new Dimension(0, 15)));
        return p;
    }
    
    private JPanel createFieldPanel(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout());
        JLabel l = new JLabel(label);
        l.setFont(new Font("SansSerif", Font.BOLD, 10));
        p.add(l, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private JToggleButton createJornadaBtn(String title, String descHtml) {
        JToggleButton tb = new JToggleButton("<html><center><b>" + title + "</b><br>" + descHtml + "</center></html>");
        tb.setFont(new Font("SansSerif", Font.PLAIN, 9));
        return tb;
    }

    private void guardar() {
        if (nuevoPersonal == null) {
            nuevoPersonal = new Personal();
            nuevoPersonal.setId(new Random().nextInt(1000));
        }
        
        nuevoPersonal.setNombre(txtNombre.getText());
        nuevoPersonal.setDni(txtDni.getText());
        nuevoPersonal.setFechaNacimiento(txtFechaNac.getText());
        nuevoPersonal.setGenero(cbGenero.getSelectedItem() != null ? cbGenero.getSelectedItem().toString() : "");
        nuevoPersonal.setTelefono(txtTelefono.getText());
        nuevoPersonal.setEmail(txtEmail.getText());
        nuevoPersonal.setDireccion(txtDireccion.getText());
        
        nuevoPersonal.setCargo(cbRolPrincipal.getSelectedItem() != null ? cbRolPrincipal.getSelectedItem().toString() : "");
        nuevoPersonal.setEspecialidad(txtEspecialidad.getText());
        nuevoPersonal.setColegiado(txtColegiado.getText());
        nuevoPersonal.setFechaContratacion(txtFechaContratacion.getText());
        
        nuevoPersonal.setUsername(txtUsername.getText());
        nuevoPersonal.setRolSistema(cbRolSistema.getSelectedItem() != null ? cbRolSistema.getSelectedItem().toString() : "");
        nuevoPersonal.setDepartamento(nuevoPersonal.getRolSistema());
        if (nuevoPersonal.getEstado() == null) nuevoPersonal.setEstado("● Activo");

        saved = true;
        dispose();
    }

    public void setPersonalToEdit(Personal p) {
        this.nuevoPersonal = p;
        setTitle("Editar Empleado");
        
        txtNombre.setText(p.getNombre());
        txtDni.setText(p.getDni());
        txtFechaNac.setText(p.getFechaNacimiento());
        if (p.getGenero() != null) cbGenero.setSelectedItem(p.getGenero());
        txtTelefono.setText(p.getTelefono());
        txtEmail.setText(p.getEmail());
        txtDireccion.setText(p.getDireccion());
        
        if (p.getCargo() != null) cbRolPrincipal.setSelectedItem(p.getCargo());
        txtEspecialidad.setText(p.getEspecialidad());
        txtColegiado.setText(p.getColegiado());
        txtFechaContratacion.setText(p.getFechaContratacion());
        
        txtUsername.setText(p.getUsername());
        if (p.getRolSistema() != null) cbRolSistema.setSelectedItem(p.getRolSistema());
    }

    public boolean isSaved() {
        return saved;
    }

    public Personal getNuevoPersonal() {
        return nuevoPersonal;
    }
}
