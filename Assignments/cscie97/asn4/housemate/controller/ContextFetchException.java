package cscie97.asn4.housemate.controller;

public class ContextFetchException extends Exception {

    ContextFetchException(Exception e){
        super("There was an issue populating the context. The underlying issue was: " + e);
    }

}
