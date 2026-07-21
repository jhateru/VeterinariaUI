package society.dao;

import java.util.List;
import com.google.gson.reflect.TypeToken;

import society.modell.reportes.Reportes;

public class ReportesDao extends MasterJsonDao<Reportes> {
    public ReportesDao() {
        super("reportes", new TypeToken<List<Reportes>>(){}.getType());
    }

    

    
}
