package society.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import society.dao.CitaDao;
import society.modell.recepcion.Cita;
import society.view.components.CardAlerta;
import society.view.components.CardHorizontal;

public class CitasViewBasic extends JPanel {
    
    private JTable calendarTable;
    private DefaultTableModel tableModel;
    private CitaDao citaDao;

    public CitasViewBasic() {
        citaDao = new CitaDao();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 1. Header (Title, Date Nav, Nueva Cita)
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        JPanel titleNavPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel title = new JLabel("Gestión de Citas");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleNavPanel.add(title);
        
        titleNavPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        JButton prevBtn = new JButton("<");
        JLabel dateRange = new JLabel(" Esta Semana ");
        dateRange.setFont(new Font("SansSerif", Font.BOLD, 12));
        JButton nextBtn = new JButton(">");
        titleNavPanel.add(prevBtn);
        titleNavPanel.add(dateRange);
        titleNavPanel.add(nextBtn);
        
        headerPanel.add(titleNavPanel, BorderLayout.WEST);
        
        JButton newCitaBtn = new JButton("+ Nueva Cita");
        newCitaBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        newCitaBtn.addActionListener(e -> {
            Window parentWindow = SwingUtilities.windowForComponent(this);
            RegistroCitasBasic dialog = new RegistroCitasBasic((Frame) parentWindow);
            dialog.setVisible(true);
            
            if (dialog.isSaved()) {
                Cita c = dialog.getNuevaCita();
                citaDao.save(c);
                loadCalendarData(); 
                JOptionPane.showMessageDialog(this, "Cita creada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        headerPanel.add(newCitaBtn, BorderLayout.EAST);

        // 2. Filters
        JPanel filtersPanel = new JPanel(new BorderLayout());
        filtersPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        comboPanel.add(createFilter("VETERINARIO", "Todos los Doctores"));
        comboPanel.add(createFilter("ESTADO", "Todos los Estados"));
        comboPanel.add(createFilter("SERVICIO", "Todos los Servicios"));
        filtersPanel.add(comboPanel, BorderLayout.WEST);
        
        JPanel viewModePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JToggleButton btnSemana = new JToggleButton("Semana", true);
        JToggleButton btnMes = new JToggleButton("Mes");
        JToggleButton btnDia = new JToggleButton("Día");
        ButtonGroup bg = new ButtonGroup();
        bg.add(btnSemana); bg.add(btnMes); bg.add(btnDia);
        viewModePanel.add(btnSemana);
        viewModePanel.add(btnMes);
        viewModePanel.add(btnDia);
        filtersPanel.add(viewModePanel, BorderLayout.EAST);

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.add(headerPanel, BorderLayout.NORTH);
        topSection.add(filtersPanel, BorderLayout.SOUTH);
        topSection.add(new JSeparator(), BorderLayout.CENTER);
        
        add(topSection, BorderLayout.NORTH);

        // 3. Main Content
        JPanel mainContent = new JPanel(new BorderLayout(15, 0));
        
        // --- CALENDAR ---
        JPanel calendarWrapper = new JPanel(new BorderLayout());
        calendarWrapper.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        String[] columnNames = {"Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Cell content should not be editable directly like text
            }
        };
        calendarTable = new JTable(tableModel);
        calendarTable.setRowHeight(50);
        calendarTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Disable auto-resize to allow horizontal scroll
        
        // Set column widths
        calendarTable.getColumnModel().getColumn(0).setMaxWidth(60);
        calendarTable.getColumnModel().getColumn(0).setMinWidth(60);
        calendarTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        
        for (int i = 1; i < calendarTable.getColumnCount(); i++) {
            calendarTable.getColumnModel().getColumn(i).setMinWidth(150);
            calendarTable.getColumnModel().getColumn(i).setPreferredWidth(200);
        }
        
        calendarTable.setDefaultRenderer(Object.class, new CitaCellRenderer());

        calendarTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = calendarTable.rowAtPoint(evt.getPoint());
                    int col = calendarTable.columnAtPoint(evt.getPoint());
                    if (row >= 0 && col > 0) {
                        Object val = tableModel.getValueAt(row, col);
                        if (val instanceof Cita) {
                            editarEliminarCita((Cita) val);
                        }
                    }
                }
            }
        });
        
        loadCalendarData();
        
        calendarWrapper.add(calendarTable.getTableHeader(), BorderLayout.NORTH);
        calendarWrapper.add(new JScrollPane(calendarTable), BorderLayout.CENTER);
        mainContent.add(calendarWrapper, BorderLayout.CENTER);

        // --- SIDEBAR RIGHT ---
        JPanel rightSidebar = new JPanel();
        rightSidebar.setLayout(new BoxLayout(rightSidebar, BoxLayout.Y_AXIS));
        rightSidebar.setPreferredSize(new Dimension(300, 0));
        rightSidebar.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        JPanel rightPadding = new JPanel();
        rightPadding.setLayout(new BoxLayout(rightPadding, BoxLayout.Y_AXIS));
        rightPadding.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel hoyTitle = new JLabel("Hoy: 17 Mayo");
        hoyTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        rightPadding.add(hoyTitle);
        rightPadding.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        statsPanel.setOpaque(false);
        
        CardAlerta cardCitas = new CardAlerta("CITAS", "12");
        CardAlerta cardAtendidos = new CardAlerta("ATENDIDOS", "4");
        CardAlerta cardPend = new CardAlerta("PEND.", "2");
        
        statsPanel.add(cardCitas);
        statsPanel.add(cardAtendidos);
        statsPanel.add(cardPend);
        
        rightPadding.add(statsPanel);
        rightPadding.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPadding.add(new JSeparator());
        rightPadding.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JLabel proximasTitle = new JLabel("PRÓXIMAS CITAS");
        proximasTitle.setFont(new Font("SansSerif", Font.BOLD, 10));
        rightPadding.add(proximasTitle);
        rightPadding.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Contenedor para las próximas citas (CardHorizontal) con Scroll
        JPanel listProximas = new JPanel();
        listProximas.setLayout(new BoxLayout(listProximas, BoxLayout.Y_AXIS));
        listProximas.setOpaque(false);
        
        CardHorizontal card1 = new CardHorizontal("🐾 Buddy", "12:30", "Golden Retriever", "Confirmado - Consulta", "Detalles", "↪");
        CardHorizontal card2 = new CardHorizontal("🐾 Mimi", "13:15", "Siamés", "Llegada - Vacunas", "Check-in", "↪");
        CardHorizontal card3 = new CardHorizontal("🐾 Paco", "14:00", "Loro", "En sala de espera", "Detalles", "↪");
        CardHorizontal card4 = new CardHorizontal("🐾 Max", "15:30", "Pastor Alemán", "Confirmado - Cirugía", "Detalles", "↪");
        CardHorizontal card5 = new CardHorizontal("🐾 Luna", "16:45", "Gato Persa", "Llegada - Revisión", "Check-in", "↪");
        
        listProximas.add(card1);
        listProximas.add(Box.createRigidArea(new Dimension(0, 10)));
        listProximas.add(card2);
        listProximas.add(Box.createRigidArea(new Dimension(0, 10)));
        listProximas.add(card3);
        listProximas.add(Box.createRigidArea(new Dimension(0, 10)));
        listProximas.add(card4);
        listProximas.add(Box.createRigidArea(new Dimension(0, 10)));
        listProximas.add(card5);
        
        JScrollPane scrollProximas = new JScrollPane(listProximas);
        scrollProximas.setBorder(null);
        scrollProximas.getViewport().setOpaque(false);
        scrollProximas.setOpaque(false);
        scrollProximas.setPreferredSize(new Dimension(280, 400)); // Increased height
        scrollProximas.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        rightPadding.add(scrollProximas);
        
        rightPadding.add(Box.createVerticalGlue());
        
        JButton printBtn = new JButton("🖨️ Imprimir Agenda Diaria");
        printBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        printBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        printBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Imprimiendo Agenda..."));
        rightPadding.add(printBtn);
        
        rightSidebar.add(rightPadding);
        mainContent.add(rightSidebar, BorderLayout.EAST);
        
        add(mainContent, BorderLayout.CENTER);
    }
    
    private void loadCalendarData() {
        tableModel.setRowCount(0);
        java.util.List<Cita> citas = citaDao.getAll();
        
        String[] hours = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"};
        
        for (String hour : hours) {
            Object[] row = new Object[8];
            row[0] = hour; 
            
            int hourInt = Integer.parseInt(hour.split(":")[0]);
            
            for (Cita c : citas) {
                if (c.getFechaHora() != null && c.getFechaHora().getHour() == hourInt) {
                    int dayOfWeek = c.getFechaHora().getDayOfWeek().getValue();
                    if (row[dayOfWeek] == null) {
                        row[dayOfWeek] = c;
                    }
                }
            }
            
            tableModel.addRow(row);
        }
    }
    
    private void editarEliminarCita(Cita c) {
        String[] options = {"Editar", "Eliminar", "Cancelar"};
        int choice = JOptionPane.showOptionDialog(this,
                "¿Qué desea hacer con la cita de " + c.getPacienteNombre() + "?",
                "Gestionar Cita",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);
                
        if (choice == 0) {
            Window parentWindow = SwingUtilities.windowForComponent(this);
            RegistroCitasBasic dialog = new RegistroCitasBasic((Frame) parentWindow);
            dialog.setCitaToEdit(c);
            dialog.setVisible(true);
            
            if (dialog.isSaved()) {
                citaDao.update(dialog.getNuevaCita());
                loadCalendarData();
                JOptionPane.showMessageDialog(this, "Cita actualizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (choice == 1) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar esta cita?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                citaDao.delete(c.getId());
                loadCalendarData();
            }
        }
    }

    class CitaCellRenderer implements TableCellRenderer {
        private final DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Cita) {
                Cita c = (Cita) value;
                JPanel card = createAppointmentCard(c.getPacienteNombre(), c.getMotivo());
                if (isSelected) {
                    card.setBackground(table.getSelectionBackground());
                } else {
                    card.setBackground(Color.WHITE);
                }
                return card;
            } else {
                Component comp = defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 0) {
                    ((JLabel) comp).setHorizontalAlignment(SwingConstants.CENTER);
                    comp.setFont(new Font("SansSerif", Font.BOLD, 12));
                    comp.setBackground(new Color(240, 240, 240));
                } else {
                    comp.setBackground(Color.WHITE);
                }
                return comp;
            }
        }
    }
    
    private JPanel createAppointmentCard(String name, String detail) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        
        JLabel nameL = new JLabel(name != null ? name : "");
        nameL.setFont(new Font("SansSerif", Font.BOLD, 11));
        
        JLabel detL = new JLabel(detail != null ? detail : "");
        detL.setFont(new Font("SansSerif", Font.PLAIN, 9));
        
        card.add(nameL, BorderLayout.NORTH);
        card.add(detL, BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createFilter(String labelText, String comboText) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        JLabel l = new JLabel(labelText);
        l.setFont(new Font("SansSerif", Font.BOLD, 10));
        p.add(l);
        p.add(new JComboBox<>(new String[]{comboText}));
        return p;
    }
    
    private JPanel createStatItem(String num, String label) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel nl = new JLabel(num);
        nl.setFont(new Font("SansSerif", Font.BOLD, 18));
        nl.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel ll = new JLabel(label);
        ll.setFont(new Font("SansSerif", Font.PLAIN, 10));
        ll.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(nl);
        p.add(ll);
        return p;
    }
    
    private JPanel createUpcomingCard(String name, String breed, String time, String status, String btnAction) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JPanel top = new JPanel(new BorderLayout());
        JLabel nLabel = new JLabel("🐾 " + name);
        nLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        top.add(nLabel, BorderLayout.WEST);
        
        JLabel tLabel = new JLabel(time);
        tLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        top.add(tLabel, BorderLayout.EAST);
        
        card.add(top);
        
        JLabel bLabel = new JLabel(breed);
        bLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        card.add(bLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JLabel sLabel = new JLabel("● " + status);
        sLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        card.add(sLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel actions = new JPanel(new BorderLayout(5, 0));
        JButton actionBtn = new JButton(btnAction);
        JButton extraBtn = new JButton("↪");
        actions.add(actionBtn, BorderLayout.CENTER);
        actions.add(extraBtn, BorderLayout.EAST);
        
        card.add(actions);
        return card;
    }
}