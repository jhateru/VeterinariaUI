package society.view;

import society.dao.InventarioDao;
import society.modell.inventario.Inventario;
import society.view.components.CardMetrica;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventarioViewBasic extends JPanel {
    
    private Color darkTeal = new Color(0, 80, 100);
    private Color lightBg = new Color(248, 250, 250);
    
    private InventarioDao inventarioDao;
    private List<Inventario> inventarioList;
    
    private JTable tablaInventario;
    private DefaultTableModel tableModel;
    private JPanel summaryCardsPanel;

    public InventarioViewBasic() {
        inventarioDao = new InventarioDao();
        
        setLayout(new BorderLayout());
        setBackground(lightBg);
        setBorder(new EmptyBorder(25, 25, 25, 25));
        
        cargarDatos();
        
        JPanel mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setBackground(lightBg);
        
        // --- 1. Header (Top) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(lightBg);
        
        JLabel title = new JLabel("Gestión de Inventario");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(darkTeal);
        headerPanel.add(title, BorderLayout.WEST);
        
        JButton btnAgregar = new JButton("⊕ Agregar Producto");
        btnAgregar.setBackground(darkTeal);
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAgregar.setFocusPainted(false);
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregar.addActionListener(e -> abrirRegistro(null));
        headerPanel.add(btnAgregar, BorderLayout.EAST);
        
        // --- 2. Summary Cards ---
        summaryCardsPanel = buildSummaryCards();
        
        JPanel topContainer = new JPanel(new BorderLayout(0, 20));
        topContainer.setBackground(lightBg);
        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(summaryCardsPanel, BorderLayout.CENTER);
        
        mainContent.add(topContainer, BorderLayout.NORTH);
        
        // --- 3. Filtros y Tabla ---
        JPanel tableContainer = new JPanel(new BorderLayout(0, 15));
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Tools panel (Filters & Actions)
        JPanel toolsPanel = new JPanel(new BorderLayout());
        toolsPanel.setOpaque(false);
        
        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filters.setOpaque(false);
        JLabel lblDetalle = new JLabel("Detalle de Existencias");
        lblDetalle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblDetalle.setForeground(Color.DARK_GRAY);
        filters.add(lblDetalle);
        filters.add(Box.createRigidArea(new Dimension(10, 0)));
        filters.add(createFilterBadge("Todos", true));
        filters.add(createFilterBadge("Medicamentos", false));
        filters.add(createFilterBadge("Quirúrgico", false));
        filters.add(createFilterBadge("Dietas", false));
        
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        
        JButton btnEditar = new JButton("✏️ Editar");
        btnEditar.setBackground(new Color(240, 173, 78));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFocusPainted(false);
        btnEditar.addActionListener(e -> {
            int selected = tablaInventario.getSelectedRow();
            if (selected >= 0) {
                abrirRegistro(inventarioList.get(selected));
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para editar.");
            }
        });
        
        JButton btnEliminar = new JButton("🗑️ Eliminar");
        btnEliminar.setBackground(new Color(217, 83, 79));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(e -> {
            int selected = tablaInventario.getSelectedRow();
            if (selected >= 0) {
                Inventario inv = inventarioList.get(selected);
                int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar " + inv.getProducto() + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (conf == JOptionPane.YES_OPTION) {
                    inventarioDao.delete(inv.getId());
                    actualizarUICompleta();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar.");
            }
        });
        
        actions.add(btnEditar);
        actions.add(btnEliminar);
        
        toolsPanel.add(filters, BorderLayout.WEST);
        toolsPanel.add(actions, BorderLayout.EAST);
        tableContainer.add(toolsPanel, BorderLayout.NORTH);
        
        // Tabla
        String[] columnas = {"PRODUCTO", "CATEGORÍA", "STOCK ACTUAL", "UNIDAD", "PUNTO REORDEN", "ESTADO"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tablaInventario = new JTable(tableModel);
        tablaInventario.setRowHeight(60); // Mas alto para acomodar subtitulos
        tablaInventario.setShowGrid(false);
        tablaInventario.setIntercellSpacing(new Dimension(0, 0));
        tablaInventario.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tablaInventario.getTableHeader().setBackground(Color.WHITE);
        tablaInventario.getTableHeader().setForeground(Color.GRAY);
        tablaInventario.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        tablaInventario.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Apply cell renderer
        InventarioCellRenderer renderer = new InventarioCellRenderer();
        for (int i = 0; i < columnas.length; i++) {
            tablaInventario.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        tablaInventario.getColumnModel().getColumn(0).setPreferredWidth(250);
        
        JScrollPane scroll = new JScrollPane(tablaInventario);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        
        tableContainer.add(scroll, BorderLayout.CENTER);
        mainContent.add(tableContainer, BorderLayout.CENTER);
        
        add(mainContent, BorderLayout.CENTER);
        actualizarTabla();
    }
    
    private void cargarDatos() {
        inventarioList = inventarioDao.getAll();
    }
    
    private JPanel buildSummaryCards() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(lightBg);
        
        int totalSku = inventarioList.size();
        int bajoStock = (int) inventarioList.stream().filter(i -> i.getStock() < i.getPuntoReorden()).count();
        int vencidos = 8; // Simulado o calculado
        double valorTotal = inventarioList.stream().mapToDouble(i -> i.getStock() * i.getPrecio()).sum();
        
        CardMetrica card1 = new CardMetrica("TOTAL ARTÍCULOS", String.valueOf(totalSku), null, null, "📋", brandBlue(), new Color(240, 240, 240), null);
        CardMetrica card2 = new CardMetrica("STOCK CRÍTICO", String.valueOf(bajoStock), "alertas", new Color(180, 0, 0), "⚠️", new Color(180, 0, 0), new Color(255, 230, 230), new Color(180, 0, 0));
        CardMetrica card3 = new CardMetrica("PRÓXIMOS A VENCER", String.valueOf(vencidos), "lotes (30d)", Color.GRAY, "📅", new Color(180, 100, 0), new Color(255, 245, 220), new Color(255, 165, 0));
        CardMetrica card4 = new CardMetrica("INVERSIÓN TOTAL", String.format("$%,.0f", valorTotal), null, null, "💵", Color.WHITE, new Color(100, 150, 255), null);
        
        panel.add(card1);
        panel.add(card2);
        panel.add(card3);
        panel.add(card4);
        
        return panel;
    }
    
    private JLabel createFilterBadge(String text, boolean active) {
        JLabel badge = new JLabel(text);
        badge.setFont(new Font("SansSerif", active ? Font.BOLD : Font.PLAIN, 12));
        badge.setForeground(active ? brandBlue() : Color.GRAY);
        badge.setOpaque(true);
        badge.setBackground(active ? new Color(220, 240, 245) : new Color(240, 240, 240));
        badge.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        return badge;
    }
    
    private Color brandBlue() { return new Color(0, 80, 100); }
    
    private void actualizarTabla() {
        tableModel.setRowCount(0);
        for (Inventario inv : inventarioList) {
            // Pasamos el objeto Inventario a todas las columnas
            tableModel.addRow(new Object[]{inv, inv, inv, inv, inv, inv});
        }
    }
    
    private void actualizarUICompleta() {
        cargarDatos();
        actualizarTabla();
        tablaInventario.repaint();
        
        Container parent = summaryCardsPanel.getParent();
        parent.remove(summaryCardsPanel);
        summaryCardsPanel = buildSummaryCards();
        parent.add(summaryCardsPanel, BorderLayout.CENTER);
        parent.revalidate();
        parent.repaint();
    }

    private void abrirRegistro(Inventario inv) {
        RegistroInventarioBasic dialog = new RegistroInventarioBasic((Frame) SwingUtilities.getWindowAncestor(this));
        if (inv != null) {
            dialog.setInventarioToEdit(inv);
        }
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            if (inv == null) {
                inventarioDao.create(dialog.getInventario());
            } else {
                inventarioDao.update(dialog.getInventario());
            }
            actualizarUICompleta();
        }
    }
}
