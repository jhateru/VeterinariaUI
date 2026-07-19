package society.view;

import society.modell.administracion.Proveedores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ProveedoresCellRenderer implements TableCellRenderer {

    private Color brandBlue = new Color(0, 80, 100);
    private Color rowNormal = Color.WHITE;
    private Color rowSelected = new Color(245, 250, 250);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(isSelected ? rowSelected : rowNormal);
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));

        if (value instanceof Proveedores) {
            Proveedores prov = (Proveedores) value;
            
            switch (column) {
                case 0: // Proveedor (Avatar, Nombre, ID)
                    JPanel pnlProv = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
                    pnlProv.setOpaque(false);
                    
                    // Avatar (Initials)
                    String initials = getInitials(prov.getNombre());
                    JLabel lblAvatar = new JLabel(initials, SwingConstants.CENTER);
                    lblAvatar.setFont(new Font("SansSerif", Font.BOLD, 14));
                    lblAvatar.setPreferredSize(new Dimension(40, 40));
                    lblAvatar.setOpaque(true);
                    lblAvatar.setBackground(new Color(235, 245, 245));
                    lblAvatar.setForeground(brandBlue);
                    lblAvatar.setBorder(BorderFactory.createLineBorder(new Color(200, 220, 220), 1, true));
                    pnlProv.add(lblAvatar);

                    JPanel pnlText = new JPanel();
                    pnlText.setLayout(new BoxLayout(pnlText, BoxLayout.Y_AXIS));
                    pnlText.setOpaque(false);

                    JLabel name = new JLabel(prov.getNombre());
                    name.setFont(new Font("SansSerif", Font.BOLD, 14));
                    name.setForeground(Color.DARK_GRAY);
                    
                    JLabel idLbl = new JLabel("ID: " + prov.getIdProveedorStr());
                    idLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
                    idLbl.setForeground(new Color(130, 130, 130));

                    pnlText.add(name);
                    pnlText.add(Box.createRigidArea(new Dimension(0, 3)));
                    pnlText.add(idLbl);
                    
                    pnlProv.add(pnlText);
                    panel.add(pnlProv, BorderLayout.WEST);
                    break;
                    
                case 1: // Categoría (Icon, Text)
                    JPanel pnlCat = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
                    pnlCat.setOpaque(false);
                    
                    JLabel lblIconCat = new JLabel(getIconForCategory(prov.getCategoria()));
                    lblIconCat.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
                    
                    JLabel lblCat = new JLabel(prov.getCategoria());
                    lblCat.setFont(new Font("SansSerif", Font.PLAIN, 13));
                    lblCat.setForeground(Color.DARK_GRAY);
                    
                    pnlCat.add(lblIconCat);
                    pnlCat.add(lblCat);
                    panel.add(pnlCat, BorderLayout.WEST);
                    break;

                case 2: // Contacto (Email/Nombre, Telefono)
                    JPanel pnlContacto = new JPanel();
                    pnlContacto.setLayout(new BoxLayout(pnlContacto, BoxLayout.Y_AXIS));
                    pnlContacto.setOpaque(false);
                    pnlContacto.setBorder(new EmptyBorder(5, 0, 0, 0));
                    
                    JLabel lblContacto = new JLabel(prov.getContactoNombre());
                    lblContacto.setFont(new Font("SansSerif", Font.PLAIN, 13));
                    lblContacto.setForeground(Color.DARK_GRAY);
                    
                    JLabel lblTel = new JLabel(prov.getContactoTelefono());
                    lblTel.setFont(new Font("SansSerif", Font.PLAIN, 11));
                    lblTel.setForeground(Color.GRAY);
                    
                    pnlContacto.add(lblContacto);
                    pnlContacto.add(Box.createRigidArea(new Dimension(0, 3)));
                    pnlContacto.add(lblTel);
                    panel.add(pnlContacto, BorderLayout.WEST);
                    break;

                case 3: // Estado (Badge)
                    JLabel lblEstado = new JLabel(" • " + prov.getEstado() + " ");
                    lblEstado.setFont(new Font("SansSerif", Font.BOLD, 11));
                    lblEstado.setOpaque(true);
                    
                    String est = prov.getEstado() != null ? prov.getEstado().toLowerCase() : "";
                    if (est.contains("activo")) {
                        lblEstado.setBackground(new Color(230, 245, 245));
                        lblEstado.setForeground(brandBlue);
                    } else if (est.contains("revisión") || est.contains("revision")) {
                        lblEstado.setBackground(new Color(245, 240, 230));
                        lblEstado.setForeground(new Color(150, 100, 50));
                    } else {
                        lblEstado.setBackground(new Color(250, 230, 230));
                        lblEstado.setForeground(new Color(180, 50, 50));
                    }
                    
                    // Contenedor para que el badge no se estire
                    JPanel pnlEstado = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
                    pnlEstado.setOpaque(false);
                    pnlEstado.add(lblEstado);
                    panel.add(pnlEstado, BorderLayout.WEST);
                    break;
                    
                case 4: // Última Orden
                    JPanel pnlOrden = new JPanel();
                    pnlOrden.setLayout(new BoxLayout(pnlOrden, BoxLayout.Y_AXIS));
                    pnlOrden.setOpaque(false);
                    pnlOrden.setBorder(new EmptyBorder(5, 0, 0, 0));
                    
                    JLabel lblFecha = new JLabel(prov.getUltimaOrdenFecha());
                    lblFecha.setFont(new Font("SansSerif", Font.PLAIN, 13));
                    lblFecha.setForeground(Color.DARK_GRAY);
                    
                    // Simular un ID de orden
                    JLabel lblOrdId = new JLabel("ORD-" + prov.getId());
                    lblOrdId.setFont(new Font("SansSerif", Font.PLAIN, 11));
                    lblOrdId.setForeground(Color.GRAY);
                    
                    pnlOrden.add(lblFecha);
                    pnlOrden.add(Box.createRigidArea(new Dimension(0, 3)));
                    pnlOrden.add(lblOrdId);
                    panel.add(pnlOrden, BorderLayout.WEST);
                    break;
                    
                case 5: // ITEMS
                    JPanel pnlItems = new JPanel();
                    pnlItems.setLayout(new BoxLayout(pnlItems, BoxLayout.Y_AXIS));
                    pnlItems.setOpaque(false);
                    pnlItems.setBorder(new EmptyBorder(5, 0, 0, 0));
                    
                    int totalItems = 0;
                    if (prov.getInventariosSuministrados() != null) {
                        for (society.modell.administracion.Consumible c : prov.getInventariosSuministrados()) {
                            totalItems += c.getCantidad();
                        }
                    }
                    
                    JLabel lblItems = new JLabel(String.valueOf(totalItems));
                    lblItems.setFont(new Font("SansSerif", Font.BOLD, 14));
                    lblItems.setForeground(brandBlue);
                    
                    JLabel lblUnidades = new JLabel("unidades");
                    lblUnidades.setFont(new Font("SansSerif", Font.PLAIN, 11));
                    lblUnidades.setForeground(Color.GRAY);
                    
                    pnlItems.add(lblItems);
                    pnlItems.add(Box.createRigidArea(new Dimension(0, 3)));
                    pnlItems.add(lblUnidades);
                    panel.add(pnlItems, BorderLayout.WEST);
                    break;
            }
        }
        
        return panel;
    }
    
    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) return "PV";
        String[] parts = name.trim().split(" ");
        if (parts.length == 1) {
            return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
        } else {
            return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
        }
    }
    
    private String getIconForCategory(String cat) {
        if (cat == null) return "🏢";
        cat = cat.toLowerCase();
        if (cat.contains("medicamento") || cat.contains("farmacia")) return "➕"; // Cruz médica
        if (cat.contains("equipamiento") || cat.contains("equipo")) return "🖥️";
        if (cat.contains("alimento")) return "🍴";
        return "🏢";
    }
}
