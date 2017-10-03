package cscie97.asn2.housemate.model;

import com.sun.deploy.util.StringUtils;
import com.sun.tools.internal.ws.wsdl.document.Import;
import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.QueryEngineException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandProcessor {


    //Singleton methods
    private static CommandProcessor ourInstance = new CommandProcessor();
    public static CommandProcessor getInstance() {
        return ourInstance;
    }

    //Private constructor, prevents duplicates
    private CommandProcessor() {
    }


    /**
     * Processes a housemate model service command script file.
     *
     * @param filename
     * @throws ImportException
     */
    public static void processCommandFile(String filename) throws Exception {

        System.out.println("\nExecuting Script file: '" + filename + "'");

        //Read file, throw exception if file not found
        List<String> commands;
        try {
            Path path = Paths.get(filename);
            commands = Files.readAllLines(path, Charset.forName("UTF-8"));
        }catch(InvalidPathException invalidPath){
            System.out.println("Invalid path");
            throw new Exception(invalidPath.getReason());
        }catch(IOException ioe){
            System.out.println("IO Exception");
            throw new Exception(ioe.getLocalizedMessage());
        }catch(SecurityException se){
            System.out.println("Security exception");
            throw new Exception(se.getLocalizedMessage());
        }catch(Exception e){
            throw new Exception(e.getLocalizedMessage());
        }

        processCommandList(commands);

    }


    /**
     * Processes a list of House Mate Model Service Commands
     *
     * @param commands
     */
    public static void processCommandList(List<String> commands) throws
            UnauthorizedException,
            QueryEngineException,
            ImportException,
            InvalidDeviceStateValueException,
            UnsupportedFeatureException,
            ItemNotFoundException,
            ItemExistsException
    {


        if (commands.isEmpty()){

            System.out.println("Empty command input received.");

        }else{

            HouseMateModelService service = HouseMateModelServiceImpl.getInstance();

            Iterator<String> commandIterator = commands.iterator();
            while(commandIterator.hasNext()){

                String nextCommand = commandIterator.next();

                //Trim the white space
                String trimmedCommand = nextCommand.trim();

                //Create a matcher
                Matcher m = null;

                //Add Setting
                Pattern createSetting = Pattern.compile("define device setting (.*) type (.*)");
                m = createSetting.matcher(trimmedCommand);
                if (m.matches()){
                    List<String> result = service.createSetting("1",m.group(1), m.group(2));
                    System.out.println(StringUtils.join(result, "\n"));
                    continue;
                }

                //Add Measure
                Pattern createMeasure = Pattern.compile("define device measure (.*) type (.*)");
                m = createMeasure.matcher(trimmedCommand);
                if (m.matches()){
                    List<String> result = service.createMeasure("1",m.group(1), m.group(2));
                    System.out.println(StringUtils.join(result, "\n"));
                    continue;
                }

                //Define House
                Pattern defineHouse = Pattern.compile("define house (.*) address (.*) floors (\\d)");
                m = defineHouse.matcher(trimmedCommand);
                if (m.matches()){
                    List<String> result = service.createHouse("1",m.group(1), m.group(2), m.group(3));
                    System.out.println(StringUtils.join(result, "\n"));
                    continue;
                }

                //Define room
                Pattern defineRoom = Pattern.compile("define room (.*) house (.*) floor (\\d)");
                m = defineRoom.matcher(trimmedCommand);
                if (m.matches()){
                    List<String> result = service.createRoom("1",m.group(2), m.group(1), m.group(3));
                    System.out.println(StringUtils.join(result, "\n"));
                    continue;
                }

                //Define sensor
                Pattern defineSensor = Pattern.compile("define sensor (.*) type (.*) room (.*)");
                m = defineSensor.matcher(trimmedCommand);
                if (m.matches()){
                    List<String> result = service.createDevice("1", m.group(3), m.group(1), m.group(2));
                    System.out.println(StringUtils.join(result, "\n"));
                    continue;
                }

                //Define appliance
                Pattern defineAppliance = Pattern.compile("define appliance (.*) type (.*) room (.*)");
                m = defineAppliance.matcher(trimmedCommand);
                if (m.matches()){
                    List<String> result = service.createDevice("1", m.group(3), m.group(1), m.group(2));
                    System.out.println(StringUtils.join(result, "\n"));
                    continue;
                }

                //Register Feature on Device
                Pattern addFeature = Pattern.compile("add feature (.*) device (.*)");
                m = addFeature.matcher(trimmedCommand);
                if (m.matches()){
                    List<String> result = service.addFeature("1", m.group(2), m.group(1));
                    System.out.println(StringUtils.join(result, "\n"));
                    continue;
                }

                //Define occupant
                Pattern defineOccupant = Pattern.compile("define occupant (.*) name (.*) type (.*)");
                m = defineOccupant.matcher(trimmedCommand);
                if (m.matches()){
                    List<String> result = service.createOccupant("1", m.group(1), m.group(2), m.group(3));
                    System.out.println(StringUtils.join(result, "\n"));
                    continue;
                }

                //Add occupant to house
                Pattern addOccupantToHouse = Pattern.compile("add occupant (.*) house (.*)");
                m = addOccupantToHouse.matcher(trimmedCommand);
                if (m.matches()){
                    List<String> result = service.addOccupantToHouse("1", m.group(1), m.group(2));
                    System.out.println(StringUtils.join(result, "\n"));
                    continue;
                }

                //Move occupant to room
                Pattern moveOccupant = Pattern.compile("move occupant (.*) room (.*)");
                m = moveOccupant.matcher(trimmedCommand);
                if (m.matches()){
                    List<String> result = service.moveOccupant("1", m.group(1), m.group(2));
                    System.out.println(StringUtils.join(result, "\n"));
                    continue;
                }

                //Set sensor or appliance value
                Pattern setDeviceValue = Pattern.compile("set (sensor|appliance) (.*) status (.*) value (.*)");
                m = setDeviceValue.matcher(trimmedCommand);
                if (m.matches()){
                    List<String> result = service.updateFeature("1", m.group(2), m.group(3), m.group(4));
                    System.out.println(StringUtils.join(result, "\n"));
                    continue;
                }

                //Get sensor or appliance value
                Pattern getDeviceValue = Pattern.compile("show (sensor|appliance) (.*) status (.*)");
                m = getDeviceValue.matcher(trimmedCommand);
                if (m.matches()){
                    List<String> result = service.getFeature("1", m.group(2), m.group(3));
                    System.out.println(StringUtils.join(result, "\n"));
                    continue;
                }

                //Get sensor or appliance value
                Pattern getDevice = Pattern.compile("show (sensor|appliance) (.*)");
                m = getDevice.matcher(trimmedCommand);
                if (m.matches()){
                    List<String> result = service.getDevice("1", m.group(2));
                    System.out.println(StringUtils.join(result, "\n"));
                    continue;
                }

                //Get house
                Pattern getHouse = Pattern.compile("show configuration house (.*)");
                m = getHouse.matcher(trimmedCommand);
                if (m.matches()){
                    List<String> result = service.getHouse("1", m.group(1));
                    System.out.println(StringUtils.join(result, "\n"));
                    continue;
                }

                //Get room
                Pattern getRoom = Pattern.compile("show configuration room (.*)");
                m = getRoom.matcher(trimmedCommand);
                if (m.matches()){
                    List<String> result = service.getRoom("1", m.group(1));
                    System.out.println(StringUtils.join(result, "\n"));
                    continue;
                }


                //Get all
                Pattern getAll = Pattern.compile("show configuration");
                m = getAll.matcher(trimmedCommand);
                if (m.matches()){
                    List<String> result = service.getAll("1");
                    System.out.println(StringUtils.join(result, "\n"));
                    continue;
                }




            }
        }

    }

}
