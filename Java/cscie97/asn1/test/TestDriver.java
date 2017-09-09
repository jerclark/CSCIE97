package cscie97.asn1.test;

import cscie97.asn1.knowledge.engine.*;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;


public class TestDriver {

    public static void main(String[] args){
        if (args.length == 0){
            System.out.println("Please pass a triple file name as a program argument, and optionally a query file as a second argument.");
            return;
        }

        //Get the triple file fully qualified path
        String tripleFile = args[0];
        URL tripleFilePath = TestDriver.class.getResource(tripleFile);
        String tripleFileString;
        try{
            tripleFileString = URLDecoder.decode(tripleFilePath.getPath(), "UTF-8");
        }catch(UnsupportedEncodingException e){
            System.out.println(e);
            return;
        }

        //Get the query file fully qualified path
        String queryFile = args[1];
        URL queryFilePath = TestDriver.class.getResource(queryFile);
        String queryFileString;
        try{
            queryFileString = URLDecoder.decode(queryFilePath.getPath(), "UTF-8");
        }catch(UnsupportedEncodingException e){
            System.out.println(e);
            return;
        }


        TestDriver tester = new TestDriver();
        //tester.testTriple();
        //tester.testImportTriple();
        //tester.testQuery();
        //tester.testImportTripleFile(tripleFileString);
        tester.testQueryEngineExecuteQueryFile(tripleFileString, queryFileString);
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

    public void testQuery(){
        KnowledgeGraph.getInstance().importTriple("Jeremy", "is_married_to", "Molly");
        KnowledgeGraph.getInstance().importTriple("Jeremy", "is_father_of", "Mila");
        System.out.println(KnowledgeGraph.getInstance().executeQuery("Jeremy", "?", "Molly"));
    }

    public void testQueryEngineExecuteQuery() {
        QueryEngine qe = new QueryEngine();

        //Empty query test
        try {
            qe.executeQuery("");
        } catch (QueryEngineException e) {
            e.printStackTrace();
        }

        //Malformed query test
        try {
            qe.executeQuery("Jeremy eats a terd");
        } catch (QueryEngineException e) {
            e.printStackTrace();
        }

        //No results query test
        try {
            qe.executeQuery("Melville ? ?");
        } catch (QueryEngineException e) {
            e.printStackTrace();
        }

        //Results query test
        try {
            qe.executeQuery("Starbucks ? ?");
        } catch (QueryEngineException e) {
            e.printStackTrace();
        }

    }


    public void testQueryEngineExecuteQueryFile(String tripleFileName, String queryFileName) {

        Importer importer = new Importer();
        try {
            importer.importTripleFile(tripleFileName);
        }catch(ImportException ie){
            System.out.println(ie);
        }


        QueryEngine qe = new QueryEngine();
        try {
            qe.executeQueryFile(queryFileName);
        } catch (QueryEngineException e) {
            e.printStackTrace();
        }

    }


    public void testImportTripleFile(String tripleFile){
        Importer importer = new Importer();
        try {
            importer.importTripleFile(tripleFile);
        }catch(ImportException ie){
            System.out.println(ie);
        }
    }



}
