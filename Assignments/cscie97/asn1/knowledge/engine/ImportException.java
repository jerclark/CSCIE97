package cscie97.asn1.knowledge.engine;

public class ImportException extends Exception {

    ImportException(String message){
        super("Import Issue encountered: " + message);
    }

}