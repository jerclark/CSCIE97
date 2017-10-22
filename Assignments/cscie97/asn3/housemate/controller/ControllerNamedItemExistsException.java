package cscie97.asn3.housemate.controller;

public class ControllerNamedItemExistsException extends Exception {
    public ControllerNamedItemExistsException(String ruleName, String type) {
        super("Controller Item of type " + type + " already exists with name: " + ruleName + ". Please use another name.");
    }
}
