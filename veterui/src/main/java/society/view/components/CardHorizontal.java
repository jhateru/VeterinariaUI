package society.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Componente Tarjeta Horizontal Reutilizable.
 * Ideal para mostrar información resumida de un registro JSON (ej. Citas, Tareas).
 */
public class CardHorizontal extends JPanel {

    private JLabel lblTitle;
    private JLabel lblTopRight;
    private JLabel lblSubtitle;
    private JLabel lblStatus;
    private JButton btnMainAction;
    private JButton btnSecondaryAction;

    public CardHorizontal(String title, String topRight, String subtitle, String status, String mainBtnText, String secBtnText) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setBackground(Color.WHITE);

        // Header (Título y Texto Superior Derecho)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        topPanel.add(lblTitle, BorderLayout.WEST);
        
        lblTopRight = new JLabel(topRight);
        lblTopRight.setFont(new Font("SansSerif", Font.BOLD, 12));
        topPanel.add(lblTopRight, BorderLayout.EAST);
        
        add(topPanel);
        add(Box.createRigidArea(new Dimension(0, 5)));

        // Cuerpo (Subtítulo y Estado)
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setOpaque(false);
        
        lblSubtitle = new JLabel(subtitle);
        lblSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        lblStatus = new JLabel("● " + status);
        lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        bodyPanel.add(lblSubtitle);
        bodyPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        bodyPanel.add(lblStatus);
        
        add(bodyPanel);
        add(Box.createRigidArea(new Dimension(0, 10)));

        // Acciones (Botones)
        JPanel actionsPanel = new JPanel(new BorderLayout(5, 0));
        actionsPanel.setOpaque(false);
        
        btnMainAction = new JButton(mainBtnText);
        btnMainAction.setBackground(new Color(240, 248, 255));
        btnMainAction.setFocusPainted(false);
        
        btnSecondaryAction = new JButton(secBtnText);
        btnSecondaryAction.setBackground(new Color(240, 248, 255));
        btnSecondaryAction.setFocusPainted(false);
        
        actionsPanel.add(btnMainAction, BorderLayout.CENTER);
        actionsPanel.add(btnSecondaryAction, BorderLayout.EAST);
        
        add(actionsPanel);
    }

    // Métodos para agregar interactividad (Listeners) a los botones
    public void addMainAction(ActionListener listener) {
        btnMainAction.addActionListener(listener);
    }

    public void addSecondaryAction(ActionListener listener) {
        btnSecondaryAction.addActionListener(listener);
    }

    // Setters para actualizar datos dinámicamente desde un JSON
    public void updateData(String title, String topRight, String subtitle, String status) {
        lblTitle.setText(title);
        lblTopRight.setText(topRight);
        lblSubtitle.setText(subtitle);
        lblStatus.setText("● " + status);
    }
}
