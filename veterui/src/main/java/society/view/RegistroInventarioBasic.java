package society.view;

import society.modell.inventario.Inventario;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class RegistroInventarioBasic extends JDialog {

    private boolean saved = false;
    private Inventario inventario;

    private JTextField txtProducto;
    private JComboBox<String> cbCategoria;
    private JTextField txtLote;
    private JTextField txtFefo;
    private JTextField txtStock;
    private JTextField txtUnidad;
    private JTextField txtPuntoReorden;
    private JTextField txtPrecio;
    private JComboBox<String> cbEstado;

    public RegistroInventarioBasic(Frame parent) {
        super(parent, "Registro de Inventario", true);
        setSize(450, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(15, 15));

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));
        JLabel title = new JLabel("Detalles del Producto");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(new JSeparator(), BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // --- MAIN CONTENT ---
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        txtProducto = new JTextField();
        mainPanel.add(createFieldPanel("NOMBRE DEL PRODUCTO", txtProducto));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        cbCategoria = new JComboBox<>(new String[]{"Antibiótico", "Quirúrgico", "Anestésico", "Dietas Especiales", "Biológicos", "Medicamentos", "Suministro Médico"});
        mainPanel.add(createFieldPanel("CATEGORÍA", cbCategoria));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        txtLote = new JTextField();
        mainPanel.add(createFieldPanel("LOTE (Ej. AMX-2024-09)", txtLote));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        txtFefo = new JTextField();
        mainPanel.add(createFieldPanel("FECHA DE VENCIMIENTO (FEFO Ej. 12/2025)", txtFefo));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel rowPanel1 = new JPanel(new GridLayout(1, 2, 10, 0));
        txtStock = new JTextField();
        txtUnidad = new JTextField();
        rowPanel1.add(createFieldPanel("STOCK ACTUAL", txtStock));
        rowPanel1.add(createFieldPanel("UNIDAD (Ej. Blisters)", txtUnidad));
        mainPanel.add(rowPanel1);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel rowPanel2 = new JPanel(new GridLayout(1, 2, 10, 0));
        txtPuntoReorden = new JTextField();
        txtPrecio = new JTextField();
        rowPanel2.add(createFieldPanel("PUNTO REORDEN", txtPuntoReorden));
        rowPanel2.add(createFieldPanel("PRECIO UNITARIO ($)", txtPrecio));
        mainPanel.add(rowPanel2);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        cbEstado = new JComboBox<>(new String[]{"Estable", "STOCK BAJO", "VENCIMIENTO"});
        mainPanel.add(createFieldPanel("ESTADO", cbEstado));

        add(mainPanel, BorderLayout.CENTER);

        // --- FOOTER ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnGuardar = new JButton("💾 Guardar");
        btnGuardar.setBackground(new Color(0, 100, 100));
        btnGuardar.setForeground(Color.WHITE);

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
        if (txtProducto.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del producto es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int stock = 0;
        int pReorden = 0;
        double precio = 0;
        try {
            stock = Integer.parseInt(txtStock.getText().trim());
            pReorden = Integer.parseInt(txtPuntoReorden.getText().trim());
            precio = Double.parseDouble(txtPrecio.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Stock, Punto Reorden y Precio deben ser numéricos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (inventario == null) {
            inventario = new Inventario();
            inventario.setId("INV-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase());
        }

        inventario.setProducto(txtProducto.getText().trim());
        inventario.setCategoria(cbCategoria.getSelectedItem().toString());
        inventario.setLote(txtLote.getText().trim());
        inventario.setFefo(txtFefo.getText().trim());
        inventario.setStock(stock);
        inventario.setUnidad(txtUnidad.getText().trim());
        inventario.setPuntoReorden(pReorden);
        inventario.setPrecio(precio);
        inventario.setEstado(cbEstado.getSelectedItem().toString());
        // Default descripcion
        inventario.setDescripcion("");

        saved = true;
        dispose();
    }

    public void setInventarioToEdit(Inventario inv) {
        this.inventario = inv;
        setTitle("Editar Producto");

        txtProducto.setText(inv.getProducto());
        cbCategoria.setSelectedItem(inv.getCategoria());
        txtLote.setText(inv.getLote());
        txtFefo.setText(inv.getFefo());
        txtStock.setText(String.valueOf(inv.getStock()));
        txtUnidad.setText(inv.getUnidad());
        txtPuntoReorden.setText(String.valueOf(inv.getPuntoReorden()));
        txtPrecio.setText(String.valueOf(inv.getPrecio()));
        cbEstado.setSelectedItem(inv.getEstado());
    }

    public boolean isSaved() {
        return saved;
    }

    public Inventario getInventario() {
        return inventario;
    }
}
