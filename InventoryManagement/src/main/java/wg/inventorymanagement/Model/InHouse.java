package wg.inventorymanagement.Model;

/**
 * This class will create the inHouse objects. It is an extension of the abstract Part class and adds machineID when instantiated.
 * */
public class InHouse extends Part {

    private int machineId;


    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    // getter
    public int getMachineId() {
        return machineId;
    }

    // setter
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}