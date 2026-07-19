package society.view;

import society.dao.InventarioDao;
import society.modell.administracion.Consumible;
import society.modell.administracion.Proveedores;
import society.modell.inventario.Inventario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RegistroProveedorBasic extends JDialog {

    private boolean saved = false;
    private Proveedores proveedor;

    private JTextField txtId;
    private JTextField txtRuc;
    private JTextField txtNombre;
    private JComboBox<String> cbCategoria;
    private JTextField txtContactoNombre;
    private JTextField txtContactoTelefono;
    private JTextField txtDireccion;
    private JTextField txtEmail;
    private JTextField txtSitioWeb;
    private JComboBox<String> cbEstado;

    private JTable tablaInventarios;
    private DefaultTableModel modeloInventarios;
    private List<Consumible> inventariosList; // Lista de productos suministrados
    private List<Inventario> inventarioCache;
    private InventarioDao inventarioDao;

    public RegistroProveedorBasic(Frame parent) {
        super(parent, "Registro de Proveedor", true);
        setSize(800, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(15, 15));

        inventarioDao = new InventarioDao();
        inventarioCache = inventarioDao.getAll();
        inventariosList = new ArrayList<>();

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));
        JLabel title = new JLabel("Detalles del Proveedor");
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

        txtId = new JTextField("PRV-");
        leftPanel.add(createFieldPanel("ID PROVEEDOR", txtId));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        txtRuc = new JTextField();
        leftPanel.add(createFieldPanel("RUC", txtRuc));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        txtNombre = new JTextField();
        leftPanel.add(createFieldPanel("RAZÓN SOCIAL", txtNombre));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        cbCategoria = new JComboBox<>(new String[]{"Medicamentos", "Equipamiento", "Alimentos", "Cirugía", "Otros"});
        leftPanel.add(createFieldPanel("CATEGORÍA", cbCategoria));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        txtContactoNombre = new JTextField();
        leftPanel.add(createFieldPanel("CONTACTO PRINCIPAL", txtContactoNombre));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        txtContactoTelefono = new JTextField();
        leftPanel.add(createFieldPanel("TELÉFONO", txtContactoTelefono));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        txtDireccion = new JTextField();
        leftPanel.add(createFieldPanel("DIRECCIÓN", txtDireccion));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        txtEmail = new JTextField();
        leftPanel.add(createFieldPanel("EMAIL", txtEmail));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        txtSitioWeb = new JTextField();
        leftPanel.add(createFieldPanel("SITIO WEB", txtSitioWeb));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        cbEstado = new JComboBox<>(new String[]{"Activo", "En Revisión", "Inactivo"});
        leftPanel.add(createFieldPanel("ESTADO", cbEstado));

        // RIGHT: Detalle (Detail) - Inventarios Suministrados
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        
        JLabel lblMatTitle = new JLabel("Inventarios Suministrados");
        lblMatTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        rightPanel.add(lblMatTitle, BorderLayout.NORTH);

        modeloInventarios = new DefaultTableModel(new String[]{"ID", "Producto", "Cant.", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaInventarios = new JTable(modeloInventarios);
        tablaInventarios.setRowHeight(25);
        JScrollPane scrollInv = new JScrollPane(tablaInventarios);
        rightPanel.add(scrollInv, BorderLayout.CENTER);

        JPanel invActions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAddInv = new JButton("+ Añadir");
        JButton btnRemoveInv = new JButton("- Quitar");
        
        btnAddInv.addActionListener(e -> agregarInventario());
        btnRemoveInv.addActionListener(e -> {
            int row = tablaInventarios.getSelectedRow();
            if (row >= 0) {
                inventariosList.remove(row);
                actualizarTablaInventarios();
            }
        });
        invActions.add(btnAddInv);
        invActions.add(btnRemoveInv);
        rightPanel.add(invActions, BorderLayout.SOUTH);

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

    private void agregarInventario() {
        if (inventarioCache.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos en el inventario global.");
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

        int result = JOptionPane.showConfirmDialog(this, p, "Seleccionar Inventario", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int index = comboInv.getSelectedIndex();
                Inventario invSeleccionado = inventarioCache.get(index);
                int cantidad = Integer.parseInt(txtCant.getText().trim());
                
                boolean existe = false;
                for (Consumible c : inventariosList) {
                    if (c.getInventarioId().equals(invSeleccionado.getId())) {
                        existe = true;
                        break;
                    }
                }
                
                if (!existe) {
                    inventariosList.add(new Consumible(invSeleccionado.getId(), cantidad));
                    actualizarTablaInventarios();
                } else {
                    JOptionPane.showMessageDialog(this, "El producto ya está en la lista.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarTablaInventarios() {
        modeloInventarios.setRowCount(0);
        for (Consumible c : inventariosList) {
            String nombre = c.getInventarioId();
            double subtotal = 0;
            for (Inventario inv : inventarioCache) {
                if (inv.getId().equals(c.getInventarioId())) {
                    nombre = inv.getProducto();
                    subtotal = inv.getPrecio() * c.getCantidad();
                    break;
                }
            }
            modeloInventarios.addRow(new Object[]{
                c.getInventarioId(), nombre, c.getCantidad(), String.format("$%.2f", subtotal)
            });
        }
    }

    private void guardar() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La razón social es obligatoria", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (proveedor == null) {
            proveedor = new Proveedores();
            proveedor.setId(new Random().nextInt(10000));
            proveedor.setUltimaOrdenFecha("-");
        }

        proveedor.setIdProveedorStr(txtId.getText().trim());
        proveedor.setRuc(txtRuc.getText().trim());
        proveedor.setNombre(txtNombre.getText().trim());
        proveedor.setCategoria(cbCategoria.getSelectedItem().toString());
        proveedor.setContactoNombre(txtContactoNombre.getText().trim());
        proveedor.setContactoTelefono(txtContactoTelefono.getText().trim());
        proveedor.setDireccion(txtDireccion.getText().trim());
        proveedor.setEmail(txtEmail.getText().trim());
        proveedor.setSitioWeb(txtSitioWeb.getText().trim());
        proveedor.setEstado(cbEstado.getSelectedItem().toString());
        
        // Asignar los inventarios
        proveedor.setInventariosSuministrados(new ArrayList<>(inventariosList));

        saved = true;
        dispose();
    }

    public void setProveedorToEdit(Proveedores p) {
        this.proveedor = p;
        setTitle("Editar Proveedor");

        txtId.setText(p.getIdProveedorStr() != null ? p.getIdProveedorStr() : "");
        txtRuc.setText(p.getRuc() != null ? p.getRuc() : "");
        txtNombre.setText(p.getNombre() != null ? p.getNombre() : "");
        if (p.getCategoria() != null) cbCategoria.setSelectedItem(p.getCategoria());
        txtContactoNombre.setText(p.getContactoNombre() != null ? p.getContactoNombre() : "");
        txtContactoTelefono.setText(p.getContactoTelefono() != null ? p.getContactoTelefono() : "");
        txtDireccion.setText(p.getDireccion() != null ? p.getDireccion() : "");
        txtEmail.setText(p.getEmail() != null ? p.getEmail() : "");
        txtSitioWeb.setText(p.getSitioWeb() != null ? p.getSitioWeb() : "");
        if (p.getEstado() != null) cbEstado.setSelectedItem(p.getEstado());
        
        if (p.getInventariosSuministrados() != null) {
            this.inventariosList = new ArrayList<>(p.getInventariosSuministrados());
            actualizarTablaInventarios();
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public Proveedores getProveedor() {
        return proveedor;
    }
}
