package cscie97.asn2.housemate.model;
import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.QueryEngineException;
import cscie97.asn3.housemate.controller.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * Processes command line inputs to call API functions
 */
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
     *
     * Supported commands are of the form:
     * "define setting (.*) type (.*)"
     * "define measure (.*) type (.*)"
     * "define house (.*) address (.*) floors (\\d)"
     * "define room (.*) house (.*) floor (\\d)"
     * "define sensor (.*) type (.*) room (.*)"
     * "define appliance (.*) type (.*) room (.*)"
     * "add feature (.*) device (.*)"
     * "define occupant (.*) name (.*) type (.*)"
     * "move occupant (.*) room (.*)"
     * "set (sensor|appliance) (.*) status (.*) value (.*)"
     * "show (sensor|appliance) (.*) status (.*)"
     * "show (sensor|appliance) (.*)"
     * "show configuration house (.*)"
     * "show configuration room (.*)"
     * "show configuration"
     */
    public static void processCommandList(List<String> commands) throws
            UnauthorizedException,
            QueryEngineException,
            ImportException,
            InvalidDeviceStateValueException,
            UnsupportedFeatureException,
            ItemNotFoundException,
            ItemExistsException,
            UnauthorizedControllerException,
            ControllerNamedItemExistsException {


        if (commands.isEmpty()){

            System.out.println("Empty command input received.");

        }else{

            HouseMateModelService service = HouseMateModelServiceImpl.getInstance();
            HouseMateControllerService controllerService = HouseMateControllerServiceImpl.getInstance();


            Iterator<String> commandIterator = commands.iterator();
            while(commandIterator.hasNext()){

                try {

                    String nextCommand = commandIterator.next();

                    //Trim the white space
                    String trimmedCommand = nextCommand.trim();

                    //Create a matcher
                    Matcher m = null;

                    if (trimmedCommand.isEmpty()) {
                        System.out.println();
                        continue;
                    }

                    //Check for slient mode
                    Boolean silentMode = false;
                    Pattern silent = Pattern.compile("^(.*) silent$");
                    m = silent.matcher(trimmedCommand);
                    if (m.matches()) {
                        silentMode = true;
                        trimmedCommand = trimmedCommand.substring(0, trimmedCommand.length() - 7);
                    }

                    //Skip comments
                    Pattern comment = Pattern.compile("^# (.*)");
                    m = comment.matcher(trimmedCommand);
                    if (m.matches()) {
                        continue;
                    }

                    //Print a message
                    Pattern consoleMsg = Pattern.compile("console (.*)");
                    m = consoleMsg.matcher(trimmedCommand);
                    if (m.matches()) {
                        if (!silentMode) System.out.println("CONSOLE: " + m.group(1));
                        continue;
                    }


                    //Add Setting
                    Pattern createSetting = Pattern.compile("define setting (.*) type (.*)");
                    m = createSetting.matcher(trimmedCommand);
                    if (m.matches()) {
                        List<String> result = service.createSetting("1", m.group(1), m.group(2));
                        if (!silentMode) System.out.println(result.stream().collect(Collectors.joining("\n")));
                        continue;
                    }

                    //Add Measure
                    Pattern createMeasure = Pattern.compile("define measure (.*) type (.*)");
                    m = createMeasure.matcher(trimmedCommand);
                    if (m.matches()) {
                        List<String> result = service.createMeasure("1", m.group(1), m.group(2));
                        if (!silentMode) System.out.println(result.stream().collect(Collectors.joining("\n")));
                        continue;
                    }

                    //Define House
                    Pattern defineHouse = Pattern.compile("define house (.*) address (.*) floors (\\d)");
                    m = defineHouse.matcher(trimmedCommand);
                    if (m.matches()) {
                        List<String> result = service.createHouse("1", m.group(1), m.group(2), m.group(3));
                        if (!silentMode) System.out.println(result.stream().collect(Collectors.joining("\n")));
                        continue;
                    }

                    //Define room
                    Pattern defineRoom = Pattern.compile("define room (.*) house (.*) floor (\\d) window_count (\\d)");
                    m = defineRoom.matcher(trimmedCommand);
                    if (m.matches()) {
                        List<String> result = service.createRoom("1", m.group(2), m.group(1), m.group(3), m.group(4));
                        if (!silentMode) System.out.println(result.stream().collect(Collectors.joining("\n")));
                        continue;
                    }

                    //Define sensor
                    Pattern defineSensor = Pattern.compile("define sensor (.*) type (.*) room (.*)");
                    m = defineSensor.matcher(trimmedCommand);
                    if (m.matches()) {
                        List<String> result = service.createDevice("1", m.group(3), m.group(1), m.group(2));
                        if (!silentMode) System.out.println(result.stream().collect(Collectors.joining("\n")));
                        continue;
                    }

                    //Define appliance
                    Pattern defineAppliance = Pattern.compile("define appliance (.*) type (.*) room (.*)");
                    m = defineAppliance.matcher(trimmedCommand);
                    if (m.matches()) {
                        List<String> result = service.createDevice("1", m.group(3), m.group(1), m.group(2));
                        if (!silentMode) System.out.println(result.stream().collect(Collectors.joining("\n")));
                        continue;
                    }

                    //Register Feature on Device
                    Pattern addFeature = Pattern.compile("add feature (.*) device (.*)");
                    m = addFeature.matcher(trimmedCommand);
                    if (m.matches()) {
                        List<String> result = service.addFeature("1", m.group(2), m.group(1));
                        if (!silentMode) System.out.println(result.stream().collect(Collectors.joining("\n")));
                        continue;
                    }

                    //Define occupant
                    Pattern defineOccupant = Pattern.compile("define occupant (.*) name (.*) type (.*)");
                    m = defineOccupant.matcher(trimmedCommand);
                    if (m.matches()) {
                        List<String> result = service.createOccupant("1", m.group(1), m.group(2), m.group(3));
                        if (!silentMode) System.out.println(result.stream().collect(Collectors.joining("\n")));
                        continue;
                    }

                    //Add occupant to house
                    Pattern addOccupantToHouse = Pattern.compile("add occupant (.*) house (.*)");
                    m = addOccupantToHouse.matcher(trimmedCommand);
                    if (m.matches()) {
                        List<String> result = service.addOccupantToHouse("1", m.group(1), m.group(2));
                        if (!silentMode) System.out.println(result.stream().collect(Collectors.joining("\n")));
                        continue;
                    }

                    //Move occupant to room
                    Pattern moveOccupant = Pattern.compile("move occupant (.*) room (.*)");
                    m = moveOccupant.matcher(trimmedCommand);
                    if (m.matches()) {
                        List<String> result = service.moveOccupant("1", m.group(1), m.group(2));
                        System.out.println(result.stream().collect(Collectors.joining("\n")));
                        continue;
                    }

                    //Set sensor or appliance value
                    Pattern setDeviceValue = Pattern.compile("set (sensor|appliance) (.*) status (.*) value (.*)");
                    m = setDeviceValue.matcher(trimmedCommand);
                    if (m.matches()) {
                        List<String> result = service.updateFeature("1", m.group(2), m.group(3), m.group(4));
                        if (!silentMode) System.out.println(result.stream().collect(Collectors.joining("\n")));
                        continue;
                    }

                    //Get sensor or appliance value
                    Pattern getDeviceValue = Pattern.compile("show (sensor|appliance) (.*) status (.*)");
                    m = getDeviceValue.matcher(trimmedCommand);
                    if (m.matches()) {
                        List<String> result = service.getFeature("1", m.group(2), m.group(3));
                        if (!silentMode) System.out.println(result.stream().collect(Collectors.joining("\n")));
                        continue;
                    }

                    //Get sensor or appliance value
                    Pattern getDevice = Pattern.compile("show (sensor|appliance) (.*)");
                    m = getDevice.matcher(trimmedCommand);
                    if (m.matches()) {
                        List<String> result = service.getDevice("1", m.group(2));
                        if (!silentMode) System.out.println(result.stream().collect(Collectors.joining("\n")));
                        continue;
                    }

                    //Get house
                    Pattern getHouse = Pattern.compile("show configuration house (.*)");
                    m = getHouse.matcher(trimmedCommand);
                    if (m.matches()) {
                        List<String> result = service.getHouse("1", m.group(1));
                        if (!silentMode) System.out.println(result.stream().collect(Collectors.joining("\n")));
                        continue;
                    }

                    //Get room
                    Pattern getRoom = Pattern.compile("show configuration room (.*)");
                    m = getRoom.matcher(trimmedCommand);
                    if (m.matches()) {
                        List<String> result = service.getRoom("1", m.group(1));
                        if (!silentMode) System.out.println(result.stream().collect(Collectors.joining("\n")));
                        continue;
                    }

                    //Get occupant
                    Pattern getOccupant = Pattern.compile("show configuration occupant (.*)");
                    m = getOccupant.matcher(trimmedCommand);
                    if (m.matches()) {
                        List<String> result = service.getOccupant("1", m.group(1));
                        if (!silentMode) System.out.println(result.stream().collect(Collectors.joining("\n")));
                        continue;
                    }


                    //Get all
                    Pattern getAll = Pattern.compile("show configuration");
                    m = getAll.matcher(trimmedCommand);
                    if (m.matches()) {
                        List<String> result = service.getAll("1");
                        if (!silentMode) System.out.println(result.stream().collect(Collectors.joining("\n")));
                        continue;
                    }


                    //Subscribe to a feature
                    Pattern subscribeToFeature = Pattern.compile("subscribe feature (.*) rule (.*)");
                    m = subscribeToFeature.matcher(trimmedCommand);
                    if (m.matches()) {
                        Rule targetRule = controllerService.getRule("1", m.group(2));
                        service.subscribeToFeature("1", m.group(1), targetRule);
                        if (!silentMode) System.out.println("Rule '" + targetRule.getName() + "' subscribed to feature: '" + m.group(1));
                        continue;
                    }

                    //Subscribe to a feature
                    Pattern unsubscribeFromFeature = Pattern.compile("unsubscribe feature (.*) rule (.*)");
                    m = unsubscribeFromFeature.matcher(trimmedCommand);
                    if (m.matches()) {
                        Rule targetRule = controllerService.getRule("1", m.group(2));
                        service.unsubscribeFromFeature("1", m.group(1), targetRule);
                        if (!silentMode) System.out.println("Rule '" + targetRule.getName() + "' unsubscribed from feature: '" + m.group(1));
                        continue;
                    }


                    //Add Rule
                    Pattern createRule = Pattern.compile("create rule (.*)");
                    m = createRule.matcher(trimmedCommand);
                    if (m.matches()) {
                        Rule result = controllerService.createRule("1", m.group(1));
                        if (!silentMode) System.out.println("Created rule: " + m.group(1));
                        continue;
                    }

                    //Add Feature Update Command
                    Pattern createCommand = Pattern.compile("create feature_update_command name (.*) instructions (.*)");
                    m = createCommand.matcher(trimmedCommand);
                    if (m.matches()) {
                        Command result = controllerService.createCommand("1", m.group(1), m.group(2), ConfigItemCommandType.FEATURE_UPDATE_COMMAND);
                        if (!silentMode) System.out.println("Created command: " + m.group(1));
                        continue;
                    }


                    //Add Move Occupant Command
                    Pattern createMoveCommand = Pattern.compile("create move_occupant_command name (.*) instructions (.*)");
                    m = createMoveCommand.matcher(trimmedCommand);
                    if (m.matches()) {
                        Command result = controllerService.createCommand("1", m.group(1), m.group(2), ConfigItemCommandType.MOVE_OCCUPANT_COMMAND);
                        if (!silentMode) System.out.println("Created command: " + m.group(1));
                        continue;
                    }

                    //Add Set Occupant Activity Command
                    Pattern createOccupantActivityCommand = Pattern.compile("create set_occupant_activity_command name (.*) instructions (.*)");
                    m = createOccupantActivityCommand.matcher(trimmedCommand);
                    if (m.matches()) {
                        Command result = controllerService.createCommand("1", m.group(1), m.group(2), ConfigItemCommandType.SET_OCCUPANT_ACTIVITY_COMMAND);
                        if (!silentMode) System.out.println("Created command: " + m.group(1));
                        continue;
                    }


                    //Add rule Predicate
                    Pattern createPredicate = Pattern.compile("create rule_predicate name (.*) test (.*)");
                    m = createPredicate.matcher(trimmedCommand);
                    if (m.matches()) {
                        Predicate result = controllerService.createPredicate("1", m.group(1), m.group(2));
                        if (!silentMode) System.out.println("Created predicate: " + m.group(1));
                        continue;
                    }


                    //Add Context
                    Pattern createContext = Pattern.compile("create context name (.*) type (.*) fqn (.*)");
                    m = createContext.matcher(trimmedCommand);
                    if (m.matches()) {
                        String type = m.group(2);
                        ConfigItemType ciType = null;
                        switch(type){
                            case "house":
                                ciType = ConfigItemType.CONFIG_ITEM_TYPE_HOUSE;
                                break;
                            case "room":
                                ciType = ConfigItemType.CONFIG_ITEM_TYPE_ROOM;
                                break;
                            case "device":
                                ciType = ConfigItemType.CONFIG_ITEM_TYPE_DEVICE;
                                break;
                            case "feature":
                                ciType = ConfigItemType.CONFIG_ITEM_TYPE_FEATURE;
                                break;
                            case "occupant":
                                ciType = ConfigItemType.CONFIG_ITEM_TYPE_OCCUPANT;
                                break;
                            case "all":
                                ciType = ConfigItemType.CONFIG_ITEM_ALL;
                                break;
                        }
                        Context result = controllerService.createContext("1", m.group(1), ciType, m.group(3));
                        if (!silentMode) System.out.println("Created context: " + m.group(1));
                        continue;
                    }


                    //Show rule names
                    Pattern getRuleNames = Pattern.compile("show rules");
                    m = getRuleNames.matcher(trimmedCommand);
                    if (m.matches()) {
                        Set<String> result = controllerService.getRuleNames("1");
                        if (!silentMode) System.out.println(result.stream().collect(Collectors.joining("\n")));
                        continue;
                    }


                    //Show predicate names
                    Pattern getPredicateNames = Pattern.compile("show predicates");
                    m = getPredicateNames.matcher(trimmedCommand);
                    if (m.matches()) {
                        Set<String> result = controllerService.getPredicateNames("1");
                        if (!silentMode) System.out.println(result.stream().collect(Collectors.joining("\n")));
                        continue;
                    }

                    //Show command names
                    Pattern getCommandNames = Pattern.compile("show commands");
                    m = getCommandNames.matcher(trimmedCommand);
                    if (m.matches()) {
                        Set<String> result = controllerService.getCommandNames("1");
                        if (!silentMode) System.out.println(result.stream().collect(Collectors.joining("\n")));
                        continue;
                    }


                    //Show context names
                    Pattern getContextNames = Pattern.compile("show contexts");
                    m = getContextNames.matcher(trimmedCommand);
                    if (m.matches()) {
                        Set<String> result = controllerService.getContextNames("1");
                        System.out.println(result.stream().collect(Collectors.joining("\n")));
                        continue;
                    }

                    //Add command to rule
                    Pattern addCommandToRule = Pattern.compile("add command (.*) rule (.*)");
                    m = addCommandToRule.matcher(trimmedCommand);
                    if (m.matches()) {
                        controllerService.addCommandToRule("1", m.group(1), m.group(2));
                        if (!silentMode) System.out.println("Added command: '" + m.group(1) + "' to rule: '" + m.group(2) + "'");
                        continue;
                    }

                    //Add rule predicate to rule
                    Pattern addPredicateToRule = Pattern.compile("add predicate (.*) rule (.*)");
                    m = addPredicateToRule.matcher(trimmedCommand);
                    if (m.matches()) {
                        controllerService.addPredicateToRule("1", m.group(1), m.group(2));
                        if (!silentMode) System.out.println("Added predicate: '" + m.group(1) + "' to rule: '" + m.group(2) + "'");
                        continue;
                    }


                    //Add context to rule
                    Pattern addContextToRule = Pattern.compile("add context (.*) rule (.*)");
                    m = addContextToRule.matcher(trimmedCommand);
                    if (m.matches()) {
                        controllerService.addContextToRule("1", m.group(1), m.group(2));
                        if (!silentMode) System.out.println("Added context: '" + m.group(1) + "' to rule: '" + m.group(2) + "'");
                        continue;
                    }

                    //Remove command from rule
                    Pattern removeCommandFromRule = Pattern.compile("remove command (.*) rule (.*)");
                    m = removeCommandFromRule.matcher(trimmedCommand);
                    if (m.matches()) {
                        controllerService.removeCommandFromRule("1", m.group(1), m.group(2));
                        if (!silentMode) System.out.println("Removed command: '" + m.group(1) + "' to rule: '" + m.group(2) + "'");
                        continue;
                    }

                    //Remove predicate from rule
                    Pattern removePredicateFromRule = Pattern.compile("remove predicate (.*) rule (.*)");
                    m = removePredicateFromRule.matcher(trimmedCommand);
                    if (m.matches()) {
                        controllerService.removePredicateFromRule("1", m.group(1), m.group(2));
                        if (!silentMode) System.out.println("Removed predicate: '" + m.group(1) + "' to rule: '" + m.group(2) + "'");
                        continue;
                    }

                    //Remove context from rule
                    Pattern removeContextFromRule = Pattern.compile("remove context (.*) rule (.*)");
                    m = removeContextFromRule.matcher(trimmedCommand);
                    if (m.matches()) {
                        controllerService.removeContextFromRule("1", m.group(1), m.group(2));
                        if (!silentMode) System.out.println("Removed context: '" + m.group(1) + "' to rule: '" + m.group(2) + "'");
                    }



                }catch (Exception e){
                    System.err.println(e);
                }

            }
        }

    }

}
