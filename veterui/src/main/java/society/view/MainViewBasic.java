package society.view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainViewBasic extends JPanel {
    private JPanel centerPanel;
    private Map<String, JPanel> viewCache = new HashMap<>();

    public MainViewBasic() {
        setLayout(new BorderLayout());

        add(new SideBarBasic(this), BorderLayout.WEST);
        add(new TopBarBasic(), BorderLayout.NORTH);
        
        centerPanel = new JPanel(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);
        
        // Cargar vista inicial
        cargarVista("DashboardResumenBasic");
    }

    public void cargarVista(String vistaClassName) {
        if (viewCache.containsKey(vistaClassName)) {
            mostrarVista(viewCache.get(vistaClassName));
            return;
        }

        try {
            Class<?> clazz = Class.forName("society.view." + vistaClassName);
            JPanel nuevaVista = (JPanel) clazz.getDeclaredConstructor().newInstance();
            viewCache.put(vistaClassName, nuevaVista);
            mostrarVista(nuevaVista);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista en Swing: " + vistaClassName);
        }
    }

    private void mostrarVista(JPanel vista) {
        centerPanel.removeAll();
        centerPanel.add(vista, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }
    
    public JPanel getView(String vistaClassName) {
        return viewCache.get(vistaClassName);
    }
}
