package cscie97.asn4.housemate.model;

public class ItemExistsException extends Exception {

    ItemExistsException(String fqn, ConfigurationItem item){
        super("Item of type " + item.getClass().toString() + " already exists with identifier: " + fqn);
    }

}
