import os

target_dir = r'C:\Users\user\Java\VeterinariaUI\veterui\src\main\java\society\view'

files_data = {
    'AdministracionViewBasic.java': '''package society.view;

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
''',
    'AreaMedicaViewBasic.java': '''package society.view;

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
''',
    'DuenosViewBasic.java': '''package society.view;

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
''',
    'FacturacionViewBasic.java': '''package society.view;

import javax.swing.*;
import java.awt.*;

public class FacturacionViewBasic extends JPanel {
    public FacturacionViewBasic() {
        setLayout(new FlowLayout());
        
        JPanel leftVBox = new JPanel();
        leftVBox.setLayout(new BoxLayout(leftVBox, BoxLayout.Y_AXIS));
        leftVBox.add(new JLabel("Facturación - Izquierda"));
        leftVBox.add(new JScrollPane(new JPanel(new FlowLayout())));
        
        JPanel rightVBox = new JPanel();
        rightVBox.setLayout(new BoxLayout(rightVBox, BoxLayout.Y_AXIS));
        rightVBox.add(new JLabel("Facturación - Derecha"));
        rightVBox.add(new JScrollPane(new JPanel()));
        
        add(leftVBox);
        add(rightVBox);
    }
}
''',
    'HistoriaClinicasViewBasic.java': '''package society.view;

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
''',
    'HospitalizacionViewBasic.java': '''package society.view;

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
''',
    'LaboratorioViewBasic.java': '''package society.view;

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
''',
    'PacientesViewBasic.java': '''package society.view;

import javax.swing.*;
import java.awt.*;

public class PacientesViewBasic extends JPanel {
    public PacientesViewBasic() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        JPanel hBox1 = new JPanel(new FlowLayout());
        hBox1.add(new JLabel("Pacientes Header"));
        
        JPanel hBox2 = new JPanel(new FlowLayout());
        hBox2.add(new JLabel("Pacientes Filters"));
        
        JPanel contentVBox = new JPanel();
        contentVBox.setLayout(new BoxLayout(contentVBox, BoxLayout.Y_AXIS));
        contentVBox.add(new JScrollPane(new JPanel()));
        
        add(hBox1);
        add(hBox2);
        add(contentVBox);
    }
}
''',
    'ProveedoresViewBasic.java': '''package society.view;

import javax.swing.*;
import java.awt.*;

public class ProveedoresViewBasic extends JPanel {
    public ProveedoresViewBasic() {
        setLayout(new BorderLayout());
        
        JPanel topPane = new JPanel(new FlowLayout());
        topPane.add(new JLabel("Proveedores Top"));
        
        JPanel centerPane = new JPanel(new FlowLayout());
        centerPane.add(new JLabel("Proveedores Center"));
        
        add(topPane, BorderLayout.NORTH);
        add(centerPane, BorderLayout.CENTER);
    }
}
''',
    'ReportesViewBasic.java': '''package society.view;

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
'''
}

for filename, content in files_data.items():
    path = os.path.join(target_dir, filename)
    with open(path, 'w', encoding='utf-8') as f:
        f.write(content)
    print(f'Wrote {filename}')
