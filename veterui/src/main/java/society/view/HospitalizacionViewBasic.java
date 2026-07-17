package society.view;

import javax.swing.*;
import java.awt.*;

public class HospitalizacionViewBasic extends JPanel {
    public HospitalizacionViewBasic() {
        setLayout(new BorderLayout());
        
        JPanel topPane = new JPanel(new FlowLayout());
        topPane.add(new JLabel("Hospitalización Top"));
        
        JPanel centerPane = new JPanel(new FlowLayout());
        centerPane.add(new JLabel("Hospitalización Center"));
        
        add(topPane, BorderLayout.NORTH);
        add(centerPane, BorderLayout.CENTER);
    }
}
