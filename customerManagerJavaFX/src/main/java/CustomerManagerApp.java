import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomerManagerApp extends Application {

    // ObservableList holds customer records in memory and updates the UI automatically
    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Customer Manager");

        // 1. Input Controls
        Label nameLabel = new Label("Customer name:");
        TextField nameField = new TextField();
        nameField.setPromptText("e.g., Mary Banda");
        nameLabel.setLabelFor(nameField);

        Label provinceLabel = new Label("Province:");
        ComboBox<String> provinceBox = new ComboBox<>();
        provinceBox.getItems().addAll("Central", "Copperbelt", "Eastern", "Luapula", "Lusaka", "Muchinga", "Northern", "North-Western", "Southern", "Western");
        provinceBox.setPromptText("Choose a province");

        Button saveButton = new Button("Save customer");
        saveButton.setDefaultButton(true); // Enter key triggers save

        Button deleteButton = new Button("Delete selected"); // Delete action control

        Label statusLabel = new Label(); // User feedback

        // 2. Table Controls
        TableView<Customer> table = new TableView<>();
        table.setItems(customerList);

        TableColumn<Customer, String> nameCol = new TableColumn<>("Customer name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Customer, String> provinceCol = new TableColumn<>("Province");
        provinceCol.setCellValueFactory(new PropertyValueFactory<>("province"));

        table.getColumns().addAll(nameCol, provinceCol);

        // 3. Validation & Save Action
        saveButton.setOnAction(event -> {
            String name = nameField.getText().trim();
            String province = provinceBox.getValue();

            if (name.isEmpty()) {
                statusLabel.setText("Enter the customer name.");
                nameField.requestFocus();
                return;
            }

            if (province == null) {
                statusLabel.setText("Choose a province.");
                provinceBox.requestFocus();
                return;
            }

            customerList.add(new Customer(name, province));
            statusLabel.setText("Customer saved.");

            nameField.clear();
            provinceBox.setValue(null);
            nameField.requestFocus();
        });

        // 4. Delete Action with Confirmation Alert
        deleteButton.setOnAction(event -> {
            Customer selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                statusLabel.setText("Select a customer from table to delete.");
                return;
            }

            ButtonType deleteBtnType = new ButtonType("Delete");
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Delete the selected customer?", deleteBtnType, ButtonType.CANCEL);
            confirmAlert.setHeaderText("Confirm deletion");

            if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) == deleteBtnType) {
                customerList.remove(selected);
                statusLabel.setText("Customer deleted.");
            }
        });

        // 5. Layout Setup
        HBox buttonBox = new HBox(10, saveButton, deleteButton);
        VBox formLayout = new VBox(10, nameLabel, nameField, provinceLabel, provinceBox, buttonBox, statusLabel);
        formLayout.setPadding(new Insets(10));

        VBox mainLayout = new VBox(15, formLayout, table);
        mainLayout.setPadding(new Insets(15));

        Scene scene = new Scene(mainLayout, 450, 520);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}