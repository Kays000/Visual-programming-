package com.example.lab4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class HelloController {

    @FXML private BorderPane rootPane;
    @FXML private VBox navigationBox;

    @FXML private TextField txtModel;
    @FXML private TextField txtLicensePlate;
    @FXML private ComboBox<String> cmbYear;
    @FXML private ChoiceBox<String> chbFuelType;
    @FXML private TextArea txtNote;

    @FXML private TableView<Car> tblCars;
    @FXML private TableColumn<Car, String> colModel;
    @FXML private TableColumn<Car, String> colPlate;
    @FXML private TableColumn<Car, String> colYear;
    @FXML private TableColumn<Car, String> colFuel;

    @FXML private Label lblStatus;

    private final ObservableList<Car> carList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cmbYear.getItems().addAll("2026", "2025", "2024", "2023", "2022", "2021", "2020");
        cmbYear.getSelectionModel().selectFirst();

        chbFuelType.getItems().addAll("Бензин", "Дизель", "Электричество", "Гибрид");
        chbFuelType.getSelectionModel().selectFirst();

        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colPlate.setCellValueFactory(new PropertyValueFactory<>("licensePlate"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colFuel.setCellValueFactory(new PropertyValueFactory<>("fuelType"));

        tblCars.setItems(carList);
    }

    @FXML
    private void onSaveClick() {
        String model = txtModel.getText().trim();
        String plate = txtLicensePlate.getText().trim();
        String year = cmbYear.getValue();
        String fuel = chbFuelType.getValue();
        String note = txtNote.getText().trim();

        if (model.isBlank() || plate.isBlank()) {
            lblStatus.setText("Статус: заполните Марку/Модель и Гос. номер!");
            return;
        }

        Car car = new Car(model, plate, year, fuel, note);
        carList.add(car);

        lblStatus.setText("Статус: автомобиль " + model + " [" + plate + "] добавлен.");
        onClearClick();
    }

    @FXML
    private void onClearClick() {
        txtModel.clear();
        txtLicensePlate.clear();
        cmbYear.getSelectionModel().selectFirst();
        chbFuelType.getSelectionModel().selectFirst();
        txtNote.clear();
        if (lblStatus.getText().contains("заполните")) {
            lblStatus.setText("Статус: форма очищена");
        }
    }

    @FXML
    private void onToggleNavigation() {
        if (rootPane.getLeft() != null) {
            rootPane.setLeft(null);
            lblStatus.setText("Статус: панель навигации скрыта");
        } else {
            rootPane.setLeft(navigationBox);
            lblStatus.setText("Статус: панель навигации отображена");
        }
    }
}
