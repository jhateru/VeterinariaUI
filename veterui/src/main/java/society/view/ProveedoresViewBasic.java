package society.view;

import society.dao.ProveedoresDao;
import society.modell.administracion.Proveedores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ProveedoresViewBasic extends JPanel {

    private Color brandBlue = new Color(0, 80, 100);
    private Color lightBg = new Color(248, 250, 250);
    
    private ProveedoresDao dao;
    private List<Proveedores> proveedoresList;
    
    private JTable tablaProveedores;
    private DefaultTableModel tableModel;
    private JPanel summaryCardsPanel;
    
    private JButton btnEditar;
    private JButton btnEliminar;

    public ProveedoresViewBasic() {
        dao = new ProveedoresDao();
        
        setLayout(new BorderLayout());
        setBackground(lightBg);
        setBorder(new EmptyBorder(25, 25, 25, 25));
        
        cargarDatos();
        
        JPanel mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setBackground(lightBg);
        
        // Top section (Header + Cards)
        JPanel topSection = new JPanel(new BorderLayout(0, 20));
        topSection.setBackground(lightBg);
        topSection.add(buildHeader(), BorderLayout.NORTH);
        
        summaryCardsPanel = buildSummaryCards();
        topSection.add(summaryCardsPanel, BorderLayout.CENTER);
        
        mainContent.add(topSection, BorderLayout.NORTH);
        
        // Center section (Table + Toolbar)
        mainContent.add(buildTableSection(), BorderLayout.CENTER);
        
        add(new JScrollPane(mainContent), BorderLayout.CENTER);
    }
    
    private void cargarDatos() {
        proveedoresList = dao.getAll();
        
        // Create dummy data if empty to match image
        if (proveedoresList.isEmpty()) {
            dao.save(new Proveedores(1, "PRV-001", "PharmaVet Solutions", "Medicamentos", "ventas@pharmavet.com", "+1 555-0123", "Activo", "24 Oct, 2023"));
            dao.save(new Proveedores(42, "PRV-042", "BioPet Supplies", "Equipamiento", "soporte@biopet.io", "+1 555-8890", "En Revisión", "15 Sep, 2023"));
            dao.save(new Proveedores(15, "PRV-015", "VetNutri Distribuidora", "Alimentos", "pedidos@vetnutri.com", "+1 555-4321", "Activo", "02 Nov, 2023"));
            proveedoresList = dao.getAll();
        }
    }
    
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(lightBg);
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(lightBg);
        
        JLabel title = new JLabel("Gestión de Proveedores");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        
        JLabel subtitle = new JLabel("Centraliza y optimiza el abastecimiento de suministros médicos y alimentos.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        
        titlePanel.add(title);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitle);
        
        header.add(titlePanel, BorderLayout.WEST);
        
        JButton btnNuevo = new JButton("➕ Nuevo Proveedor");
        btnNuevo.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnNuevo.setBackground(brandBlue);
        btnNuevo.setForeground(Color.WHITE);
        btnNuevo.setFocusPainted(false);
        btnNuevo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnNuevo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNuevo.addActionListener(e -> mostrarFormulario(null));
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(lightBg);
        actionPanel.add(btnNuevo);
        header.add(actionPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel buildSummaryCards() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setBackground(lightBg);
        
        int total = proveedoresList.size();
        panel.add(createStatCard("TOTAL PROVEEDORES", String.valueOf(total), "🏪", "+2 este mes"));
        panel.add(createStatCard("ÓRDENES EN TRÁNSITO", "12", "🚚", "4 llegan hoy"));
        panel.add(createStatCard("INVERSIÓN MENSUAL", "$14,250.00", "💵", "+5% vs mes"));
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, String icon, String subtext) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        top.setBackground(Color.WHITE);
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblTitle.setForeground(Color.GRAY);
        top.add(lblIcon);
        top.add(lblTitle);
        
        card.add(top, BorderLayout.NORTH);
        
        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        center.setBackground(Color.WHITE);
        center.setBorder(new EmptyBorder(10, 0, 0, 0));
        JLabel lblVal = new JLabel(value);
        lblVal.setFont(new Font("SansSerif", Font.BOLD, 32));
        
        JLabel lblSub = new JLabel(subtext);
        lblSub.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblSub.setForeground(brandBlue);
        lblSub.setBackground(new Color(230, 245, 245));
        lblSub.setOpaque(true);
        lblSub.setBorder(new EmptyBorder(2, 5, 2, 5));
        
        center.add(lblVal);
        center.add(lblSub);
        
        card.add(center, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel buildTableSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        // Toolbar
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(new EmptyBorder(5, 5, 15, 5));
        
        JLabel lblTitle = new JLabel("📇 Directorio de Proveedores");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        toolbar.add(lblTitle, BorderLayout.WEST);
        
        JPanel toolsRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        toolsRight.setBackground(Color.WHITE);
        
        btnEditar = createToolButton("✏️ Editar");
        btnEditar.setEnabled(false);
        btnEditar.addActionListener(e -> {
            int row = tablaProveedores.getSelectedRow();
            if (row >= 0) {
                mostrarFormulario(proveedoresList.get(row));
            }
        });
        
        btnEliminar = createToolButton("🗑️ Eliminar");
        btnEliminar.setEnabled(false);
        btnEliminar.setForeground(new Color(200, 50, 50));
        btnEliminar.addActionListener(e -> eliminarProveedor());
        
        JButton btnExportar = createToolButton("📥 Exportar");
        btnExportar.addActionListener(e -> exportarCSV());
        
        toolsRight.add(btnEditar);
        toolsRight.add(btnEliminar);
        toolsRight.add(new JLabel(" | "));
        toolsRight.add(createToolButton("≡ Filtros"));
        toolsRight.add(btnExportar);
        
        toolbar.add(toolsRight, BorderLayout.EAST);
        section.add(toolbar, BorderLayout.NORTH);
        
        // Table
        String[] columnas = {"PROVEEDOR", "CATEGORÍA", "CONTACTO", "ESTADO", "ÚLTIMA ORDEN"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        actualizarTabla();
        
        tablaProveedores = new JTable(tableModel);
        tablaProveedores.setRowHeight(75);
        tablaProveedores.setShowGrid(false);
        tablaProveedores.setIntercellSpacing(new Dimension(0, 0));
        tablaProveedores.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        tablaProveedores.getTableHeader().setBackground(Color.WHITE);
        tablaProveedores.getTableHeader().setForeground(Color.GRAY);
        tablaProveedores.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
        
        ProveedoresCellRenderer renderer = new ProveedoresCellRenderer();
        for (int i = 0; i < columnas.length; i++) {
            tablaProveedores.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        
        tablaProveedores.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = tablaProveedores.getSelectedRow() != -1;
            btnEditar.setEnabled(hasSelection);
            btnEliminar.setEnabled(hasSelection);
        });
        
        JScrollPane scroll = new JScrollPane(tablaProveedores);
        scroll.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(220, 220, 220)));
        scroll.getViewport().setBackground(Color.WHITE);
        section.add(scroll, BorderLayout.CENTER);
        
        // Footer (Pagination)
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(10, 0, 0, 0));
        footer.add(new JLabel("Mostrando 1-" + proveedoresList.size() + " de " + proveedoresList.size() + " proveedores"));
        section.add(footer, BorderLayout.SOUTH);
        
        return section;
    }
    
    private JButton createToolButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.DARK_GRAY);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(6, 12, 6, 12)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    private void actualizarTabla() {
        tableModel.setRowCount(0);
        for (Proveedores p : proveedoresList) {
            tableModel.addRow(new Object[]{p, p, p, p, p});
        }
    }
    
    private void actualizarUI() {
        proveedoresList = dao.getAll();
        actualizarTabla();
        
        // Update summary cards
        Container parent = summaryCardsPanel.getParent();
        parent.remove(summaryCardsPanel);
        summaryCardsPanel = buildSummaryCards();
        parent.add(summaryCardsPanel, BorderLayout.CENTER);
        parent.revalidate();
        parent.repaint();
    }
    
    // --- Lógica CRUD ---
    
    private void mostrarFormulario(Proveedores proveedorAEditar) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Proveedor", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel pnl = new JPanel(new GridLayout(6, 2, 10, 10));
        pnl.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JTextField txtId = new JTextField(proveedorAEditar != null ? proveedorAEditar.getIdProveedorStr() : "PRV-");
        JTextField txtNombre = new JTextField(proveedorAEditar != null ? proveedorAEditar.getNombre() : "");
        JComboBox<String> cmbCat = new JComboBox<>(new String[]{"Medicamentos", "Equipamiento", "Alimentos"});
        if (proveedorAEditar != null) cmbCat.setSelectedItem(proveedorAEditar.getCategoria());
        
        JTextField txtEmail = new JTextField(proveedorAEditar != null ? proveedorAEditar.getContactoNombre() : "");
        JTextField txtTel = new JTextField(proveedorAEditar != null ? proveedorAEditar.getContactoTelefono() : "");
        JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Activo", "En Revisión", "Inactivo"});
        if (proveedorAEditar != null) cmbEstado.setSelectedItem(proveedorAEditar.getEstado());
        
        pnl.add(new JLabel("ID Proveedor:")); pnl.add(txtId);
        pnl.add(new JLabel("Razón Social:")); pnl.add(txtNombre);
        pnl.add(new JLabel("Categoría:")); pnl.add(cmbCat);
        pnl.add(new JLabel("Email/Contacto:")); pnl.add(txtEmail);
        pnl.add(new JLabel("Teléfono:")); pnl.add(txtTel);
        pnl.add(new JLabel("Estado:")); pnl.add(cmbEstado);
        
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> {
            if (txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "El nombre es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Proveedores p = proveedorAEditar != null ? proveedorAEditar : new Proveedores();
            if (proveedorAEditar == null) {
                p.setId((int)(Math.random() * 1000));
                p.setUltimaOrdenFecha("-");
            }
            
            p.setIdProveedorStr(txtId.getText().trim());
            p.setNombre(txtNombre.getText().trim());
            p.setCategoria(cmbCat.getSelectedItem().toString());
            p.setContactoNombre(txtEmail.getText().trim());
            p.setContactoTelefono(txtTel.getText().trim());
            p.setEstado(cmbEstado.getSelectedItem().toString());
            
            if (proveedorAEditar == null) {
                proveedoresList.add(p);
            }
            
            dao.saveAll(proveedoresList);
            actualizarUI();
            dialog.dispose();
        });
        
        JPanel pnlBtn = new JPanel();
        pnlBtn.add(btnGuardar);
        
        dialog.add(pnl, BorderLayout.CENTER);
        dialog.add(pnlBtn, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void eliminarProveedor() {
        int row = tablaProveedores.getSelectedRow();
        if (row >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este proveedor?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                proveedoresList.remove(row);
                dao.saveAll(proveedoresList);
                actualizarUI();
            }
        }
    }
    
    private void exportarCSV() {
        try {
            FileWriter writer = new FileWriter("proveedores_export.csv");
            writer.append("ID,Razón Social,Categoría,Contacto,Teléfono,Estado,Última Orden\n");
            
            for (Proveedores p : proveedoresList) {
                writer.append(p.getIdProveedorStr()).append(",")
                      .append(p.getNombre().replace(",", "")).append(",")
                      .append(p.getCategoria()).append(",")
                      .append(p.getContactoNombre()).append(",")
                      .append(p.getContactoTelefono()).append(",")
                      .append(p.getEstado()).append(",")
                      .append(p.getUltimaOrdenFecha()).append("\n");
            }
            
            writer.flush();
            writer.close();
            JOptionPane.showMessageDialog(this, "Proveedores exportados a 'proveedores_export.csv' exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al exportar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
