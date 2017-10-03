package cscie97.asn1.knowledge.engine;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;


/**
 * Imports triple files
 */
public class Importer {


    public Importer() {
    }

    public void removeTripleList(HashSet<Triple> triples) throws ImportException{

        if (triples.isEmpty()) {
            //System.out.println("Empty input received.");
        }else {
            ArrayList<Triple> tripleList = new ArrayList<Triple>(triples);
            for (int i = (tripleList.size() - 1); i >= 0; i--) {
                KnowledgeGraph.getInstance().removeTriple(tripleList.get(i));
            }
        }

    }


    public void importTripleList(List<String> triples) throws ImportException{

        //Create a 'consumer' iterator to add the triple for each line
        Consumer<String> tripleAdder = (tripleString) -> {
            //Trim the white space
            tripleString.trim();

            //Prune out any items with a question mark
            if (tripleString.contains("?")){
                System.err.println("\tSkipped: '" + tripleString + "'. Triples containing '?' are disallowed");
                return;
            }

            //Trim the trailing period from the triples
            String trimmedTriple = tripleString;
            if (tripleString.endsWith(".")){
                trimmedTriple = trimmedTriple.substring(0, trimmedTriple.length());
            }

            //Break the triple into space-delimited parts
            String[] tripleParts = trimmedTriple.split(" ");

            //If not exactly 3 parts, just return.
            if (tripleParts.length != 3){
                System.err.println("\tSkipped: '" + tripleString + "'. Triples must contain 3 space delimited parts.");
                return;
            }

            //Import the triple
            KnowledgeGraph.getInstance().importTriple(tripleParts[0], tripleParts[1], tripleParts[2]);
        };

        if (triples.isEmpty()){
            //System.out.println("Empty input received.");
        }else{
            triples.forEach(tripleAdder);
        }

    }



    /**
     * Imports a file of Triple facts. Parses items of the pattern 'subject predicate object' and
     * Passes the string identifiers for each part to KnowledgeGraph.importTriple()
     *
     * @param filename
     * @throws ImportException
     */
    public void importTripleFile(String filename) throws ImportException{

        System.out.println("\nIMPORTING from file: '" + filename + "'");

        //Read file, throw exception if file not found
        List<String> lines;
        try {
            Path path = Paths.get(filename);
            lines = Files.readAllLines(path, Charset.forName("UTF-8"));
        }catch(InvalidPathException invalidPath){
            System.out.println("Invalid path");
            throw new ImportException(invalidPath.getReason());
        }catch(IOException ioe){
            System.out.println("IO Exception");
            throw new ImportException(ioe.getLocalizedMessage());
        }catch(SecurityException se){
            System.out.println("Security exception");
            throw new ImportException(se.getLocalizedMessage());
        }catch(Exception e){
            throw new ImportException(e.getLocalizedMessage());
        }

        importTripleList(lines);

    }
}


