package society.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class DashboardResumenBasic extends JPanel {
    public DashboardResumenBasic() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        JLabel title = new JLabel("Bienvenido al hospital Healthy Little Paws");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        JLabel subtitle = new JLabel("Aquí tienes un resumen del estado actual de la clínica hoy, 24 de Mayo.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        headerPanel.add(title);
        headerPanel.add(subtitle);
        
        add(headerPanel, BorderLayout.NORTH);

        // Center Content
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        
        // Top 3 Cards
        JPanel topCardsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        topCardsPanel.add(createCard("RECEPCIÓN - Actividad Diaria", 
                                     new String[]{"📅 Citas hoy: 18", "🧍 En sala de espera: 4"},
                                     "VER CALENDARIO ->"));
        topCardsPanel.add(createCard("ÁREA MÉDICA - Capacidad y Análisis", 
                                     new String[]{"🛏️ Hospitalización: 7/10 CAMAS OCUPADAS", "🔬 Pruebas pendientes: 12"},
                                     "REVISAR RESULTADOS ->"));
        topCardsPanel.add(createCard("ADMINISTRACIÓN - Finanzas y Stock", 
                                     new String[]{"💵 Ingresos totales: $3,240", "⚠️ Alertas de stock: 5"},
                                     "GESTIONAR INVENTARIO ->"));
        
        centerPanel.add(topCardsPanel, BorderLayout.NORTH);

        // Bottom area (Split 2 columns)
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        
        // Bottom Left: Chart Placeholder
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createTitledBorder("Tendencias de la Semana"));
        chartPanel.add(new JLabel("Comparativa de pacientes atendidos vs. ingresos", SwingConstants.CENTER), BorderLayout.NORTH);
        
        JPanel chartGrid = new JPanel(new GridLayout(1, 7));
        String[] days = {"LUN", "MAR", "MIE", "JUE", "VIE", "SAB", "DOM"};
        for(String d : days) {
            chartGrid.add(new JLabel(d, SwingConstants.CENTER));
        }
        chartPanel.add(chartGrid, BorderLayout.CENTER);
        
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        legendPanel.add(new JLabel("⚫ Ingresos"));
        legendPanel.add(new JLabel("⚪ Pacientes"));
        chartPanel.add(legendPanel, BorderLayout.SOUTH);
        
        bottomPanel.add(chartPanel);
        
        // Bottom Right: Alerts and Activity
        JPanel alertsActivityPanel = new JPanel(new GridLayout(2, 1, 0, 15));
        
        JPanel alertsPanel = new JPanel();
        alertsPanel.setLayout(new BoxLayout(alertsPanel, BoxLayout.Y_AXIS));
        alertsPanel.setBorder(BorderFactory.createTitledBorder("! ALERTAS URGENTES"));
        alertsPanel.add(new JLabel("🚨 Cirugía Urgente: Firulais (Quirófano 2 - En 15 min)"));
        alertsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        alertsPanel.add(new JLabel("📦 Stock Bajo: Vacuna Rabia (Quedan solo 3 unidades)"));
        
        JPanel activityPanel = new JPanel();
        activityPanel.setLayout(new BoxLayout(activityPanel, BoxLayout.Y_AXIS));
        activityPanel.setBorder(BorderFactory.createTitledBorder("ACTIVIDAD RECIENTE"));
        activityPanel.add(new JLabel("✔️ Alta médica: Luna (Gato) - Hace 12 min"));
        activityPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        activityPanel.add(new JLabel("💵 Pago recibido: $120.00 - Hace 45 min"));
        activityPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        activityPanel.add(new JLabel("🔬 Resultados listos: Max - Hace 1 hora"));
        
        JButton logButton = new JButton("VER TODO EL LOG");
        logButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        activityPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        activityPanel.add(logButton);

        alertsActivityPanel.add(alertsPanel);
        alertsActivityPanel.add(activityPanel);
        
        bottomPanel.add(alertsActivityPanel);
        
        centerPanel.add(bottomPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
        
        // Floating Action Button Placeholder
        JPanel fabPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton fab = new JButton("+");
        fab.setFont(new Font("SansSerif", Font.BOLD, 18));
        fabPanel.add(fab);
        add(fabPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createCard(String titleText, String[] items, String buttonText) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBorder(BorderFactory.createTitledBorder(null, titleText, TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 12)));
        
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        for(String item : items) {
            JLabel lbl = new JLabel(item);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
            itemsPanel.add(lbl);
            itemsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
        card.add(itemsPanel, BorderLayout.CENTER);
        
        JButton btn = new JButton(buttonText);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        card.add(btn, BorderLayout.SOUTH);
        
        return card;
    }
}
