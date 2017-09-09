package cscie97.asn1.knowledge.engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;


/**
 * Represents all known facts in the system, as well as a query index to look them up
 *
 * @author Jeremy Clark
 *
 */
public class KnowledgeGraph {

    //Singleton methods
    private static KnowledgeGraph ourInstance = new KnowledgeGraph();
    public static KnowledgeGraph getInstance() {
        return ourInstance;
    }

    //Private constructor, prevents duplicates
    private KnowledgeGraph() {
        this.nodeMap = new HashMap<String,Node>();
        this.queryMapSet.put("? ? ?", new HashSet<Triple>());
    }

    //Object maps, stored in memory
    private Map<String,Node> nodeMap = new HashMap<String,Node>();
    private Map<String,Predicate> predicateMap = new HashMap<String,Predicate>();
    private Map<String,Triple> tripleMap = new HashMap<String,Triple>();
    private Map<String,HashSet<Triple>> queryMapSet = new HashMap<String,HashSet<Triple>>();


    /**
     * Imports the triple in to the knowledge graph.
     * Converts the incoming strings into Node/Predicate objects
     * then calls into getTriple, which will handle fetching
     * existing Triples or creating a new one.
     *
     *
     * @param s subject string
     * @param p predicate string
     * @param o object string
     *
     */
    public void importTriple(String s, String p, String o){

        //Fetch/Create the triple part objects
        Node subject = getNode(s);
        Predicate predicate = getPredicate(p);
        Node object = getNode(o);

        //Now fetch/create the triple.
        getTriple(subject, predicate, object);


    }

    /**
     * Executes a query on the knowlege graph.
     * Concatenates the passed in values into a query string,
     * which is used as a key in queryMapSet to fetch the stored matches.
     * Any or all of the incoming parameters can be "?" wildcards.
     *
     * @param s subject string
     * @param p predicate string
     * @param o object string
     * @return
     */
    public HashSet<Triple> executeQuery(String s, String p, String o) {
        String queryString = s + " " + p + " " + o;
        HashSet<Triple> result = new HashSet<Triple>();
        if (queryMapSet.containsKey(queryString)) {
            result = queryMapSet.get(queryString);
        }
        return result;
    }

    /**
     * Gets a node from the stored node map.
     *
     * @param id node string identifier
     * @return Node
     */
    public Node getNode(String id){
        Node n;
        if (nodeMap.containsKey(id)){
            n = nodeMap.get(id);
        }else{
            n = new Node(id);
            nodeMap.put(id, n);
        }
        return n;
    }

    /**
     * Gets a predicate from the stored set of predicates used in the knowledge graph.
     *
     * @param id predicate string identifier
     * @return Predicate
     */
    public Predicate getPredicate(String id){
        Predicate p;
        if (predicateMap.containsKey(id)){
            p = predicateMap.get(id);
        }else{
            p = new Predicate(id);
            predicateMap.put(id, p);
        }
        return p;
    }


    /**
     * Gets a triple from the available triples in the knowledge graph.
     * If triple doesn't exist, creates a new one, adds it to the tripleMap and
     * creates a queryMapSet for that triple.
     *
     * @param subject Node
     * @param predicate Predicate
     * @param object Node
     * @return Triple
     */
    public Triple getTriple(Node subject, Predicate predicate, Node object){

        String tripleId = subject.getIdentifier() + " " + predicate.getIdentifier() + " " + object.getIdentifier();

        if (tripleMap.containsKey(tripleId)) {
            System.out.println("Triple ID: '" + tripleId + "' already exists.");
            return tripleMap.get(tripleId);
        }else{
            Triple newTriple = new Triple(subject, predicate, object);
            tripleMap.put(tripleId, newTriple);

            // Create the query strings
            for (Integer i = 7;i >= 0;--i) {

                String queryString = "";
                String mask = String.format("%3s", Integer.toBinaryString(i)).replace(' ', '0');
                String[] query = {subject.getIdentifier(),predicate.getIdentifier(),object.getIdentifier()};
                for(Integer cnt = 0;cnt < query.length;cnt++){
                    if (mask.charAt(cnt) == '0'){
                        query[cnt] = "?";
                    }
                    queryString = String.join(" ", query);
                }

                HashSet<Triple> queryTriples; // = queryMapSet.get(queryString);
                if (queryMapSet.containsKey(queryString)){
                    queryTriples = queryMapSet.get(queryString);
                }else {
                    queryTriples = new HashSet<Triple>();
                }
                queryTriples.add(newTriple);
                queryMapSet.put(queryString, queryTriples);
            }

            System.out.println("\tImported: '" + tripleId + "'");

            return newTriple;

        }

    }

}
