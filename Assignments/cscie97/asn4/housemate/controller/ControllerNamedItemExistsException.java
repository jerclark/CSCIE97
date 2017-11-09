package cscie97.asn4.housemate.controller;


/**
 * Thrown when trying to add a Rule, Predicate, Context or Command with a name that already exists for a similarly typed obejct.
 */
public class ControllerNamedItemExistsException extends Exception {
    public ControllerNamedItemExistsException(String ruleName, String type) {
        super("Controller Item of type " + type + " already exists with name: " + ruleName + ". Please use another name.");
    }
}
