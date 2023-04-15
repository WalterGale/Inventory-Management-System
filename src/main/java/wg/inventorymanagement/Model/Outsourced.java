package wg.inventorymanagement.Model;

/**
 * This class will create the outsourced objects. It is an extension of the abstract Part class and adds companyName when instantiated.
 * */
public class Outsourced extends Part{

    private String companyName;
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    // getter method
    public String getCompanyName() {
        return companyName;
    }

    // setter method
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}

