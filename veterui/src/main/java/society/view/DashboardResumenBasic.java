package society.view;

import society.dao.CitaDao;
import society.dao.PacienteDao;
import society.dao.ProveedoresDao;
import society.dao.PersonalDao;
import society.modell.recepcion.Cita;
import society.modell.recepcion.Paciente;
import society.modell.administracion.Proveedores;
import society.modell.administracion.Personal;
import society.view.components.CardAlerta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardResumenBasic extends JPanel {

    private Color brandBlue = new Color(0, 80, 100);
    private Color lightBg = new Color(248, 250, 250);

    private CitaDao citaDao;
    private PacienteDao pacienteDao;
    private ProveedoresDao proveedoresDao;
    private PersonalDao personalDao;

    private CardAlerta cardCitasHoy;
    private CardAlerta cardInternados;
    private CardAlerta cardProveedores;
    private CardAlerta cardPersonal;

    private JTable tablaCitasHoy;
    private DefaultTableModel modeloCitasHoy;
    private JPanel alertasPanel;

    public DashboardResumenBasic() {
        citaDao = new CitaDao();
        pacienteDao = new PacienteDao();
        proveedoresDao = new ProveedoresDao();
        personalDao = new PersonalDao();

        setLayout(new BorderLayout());
        setBackground(lightBg);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // Top Section (Header + Cards)
        JPanel topSection = new JPanel(new BorderLayout(0, 20));
        topSection.setBackground(lightBg);
        
        topSection.add(buildHeader(), BorderLayout.NORTH);
        topSection.add(buildSummaryCards(), BorderLayout.CENTER);

        add(topSection, BorderLayout.NORTH);

        // Center Section (Tables and Lists)
        JPanel centerSection = new JPanel(new GridLayout(1, 2, 20, 0));
        centerSection.setBackground(lightBg);
        centerSection.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        centerSection.add(buildProximasCitasPanel());
        centerSection.add(buildAlertasPanel());
        
        add(centerSection, BorderLayout.CENTER);
        
        cargarDatos();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(lightBg);
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(lightBg);
        
        society.modell.administracion.Personal user = society.App.getUsuarioLogueado();
        String nombre = user != null ? user.getNombre() : "Usuario";
        
        JLabel title = new JLabel("Hola, " + nombre + " 👋");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        
        JLabel subtitle = new JLabel("Aquí tienes el resumen del estado actual de la clínica (" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")) + ").");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        
        titlePanel.add(title);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitle);
        
        header.add(titlePanel, BorderLayout.WEST);
        
        return header;
    }

    private JPanel buildSummaryCards() {
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsPanel.setBackground(lightBg);
        
        cardCitasHoy = new CardAlerta("CITAS HOY", "0", "📅", new Color(240, 248, 255), brandBlue);
        cardInternados = new CardAlerta("INTERNADOS", "0", "🏨", new Color(255, 240, 240), new Color(180, 0, 0));
        cardProveedores = new CardAlerta("PROV. ACTIVOS", "0", "🚚", new Color(240, 255, 240), new Color(0, 150, 0));
        cardPersonal = new CardAlerta("PERSONAL", "0", "👨‍⚕️", new Color(255, 250, 240), new Color(200, 120, 0));
        
        cardsPanel.add(cardCitasHoy);
        cardsPanel.add(cardInternados);
        cardsPanel.add(cardProveedores);
        cardsPanel.add(cardPersonal);
        
        return cardsPanel;
    }

    private JPanel buildProximasCitasPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitle = new JLabel("⏳ Citas Programadas para Hoy");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        panel.add(lblTitle, BorderLayout.NORTH);
        
        String[] columnas = {"Hora", "Paciente", "Motivo", "Estado"};
        modeloCitasHoy = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tablaCitasHoy = new JTable(modeloCitasHoy);
        tablaCitasHoy.setRowHeight(35);
        tablaCitasHoy.setShowGrid(false);
        tablaCitasHoy.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tablaCitasHoy.getTableHeader().setBackground(Color.WHITE);
        
        JScrollPane scroll = new JScrollPane(tablaCitasHoy);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240)));
        
        panel.add(scroll, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel buildAlertasPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitle = new JLabel("⚠️ Alertas del Sistema");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        panel.add(lblTitle, BorderLayout.NORTH);
        
        alertasPanel = new JPanel();
        alertasPanel.setLayout(new BoxLayout(alertasPanel, BoxLayout.Y_AXIS));
        alertasPanel.setBackground(Color.WHITE);
        
        JScrollPane scroll = new JScrollPane(alertasPanel);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(null);
        
        panel.add(scroll, BorderLayout.CENTER);
        
        return panel;
    }

    private void cargarDatos() {
        LocalDate hoy = LocalDate.now();
        
        // Citas de hoy
        List<Cita> todasCitas = citaDao.getAll();
        List<Cita> citasHoy = todasCitas.stream()
            .filter(c -> c.getFechaHora() != null && c.getFechaHora().toLocalDate().equals(hoy))
            .sorted((c1, c2) -> c1.getFechaHora().compareTo(c2.getFechaHora()))
            .collect(Collectors.toList());
            
        long citasPendientes = citasHoy.stream().filter(c -> c.getEstado() == Cita.EstadoCita.PENDIENTE).count();
        cardCitasHoy.updateData(String.valueOf(citasHoy.size()));
        
        // Internados
        List<Paciente> todosPacientes = pacienteDao.getAll();
        long internados = todosPacientes.stream()
            .filter(p -> p.getEstado() == Paciente.EstadoPaciente.EN_CLINICA)
            .count();
        cardInternados.updateData(String.valueOf(internados));
        
        // Proveedores activos
        List<Proveedores> todosProv = proveedoresDao.getAll();
        long provActivos = todosProv.stream().filter(p -> "Activo".equalsIgnoreCase(p.getEstado())).count();
        cardProveedores.updateData(String.valueOf(provActivos));
        
        // Personal total
        List<Personal> todoPersonal = personalDao.getAll();
        cardPersonal.updateData(String.valueOf(todoPersonal.size()));
        
        // Llenar tabla de citas
        modeloCitasHoy.setRowCount(0);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        for(Cita c : citasHoy) {
            String hora = c.getFechaHora().format(timeFormatter);
            
            // Buscar nombre del paciente
            String nomPaciente = "ID " + c.getPacienteId();
            for(Paciente p : todosPacientes) {
                if(p.getId() == c.getPacienteId()) {
                    nomPaciente = p.getNombre();
                    break;
                }
            }
            
            modeloCitasHoy.addRow(new Object[]{hora, nomPaciente, c.getMotivo(), c.getEstado().toString()});
        }
        
        // Llenar alertas
        alertasPanel.removeAll();
        
        // Alerta: Citas de Urgencia hoy
        long urgencias = citasHoy.stream().filter(c -> c.getEstado() == Cita.EstadoCita.URGENCIA).count();
        if(urgencias > 0) {
            addAlerta("🚨 " + urgencias + " Cita(s) de Urgencia programada(s) para hoy.", new Color(255, 230, 230), new Color(180, 0, 0));
        }
        
        // Alerta: Proveedores en revisión
        long provRevision = todosProv.stream().filter(p -> p.getEstado().contains("Revis")).count();
        if(provRevision > 0) {
            addAlerta("⚠️ Hay " + provRevision + " proveedor(es) en estado de revisión.", new Color(255, 245, 220), new Color(180, 120, 0));
        }
        
        // Alerta: Pacientes internados
        if(internados > 0) {
            addAlerta("🏨 Tienes " + internados + " paciente(s) actualmente en clínica.", new Color(230, 245, 255), brandBlue);
        }
        
        if(urgencias == 0 && provRevision == 0 && internados == 0) {
            JLabel lblOk = new JLabel("Todo está en orden hoy. ✅");
            lblOk.setFont(new Font("SansSerif", Font.PLAIN, 14));
            lblOk.setForeground(Color.GRAY);
            alertasPanel.add(lblOk);
        }
        
        alertasPanel.revalidate();
        alertasPanel.repaint();
    }
    
    private void addAlerta(String texto, Color bg, Color fg) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(bg);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(fg, 1, true),
            new EmptyBorder(12, 15, 12, 15)
        ));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        JLabel l = new JLabel(texto);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(fg);
        
        p.add(l, BorderLayout.CENTER);
        
        alertasPanel.add(p);
        alertasPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    }
}
