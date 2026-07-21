package society.view;

import society.dao.PacienteDao;
import society.modell.recepcion.Paciente;
import society.view.components.CardAlerta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class PacientesViewBasic extends JPanel {

    private PacienteDao pacienteDao;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Paciente> pacientesCache;
    
    // Filtros
    private JTextField txtSearch;
    private JComboBox<String> cmbEspecie;
    private JComboBox<String> cmbEstado;
    
    private CardAlerta cardTotalPacientes;
    private CardAlerta cardEnClinica;
    private CardAlerta cardSeguimiento;

    public PacientesViewBasic() {
        pacienteDao = new PacienteDao();
        
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setBackground(Color.WHITE);

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        
        JLabel lblTitle = new JLabel("Gestión de Pacientes");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 80, 100)); // Teal oscuro
        
        JLabel lblSub = new JLabel("Administra los expedientes y el estado actual de todos los animales registrados.");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSub.setForeground(Color.GRAY);
        
        titlePanel.add(lblTitle);
        titlePanel.add(lblSub);
        
        JButton btnNuevo = new JButton("+ Registrar Nuevo Paciente");
        btnNuevo.setBackground(new Color(0, 100, 100));
        btnNuevo.setForeground(Color.WHITE);
        btnNuevo.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnNuevo.setFocusPainted(false);
        btnNuevo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNuevo.addActionListener(e -> abrirRegistroPaciente(null));
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        headerPanel.add(btnNuevo, BorderLayout.EAST);
        
        // --- FILTROS ---
        JPanel filtersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        filtersPanel.setOpaque(false);
        
        JLabel lblSearch = new JLabel("Buscar Paciente:");
        lblSearch.setFont(new Font("SansSerif", Font.BOLD, 12));
        filtersPanel.add(lblSearch);
        
        txtSearch = new JTextField(15);
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { cargarDatos(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { cargarDatos(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { cargarDatos(); }
        });
        filtersPanel.add(txtSearch);
        
        filtersPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        
        JLabel lblEspecie = new JLabel("Especie:");
        lblEspecie.setFont(new Font("SansSerif", Font.BOLD, 12));
        filtersPanel.add(lblEspecie);
        
        cmbEspecie = new JComboBox<>(new String[]{"Todas", "Perros", "Gatos", "Exóticos"});
        cmbEspecie.setBackground(Color.WHITE);
        cmbEspecie.addActionListener(e -> cargarDatos());
        filtersPanel.add(cmbEspecie);
        
        filtersPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        
        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setFont(new Font("SansSerif", Font.BOLD, 12));
        filtersPanel.add(lblEstado);
        
        cmbEstado = new JComboBox<>(new String[]{"Todos", "En Clínica", "Alta", "Seguimiento"});
        cmbEstado.setBackground(Color.WHITE);
        cmbEstado.addActionListener(e -> cargarDatos());
        filtersPanel.add(cmbEstado);
        
        JPanel topContainer = new JPanel(new BorderLayout(0, 15));
        topContainer.setOpaque(false);
        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(filtersPanel, BorderLayout.CENTER);
        
        add(topContainer, BorderLayout.NORTH);

        // --- TABLA ---
        String[] columns = {"PACIENTE", "ESPECIE / RAZA", "DUEÑO", "ÚLTIMA VISITA", "ESTADO", "ACCIONES"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return Paciente.class; // Para que el Renderer reciba el objeto completo en todas las celdas
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(70); // Fila alta para mostrar multiples líneas
        table.setDefaultRenderer(Paciente.class, new PacienteCellRenderer());
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setShowGrid(false);
        
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        table.getTableHeader().setBackground(new Color(245, 245, 245));
        
        // Simular clic en "Acciones"
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 5) { // Columna ACCIONES
                    Paciente p = (Paciente) tableModel.getValueAt(row, 0);
                    Rectangle cellRect = table.getCellRect(row, col, false);
                    int xInCell = e.getX() - cellRect.x;
                    if (xInCell > cellRect.width / 2) {
                        // Clic lado derecho = Eliminar
                        int resp = JOptionPane.showConfirmDialog(PacientesViewBasic.this, "¿Eliminar paciente " + p.getNombre() + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
                        if (resp == JOptionPane.YES_OPTION) {
                            pacienteDao.delete(p.getId());
                            cargarDatos();
                        }
                    } else {
                        // Clic lado izquierdo = Editar
                        abrirRegistroPaciente(p);
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        add(scrollPane, BorderLayout.CENTER);

        // --- FOOTER (CardAlertas) ---
        JPanel footerPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        footerPanel.setOpaque(false);
        
        Color brandBlue = new Color(0, 80, 100);
        cardTotalPacientes = new CardAlerta("TOTAL PACIENTES", "0", "🐾", new Color(240, 248, 255), brandBlue);
        cardEnClinica = new CardAlerta("EN CLÍNICA", "0", "🏨", new Color(240, 255, 240), new Color(0, 150, 0));
        cardSeguimiento = new CardAlerta("SEGUIMIENTO", "0", "📋", new Color(255, 250, 240), new Color(200, 150, 0));
        
        footerPanel.add(cardTotalPacientes);
        footerPanel.add(cardEnClinica);
        footerPanel.add(cardSeguimiento);
        
        add(footerPanel, BorderLayout.SOUTH);
        
        cargarDatos();
    }
    
    private void cargarDatos() {
        pacientesCache = pacienteDao.getAll();
        
        String query = "";
        if (txtSearch != null) {
            query = txtSearch.getText().trim().toLowerCase();
        }
        
        String selEspecie = "Todas";
        if (cmbEspecie != null) {
            selEspecie = (String) cmbEspecie.getSelectedItem();
        }
        
        String selEstado = "Todos";
        if (cmbEstado != null) {
            selEstado = (String) cmbEstado.getSelectedItem();
        }
        
        final String fQuery = query;
        final String fEspecie = selEspecie;
        final String fEstado = selEstado;

        // Aplicar filtros
        List<Paciente> filtrados = pacientesCache.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(fQuery))
                .filter(p -> {
                    if (fEspecie.equals("Todas")) return true;
                    if (p.getEspecie() == null) return false;
                    if (fEspecie.equals("Perros") && p.getEspecie() == Paciente.EspecieAnimal.PERRO) return true;
                    if (fEspecie.equals("Gatos") && p.getEspecie() == Paciente.EspecieAnimal.GATO) return true;
                    if (fEspecie.equals("Exóticos") && p.getEspecie() == Paciente.EspecieAnimal.EXOTICO) return true;
                    return false;
                })
                .filter(p -> {
                    if (fEstado.equals("Todos")) return true;
                    if (p.getEstado() == null) return false;
                    if (fEstado.equals("En Clínica") && p.getEstado() == Paciente.EstadoPaciente.EN_CLINICA) return true;
                    if (fEstado.equals("Alta") && p.getEstado() == Paciente.EstadoPaciente.ALTA) return true;
                    if (fEstado.equals("Seguimiento") && p.getEstado() == Paciente.EstadoPaciente.SEGUIMIENTO) return true;
                    return false;
                })
                .collect(Collectors.toList());
                
        long total = filtrados.size();
        long enClinica = filtrados.stream().filter(p -> p.getEstado() == Paciente.EstadoPaciente.EN_CLINICA).count();
        long seguimiento = filtrados.stream().filter(p -> p.getEstado() == Paciente.EstadoPaciente.SEGUIMIENTO).count();
        
        if (cardTotalPacientes != null) cardTotalPacientes.updateData(String.valueOf(total));
        if (cardEnClinica != null) cardEnClinica.updateData(String.valueOf(enClinica));
        if (cardSeguimiento != null) cardSeguimiento.updateData(String.valueOf(seguimiento));
                
        tableModel.setRowCount(0);
        for (Paciente p : filtrados) {
            // Llenamos todas las columnas con el mismo objeto, el Renderer decide qué mostrar
            tableModel.addRow(new Object[]{p, p, p, p, p, p});
        }
    }
    
    private void abrirRegistroPaciente(Paciente p) {
        RegistroPacienteBasic dialog = new RegistroPacienteBasic((Frame) SwingUtilities.getWindowAncestor(this));
        if (p != null) {
            dialog.setPacienteToEdit(p);
        }
        dialog.setVisible(true);
        
        if (dialog.isSaved()) {
            if (p == null) {
                pacienteDao.create(dialog.getPaciente());
            } else {
                pacienteDao.update(dialog.getPaciente());
            }
            cargarDatos();
        }
    }
    
    public void abrirRegistroParaDueno(society.modell.recepcion.Dueno d) {
        RegistroPacienteBasic dialog = new RegistroPacienteBasic((Frame) SwingUtilities.getWindowAncestor(this));
        if (d != null) {
            dialog.preseleccionarDueno(d);
        }
        dialog.setVisible(true);
        
        if (dialog.isSaved()) {
            pacienteDao.create(dialog.getPaciente());
            cargarDatos();
        }
    }
}
