package society.view;

import society.dao.InventarioDao;
import society.modell.administracion.Consumible;
import society.modell.administracion.Servicio;
import society.modell.inventario.Inventario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
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

    private JTable tablaMateriales;
    private DefaultTableModel modeloMateriales;
    private List<Consumible> materialesList;
    private List<Inventario> inventarioCache;
    private InventarioDao inventarioDao;

    public RegistroServicioBasic(Frame parent) {
        super(parent, "Registro de Servicio", true);
        setSize(700, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(15, 15));

        inventarioDao = new InventarioDao();
        inventarioCache = inventarioDao.getAll();
        materialesList = new ArrayList<>();

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));
        JLabel title = new JLabel("Detalles del Servicio");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(new JSeparator(), BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // --- MAIN CONTENT ---
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // LEFT: Maestro (Master)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        leftPanel.add(createFieldPanel("NOMBRE DEL SERVICIO", txtNombre = new JTextField()));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        txtDescripcion = new JTextArea(3, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        leftPanel.add(createFieldPanel("DESCRIPCIÓN", scrollDesc));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        cbCategoria = new JComboBox<>(new String[]{"Consulta", "Vacunación", "Cirugía", "Estética", "Examen", "Otro"});
        leftPanel.add(createFieldPanel("CATEGORÍA", cbCategoria));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        txtPrecioBase = new JTextField();
        leftPanel.add(createFieldPanel("PRECIO BASE ($)", txtPrecioBase));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        txtDuracion = new JTextField();
        leftPanel.add(createFieldPanel("DURACIÓN ESTIMADA (Minutos)", txtDuracion));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        cbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
        leftPanel.add(createFieldPanel("ESTADO", cbEstado));

        // RIGHT: Detalle (Detail) - Materiales
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        
        JLabel lblMatTitle = new JLabel("Materiales / Insumos Utilizados");
        lblMatTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        rightPanel.add(lblMatTitle, BorderLayout.NORTH);

        modeloMateriales = new DefaultTableModel(new String[]{"Producto", "Cant.", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaMateriales = new JTable(modeloMateriales);
        tablaMateriales.setRowHeight(25);
        JScrollPane scrollMat = new JScrollPane(tablaMateriales);
        rightPanel.add(scrollMat, BorderLayout.CENTER);

        JPanel matActions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAddMat = new JButton("+ Añadir");
        JButton btnRemoveMat = new JButton("- Quitar");
        
        btnAddMat.addActionListener(e -> agregarMaterial());
        btnRemoveMat.addActionListener(e -> {
            int row = tablaMateriales.getSelectedRow();
            if (row >= 0) {
                materialesList.remove(row);
                actualizarTablaMateriales();
            }
        });
        matActions.add(btnAddMat);
        matActions.add(btnRemoveMat);
        rightPanel.add(matActions, BorderLayout.SOUTH);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
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

    private void agregarMaterial() {
        if (inventarioCache.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos en el inventario.");
            return;
        }

        JComboBox<String> comboInv = new JComboBox<>();
        for (Inventario inv : inventarioCache) {
            comboInv.addItem(inv.getId() + " - " + inv.getProducto());
        }

        JTextField txtCant = new JTextField("1");
        
        JPanel p = new JPanel(new GridLayout(2, 2, 5, 5));
        p.add(new JLabel("Producto:"));
        p.add(comboInv);
        p.add(new JLabel("Cantidad:"));
        p.add(txtCant);

        int result = JOptionPane.showConfirmDialog(this, p, "Seleccionar Material", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int index = comboInv.getSelectedIndex();
                Inventario invSeleccionado = inventarioCache.get(index);
                int cantidad = Integer.parseInt(txtCant.getText().trim());
                
                materialesList.add(new Consumible(invSeleccionado.getId(), cantidad));
                actualizarTablaMateriales();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarTablaMateriales() {
        modeloMateriales.setRowCount(0);
        for (Consumible c : materialesList) {
            String nombre = c.getInventarioId();
            double subtotal = 0;
            for (Inventario inv : inventarioCache) {
                if (inv.getId().equals(c.getInventarioId())) {
                    nombre = inv.getProducto();
                    subtotal = inv.getPrecio() * c.getCantidad();
                    break;
                }
            }
            modeloMateriales.addRow(new Object[]{
                nombre, c.getCantidad(), String.format("$%.2f", subtotal)
            });
        }
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
        
        // Asignar los materiales
        servicio.setMaterialesUsados(new ArrayList<>(materialesList));

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
        
        if (s.getMaterialesUsados() != null) {
            this.materialesList = new ArrayList<>(s.getMaterialesUsados());
            actualizarTablaMateriales();
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public Servicio getServicio() {
        return servicio;
    }
}
