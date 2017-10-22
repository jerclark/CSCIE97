package cscie97.asn3.housemate.test;

import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.QueryEngineException;
import cscie97.asn2.housemate.model.CommandProcessor;
import cscie97.asn2.housemate.model.HouseMateModelService;
import cscie97.asn2.housemate.model.HouseMateModelServiceImpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TestDriver {

    public static void main(String[] args) {

        if (args.length == 0){
            System.out.println("Please pass a housemate script file name as a program argument.");
            return;
        }

        //Get the triple file fully qualified path
        String scriptFile = args[0];
        URL scriptFilePath = cscie97.asn3.housemate.test.TestDriver.class.getResource(scriptFile);
        if (scriptFilePath == null){
            System.out.println("Missing input file: " + scriptFile);
            return;
        }
        String scriptFileString;
        try{
            scriptFileString = URLDecoder.decode(scriptFilePath.getPath(), "UTF-8");
        }catch(UnsupportedEncodingException e){
            System.out.println(e);
            return;
        }

        try {
            CommandProcessor.processCommandFile(scriptFileString);
        }catch (Exception e){
            System.err.println(e);
        }



    }


//    /**
//     * Processes a housemate model service command script file.
//     *
//     * @param filename
//     * @throws ImportException
//     */
//    public static void processCommandFile(String filename) throws Exception {
//
//        System.out.println("\nIMPORTING from file: '" + filename + "'");
//
//        //Read file, throw exception if file not found
//        List<String> commands;
//        try {
//            Path path = Paths.get(filename);
//            commands = Files.readAllLines(path, Charset.forName("UTF-8"));
//        }catch(InvalidPathException invalidPath){
//            System.out.println("Invalid path");
//            throw new Exception(invalidPath.getReason());
//        }catch(IOException ioe){
//            System.out.println("IO Exception");
//            throw new Exception(ioe.getLocalizedMessage());
//        }catch(SecurityException se){
//            System.out.println("Security exception");
//            throw new Exception(se.getLocalizedMessage());
//        }catch(Exception e){
//            throw new Exception(e.getLocalizedMessage());
//        }
//
//        processCommandList(commands);
//
//    }
//
//
//    /**
//     * Processes a list of House Mate Model Service Commands
//     *
//     * @param commands
//     */
//    public static void processCommandList(List<String> commands) throws Exception, QueryEngineException {
//
//
//        if (commands.isEmpty()){
//
//            System.out.println("Empty script input received.");
//
//        }else{
//
//            HouseMateModelService service = HouseMateModelServiceImpl.getInstance();
//
//            Iterator<String> commandIterator = commands.iterator();
//            while(commandIterator.hasNext()){
//
//                String nextCommand = commandIterator.next();
//
//                //Trim the white space
//                String trimmedCommand = nextCommand.trim();
//
//                //Define House
//                Pattern defineHouse = Pattern.compile("define house (.*) address (.*) floors (\\d)");
//                Matcher m = defineHouse.matcher(trimmedCommand);
//                if (m.matches()){
//                    List<String> result = service.createHouse("1",m.group(1), m.group(2), m.group(3));
//                    System.out.println(result.stream().collect(Collectors.joining("\n")));
//                }
//
//            }
//        }
//
//    }

}
