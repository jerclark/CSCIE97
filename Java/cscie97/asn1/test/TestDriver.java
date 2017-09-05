package cscie97.asn1.test;

import cscie97.asn1.knowledge.engine.*;


public class TestDriver {

    public static void main(String[] args){
        TestDriver tester = new TestDriver();
        tester.testTriple();
        tester.testImportTriple();
    }

    public void testTriple(){
        Node sub = new Node("Jeremy");
        Node obj = new Node("Molly");
        Predicate pred = new Predicate("is_married_to");
        Triple trip = new Triple(sub, pred, obj);
        System.out.println(trip.getIdentifier());
    }


    public void testImportTriple(){
        KnowledgeGraph.getInstance().importTriple("Jeremy", "is_married_to", "Molly");
        KnowledgeGraph.getInstance().importTriple("Jeremy", "is_father_of", "Mila");
        KnowledgeGraph.getInstance().importTriple("Jeremy", "is_married_to", "Molly");
    }

}
