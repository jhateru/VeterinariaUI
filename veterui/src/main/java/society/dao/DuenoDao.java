package society.dao;

import society.modell.recepcion.Dueno;

public class DuenoDao extends CsvDao<Dueno> {

    public DuenoDao() {
        super("duenos.csv");
    }

    @Override
    protected Dueno fromCsv(String csvLine) {
        if (csvLine == null || csvLine.trim().isEmpty()) return null;
        String[] parts = csvLine.split(",", -1); // -1 to keep empty trailing fields
        if (parts.length < 9) return null;

        try {
            int id = Integer.parseInt(parts[0]);
            String nombre = parts[1];
            String apellidos = parts[2];
            String dni = parts[3];
            String telefono = parts[4];
            String email = parts[5];
            String direccion = parts[6];
            Dueno.EstadoDueno estado = Dueno.EstadoDueno.valueOf(parts[7]);
            String mascotas = parts[8];

            return new Dueno(id, nombre, apellidos, dni, telefono, email, direccion, estado, mascotas);
        } catch (Exception e) {
            System.err.println("Error parsing dueno CSV line: " + csvLine);
            return null;
        }
    }

    @Override
    protected String toCsv(Dueno entity) {
        return entity.getId() + "," +
               (entity.getNombre() != null ? entity.getNombre() : "") + "," +
               (entity.getApellidos() != null ? entity.getApellidos() : "") + "," +
               (entity.getDni() != null ? entity.getDni() : "") + "," +
               (entity.getTelefono() != null ? entity.getTelefono() : "") + "," +
               (entity.getEmail() != null ? entity.getEmail() : "") + "," +
               (entity.getDireccion() != null ? entity.getDireccion() : "") + "," +
               (entity.getEstado() != null ? entity.getEstado().name() : Dueno.EstadoDueno.NUEVO.name()) + "," +
               (entity.getMascotasNombres() != null ? entity.getMascotasNombres() : "");
    }
}
