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
import wg.inventorymanagement.Model.Inventory;
import wg.inventorymanagement.Model.Part;
import wg.inventorymanagement.Model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the controller class responsible for providing the control logic for the modify product screen of the app.
 */
public class ModifyProductController implements Initializable {

    private Stage stage;

    private Object scene;

    private int productId;
    private Product selectedProduct;

    private Product adjustedProduct;

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private ObservableList<Part> originalParts = FXCollections.observableArrayList();

    // Text field group

    @FXML
    private TextField id;

    @FXML
    private TextField name;

    @FXML
    private TextField inventory;

    @FXML
    private TextField price;

    @FXML
    private TextField maximum;

    @FXML
    private TextField minimum;

    @FXML
    private TextField partSearch;

    // Original parts group Table View
    @FXML
    private TableView<Part> partsTableView;

    @FXML
    private TableColumn<Part, String> partNameView;

    @FXML
    private TableColumn<Part, Integer> partsIdCol;

    @FXML
    private TableColumn<Part, Double> pricePerUnitCol;

    @FXML
    private TableColumn<Part, Integer> inventoryCol;

    // Associated parts group Table View
    @FXML
    private TableView<Part> associatedPartsTableView;

    @FXML
    private TableColumn<Product, Integer> associatedPartIdCol;

    @FXML
    private TableColumn<Product, Integer> associatedProductInventoryCol;

    @FXML
    private TableColumn<Product, String> associatedProductNameCol;

    @FXML
    private TableColumn<Product, Double> associatedProductPricePerUnitCol;

    // Method group
    /**
     * This method updates the partsTableView with all items from the inventory.
     * */
    public void updatePartTable() {
        partsTableView.setItems(wg.inventorymanagement.Model.Inventory.getAllParts());
    }

    /**
     *This method updates the associatedProductsTableView with all associated parts.
     */
    public void updateAssociatedPartTable() {
        associatedPartsTableView.setItems(associatedParts);
    }

    /**
     * This method populates the text fields with the relevant data for the selected Product.
     *
     * @param selectedProduct this will be the selected product in the Product table view.
     */
    public void setProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
        productId = wg.inventorymanagement.Model.Inventory.getAllProducts().indexOf(selectedProduct);
        id.setText(Integer.toString(selectedProduct.getId()));
        name.setText(selectedProduct.getName());
        inventory.setText(Integer.toString(selectedProduct.getStock()));
        price.setText(Double.toString(selectedProduct.getPrice()));
        minimum.setText(Integer.toString(selectedProduct.getMin()));
        maximum.setText(Integer.toString(selectedProduct.getMax()));
        associatedParts.addAll(selectedProduct.getAllAssociatedParts());
    }

    // OnAction group
    /**
     * This action will attach a part to the current product.
     * Features a warning prompt in case there is no part selected.
     *
     * FUTURE IMPROVEMENT: check if the part can be added more than once.
     *
     * @param event the user clicks on the Attach Part button.
     * */
    @FXML
    void onActionAttachPartToProduct(ActionEvent event) {
        Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            associatedParts.add(selectedPart);
            updateAssociatedPartTable();
        }

        else {
            MainScreenController.warningPrompt("Choose a part", "You must choose a part to be added to this product.");
        }
    }

    /**
     *  This action will modify the product and load the MainScreen.
     *  Features a confirmation prompt to verify with the user that changes should be committed.
     *  There is an input validation system implemented to check for empty text fields or incorrect minimum & maximum values.
     *  Upon completion the MainScreenController.taskComplete(event) will take the user back to the MainScreen.
     *
     *  @param event the user clicks on the Save button.
     * */
    @FXML
    void onActionSaveProduct(ActionEvent event) throws IOException {
        int productStock = Integer.parseInt(inventory.getText());
        int productMaximum = Integer.parseInt(maximum.getText());
        int productMinimum = Integer.parseInt(minimum.getText());

        if (MainScreenController.confirmationPrompt("Confirm changes?", "Are you sure you would like to commit these changes?"))
            if (productMaximum < productMinimum) {
                MainScreenController.warningPrompt("Invalid input value","The Maximum value cannot be lower than the Minimum value.");
                }
        if ((productStock < productMinimum) || (productMaximum < productStock)) {
            MainScreenController.confirmationPrompt("Invalid input value", "Product inventory must be between the Minimum and Maximum values.");
        }
            else {
                this.adjustedProduct = selectedProduct;
                selectedProduct.setId(Integer.parseInt(id.getText()));
                selectedProduct.setName(name.getText());
                selectedProduct.setStock(Integer.parseInt(inventory.getText()));
                selectedProduct.setMax(Integer.parseInt(maximum.getText()));
                selectedProduct.setMin(Integer.parseInt(minimum.getText()));
                selectedProduct.getAllAssociatedParts().clear();
                selectedProduct.addAssociatedPart(associatedParts);
                wg.inventorymanagement.Model.Inventory.updateProduct(productId, selectedProduct);

                wg.inventorymanagement.Controller.MainScreenController.taskComplete(event);
            }
        }

    /**
     * This action will take the user to the MainScreen.
     * It uses the returnToMainScreen method declared in the MainScreenController to reduce code repetition.
     *
     * @param event the user clicks on the Cancel button.
     * */
    @FXML
    void onActionCancelAndReturnToMainScreen(ActionEvent event) throws IOException {
            MainScreenController.returnToMainScreen( event);
    }

    /**
     * This action will remove the associated part from the selected product.
     * It features a warning prompt if button is pressed but no part has been selected.
     * It also features a basic confirmation check prior to removing an associated part from a product.
     *
     * @param event the user clicks on the Remove Associated Part button.
     * */
    @FXML
    void onActionRemoveAssociatedProduct(ActionEvent event){
        Part selectedPart = associatedPartsTableView.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            MainScreenController.confirmationPrompt("Confirm deletion", "Are you sure you want to proceed with removing " + selectedPart.getName() + " from this product?");
            associatedParts.remove(selectedPart);
            updateAssociatedPartTable();
        }

        else {
            MainScreenController.warningPrompt("Choose a part", " You must choose a part to be removed in order to continue.");
        }
    }

    /**
     * This initializes the modifyProduct controller class
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

        partsTableView.setItems(wg.inventorymanagement.Model.Inventory.getAllParts());
        partsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameView.setCellValueFactory(new PropertyValueFactory<>("name"));
        inventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        pricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));


        associatedPartsTableView.setItems(associatedParts);
        associatedPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedProductNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedProductInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedProductPricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        updatePartTable();
        updateAssociatedPartTable();
    }
}