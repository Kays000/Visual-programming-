package kz.atu.lab1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

public class HelloController {

    @FXML
    private TextField txtProduct; // Поле ввода товара

    @FXML
    private TextField txtPrice; // Поле ввода цены

    @FXML
    private Spinner<Integer> spnQuantity; // Спиннер для количества

    @FXML
    private Button btnCalculate; // Кнопка расчёта

    @FXML
    private Label lblResult; // Метка вывода результата

    @FXML
    public void initialize() {
        // Инициализируем Spinner значениями от 1 до 100, шаг 1, дефолтное значение 1
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        spnQuantity.setValueFactory(valueFactory);
    }

    @FXML
    protected void onCalculateClick() {
        String product = txtProduct.getText().trim();
        String priceStr = txtPrice.getText().trim();

        // 1. Проверка на пустые поля
        if (product.isEmpty()) {
            lblResult.setText("Ошибка: Введите название товара!");
            return;
        }
        if (priceStr.isEmpty()) {
            lblResult.setText("Ошибка: Введите цену!");
            return;
        }

        try {
            // 2. Парсинг цены и проверка на корректность числовых данных
            double price = Double.parseDouble(priceStr);
            if (price < 0) {
                lblResult.setText("Ошибка: Цена не может быть отрицательной!");
                return;
            }

            // 3. Получение количества из Spinner
            int quantity = spnQuantity.getValue();

            // 4. Расчёт стоимости
            double totalCost = price * quantity;

            // 5. Вывод итогового результата
            lblResult.setText(String.format(
                    "Товар: %s\nКоличество: %d шт.\nИтого к оплате: %.2f тг.",
                    product, quantity, totalCost
            ));

        } catch (NumberFormatException e) {
            lblResult.setText("Ошибка: Некорректный формат цены (вводите числа)!");
        }
    }
}
