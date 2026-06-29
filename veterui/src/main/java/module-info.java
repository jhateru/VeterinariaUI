module society {
    requires javafx.controls;
    requires javafx.fxml;

    opens society to javafx.fxml;
    exports society;

    opens society.controller.principales to javafx.fxml;
    exports society.controller.principales;

    opens society.controller.reutilizables to javafx.fxml;
    exports society.controller.reutilizables;

    opens society.modell.inventario to javafx.base, javafx.fxml;
    exports society.modell.inventario;

    opens society.modell.administracion to javafx.base, javafx.fxml;
    exports society.modell.administracion;

    opens society.modell.configuracion to javafx.base, javafx.fxml;
    exports society.modell.configuracion;
}
