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
    private Paciente.EspecieAnimal currentEspecieFilter = null; // null = Todos
    private Paciente.EstadoPaciente currentEstadoFilter = null;

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
        
        // Panel Especie
        JPanel especiePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        especiePanel.setBorder(BorderFactory.createTitledBorder("ESPECIE:"));
        especiePanel.setOpaque(false);
        String[] especies = {"Todo", "Perros", "Gatos", "Exóticos"};
        for (String sp : especies) {
            JButton b = new JButton(sp);
            b.setFocusPainted(false);
            b.setBackground(Color.WHITE);
            b.addActionListener(e -> {
                if (sp.equals("Todo")) currentEspecieFilter = null;
                else if (sp.equals("Perros")) currentEspecieFilter = Paciente.EspecieAnimal.PERRO;
                else if (sp.equals("Gatos")) currentEspecieFilter = Paciente.EspecieAnimal.GATO;
                else currentEspecieFilter = Paciente.EspecieAnimal.EXOTICO;
                cargarDatos();
            });
            especiePanel.add(b);
        }
        
        // Panel Estado
        JPanel estadoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        estadoPanel.setBorder(BorderFactory.createTitledBorder("ESTADO:"));
        estadoPanel.setOpaque(false);
        String[] estados = {"Todos", "En Clínica", "Alta", "Seguimiento"};
        for (String est : estados) {
            JButton b = new JButton(est);
            b.setFocusPainted(false);
            b.setBackground(Color.WHITE);
            b.addActionListener(e -> {
                if (est.equals("Todos")) currentEstadoFilter = null;
                else if (est.equals("En Clínica")) currentEstadoFilter = Paciente.EstadoPaciente.EN_CLINICA;
                else if (est.equals("Alta")) currentEstadoFilter = Paciente.EstadoPaciente.ALTA;
                else currentEstadoFilter = Paciente.EstadoPaciente.SEGUIMIENTO;
                cargarDatos();
            });
            estadoPanel.add(b);
        }
        
        filtersPanel.add(especiePanel);
        filtersPanel.add(estadoPanel);
        
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
                    abrirRegistroPaciente(p); // Por ahora abrir edición al hacer clic en acciones
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
        
        CardAlerta card1 = new CardAlerta("OCUPACIÓN ACTUAL", "18", "🏨", new Color(200, 240, 240), Color.BLACK);
        CardAlerta card2 = new CardAlerta("CONSULTAS HOY", "32", "🩺", new Color(240, 240, 255), Color.BLACK);
        CardAlerta card3 = new CardAlerta("PENDIENTES", "5", "📋", new Color(255, 240, 220), Color.BLACK);
        
        footerPanel.add(card1);
        footerPanel.add(card2);
        footerPanel.add(card3);
        
        add(footerPanel, BorderLayout.SOUTH);
        
        cargarDatos();
    }
    
    private void cargarDatos() {
        pacientesCache = pacienteDao.getAll();
        
        // Aplicar filtros
        List<Paciente> filtrados = pacientesCache.stream()
                .filter(p -> currentEspecieFilter == null || p.getEspecie() == currentEspecieFilter)
                .filter(p -> currentEstadoFilter == null || p.getEstado() == currentEstadoFilter)
                .collect(Collectors.toList());
                
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
}
