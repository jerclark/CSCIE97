package cscie97.asn3.housemate.controller;

public class UnauthorizedControllerException extends Exception {

    UnauthorizedControllerException(){
        super("Unauthorized Request To Controller: You are not authorized to run " + Thread.currentThread().getStackTrace()[2].getMethodName());
    }

}
