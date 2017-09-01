package cscie97.asn1.knowledge.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class KnowledgeGraph {

    //Singleton methods
    private static KnowledgeGraph ourInstance = new KnowledgeGraph();
    public static KnowledgeGraph getInstance() {
        return ourInstance;
    }

    //Private constructor, prevents duplicates
    private KnowledgeGraph() {
        this.nodeMap = new HashMap<String,Node>();
    }

    //Object maps, stored in memory
    private Map<String,Node> nodeMap = new HashMap<String,Node>();
    private Map<String,Predicate> predicateMap = new HashMap<String,Predicate>();
    private Map<String,Triple> tripleMap = new HashMap<String,Triple>();
    private Map<String,Set<Triple>> queryMapSet = new HashMap<String,Set<Triple>>();


    public void importTriple(String s, String p, String o){

        String tripleId = s + " " + p + " " + o;
        System.out.println("Attempting to add triple: " + tripleId);


        if (tripleMap.containsKey(tripleId)){ //We've already got the triple
            System.out.println("Triple ID: '" + tripleId + "' already exists.");
            return;

        }else{                                //Need to add triple

            //Now, check for each part of the triple
            Node subject;
            if (nodeMap.containsKey(s)){
                subject = nodeMap.get(s);
            }else{
                subject = new Node(s);
                nodeMap.put(s, subject);
            }

            Predicate predicate;
            if (predicateMap.containsKey(p)){
                predicate = predicateMap.get(p);
            }else{
                predicate = new Predicate(p);
                predicateMap.put(p, predicate);
            }

            Node object;
            if (nodeMap.containsKey(o)){
                object = nodeMap.get(o);
            }else{
                object = new Node(o);
                nodeMap.put(o, object);
            }

            Triple newTriple = new Triple(subject, predicate, object);
            tripleMap.put(tripleId, newTriple);

            //Now populate the queryMap

            System.out.println("Added Triple: " + tripleId);

        }
    }




}
