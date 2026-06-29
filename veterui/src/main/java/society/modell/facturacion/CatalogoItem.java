package society.modell.facturacion;

public class CatalogoItem {
    private String id;
    private String titulo;
    private String descripcion;
    private double precio;
    private String categoria; // Médico, Estética, Alimentos
    private String iconoSVG;
    private String colorFondoSVG;
    private String colorIconoSVG;

    public CatalogoItem(String id, String titulo, String descripcion, double precio, String categoria, String iconoSVG, String colorFondoSVG, String colorIconoSVG) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.iconoSVG = iconoSVG;
        this.colorFondoSVG = colorFondoSVG;
        this.colorIconoSVG = colorIconoSVG;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public String getCategoria() { return categoria; }
    public String getIconoSVG() { return iconoSVG; }
    public String getColorFondoSVG() { return colorFondoSVG; }
    public String getColorIconoSVG() { return colorIconoSVG; }
}
