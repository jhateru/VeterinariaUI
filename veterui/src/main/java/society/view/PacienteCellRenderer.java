package society.view;

import society.modell.recepcion.Paciente;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class PacienteCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (!(value instanceof Paciente)) {
            return new JLabel();
        }
        
        Paciente p = (Paciente) value;
        JPanel cell = new JPanel();
        cell.setOpaque(true);
        cell.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
        cell.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));
        
        Color textColor = isSelected ? Color.WHITE : Color.BLACK;
        Color subTextColor = isSelected ? Color.WHITE : Color.GRAY;

        switch (column) {
            case 0: // PACIENTE
                cell.setLayout(new BorderLayout(10, 0));
                cell.setBorder(BorderFactory.createCompoundBorder(cell.getBorder(), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
                
                JLabel lblAvatar = new JLabel(p.getNombre().substring(0, 1).toUpperCase());
                lblAvatar.setPreferredSize(new Dimension(40, 40));
                lblAvatar.setOpaque(true);
                lblAvatar.setBackground(new Color(220, 220, 220));
                lblAvatar.setForeground(Color.DARK_GRAY);
                lblAvatar.setHorizontalAlignment(SwingConstants.CENTER);
                lblAvatar.setFont(new Font("SansSerif", Font.BOLD, 14));
                cell.add(lblAvatar, BorderLayout.WEST);

                JPanel pnlTextos = new JPanel(new GridLayout(2, 1));
                pnlTextos.setOpaque(false);
                JLabel lblNombre = new JLabel(p.getNombre());
                lblNombre.setFont(new Font("SansSerif", Font.BOLD, 13));
                lblNombre.setForeground(textColor);
                JLabel lblInfo = new JLabel(p.getSexo() + ", " + p.getEdadAproximada());
                lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 11));
                lblInfo.setForeground(subTextColor);
                pnlTextos.add(lblNombre);
                pnlTextos.add(lblInfo);
                cell.add(pnlTextos, BorderLayout.CENTER);
                return cell;

            case 1: // ESPECIE / RAZA
                cell.setLayout(new GridLayout(2, 1));
                cell.setBorder(BorderFactory.createCompoundBorder(cell.getBorder(), BorderFactory.createEmptyBorder(15, 10, 10, 10)));
                JLabel lblEspecie = new JLabel(p.getEspecie() != null ? capitalize(p.getEspecie().name()) : "");
                lblEspecie.setFont(new Font("SansSerif", Font.PLAIN, 13));
                lblEspecie.setForeground(textColor);
                JLabel lblRaza = new JLabel(p.getRaza());
                lblRaza.setFont(new Font("SansSerif", Font.PLAIN, 11));
                lblRaza.setForeground(subTextColor);
                cell.add(lblEspecie);
                cell.add(lblRaza);
                return cell;

            case 2: // DUEÑO
                cell.setLayout(new GridLayout(2, 1));
                cell.setBorder(BorderFactory.createCompoundBorder(cell.getBorder(), BorderFactory.createEmptyBorder(15, 10, 10, 10)));
                JLabel lblDueno = new JLabel(p.getNombreDueno());
                lblDueno.setFont(new Font("SansSerif", Font.PLAIN, 13));
                lblDueno.setForeground(textColor);
                JLabel lblTel = new JLabel(p.getTelefonoDueno());
                lblTel.setFont(new Font("SansSerif", Font.PLAIN, 11));
                lblTel.setForeground(isSelected ? Color.WHITE : new Color(30, 100, 200));
                cell.add(lblDueno);
                cell.add(lblTel);
                return cell;

            case 3: // ÚLTIMA VISITA
                cell.setLayout(new BorderLayout());
                cell.setBorder(BorderFactory.createCompoundBorder(cell.getBorder(), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
                JLabel lblUltima = new JLabel(p.getUltimaVisita());
                lblUltima.setFont(new Font("SansSerif", Font.PLAIN, 13));
                lblUltima.setForeground(textColor);
                lblUltima.setVerticalAlignment(SwingConstants.CENTER);
                cell.add(lblUltima, BorderLayout.CENTER);
                return cell;

            case 4: // ESTADO
                cell.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 20)); // Centrado vertical manual
                String estadoStr = p.getEstado() != null ? capitalize(p.getEstado().name().replace("_", " ")) : "";
                JLabel lblEstado = new JLabel("● " + estadoStr);
                lblEstado.setOpaque(true);
                lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 11));
                lblEstado.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
                
                if (p.getEstado() == Paciente.EstadoPaciente.EN_CLINICA) {
                    lblEstado.setBackground(new Color(220, 255, 220));
                    lblEstado.setForeground(new Color(0, 150, 0));
                } else if (p.getEstado() == Paciente.EstadoPaciente.ALTA) {
                    lblEstado.setBackground(new Color(240, 240, 240));
                    lblEstado.setForeground(Color.GRAY);
                } else {
                    lblEstado.setBackground(new Color(255, 245, 220));
                    lblEstado.setForeground(new Color(200, 150, 0));
                }
                cell.add(lblEstado);
                return cell;

            case 5: // ACCIONES
                cell.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20));
                JLabel lblView = new JLabel("👁");
                lblView.setFont(new Font("SansSerif", Font.PLAIN, 16));
                lblView.setForeground(subTextColor);
                JLabel lblEdit = new JLabel("✎");
                lblEdit.setFont(new Font("SansSerif", Font.PLAIN, 16));
                lblEdit.setForeground(subTextColor);
                cell.add(lblView);
                cell.add(lblEdit);
                return cell;

            default:
                return cell;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
