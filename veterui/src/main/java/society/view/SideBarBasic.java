package society.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SideBarBasic extends JPanel {
    private MainViewBasic mainView;

    public SideBarBasic(MainViewBasic mainView) {
        this.mainView = mainView;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        setPreferredSize(new Dimension(200, 0));

        add(Box.createRigidArea(new Dimension(0, 10)));

        add(createButton("🔠 Resumen", true, "DashboardResumenBasic"));
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        add(createCategoryLabel("RECEPCIÓN"));
        add(createButton("📅 Citas", false, "CitasViewBasic"));
        add(createButton("👥 Dueños", false, "DuenosViewBasic"));
        add(createButton("🐾 Pacientes", false, "PacientesViewBasic"));
        
        add(Box.createRigidArea(new Dimension(0, 10)));

        add(createCategoryLabel("ÁREA MÉDICA"));
        add(createButton("📋 Historias Clínicas", false, "HistoriaClinicasViewBasic"));
        add(createButton("🏥 Hospitalización", false, "HospitalizacionViewBasic"));
        add(createButton("🔬 Laboratorio", false, "LaboratorioViewBasic"));

        add(Box.createRigidArea(new Dimension(0, 10)));

        add(createCategoryLabel("ADMINISTRACIÓN"));
        add(createButton("📠 Punto de Venta", false, "FacturacionViewBasic"));
        add(createButton("📦 Inventario", false, "InventarioViewBasic"));
        add(createButton("🩺 Servicios", false, "ServiciosViewBasic"));
        add(createButton("🚚 Proveedores", false, "ProveedoresViewBasic"));
        add(createButton("🧑‍⚕️ Personal", false, "PersonalViewBasic"));

        add(Box.createVerticalGlue());
        add(new JSeparator(SwingConstants.HORIZONTAL));
        add(Box.createRigidArea(new Dimension(0, 10)));

        add(createButton("📊 Reportes", false, "ReportesViewBasic"));
        add(createButton("⚙️ Configuración", false, "ConfiguracionViewBasic"));
    }

    private JLabel createCategoryLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setFont(new Font("SansSerif", Font.BOLD, 10));
        label.setForeground(new Color(120, 120, 120)); // Subtle gray for category titles
        label.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
        return label;
    }

    private JButton createButton(String text, boolean isActive, String targetView) {
        JButton button = new JButton(text);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        if (isActive) {
            button.setFont(new Font("SansSerif", Font.BOLD, 12));
        } else {
            button.setFont(new Font("SansSerif", Font.PLAIN, 12));
        }
        
        button.addActionListener((ActionEvent e) -> {
            if (mainView != null) {
                mainView.cargarVista(targetView);
            }
        });
        
        return button;
    }
}
