package wg.inventorymanagement.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import wg.inventorymanagement.Main;
import wg.inventorymanagement.Model.Part;
import wg.inventorymanagement.Model.Product;
import wg.inventorymanagement.Model.Inventory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static wg.inventorymanagement.Model.Inventory.getAllParts;
import static wg.inventorymanagement.Model.Inventory.getAllProducts;

/**
 * This is the controller class responsible for providing the control logic for the add product screen of the app.
 */
public class AddProductController implements Initializable {

    private Stage stage;
    private Object scene;

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private ObservableList<Part> originalParts = FXCollections.observableArrayList();

    // Text fields group
    @FXML
    private TextField id;

    @FXML
    private TextField addProductName;

    @FXML
    private TextField addProductInventory;

    @FXML
    private TextField addProductPrice;

    @FXML
    private TextField addProductMaximum;

    @FXML
    private TextField addProductMinimum;

    @FXML
    private TextField partSearch;

    // Associated parts group Table View
    @FXML
    private TableView<Part> associatedPartsTableView;

    @FXML
    private TableColumn<Part, Integer> associatedPartIdCol;

    @FXML
    private TableColumn<Part, Integer> associatedProductInventoryCol;

    @FXML
    private TableColumn<Part, String> associatedProductNameCol;

    @FXML
    private TableColumn<Part, Double> associatedProductPricePerUnitCol;

    // Original parts group Table View
    @FXML
    private TableView<Part> partsTableView;

    @FXML
    private TableColumn<Part, Integer> productIdCol;

    @FXML
    private TableColumn<Part, Integer> productInventoryCol;

    @FXML
    private TableColumn<Part, String> productNameCol;

    @FXML
    private TableColumn<Part, Double> productPricePerUnitCol;

    // Method group
    /**
     * This method updates the partsTableView with all items from the inventory.
     * */
    public void updatePartsTable() {
        partsTableView.setItems(Inventory.getAllParts());
    }

    /**
     *This method updates the associatedProductsTableView with all associated parts.
     */
    public void updateAssociatedPartsTable() {
        associatedPartsTableView.setItems(associatedParts);
    }

    /**
     * This method is responsible for creating a newProductId based on the current size of getAllProducts()
     * it increments the newProductId to always be larger than the number of products in the inventory.
     */
    private int createNewProductId() {
        int newProductId = 101;
        boolean alreadyExists = true;

        while (alreadyExists) {
            alreadyExists = false;
            for (int i = 0; i < Inventory.getAllProducts().size(); i++) {
                if (Inventory.getAllProducts().get(i).getId() == newProductId) {
                    newProductId++;
                    alreadyExists = true;
                    break;
                }
            }
        }
        return newProductId;
    }

    // OnAction group
    /**
     *  This will add a part from the partsTableView to the currently added product.
     *  Features a warning prompt in case there is no part selected.
     *
     *  FUTURE IMPROVEMENT: check if the part can be added more than once.
     * @param event the user clicks on the Attach Part button.
     *  */
    @FXML
    void onActionAttachPartToProduct(ActionEvent event) {
        Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            associatedParts.add(selectedPart);
            updatePartsTable();
            updateAssociatedPartsTable();
        }
        else {
            MainScreenController.warningPrompt("Choose a part", "Please select a part to be added to the product.");
        }
    }

    /**
     *  This action will save the product and load the MainScreen.
     *  There is an input validation system implemented to check for empty text fields or incorrect minimum & maximum values.
     *
     *  @param event the user clicks on the Save button.
     *
     *  */
    @FXML
    void onActionSaveProduct(ActionEvent event) throws IOException {
        try {
            Integer productInventory = Integer.parseInt(this.addProductInventory.getText());
            Integer productMinimum = Integer.parseInt(this.addProductMinimum.getText());
            Integer productMaximum = Integer.parseInt(this.addProductMaximum.getText());

            if (MainScreenController.confirmationPrompt("Save Product?", "Would you like this product to be saved?"))
                if (productMaximum < productMinimum) {
                    MainScreenController.warningPrompt("Invalid input value", "The Maximum value cannot be lower than the Minimum value.");
                }
            if (productMinimum > productInventory || productInventory > productMaximum) {
                MainScreenController.warningPrompt("Invalid input", "Product inventory must be between the Minimum and Maximum values.");
            }
            else {
                Product product = new Product();
                product.setId(createNewProductId());
                product.setName(this.addProductName.getText());
                product.setStock(Integer.parseInt(this.addProductInventory.getText()));
                product.setMin(Integer.parseInt(this.addProductMinimum.getText()));
                product.setPrice(Double.parseDouble(this.addProductPrice.getText()));
                product.addAssociatedPart(associatedParts);
                Inventory.addProduct(product);

                wg.inventorymanagement.Controller.MainScreenController.taskComplete(event);
            }
        }
        catch (NumberFormatException missingFields) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Missing Correct Input");
            alert.setContentText("All fields must be filled out before the product can be added.");
            alert.showAndWait();
        }
    }

    /**
     * This action will cancel the current process and return the user to MainScreen.
     *
     * @param event the user clicks on the Cancel button.
     * */
    @FXML
    void onActionCancelAndReturnToMainScreen(ActionEvent event) throws IOException {
            MainScreenController.returnToMainScreen(event);
    }

    /**
     * This action will remove the associated part from the selected product.
     * It features a warning prompt if button is pressed but no part has been selected.
     * It also features a basic confirmation check prior to removing an associated part from a product.
     *
     * @param event the user clicks on the Remove Associated Part button.
     * */
    @FXML
    void onActionRemoveAssociatedPart(ActionEvent event) {
        Part selectedPart = associatedPartsTableView.getSelectionModel().getSelectedItem();

        if(selectedPart != null) {
            MainScreenController.confirmationPrompt("Confirm deletion", "Are you sure you want to proceed with removing " + selectedPart.getName() + " from this product?");
            associatedParts.remove(selectedPart);
            updatePartsTable();
            updateAssociatedPartsTable();
        }
        else {
            MainScreenController.confirmationPrompt("Confirm deletion", "Are you sure you want to proceed with");
        }
    }

    /**
     * This initializes the addProduct controller class.
     * It implements a listener on the partSearch text field using a lambda function to allow filtering of results without requiring a button press.
     *
     * RUNTIME ERROR: kept getting an error and shortly after a crash while trying to get the search box to filter the partsTableView implemented a lambda function which returns the searchPart results into the table View correctly.
     * FUTURE IMPROVEMENT: allow for the search parameters to return values in either lower or upper case, currently it only returns results that match the casing directly.
     * @param location
     * @param resources
     * */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        partSearch.textProperty().addListener((observable, oldValue,newValue) -> {
                    FilteredList searchProductResult = Inventory.getAllProducts().filtered((product) -> product.getName().contains(newValue));
                    partsTableView.setItems(searchProductResult);
                }
        );

        originalParts = Inventory.getAllParts();

        associatedPartsTableView.setItems(associatedParts);
        associatedPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedProductNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedProductInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedProductPricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        partsTableView.setItems(originalParts);
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        updatePartsTable();
        updateAssociatedPartsTable();
    }
}