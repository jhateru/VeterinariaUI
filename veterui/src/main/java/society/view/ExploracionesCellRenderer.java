package society.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ExploracionesCellRenderer implements TableCellRenderer {

    private Color brandBlue = new Color(0, 80, 100);
    private Color rowNormal = Color.WHITE;
    private Color rowSelected = new Color(245, 250, 250);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(isSelected ? rowSelected : rowNormal);
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));

        if (value instanceof String[]) {
            String[] parts = (String[]) value;
            
            // parts = [Fecha, Motivo, Exploracion, Diagnostico, ProximoControl, V]
            
            JPanel pnlText = new JPanel();
            pnlText.setLayout(new BoxLayout(pnlText, BoxLayout.Y_AXIS));
            pnlText.setOpaque(false);
            
            JLabel mainLabel = new JLabel();
            mainLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
            mainLabel.setForeground(Color.DARK_GRAY);
            
            JLabel subLabel = new JLabel();
            subLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
            subLabel.setForeground(Color.GRAY);

            switch (column) {
                case 0: // FECHA
                    mainLabel.setText(parts.length > 0 ? parts[0] : "");
                    subLabel.setText("Visita pasada");
                    break;
                case 1: // MOTIVO
                    mainLabel.setText(parts.length > 1 ? parts[1] : "");
                    subLabel.setText("Razón de consulta");
                    break;
                case 2: // EXPLORACIÓN
                    String explo = parts.length > 2 ? parts[2] : "";
                    if (explo.length() > 40) {
                        mainLabel.setText(explo.substring(0, 40) + "...");
                        mainLabel.setToolTipText(explo);
                    } else {
                        mainLabel.setText(explo);
                    }
                    subLabel.setText("Signos vitales");
                    break;
                case 3: // DIAGNÓSTICO
                    String diagPart = parts.length > 3 ? parts[3] : "";
                    if (diagPart.contains(" | ")) {
                        String[] dSplit = diagPart.split(" \\| ");
                        mainLabel.setText(dSplit[0]);
                        subLabel.setText(dSplit.length > 1 ? dSplit[1] : "Sin descripción");
                    } else {
                        mainLabel.setText(diagPart);
                        subLabel.setText("Sin descripción");
                    }
                    mainLabel.setForeground(brandBlue);
                    break;
                case 4: // PRÓXIMO CONTROL
                    mainLabel.setText(parts.length > 4 ? parts[4] : "");
                    subLabel.setText("Cita sugerida");
                    break;
            }
            
            pnlText.add(mainLabel);
            pnlText.add(Box.createRigidArea(new Dimension(0, 3)));
            pnlText.add(subLabel);
            panel.add(pnlText, BorderLayout.WEST);
        }
        
        return panel;
    }
}
