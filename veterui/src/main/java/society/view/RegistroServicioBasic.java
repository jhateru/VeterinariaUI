package society.view;

import society.modell.administracion.Servicio;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class RegistroServicioBasic extends JDialog {

    private boolean saved = false;
    private Servicio servicio;

    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JComboBox<String> cbCategoria;
    private JTextField txtPrecioBase;
    private JTextField txtDuracion;
    private JComboBox<String> cbEstado;

    public RegistroServicioBasic(Frame parent) {
        super(parent, "Registro de Servicio", true);
        setSize(400, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(15, 15));

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));
        JLabel title = new JLabel("Detalles del Servicio");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(new JSeparator(), BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // --- MAIN CONTENT ---
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        mainPanel.add(createFieldPanel("NOMBRE DEL SERVICIO", txtNombre = new JTextField()));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        txtDescripcion = new JTextArea(3, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        mainPanel.add(createFieldPanel("DESCRIPCIÓN", scrollDesc));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        cbCategoria = new JComboBox<>(new String[]{"Consulta", "Vacunación", "Cirugía", "Estética", "Examen", "Otro"});
        mainPanel.add(createFieldPanel("CATEGORÍA", cbCategoria));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        txtPrecioBase = new JTextField();
        mainPanel.add(createFieldPanel("PRECIO BASE ($)", txtPrecioBase));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        txtDuracion = new JTextField();
        mainPanel.add(createFieldPanel("DURACIÓN ESTIMADA (Minutos)", txtDuracion));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        cbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
        mainPanel.add(createFieldPanel("ESTADO", cbEstado));

        add(mainPanel, BorderLayout.CENTER);

        // --- FOOTER ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnGuardar = new JButton("💾 Guardar");

        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> guardar());

        footerPanel.add(btnCancelar);
        footerPanel.add(btnGuardar);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createFieldPanel(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        JLabel l = new JLabel(label);
        l.setFont(new Font("SansSerif", Font.BOLD, 10));
        p.add(l, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private void guardar() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del servicio es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double precio = 0;
        int duracion = 0;
        try {
            precio = Double.parseDouble(txtPrecioBase.getText().trim());
            duracion = Integer.parseInt(txtDuracion.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Precio y Duración deben ser numéricos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (servicio == null) {
            servicio = new Servicio();
            servicio.setId(new Random().nextInt(10000));
        }

        servicio.setNombre(txtNombre.getText().trim());
        servicio.setDescripcion(txtDescripcion.getText().trim());
        servicio.setCategoria(cbCategoria.getSelectedItem().toString());
        servicio.setPrecioBase(precio);
        servicio.setDuracionEstimadaMinutos(duracion);
        servicio.setEstado(cbEstado.getSelectedItem().toString());

        saved = true;
        dispose();
    }

    public void setServicioToEdit(Servicio s) {
        this.servicio = s;
        setTitle("Editar Servicio");

        txtNombre.setText(s.getNombre());
        txtDescripcion.setText(s.getDescripcion());
        cbCategoria.setSelectedItem(s.getCategoria());
        txtPrecioBase.setText(String.valueOf(s.getPrecioBase()));
        txtDuracion.setText(String.valueOf(s.getDuracionEstimadaMinutos()));
        cbEstado.setSelectedItem(s.getEstado());
    }

    public boolean isSaved() {
        return saved;
    }

    public Servicio getServicio() {
        return servicio;
    }
}
