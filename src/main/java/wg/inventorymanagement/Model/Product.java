package wg.inventorymanagement.Model;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

/**
 * This class will create the Product objects.
 * It contains getters and setters for all parameters of a Product.
 *
 * */
public class Product {

    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    public Product(int id, String name, double price, int stock, int min, int max ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    public Product() {
        this (0, null, 0.00, 0, 0, 0);
    }

    // getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    //Class methods
    public void addAssociatedPart(ObservableList <Part> newPart) {
        this.associatedParts.addAll(newPart);
    }

    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return associatedParts.remove(selectedAssociatedPart);
    }

    public void setAssociatedParts(ObservableList<Part> associatedParts) {
        this.associatedParts = associatedParts;
    }

    public Part lookupAssociatedPart (Part selectedAssociatedPart) {
        for (Part associatedPart : associatedParts) {
            if (selectedAssociatedPart.equals(associatedPart)) {
                return associatedPart;
            } else {
                throw new IllegalArgumentException("This part could not be found.");
            }
        }
        return null;
    }

    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}