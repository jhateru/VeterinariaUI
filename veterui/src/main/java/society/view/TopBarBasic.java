package society.view;

import javax.swing.*;
import java.awt.*;

public class TopBarBasic extends JPanel {
    public TopBarBasic() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Derecha: Notificaciones y Perfil
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        
        // Iconos de notificación y ayuda
        rightPanel.add(new JLabel("🔔"));
        rightPanel.add(new JLabel("❓"));
        
        // Obtener usuario actual
        society.modell.administracion.Personal user = society.App.getUsuarioLogueado();
        String nombre = user != null && user.getNombre() != null ? user.getNombre() : "Usuario Desconocido";
        String rol = user != null && user.getRolSistema() != null ? user.getRolSistema().toUpperCase() : "INVITADO";
        
        // Perfil de usuario
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        JLabel nameLabel = new JLabel(nombre);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        nameLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JLabel roleLabel = new JLabel(rol);
        roleLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        userPanel.add(nameLabel);
        userPanel.add(roleLabel);
        
        JLabel avatarLabel = new JLabel("👨‍⚕️");
        avatarLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        avatarLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Menu desplegable
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem changePassItem = new JMenuItem("Cambiar contraseña");
        JMenuItem logoutItem = new JMenuItem("Cerrar sesión");
        popupMenu.add(changePassItem);
        popupMenu.addSeparator();
        popupMenu.add(logoutItem);
        
        changePassItem.addActionListener(e -> {
            if (user != null) {
                JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
                JPasswordField oldPass = new JPasswordField(10);
                JPasswordField newPass = new JPasswordField(10);
                JPasswordField confPass = new JPasswordField(10);
                
                panel.add(new JLabel("Contraseña actual:"));
                panel.add(oldPass);
                panel.add(new JLabel("Nueva contraseña:"));
                panel.add(newPass);
                panel.add(new JLabel("Confirmar contraseña:"));
                panel.add(confPass);
                
                int result = JOptionPane.showConfirmDialog(this, panel, "Cambiar Contraseña", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    String op = new String(oldPass.getPassword());
                    String np = new String(newPass.getPassword());
                    String cp = new String(confPass.getPassword());
                    
                    if (!op.equals(user.getPassword())) {
                        JOptionPane.showMessageDialog(this, "La contraseña actual es incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (np.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "La nueva contraseña no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (!np.equals(cp)) {
                        JOptionPane.showMessageDialog(this, "Las nuevas contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        user.setPassword(np);
                        new society.dao.PersonalDao().update(user);
                        JOptionPane.showMessageDialog(this, "Contraseña actualizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        
        logoutItem.addActionListener(e -> {
            Window currentWindow = SwingUtilities.getWindowAncestor(this);
            if (currentWindow != null) {
                currentWindow.dispose();
            }
            society.App.logout();
        });
        
        java.awt.event.MouseAdapter clickAdapter = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                popupMenu.show(e.getComponent(), 0, e.getComponent().getHeight());
            }
        };
        
        nameLabel.addMouseListener(clickAdapter);
        avatarLabel.addMouseListener(clickAdapter);
        
        rightPanel.add(userPanel);
        rightPanel.add(avatarLabel);
        
        // Agregamos un panel vacío a la izquierda para mantener a rightPanel en el lado este
        add(new JPanel(), BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }
}
