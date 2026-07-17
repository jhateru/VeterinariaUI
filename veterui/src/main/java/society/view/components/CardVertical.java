package society.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Componente Tarjeta Vertical Reutilizable.
 * Ideal para perfiles completos (ej. Clientes, Personal) desde un JSON.
 */
public class CardVertical extends JPanel {

    private JLabel lblTitle;
    private JLabel lblSubtitle;
    private JLabel lblTag;
    private JPanel infoPanel;
    private JPanel tagsPanel;
    private JButton btnViewDetails;
    private JButton btnEdit;

    public CardVertical(String title, String subtitle, String mainTag, List<String> contactInfo, List<String> subTags) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(300, 320));
        setMaximumSize(new Dimension(350, 400));

        // Top Section: Avatar(Initials) + Title + Subtitle + Options
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setOpaque(false);
        
        // Pseudo-Avatar (Círculo con iniciales)
        JLabel lblAvatar = new JLabel(getInitials(title), SwingConstants.CENTER);
        lblAvatar.setPreferredSize(new Dimension(50, 50));
        lblAvatar.setOpaque(true);
        lblAvatar.setBackground(new Color(220, 200, 180));
        lblAvatar.setFont(new Font("SansSerif", Font.BOLD, 18));
        headerPanel.add(lblAvatar, BorderLayout.WEST);

        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.Y_AXIS));
        titleContainer.setOpaque(false);
        
        lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        lblSubtitle = new JLabel(subtitle);
        lblSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSubtitle.setForeground(Color.GRAY);
        
        lblTag = new JLabel(" " + mainTag + " ");
        lblTag.setOpaque(true);
        lblTag.setBackground(new Color(230, 240, 255));
        lblTag.setFont(new Font("SansSerif", Font.PLAIN, 10));
        
        titleContainer.add(lblTitle);
        titleContainer.add(Box.createRigidArea(new Dimension(0, 2)));
        titleContainer.add(lblSubtitle);
        titleContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        titleContainer.add(lblTag);
        
        headerPanel.add(titleContainer, BorderLayout.CENTER);
        
        // Options icon (tres puntos)
        JLabel lblOptions = new JLabel("⋮");
        lblOptions.setFont(new Font("SansSerif", Font.BOLD, 16));
        headerPanel.add(lblOptions, BorderLayout.EAST);

        add(headerPanel);
        add(Box.createRigidArea(new Dimension(0, 15)));

        // Middle Section: Contact Info
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        populateContactInfo(contactInfo);
        add(infoPanel);
        
        add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Separador
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        add(separator);
        
        add(Box.createRigidArea(new Dimension(0, 15)));

        // Subtags Section (Mascotas registradas, etc)
        JLabel lblSubTitle = new JLabel("DATOS RELACIONADOS");
        lblSubTitle.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblSubTitle.setForeground(Color.GRAY);
        lblSubTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(lblSubTitle);
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        tagsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        tagsPanel.setOpaque(false);
        tagsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        populateSubTags(subTags);
        add(tagsPanel);
        
        add(Box.createVerticalGlue()); // Empuja los botones hacia abajo

        // Footer: Botones
        JPanel footerPanel = new JPanel(new BorderLayout(10, 0));
        footerPanel.setOpaque(false);
        footerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnViewDetails = new JButton("Ver Detalles Completos");
        btnViewDetails.setBackground(Color.WHITE);
        btnViewDetails.setFocusPainted(false);
        
        btnEdit = new JButton("✎");
        btnEdit.setBackground(new Color(200, 240, 240));
        btnEdit.setFocusPainted(false);
        
        footerPanel.add(btnViewDetails, BorderLayout.CENTER);
        footerPanel.add(btnEdit, BorderLayout.EAST);
        
        add(footerPanel);
    }

    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) return "?";
        String[] parts = name.trim().split(" ");
        if (parts.length >= 2) {
            return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
        }
        return name.substring(0, Math.min(name.length(), 2)).toUpperCase();
    }

    public void populateContactInfo(List<String> infos) {
        infoPanel.removeAll();
        for (String info : infos) {
            JLabel lbl = new JLabel("• " + info);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
            infoPanel.add(lbl);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        infoPanel.revalidate();
        infoPanel.repaint();
    }

    public void populateSubTags(List<String> tags) {
        tagsPanel.removeAll();
        for (String t : tags) {
            JLabel tagLbl = new JLabel(" " + t + " ");
            tagLbl.setOpaque(true);
            tagLbl.setBackground(new Color(240, 240, 240));
            tagLbl.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
            tagLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
            tagsPanel.add(tagLbl);
        }
        tagsPanel.revalidate();
        tagsPanel.repaint();
    }

    // Actualización dinámica
    public void updateData(String title, String subtitle, String mainTag, List<String> contactInfo, List<String> subTags) {
        lblTitle.setText(title);
        lblSubtitle.setText(subtitle);
        lblTag.setText(" " + mainTag + " ");
        populateContactInfo(contactInfo);
        populateSubTags(subTags);
    }

    // Listeners
    public void addViewDetailsAction(ActionListener listener) {
        btnViewDetails.addActionListener(listener);
    }

    public void addEditAction(ActionListener listener) {
        btnEdit.addActionListener(listener);
    }
}
