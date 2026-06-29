package society.dao;

import society.modell.areamedica.Laboratorio;
import society.modell.recepcion.Paciente;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class LaboratorioDao extends CsvDao<Laboratorio> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final PacienteDao pacienteDao;

    public LaboratorioDao() {
        super("laboratorios.csv");
        this.pacienteDao = new PacienteDao();
    }

    @Override
    protected Laboratorio fromCsv(String csvLine) {
        if (csvLine == null || csvLine.trim().isEmpty()) return null;
        String[] parts = csvLine.split(";", -1);
        if (parts.length < 8) return null;

        try {
            Laboratorio lab = new Laboratorio();
            lab.setId(Integer.parseInt(parts[0]));
            
            int pacienteId = Integer.parseInt(parts[1]);
            Optional<Paciente> pacOpt = pacienteDao.getAll().stream()
                                            .filter(p -> p.getId() == pacienteId)
                                            .findFirst();
            pacOpt.ifPresent(lab::setPaciente);

            lab.setTipoExamen(parts[2]);
            lab.setDoctorAsignado(parts[3]);
            lab.setPrioridad(parts[4]);
            lab.setEstado(parts[5]);
            lab.setResultadosCortos(parts[6].replace("~", ";").replace("^", "\n"));
            lab.setRequiereAtencion(Boolean.parseBoolean(parts[7]));
            
            if (parts.length > 8 && !parts[8].isEmpty()) lab.setFechaSolicitud(LocalDateTime.parse(parts[8], FORMATTER));
            if (parts.length > 9 && !parts[9].isEmpty()) lab.setFechaResultado(LocalDateTime.parse(parts[9], FORMATTER));
            if (parts.length > 10) lab.setTiempoFinalizadoStr(parts[10]);

            return lab;
        } catch (Exception e) {
            System.err.println("Error parsing laboratorio CSV line: " + csvLine);
            return null;
        }
    }

    @Override
    protected String toCsv(Laboratorio entity) {
        String pacienteId = entity.getPaciente() != null ? String.valueOf(entity.getPaciente().getId()) : "";
        String fs = entity.getFechaSolicitud() != null ? entity.getFechaSolicitud().format(FORMATTER) : "";
        String fr = entity.getFechaResultado() != null ? entity.getFechaResultado().format(FORMATTER) : "";
        
        String notas = entity.getResultadosCortos() != null ? entity.getResultadosCortos().replace(";", "~").replace("\n", "^") : "";

        return String.format("%d;%s;%s;%s;%s;%s;%s;%b;%s;%s;%s",
                entity.getId(),
                pacienteId,
                entity.getTipoExamen() != null ? entity.getTipoExamen() : "",
                entity.getDoctorAsignado() != null ? entity.getDoctorAsignado() : "",
                entity.getPrioridad() != null ? entity.getPrioridad() : "Normal",
                entity.getEstado() != null ? entity.getEstado() : "En Proceso",
                notas,
                entity.isRequiereAtencion(),
                fs,
                fr,
                entity.getTiempoFinalizadoStr() != null ? entity.getTiempoFinalizadoStr() : ""
        );
    }
}
