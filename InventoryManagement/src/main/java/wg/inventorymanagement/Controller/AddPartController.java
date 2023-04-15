package wg.inventorymanagement.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import wg.inventorymanagement.Main;
import wg.inventorymanagement.Model.InHouse;
import wg.inventorymanagement.Model.Outsourced;
import static wg.inventorymanagement.Model.Inventory.*;
import wg.inventorymanagement.Model.Part.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.jar.Attributes;

/**
 * This is the controller class responsible for providing the control logic for the add part screen of the app.
 *
 */
public class AddPartController implements Initializable {

    private Stage stage;

    private Object scene;

    // Text fields group
    @FXML
    private TextField addPartId;

    @FXML
    private TextField addPartName;

    @FXML
    private TextField addPartInventory;

    @FXML
    private TextField addPartMaximum;

    @FXML
    private TextField addPartMinimum;

    @FXML
    private TextField addPartPrice;

    @FXML
    private TextField machineIdOrCompanyName;

    // Radio button group
    @FXML
    private ToggleGroup addPartToggleGroup;

    @FXML
    private RadioButton inHouseRadio;

    @FXML
    private RadioButton outsourcedRadio;

    @FXML
    private Label machineIdOrComapnyLabel;

    // Method group
    /**
     * This method is responsible for creating a newPartId based on the current size of getAllParts().
     * It increments the newPartId to always be larger than the number of parts in the inventory.
     * */
    private int createNewPartId() {
        int newPartId = 1;

        for (int i = 0; i < getAllParts().size(); i++) {
            newPartId++;
        }
        return newPartId;
    }

    // OnAction group
    /**
     * Changes the machineIdOrCompanyName depending on which of the radio buttons is selected.
     *
     * @param event the user clicks on one of the radio buttons
     */
    @FXML
    void inHouseOrOutsourced(ActionEvent event) {
        if (inHouseRadio.isSelected()) {
            this.machineIdOrComapnyLabel.setText("Machine ID");
        }
        else
            this.machineIdOrComapnyLabel.setText("Company Name");
    }

    /**
     * This action will take the user to the MainScreen.
     * It uses the returnToMainScreen method declared in the MainScreenController to reduce code repetition.
     *
     * @param event the user clicks on the Cancel button
     * */
    @FXML
    void onActionCancelAndReturnToMainScreen(ActionEvent event) throws IOException {
            MainScreenController.returnToMainScreen( event);
    }

    /**
     * This action will add a new part to the inventory and return the user to MainScreen.
     *
     * The taskComplete method is used to return to the Main Screen.
     * Included an error catch and handle system to prevent empty fields and to ensure that the partMinimum is not greater than partMaximum.
     *
     * Upon completion the MainScreenController.taskComplete(event) takes the user back to the Main Screen.
     *
     * @param event the user clicks on the Save button
     * */
    @FXML
    void onActionSavePart(ActionEvent event) throws IOException {
        try {

            int partStock = Integer.parseInt(addPartInventory.getText());
            int partMaximum = Integer.parseInt(addPartMaximum.getText());
            int partMinimum = Integer.parseInt(addPartMinimum.getText());

            if(MainScreenController.confirmationPrompt("Save Part?","Would you like this part to be saved?"))
                if (partMaximum < partMinimum) {
                MainScreenController.warningPrompt("Invalid input value","The Maximum value cannot be lower than the Minimum value.");
                }
            if ((partStock < partMinimum) || (partMaximum < partStock)) {
                MainScreenController.warningPrompt("Invalid input value", "Part inventory must be between the Minimum and Maximum values.");
            }
            else {
                int newId = createNewPartId();
                String name = addPartName.getText();
                int inventory = Integer.parseInt(addPartInventory.getText());
                double price = Double.parseDouble(addPartPrice.getText());
                int minimum = Integer.parseInt(addPartMinimum.getText());
                int maximum = Integer.parseInt(addPartMaximum.getText());

                    if (inHouseRadio.isSelected()) {
                        try {
                            int machineId = Integer.parseInt(machineIdOrCompanyName.getText());
                            InHouse inHouseStorage = new InHouse(newId, name, price, inventory, minimum, maximum, machineId);

                            wg.inventorymanagement.Model.Inventory.addPart(inHouseStorage);
                        }
                        catch (Exception caught) {
                            MainScreenController.warningPrompt("Error adding part", "Check that the machine ID contains only numbers between 0 and 9.");
                        }
                    }
                    else {
                        String companyName = machineIdOrCompanyName.getText();
                        Outsourced outsourcedStorage = new Outsourced(newId, name, price, inventory, minimum, maximum, companyName);
                        wg.inventorymanagement.Model.Inventory.addPart(outsourcedStorage);
                    }
                wg.inventorymanagement.Controller.MainScreenController.taskComplete(event);
            }
        }

        catch (NumberFormatException missingFields) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Missing Correct Input");
            alert.setContentText("All fields must be filled out before the part can be added.");
            alert.showAndWait();
        }
    }

    /**
     * This initializes the addPart controller class.
     *
     * @param location
     * @param resources
     * */
    @Override
    public void initialize(URL location, ResourceBundle resources){

        inHouseRadio.setSelected(true);
    }
}
