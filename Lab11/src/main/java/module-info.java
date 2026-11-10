module kz.atu.lab11 {
    requires javafx.controls;
    requires javafx.fxml;

    opens kz.atu.lab11 to javafx.fxml;
    opens kz.atu.lab11.controller to javafx.fxml;
    opens kz.atu.lab11.model to javafx.base;

    exports kz.atu.lab11;
}
