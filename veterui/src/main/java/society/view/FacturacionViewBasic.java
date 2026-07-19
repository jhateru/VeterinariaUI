package society.view;

import society.dao.FacturacionDao;
import society.modell.facturacion.CatalogoItem;
import society.modell.facturacion.DetalleFacturacion;
import society.modell.facturacion.Facturacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FacturacionViewBasic extends JPanel {

    private Color darkTeal = new Color(0, 80, 100);
    private Color lightBg = new Color(248, 250, 250);
    private Color grayText = new Color(130, 130, 130);
    
    // MVC Models & DAO
    private Facturacion facturaActual;
    private FacturacionDao facturacionDao;
    private List<CatalogoItem> catalogoCompleto;

    // UI Elements
    private JPanel cartListPanel;
    private JLabel lblIdVenta;
    private JLabel lblSubtotal;
    private JLabel lblDescuentos;
    private JLabel lblIva;
    private JLabel lblTotal;
    private JPanel catalogoGrid;
    private DecimalFormat df = new DecimalFormat("#,##0.00");
    
    // Filtros UI
    private JTextField txtSearch;
    private JComboBox<String> cmbCategoriaServicio;
    private JComboBox<String> cmbCategoriaInventario;

    public FacturacionViewBasic() {
        facturacionDao = new FacturacionDao();
        cargarCatalogoReal();
        
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel catalogPanel = buildCatalogPanel();
        JPanel cartPanel = buildCartPanel();

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, catalogPanel, cartPanel);
        split.setDividerSize(1);
        split.setResizeWeight(0.65);
        split.setBorder(null);
        
        add(split, BorderLayout.CENTER);
        
        iniciarNuevaFactura();
        renderCatalogo();
    }
    
    private void cargarCatalogoReal() {
        catalogoCompleto = new ArrayList<>();
        
        society.dao.ServicioDao servicioDao = new society.dao.ServicioDao();
        List<society.modell.administracion.Servicio> servicios = servicioDao.getAll();
        
        society.dao.InventarioDao inventarioDao = new society.dao.InventarioDao();
        List<society.modell.inventario.Inventario> inventarios = inventarioDao.getAll();

        if (servicios != null) {
            for (society.modell.administracion.Servicio s : servicios) {
                String desc = s.getDescripcion() != null ? s.getDescripcion() : "Servicio";
                if (desc.length() > 30) desc = desc.substring(0, 27) + "...";
                String cat = s.getCategoria() != null ? s.getCategoria() : "Servicio";
                
                double costoMateriales = 0.0;
                if (s.getMaterialesUsados() != null) {
                    for (society.modell.administracion.Consumible c : s.getMaterialesUsados()) {
                        for (society.modell.inventario.Inventario inv : inventarios) {
                            if (inv.getId().equals(c.getInventarioId())) {
                                costoMateriales += (inv.getPrecio() * c.getCantidad());
                                break;
                            }
                        }
                    }
                }
                double costoTotal = s.getPrecioBase() + costoMateriales;

                catalogoCompleto.add(new CatalogoItem(
                    String.valueOf(s.getId()),
                    s.getNombre(),
                    desc,
                    costoTotal,
                    cat,
                    "🩺",
                    "#008080",
                    "white"
                ));
            }
        }

        if (inventarios != null) {
            for (society.modell.inventario.Inventario inv : inventarios) {
                String desc = "Stock: " + inv.getStock();
                String cat = inv.getCategoria() != null ? inv.getCategoria() : "Producto";

                catalogoCompleto.add(new CatalogoItem(
                    inv.getId(),
                    inv.getProducto(),
                    desc,
                    inv.getPrecio(),
                    cat,
                    "📦",
                    "#b49678",
                    "white"
                ));
            }
        }
    }
    
    private List<String> obtenerCategoriasPorTipo(String tipo) {
        List<String> categorias = new ArrayList<>();
        categorias.add("Todas");
        
        if (catalogoCompleto != null) {
            List<String> cats = catalogoCompleto.stream()
                .filter(item -> tipo.equals("SERVICIO") ? item.getIconoSVG().equals("🩺") : item.getIconoSVG().equals("📦"))
                .map(CatalogoItem::getCategoria)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
            categorias.addAll(cats);
        }
        return categorias;
    }
    
    private void iniciarNuevaFactura() {
        facturaActual = new Facturacion();
        facturaActual.setId("POS-" + (int)(Math.random() * 100000));
        facturaActual.setClienteInfo("Max, Golden Retriever, Dueño: Carlos R.");
        facturaActual.setDescuentoPorcentaje(10); // 10% por defecto (simulando "Cliente Frecuente")
        
        if (lblIdVenta != null) {
            lblIdVenta.setText("ID: #" + facturaActual.getId());
        }
        renderCart();
    }

    private JPanel buildCatalogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Catálogo");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(darkTeal);
        header.add(title, BorderLayout.WEST);

        // Filtros Tool Panel
        JPanel toolsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        toolsPanel.setBackground(Color.WHITE);
        
        JLabel lblSearch = new JLabel("Buscar:");
        lblSearch.setFont(new Font("SansSerif", Font.BOLD, 12));
        toolsPanel.add(lblSearch);
        
        txtSearch = new JTextField(15);
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { renderCatalogo(); }
            public void removeUpdate(DocumentEvent e) { renderCatalogo(); }
            public void changedUpdate(DocumentEvent e) { renderCatalogo(); }
        });
        toolsPanel.add(txtSearch);
        
        toolsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        
        JLabel lblCatServ = new JLabel("Categoría Servicio:");
        lblCatServ.setFont(new Font("SansSerif", Font.BOLD, 12));
        toolsPanel.add(lblCatServ);
        
        cmbCategoriaServicio = new JComboBox<>();
        cmbCategoriaServicio.setBackground(Color.WHITE);
        for (String cat : obtenerCategoriasPorTipo("SERVICIO")) {
            cmbCategoriaServicio.addItem(cat);
        }
        cmbCategoriaServicio.addActionListener(e -> renderCatalogo());
        toolsPanel.add(cmbCategoriaServicio);
        
        toolsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        
        JLabel lblCatInv = new JLabel("Categoría Inventario:");
        lblCatInv.setFont(new Font("SansSerif", Font.BOLD, 12));
        toolsPanel.add(lblCatInv);
        
        cmbCategoriaInventario = new JComboBox<>();
        cmbCategoriaInventario.setBackground(Color.WHITE);
        for (String cat : obtenerCategoriasPorTipo("PRODUCTO")) {
            cmbCategoriaInventario.addItem(cat);
        }
        cmbCategoriaInventario.addActionListener(e -> renderCatalogo());
        toolsPanel.add(cmbCategoriaInventario);
        
        header.add(toolsPanel, BorderLayout.SOUTH);
        panel.add(header, BorderLayout.NORTH);

        // Grid of products
        catalogoGrid = new JPanel(new GridLayout(0, 3, 15, 15));
        catalogoGrid.setBackground(Color.WHITE);
        catalogoGrid.setBorder(new EmptyBorder(20, 0, 0, 0));

        JScrollPane scroll = new JScrollPane(catalogoGrid);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }
    
    private List<CatalogoItem> getFilteredCatalog() {
        if (catalogoCompleto == null) return new ArrayList<>();
        
        String query = txtSearch.getText().trim().toLowerCase();
        String catServ = (String) cmbCategoriaServicio.getSelectedItem();
        String catInv = (String) cmbCategoriaInventario.getSelectedItem();
        
        boolean filterServ = catServ != null && !catServ.equals("Todas");
        boolean filterInv = catInv != null && !catInv.equals("Todas");
        
        return catalogoCompleto.stream()
            .filter(item -> item.getTitulo().toLowerCase().contains(query))
            .filter(item -> {
                boolean isServicio = item.getIconoSVG().equals("🩺");
                if (isServicio && filterServ) return item.getCategoria().equals(catServ);
                if (!isServicio && filterInv) return item.getCategoria().equals(catInv);
                return true;
            })
            .collect(Collectors.toList());
    }

    private void renderCatalogo() {
        if (catalogoGrid == null) return;
        catalogoGrid.removeAll();
        
        List<CatalogoItem> filtrados = getFilteredCatalog();
        
        for (CatalogoItem item : filtrados) {
            catalogoGrid.add(createProductCard(item));
        }
        catalogoGrid.revalidate();
        catalogoGrid.repaint();
    }

    private JPanel createProductCard(CatalogoItem item) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));

        // Top: Icon
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        top.setBackground(Color.WHITE);
        JLabel lblIcon = new JLabel(item.getIconoSVG(), SwingConstants.CENTER); // Usando iconoSVG como Emoji textual
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        lblIcon.setOpaque(true);
        try {
            lblIcon.setBackground(Color.decode(item.getColorFondoSVG()));
        } catch (Exception e) {
            lblIcon.setBackground(Color.GRAY);
        }
        lblIcon.setForeground(Color.WHITE);
        lblIcon.setPreferredSize(new Dimension(40, 40));
        top.add(lblIcon);
        card.add(top, BorderLayout.NORTH);

        // Center: Texts
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.WHITE);
        center.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JLabel lblName = new JLabel("<html><div style='width:120px;'>" + item.getTitulo() + "</div></html>");
        lblName.setFont(new Font("SansSerif", Font.BOLD, 15));
        center.add(lblName);
        
        center.add(Box.createRigidArea(new Dimension(0, 5)));
        JLabel lblDesc = new JLabel("<html><div style='width:120px; color:#888888;'>" + item.getDescripcion() + "</div></html>");
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 11));
        center.add(lblDesc);
        card.add(center, BorderLayout.CENTER);

        // Bottom: Price & Category
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        JLabel lblPrice = new JLabel("$" + df.format(item.getPrecio()));
        lblPrice.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblPrice.setForeground(darkTeal);
        bottom.add(lblPrice, BorderLayout.WEST);
        
        JLabel lblCat = new JLabel(" " + item.getCategoria().toUpperCase() + " ");
        lblCat.setFont(new Font("SansSerif", Font.BOLD, 9));
        lblCat.setOpaque(true);
        lblCat.setBackground(new Color(240, 240, 240));
        lblCat.setForeground(Color.DARK_GRAY);
        bottom.add(lblCat, BorderLayout.EAST);
        
        card.add(bottom, BorderLayout.SOUTH);

        // Add Click Action
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                agregarAlCarrito(item);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(darkTeal, 1, true),
                    new EmptyBorder(15, 15, 15, 15)
                ));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                    new EmptyBorder(15, 15, 15, 15)
                ));
            }
        });

        return card;
    }

    private JPanel buildCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(lightBg);
        panel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(220, 220, 220)));

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(lightBg);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(lightBg);
        JLabel title = new JLabel("Detalle de Venta");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        header.add(title, BorderLayout.WEST);
        
        lblIdVenta = new JLabel("ID: #POS-00000");
        lblIdVenta.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblIdVenta.setForeground(darkTeal);
        header.add(lblIdVenta, BorderLayout.EAST);
        
        // Patient Card
        JPanel patientCard = new JPanel(new BorderLayout(10, 0));
        patientCard.setBackground(Color.WHITE);
        patientCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 220, 220), 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        JLabel lblAvatar = new JLabel("🐶");
        lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        patientCard.add(lblAvatar, BorderLayout.WEST);
        
        JPanel pCenter = new JPanel();
        pCenter.setLayout(new BoxLayout(pCenter, BoxLayout.Y_AXIS));
        pCenter.setBackground(Color.WHITE);
        JLabel pName = new JLabel("Max");
        pName.setFont(new Font("SansSerif", Font.BOLD, 14));
        JLabel pDesc = new JLabel("Golden Retriever • Dueño: Carlos R.");
        pDesc.setFont(new Font("SansSerif", Font.PLAIN, 11));
        pDesc.setForeground(grayText);
        pCenter.add(pName);
        pCenter.add(pDesc);
        patientCard.add(pCenter, BorderLayout.CENTER);
        
        JLabel lblEdit = new JLabel("✏️");
        lblEdit.setForeground(Color.BLUE);
        lblEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        patientCard.add(lblEdit, BorderLayout.EAST);

        JPanel topSection = new JPanel(new BorderLayout(0, 15));
        topSection.setBackground(lightBg);
        topSection.add(header, BorderLayout.NORTH);
        topSection.add(patientCard, BorderLayout.CENTER);
        
        content.add(topSection, BorderLayout.NORTH);

        // Cart Items List
        cartListPanel = new JPanel();
        cartListPanel.setLayout(new BoxLayout(cartListPanel, BoxLayout.Y_AXIS));
        cartListPanel.setBackground(Color.WHITE);
        
        JScrollPane scroll = new JScrollPane(cartListPanel);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scroll.setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        
        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setBackground(lightBg);
        listWrapper.setBorder(new EmptyBorder(15, 0, 15, 0));
        listWrapper.add(scroll, BorderLayout.CENTER);
        content.add(listWrapper, BorderLayout.CENTER);

        // Footer Summary
        JPanel footer = new JPanel(new BorderLayout(0, 15));
        footer.setBackground(lightBg);
        
        // Buttons % Descuento / Cupon
        JPanel discBtns = new JPanel(new GridLayout(1, 2, 10, 0));
        discBtns.setBackground(lightBg);
        JButton btnDesc = new JButton("% Descuento");
        btnDesc.setBackground(Color.WHITE);
        btnDesc.setFocusPainted(false);
        JButton btnCup = new JButton("🎫 Cupón");
        btnCup.setBackground(Color.WHITE);
        btnCup.setFocusPainted(false);
        discBtns.add(btnDesc);
        discBtns.add(btnCup);
        footer.add(discBtns, BorderLayout.NORTH);
        
        // Totals Grid
        JPanel totals = new JPanel(new GridLayout(3, 2, 0, 8));
        totals.setBackground(lightBg);
        
        totals.add(createSummaryLabel("Subtotal", false));
        lblSubtotal = createSummaryLabel("$0.00", true);
        totals.add(lblSubtotal);
        
        totals.add(createSummaryLabel("Descuentos", false));
        lblDescuentos = createSummaryLabel("-$0.00", true);
        lblDescuentos.setForeground(new Color(200, 50, 50));
        totals.add(lblDescuentos);
        
        totals.add(createSummaryLabel("IVA (15%)", false));
        lblIva = createSummaryLabel("$0.00", true);
        totals.add(lblIva);
        
        // Separator and Grand Total
        JPanel grandPanel = new JPanel(new BorderLayout());
        grandPanel.setBackground(lightBg);
        grandPanel.setBorder(new EmptyBorder(15, 0, 15, 0));
        grandPanel.add(new JSeparator(), BorderLayout.NORTH);
        
        JPanel grandValues = new JPanel(new BorderLayout());
        grandValues.setBackground(lightBg);
        grandValues.setBorder(new EmptyBorder(15, 0, 0, 0));
        JLabel lblTText = new JLabel("Total");
        lblTText.setFont(new Font("SansSerif", Font.PLAIN, 20));
        grandValues.add(lblTText, BorderLayout.WEST);
        
        lblTotal = new JLabel("$0.00");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTotal.setForeground(darkTeal);
        grandValues.add(lblTotal, BorderLayout.EAST);
        grandPanel.add(grandValues, BorderLayout.CENTER);
        
        JPanel summaryWrap = new JPanel(new BorderLayout());
        summaryWrap.setBackground(lightBg);
        summaryWrap.add(totals, BorderLayout.NORTH);
        summaryWrap.add(grandPanel, BorderLayout.CENTER);
        
        footer.add(summaryWrap, BorderLayout.CENTER);
        
        // Action Buttons
        JPanel actions = new JPanel(new GridLayout(1, 2, 10, 0));
        actions.setBackground(lightBg);
        
        JButton btnSave = new JButton("<html><center>Guardar<br>Proforma</center></html>");
        btnSave.setBackground(Color.WHITE);
        btnSave.setForeground(darkTeal);
        btnSave.setBorder(BorderFactory.createLineBorder(darkTeal, 1));
        btnSave.setFocusPainted(false);
        btnSave.setPreferredSize(new Dimension(0, 50));
        btnSave.addActionListener(e -> guardarFacturaEnDAO(Facturacion.EstadoFactura.PROFORMA));
        
        JButton btnPay = new JButton("Procesar Pago >");
        btnPay.setBackground(darkTeal);
        btnPay.setForeground(Color.WHITE);
        btnPay.setFocusPainted(false);
        btnPay.setPreferredSize(new Dimension(0, 50));
        btnPay.addActionListener(e -> guardarFacturaEnDAO(Facturacion.EstadoFactura.PAGADA));
        
        actions.add(btnSave);
        actions.add(btnPay);
        footer.add(actions, BorderLayout.SOUTH);
        
        content.add(footer, BorderLayout.SOUTH);
        panel.add(content, BorderLayout.CENTER);

        return panel;
    }
    
    private JLabel createSummaryLabel(String text, boolean right) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        l.setForeground(Color.DARK_GRAY);
        if (right) {
            l.setHorizontalAlignment(SwingConstants.RIGHT);
            l.setFont(new Font("SansSerif", Font.BOLD, 12));
        }
        return l;
    }

    // --- Lógica MVC con Modelos y DAOs ---

    private void agregarAlCarrito(CatalogoItem item) {
        boolean encontrado = false;
        for (DetalleFacturacion dfac : facturaActual.getDetalles()) {
            if (dfac.getItem().getId().equals(item.getId())) {
                dfac.setCantidad(dfac.getCantidad() + 1);
                encontrado = true;
                break;
            }
        }
        
        if (!encontrado) {
            facturaActual.addDetalle(new DetalleFacturacion(item, 1, 0.0));
        }
        renderCart();
    }

    private void renderCart() {
        if (cartListPanel == null) return;
        cartListPanel.removeAll();
        
        for (DetalleFacturacion dfac : facturaActual.getDetalles()) {
            cartListPanel.add(buildCartRow(dfac));
            JSeparator sep = new JSeparator();
            sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            cartListPanel.add(sep);
        }
        
        // Render discount summary row if applicable
        if (facturaActual.getDescuentoPorcentaje() > 0 && !facturaActual.getDetalles().isEmpty()) {
            cartListPanel.add(buildDiscountRow());
            JSeparator sep2 = new JSeparator();
            sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            cartListPanel.add(sep2);
        }
        
        // Filler
        cartListPanel.add(Box.createVerticalGlue());
        cartListPanel.revalidate();
        cartListPanel.repaint();
        
        lblSubtotal.setText("$" + df.format(facturaActual.getSubtotal()));
        lblDescuentos.setText("-$" + df.format(facturaActual.getDescuentoTotal()));
        lblIva.setText("$" + df.format(facturaActual.getIvaTotal()));
        lblTotal.setText("$" + df.format(facturaActual.getTotal()));
    }

    private JPanel buildDiscountRow() {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setBorder(new EmptyBorder(10, 15, 10, 15));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        JLabel lblIcon = new JLabel("🏷️", SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        lblIcon.setOpaque(true);
        lblIcon.setBackground(new Color(240, 240, 240));
        lblIcon.setForeground(Color.DARK_GRAY);
        lblIcon.setPreferredSize(new Dimension(35, 35));
        
        JPanel pIcon = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        pIcon.setBackground(Color.WHITE);
        pIcon.add(lblIcon);
        row.add(pIcon, BorderLayout.WEST);
        
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.WHITE);
        center.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        JLabel name = new JLabel("Descuento Cliente Frecuente");
        name.setFont(new Font("SansSerif", Font.BOLD, 12));
        center.add(name);
        
        JLabel desc = new JLabel("-" + (int)facturaActual.getDescuentoPorcentaje() + "% sobre servicios");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 11));
        desc.setForeground(new Color(200, 100, 100));
        center.add(desc);
        
        row.add(center, BorderLayout.CENTER);
        
        JLabel lblPrice = new JLabel("-$" + df.format(facturaActual.getDescuentoTotal()));
        lblPrice.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblPrice.setForeground(new Color(200, 50, 50));
        lblPrice.setHorizontalAlignment(SwingConstants.RIGHT);
        row.add(lblPrice, BorderLayout.EAST);
        
        return row;
    }

    private JPanel buildCartRow(DetalleFacturacion dfac) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setBorder(new EmptyBorder(10, 15, 10, 15));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        // Left Icon
        JLabel lblIcon = new JLabel(dfac.getItem().getIconoSVG(), SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        lblIcon.setOpaque(true);
        try {
            lblIcon.setBackground(Color.decode(dfac.getItem().getColorFondoSVG()));
        } catch (Exception ex) {
            lblIcon.setBackground(Color.GRAY);
        }
        lblIcon.setForeground(Color.WHITE);
        lblIcon.setPreferredSize(new Dimension(35, 35));
        
        JPanel pIcon = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        pIcon.setBackground(Color.WHITE);
        pIcon.add(lblIcon);
        row.add(pIcon, BorderLayout.WEST);
        
        // Center text
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.WHITE);
        center.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        JLabel name = new JLabel(dfac.getItem().getTitulo());
        name.setFont(new Font("SansSerif", Font.BOLD, 12));
        center.add(name);
        
        JLabel unit = new JLabel("Unidad: $" + df.format(dfac.getItem().getPrecio()));
        unit.setFont(new Font("SansSerif", Font.PLAIN, 11));
        unit.setForeground(grayText);
        center.add(unit);
        
        row.add(center, BorderLayout.CENTER);
        
        // Right Controls & Price
        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(Color.WHITE);
        
        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        ctrl.setBackground(Color.WHITE);
        
        JButton btnMinus = new JButton("-");
        btnMinus.setMargin(new Insets(0,5,0,5));
        btnMinus.setFocusPainted(false);
        btnMinus.addActionListener(e -> {
            if (dfac.getCantidad() > 1) {
                dfac.setCantidad(dfac.getCantidad() - 1);
            } else {
                facturaActual.getDetalles().remove(dfac);
            }
            renderCart();
        });
        
        JLabel lblQty = new JLabel(String.valueOf(dfac.getCantidad()));
        lblQty.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        JButton btnPlus = new JButton("+");
        btnPlus.setMargin(new Insets(0,5,0,5));
        btnPlus.setFocusPainted(false);
        btnPlus.addActionListener(e -> {
            dfac.setCantidad(dfac.getCantidad() + 1);
            renderCart();
        });
        
        ctrl.add(btnMinus);
        ctrl.add(lblQty);
        ctrl.add(btnPlus);
        right.add(ctrl, BorderLayout.NORTH);
        
        JLabel lblPrice = new JLabel("$" + df.format(dfac.getSubtotal()));
        lblPrice.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblPrice.setHorizontalAlignment(SwingConstants.RIGHT);
        right.add(lblPrice, BorderLayout.SOUTH);
        
        row.add(right, BorderLayout.EAST);
        
        return row;
    }
    
    private void guardarFacturaEnDAO(Facturacion.EstadoFactura estado) {
        if (facturaActual.getDetalles().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        facturaActual.setEstado(estado);
        facturaActual.setFechaHora(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        
        facturacionDao.save(facturaActual); // <-- MVC: DAO persistencia
        
        String msj = estado == Facturacion.EstadoFactura.PAGADA ? "Pago procesado con éxito." : "Proforma guardada exitosamente.";
        JOptionPane.showMessageDialog(this, msj, "Éxito", JOptionPane.INFORMATION_MESSAGE);
        
        iniciarNuevaFactura();
    }
}
