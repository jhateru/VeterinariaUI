package society.controller.principales;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import society.dao.ServicioDao;
import society.modell.administracion.Servicio;

import java.util.List;

public class ServiciosController {
    
    @FXML private Label lblServiciosActivos;
    @FXML private Label lblMostrando;
    
    @FXML private TableView<Servicio> tablaServicios;
    @FXML private TableColumn<Servicio, String> colServicio;
    @FXML private TableColumn<Servicio, String> colCategoria;
    @FXML private TableColumn<Servicio, Integer> colDuracion;
    @FXML private TableColumn<Servicio, Double> colPrecio;
    @FXML private TableColumn<Servicio, String> colEstado;
    @FXML private TableColumn<Servicio, Void> colAcciones;

    private ServicioDao servicioDao;
    private ObservableList<Servicio> serviciosObservable;

    @FXML
    public void initialize() {
        servicioDao = new ServicioDao();
        cargarDatos();
    }

    private void cargarDatos() {
        List<Servicio> servicios = servicioDao.getAll();
        serviciosObservable = FXCollections.observableArrayList(servicios);
        tablaServicios.setItems(serviciosObservable);
        
        long activos = servicios.stream().filter(s -> "Activo".equals(s.getEstado())).count();
        lblServiciosActivos.setText(String.valueOf(activos));
        lblMostrando.setText("Mostrando 1 a " + servicios.size() + " de " + servicios.size() + " servicios");
    }
}
