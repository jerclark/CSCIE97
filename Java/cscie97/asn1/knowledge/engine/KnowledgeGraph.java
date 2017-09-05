package cscie97.asn1.knowledge.engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

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


    public void importTriple(String s, String p, String o){

        String tripleId = s + " " + p + " " + o;
        System.out.println("Attempting to add triple: " + tripleId);

        //Check if we've already got the triple
        if (tripleMap.containsKey(tripleId)){
            System.out.println("Triple ID: '" + tripleId + "' already exists.");
            return;

        }else{                                //Need to add triple

            //Check for each part of the triple in the associated map
            //If it exists use it, if not add it to the map
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


            // Create the query strings
            for (Integer i = 7;i >= 0;--i) {

                String queryString = "";
                String mask = String.format("%3s", Integer.toBinaryString(i)).replace(' ', '0');
                String[] query = {s,p,o};
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

            System.out.println(queryMapSet);

            System.out.println("Added Triple: " + tripleId);

        }
    }




}
