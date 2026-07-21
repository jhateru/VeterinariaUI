package society.view;

import society.modell.recepcion.Cita;
import society.modell.recepcion.Paciente;
import society.modell.administracion.Personal;
import society.dao.PacienteDao;
import society.dao.PersonalDao;
import society.dao.DuenoDao;
import society.modell.recepcion.Dueno;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class RegistroCitasBasic extends JDialog {

    private boolean saved = false;
    private Cita nuevaCita;

    // UI Components
    private JComboBox<String> cbPacientes;
    private JPanel patientCardPanel;
    private JLabel lblPatientName;
    private JLabel lblPatientInfo;
    private JLabel lblPatientOwner;

    private ButtonGroup motivoGroup;
    private JTextArea txtNotas;
    private JComboBox<Cita.EstadoCita> cbEstado;

    private JPanel vetsContainer;
    private ButtonGroup vetsGroup;
    private int selectedVeterinarioId = -1;
    private int selectedPacienteId = -1;

    private JDateChooser dateChooser;
    private JPanel timeSlotsContainer;
    private String selectedTimeStr = null;
    
    private List<Paciente> pacientes;
    private List<Personal> veterinarios;

    private Color tealColor = new Color(0, 96, 103);
    private Color lightBg = new Color(248, 250, 252);
    private Color borderCol = new Color(226, 232, 240);

    public RegistroCitasBasic(Frame parent) {
        super(parent, "Nueva Cita", true);
        setSize(800, 600);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(10, 20, 5, 20));
        
        JLabel lblTitle = new JLabel("Nueva Cita");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(30, 41, 59)); // slate-800
        JLabel lblSub = new JLabel("Configure los detalles para el nuevo encuentro médico.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(100, 116, 139)); // slate-500
        
        JPanel titleWrap = new JPanel(new GridLayout(2, 1));
        titleWrap.setBackground(Color.WHITE);
        titleWrap.add(lblTitle);
        titleWrap.add(lblSub);
        headerPanel.add(titleWrap, BorderLayout.WEST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Grid Content (Left and Right columns)
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(lightBg);
        contentPanel.setBorder(new EmptyBorder(5, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        // --- LEFT COLUMN ---
        // 1. Patient & Owner
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(createPatientPanel(), gbc);

        // 2. Appointment Details
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(createDetailsPanel(), gbc);

        // --- RIGHT COLUMN ---
        // 3. Veterinarian
        gbc.gridx = 1; gbc.gridy = 0;
        contentPanel.add(createVeterinarianPanel(), gbc);

        // 4. Schedule
        gbc.gridx = 1; gbc.gridy = 1;
        contentPanel.add(createSchedulePanel(), gbc);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Footer Actions
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, borderCol));
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setForeground(new Color(100, 116, 139));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton btnConfirmar = new JButton("Confirmar Cita");
        btnConfirmar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnConfirmar.setBackground(tealColor);
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnCancelar.addActionListener(e -> dispose());
        btnConfirmar.addActionListener(e -> guardar());
        
        footerPanel.add(btnCancelar);
        footerPanel.add(btnConfirmar);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        
        loadData();
    }
    
    private JPanel createRoundedPanel(String titleStr) {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderCol, 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        if (titleStr != null) {
            JLabel title = new JLabel(titleStr);
            title.setFont(new Font("Segoe UI", Font.BOLD, 16));
            title.setForeground(tealColor);
            p.add(title, BorderLayout.NORTH);
        }
        return p;
    }

    private JPanel createPatientPanel() {
        JPanel p = createRoundedPanel("Paciente & Dueño");
        
        JPanel searchWrap = new JPanel(new BorderLayout(5, 5));
        searchWrap.setBackground(Color.WHITE);
        
        cbPacientes = new JComboBox<>();
        cbPacientes.addItem("Seleccione un paciente...");
        cbPacientes.setBackground(Color.WHITE);
        cbPacientes.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                l.setBorder(new EmptyBorder(5, 5, 5, 5));
                return l;
            }
        });
        cbPacientes.addActionListener(e -> updatePatientCard());
        
        searchWrap.add(cbPacientes, BorderLayout.NORTH);
        
        patientCardPanel = new JPanel(new BorderLayout(10, 0));
        patientCardPanel.setBackground(lightBg);
        patientCardPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        patientCardPanel.setVisible(false);
        
        JLabel avatar = new JLabel("🐾");
        avatar.setFont(new Font("SansSerif", Font.PLAIN, 32));
        
        JPanel info = new JPanel(new GridLayout(3, 1));
        info.setBackground(lightBg);
        lblPatientName = new JLabel("");
        lblPatientName.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblPatientInfo = new JLabel("");
        lblPatientInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPatientInfo.setForeground(new Color(100, 116, 139));
        lblPatientOwner = new JLabel("");
        lblPatientOwner.setForeground(tealColor);
        
        info.add(lblPatientName);
        info.add(lblPatientInfo);
        info.add(lblPatientOwner);
        
        patientCardPanel.add(avatar, BorderLayout.WEST);
        patientCardPanel.add(info, BorderLayout.CENTER);
        
        searchWrap.add(patientCardPanel, BorderLayout.CENTER);
        p.add(searchWrap, BorderLayout.CENTER);
        
        return p;
    }

    private JPanel createDetailsPanel() {
        JPanel p = createRoundedPanel("Detalles de Cita");
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Color.WHITE);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = 0; gc.gridy = 0; gc.weightx = 1.0; gc.insets = new Insets(5, 0, 5, 0);

        JLabel lblMotivo = new JLabel("Motivo de Consulta");
        lblMotivo.setFont(new Font("SansSerif", Font.BOLD, 12));
        content.add(lblMotivo, gc);

        JPanel togglePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        togglePanel.setBackground(Color.WHITE);
        motivoGroup = new ButtonGroup();
        String[] motivos = {"Vacunación", "Control", "Cirugía", "Urgencia"};
        for (String m : motivos) {
            JToggleButton btn = new JToggleButton(m);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(30, 41, 59));
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createLineBorder(borderCol, 1, true));
            btn.setActionCommand(m);
            
            // UI logic for toggle selection
            btn.addItemListener(e -> {
                if (btn.isSelected()) {
                    btn.setBackground(tealColor);
                    btn.setForeground(Color.WHITE);
                } else {
                    btn.setBackground(Color.WHITE);
                    btn.setForeground(new Color(30, 41, 59));
                }
            });
            
            motivoGroup.add(btn);
            togglePanel.add(btn);
        }
        gc.gridy++;
        content.add(togglePanel, gc);

        gc.gridy++;
        JLabel lblNotas = new JLabel("Notas adicionales / Síntomas");
        lblNotas.setFont(new Font("SansSerif", Font.BOLD, 12));
        content.add(lblNotas, gc);
        
        gc.gridy++;
        txtNotas = new JTextArea(8, 20);
        txtNotas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtNotas.setLineWrap(true);
        txtNotas.setWrapStyleWord(true);
        txtNotas.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane scrollNotas = new JScrollPane(txtNotas);
        scrollNotas.setBorder(BorderFactory.createLineBorder(borderCol, 1, true));
        content.add(scrollNotas, gc);
        


        p.add(content, BorderLayout.CENTER);
        return p;
    }

    private JPanel createVeterinarianPanel() {
        JPanel p = createRoundedPanel("Veterinario");
        
        vetsContainer = new JPanel();
        vetsContainer.setLayout(new BoxLayout(vetsContainer, BoxLayout.Y_AXIS));
        vetsContainer.setBackground(Color.WHITE);
        
        JScrollPane scroll = new JScrollPane(vetsContainer);
        scroll.setBorder(null);
        p.add(scroll, BorderLayout.CENTER);
        
        return p;
    }

    private JPanel createSchedulePanel() {
        JPanel p = createRoundedPanel("Horario");
        
        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBackground(Color.WHITE);
        
        dateChooser = new JDateChooser();
        dateChooser.setDate(new Date());
        content.add(dateChooser, BorderLayout.NORTH);
        
        JPanel timeWrap = new JPanel(new BorderLayout(5, 5));
        timeWrap.setBackground(Color.WHITE);
        JLabel lblTime = new JLabel("Horas Disponibles");
        lblTime.setFont(new Font("SansSerif", Font.BOLD, 12));
        timeWrap.add(lblTime, BorderLayout.NORTH);
        
        timeSlotsContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        timeSlotsContainer.setBackground(Color.WHITE);
        timeSlotsContainer.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JComboBox<String> horaCombo = new JComboBox<>();
        for (int i = 8; i <= 20; i++) {
            horaCombo.addItem(String.format("%02d", i));
        }
        
        JComboBox<String> minutoCombo = new JComboBox<>(new String[]{"00", "15", "30", "45"});
        
        timeSlotsContainer.add(horaCombo);
        timeSlotsContainer.add(minutoCombo);
        
        // Listeners to update selectedTimeStr
        java.awt.event.ActionListener timeListener = e -> {
            selectedTimeStr = horaCombo.getSelectedItem() + ":" + minutoCombo.getSelectedItem();
        };
        horaCombo.addActionListener(timeListener);
        minutoCombo.addActionListener(timeListener);
        
        // Initialize default
        horaCombo.setSelectedIndex(0);
        
        timeWrap.add(timeSlotsContainer, BorderLayout.CENTER);
        content.add(timeWrap, BorderLayout.CENTER);
        
        JPanel estadoWrap = new JPanel(new BorderLayout(5, 5));
        estadoWrap.setBackground(Color.WHITE);
        JLabel lblEstado = new JLabel("Estado de Cita");
        lblEstado.setFont(new Font("SansSerif", Font.BOLD, 12));
        estadoWrap.add(lblEstado, BorderLayout.NORTH);
        
        cbEstado = new JComboBox<>(Cita.EstadoCita.values());
        cbEstado.setBackground(Color.WHITE);
        cbEstado.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                l.setBorder(new EmptyBorder(5, 5, 5, 5));
                return l;
            }
        });
        estadoWrap.add(cbEstado, BorderLayout.CENTER);
        
        content.add(estadoWrap, BorderLayout.SOUTH);
        
        p.add(content, BorderLayout.CENTER);
        return p;
    }

    private void loadData() {
        // Load Pacientes
        PacienteDao pDao = new PacienteDao();
        pacientes = pDao.getAll();
        for (Paciente pac : pacientes) {
            cbPacientes.addItem(pac.getNombre());
        }

        // Load Veterinarios
        PersonalDao perDao = new PersonalDao();
        veterinarios = perDao.getAll();
        vetsGroup = new ButtonGroup();
        
        for (Personal per : veterinarios) {
            if ("Veterinario".equalsIgnoreCase(per.getCargo())) {
                JToggleButton btn = new JToggleButton();
                btn.setLayout(new BorderLayout(10, 10));
                btn.setBackground(Color.WHITE);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(borderCol),
                        new EmptyBorder(10, 10, 10, 10)
                ));
                btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
                
                JLabel av = new JLabel("👨‍⚕️");
                av.setFont(new Font("SansSerif", Font.PLAIN, 24));
                
                JPanel texts = new JPanel(new GridLayout(2, 1));
                texts.setOpaque(false);
                JLabel n = new JLabel(per.getNombre());
                n.setFont(new Font("SansSerif", Font.BOLD, 13));
                JLabel desc = new JLabel(per.getEspecialidad() != null ? per.getEspecialidad() : "Medicina General");
                desc.setFont(new Font("SansSerif", Font.PLAIN, 11));
                desc.setForeground(Color.GRAY);
                texts.add(n);
                texts.add(desc);
                
                btn.add(av, BorderLayout.WEST);
                btn.add(texts, BorderLayout.CENTER);
                
                btn.addActionListener(e -> {
                    selectedVeterinarioId = per.getId();
                    for(Component c : vetsContainer.getComponents()){
                        if(c instanceof JToggleButton){
                            JToggleButton t = (JToggleButton)c;
                            if(t.isSelected()){
                                t.setBackground(tealColor);
                                // Workaround for simple styling
                            } else {
                                t.setBackground(Color.WHITE);
                            }
                        }
                    }
                });
                
                vetsGroup.add(btn);
                vetsContainer.add(btn);
                vetsContainer.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
    }

    private void updatePatientCard() {
        int idx = cbPacientes.getSelectedIndex() - 1; // -1 because of prompt
        if (idx >= 0 && idx < pacientes.size()) {
            Paciente p = pacientes.get(idx);
            selectedPacienteId = p.getId();
            
            lblPatientName.setText(p.getNombre());
            String info = (p.getRaza() != null ? p.getRaza() : "Desconocida") + " • " + (p.getSexo() != null ? p.getSexo() : "?");
            lblPatientInfo.setText(info);
            
            Dueno d = new DuenoDao().getById(p.getDuenoId());
            lblPatientOwner.setText("Dueño: " + (d != null ? d.getNombre() : "Sin Dueño"));
            
            patientCardPanel.setVisible(true);
        } else {
            selectedPacienteId = -1;
            patientCardPanel.setVisible(false);
        }
        revalidate();
        repaint();
    }

    private void guardar() {
        if (selectedPacienteId == -1 || selectedVeterinarioId == -1 || dateChooser.getDate() == null || selectedTimeStr == null || motivoGroup.getSelection() == null) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios (Paciente, Veterinario, Motivo, Fecha, Hora).", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date selectedDate = dateChooser.getDate();
        LocalDate datePart = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime timePart = LocalTime.parse(selectedTimeStr);
        LocalDateTime fechaHora = LocalDateTime.of(datePart, timePart);

        // Use appending the text area to the toggled "motivo"
        String extraNotes = txtNotas.getText().trim();
        String finalMotivo = motivoGroup.getSelection().getActionCommand();
        if(!extraNotes.isEmpty()) {
            finalMotivo += " - " + extraNotes;
        }

        if (nuevaCita == null) {
            nuevaCita = new Cita();
            nuevaCita.setId(new Random().nextInt(10000));
        }

        nuevaCita.setPacienteId(selectedPacienteId);
        nuevaCita.setVeterinarioId(selectedVeterinarioId);
        nuevaCita.setMotivo(finalMotivo);
        nuevaCita.setFechaHora(fechaHora);
        nuevaCita.setEstado((Cita.EstadoCita) cbEstado.getSelectedItem());

        saved = true;
        dispose();
    }

    public void setCitaToEdit(Cita c) {
        this.nuevaCita = c;
        setTitle("Editar Cita");
        
        if(pacientes != null){
            for(int i = 0; i < pacientes.size(); i++){
                if(pacientes.get(i).getId() == c.getPacienteId()){
                    cbPacientes.setSelectedIndex(i + 1);
                    break;
                }
            }
        }
        
        // Find vet button
        if(veterinarios != null){
            int idx = 0;
            for(Personal per : veterinarios){
                if ("Veterinario".equalsIgnoreCase(per.getCargo())) {
                    if(per.getId() == c.getVeterinarioId()){
                        Component[] comps = vetsContainer.getComponents();
                        // every other component is RigidArea
                        if(idx * 2 < comps.length && comps[idx*2] instanceof JToggleButton) {
                            ((JToggleButton)comps[idx*2]).doClick();
                        }
                        break;
                    }
                    idx++;
                }
            }
        }
        
        String mot = c.getMotivo();
        if(mot != null) {
            if(mot.contains(" - ")) {
                String[] parts = mot.split(" - ", 2);
                txtNotas.setText(parts[1]);
                selectMotivo(parts[0]);
            } else {
                selectMotivo(mot);
            }
        }

        if (c.getFechaHora() != null) {
            Date out = Date.from(c.getFechaHora().atZone(ZoneId.systemDefault()).toInstant());
            dateChooser.setDate(out);
            
            // Format time slot
            String h = String.format("%02d", c.getFechaHora().getHour());
            String m = String.format("%02d", c.getFechaHora().getMinute());
            
            if(timeSlotsContainer.getComponentCount() == 2){
                JComboBox<String> hc = (JComboBox<String>) timeSlotsContainer.getComponent(0);
                JComboBox<String> mc = (JComboBox<String>) timeSlotsContainer.getComponent(1);
                hc.setSelectedItem(h);
                mc.setSelectedItem(m);
            }
        }
        
        if (c.getEstado() != null) {
            cbEstado.setSelectedItem(c.getEstado());
        }
    }
    
    private void selectMotivo(String actionCommand) {
        if (motivoGroup == null) return;
        java.util.Enumeration<AbstractButton> elements = motivoGroup.getElements();
        while (elements.hasMoreElements()) {
            AbstractButton button = elements.nextElement();
            if (button.getActionCommand().equals(actionCommand)) {
                button.doClick();
                break;
            }
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public Cita getNuevaCita() {
        return nuevaCita;
    }
}
