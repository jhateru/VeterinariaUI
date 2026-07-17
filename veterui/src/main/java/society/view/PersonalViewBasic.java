package society.view;

import society.dao.PersonalDao;
import society.modell.administracion.Personal;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PersonalViewBasic extends JPanel {

    private JTable empleadosTable;
    private DefaultTableModel tableModel;
    private PersonalDao personalDao;

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
        cardsPanel.add(createSummaryCard("👥 +2 este mes", "TOTAL PERSONAL", "24", ""));
        cardsPanel.add(createSummaryCard("💼 +6", "EN TURNO AHORA", "8", "🕒 Turno Mañana: 08:00 - 14:00"));
        cardsPanel.add(createSummaryCard("📅", "PRÓXIMA REUNIÓN", "Staff General", "⏰ 15:30 PM Today"));
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
        String[] columns = {"ID", "NOMBRE", "CARGO", "DNI", "DEPARTAMENTO", "ROL", "ESTADO", "EMAIL", "TELÉFONO", "CONTRATACIÓN"};
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
        
        rightPadding.add(createTurnoItem("08:00 - 14:00", "Turno Mañana", "8 Veterinarios en turno"));
        rightPadding.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPadding.add(createTurnoItem("14:00 - 20:00", "Turno Tarde", "6 Programados"));
        rightPadding.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPadding.add(createTurnoItem("20:00 - 08:00", "Turno Noche / Urgencias", "2 Programados"));
        
        rightPadding.add(Box.createVerticalGlue());
        
        rightSidebar.add(rightPadding);
        mainContent.add(rightSidebar, BorderLayout.EAST);
        
        add(mainContent, BorderLayout.CENTER);
    }
    
    private JPanel createSummaryCard(String topBadge, String title, String mainValue, String subText) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        JLabel badge = new JLabel(topBadge);
        badge.setFont(new Font("SansSerif", Font.PLAIN, 10));
        card.add(badge, BorderLayout.NORTH);
        
        JPanel centerP = new JPanel();
        centerP.setLayout(new BoxLayout(centerP, BoxLayout.Y_AXIS));
        centerP.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel tLbl = new JLabel(title);
        tLbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        centerP.add(tLbl);
        JLabel vLbl = new JLabel(mainValue);
        vLbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        centerP.add(vLbl);
        card.add(centerP, BorderLayout.CENTER);
        
        if (!subText.isEmpty()) {
            JLabel sLbl = new JLabel(subText);
            sLbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
            card.add(sLbl, BorderLayout.SOUTH);
        }
        return card;
    }
    
    private JLabel createBoldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 10));
        return l;
    }
    
    private JPanel createTurnoItem(String time, String title, String desc) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, Color.LIGHT_GRAY)); 
        
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        JLabel tLbl = new JLabel(time);
        tLbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
        JLabel mainLbl = new JLabel(title);
        mainLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        JLabel dLbl = new JLabel(desc);
        dLbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
        
        inner.add(tLbl);
        inner.add(mainLbl);
        inner.add(dLbl);
        p.add(inner, BorderLayout.CENTER);
        return p;
    }
    
    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Personal> empleados = personalDao.getAll();
        for (Personal p : empleados) {
            tableModel.addRow(new Object[]{
                p.getId(),
                p.getNombre() != null ? p.getNombre() : "",
                p.getCargo() != null ? p.getCargo() : "",
                p.getDni() != null ? p.getDni() : "",
                p.getDepartamento() != null ? p.getDepartamento() : "",
                p.getRolSistema() != null ? p.getRolSistema() : "",
                p.getEstado() != null ? p.getEstado() : "● Activo",
                p.getEmail() != null ? p.getEmail() : "",
                p.getTelefono() != null ? p.getTelefono() : "",
                p.getFechaContratacion() != null ? p.getFechaContratacion() : ""
            });
        }
    }
}
