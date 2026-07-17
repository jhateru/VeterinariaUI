package society.view;

import javax.swing.*;
import java.awt.*;

public class AreaMedicaViewBasic extends JPanel {
    public AreaMedicaViewBasic() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Panel General del Área Médica", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);
    }
}
