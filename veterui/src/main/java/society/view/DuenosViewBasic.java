package society.view;

import society.dao.DuenoDao;
import society.modell.recepcion.Dueno;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class DuenosViewBasic extends JPanel {

    private Color darkTeal = new Color(0, 100, 100);
    private Color lightBg = new Color(248, 250, 250);
    private Color grayText = new Color(130, 130, 130);

    private DuenoDao duenoDao;
    private List<Dueno> duenos;
    
    private JPanel gridPanel;
    private String currentFilter = "Todos";
    
    // UI Filters and Selection
    private JButton btnTodos;
    private JButton btnFrecuentes;
    private JButton btnNuevos;
    
    private JTextField txtBuscar;
    private JButton btnEditar;
    private JButton btnEliminar;
    
    private Dueno selectedDueno;
    private JPanel selectedPanel;
    
    public DuenosViewBasic() {
        duenoDao = new DuenoDao();
        
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header & Filters
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Directorio de Dueños");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        
        // Breadcrumb
        JPanel breadcrumb = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        breadcrumb.setBackground(Color.WHITE);
        JLabel lblB = new JLabel("<html><span style='color:gray; font-size:10px;'>Inicio &gt; <b style='color:#006464;'>Dueños</b></span></html>");
        breadcrumb.add(lblB);
        
        JPanel titleArea = new JPanel(new BorderLayout());
        titleArea.setBackground(Color.WHITE);
        titleArea.add(title, BorderLayout.NORTH);
        titleArea.add(breadcrumb, BorderLayout.SOUTH);
        
        header.add(titleArea, BorderLayout.WEST);

        JButton btnNuevo = new JButton("👤+ Nuevo Dueño");
        btnNuevo.setBackground(darkTeal);
        btnNuevo.setForeground(Color.WHITE);
        btnNuevo.setFocusPainted(false);
        btnNuevo.addActionListener(e -> {
            RegistroDuenoBasic modal = new RegistroDuenoBasic((Frame) SwingUtilities.getWindowAncestor(this));
            modal.setVisible(true);
            if (modal.isSaved()) {
                duenoDao.save(modal.getDueno());
                cargarDatos();
            }
        });
        header.add(btnNuevo, BorderLayout.EAST);
        
        topPanel.add(header, BorderLayout.NORTH);

        // Filters Toolbar
        JPanel filters = new JPanel(new BorderLayout());
        filters.setBackground(Color.WHITE);
        filters.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        JPanel leftFilters = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftFilters.setBackground(Color.WHITE);
        
        txtBuscar = new JTextField(15);
        txtBuscar.setToolTipText("Buscar por Nombre Completo...");
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                renderGrid();
            }
        });
        leftFilters.add(new JLabel("🔍 "));
        leftFilters.add(txtBuscar);
        
        btnTodos = createFilterBtn("Todos");
        btnFrecuentes = createFilterBtn("Frecuentes");
        btnNuevos = createFilterBtn("Nuevos (Mes)");
        
        leftFilters.add(btnTodos);
        leftFilters.add(btnFrecuentes);
        leftFilters.add(btnNuevos);
        
        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightActions.setBackground(Color.WHITE);
        
        btnEditar = new JButton("✏️ Editar");
        btnEditar.setEnabled(false);
        btnEditar.addActionListener(e -> {
            if (selectedDueno != null) {
                RegistroDuenoBasic modal = new RegistroDuenoBasic((Frame) SwingUtilities.getWindowAncestor(this));
                modal.setDuenoToEdit(selectedDueno);
                modal.setVisible(true);
                if (modal.isSaved()) {
                    duenoDao.save(modal.getDueno());
                    cargarDatos();
                }
            }
        });
        
        btnEliminar = new JButton("🗑️ Eliminar");
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(e -> {
            if (selectedDueno != null) {
                int resp = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar este dueño?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    List<Dueno> currentList = duenoDao.getAll();
                    currentList.removeIf(d -> d.getId() == selectedDueno.getId());
                    duenoDao.saveAll(currentList);
                    selectedDueno = null;
                    updateActionButtons();
                    cargarDatos();
                }
            }
        });
        
        rightActions.add(btnEditar);
        rightActions.add(btnEliminar);
        
        filters.add(leftFilters, BorderLayout.WEST);
        filters.add(rightActions, BorderLayout.EAST);

        topPanel.add(filters, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Grid Area
        gridPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
        
        // Floating action button simulado a la derecha abajo
        JPanel floatingWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        floatingWrap.setOpaque(false);
        
        cargarDatos();
    }
    
    private JButton createFilterBtn(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.DARK_GRAY);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(5, 15, 5, 15)
        ));
        btn.addActionListener(e -> {
            currentFilter = text;
            updateFilterStyles();
            renderGrid();
        });
        return btn;
    }
    
    private void updateFilterStyles() {
        JButton[] btns = {btnTodos, btnFrecuentes, btnNuevos};
        for (JButton b : btns) {
            if (b.getText().startsWith(currentFilter)) {
                b.setBackground(darkTeal);
                b.setForeground(Color.WHITE);
            } else {
                b.setBackground(Color.WHITE);
                b.setForeground(Color.DARK_GRAY);
            }
        }
    }

    private void cargarDatos() {
        duenos = duenoDao.getAll();
        updateFilterStyles();
        if (duenos != null) {
            btnTodos.setText("Todos (" + duenos.size() + ")");
        }
        renderGrid();
    }
    
    private void updateActionButtons() {
        boolean hasSelection = selectedDueno != null;
        btnEditar.setEnabled(hasSelection);
        btnEliminar.setEnabled(hasSelection);
    }
    
    private void renderGrid() {
        gridPanel.removeAll();
        selectedDueno = null;
        selectedPanel = null;
        updateActionButtons();
        
        if (duenos != null) {
            String searchText = txtBuscar.getText().toLowerCase();
            
            List<Dueno> filtrados = duenos.stream().filter(d -> {
                boolean matchStatus = true;
                if (currentFilter.startsWith("Frecuentes")) matchStatus = d.getEstado() == Dueno.EstadoDueno.FRECUENTE;
                if (currentFilter.startsWith("Nuevos")) matchStatus = d.getEstado() == Dueno.EstadoDueno.NUEVO;
                
                boolean matchSearch = true;
                if (!searchText.isEmpty() && d.getNombre() != null) {
                    matchSearch = d.getNombre().toLowerCase().contains(searchText);
                }
                
                return matchStatus && matchSearch;
            }).collect(Collectors.toList());
            
            for (Dueno d : filtrados) {
                gridPanel.add(buildOwnerCard(d));
            }
        }
        
        gridPanel.revalidate();
        gridPanel.repaint();
    }
    
    private JPanel buildOwnerCard(Dueno d) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));

        // Header: Avatar, Name, Status, Options
        JPanel topArea = new JPanel(new BorderLayout(10, 0));
        topArea.setBackground(Color.WHITE);
        
        // Avatar (initials or photo placeholder)
        JLabel lblAvatar = new JLabel();
        lblAvatar.setOpaque(true);
        lblAvatar.setBackground(new Color(200, 200, 180));
        lblAvatar.setForeground(Color.BLACK);
        lblAvatar.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblAvatar.setHorizontalAlignment(SwingConstants.CENTER);
        lblAvatar.setPreferredSize(new Dimension(50, 50));
        if (d.getNombre() != null && !d.getNombre().isEmpty()) {
            lblAvatar.setText(d.getNombre().substring(0, 1).toUpperCase());
        }
        topArea.add(lblAvatar, BorderLayout.WEST);
        
        JPanel infoCenter = new JPanel();
        infoCenter.setLayout(new BoxLayout(infoCenter, BoxLayout.Y_AXIS));
        infoCenter.setBackground(Color.WHITE);
        
        JLabel lblName = new JLabel(d.getNombre() + " " + (d.getApellidos() != null ? d.getApellidos() : ""));
        lblName.setFont(new Font("SansSerif", Font.PLAIN, 15));
        
        JLabel lblId = new JLabel("ID: " + d.getDni());
        lblId.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblId.setForeground(grayText);
        
        JLabel lblStatus = new JLabel(" " + (d.getEstado() != null ? d.getEstado().name() : "NUEVO") + " ");
        lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblStatus.setOpaque(true);
        if (d.getEstado() == Dueno.EstadoDueno.DEUDA_PENDIENTE) {
            lblStatus.setBackground(new Color(255, 230, 230));
            lblStatus.setForeground(new Color(200, 50, 50));
        } else if (d.getEstado() == Dueno.EstadoDueno.FRECUENTE) {
            lblStatus.setBackground(new Color(230, 240, 255));
            lblStatus.setForeground(new Color(50, 100, 200));
        } else {
            lblStatus.setBackground(new Color(240, 240, 240));
            lblStatus.setForeground(Color.DARK_GRAY);
        }
        
        infoCenter.add(lblName);
        infoCenter.add(lblId);
        infoCenter.add(Box.createRigidArea(new Dimension(0, 4)));
        
        JPanel statusWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statusWrap.setBackground(Color.WHITE);
        statusWrap.add(lblStatus);
        infoCenter.add(statusWrap);
        
        topArea.add(infoCenter, BorderLayout.CENTER);
        
        JLabel lblDots = new JLabel("⋮");
        lblDots.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblDots.setForeground(Color.GRAY);
        lblDots.setCursor(new Cursor(Cursor.HAND_CURSOR));
        topArea.add(lblDots, BorderLayout.EAST);
        
        // Contact details
        JPanel midArea = new JPanel();
        midArea.setLayout(new BoxLayout(midArea, BoxLayout.Y_AXIS));
        midArea.setBackground(Color.WHITE);
        midArea.setBorder(new EmptyBorder(15, 0, 10, 0));
        
        JLabel lblPhone = new JLabel("📞 " + (d.getTelefono() != null ? d.getTelefono() : ""));
        lblPhone.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblPhone.setForeground(Color.DARK_GRAY);
        
        JLabel lblEmail = new JLabel("✉ " + (d.getEmail() != null ? d.getEmail() : ""));
        lblEmail.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblEmail.setForeground(Color.DARK_GRAY);
        
        midArea.add(lblPhone);
        midArea.add(Box.createRigidArea(new Dimension(0, 5)));
        midArea.add(lblEmail);
        
        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(230, 230, 230));
        
        // Mascotas
        JPanel petArea = new JPanel(new BorderLayout());
        petArea.setBackground(Color.WHITE);
        petArea.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JLabel lblPetTitle = new JLabel("MASCOTAS REGISTRADAS");
        lblPetTitle.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblPetTitle.setForeground(grayText);
        petArea.add(lblPetTitle, BorderLayout.NORTH);
        
        JPanel tagsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        tagsPanel.setBackground(Color.WHITE);
        
        final String[] selectedPetName = new String[]{null};
        final JLabel[] selectedPetLabel = new JLabel[]{null};

        society.dao.PacienteDao pDao = new society.dao.PacienteDao();
        List<society.modell.recepcion.Paciente> allPets = pDao.getAll();
        List<society.modell.recepcion.Paciente> ownerPets = new java.util.ArrayList<>();
        for (society.modell.recepcion.Paciente p : allPets) {
            if (p.getDuenoId() == d.getId()) {
                ownerPets.add(p);
            }
        }
        
        if (!ownerPets.isEmpty()) {
            for (society.modell.recepcion.Paciente p : ownerPets) {
                String petName = p.getNombre().trim();
                if (petName.isEmpty()) continue;
                JLabel lblTag = new JLabel(" 🐾 " + petName + " ");
                lblTag.setFont(new Font("SansSerif", Font.PLAIN, 11));
                lblTag.setOpaque(true);
                lblTag.setBackground(new Color(240, 240, 240));
                lblTag.setForeground(Color.DARK_GRAY);
                lblTag.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                lblTag.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (selectedPetLabel[0] != null) {
                            selectedPetLabel[0].setBackground(new Color(240, 240, 240));
                            selectedPetLabel[0].setForeground(Color.DARK_GRAY);
                        }
                        selectedPetLabel[0] = lblTag;
                        selectedPetName[0] = petName;
                        lblTag.setBackground(new Color(150, 230, 230));
                        lblTag.setForeground(darkTeal);
                    }
                });
                
                tagsPanel.add(lblTag);
            }
        } else {
            JLabel lblNo = new JLabel(" Ninguna ");
            lblNo.setForeground(Color.LIGHT_GRAY);
            tagsPanel.add(lblNo);
        }
        petArea.add(tagsPanel, BorderLayout.CENTER);
        
        // Action Buttons
        JPanel bottomArea = new JPanel(new BorderLayout(10, 0));
        bottomArea.setBackground(Color.WHITE);
        
        JButton btnHistorial = new JButton("Registro de Mascota");
        btnHistorial.setBackground(Color.WHITE);
        btnHistorial.setForeground(darkTeal);
        btnHistorial.setFocusPainted(false);
        btnHistorial.addActionListener(e -> {
            if (selectedPetName[0] != null) {
                List<society.modell.recepcion.Paciente> plist = pDao.getAll();
                society.modell.recepcion.Paciente found = null;
                for (society.modell.recepcion.Paciente p : plist) {
                    if (p.getNombre().equalsIgnoreCase(selectedPetName[0]) && p.getDuenoId() == d.getId()) {
                        found = p;
                        break;
                    }
                }
                if (found != null) {
                    RegistroPacienteBasic modal = new RegistroPacienteBasic((Frame) SwingUtilities.getWindowAncestor(this));
                    modal.setPacienteToEdit(found);
                    modal.setVisible(true);
                    if (modal.isSaved()) {
                        pDao.update(modal.getPaciente());
                        cargarDatos();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró el paciente en la base de datos completa.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                MainViewBasic main = (MainViewBasic) SwingUtilities.getAncestorOfClass(MainViewBasic.class, this);
                if (main != null) {
                    main.cargarVista("PacientesViewBasic");
                    PacientesViewBasic pView = (PacientesViewBasic) main.getView("PacientesViewBasic");
                    if (pView != null) {
                        pView.abrirRegistroParaDueno(d);
                    }
                }
            }
        });
        
        bottomArea.add(btnHistorial, BorderLayout.CENTER);
        

        // Assemble Card
        JPanel mainWrap = new JPanel(new BorderLayout());
        mainWrap.setBackground(Color.WHITE);
        
        JPanel topWrap = new JPanel(new BorderLayout());
        topWrap.setBackground(Color.WHITE);
        topWrap.add(topArea, BorderLayout.NORTH);
        topWrap.add(midArea, BorderLayout.CENTER);
        topWrap.add(sep, BorderLayout.SOUTH);
        
        mainWrap.add(topWrap, BorderLayout.NORTH);
        mainWrap.add(petArea, BorderLayout.CENTER);
        mainWrap.add(bottomArea, BorderLayout.SOUTH);
        
        card.add(mainWrap, BorderLayout.CENTER);
        
        // Selection Logic
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedPanel != null) {
                    selectedPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                        new EmptyBorder(15, 15, 15, 15)
                    ));
                    selectedPanel.setBackground(Color.WHITE);
                }
                
                selectedDueno = d;
                selectedPanel = card;
                
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(darkTeal, 2, true),
                    new EmptyBorder(14, 14, 14, 14)
                ));
                card.setBackground(new Color(245, 250, 250));
                
                updateActionButtons();
            }
        });
        
        // Removed old dot menu click handler
        
        return card;
    }
}
