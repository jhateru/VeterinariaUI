package society.view;

import society.modell.inventario.Inventario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class InventarioCellRenderer implements TableCellRenderer {

    private Color brandBlue = new Color(0, 80, 100);
    private Color rowNormal = Color.WHITE;
    private Color rowSelected = new Color(240, 248, 255);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(isSelected ? rowSelected : rowNormal);
        panel.setBorder(new EmptyBorder(5, 10, 5, 10));
        panel.setOpaque(true);

        if (value instanceof Inventario) {
            Inventario inv = (Inventario) value;
            
            switch (column) {
                case 0: // PRODUCTO
                    JPanel pnlProd = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
                    pnlProd.setOpaque(false);
                    
                    JLabel lblIcon = new JLabel(getIconForCategory(inv.getCategoria()), SwingConstants.CENTER);
                    lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
                    lblIcon.setPreferredSize(new Dimension(40, 40));
                    lblIcon.setOpaque(true);
                    lblIcon.setBackground(new Color(240, 248, 255)); // light blue
                    lblIcon.setForeground(brandBlue);
                    lblIcon.setBorder(BorderFactory.createLineBorder(new Color(220, 235, 250), 1, true));
                    pnlProd.add(lblIcon);

                    JPanel pnlText = new JPanel();
                    pnlText.setLayout(new BoxLayout(pnlText, BoxLayout.Y_AXIS));
                    pnlText.setOpaque(false);

                    JLabel name = new JLabel(inv.getProducto());
                    name.setFont(new Font("SansSerif", Font.BOLD, 13));
                    name.setForeground(brandBlue);
                    
                    JLabel desc = new JLabel("Lote: " + inv.getLote() + " | Vence: " + inv.getFefo());
                    desc.setFont(new Font("SansSerif", Font.PLAIN, 10));
                    desc.setForeground(new Color(130, 130, 130));

                    pnlText.add(name);
                    pnlText.add(Box.createRigidArea(new Dimension(0, 3)));
                    pnlText.add(desc);
                    
                    pnlProd.add(pnlText);
                    panel.add(pnlProd, BorderLayout.WEST);
                    break;
                    
                case 1: // CATEGORÍA (Badge)
                    JLabel lblCat = new JLabel(inv.getCategoria());
                    lblCat.setFont(new Font("SansSerif", Font.BOLD, 10));
                    lblCat.setOpaque(true);
                    lblCat.setBackground(new Color(235, 235, 235));
                    lblCat.setForeground(Color.DARK_GRAY);
                    lblCat.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                    
                    JPanel pnlCat = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 15));
                    pnlCat.setOpaque(false);
                    pnlCat.add(lblCat);
                    panel.add(pnlCat, BorderLayout.CENTER);
                    break;

                case 2: // STOCK ACTUAL
                    JLabel lblStock = new JLabel(String.valueOf(inv.getStock()));
                    lblStock.setFont(new Font("SansSerif", Font.BOLD, 14));
                    if (inv.getStock() <= inv.getPuntoReorden()) {
                        lblStock.setForeground(Color.RED);
                    } else {
                        lblStock.setForeground(Color.DARK_GRAY);
                    }
                    
                    JPanel pnlStock = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
                    pnlStock.setOpaque(false);
                    pnlStock.add(lblStock);
                    panel.add(pnlStock, BorderLayout.CENTER);
                    break;
                    
                case 3: // UNIDAD
                    JLabel lblUnidad = new JLabel(inv.getUnidad());
                    lblUnidad.setFont(new Font("SansSerif", Font.PLAIN, 12));
                    lblUnidad.setForeground(Color.DARK_GRAY);
                    
                    JPanel pnlUnidad = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 15));
                    pnlUnidad.setOpaque(false);
                    pnlUnidad.add(lblUnidad);
                    panel.add(pnlUnidad, BorderLayout.CENTER);
                    break;

                case 4: // PUNTO REORDEN
                    JLabel lblReorden = new JLabel(String.valueOf(inv.getPuntoReorden()));
                    lblReorden.setFont(new Font("SansSerif", Font.PLAIN, 12));
                    lblReorden.setForeground(Color.DARK_GRAY);
                    
                    JPanel pnlReorden = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
                    pnlReorden.setOpaque(false);
                    pnlReorden.add(lblReorden);
                    panel.add(pnlReorden, BorderLayout.CENTER);
                    break;

                case 5: // ESTADO
                    String estado = inv.getEstado().toUpperCase();
                    Color dotColor;
                    
                    if (estado.contains("ESTABLE") || estado.contains("OPTIMO")) {
                        dotColor = new Color(0, 150, 150);
                    } else if (estado.contains("BAJO") || estado.contains("REORDEN")) {
                        dotColor = Color.RED;
                    } else { // VENCIMIENTO or others
                        dotColor = new Color(139, 69, 19); // brown
                    }

                    JPanel pnlEstado = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 15));
                    pnlEstado.setOpaque(false);
                    
                    // The dot
                    JPanel dot = new JPanel() {
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            Graphics2D g2 = (Graphics2D) g;
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setColor(dotColor);
                            g2.fillOval(0, 0, 6, 6);
                        }
                    };
                    dot.setPreferredSize(new Dimension(10, 10));
                    dot.setOpaque(false);
                    
                    JLabel lblEstado = new JLabel(estado);
                    lblEstado.setFont(new Font("SansSerif", Font.BOLD, 10));
                    lblEstado.setForeground(dotColor);
                    
                    pnlEstado.add(dot);
                    pnlEstado.add(lblEstado);
                    panel.add(pnlEstado, BorderLayout.CENTER);
                    break;
            }
        }
        
        return panel;
    }
    
    private String getIconForCategory(String cat) {
        if (cat == null) return "📦";
        cat = cat.toLowerCase();
        if (cat.contains("antibiótico") || cat.contains("medicamento") || cat.contains("anestésico")) return "💊";
        if (cat.contains("quirúrgico")) return "✂️";
        if (cat.contains("dietas")) return "🥫";
        if (cat.contains("vacuna")) return "💉";
        return "📦";
    }
}
