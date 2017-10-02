package cscie97.asn2.housemate.model;

public class UnauthorizedException extends Exception {

    UnauthorizedException(){
        super("Unauthorized Request: You are not authorized to run " + Thread.currentThread().getStackTrace()[2].getMethodName());
    }

}
