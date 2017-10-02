package cscie97.asn2.housemate.test;
import com.sun.deploy.util.StringUtils;
import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.QueryEngineException;
import cscie97.asn2.housemate.model.*; //HouseMateModelService;

import java.nio.file.Paths;

public class TestDriver {

    public static void main(String[] args) {

        HouseMateModelService service = HouseMateModelServiceImpl.getInstance();

        //Auth
        testBadAuth(service);

        //House
        testCreateHouse(service);
        testCreateExistingHouse(service);
        testUpdateHouseName(service);

        //Room
        testAddRoom(service);

        //Devices
        testCreateDevice(service);

        //Device States
        testCreateMeasure(service);
        testCreateSetting(service);

        //Occupant
        testOccupant(service);

    }

    private static void testBadAuth(HouseMateModelService service) {

        System.out.println("TESTING BAD AUTH:");
        try {
            service.createHouse("", "MyHouse", "123 Awesome Lane", 2);
        } catch (UnauthorizedException ue) {
            System.out.println(ue);
        } catch (ItemExistsException iee) {
            System.out.println(iee);
        }

    }

    private static void testCreateHouse(HouseMateModelService service) {

        System.out.println("\nTESTING CREATE HOUSE:");
        try {
            House newHouse = service.createHouse("1", "MyHouse", "123 Awesome Lane", 2);
            System.out.println(StringUtils.join(newHouse.getState(), "\n"));
        } catch (UnauthorizedException ue) {
            System.out.println(ue);
        } catch (ItemExistsException iee) {
            System.out.println(iee);
        } catch (QueryEngineException qee){
            System.out.println(qee);
        }

    }

    private static void testCreateExistingHouse(HouseMateModelService service) {

        System.out.println("\nTESTING ITEM EXISTS HOUSE:");
        try {
            House newHouse = service.createHouse("1", "MyHouse", "123 Awesome Lane", 2);
            House newHouse2 = service.createHouse("1", "MyHouse", "123 Awesome Lane", 2);
            System.out.println(StringUtils.join(newHouse.getState(), "\n"));
        } catch (UnauthorizedException ue) {
            System.out.println(ue);
        } catch (ItemExistsException iee) {
            System.out.println(iee);
        } catch (QueryEngineException qee){
            System.out.println(qee);
        }

    }

    private static void testUpdateHouseName(HouseMateModelService service) {

        System.out.println("\nTESTING UPDATE HOUSE NAME:");
        try {
            House newHouse = service.createHouse("1", "OriginalHouse", "123 Awesome Lane", 2);
            System.out.println(StringUtils.join(newHouse.getState(), "\n"));
            newHouse.setName("UpdatedHouse");
            System.out.println(StringUtils.join(newHouse.getState(), "\n"));
        } catch (UnauthorizedException ue) {
            System.out.println(ue);
        } catch (ItemExistsException iee) {
            System.out.println(iee);
        } catch (QueryEngineException qee){
            System.out.println(qee);
        } catch (ImportException ie){
            System.out.println(ie);
        }

    }

    private static void testAddRoom(HouseMateModelService service) {

        System.out.println("\nTESTING ADD ROOM:");
        try {
            House newHouse = service.createHouse("1", "RoomHouse", "123 Awesome Lane", 2);
            Room newRoom = service.createRoom("1", newHouse.getFqn(), "LivingRoom", 1);
            System.out.println(StringUtils.join(newHouse.getState(), "\n"));
            System.out.println(StringUtils.join(newRoom.getState(), "\n"));
        } catch (UnauthorizedException ue) {
            System.out.println(ue);
        } catch (ItemExistsException iee) {
            System.out.println(iee);
        } catch (QueryEngineException qee){
            System.out.println(qee);
        }catch (ItemNotFoundException inf){
            System.out.println(inf);
        }

    }

    private static void testCreateDevice(HouseMateModelService service) {

        System.out.println("\nTESTING CREATE DEVICE:");
        House newHouse = null;
        Room newRoom = null;
        Device newDevice = null;
        try {
            newHouse = service.createHouse("1", "DeviceTestHouse", "123 Awesome Lane", 2);
            newRoom = service.createRoom("1", newHouse.getFqn(), "DeviceTestRoom", 1);
            newDevice = service.createDevice("1", newRoom.getFqn(), "DeviceTestDevice", "SmartOven");
            System.out.println(StringUtils.join(newDevice.getState(), "\n"));
            System.out.println(StringUtils.join(newRoom.getState(), "\n"));
        } catch (UnauthorizedException ue) {
            System.out.println(ue);
        } catch (ItemExistsException iee) {
            System.out.println(iee);
        } catch (QueryEngineException qee){
            System.out.println(qee);
        }catch (ItemNotFoundException inf){
            System.out.println(inf);
        }


        System.out.println("\nTESTING ADDING EXISTING DEVICE:");
        try {
            Device newDevice2 = service.createDevice("1", newRoom.getFqn(), "DeviceTestDevice", "SmartOven");
        } catch (UnauthorizedException ue) {
            System.out.println(ue);
        } catch (ItemExistsException iee) {
            System.out.println(iee);
        }catch (ItemNotFoundException inf){
            System.out.println(inf);
        }

    }

    private static void testCreateMeasure(HouseMateModelService service) {


        Measure newMeasure = null;
        House newHouse = null;
        Room newRoom = null;
        Device newDevice = null;
        Feature newFeature = null;
        try {
            System.out.println("\nTESTING CREATE MEASURE:");
            newMeasure = service.createMeasure("1", "Temperature", "Float");
            System.out.println(StringUtils.join(newMeasure.getState(), "\n"));
        } catch (UnauthorizedException ue) {
            System.out.println(ue);
        } catch (ItemExistsException iee) {
            System.out.println(iee);
        } catch (QueryEngineException qee){
            System.out.println(qee);
        }catch (ItemNotFoundException inf){
            System.out.println(inf);
        }


        System.out.println("\nTESTING CREATE EXISTING MEASURE:");
        try {
            Measure newMeasure2 = service.createMeasure("1", "Temperature", "Integer");
        } catch (UnauthorizedException ue) {
            System.out.println(ue);
        } catch (ItemExistsException iee) {
            System.out.println(iee);
        }catch (ItemNotFoundException inf){
            System.out.println(inf);
        }


        System.out.println("\nTESTING ADD MEASURE TO DEVICE:");

        try {
            newHouse = service.createHouse("1", "AddMeasureTestHouse", "123 Awesome Lane", 2);
            newRoom = service.createRoom("1", newHouse.getFqn(), "AddMeasureTestRoom", 1);
            newDevice = service.createDevice("1", newRoom.getFqn(), "AddMeasureTestDevice", "SmartOven");
            newFeature = service.addFeature("1", newDevice.getFqn(), newMeasure.getFqn());
            System.out.println(StringUtils.join(newFeature.getState(), "\n"));
        } catch (UnauthorizedException ue) {
            System.out.println(ue);
        } catch (ItemExistsException iee) {
            System.out.println(iee);
        }catch (ItemNotFoundException inf){
            System.out.println(inf);
        } catch (QueryEngineException qee) {
            System.out.println(qee);
        } catch (ImportException ie) {
            System.out.println(ie);
        }


        System.out.println("\nTESTING UPDATE MEASURE VALUE:");
        try {
            service.updateFeature("1", newDevice.getFqn(), newMeasure.getFqn(), "75");
            System.out.println(StringUtils.join(newFeature.getState(), "\n"));
        }catch (UnauthorizedException ue) {
            System.out.println(ue);
        }catch (ItemNotFoundException inf){
            System.out.println(inf);
        }catch (QueryEngineException qee) {
            System.out.println(qee);
        }catch (UnsupportedFeatureException ufe){
            System.out.println(ufe);
        }catch (ImportException ie){
            System.out.println(ie);
        }catch (InvalidDeviceStateValueException ive){
            System.out.println(ive);
        }

    }

    private static void testCreateSetting(HouseMateModelService service) {

        Setting newSetting= null;
        House newHouse = null;
        Room newRoom = null;
        Device newDevice = null;
        Feature newFeature = null;
        try {
            System.out.println("\nTESTING CREATE SETTING:");
            newSetting = service.createSetting("1", "CookType", "BAKE|TOAST|BROIL");
            System.out.println(StringUtils.join(newSetting.getState(), "\n"));
        } catch (UnauthorizedException ue) {
            System.out.println(ue);
        } catch (ItemExistsException iee) {
            System.out.println(iee);
        } catch (QueryEngineException qee){
            System.out.println(qee);
        }catch (ItemNotFoundException inf){
            System.out.println(inf);
        }


        System.out.println("\nTESTING CREATE EXISTING SETTING:");
        try {
            Setting newSetting2 = service.createSetting("1", "CookType", "Integer");
        } catch (UnauthorizedException ue) {
            System.out.println(ue);
        } catch (ItemExistsException iee) {
            System.out.println(iee);
        }catch (ItemNotFoundException inf){
            System.out.println(inf);
        }


        System.out.println("\nTESTING ADD SETTING TO DEVICE:");

        try {
            newHouse = service.createHouse("1", "AddSettingTestHouse", "123 Awesome Lane", 2);
            newRoom = service.createRoom("1", newHouse.getFqn(), "AddSettingTestRoom", 1);
            newDevice = service.createDevice("1", newRoom.getFqn(), "AddSettingTestDevice", "SmartOven");
            newFeature = service.addFeature("1", newDevice.getFqn(), newSetting.getFqn());
            System.out.println(StringUtils.join(newFeature.getState(), "\n"));
        } catch (UnauthorizedException ue) {
            System.out.println(ue);
        } catch (ItemExistsException iee) {
            System.out.println(iee);
        }catch (ItemNotFoundException inf){
            System.out.println(inf);
        } catch (QueryEngineException qee) {
            System.out.println(qee);
        }catch (ImportException ie) {
            System.out.println(ie);
        }


        System.out.println("\nTESTING UPDATE SETTING VALUE:");
        try {
            service.updateFeature("1", newDevice.getFqn(), newSetting.getFqn(), "BAKE");
            System.out.println("\nDEVICE STATE:");
            System.out.println(StringUtils.join(newDevice.getState(), "\n"));
            System.out.println("\nROOM STATE:");
            System.out.println(StringUtils.join(newRoom.getState(), "\n"));
            System.out.println("\nHOUSE STATE:");
            System.out.println(StringUtils.join(newHouse.getState(), "\n"));
            System.out.println("\nTESTING INVALID UPDATE SETTING VALUE:");
            service.updateFeature("1", newDevice.getFqn(), newSetting.getFqn(), "CONVECTION");
        }catch (UnauthorizedException ue) {
            System.out.println(ue);
        }catch (ItemNotFoundException inf){
            System.out.println(inf);
        }catch (QueryEngineException qee) {
            System.out.println(qee);
        }catch (UnsupportedFeatureException ufe){
            System.out.println(ufe);
        }catch (ImportException ie){
            System.out.println(ie);
        }catch (InvalidDeviceStateValueException ive){
            System.out.println(ive);
        }

    }

    private static void testOccupant(HouseMateModelService service) {

        House newHouse = null;
        House newHouse2 = null;
        Room newRoom = null;
        Room newRoom2 = null;
        Occupant newOccupant = null;
        try {
            System.out.println("\nTESTING CREATING OCCUPANT:");
            newOccupant = service.createOccupant("1", "jerclark", "Jeremy Clark","Adult");
            System.out.println("\nOCCUPANT STATE:");
            System.out.println(StringUtils.join(newOccupant.getState(), "\n"));
            newHouse = service.createHouse("1", "OccupantHouse", "123 Something Lane",2);
            newHouse2 = service.createHouse("1", "NEWOccupantHouse", "123 Something Lane",2);
            System.out.println("\nADDING OCCUPANT TO HOUSE:");
            service.addOccupantToHouse("1", "jerclark", newHouse.getFqn());
            System.out.println("\nOCCUPANT STATE WITH HOUSE:");
            System.out.println(StringUtils.join(newOccupant.getState(), "\n"));
            System.out.println("\nADDING OCCUPANT TO SECOND HOUSE:");
            service.addOccupantToHouse("1", "jerclark", newHouse2.getFqn());
            System.out.println("\nOCCUPANT STATE WITH TWO HOUSES:");
            System.out.println(StringUtils.join(newOccupant.getState(), "\n"));
            System.out.println("\nMOVING OCCUPANT TO ROOM:");
            newRoom = service.createRoom("1", newHouse.getFqn(), "OccupantRoom", 1);
            service.moveOccupant("1", "jerclark", newRoom.getFqn());
            System.out.println("\nOCCUPANT STATE WITH ROOM:");
            System.out.println(StringUtils.join(newOccupant.getState(), "\n"));
            System.out.println("\nMOVING OCCUPANT TO ANOTHER ROOM:");
            newRoom2 = service.createRoom("1", newHouse.getFqn(), "OccupantRoom2", 1);
            service.moveOccupant("1", "jerclark", newRoom2.getFqn());
            System.out.println("\nOCCUPANT STATE WITH NEW ROOM:");
            System.out.println(StringUtils.join(newOccupant.getState(), "\n"));
        } catch (UnauthorizedException ue) {
            System.out.println(ue);
        } catch (ItemExistsException iee) {
            System.out.println(iee);
        } catch (ItemNotFoundException inf){
            System.out.println(inf);
        }catch (ImportException ie){
            System.out.println(ie);
        }


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

    }
}
