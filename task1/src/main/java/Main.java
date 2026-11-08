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
import javafx.util.StringConverter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.time.format.DateTimeFormatter;

public class Main extends Application {
    private static ResourceBundle bundle;
    private static Locale currentLocale = new Locale("en", "US");

    static {
        bundle = ResourceBundle.getBundle("MessagesBundle", currentLocale);
    }

    public static double convertTemperature(double value, TemperatureUnit fromUnit, TemperatureUnit toUnit) {
        if (fromUnit.getId() == toUnit.getId()) {
            return value;
        }
        String fromSymbol = fromUnit.getSymbol();
        String toSymbol = toUnit.getSymbol();

        double celsiusValue;
        switch (fromSymbol) {
            case "C":
                celsiusValue = value;
                break;
            case "F":
                celsiusValue = TemperatureConverter.fahrenheitToCelsius(value);
                break;
            case "K":
                celsiusValue = TemperatureConverter.kelvinToCelsius(value);
                break;
            default:
                throw new IllegalArgumentException("Invalid temperature units");
        }
        switch (toSymbol) {
            case "C":
                return celsiusValue;
            case "F":
                return TemperatureConverter.celsiusToFahrenheit(celsiusValue);
            case "K":
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
        Label langLabel = new Label("Language / Kieli / Språk / 言語 / اللغة:");
        ComboBox<String> langComboBox = new ComboBox<>();
        langComboBox.getItems().addAll(
                "English (en_US)",
                "Finnish (fi_FI)",
                "Swedish (sv_SE)",
                "Japanese (ja_JP)",
                "Arabic (ar_SA)"
        );

        if (currentLocale.getLanguage().equals("fi")) langComboBox.setValue("Finnish (fi_FI)");
        else if (currentLocale.getLanguage().equals("sv")) langComboBox.setValue("Swedish (sv_SE)");
        else if (currentLocale.getLanguage().equals("ja")) langComboBox.setValue("Japanese (ja_JP)");
        else if (currentLocale.getLanguage().equals("ar")) langComboBox.setValue("Arabic (ar_SA)");
        else langComboBox.setValue("English (en_US)");

        langComboBox.setOnAction(e -> {
            String selected = langComboBox.getValue();
            if (selected.contains("Finnish")) currentLocale = new Locale("fi", "FI");
            else if (selected.contains("Swedish")) currentLocale = new Locale("sv", "SE");
            else if (selected.contains("Japanese")) currentLocale = new Locale("ja", "JP");
            else if (selected.contains("Arabic")) currentLocale = new Locale("ar", "SA");
            else currentLocale = new Locale("en", "US");

            bundle = ResourceBundle.getBundle("MessagesBundle", currentLocale);
            showUI(stage);
        });

        TemperatureUnitDAO unitDAO = new TemperatureUnitDAO();
        TemperatureRecordDAO recordDAO = new TemperatureRecordDAO();

        String langCode = currentLocale.getLanguage();
        List<TemperatureUnit> availableUnits = unitDAO.findAllByLang(langCode);

        Label unit1Label = new Label(bundle.getString("label.unit1"));
        ComboBox<TemperatureUnit> unit1ComboBox = new ComboBox<>();
        unit1ComboBox.getItems().addAll(availableUnits);
        if (!availableUnits.isEmpty()) unit1ComboBox.setValue(availableUnits.get(0));
        unit1ComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(TemperatureUnit unit) {
                return unit != null ? unit.getName() + " (" + unit.getSymbol() + ")" : "";
            }
            @Override
            public TemperatureUnit fromString(String string) { return null; }
        });

        Label unit2Label = new Label(bundle.getString("label.unit2"));
        ComboBox<TemperatureUnit> unit2ComboBox = new ComboBox<>();
        unit2ComboBox.getItems().addAll(availableUnits);
        if (availableUnits.size() > 1) unit2ComboBox.setValue(availableUnits.get(1));
        else if (!availableUnits.isEmpty()) unit2ComboBox.setValue(availableUnits.get(0));
        unit2ComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(TemperatureUnit unit) {
                return unit != null ? unit.getName() + " (" + unit.getSymbol() + ")" : "";
            }
            @Override
            public TemperatureUnit fromString(String string) { return null; }
        });

        Label valueLabel = new Label(bundle.getString("label.value"));
        TextField valueTextField = new TextField();

        Button convertButton = new Button(bundle.getString("btn.convert"));
        Label resultLabel = new Label();

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
                TemperatureUnit source = unit1ComboBox.getValue();
                TemperatureUnit target = unit2ComboBox.getValue();

                double result = convertTemperature(value, source, target);
                result = Math.round(result * 100.0) / 100.0;

                if (source != null && target != null) {
                    TemperatureRecord record = new TemperatureRecord(source.getId(), target.getId(), value, result);
                    recordDAO.save(record);
                    historyTable.getItems().clear();
                    historyTable.getItems().addAll(recordDAO.findAll());
                }

                resultLabel.setText(String.format("%.2f %s -> %.2f %s", value, source.getSymbol(), result, target.getSymbol()));

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