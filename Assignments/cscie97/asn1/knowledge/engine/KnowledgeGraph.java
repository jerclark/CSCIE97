package cscie97.asn1.knowledge.engine;

import java.util.*;
import java.util.function.Consumer;


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
            generateQueryMap(newTriple);
            //System.out.println("\tImported: '" + tripleId + "'");
            return newTriple;

        }

    }


    /**
     * Removes a triple from the available triples in the knowledge graph.
     * If triple doesn't exist,skips it.
     * Removes the triple from the triple map and the queryMapSet.
     * Does NOT modify Node or Predicate collections. That is, if a Node or Predicate is no longer
     * Used after removal of this triple, it will still exist in the KG.
     *
     * @param triple Triple
     * @return void
     */
    public void removeTriple(Triple triple){

        if (tripleMap.containsKey(triple.getIdentifier())){
            tripleMap.remove(triple.getIdentifier());
        }
        removeTripleFromQueryMap(triple);
    }


    /**
     * Takes a triple, determines all possible query strings, and adds the
     * mappings to the query map set.<br>
     * For each query string associated with a triple,
     * each part of the triple is either the part's identifier (let's call it '1')
     * OR a '?' character (let's call it '0').<br>
     * Since a triple has 3 parts, and each part can be one of two things in a query
     * string, there are 2^3 = 8 possible permutations.<br>
     * The algorithm below iterates from 0 to n-1 where n is number of permutations.
     * For each iterated integer, the decimal number is converted to a 3-digit binary number.
     * That binary number is then mapped to the query string, where 0=? and 1={the part's original identifier.}
     * For example if the Triple is "Joe eats donuts":
     * <ul>
     *     <li>
     *         0 = 000 = "? ? ?"
     *     </li>
     *     <li>
     *         1 = 001 = "? ? donuts"
     *     </li>
     *     <li>
     *         2 = 010 = "? eats ?"
     *     </li>
     *     <li>
     *         And so on...
     *     </li>
     *</ul>
     * This way, the algorithm could be generalized to handle any cardinality of facts - for example:
     * <ul>
     *     <li>
     *         doubles would loop 2^2 times,
     *     </li>
     *      <li>
     *         quadruples 2^4 times
     *     </li>
     *      <li>
     *         quintuples 2^5 times, etc
     *     </li>
     * </ul>
     * The implementation below, however, is hard coded to loop 8 times (0-7) since
     * we know the knowledge graph currently only handles triples.
     *
     * @param t A Triple that needs to be added to the queryMap.
     */
    private void generateQueryMap(Triple t){

        //Loop 2^3 = 8 times
        for (Integer i = 7;i >= 0;--i) {

            //Initialize the output string
            String queryString = ""; //Intialize the output string

            //Create the query 'mask' - convert 'i' into a 3-digit binary number
            String mask = String.format("%3s", Integer.toBinaryString(i)).replace(' ', '0');

            //Split the triple identifer into an array
            String[] query = t.getIdentifier().split(" ");

            //For each item in the triple ID array - replace the value with a "?" if the
            //corresponding index in the mask is 0.
            for(Integer cnt = 0;cnt < query.length;cnt++){
                if (mask.charAt(cnt) == '0'){
                    query[cnt] = "?";
                }
                queryString = String.join(" ", query);
            }

            //Now if the query string already exists, add this triple to the value set
            //Otherwise create the queryMapSet key with the current query string then add
            //the value to that set.
            HashSet<Triple> queryTriples;
            if (queryMapSet.containsKey(queryString)){
                queryTriples = queryMapSet.get(queryString);
            }else {
                queryTriples = new HashSet<Triple>();
            }

            //Update the value for the query string key with the triple
            queryTriples.add(t);
            queryMapSet.put(queryString, queryTriples);

        }
    }

    /**
     * The same algorithm as "generateQueryMap" to determine all
     * possible query strings for a triple. But in this case, we
     * remove the triple from all of it's queryStrings. If it's the last
     * triple for a query string, we remove the query string.
     *
     * @param t Triple
     */
    private void removeTripleFromQueryMap(Triple t){

        //Loop 2^3 = 8 times
        for (Integer i = 7;i >= 0;--i) {

            //Initialize the output string
            String queryString = ""; //Intialize the output string

            //Create the query 'mask' - convert 'i' into a 3-digit binary number
            String mask = String.format("%3s", Integer.toBinaryString(i)).replace(' ', '0');

            //Split the triple identifer into an array
            String[] query = t.getIdentifier().split(" ");

            //For each item in the triple ID array - replace the value with a "?" if the
            //corresponding index in the mask is 0.
            for(Integer cnt = 0;cnt < query.length;cnt++){
                if (mask.charAt(cnt) == '0'){
                    query[cnt] = "?";
                }
                queryString = String.join(" ", query);
            }

            //Now if the query string already exists, add this triple to the value set
            //Otherwise create the queryMapSet key with the current query string then add
            //the value to that set.
            HashSet<Triple> queryTriples = queryMapSet.get(queryString);

            //Update the value for the query string key with the triple
            queryTriples.remove(t);
            if (queryTriples.isEmpty()){
                queryMapSet.remove(queryString);
            }else {
                queryMapSet.put(queryString, queryTriples);
            }

        }
    }



}
