package society.view;

import javax.swing.*;
import java.awt.*;

public class TopBarBasic extends JPanel {
    public TopBarBasic() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Centro: Buscador
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(40);
        // Usamos setToolTipText en lugar de hint/placeholder ya que Swing básico no tiene placeholder nativo
        searchField.setToolTipText("Buscar pacientes, historias o facturas...");
        centerPanel.add(new JLabel("🔍"));
        centerPanel.add(searchField);
        
        // Derecha: Notificaciones y Perfil
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        
        // Iconos de notificación y ayuda
        rightPanel.add(new JLabel("🔔"));
        rightPanel.add(new JLabel("❓"));
        
        // Perfil de usuario
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        JLabel nameLabel = new JLabel("Dr. Martínez");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        JLabel roleLabel = new JLabel("VETERINARIO JEFE");
        roleLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        userPanel.add(nameLabel);
        userPanel.add(roleLabel);
        
        rightPanel.add(userPanel);
        rightPanel.add(new JLabel("👨‍⚕️")); // Avatar
        
        add(centerPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }
}
