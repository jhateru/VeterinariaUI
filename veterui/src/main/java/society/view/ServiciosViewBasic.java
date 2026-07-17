package society.view;

import society.dao.ServicioDao;
import society.modell.administracion.Servicio;
import society.view.components.CardAlerta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ServiciosViewBasic extends JPanel {

    private ServicioDao servicioDao;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Servicio> serviciosCache;

    public ServiciosViewBasic() {
        servicioDao = new ServicioDao();
        
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setBackground(Color.WHITE);

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        
        JLabel lblTitle = new JLabel("Gestión de Servicios");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 80, 100)); // Teal oscuro
        
        JLabel lblSub = new JLabel("Administra el catálogo de servicios, precios y paquetes ofrecidos por la clínica.");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSub.setForeground(Color.GRAY);
        
        titlePanel.add(lblTitle);
        titlePanel.add(lblSub);
        
        JButton btnNuevo = new JButton("+ Nuevo Servicio");
        btnNuevo.setBackground(new Color(0, 100, 100));
        btnNuevo.setForeground(Color.WHITE);
        btnNuevo.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnNuevo.setFocusPainted(false);
        btnNuevo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNuevo.addActionListener(e -> abrirRegistroServicio(null));
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        headerPanel.add(btnNuevo, BorderLayout.EAST);
        
        // --- TOP CARDS (CardAlerta) ---
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsPanel.setOpaque(false);
        
        CardAlerta card1 = new CardAlerta("TOTAL SERVICIOS ACTIVOS", "42", "✓", new Color(220, 245, 255), new Color(0, 80, 100));
        CardAlerta card2 = new CardAlerta("INGRESOS MTD", "$12,450.00", "$", new Color(230, 255, 230), new Color(0, 150, 50));
        CardAlerta card3 = new CardAlerta("CATEGORÍA POPULAR", "Vacunación", "★", new Color(255, 250, 220), new Color(200, 120, 0));
        
        cardsPanel.add(card1);
        cardsPanel.add(card2);
        cardsPanel.add(card3);
        
        // --- SEARCH BAR & ACTIONS ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);
        
        JLabel lblSearch = new JLabel("Buscar:");
        lblSearch.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        JTextField txtSearch = new JTextField(30);
        
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(Color.LIGHT_GRAY);
        btnBuscar.setFocusPainted(false);
        
        JButton btnEditar = new JButton("✏️ Editar");
        btnEditar.setBackground(new Color(240, 173, 78));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFocusPainted(false);
        btnEditar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0 && selectedRow < serviciosCache.size()) {
                Servicio s = serviciosCache.get(selectedRow);
                abrirRegistroServicio(s);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un servicio de la tabla para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        JButton btnEliminar = new JButton("🗑️ Eliminar");
        btnEliminar.setBackground(new Color(217, 83, 79));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0 && selectedRow < serviciosCache.size()) {
                Servicio s = serviciosCache.get(selectedRow);
                int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar el servicio: " + s.getNombre() + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    servicioDao.delete(s.getId());
                    cargarDatos();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un servicio de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnBuscar);
        searchPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        searchPanel.add(btnEditar);
        searchPanel.add(btnEliminar);
        
        // Top Container
        JPanel topContainer = new JPanel(new BorderLayout(0, 15));
        topContainer.setOpaque(false);
        topContainer.add(headerPanel, BorderLayout.NORTH);
        
        JPanel cardsAndSearch = new JPanel(new BorderLayout(0, 15));
        cardsAndSearch.setOpaque(false);
        cardsAndSearch.add(cardsPanel, BorderLayout.NORTH);
        cardsAndSearch.add(searchPanel, BorderLayout.SOUTH);
        
        topContainer.add(cardsAndSearch, BorderLayout.CENTER);
        
        add(topContainer, BorderLayout.NORTH);

        // --- TABLA ---
        String[] columns = {"NOMBRE DEL SERVICIO", "CATEGORÍA", "DURACIÓN", "PRECIO BASE", "ESTADO"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(40);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(245, 245, 245));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        add(scrollPane, BorderLayout.CENTER);
        
        cargarDatos();
    }
    
    private void cargarDatos() {
        serviciosCache = servicioDao.getAll();
        
        tableModel.setRowCount(0);
        for (Servicio s : serviciosCache) {
            String duracionStr = s.getDuracionEstimadaMinutos() + " min";
            String precioStr = String.format("$%.2f", s.getPrecioBase());
            String estadoStr = (s.getEstado() != null && s.getEstado().equalsIgnoreCase("Activo")) ? "Activo" : "Inactivo";
            
            tableModel.addRow(new Object[]{
                s.getNombre(),
                s.getCategoria() != null ? s.getCategoria() : "General",
                duracionStr,
                precioStr,
                estadoStr
            });
        }
    }

    private void abrirRegistroServicio(Servicio s) {
        RegistroServicioBasic dialog = new RegistroServicioBasic((Frame) SwingUtilities.getWindowAncestor(this));
        if (s != null) {
            dialog.setServicioToEdit(s);
        }
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            if (s == null) {
                servicioDao.create(dialog.getServicio());
            } else {
                servicioDao.update(dialog.getServicio());
            }
            cargarDatos();
        }
    }
}
