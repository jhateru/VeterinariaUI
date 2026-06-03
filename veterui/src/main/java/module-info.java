module society {
    requires javafx.controls;
    requires javafx.fxml;

    opens society to javafx.fxml;
    exports society;
}
