package society.dao;

import society.modell.administracion.Proveedores;

public class ProveedoresDao extends CsvDao<Proveedores> {

    public ProveedoresDao() {
        super("proveedores.csv");
    }

    @Override
    protected Proveedores fromCsv(String csvLine) {
        if (csvLine == null || csvLine.trim().isEmpty()) return null;
        String[] parts = csvLine.split(";", -1);
        if (parts.length < 8) return null;

        try {
            Proveedores p = new Proveedores();
            p.setId(Integer.parseInt(parts[0]));
            p.setIdProveedorStr(parts[1]);
            p.setNombre(parts[2]);
            p.setCategoria(parts[3]);
            p.setContactoNombre(parts[4]);
            p.setContactoTelefono(parts[5]);
            p.setEstado(parts[6]);
            p.setUltimaOrdenFecha(parts[7]);
            return p;
        } catch (Exception e) {
            System.err.println("Error parsing proveedores CSV line: " + csvLine);
            return null;
        }
    }

    @Override
    protected String toCsv(Proveedores entity) {
        return String.format("%d;%s;%s;%s;%s;%s;%s;%s",
                entity.getId(),
                entity.getIdProveedorStr() != null ? entity.getIdProveedorStr() : "",
                entity.getNombre() != null ? entity.getNombre() : "",
                entity.getCategoria() != null ? entity.getCategoria() : "",
                entity.getContactoNombre() != null ? entity.getContactoNombre() : "",
                entity.getContactoTelefono() != null ? entity.getContactoTelefono() : "",
                entity.getEstado() != null ? entity.getEstado() : "",
                entity.getUltimaOrdenFecha() != null ? entity.getUltimaOrdenFecha() : ""
        );
    }
}
