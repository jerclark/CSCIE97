package cscie97.asn1.knowledge.engine;

public class QueryEngineException extends Exception {

    QueryEngineException(String message){
        super("Query Engine issue encountered: " + message);
    }

}
