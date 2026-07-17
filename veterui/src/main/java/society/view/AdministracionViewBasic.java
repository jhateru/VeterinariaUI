package society.view;

import javax.swing.*;
import java.awt.*;

public class AdministracionViewBasic extends JPanel {
    public AdministracionViewBasic() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Panel General de Administración", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);
    }
}
