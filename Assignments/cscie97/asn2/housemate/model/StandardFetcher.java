package cscie97.asn2.housemate.model;
import cscie97.asn1.knowledge.engine.KnowledgeGraph;
import cscie97.asn1.knowledge.engine.Triple;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

public class StandardFetcher implements Fetcher {
    public ArrayList<String> getState(String subject){

        ArrayList<String> result = new ArrayList<String>();

        //Create a 'consumer' iterator to add the triple for each line
        Consumer<Triple> tripleToString = (triple) -> {
            result.add(triple.getIdentifier());
        };

        KnowledgeGraph kg = KnowledgeGraph.getInstance();
        HashSet<Triple> triples = kg.executeQuery(subject, "?", "?");
        triples.forEach(tripleToString);
        return result;
    };
}