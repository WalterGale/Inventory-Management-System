package wg.inventorymanagement.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;


import wg.inventorymanagement.Model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the controller class responsible for providing the control logic for the modify part screen of the app.
 */
public class ModifyPartController implements Initializable {

    private Stage stage;

    private Object scene;

    private int selectedPartId;
    public Part selectedPart;

    // public Part partToBeModified;
    // Text fields group
    @FXML
    private TextField modifyPartId;

    @FXML
    private TextField modifyPartNameText;

    @FXML
    private TextField modifyPartInventoryText;

    @FXML
    private TextField modifyPartPrice;

    @FXML
    private TextField partMaximumText;

    @FXML
    private TextField partMinimumText;

    @FXML
    private TextField machineIdOrCompanyName;

    // Radio group
    @FXML
    private ToggleGroup modifyPartToggleGroup;

    @FXML
    private RadioButton inHouseRadio;

    @FXML
    private RadioButton outsourcedRadio;

    @FXML
    private Label machineIdOrCompanyLabel;

    // Method group

    /**
     * This method populates the text fields with the relevant data for the selected Part.
     *
     * @param selectedPart this will be the selected part in the Part table view.
     */
    public void setPart (Part selectedPart) {
        this.selectedPart = selectedPart;
        selectedPartId = wg.inventorymanagement.Model.Inventory.getAllParts().indexOf(selectedPart);

        modifyPartId.setText(Integer.toString(selectedPart.getId()));
        modifyPartNameText.setText(selectedPart.getName());
        modifyPartInventoryText.setText(Integer.toString(selectedPart.getStock()));
        modifyPartPrice.setText(Double.toString(selectedPart.getPrice()));
        partMinimumText.setText(Integer.toString(selectedPart.getMin()));
        partMaximumText.setText(Integer.toString(selectedPart.getMax()));

        if (selectedPart instanceof InHouse) {
            InHouse inHouse = (InHouse) selectedPart;
            inHouseRadio.setSelected(true);
            this.machineIdOrCompanyName.setText("Machine ID");
            machineIdOrCompanyName.setText(Integer.toString(inHouse.getMachineId()));
        }
        else {
            Outsourced outsourced = (Outsourced) selectedPart;
            outsourcedRadio.setSelected(true);
            this.machineIdOrCompanyName.setText("Company Name");
            machineIdOrCompanyName.setText(outsourced.getCompanyName());
        }
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
            this.machineIdOrCompanyLabel.setText("Machine ID");
        }
        else
            this.machineIdOrCompanyLabel.setText("Company Name");
    }

    /**
     * This action will save the modified part to the inventory and return the user to MainScreen.
     *
     * The taskComplete method is used to return to the Main Screen.
     * Features a confirmation prompt to verify with the user that changes should be committed.
     * Features an error catch and handle system to prevent empty fields and to ensure that the partMinimum is not greater than partMaximum.
     * Upon completion the MainScreenController.taskComplete(event) takes the user back to the Main Screen
     *
     * LOGIC ERROR: Had an error where the part to be updated would create a duplicate & overwrite the next entry in the list.
     * LOGIC ERROR: Located the cause being the updatePart function in the Inventory Model and that its index was not correct, subtracting 1 from the index resolved the issue entirely.
     *
     * @param event the user clicks on the Save button.
     * @throws IOException
     * */
    @FXML
    void onActionSavePart (ActionEvent event) throws IOException {
        int partStock = Integer.parseInt(modifyPartInventoryText.getText());
        int partMaximum = Integer.parseInt(partMaximumText.getText());
        int partMinimum = Integer.parseInt(partMinimumText.getText());

        if (MainScreenController.confirmationPrompt("Confirm changes?", "Are you sure you would like to commit these changes?"))
            if (partMaximum < partMinimum) {
                MainScreenController.warningPrompt("Invalid input value","The Maximum value cannot be lower than the Minimum value.");
                }
        if ((partStock < partMinimum) || (partMaximum < partStock)) {
            MainScreenController.warningPrompt("Invalid input value", "Part inventory must be between the Minimum and Maximum values.");
        }
            else {
              // inline INTs directly into the constructor
              int id = Integer.parseInt(modifyPartId.getText());
              String name = modifyPartNameText.getText();
              int inventory = Integer.parseInt(modifyPartInventoryText.getText());
              double price = Double.parseDouble(modifyPartPrice.getText());
              int minimum = Integer.parseInt(partMinimumText.getText());
              int maximum = Integer.parseInt(partMaximumText.getText());

            if (inHouseRadio.isSelected()) {
                try {
                    int machineID = Integer.parseInt(machineIdOrCompanyName.getText());
                    InHouse storage = new InHouse(id, name, price, inventory, minimum, maximum, machineID);
                    wg.inventorymanagement.Model.Inventory.updatePart(id, storage);

                    wg.inventorymanagement.Controller.MainScreenController.taskComplete(event);
                }
                catch (Exception caught) {
                    MainScreenController.warningPrompt("Error adding part", "Check that the machine ID contains only numbers between 0 and 9.");
                }
            }
            else {
                String companyName = machineIdOrCompanyName.getText();
                Outsourced storage = new Outsourced(id ,name, price, inventory, minimum, maximum, companyName);
                wg.inventorymanagement.Model.Inventory.updatePart(id, storage);

                wg.inventorymanagement.Controller.MainScreenController.taskComplete(event);
            }
        }
    }

    /**
     * This action will take the user to the MainScreen.
     * It uses the returnToMainScreen method declared in the MainScreenController to reduce code repetition.
     *
     * @param event the user clicks on the Cancel button.
     * @throws IOException
     * */
    @FXML
    void onActionCancelAndReturnToMainScreen(ActionEvent event) throws IOException {
            MainScreenController.returnToMainScreen(event);
    }

    /**
     * This initializes the modifyPart controller class
     *
     * @param location
     * @param resources
     * */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}