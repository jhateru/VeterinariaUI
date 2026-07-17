package society.view;

import javax.swing.*;
import java.awt.*;

public class HistoriaClinicasViewBasic extends JPanel {
    public HistoriaClinicasViewBasic() {
        setLayout(new FlowLayout());
        
        JPanel vBox1 = new JPanel();
        vBox1.setLayout(new BoxLayout(vBox1, BoxLayout.Y_AXIS));
        vBox1.add(new JLabel("Pacientes Recientes"));
        
        JPanel vBox2 = new JPanel();
        vBox2.setLayout(new BoxLayout(vBox2, BoxLayout.Y_AXIS));
        vBox2.add(new JLabel("Evoluciones Médicas"));
        
        JPanel vBox3 = new JPanel();
        vBox3.setLayout(new BoxLayout(vBox3, BoxLayout.Y_AXIS));
        vBox3.add(new JLabel("Monitoreo"));
        
        add(vBox1);
        add(new JSeparator(SwingConstants.VERTICAL));
        add(vBox2);
        add(new JSeparator(SwingConstants.VERTICAL));
        add(vBox3);
    }
}
