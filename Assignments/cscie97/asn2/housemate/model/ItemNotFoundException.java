package cscie97.asn2.housemate.model;

public class ItemNotFoundException extends Exception{

    ItemNotFoundException(String fqn){
        super("No item found with identifier: " + fqn);
    }

}
