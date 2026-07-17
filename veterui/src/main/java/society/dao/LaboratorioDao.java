package society.dao;

import java.util.List;
import com.google.gson.reflect.TypeToken;

import society.modell.areamedica.Laboratorio;
import society.modell.recepcion.Paciente;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class LaboratorioDao extends JsonDao<Laboratorio> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final PacienteDao pacienteDao;

    public LaboratorioDao() {
        super("laboratorios.json", new TypeToken<List<Laboratorio>>(){}.getType());
        this.pacienteDao = new PacienteDao();
    }

    

    
}
