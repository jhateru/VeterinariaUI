package society.dao;

import society.modell.recepcion.Cita;
import java.time.LocalDateTime;

public class CitaDao extends CsvDao<Cita> {

    public CitaDao() {
        super("citas.csv");
    }

    @Override
    protected Cita fromCsv(String csvLine) {
        if (csvLine == null || csvLine.trim().isEmpty()) return null;
        String[] parts = csvLine.split(",");
        if (parts.length < 6) return null;
        
        try {
            int id = Integer.parseInt(parts[0]);
            LocalDateTime fechaHora = LocalDateTime.parse(parts[1]);
            Cita.EstadoCita estado = Cita.EstadoCita.valueOf(parts[2]);
            String paciente = parts[3];
            String veterinario = parts[4];
            String motivo = parts[5];
            
            return new Cita(id, fechaHora, estado, paciente, veterinario, motivo);
        } catch (Exception e) {
            System.err.println("Error parsing cita CSV line: " + csvLine);
            return null;
        }
    }

    @Override
    protected String toCsv(Cita entity) {
        return entity.getId() + "," +
               entity.getFechaHora().toString() + "," +
               entity.getEstado().name() + "," +
               (entity.getPacienteNombre() != null ? entity.getPacienteNombre() : "") + "," +
               (entity.getVeterinarioNombre() != null ? entity.getVeterinarioNombre() : "") + "," +
               (entity.getMotivo() != null ? entity.getMotivo() : "");
    }
}
