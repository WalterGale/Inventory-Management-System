package wg.inventorymanagement.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * This class contains the inventory for products and parts.
 * It also includes methods to add, lookup(via name or ID), update, delete as well as get all items for parts and products
 * */
public class Inventory {
    //private static int partId = 0;
    //private static int productId = 0;

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();


    //Class methods
    public static void addPart (Part newPart) {
        if (newPart != null) {
            allParts.add(newPart);
        }
    }

    public static void addProduct (Product newProduct) {
        if (newProduct != null) {
            allProducts.add(newProduct);
        }
    }
    public static Part lookupPartById(int partId) {
        Part partLocated = null;

        for (Part part : allParts) {
            if (part.getId() == partId) {
                partLocated = part;
            }
        }
        return partLocated;
    }

    public static Product lookupProductById (int productId) {
        Product productLocated = null;

        for (Product product : allProducts) {
            if (product.getId() == productId) {
                productLocated = product;
            }
        }
        return productLocated;
    }

    public static ObservableList<Part> lookupPartByName (String searchByPartName) {
        ObservableList<Part> partLocated = FXCollections.observableArrayList();

        if (searchByPartName.length() == 0) {
            partLocated = allParts;
        }
        else {
            for (int i = 0; i < allParts.size(); i++) {
                if (allParts.get(i).getName().toLowerCase().contains(searchByPartName.toLowerCase())) {
                    partLocated.add(allParts.get(i));
                }
            }
        }
        return partLocated;
    }

    public static ObservableList<Product> lookupProductByName (String searchByProductName) {
        ObservableList<Product> productLocated = FXCollections.observableArrayList();

        if (searchByProductName.length() == 0) {
            productLocated = allProducts;
        }
        else {
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getName().toLowerCase().contains(searchByProductName.toLowerCase())) {
                    productLocated.add(allProducts.get(i));
                }
            }
        }
        return productLocated;
    }
    /**
     * LOGIC ERROR: Had an error where the part to be updated would create a duplicate and overwrite the next entry in the list.
     * LOGIC ERROR: Located the cause being the updatePart function and that its index was not correct, subtracting 1 from the index resolved the issue entirely.
     * */
    // had an issue with the updatePart method overwriting the next id entry based on the index start point, subtracted 1 from the index start point and the issue was resolved
    public static void updatePart (int index, Part selectedPart) {
        allParts.set(index - 1, selectedPart);
    }

    public static void updateProduct (int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    public static boolean deletePart (Part selectedPart) {
        if (allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean deleteProduct (Product selectedProduct) {
        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;
        }
        else {
            return false;
        }
    }

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }


}
