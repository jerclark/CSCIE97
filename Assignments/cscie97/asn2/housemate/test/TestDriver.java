package cscie97.asn2.housemate.test;
import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.QueryEngineException;
import cscie97.asn2.housemate.model.*; //HouseMateModelService;

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
        URL scriptFilePath = cscie97.asn2.housemate.test.TestDriver.class.getResource(scriptFile);
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


    /**
     * Processes a housemate model service command script file.
     *
     * @param filename
     * @throws ImportException
     */
    public static void processCommandFile(String filename) throws Exception {

        System.out.println("\nIMPORTING from file: '" + filename + "'");

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
    public static void processCommandList(List<String> commands) throws Exception, QueryEngineException {


        if (commands.isEmpty()){

            System.out.println("Empty script input received.");

        }else{

            HouseMateModelService service = HouseMateModelServiceImpl.getInstance();

            Iterator<String> commandIterator = commands.iterator();
            while(commandIterator.hasNext()){

                String nextCommand = commandIterator.next();

                //Trim the white space
                String trimmedCommand = nextCommand.trim();

                //Define House
                Pattern defineHouse = Pattern.compile("define house (.*) address (.*) floors (\\d)");
                Matcher m = defineHouse.matcher(trimmedCommand);
                if (m.matches()){
                    List<String> result = service.createHouse("1",m.group(1), m.group(2), m.group(3));
                    System.out.println(result.stream().collect(Collectors.joining("\n")));
                }

            }
        }





    }






//    private static void testBadAuth(HouseMateModelService service) {
//
//        System.out.println("TESTING BAD AUTH:");
//        try {
//            service.createHouse("", "MyHouse", "123 Awesome Lane", 2);
//        } catch (UnauthorizedException ue) {
//            System.out.println(ue);
//        } catch (ItemExistsException iee) {
//            System.out.println(iee);
//        }
//
//    }
//
//    private static void testCreateHouse(HouseMateModelService service) {
//
//        System.out.println("\nTESTING CREATE HOUSE:");
//        try {
//            House newHouse = service.createHouse("1", "MyHouse", "123 Awesome Lane", 2);
//            System.out.println(StringUtils.join(newHouse.getState(), "\n"));
//        } catch (UnauthorizedException ue) {
//            System.out.println(ue);
//        } catch (ItemExistsException iee) {
//            System.out.println(iee);
//        } catch (QueryEngineException qee){
//            System.out.println(qee);
//        }
//
//    }
//
//    private static void testCreateExistingHouse(HouseMateModelService service) {
//
//        System.out.println("\nTESTING ITEM EXISTS HOUSE:");
//        try {
//            House newHouse = service.createHouse("1", "MyHouse", "123 Awesome Lane", 2);
//            House newHouse2 = service.createHouse("1", "MyHouse", "123 Awesome Lane", 2);
//            System.out.println(StringUtils.join(newHouse.getState(), "\n"));
//        } catch (UnauthorizedException ue) {
//            System.out.println(ue);
//        } catch (ItemExistsException iee) {
//            System.out.println(iee);
//        } catch (QueryEngineException qee){
//            System.out.println(qee);
//        }
//
//    }
//
//    private static void testUpdateHouseName(HouseMateModelService service) {
//
//        System.out.println("\nTESTING UPDATE HOUSE NAME:");
//        try {
//            House newHouse = service.createHouse("1", "OriginalHouse", "123 Awesome Lane", 2);
//            System.out.println(StringUtils.join(newHouse.getState(), "\n"));
//            newHouse.setName("UpdatedHouse");
//            System.out.println(StringUtils.join(newHouse.getState(), "\n"));
//        } catch (UnauthorizedException ue) {
//            System.out.println(ue);
//        } catch (ItemExistsException iee) {
//            System.out.println(iee);
//        } catch (QueryEngineException qee){
//            System.out.println(qee);
//        } catch (ImportException ie){
//            System.out.println(ie);
//        }
//
//    }
//
//    private static void testAddRoom(HouseMateModelService service) {
//
//        System.out.println("\nTESTING ADD ROOM:");
//        try {
//            House newHouse = service.createHouse("1", "RoomHouse", "123 Awesome Lane", 2);
//            Room newRoom = service.createRoom("1", newHouse.getFqn(), "LivingRoom", 1);
//            System.out.println(StringUtils.join(newHouse.getState(), "\n"));
//            System.out.println(StringUtils.join(newRoom.getState(), "\n"));
//        } catch (UnauthorizedException ue) {
//            System.out.println(ue);
//        } catch (ItemExistsException iee) {
//            System.out.println(iee);
//        } catch (QueryEngineException qee){
//            System.out.println(qee);
//        }catch (ItemNotFoundException inf){
//            System.out.println(inf);
//        }
//
//    }
//
//    private static void testCreateDevice(HouseMateModelService service) {
//
//        System.out.println("\nTESTING CREATE DEVICE:");
//        House newHouse = null;
//        Room newRoom = null;
//        Device newDevice = null;
//        try {
//            newHouse = service.createHouse("1", "DeviceTestHouse", "123 Awesome Lane", 2);
//            newRoom = service.createRoom("1", newHouse.getFqn(), "DeviceTestRoom", 1);
//            newDevice = service.createDevice("1", newRoom.getFqn(), "DeviceTestDevice", "SmartOven");
//            System.out.println(StringUtils.join(newDevice.getState(), "\n"));
//            System.out.println(StringUtils.join(newRoom.getState(), "\n"));
//        } catch (UnauthorizedException ue) {
//            System.out.println(ue);
//        } catch (ItemExistsException iee) {
//            System.out.println(iee);
//        } catch (QueryEngineException qee){
//            System.out.println(qee);
//        }catch (ItemNotFoundException inf){
//            System.out.println(inf);
//        }
//
//
//        System.out.println("\nTESTING ADDING EXISTING DEVICE:");
//        try {
//            Device newDevice2 = service.createDevice("1", newRoom.getFqn(), "DeviceTestDevice", "SmartOven");
//        } catch (UnauthorizedException ue) {
//            System.out.println(ue);
//        } catch (ItemExistsException iee) {
//            System.out.println(iee);
//        }catch (ItemNotFoundException inf){
//            System.out.println(inf);
//        }
//
//    }
//
//    private static void testCreateMeasure(HouseMateModelService service) {
//
//
//        Measure newMeasure = null;
//        House newHouse = null;
//        Room newRoom = null;
//        Device newDevice = null;
//        Feature newFeature = null;
//        try {
//            System.out.println("\nTESTING CREATE MEASURE:");
//            newMeasure = service.createMeasure("1", "Temperature", "Float");
//            System.out.println(StringUtils.join(newMeasure.getState(), "\n"));
//        } catch (UnauthorizedException ue) {
//            System.out.println(ue);
//        } catch (ItemExistsException iee) {
//            System.out.println(iee);
//        } catch (QueryEngineException qee){
//            System.out.println(qee);
//        }catch (ItemNotFoundException inf){
//            System.out.println(inf);
//        }
//
//
//        System.out.println("\nTESTING CREATE EXISTING MEASURE:");
//        try {
//            Measure newMeasure2 = service.createMeasure("1", "Temperature", "Integer");
//        } catch (UnauthorizedException ue) {
//            System.out.println(ue);
//        } catch (ItemExistsException iee) {
//            System.out.println(iee);
//        }catch (ItemNotFoundException inf){
//            System.out.println(inf);
//        }
//
//
//        System.out.println("\nTESTING ADD MEASURE TO DEVICE:");
//
//        try {
//            newHouse = service.createHouse("1", "AddMeasureTestHouse", "123 Awesome Lane", 2);
//            newRoom = service.createRoom("1", newHouse.getFqn(), "AddMeasureTestRoom", 1);
//            newDevice = service.createDevice("1", newRoom.getFqn(), "AddMeasureTestDevice", "SmartOven");
//            newFeature = service.addFeature("1", newDevice.getFqn(), newMeasure.getFqn());
//            System.out.println(StringUtils.join(newFeature.getState(), "\n"));
//        } catch (UnauthorizedException ue) {
//            System.out.println(ue);
//        } catch (ItemExistsException iee) {
//            System.out.println(iee);
//        }catch (ItemNotFoundException inf){
//            System.out.println(inf);
//        } catch (QueryEngineException qee) {
//            System.out.println(qee);
//        } catch (ImportException ie) {
//            System.out.println(ie);
//        }
//
//
//        System.out.println("\nTESTING UPDATE MEASURE VALUE:");
//        try {
//            service.updateFeature("1", newDevice.getFqn(), newMeasure.getFqn(), "75");
//            System.out.println(StringUtils.join(newFeature.getState(), "\n"));
//        }catch (UnauthorizedException ue) {
//            System.out.println(ue);
//        }catch (ItemNotFoundException inf){
//            System.out.println(inf);
//        }catch (QueryEngineException qee) {
//            System.out.println(qee);
//        }catch (UnsupportedFeatureException ufe){
//            System.out.println(ufe);
//        }catch (ImportException ie){
//            System.out.println(ie);
//        }catch (InvalidDeviceStateValueException ive){
//            System.out.println(ive);
//        }
//
//    }
//
//    private static void testCreateSetting(HouseMateModelService service) {
//
//        Setting newSetting= null;
//        House newHouse = null;
//        Room newRoom = null;
//        Device newDevice = null;
//        Feature newFeature = null;
//        try {
//            System.out.println("\nTESTING CREATE SETTING:");
//            newSetting = service.createSetting("1", "CookType", "BAKE|TOAST|BROIL");
//            System.out.println(StringUtils.join(newSetting.getState(), "\n"));
//        } catch (UnauthorizedException ue) {
//            System.out.println(ue);
//        } catch (ItemExistsException iee) {
//            System.out.println(iee);
//        } catch (QueryEngineException qee){
//            System.out.println(qee);
//        }catch (ItemNotFoundException inf){
//            System.out.println(inf);
//        }
//
//
//        System.out.println("\nTESTING CREATE EXISTING SETTING:");
//        try {
//            Setting newSetting2 = service.createSetting("1", "CookType", "Integer");
//        } catch (UnauthorizedException ue) {
//            System.out.println(ue);
//        } catch (ItemExistsException iee) {
//            System.out.println(iee);
//        }catch (ItemNotFoundException inf){
//            System.out.println(inf);
//        }
//
//
//        System.out.println("\nTESTING ADD SETTING TO DEVICE:");
//
//        try {
//            newHouse = service.createHouse("1", "AddSettingTestHouse", "123 Awesome Lane", 2);
//            newRoom = service.createRoom("1", newHouse.getFqn(), "AddSettingTestRoom", 1);
//            newDevice = service.createDevice("1", newRoom.getFqn(), "AddSettingTestDevice", "SmartOven");
//            newFeature = service.addFeature("1", newDevice.getFqn(), newSetting.getFqn());
//            System.out.println(StringUtils.join(newFeature.getState(), "\n"));
//        } catch (UnauthorizedException ue) {
//            System.out.println(ue);
//        } catch (ItemExistsException iee) {
//            System.out.println(iee);
//        }catch (ItemNotFoundException inf){
//            System.out.println(inf);
//        } catch (QueryEngineException qee) {
//            System.out.println(qee);
//        }catch (ImportException ie) {
//            System.out.println(ie);
//        }
//
//
//        System.out.println("\nTESTING UPDATE SETTING VALUE:");
//        try {
//            service.updateFeature("1", newDevice.getFqn(), newSetting.getFqn(), "BAKE");
//            System.out.println("\nDEVICE STATE:");
//            System.out.println(StringUtils.join(newDevice.getState(), "\n"));
//            System.out.println("\nROOM STATE:");
//            System.out.println(StringUtils.join(newRoom.getState(), "\n"));
//            System.out.println("\nHOUSE STATE:");
//            System.out.println(StringUtils.join(newHouse.getState(), "\n"));
//            System.out.println("\nTESTING INVALID UPDATE SETTING VALUE:");
//            service.updateFeature("1", newDevice.getFqn(), newSetting.getFqn(), "CONVECTION");
//        }catch (UnauthorizedException ue) {
//            System.out.println(ue);
//        }catch (ItemNotFoundException inf){
//            System.out.println(inf);
//        }catch (QueryEngineException qee) {
//            System.out.println(qee);
//        }catch (UnsupportedFeatureException ufe){
//            System.out.println(ufe);
//        }catch (ImportException ie){
//            System.out.println(ie);
//        }catch (InvalidDeviceStateValueException ive){
//            System.out.println(ive);
//        }
//
//    }
//
//    private static void testOccupant(HouseMateModelService service) {
//
//        House newHouse = null;
//        House newHouse2 = null;
//        Room newRoom = null;
//        Room newRoom2 = null;
//        Occupant newOccupant = null;
//        try {
//            System.out.println("\nTESTING CREATING OCCUPANT:");
//            newOccupant = service.createOccupant("1", "jerclark", "Jeremy Clark","Adult");
//            System.out.println("\nOCCUPANT STATE:");
//            System.out.println(StringUtils.join(newOccupant.getState(), "\n"));
//            newHouse = service.createHouse("1", "OccupantHouse", "123 Something Lane",2);
//            newHouse2 = service.createHouse("1", "NEWOccupantHouse", "123 Something Lane",2);
//            System.out.println("\nADDING OCCUPANT TO HOUSE:");
//            service.addOccupantToHouse("1", "jerclark", newHouse.getFqn());
//            System.out.println("\nOCCUPANT STATE WITH HOUSE:");
//            System.out.println(StringUtils.join(newOccupant.getState(), "\n"));
//            System.out.println("\nADDING OCCUPANT TO SECOND HOUSE:");
//            service.addOccupantToHouse("1", "jerclark", newHouse2.getFqn());
//            System.out.println("\nOCCUPANT STATE WITH TWO HOUSES:");
//            System.out.println(StringUtils.join(newOccupant.getState(), "\n"));
//            System.out.println("\nMOVING OCCUPANT TO ROOM:");
//            newRoom = service.createRoom("1", newHouse.getFqn(), "OccupantRoom", 1);
//            service.moveOccupant("1", "jerclark", newRoom.getFqn());
//            System.out.println("\nOCCUPANT STATE WITH ROOM:");
//            System.out.println(StringUtils.join(newOccupant.getState(), "\n"));
//            System.out.println("\nMOVING OCCUPANT TO ANOTHER ROOM:");
//            newRoom2 = service.createRoom("1", newHouse.getFqn(), "OccupantRoom2", 1);
//            service.moveOccupant("1", "jerclark", newRoom2.getFqn());
//            System.out.println("\nOCCUPANT STATE WITH NEW ROOM:");
//            System.out.println(StringUtils.join(newOccupant.getState(), "\n"));
//        } catch (UnauthorizedException ue) {
//            System.out.println(ue);
//        } catch (ItemExistsException iee) {
//            System.out.println(iee);
//        } catch (ItemNotFoundException inf){
//            System.out.println(inf);
//        }catch (ImportException ie){
//            System.out.println(ie);
//        }
//
//
////        System.out.println("\nTESTING CREATE EXISTING SETTING:");
////        try {
////            Setting newSetting2 = service.createSetting("1", "CookType", "Integer");
////        } catch (UnauthorizedException ue) {
////            System.out.println(ue);
////        } catch (ItemExistsException iee) {
////            System.out.println(iee);
////        }catch (ItemNotFoundException inf){
////            System.out.println(inf);
////        }
////
////
////        System.out.println("\nTESTING ADD SETTING TO DEVICE:");
////
////        try {
////            newHouse = service.createHouse("1", "AddSettingTestHouse", "123 Awesome Lane", 2);
////            newRoom = service.createRoom("1", newHouse.getFqn(), "AddSettingTestRoom", 1);
////            newDevice = service.createDevice("1", newRoom.getFqn(), "AddSettingTestDevice", "SmartOven");
////            newFeature = service.addFeature("1", newDevice.getFqn(), newSetting.getFqn());
////            System.out.println(StringUtils.join(newFeature.getState(), "\n"));
////        } catch (UnauthorizedException ue) {
////            System.out.println(ue);
////        } catch (ItemExistsException iee) {
////            System.out.println(iee);
////        }catch (ItemNotFoundException inf){
////            System.out.println(inf);
////        } catch (QueryEngineException qee) {
////            System.out.println(qee);
////        }catch (ImportException ie) {
////            System.out.println(ie);
////        }
////
////
////        System.out.println("\nTESTING UPDATE SETTING VALUE:");
////        try {
////            service.updateFeature("1", newDevice.getFqn(), newSetting.getFqn(), "BAKE");
////            System.out.println("\nDEVICE STATE:");
////            System.out.println(StringUtils.join(newDevice.getState(), "\n"));
////            System.out.println("\nROOM STATE:");
////            System.out.println(StringUtils.join(newRoom.getState(), "\n"));
////            System.out.println("\nHOUSE STATE:");
////            System.out.println(StringUtils.join(newHouse.getState(), "\n"));
////            System.out.println("\nTESTING INVALID UPDATE SETTING VALUE:");
////            service.updateFeature("1", newDevice.getFqn(), newSetting.getFqn(), "CONVECTION");
////        }catch (UnauthorizedException ue) {
////            System.out.println(ue);
////        }catch (ItemNotFoundException inf){
////            System.out.println(inf);
////        }catch (QueryEngineException qee) {
////            System.out.println(qee);
////        }catch (UnsupportedFeatureException ufe){
////            System.out.println(ufe);
////        }catch (ImportException ie){
////            System.out.println(ie);
////        }catch (InvalidDeviceStateValueException ive){
////            System.out.println(ive);
////        }
//
//    }
}
