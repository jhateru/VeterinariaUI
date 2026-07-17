package society.view;

import javax.swing.*;
import java.awt.*;

public class ConfiguracionViewBasic extends JPanel {
    public ConfiguracionViewBasic() {
        setLayout(new BorderLayout());
        
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        
        // Header
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.add(new JLabel("Configuración"));
        headerPanel.add(new JLabel("Administre los detalles de su clínica, seguridad y preferencias del sistema."));
        mainContent.add(headerPanel);
        
        // Top Row
        JPanel topRow = new JPanel(new GridLayout(1, 2, 20, 20));
        
        // Perfil
        JPanel perfilPanel = new JPanel();
        perfilPanel.setLayout(new BoxLayout(perfilPanel, BoxLayout.Y_AXIS));
        perfilPanel.add(new JLabel("Perfil de la Clínica"));
        perfilPanel.add(new JLabel("NOMBRE DE LA CLÍNICA"));
        perfilPanel.add(new JTextField());
        perfilPanel.add(new JLabel("ID FISCAL"));
        perfilPanel.add(new JTextField());
        perfilPanel.add(new JLabel("DIRECCIÓN"));
        perfilPanel.add(new JTextField());
        perfilPanel.add(new JLabel("TELÉFONO DE CONTACTO"));
        perfilPanel.add(new JTextField());
        perfilPanel.add(new JLabel("CORREO ELECTRÓNICO"));
        perfilPanel.add(new JTextField());
        
        JPanel perfilButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        perfilButtons.add(new JButton("Cancelar"));
        perfilButtons.add(new JButton("Guardar Cambios"));
        perfilPanel.add(perfilButtons);
        topRow.add(perfilPanel);
        
        // Seguridad
        JPanel seguridadPanel = new JPanel();
        seguridadPanel.setLayout(new BoxLayout(seguridadPanel, BoxLayout.Y_AXIS));
        seguridadPanel.add(new JLabel("Seguridad"));
        seguridadPanel.add(new JLabel("Contraseña"));
        seguridadPanel.add(new JButton("Cambiar Contraseña"));
        seguridadPanel.add(new JLabel("Autenticación 2FA"));
        seguridadPanel.add(new JCheckBox());
        topRow.add(seguridadPanel);
        
        mainContent.add(topRow);
        
        // Bottom Row
        JPanel bottomRow = new JPanel(new GridLayout(1, 2, 20, 20));
        
        // Preferencias
        JPanel prefPanel = new JPanel();
        prefPanel.setLayout(new BoxLayout(prefPanel, BoxLayout.Y_AXIS));
        prefPanel.add(new JLabel("Preferencias del Sistema"));
        prefPanel.add(new JLabel("IDIOMA DEL SISTEMA"));
        prefPanel.add(new JComboBox<>());
        prefPanel.add(new JLabel("MONEDA LOCAL"));
        prefPanel.add(new JComboBox<>());
        prefPanel.add(new JLabel("NOTIFICACIONES POR CORREO"));
        prefPanel.add(new JCheckBox("Recordatorios de citas"));
        prefPanel.add(new JCheckBox("Resultados de laboratorio"));
        prefPanel.add(new JCheckBox("Resumen semanal"));
        bottomRow.add(prefPanel);
        
        // Gestion de Datos
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.add(new JLabel("Gestión de Datos"));
        dataPanel.add(new JLabel("Respaldo de Base de Datos"));
        dataPanel.add(new JLabel("Exportar Historias Clínicas"));
        dataPanel.add(new JLabel("Zona de Peligro"));
        dataPanel.add(new JButton("Purga de Registros Antiguos"));
        bottomRow.add(dataPanel);
        
        mainContent.add(bottomRow);
        
        add(new JScrollPane(mainContent), BorderLayout.CENTER);
    }
}
