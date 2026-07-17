package society.view;

import javax.swing.*;
import java.awt.*;

public class LaboratorioViewBasic extends JPanel {
    public LaboratorioViewBasic() {
        setLayout(new BorderLayout());
        
        JPanel topPane = new JPanel(new FlowLayout());
        topPane.add(new JLabel("Laboratorio Top"));
        
        JPanel centerPane = new JPanel(new FlowLayout());
        centerPane.add(new JLabel("Laboratorio Center"));
        
        add(topPane, BorderLayout.NORTH);
        add(centerPane, BorderLayout.CENTER);
    }
}
