package cscie97.asn4.housemate.test;

import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.QueryEngineException;
import cscie97.asn4.housemate.controller.*;
import cscie97.asn4.housemate.model.*;


import static cscie97.asn4.housemate.controller.ConfigItemType.CONFIG_ITEM_TYPE_HOUSE;

public class TestBootstrapper {

    public static void tearDown(){
        HouseMateModelService hmms = HouseMateModelServiceImpl.getInstance();
        HouseMateControllerServiceImpl hmcs = (HouseMateControllerServiceImpl)HouseMateControllerServiceImpl.getInstance();
        try {
            hmms.removeAll("1");
            hmcs.removeAll();
        } catch (UnauthorizedException e) {
            e.printStackTrace();
        }
    }

    public static void setUp(){

        HouseMateModelService hmms = HouseMateModelServiceImpl.getInstance();
        HouseMateControllerService hmcs = HouseMateControllerServiceImpl.getInstance();


        try {
            hmms.createHouse("1", "House1", "123TerdSt.", "3");
            hmms.createRoom("1", "House1", "LivingRoom", "1", "1");
            hmms.createDevice("1", "House1:LivingRoom", "Thermostat", "Thermostat");
            hmms.createMeasure("1", "Temperature", "Float");
            hmms.createSetting("1", "TargetTemp", "Float");
            hmms.addFeature("1", "House1:LivingRoom:Thermostat", "Measure:Temperature");
            hmms.addFeature("1", "House1:LivingRoom:Thermostat", "Setting:TargetTemp");
            hmms.updateFeature("1", "House1:LivingRoom:Thermostat", "Measure:Temperature", "75.0");
            hmms.createOccupant("1", "clarkjj", "Jeremy Clark", "Adult");
            hmcs.createRule("1", "TestRule");
            hmcs.createCommand("1", "TestMOC", "var r = {'occupantId':'clarkjj', 'roomFqn':'H1:R1'};r;", ConfigItemCommandType.MOVE_OCCUPANT_COMMAND);
            hmcs.createCommand("1", "TestFUC", "var r = {'targetDeviceFqn':'clarkjj', 'targetDeviceStateFqn':'H1:R1', 'targetValue':'1'};r;", ConfigItemCommandType.FEATURE_UPDATE_COMMAND);
            hmcs.createPredicate("1", "TestPredicate", "true;");
            hmcs.createContext("1", "SomeContext", CONFIG_ITEM_TYPE_HOUSE, "H1");
        } catch (ItemExistsException e) {
            e.printStackTrace();
        } catch (UnauthorizedException e) {
            e.printStackTrace();
        } catch (QueryEngineException e) {
            e.printStackTrace();
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
        } catch (ImportException e) {
            e.printStackTrace();
        } catch (UnsupportedFeatureException e) {
            e.printStackTrace();
        } catch (InvalidDeviceStateValueException e) {
            e.printStackTrace();
        } catch (UnauthorizedControllerException e) {
            e.printStackTrace();
        } catch (ControllerNamedItemExistsException e) {
            e.printStackTrace();
        }
    }


}
