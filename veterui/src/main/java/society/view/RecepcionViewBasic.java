package society.view;

import javax.swing.*;
import java.awt.*;

public class RecepcionViewBasic extends JPanel {
    public RecepcionViewBasic() {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Módulo General de Recepción", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(title, BorderLayout.CENTER);
    }
}
