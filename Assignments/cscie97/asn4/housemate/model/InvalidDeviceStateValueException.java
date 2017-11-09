package cscie97.asn4.housemate.model;

public class InvalidDeviceStateValueException extends Exception {

    InvalidDeviceStateValueException(String fqn, String expectedValue, String actualValue){
        super("Device " + fqn + " accepts values of type " + expectedValue + " but received " + actualValue);
    }

}
