package cscie97.asn4.housemate.model;

public class ItemNotFoundException extends Exception{

    public ItemNotFoundException(String fqn){
        super("No item found with identifier: " + fqn);
    }

}
