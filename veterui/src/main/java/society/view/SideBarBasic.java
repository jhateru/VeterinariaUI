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

        JLabel logo = new JLabel("🐾 Healthy Little Paws");
        logo.setFont(new Font("SansSerif", Font.BOLD, 14));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(logo);
        
        JLabel subtitle = new JLabel("VETERINARY CARE");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 10));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(subtitle);
        
        add(Box.createRigidArea(new Dimension(0, 20)));

        add(createButton("🔠 Resumen", true, "DashboardResumenBasic"));
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        add(createCategoryButton("RECEPCIÓN", "RecepcionViewBasic"));
        add(createButton("📅 Citas", false, "CitasViewBasic"));
        add(createButton("👥 Dueños", false, "DuenosViewBasic"));
        add(createButton("🐾 Pacientes", false, "PacientesViewBasic"));
        
        add(Box.createRigidArea(new Dimension(0, 10)));

        add(createCategoryButton("ÁREA MÉDICA", "AreaMedicaViewBasic"));
        add(createButton("📋 Historias Clínicas", false, "HistoriaClinicasViewBasic"));
        add(createButton("🏥 Hospitalización", false, "HospitalizacionViewBasic"));
        add(createButton("🔬 Laboratorio", false, "LaboratorioViewBasic"));

        add(Box.createRigidArea(new Dimension(0, 10)));

        add(createCategoryButton("ADMINISTRACIÓN", "AdministracionViewBasic"));
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

    private JButton createCategoryButton(String text, String targetView) {
        JButton button = new JButton(text);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        button.setFont(new Font("SansSerif", Font.BOLD, 10));
        
        button.addActionListener((ActionEvent e) -> {
            if (mainView != null) {
                mainView.cargarVista(targetView);
            }
        });
        
        return button;
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
