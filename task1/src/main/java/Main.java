import dao.TemperatureRecordDAO;
import dao.TemperatureUnitDAO;
import model.TemperatureRecord;
import model.TemperatureUnit;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.time.format.DateTimeFormatter;

public class Main extends Application {
    private static ResourceBundle bundle;
    private static Locale currentLocale = new Locale("en", "US");

    static {
        bundle = ResourceBundle.getBundle("MessagesBundle", currentLocale);
    }

    public static double convertTemperature(double value, String fromUnit, String toUnit) {
        if (fromUnit.equals(toUnit)) {
            return value;
        }
        // Переводим исходное значение в Celsius как базовую единицу
        double celsiusValue;
        switch (fromUnit) {
            case "Celsius":
                celsiusValue = value;
                break;
            case "Fahrenheit":
                celsiusValue = TemperatureConverter.fahrenheitToCelsius(value);
                break;
            case "Kelvin":
                celsiusValue = TemperatureConverter.kelvinToCelsius(value);
                break;
            default:
                throw new IllegalArgumentException("Invalid temperature units");
        }
        switch (toUnit) {
            case "Celsius":
                return celsiusValue;
            case "Fahrenheit":
                return TemperatureConverter.celsiusToFahrenheit(celsiusValue);
            case "Kelvin":
                return celsiusValue + 273.15;
            default:
                throw new IllegalArgumentException("Invalid temperature units");
        }
    }

    @Override
    public void start(Stage stage) {
        showUI(stage);
    }

    private void showUI(Stage stage) {
        // Выпадающий список выбора языка прямо в GUI
        Label langLabel = new Label("Language / Kieli / Språk / 言語 / اللغة:");
        ComboBox<String> langComboBox = new ComboBox<>();
        langComboBox.getItems().addAll(
                "English (en_US)",
                "Finnish (fi_FI)",
                "Swedish (sv_SE)",
                "Japanese (ja_JP)",
                "Arabic (ar_SA)"
        );

        // Устанавливаем текущее значение в комбобоксе в соответствии с локалью
        if (currentLocale.getLanguage().equals("fi")) langComboBox.setValue("Finnish (fi_FI)");
        else if (currentLocale.getLanguage().equals("sv")) langComboBox.setValue("Swedish (sv_SE)");
        else if (currentLocale.getLanguage().equals("ja")) langComboBox.setValue("Japanese (ja_JP)");
        else if (currentLocale.getLanguage().equals("ar")) langComboBox.setValue("Arabic (ar_SA)");
        else langComboBox.setValue("English (en_US)");

        // Обработка смены языка «на лету»
        langComboBox.setOnAction(e -> {
            String selected = langComboBox.getValue();
            if (selected.contains("Finnish")) currentLocale = new Locale("fi", "FI");
            else if (selected.contains("Swedish")) currentLocale = new Locale("sv", "SE");
            else if (selected.contains("Japanese")) currentLocale = new Locale("ja", "JP");
            else if (selected.contains("Arabic")) currentLocale = new Locale("ar", "SA");
            else currentLocale = new Locale("en", "US");

            bundle = ResourceBundle.getBundle("MessagesBundle", currentLocale);
            showUI(stage); // Перерисовываем сцену на новом языке
        });

        Label unit1Label = new Label(bundle.getString("label.unit1"));
        ComboBox<String> unit1ComboBox = new ComboBox<>();
        unit1ComboBox.getItems().addAll("Celsius", "Fahrenheit", "Kelvin");
        unit1ComboBox.setValue("Celsius");

        Label unit2Label = new Label(bundle.getString("label.unit2"));
        ComboBox<String> unit2ComboBox = new ComboBox<>();
        unit2ComboBox.getItems().addAll("Celsius", "Fahrenheit", "Kelvin");
        unit2ComboBox.setValue("Fahrenheit");

        Label valueLabel = new Label(bundle.getString("label.value"));
        TextField valueTextField = new TextField();

        Button convertButton = new Button(bundle.getString("btn.convert"));
        Label resultLabel = new Label();

        TemperatureUnitDAO unitDAO = new TemperatureUnitDAO();
        TemperatureRecordDAO recordDAO = new TemperatureRecordDAO();

        TableView<TemperatureRecord> historyTable = new TableView<>();
        historyTable.setPrefHeight(250);

        TableColumn<TemperatureRecord, Double> inputColumn = new TableColumn<>(bundle.getString("table.input"));
        inputColumn.setCellValueFactory(new PropertyValueFactory<>("originalValue"));

        TableColumn<TemperatureRecord, Double> resultColumn = new TableColumn<>(bundle.getString("table.result"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("convertedValue"));

        TableColumn<TemperatureRecord, LocalDateTime> dateColumn = new TableColumn<>(bundle.getString("table.date"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        dateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                }
            }
        });

        historyTable.getColumns().addAll(inputColumn, resultColumn, dateColumn);
        try {
            historyTable.getItems().addAll(recordDAO.findAll());
        } catch (Exception ignored) {}

        convertButton.setOnAction(e -> {
            try {
                double value = Double.parseDouble(valueTextField.getText());
                String fromUnit = unit1ComboBox.getValue();
                String toUnit = unit2ComboBox.getValue();

                double result = convertTemperature(value, fromUnit, toUnit);
                result = Math.round(result * 100.0) / 100.0; // Исправлено деление/умножение округления

                TemperatureUnit source = unitDAO.findByName(fromUnit);
                TemperatureUnit target = unitDAO.findByName(toUnit);

                if (source != null && target != null) {
                    TemperatureRecord record = new TemperatureRecord(source.getId(), target.getId(), value, result);
                    recordDAO.save(record);
                    historyTable.getItems().clear();
                    historyTable.getItems().addAll(recordDAO.findAll());
                }

                resultLabel.setText(String.format("%.2f -> %.2f", value, result));

            } catch (NumberFormatException ex) {
                resultLabel.setText(bundle.getString("error.invalid.input"));
            } catch (IllegalArgumentException ex) {
                resultLabel.setText(ex.getMessage());
            }
        });

        VBox root = new VBox(10,
                langLabel, langComboBox,
                new Separator(),
                unit1Label, unit1ComboBox,
                unit2Label, unit2ComboBox,
                valueLabel, valueTextField,
                convertButton,
                resultLabel,
                new Label(bundle.getString("label.history")),
                historyTable
        );
        root.setPadding(new Insets(20));

        // Поддержка направления RTL (справа налево) для арабского языка
        if (currentLocale.getLanguage().equals("ar")) {
            root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        } else {
            root.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }

        Scene scene = new Scene(root, 600, 550);
        stage.setTitle(bundle.getString("app.title"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}