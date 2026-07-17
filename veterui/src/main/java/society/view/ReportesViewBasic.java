package society.view;

import javax.swing.*;
import java.awt.*;

public class ReportesViewBasic extends JPanel {
    public ReportesViewBasic() {
        setLayout(new BorderLayout());
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        JPanel hBox1 = new JPanel(new FlowLayout());
        hBox1.add(new JLabel("Reportes Header"));
        contentPanel.add(hBox1);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        add(scrollPane, BorderLayout.CENTER);
    }
}
