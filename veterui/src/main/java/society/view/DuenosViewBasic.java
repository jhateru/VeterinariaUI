package society.view;

import javax.swing.*;
import java.awt.*;

public class DuenosViewBasic extends JPanel {
    public DuenosViewBasic() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.add(new JLabel("Directorio de Dueños"));
        header.add(new JButton("+ Nuevo Dueño"));
        
        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filters.add(new JButton("Todos (0)"));
        filters.add(new JButton("Frecuentes"));
        filters.add(new JButton("Nuevos (Mes)"));
        filters.add(new JButton("Filtros Avanzados"));
        
        JPanel flowPane = new JPanel(new FlowLayout());
        JScrollPane scrollPane = new JScrollPane(flowPane);
        
        add(header);
        add(filters);
        add(scrollPane);
    }
}
