package society.controller.principales;

import javafx.fxml.FXML;

public class SideBarController {

    @FXML
    public void initialize() {
    }

    @FXML
    private void onRecepcionClick() {
        MainViewController.getInstance().cargarVista("RecepcionView");
    }

    @FXML
    private void onAreaMedicaClick() {
        MainViewController.getInstance().cargarVista("AreaMedicaView");
    }

    @FXML
    private void onAdministracionClick() {
        MainViewController.getInstance().cargarVista("AdministracionView");
    }

    @FXML
    private void onDashboardClick() {
        MainViewController.getInstance().cargarVista("DashboardResumen");
    }

    @FXML
    private void onPacientesClick() {
        MainViewController.getInstance().cargarVista("PacientesView");
    }

    @FXML
    private void onCitasClick() {
        MainViewController.getInstance().cargarVista("CitasView");
    }

    @FXML
    private void onDuenosClick() {
        MainViewController.getInstance().cargarVista("DuenosView");
    }

    @FXML
    private void onHistoriaClinicaClick() {
        MainViewController.getInstance().cargarVista("HistoriaClinicaView");
    }

    @FXML
    private void onFacturacionClick() {
        MainViewController.getInstance().cargarVista("FacturacionView");
    }

    @FXML
    private void onInventarioClick() {
        MainViewController.getInstance().cargarVista("InventarioView");
    }

    @FXML
    private void onHospitalizacionClick() {
        MainViewController.getInstance().cargarVista("HospitalizacionView");
    }

    @FXML
    private void onLaboratorioClick() {
        MainViewController.getInstance().cargarVista("LaboratorioView");
    }

    @FXML
    private void onServiciosClick() {
        MainViewController.getInstance().cargarVista("ServiciosView");
    }

    @FXML
    private void onProveedoresClick() {
        MainViewController.getInstance().cargarVista("ProveedoresView");
    }

    @FXML
    private void onPersonalClick() {
        MainViewController.getInstance().cargarVista("PersonalView");
    }

    @FXML
    private void onReportesClick() {
        MainViewController.getInstance().cargarVista("ReportesView");
    }

    @FXML
    private void onConfiguracionClick() {
        MainViewController.getInstance().cargarVista("ConfiguracionView");
    }
}
