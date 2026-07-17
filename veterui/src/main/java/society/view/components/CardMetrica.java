package society.view.components;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class CardMetrica extends JPanel {

    public CardMetrica(String title, String value, String subtitle, Color subtitleColor, String iconText, Color iconColor, Color iconBgColor, Color leftBorderColor) {
        setLayout(new BorderLayout(10, 0));
        setBackground(Color.WHITE);
        
        // Define border
        Border lineBorder = BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true);
        Border marginBorder = new EmptyBorder(15, 15, 15, 15);
        
        if (leftBorderColor != null) {
            // Create a custom border with a thick left line
            Border leftThick = BorderFactory.createMatteBorder(0, 4, 0, 0, leftBorderColor);
            setBorder(BorderFactory.createCompoundBorder(lineBorder, BorderFactory.createCompoundBorder(leftThick, marginBorder)));
        } else {
            setBorder(BorderFactory.createCompoundBorder(lineBorder, marginBorder));
        }

        setPreferredSize(new Dimension(240, 90));
        setMinimumSize(new Dimension(180, 90)); // Responsive support

        // --- Text Panel (Left) ---
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(title.toUpperCase());
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblTitle.setForeground(new Color(100, 100, 100));

        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        valuePanel.setOpaque(false);
        valuePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("SansSerif", Font.PLAIN, 24));
        lblValue.setForeground(Color.DARK_GRAY);
        valuePanel.add(lblValue);

        if (subtitle != null && !subtitle.isEmpty()) {
            JLabel lblSubtitle = new JLabel(subtitle);
            lblSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lblSubtitle.setForeground(subtitleColor != null ? subtitleColor : Color.GRAY);
            // Adjust vertical alignment to match baseline roughly
            lblSubtitle.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0)); 
            valuePanel.add(lblSubtitle);
        }

        textPanel.add(lblTitle);
        textPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        textPanel.add(valuePanel);

        add(textPanel, BorderLayout.CENTER);

        // --- Icon Panel (Right) ---
        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.setOpaque(false);
        
        // Custom circle panel for the icon
        JPanel circleIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(iconBgColor);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        circleIcon.setOpaque(false);
        circleIcon.setPreferredSize(new Dimension(40, 40));
        circleIcon.setMaximumSize(new Dimension(40, 40));
        circleIcon.setLayout(new BorderLayout());

        JLabel lblIcon = new JLabel(iconText, SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        lblIcon.setForeground(iconColor);
        circleIcon.add(lblIcon, BorderLayout.CENTER);

        // Wrap to center vertically on the right
        JPanel rightWrapper = new JPanel(new GridBagLayout());
        rightWrapper.setOpaque(false);
        rightWrapper.add(circleIcon);
        
        add(rightWrapper, BorderLayout.EAST);
    }
}
