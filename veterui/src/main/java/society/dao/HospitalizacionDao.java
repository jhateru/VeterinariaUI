package society.dao;

import society.modell.areamedica.Hospitalizacion;
import society.modell.recepcion.Paciente;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HospitalizacionDao {
    private static final String CSV_FILE = "veterui/data/hospitalizaciones.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final PacienteDao pacienteDao;

    public HospitalizacionDao() {
        this.pacienteDao = new PacienteDao();
        File file = new File(CSV_FILE);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save(Hospitalizacion hosp) {
        List<Hospitalizacion> all = findAll();
        if (hosp.getId() == 0) {
            int newId = all.stream().mapToInt(Hospitalizacion::getId).max().orElse(0) + 1;
            hosp.setId(newId);
        } else {
            all.removeIf(h -> h.getId() == hosp.getId());
        }
        all.add(hosp);
        writeToFile(all);
    }

    public List<Hospitalizacion> findAll() {
        List<Hospitalizacion> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(";", -1);
                
                Hospitalizacion h = new Hospitalizacion();
                h.setId(Integer.parseInt(p[0]));
                
                int pacienteId = Integer.parseInt(p[1]);
                Optional<Paciente> pacOpt = pacienteDao.getAll().stream()
                                            .filter(pac -> pac.getId() == pacienteId)
                                            .findFirst();
                pacOpt.ifPresent(h::setPaciente);
                
                h.setFechaIngreso(p[2].isEmpty() ? null : LocalDateTime.parse(p[2], FORMATTER));
                h.setFechaAlta(p[3].isEmpty() ? null : LocalDateTime.parse(p[3], FORMATTER));
                h.setMotivo(p[4]);
                h.setJaulaAsignada(p[5]);
                h.setEstado(Hospitalizacion.EstadoHospitalizacion.valueOf(p[6]));
                
                // Nuevos campos (soporte retrocompatible)
                if (p.length > 7) h.setEstadoMedicoInicial(p[7]);
                if (p.length > 8) h.setFrecuenciaMonitoreo(p[8]);
                if (p.length > 9) h.setDietaEspecial(p[9]);
                if (p.length > 10) h.setCronogramaMedicacion(p[10]);
                if (p.length > 11) h.setAlertasSignos(p[11]);
                if (p.length > 12) h.setTemperaturaIngreso(p[12]);
                if (p.length > 13) h.setFrecCardiacaIngreso(p[13]);
                if (p.length > 14) h.setFrecRespiratoriaIngreso(p[14]);

                list.add(h);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private void writeToFile(List<Hospitalizacion> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
            for (Hospitalizacion h : list) {
                String pacienteId = h.getPaciente() != null ? String.valueOf(h.getPaciente().getId()) : "";
                String fi = h.getFechaIngreso() != null ? h.getFechaIngreso().format(FORMATTER) : "";
                String fa = h.getFechaAlta() != null ? h.getFechaAlta().format(FORMATTER) : "";
                
                String line = String.format("%d;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s",
                        h.getId(), pacienteId, fi, fa,
                        h.getMotivo() != null ? h.getMotivo() : "",
                        h.getJaulaAsignada() != null ? h.getJaulaAsignada() : "",
                        h.getEstado().name(),
                        h.getEstadoMedicoInicial() != null ? h.getEstadoMedicoInicial() : "",
                        h.getFrecuenciaMonitoreo() != null ? h.getFrecuenciaMonitoreo() : "",
                        h.getDietaEspecial() != null ? h.getDietaEspecial().replace("\n", " ").replace(";", ",") : "",
                        h.getCronogramaMedicacion() != null ? h.getCronogramaMedicacion().replace("\n", " ").replace(";", ",") : "",
                        h.getAlertasSignos() != null ? h.getAlertasSignos().replace("\n", " ").replace(";", ",") : "",
                        h.getTemperaturaIngreso() != null ? h.getTemperaturaIngreso() : "",
                        h.getFrecCardiacaIngreso() != null ? h.getFrecCardiacaIngreso() : "",
                        h.getFrecRespiratoriaIngreso() != null ? h.getFrecRespiratoriaIngreso() : ""
                );
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
