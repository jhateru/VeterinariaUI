package society.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import society.dao.CitaDao;
import society.dao.PacienteDao;
import society.dao.PersonalDao;
import society.modell.recepcion.Cita;
import society.modell.recepcion.Paciente;
import society.modell.administracion.Personal;
import society.view.components.CardAlerta;
import society.view.components.CardHorizontal;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CitasViewBasic extends JPanel {
    
    private JTable citasTable;
    private DefaultTableModel tableModel;
    private CitaDao citaDao;
    private PacienteDao pacienteDao;
    private PersonalDao personalDao;
    private Map<Integer, String> pacienteNombres;
    private Map<Integer, String> personalNombres;
    private JComboBox<String> cmbVeterinario;
    private JComboBox<String> cmbEstado;
    private JComboBox<String> cmbServicio;
    private Map<String, Integer> veterinarioIdsMap;
    
    private CardAlerta cardCitas;
    private CardAlerta cardAtendidos;
    private CardAlerta cardPend;
    private List<Cita> displayedCitas;

    public CitasViewBasic() {
        citaDao = new CitaDao();
        pacienteDao = new PacienteDao();
        personalDao = new PersonalDao();
        pacienteNombres = new HashMap<>();
        personalNombres = new HashMap<>();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 1. Header (Title, Date Nav, Nueva Cita)
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        JPanel titleNavPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel title = new JLabel("Gestión de Citas");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleNavPanel.add(title);
        
        headerPanel.add(titleNavPanel, BorderLayout.WEST);
        
        // Stats Panel below title
        JPanel statsContainer = new JPanel(new GridLayout(1, 3, 20, 0));
        statsContainer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        Color brandBlue = new Color(0, 80, 100);
        cardCitas = new CardAlerta("TOTAL CITAS", "0", "📅", new Color(240, 248, 255), brandBlue);
        cardAtendidos = new CardAlerta("ATENDIDOS", "0", "✅", new Color(240, 255, 240), new Color(0, 150, 0));
        cardPend = new CardAlerta("PENDIENTES", "0", "⏳", new Color(255, 250, 240), new Color(200, 150, 0));
        
        statsContainer.add(cardCitas);
        statsContainer.add(cardAtendidos);
        statsContainer.add(cardPend);
        
        JPanel headerNorth = new JPanel(new BorderLayout());
        headerNorth.add(headerPanel, BorderLayout.NORTH);
        headerNorth.add(statsContainer, BorderLayout.CENTER);
        
        JButton newCitaBtn = new JButton("+ Nueva Cita");
        newCitaBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        newCitaBtn.addActionListener(e -> {
            Window parentWindow = SwingUtilities.windowForComponent(this);
            RegistroCitasBasic dialog = new RegistroCitasBasic((Frame) parentWindow);
            dialog.setVisible(true);
            
            if (dialog.isSaved()) {
                Cita c = dialog.getNuevaCita();
                citaDao.save(c);
                loadCitasData(); 
                JOptionPane.showMessageDialog(this, "Cita creada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        headerPanel.add(newCitaBtn, BorderLayout.EAST);

        // 2. Filters
        JPanel filtersPanel = new JPanel(new BorderLayout());
        filtersPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        
        veterinarioIdsMap = new HashMap<>();
        cmbVeterinario = new JComboBox<>();
        cmbEstado = new JComboBox<>(new String[]{"Todos los Estados", "PENDIENTE", "COMPLETADA", "CANCELADA", "URGENCIA"});
        cmbServicio = new JComboBox<>(new String[]{"Todos los Servicios", "Vacunación", "Control", "Cirugía", "Urgencia", "Peluquería", "Otro"});
        
        cmbVeterinario.addActionListener(e -> loadCitasData());
        cmbEstado.addActionListener(e -> loadCitasData());
        cmbServicio.addActionListener(e -> loadCitasData());
        
        comboPanel.add(createFilter("VETERINARIO", cmbVeterinario));
        comboPanel.add(createFilter("ESTADO", cmbEstado));
        comboPanel.add(createFilter("SERVICIO", cmbServicio));
        filtersPanel.add(comboPanel, BorderLayout.WEST);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnEditar = new JButton("✏️ Editar");
        JButton btnEliminar = new JButton("🗑️ Eliminar");
        
        btnEditar.addActionListener(e -> {
            int row = citasTable.getSelectedRow();
            if (row >= 0 && row < displayedCitas.size()) {
                Cita c = displayedCitas.get(row);
                abrirDialogoEdicion(c);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una cita primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        btnEliminar.addActionListener(e -> {
            int row = citasTable.getSelectedRow();
            if (row >= 0 && row < displayedCitas.size()) {
                Cita c = displayedCitas.get(row);
                eliminarCita(c);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una cita primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        actionPanel.add(btnEditar);
        actionPanel.add(btnEliminar);
        filtersPanel.add(actionPanel, BorderLayout.EAST);

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.add(headerNorth, BorderLayout.NORTH);
        topSection.add(filtersPanel, BorderLayout.CENTER);
        topSection.add(new JSeparator(), BorderLayout.SOUTH);
        
        add(topSection, BorderLayout.NORTH);

        // 3. Main Content
        JPanel mainContent = new JPanel(new BorderLayout(15, 0));
        
        // --- CITAS TABLE ---
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        String[] columnNames = {"ID", "Fecha", "Hora", "Paciente", "Veterinario", "Motivo", "Estado"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        citasTable = new JTable(tableModel);
        citasTable.setRowHeight(40);
        citasTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        citasTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        citasTable.getTableHeader().setBackground(new Color(240, 240, 240));
        citasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        citasTable.setSelectionBackground(new Color(220, 235, 255));
        
        // Custom renderer for Status column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        citasTable.getColumnModel().getColumn(0).setMaxWidth(50);
        citasTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        citasTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        citasTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

        citasTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = citasTable.getSelectedRow();
                    if (row >= 0 && row < displayedCitas.size()) {
                        Cita c = displayedCitas.get(row);
                        abrirDialogoEdicion(c);
                    }
                }
            }
        });
        
        populateVeterinariosCombo();
        loadCitasData();
        
        tableWrapper.add(citasTable.getTableHeader(), BorderLayout.NORTH);
        tableWrapper.add(new JScrollPane(citasTable), BorderLayout.CENTER);
        mainContent.add(tableWrapper, BorderLayout.CENTER);

        
        // Remove empty right sidebar
        add(mainContent, BorderLayout.CENTER);
    }
    
    private void loadCitasData() {
        tableModel.setRowCount(0);
        pacienteNombres.clear();
        for (Paciente p : pacienteDao.getAll()) {
            pacienteNombres.put(p.getId(), p.getNombre());
        }
        
        personalNombres.clear();
        for (Personal p : personalDao.getAll()) {
            personalNombres.put(p.getId(), p.getNombre());
        }
        
        List<Cita> allCitas = citaDao.getAll();
        displayedCitas = new java.util.ArrayList<>();
        
        LocalDate today = LocalDate.now();
        
        String selVet = (String) cmbVeterinario.getSelectedItem();
        String selEstado = (String) cmbEstado.getSelectedItem();
        String selServ = (String) cmbServicio.getSelectedItem();
        
        int countCitas = 0;
        int countAtendidos = 0;
        int countPendientes = 0;
        
        for (Cita c : allCitas) {
            if (c.getFechaHora() != null && !c.getFechaHora().toLocalDate().isBefore(today)) {
                
                // Aplicar filtros
                boolean matchVet = selVet == null || selVet.equals("Todos los Doctores") || 
                                   (veterinarioIdsMap.containsKey(selVet) && c.getVeterinarioId() == veterinarioIdsMap.get(selVet));
                
                boolean matchEstado = selEstado == null || selEstado.equals("Todos los Estados") || 
                                      (c.getEstado() != null && c.getEstado().name().equals(selEstado));
                
                boolean matchServ = selServ == null || selServ.equals("Todos los Servicios") || 
                                    (c.getMotivo() != null && c.getMotivo().equalsIgnoreCase(selServ));
                
                if (matchVet && matchEstado && matchServ) {
                    displayedCitas.add(c);
                    
                    // Solo contamos para estadísticas las que pasan los filtros
                    countCitas++;
                    if (c.getEstado() == Cita.EstadoCita.COMPLETADA) {
                        countAtendidos++;
                    } else if (c.getEstado() == Cita.EstadoCita.PENDIENTE || c.getEstado() == Cita.EstadoCita.URGENCIA) {
                        countPendientes++;
                    }
                }
            }
        }
        
        // Update stats UI
        cardCitas.updateData(String.valueOf(countCitas));
        cardAtendidos.updateData(String.valueOf(countAtendidos));
        cardPend.updateData(String.valueOf(countPendientes));
        
        // Sort chronologically
        Collections.sort(displayedCitas, new Comparator<Cita>() {
            @Override
            public int compare(Cita c1, Cita c2) {
                if (c1.getFechaHora() == null || c2.getFechaHora() == null) return 0;
                return c1.getFechaHora().compareTo(c2.getFechaHora());
            }
        });
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        for (Cita c : displayedCitas) {
            Object[] row = new Object[7];
            row[0] = c.getId();
            row[1] = c.getFechaHora() != null ? c.getFechaHora().format(dateFormatter) : "";
            row[2] = c.getFechaHora() != null ? c.getFechaHora().format(timeFormatter) : "";
            row[3] = pacienteNombres.getOrDefault(c.getPacienteId(), "ID: " + c.getPacienteId());
            row[4] = personalNombres.getOrDefault(c.getVeterinarioId(), "ID: " + c.getVeterinarioId());
            row[5] = c.getMotivo();
            row[6] = c.getEstado() != null ? c.getEstado().toString() : "";
            
            tableModel.addRow(row);
        }
    }
    
    private void populateVeterinariosCombo() {
        cmbVeterinario.removeAllItems();
        cmbVeterinario.addItem("Todos los Doctores");
        veterinarioIdsMap.clear();
        for (Personal p : personalDao.getAll()) {
            String name = p.getNombre();
            cmbVeterinario.addItem(name);
            veterinarioIdsMap.put(name, p.getId());
        }
    }
    
    private void abrirDialogoEdicion(Cita c) {
        Window parentWindow = SwingUtilities.windowForComponent(this);
        RegistroCitasBasic dialog = new RegistroCitasBasic((Frame) parentWindow);
        dialog.setCitaToEdit(c);
        dialog.setVisible(true);
        
        if (dialog.isSaved()) {
            citaDao.update(dialog.getNuevaCita());
            loadCitasData();
            JOptionPane.showMessageDialog(this, "Cita actualizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void eliminarCita(Cita c) {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar esta cita?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            citaDao.delete(c.getId());
            loadCitasData();
            JOptionPane.showMessageDialog(this, "Cita eliminada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }



    private JPanel createFilter(String labelText, JComboBox<String> combo) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        JLabel l = new JLabel(labelText);
        l.setFont(new Font("SansSerif", Font.BOLD, 10));
        p.add(l);
        p.add(combo);
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