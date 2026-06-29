package society.controller.principales;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Callback;
import society.dao.PersonalDao;
import society.modell.administracion.Personal;

public class PersonalController {

    @FXML
    private TextField searchField;

    @FXML
    private Label lblTotalPersonal;

    @FXML
    private Label lblEnTurno;

    @FXML
    private TableView<Personal> tablaPersonal;

    @FXML
    private TableColumn<Personal, Personal> colNombreCargo;

    @FXML
    private TableColumn<Personal, Personal> colDepartamento;

    @FXML
    private TableColumn<Personal, Personal> colEstado;

    @FXML
    private TableColumn<Personal, Personal> colContacto;

    private PersonalDao personalDao;

    @FXML
    public void initialize() {
        personalDao = new PersonalDao();
        
        ObservableList<Personal> personalList = FXCollections.observableArrayList(personalDao.getAll());
        
        lblTotalPersonal.setText(String.valueOf(personalList.size()));
        
        long enTurnoCount = personalList.stream().filter(p -> "En Turno".equalsIgnoreCase(p.getEstado())).count();
        lblEnTurno.setText(String.valueOf(enTurnoCount));

        configurarColumnas();
        
        tablaPersonal.setItems(personalList);
    }

    private void configurarColumnas() {
        colNombreCargo.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(param.getValue()));
        colNombreCargo.setCellFactory(new Callback<TableColumn<Personal, Personal>, TableCell<Personal, Personal>>() {
            @Override
            public TableCell<Personal, Personal> call(TableColumn<Personal, Personal> param) {
                return new TableCell<Personal, Personal>() {
                    @Override
                    protected void updateItem(Personal personal, boolean empty) {
                        super.updateItem(personal, empty);
                        if (empty || personal == null) {
                            setGraphic(null);
                        } else {
                            VBox vbox = new VBox(2);
                            Label nameLabel = new Label(personal.getNombre());
                            nameLabel.getStyleClass().add("cell-name");
                            Label cargoLabel = new Label(personal.getCargo());
                            cargoLabel.getStyleClass().add("cell-subtitle");
                            vbox.getChildren().addAll(nameLabel, cargoLabel);
                            setGraphic(vbox);
                        }
                    }
                };
            }
        });

        colDepartamento.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(param.getValue()));
        colDepartamento.setCellFactory(new Callback<TableColumn<Personal, Personal>, TableCell<Personal, Personal>>() {
            @Override
            public TableCell<Personal, Personal> call(TableColumn<Personal, Personal> param) {
                return new TableCell<Personal, Personal>() {
                    @Override
                    protected void updateItem(Personal personal, boolean empty) {
                        super.updateItem(personal, empty);
                        if (empty || personal == null) {
                            setGraphic(null);
                        } else {
                            Label deptLabel = new Label(personal.getDepartamento());
                            String badgeClass = "department-badge";
                            if (personal.getDepartamento().equalsIgnoreCase("Administración") || personal.getDepartamento().equalsIgnoreCase("Administracion")) {
                                badgeClass += "-admin";
                            } else if (personal.getDepartamento().equalsIgnoreCase("Laboratorio")) {
                                badgeClass += "-lab";
                            } else if (personal.getDepartamento().equalsIgnoreCase("Clinica") || personal.getDepartamento().equalsIgnoreCase("Clínica")) {
                                badgeClass += "-clinica";
                            }
                            deptLabel.getStyleClass().addAll("department-badge", badgeClass);
                            setGraphic(deptLabel);
                        }
                    }
                };
            }
        });

        colEstado.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(param.getValue()));
        colEstado.setCellFactory(new Callback<TableColumn<Personal, Personal>, TableCell<Personal, Personal>>() {
            @Override
            public TableCell<Personal, Personal> call(TableColumn<Personal, Personal> param) {
                return new TableCell<Personal, Personal>() {
                    @Override
                    protected void updateItem(Personal personal, boolean empty) {
                        super.updateItem(personal, empty);
                        if (empty || personal == null) {
                            setGraphic(null);
                        } else {
                            javafx.scene.layout.HBox hbox = new javafx.scene.layout.HBox(5);
                            hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                            Circle circle = new Circle(4);
                            if ("En Turno".equalsIgnoreCase(personal.getEstado())) {
                                circle.getStyleClass().add("status-en-turno");
                            } else if ("Libre".equalsIgnoreCase(personal.getEstado())) {
                                circle.getStyleClass().add("status-libre");
                            } else {
                                circle.getStyleClass().add("status-vacaciones");
                            }
                            Label statusLabel = new Label(personal.getEstado());
                            statusLabel.getStyleClass().add("cell-name");
                            hbox.getChildren().addAll(circle, statusLabel);
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });

        colContacto.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(param.getValue()));
        colContacto.setCellFactory(new Callback<TableColumn<Personal, Personal>, TableCell<Personal, Personal>>() {
            @Override
            public TableCell<Personal, Personal> call(TableColumn<Personal, Personal> param) {
                return new TableCell<Personal, Personal>() {
                    @Override
                    protected void updateItem(Personal personal, boolean empty) {
                        super.updateItem(personal, empty);
                        if (empty || personal == null) {
                            setGraphic(null);
                        } else {
                            VBox vbox = new VBox(2);
                            Label emailLabel = new Label(personal.getEmail());
                            emailLabel.getStyleClass().add("cell-text");
                            Label phoneLabel = new Label(personal.getTelefono());
                            phoneLabel.getStyleClass().add("cell-subtitle");
                            vbox.getChildren().addAll(emailLabel, phoneLabel);
                            setGraphic(vbox);
                        }
                    }
                };
            }
        });
    }
}
