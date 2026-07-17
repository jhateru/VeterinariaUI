package society.dao;

import java.util.List;
import com.google.gson.reflect.TypeToken;

import society.modell.reportes.Reportes;

public class ReportesDao extends JsonDao<Reportes> {
    public ReportesDao() {
        super("reportes.json", new TypeToken<List<Reportes>>(){}.getType());
    }

    

    
}
