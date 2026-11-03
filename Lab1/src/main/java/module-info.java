module kz.atu.lab1 {
    requires javafx.controls;
    requires javafx.fxml;

    opens kz.atu.lab1 to javafx.fxml;
    exports kz.atu.lab1;
}
