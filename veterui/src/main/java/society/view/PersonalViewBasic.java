package society.view;

import society.dao.PersonalDao;
import society.modell.administracion.Personal;
import society.view.components.CardAlerta;
import society.view.components.CardHorizontal;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PersonalViewBasic extends JPanel {

    private JTable empleadosTable;
    private DefaultTableModel tableModel;
    private PersonalDao personalDao;

    private CardAlerta cardTotalPersonal;
    private CardAlerta cardActivos;
    private CardAlerta cardReunion;

    private CardHorizontal cardManana;
    private CardHorizontal cardTarde;
    private CardHorizontal cardNoche;

    public PersonalViewBasic() {
        personalDao = new PersonalDao();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 1. Header (Title, Search, Notification, New Employee)
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        JLabel title = new JLabel("Gestión de Personal");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        headerPanel.add(title, BorderLayout.WEST);
        
        JPanel centerHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField searchField = new JTextField(30);
        searchField.setToolTipText("Buscar empleados por nombre, cargo o ID...");
        centerHeader.add(new JLabel("🔍"));
        centerHeader.add(searchField);
        headerPanel.add(centerHeader, BorderLayout.CENTER);
        
        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightHeader.add(new JLabel("🔔"));
        rightHeader.add(Box.createRigidArea(new Dimension(10, 0)));
        JButton newEmpBtn = new JButton("+ Nuevo Empleado");
        newEmpBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        newEmpBtn.addActionListener(e -> {
            Window parentWindow = SwingUtilities.windowForComponent(this);
            RegistroPersonalBasic dialog = new RegistroPersonalBasic((Frame) parentWindow);
            dialog.setVisible(true);
            
            if (dialog.isSaved()) {
                Personal p = dialog.getNuevoPersonal();
                personalDao.save(p);
                loadTableData();
                JOptionPane.showMessageDialog(this, "Empleado guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        rightHeader.add(newEmpBtn);
        headerPanel.add(rightHeader, BorderLayout.EAST);

        // Separator below header
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.add(headerPanel, BorderLayout.NORTH);
        topSection.add(new JSeparator(), BorderLayout.SOUTH);
        topSection.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(topSection, BorderLayout.NORTH);

        // 2. Main Content
        JPanel mainContent = new JPanel(new BorderLayout(15, 0));
        
        // --- LEFT PANEL ---
        JPanel leftPanel = new JPanel(new BorderLayout(0, 15));
        
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        cardTotalPersonal = new CardAlerta("TOTAL PERSONAL", "0", "👥", new Color(240, 248, 255), Color.DARK_GRAY);
        cardActivos = new CardAlerta("PERSONAL ACTIVO", "0", "💼", new Color(240, 255, 240), new Color(0, 150, 0));
        cardReunion = new CardAlerta("PRÓXIMA REUNIÓN", "Staff", "📅", new Color(255, 245, 230), Color.DARK_GRAY);

        cardsPanel.add(cardTotalPersonal);
        cardsPanel.add(cardActivos);
        cardsPanel.add(cardReunion);
        leftPanel.add(cardsPanel, BorderLayout.NORTH);
        
        JPanel directoryPanel = new JPanel(new BorderLayout());
        directoryPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        JPanel dirHeader = new JPanel(new BorderLayout());
        dirHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel dirTitle = new JLabel("Directorio de Empleados");
        dirTitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dirHeader.add(dirTitle, BorderLayout.WEST);
        
        JPanel dirButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        dirButtons.add(new JButton("≡ Filtrar"));
        JButton btnEditar = new JButton("✎ Editar");
        JButton btnEliminar = new JButton("🗑 Eliminar");
        dirButtons.add(btnEditar);
        dirButtons.add(btnEliminar);
        dirHeader.add(dirButtons, BorderLayout.EAST);
        directoryPanel.add(dirHeader, BorderLayout.NORTH);
        
        // JTable Implementation
        String[] columns = {"ID", "NOMBRE", "DEPARTAMENTO", "DNI", "ROL DE SISTEMA", "ESTADO", "EMAIL", "TELÉFONO", "CONTRATACIÓN"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        empleadosTable = new JTable(tableModel);
        empleadosTable.setRowHeight(25);
        // Hide ID column
        empleadosTable.getColumnModel().getColumn(0).setMinWidth(0);
        empleadosTable.getColumnModel().getColumn(0).setMaxWidth(0);
        empleadosTable.getColumnModel().getColumn(0).setWidth(0);

        loadTableData();
        
        directoryPanel.add(new JScrollPane(empleadosTable), BorderLayout.CENTER);
        
        // Action Listeners for Edit and Delete
        btnEditar.addActionListener(e -> {
            int selectedRow = empleadosTable.getSelectedRow();
            if (selectedRow >= 0) {
                int empId = (int) tableModel.getValueAt(selectedRow, 0);
                Personal p = personalDao.getAll().stream().filter(emp -> emp.getId() == empId).findFirst().orElse(null);
                if (p != null) {
                    Window parentWindow = SwingUtilities.windowForComponent(this);
                    RegistroPersonalBasic dialog = new RegistroPersonalBasic((Frame) parentWindow);
                    dialog.setPersonalToEdit(p);
                    dialog.setVisible(true);
                    
                    if (dialog.isSaved()) {
                        personalDao.update(dialog.getNuevoPersonal());
                        loadTableData();
                        JOptionPane.showMessageDialog(this, "Empleado actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un empleado para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnEliminar.addActionListener(e -> {
            int selectedRow = empleadosTable.getSelectedRow();
            if (selectedRow >= 0) {
                int empId = (int) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este empleado?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    personalDao.delete(empId);
                    loadTableData();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un empleado para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        JPanel tFooter = new JPanel(new BorderLayout());
        tFooter.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tFooter.add(new JLabel("Mostrando paginación..."), BorderLayout.WEST);
        
        JPanel pagination = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pagination.add(new JLabel("<"));
        pagination.add(createBoldLabel("1"));
        pagination.add(new JLabel("2"));
        pagination.add(new JLabel("3"));
        pagination.add(new JLabel(">"));
        tFooter.add(pagination, BorderLayout.EAST);
        directoryPanel.add(tFooter, BorderLayout.SOUTH);
        
        leftPanel.add(directoryPanel, BorderLayout.CENTER);
        mainContent.add(leftPanel, BorderLayout.CENTER);

        // --- RIGHT PANEL ---
        JPanel rightSidebar = new JPanel();
        rightSidebar.setLayout(new BoxLayout(rightSidebar, BoxLayout.Y_AXIS));
        rightSidebar.setPreferredSize(new Dimension(280, 0));
        
        JPanel rightPadding = new JPanel();
        rightPadding.setLayout(new BoxLayout(rightPadding, BoxLayout.Y_AXIS));
        rightPadding.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        
        JPanel turnosHeader = new JPanel(new BorderLayout());
        JLabel turnosTitle = new JLabel("Turnos Hoy");
        turnosTitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        turnosHeader.add(turnosTitle, BorderLayout.WEST);
        turnosHeader.add(new JLabel("[LIVE]"), BorderLayout.EAST);
        rightPadding.add(turnosHeader);
        rightPadding.add(Box.createRigidArea(new Dimension(0, 15)));
        
        cardManana = new CardHorizontal("Turno Mañana", "08:00-15:00", "0 Programados", "Activo", "Ver Detalle", "Notificar");
        cardTarde = new CardHorizontal("Turno Tarde", "15:00-22:00", "0 Programados", "Próximo", "Ver Detalle", "Notificar");
        cardNoche = new CardHorizontal("Turno Noche", "22:00-08:00", "0 Programados", "Inactivo", "Ver Detalle", "Notificar");

        rightPadding.add(cardManana);
        rightPadding.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPadding.add(cardTarde);
        rightPadding.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPadding.add(cardNoche);
        
        rightPadding.add(Box.createVerticalGlue());
        
        rightSidebar.add(rightPadding);
        mainContent.add(rightSidebar, BorderLayout.EAST);
        
        add(mainContent, BorderLayout.CENTER);
    }
    

    
    private JLabel createBoldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 10));
        return l;
    }
    

    
    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Personal> empleados = personalDao.getAll();
        
        updateDashboard(empleados);

        for (Personal p : empleados) {
            tableModel.addRow(new Object[]{
                p.getId(),
                p.getNombre() != null ? p.getNombre() : "",
                p.getDepartamento() != null ? p.getDepartamento() : "",
                p.getDni() != null ? p.getDni() : "",
                p.getCargo() != null ? p.getCargo() : "",
                p.getEstado() != null ? p.getEstado() : "● Activo",
                p.getEmail() != null ? p.getEmail() : "",
                p.getTelefono() != null ? p.getTelefono() : "",
                p.getFechaContratacion() != null ? p.getFechaContratacion() : ""
            });
        }
    }

    private void updateDashboard(List<Personal> empleados) {
        long total = empleados.size();
        long activos = empleados.stream().filter(e -> e.getEstado() != null && e.getEstado().contains("Activo")).count();
        
        java.time.DayOfWeek dayOfWeek = java.time.LocalDate.now().getDayOfWeek();
        String todayInitial = "";
        switch (dayOfWeek) {
            case MONDAY: todayInitial = "L"; break;
            case TUESDAY: todayInitial = "M"; break;
            case WEDNESDAY: todayInitial = "X"; break;
            case THURSDAY: todayInitial = "J"; break;
            case FRIDAY: todayInitial = "V"; break;
            case SATURDAY: todayInitial = "S"; break;
            case SUNDAY: todayInitial = "D"; break;
        }
        final String today = todayInitial;
        
        long manana = empleados.stream().filter(e -> "Mañana".equals(e.getTurno()) && e.getDiasLaborales() != null && e.getDiasLaborales().contains(today)).count();
        long tarde = empleados.stream().filter(e -> "Tarde".equals(e.getTurno()) && e.getDiasLaborales() != null && e.getDiasLaborales().contains(today)).count();
        long noche = empleados.stream().filter(e -> "Noche".equals(e.getTurno()) && e.getDiasLaborales() != null && e.getDiasLaborales().contains(today)).count();
        
        if (cardTotalPersonal != null) cardTotalPersonal.updateData(String.valueOf(total));
        if (cardActivos != null) cardActivos.updateData(String.valueOf(activos));
        
        if (cardManana != null) cardManana.updateData("Turno Mañana", "08:00-15:00", manana + " Programados", manana > 0 ? "Activo" : "Sin Personal");
        if (cardTarde != null) cardTarde.updateData("Turno Tarde", "15:00-22:00", tarde + " Programados", tarde > 0 ? "Activo" : "Sin Personal");
        if (cardNoche != null) cardNoche.updateData("Turno Noche", "22:00-08:00", noche + " Programados", noche > 0 ? "Activo" : "Sin Personal");
    }
}
