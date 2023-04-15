module wg.inventorymanagement {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens wg.inventorymanagement to javafx.fxml;
    exports wg.inventorymanagement;
    exports wg.inventorymanagement.Controller;
    opens wg.inventorymanagement.Controller to javafx.fxml;
    exports wg.inventorymanagement.Model;
    opens wg.inventorymanagement.Model to javafx.fxml;

}