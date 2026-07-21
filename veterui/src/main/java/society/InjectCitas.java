package society;

import society.dao.CitaDao;
import society.modell.recepcion.Cita;
import java.time.LocalDateTime;

public class InjectCitas {
    public static void main(String[] args) {
        CitaDao citaDao = new CitaDao();
        
        // 1. Cita para hoy, ya completada
        Cita c1 = new Cita();
        c1.setPacienteId(1); // Toby
        c1.setVeterinarioId(3); // Dra. Martínez
        c1.setFechaHora(LocalDateTime.now().minusHours(2));
        c1.setMotivo("Vacunación");
        c1.setEstado(Cita.EstadoCita.COMPLETADA);
        
        // 2. Cita para hoy, pendiente
        Cita c2 = new Cita();
        c2.setPacienteId(2); // Luna
        c2.setVeterinarioId(4); // Dr. Gómez
        c2.setFechaHora(LocalDateTime.now().plusHours(1));
        c2.setMotivo("Control");
        c2.setEstado(Cita.EstadoCita.PENDIENTE);
        
        // 3. Cita para hoy, urgencia
        Cita c3 = new Cita();
        c3.setPacienteId(3); // Max
        c3.setVeterinarioId(3); // Dra. Martínez
        c3.setFechaHora(LocalDateTime.now().plusHours(3));
        c3.setMotivo("Urgencia");
        c3.setEstado(Cita.EstadoCita.URGENCIA);
        
        // 4. Cita para mañana, pendiente
        Cita c4 = new Cita();
        c4.setPacienteId(1); // Toby
        c4.setVeterinarioId(4); // Dr. Gómez
        c4.setFechaHora(LocalDateTime.now().plusDays(1).plusHours(2));
        c4.setMotivo("Cirugía");
        c4.setEstado(Cita.EstadoCita.PENDIENTE);
        
        // 5. Cita para pasado mañana, pendiente
        Cita c5 = new Cita();
        c5.setPacienteId(2); // Luna
        c5.setVeterinarioId(3); // Dra. Martínez
        c5.setFechaHora(LocalDateTime.now().plusDays(2).minusHours(1));
        c5.setMotivo("Peluquería");
        c5.setEstado(Cita.EstadoCita.PENDIENTE);
        
        citaDao.save(c1);
        citaDao.save(c2);
        citaDao.save(c3);
        citaDao.save(c4);
        citaDao.save(c5);
        
        System.out.println("Citas inyectadas con éxito.");
    }
}
