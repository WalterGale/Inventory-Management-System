package wg.inventorymanagement.Controller;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node;

import wg.inventorymanagement.Main;
import wg.inventorymanagement.Model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This is the controller class responsible for providing the control logic for the main screen of the app.
 * It also contains a few methods which are used on the connecting pages.
 */
public class MainScreenController implements Initializable {

    private  Stage stage;
    private Parent scene;

    private static Part partToBeModified;

    private static Product productToBeModified;

    private static Part selectedPart;

    // TextFields for search
    @FXML
    private TextField partSearch;

    @FXML
    private TextField productSearch;


    // Part View TableView
    @FXML
    private TableView<Part> partsTableView;

    @FXML
    private TableColumn<Part, Integer> partInventoryCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Double> partPricePerUnitCol;

    @FXML
    private TableColumn<Part, Integer> partsIdCol;


    // Product View TableView
    @FXML
    private TableView<Product> productTableView;

    @FXML
    private TableColumn<Product, Integer> productIdCol;

    @FXML
    private TableColumn<Product, Integer> productInventoryCol;

    @FXML
    private TableColumn<Product, String> productNameCol;

    @FXML
    private TableColumn<Product, Double> productPricePerUnitCol;

    // Methods
    // Confirmation Prompts
    /**
     * confirmation prompt with 2 buttons. if OK is pressed the action is confirmed. if CANCEL is pressed the action is cancelled.
     *
     * @param title takes in a title from the declaration.
     * @param message takes in the messagge from the declaration.
     *  */
    static boolean confirmationPrompt(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText("Confirmation");
        alert.setContentText(message);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        }
        else {
            return false;
        }
    }
    /**
     * a warning prompt with a showAndWait().
     *
     * @param header takes in the header from the declaration.
     * @param message takes in the message from the declaration.
     * */
    static void warningPrompt(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * There was a recurring issue with the MainScreenController not being able to correctly reference the other .fxml files for connecting pages.
     *
     * Initially I was using getClass().getResource() as the arguments passed into FXMLLoader.load() but realised that the issue was caused by a post 2021 build of
     * Intellij. Due to a change in how Intellij references the resources folder it was pointing to the incorrect location for the resources package.
     * I was able to resolve this after consultation and changed getClass().getResource() into Main.class.getResources.
     *
     * @param event the method will be called upon successfully adding or modifying, either a product or an object.
     * */
    static void taskComplete (ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Main.class.getResource("View/MainScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    /**
     * This method will prompt the user for confirmation of cancelling the current tasks and once confirmed it will return the user to the Main Screen.
     *
     * @param event the user clicks on the Exit button.
     * */
    static boolean returnToMainScreen(ActionEvent event) throws IOException {
        boolean confirmed = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel current task?");
        alert.setHeaderText(null);
        alert.setContentText("Would you like to cancel and return to the Main Screen?");
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            confirmed = true;
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(Main.class.getResource("View/MainScreen.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        return confirmed;
    }

    // OnAction group
    /**
     * This action will switch the user to the add part section of the app.
     *
     * @param event the user clicks on the Add button.
     * */
    @FXML
    void onActionSwitchToAddPart(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/AddPartScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This action will switch the user to the modify part section of the app and populate the text fields with the data using the setPart method
     * Features a warning prompt if no part is selected.
     *
     * @param event the user clicks on the Modify button.
     * */
    @FXML
    void onActionSwitchToModifyPart(ActionEvent event) {
        if (partsTableView.getSelectionModel().isEmpty()){
            warningPrompt("No Product Selected", "You must select a product to be modified.");
        }
        try {
            Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();

            if (selectedPart == null) {
                return;
            }
            else {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/ModifyPartScreen.fxml"));
                scene = fxmlLoader.load();
                ModifyPartController modifyPartController = fxmlLoader.getController();
                modifyPartController.setPart(selectedPart);
                //Scene scene = new Scene();
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }

        catch (IOException ignored) {
        }
    }
    /**
     * The action will prompt the user for confirmation and delete the selected part once confirmed.
     * Features a warning message if no part has been selected.
     * FUTURE IMPROVEMENT: Check if the part being deleted has a product associated to it and return a list of products that it is associated with.
     *
     * @param event the user clicks on the Delete button.
     * */
    @FXML
    void onActionDeletePart(ActionEvent event){
        if (partsTableView.getSelectionModel().isEmpty()){
            warningPrompt( "No Part selected", "You must select a part to delete.");
            return;
        }
        if (confirmationPrompt("Confirm?", "Are you sure you would like to delete this product?")) {
            int selectedPart = partsTableView.getSelectionModel().getSelectedIndex();
            partsTableView.getItems().remove(selectedPart);
        }
    }

    /**
     * This action will switch the user to the add product section of the app.
     *
     * @param event the user clicks on the Add button.
     * */
    @FXML
    void onActionSwitchToAddProduct(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/AddProductScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This action will switch the user to the modify product section of the app and populate the text fields with the data using the setProduct method
     * Features a warning prompt if no product is selected.
     *
     * @param event the user clicks on the Modify button.
     * */
    @FXML
    void onActionSwitchToModifyProduct(ActionEvent event) throws IOException {
        if (productTableView.getSelectionModel().isEmpty()){
            warningPrompt("No Product Selected", "You must select a product to be modified.");
        }
        try {
            Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();

            if (selectedProduct == null) {
                return;
            } else {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/ModifyProductScreen.fxml"));
                scene = fxmlLoader.load();
                ModifyProductController modifyProductController = fxmlLoader.getController();
                modifyProductController.setProduct(selectedProduct);
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch (IOException ignored) {
        }
    }
    /**
     *The action will prompt the user for confirmation and delete the selected product once confirmed.
     * Features a warning message if no product has been selected.
     * Features a warning message if the product to be deleted has associated parts.
     * RUNTIME ERROR: if associatedParts is declared before the first if() statement it will cause an error when pressing the delete button with no item selected. Having ti be declared after the first if() statement resolves the issue entirely.
     *
     * @param event the user clicks on the Delete button.
     * */
    @FXML
    void onActionDeleteProduct(ActionEvent event){
        Product selectedProductParts = productTableView.getSelectionModel().getSelectedItem();

        if (productTableView.getSelectionModel().isEmpty()){
            warningPrompt("No Product selected", "You must select a product to delete.");
            return;
        }

        ObservableList<Part> associatedParts = selectedProductParts.getAllAssociatedParts();
        if (associatedParts.size() >= 1) {
            warningPrompt("Cannot proceed", "This Product has associated Parts that must be removed before deletion.");
            return;
        }
        if (confirmationPrompt("Confirm?", "Are you sure you would like to delete this product?")) {
            int selectedProduct = productTableView.getSelectionModel().getSelectedIndex();
            productTableView.getItems().remove(selectedProduct);
        }
    }
    /**
     * This action will prompt the user for confirmation and close the application upon confirmation.
     *
     * @param event the user clicks on the Exit button.
     * */
    @FXML
    void onActionCloseApplication(ActionEvent event) throws IOException {
        confirmationPrompt("Exit application", "Would you like to close this application?");
        System.exit(0);
    }

    /**
     * This initializes the MainScreen controller class.
     * It implements a listener on the partSearch text field using a lambda function to allow filtering of results without requiring a button press.
     * It implements a listener on the productSearch text field using a lambda function to allow filtering of results without requiring a button press.
     *
     * RUNTIME ERROR: kept getting an error and shortly after a crash while trying to get the search box to filter the partsTableView and productsTableView, the search would create repeated instances of the searched objects in the table.
     * RUNTIME ERROR CONT: implemented a lambda expression which returns the searchPart results into the table View correctly without duplicates or a need for a button.
     *
     * FUTURE IMPROVEMENT: allow for the search parameters to return values in either lower or upper case, currently it only returns results that match the casing directly.
     *
     * @param location
     * @param resources
     * */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // tried a few ways to implement a searchable text field, but it would return a large number of instances the searched product until it caused a crash
        // implemented a lambda expression which resolved this by creating a new instance of a part and passing it directly into the method
        partSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            FilteredList searchPartResult = Inventory.getAllParts().filtered((part) -> part.getName().contains(newValue) ||
                    Integer.toString(part.getId()).contains(newValue));
            partsTableView.setItems(searchPartResult);
            }
        );

        productSearch.textProperty().addListener((observable, oldValue,newValue) -> {
            FilteredList searchProductResult = Inventory.getAllProducts().filtered((product) -> product.getName().contains(newValue) ||
                    Integer.toString(product.getId()).contains(newValue));
            productTableView.setItems(searchProductResult);
            }
        );

        partsTableView.setItems(Inventory.getAllParts());
        partsIdCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partInventoryCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPricePerUnitCol.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        productTableView.setItems(Inventory.getAllProducts());
        productIdCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productInventoryCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        productPricePerUnitCol.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
    }
}