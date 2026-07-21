package society.dao;

import java.util.List;
import com.google.gson.reflect.TypeToken;

import society.modell.facturacion.Facturacion;
import society.modell.facturacion.Facturacion.EstadoFactura;
import society.modell.facturacion.DetalleFacturacion;
import society.modell.facturacion.CatalogoItem;

public class FacturacionDao extends MasterJsonDao<Facturacion> {

    public FacturacionDao() {
        super("facturacion", new TypeToken<List<Facturacion>>(){}.getType());
    }

    private String safe(String val) {
        if (val == null) return "";
        return val.replace(",", ";").replace("\n", " ");
    }

    

    
}
