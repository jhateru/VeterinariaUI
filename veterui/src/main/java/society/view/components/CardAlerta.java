package society.view.components;

import javax.swing.*;
import java.awt.*;

/**
 * Componente Tarjeta de Alerta (Métrica/Estadística).
 * Diseño ligero y reutilizable para mostrar indicadores clave (KPIs).
 * Optimizado para consumir poca memoria reutilizando los mismos JLabels al actualizar datos.
 */
public class CardAlerta extends JPanel {

    private JLabel lblTitle;
    private JLabel lblValue;
    private JPanel iconPanel;
    private JLabel lblIcon;

    /**
     * Constructor del CardAlerta.
     * @param title Título de la métrica (ej. "Servicios Activos")
     * @param initialValue Valor inicial (ej. "42")
     * @param iconText Texto o símbolo para el icono (ej. "☰", "$", "★")
     * @param iconBgColor Color de fondo de la caja del icono
     * @param valueColor Color del texto del valor principal
     */
    public CardAlerta(String title, String initialValue, String iconText, Color iconBgColor, Color valueColor) {
        setLayout(new BorderLayout(15, 0));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Para que se ajuste bien si se coloca en un contenedor con GridLayout o FlowLayout
        setPreferredSize(new Dimension(250, 80));
        setMinimumSize(new Dimension(200, 80));

        // --- Panel del Icono (Izquierda) ---
        iconPanel = new JPanel(new BorderLayout());
        iconPanel.setPreferredSize(new Dimension(50, 50));
        iconPanel.setBackground(iconBgColor);
        // Borde redondeado simulado para el icono
        iconPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        lblIcon = new JLabel(iconText, SwingConstants.CENTER);
        lblIcon.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblIcon.setForeground(Color.DARK_GRAY);
        iconPanel.add(lblIcon, BorderLayout.CENTER);
        
        add(iconPanel, BorderLayout.WEST);

        // --- Textos (Centro) ---
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false); // Fondo transparente para heredar el blanco

        lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblTitle.setForeground(new Color(100, 100, 100));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblValue = new JLabel(initialValue);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblValue.setForeground(valueColor != null ? valueColor : Color.DARK_GRAY);
        lblValue.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(lblTitle);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(lblValue);

        add(textPanel, BorderLayout.CENTER);
    }

    /**
     * Constructor para CardAlerta pequeño y cuadrado (sin icono), centrado.
     */
    public CardAlerta(String title, String initialValue) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));

        lblValue = new JLabel(initialValue);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblValue.setForeground(Color.DARK_GRAY);
        lblValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblTitle.setForeground(new Color(100, 100, 100));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalGlue());
        add(lblValue);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(lblTitle);
        add(Box.createVerticalGlue());
    }

    /**
     * Método eficiente para actualizar los datos.
     * Solo cambia el texto, sin instanciar nuevos objetos, optimizando memoria.
     */
    public void updateData(String newValue) {
        lblValue.setText(newValue);
    }

    /**
     * Actualiza el título y el valor dinámicamente.
     */
    public void updateData(String newTitle, String newValue) {
        lblTitle.setText(newTitle);
        lblValue.setText(newValue);
    }
}
