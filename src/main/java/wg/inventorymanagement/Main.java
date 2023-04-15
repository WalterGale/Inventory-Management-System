package wg.inventorymanagement;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import wg.inventorymanagement.Model.*;

import java.io.IOException;

/**
 * The Inventory Management application manages an inventory of parts and products, which consist of parts
 *
 * A feature that would be beneficial in the future is linking it to a list of orders to know which parts will be required.
 * Another feature would be to specify which parts could require duplicates, such as for example a PC build only needing 1 PSU but 4 fans.
 *
 * The folder for JavaDocs can be located in the Javadocs subdirectory of the Performance Assessment folder.
 * */

public class Main extends Application {
    /**
     * The main method creates a set of sample data and launches the application.
     * */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/MainScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 620);
        stage.setTitle("Inventory Management");
        stage.setScene(scene);
        stage.show();

        Inventory.addPart(new InHouse(1, "Motherboard", 199.99, 21, 5, 500, 10_001));
        Inventory.addPart(new InHouse(2, "Central Processing Unit", 149.99, 46, 5, 500, 10_002));
        Inventory.addPart(new InHouse(3, "Graphics Card", 499.99, 21, 9, 50, 10_003));
        Inventory.addPart(new InHouse(4, "Random Access Memory - 8GB", 99.99, 71, 20, 1_000, 10_004));
        Inventory.addPart(new InHouse(5, "Lighting", 19.99, 61, 20, 1_000, 1_0005));

        Inventory.addPart(new Outsourced(6, "Small Computer Case", 49.99, 251, 10, 1_000, "Dell"));
        Inventory.addPart(new Outsourced(7, "Large Computer Case", 69.99, 81, 10, 1_000, "NFX"));
        Inventory.addPart(new Outsourced(8, "Computer Fan", 19.99, 176, 10, 10_000, "Noctua"));
        Inventory.addPart(new Outsourced(9, "Power Supply", 99.99, 212, 10, 1_000, "Dell"));

        Inventory.addProduct(new Product(101, "Entry Level Build", 399.99, 17, 10, 100));
        Inventory.addProduct(new Product(102, "Intermediate Level Build", 599.99, 41, 10, 100));
        Inventory.addProduct(new Product(103, "Advanced Level Build", 899.99, 6, 5, 100));
        Inventory.addProduct(new Product(104, "Ultimate Level Build", 1_299.99, 9, 5, 50));
    }

    /**
     * The main method is the starting point of the app.
     * */
    public static void main(String[] args) {
        launch();
    }
}