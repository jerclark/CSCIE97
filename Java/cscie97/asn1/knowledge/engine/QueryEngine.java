package cscie97.asn1.knowledge.engine;


import javax.management.Query;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;



/**
 * Supports the execution of Knowledge Graph queries.
 * Queries are specified as Triples in N-Triple format with the special "?" identifier representing query or "wild card".
 * All matching Triples known by the Knowledge Graph are  printed to stdout, preceded by the query string.
 *
 * @see KnowledgeGraph
 */
public class QueryEngine {




    public QueryEngine() {
    }


    /**
     *
     * Queries KnowledgeGraph with a single query string
     *
     * @param query a single query string of the form "subject predicate object" using "?" as wild card for any of the parts.
     * @throws QueryEngineException when query is empty or doesn't have 3 space-delimited parts
     */
    public void executeQuery(String query) throws QueryEngineException{

        if (query.isEmpty()){
            throw new QueryEngineException("Query string:'" + query + "' must be non-empty");
        }

        //Trim the whitespace and drop the final '.' if exists
        String trimmedQuery = query.trim().replace(".", "");

        //Break the query into space-delimited parts. If not 3 parts, throw error.
        String[] queryParts = trimmedQuery.split(" ");
        if (queryParts.length != 3){
            throw new QueryEngineException("Query string:'" + query + "' must contain 3 space delmited words");
        }

        HashSet<Triple> result = KnowledgeGraph.getInstance().executeQuery(queryParts[0], queryParts[1], queryParts[2]);
        System.out.println("Query: " + query);
        if (result.isEmpty()){
            System.out.println("\tNO RESULTS FOUND");
        }else{
            result.forEach((triple)->System.out.println("\t" + triple.getIdentifier()));
        }

    }


    /**
     *
     * Iterates a query file, passing each line to execute query
     *
     * @param fileName fully qualified query filename
     * @throws QueryEngineException when filename can't be found
     */
    public void executeQueryFile(String fileName) throws QueryEngineException{

        System.out.println("\nQUERYING from file: '" + fileName + "'");


        //Read file, throw exception if file not found
        List<String> queries;
        try {
            Path path = Paths.get(fileName);
            queries = Files.readAllLines(path, Charset.forName("UTF-8"));
        }catch(InvalidPathException invalidPath){
            System.out.println("Invalid path");
            throw new QueryEngineException(invalidPath.getReason());
        }catch(IOException ioe){
            System.out.println("IO Exception");
            throw new QueryEngineException(ioe.getLocalizedMessage());
        }catch(SecurityException se){
            System.out.println("Security exception");
            throw new QueryEngineException(se.getLocalizedMessage());
        }catch(Exception e){
            throw new QueryEngineException(e.getLocalizedMessage());
        }

        if (queries.isEmpty()){
            System.out.println("Empty query file received.");
        }else{
            String[] queryStrings = queries.toArray(new String[queries.size()]);
            Integer cnt = queryStrings.length;
            Integer i;
            for (i=0;i < cnt;i++){
                executeQuery(queryStrings[i]);
            }
        }


    }




}


