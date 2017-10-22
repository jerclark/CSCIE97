package cscie97.asn3.housemate.test;
import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.QueryEngineException;
import cscie97.asn2.housemate.model.*;
import cscie97.asn3.housemate.controller.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.function.Predicate;
import static cscie97.asn3.housemate.controller.ConfigItemType.CONFIG_ITEM_TYPE_HOUSE;
import static org.junit.jupiter.api.Assertions.*;

public class TestControllerApi {

    private final HouseMateModelService hmms = HouseMateModelServiceImpl.getInstance();
    private final HouseMateControllerService hmcs = HouseMateControllerServiceImpl.getInstance();


    @BeforeEach
    public void setUp(){
        TestBootstrapper.setUp();
    }

    @AfterEach
    public void tearDown(){
        TestBootstrapper.tearDown();
    }

    @Test
    public void testCreateControllerItems() {
        try {
            hmcs.createRule("1", "TestRule");
            hmcs.createCommand("1", "TestMOC", "var r = {'occupantId':'clarkjj', 'roomFqn':'H1:R1'};r;", ConfigItemCommandType.MOVE_OCCUPANT_COMMAND);
            hmcs.createCommand("1", "TestFUC", "var r = {'targetDeviceFqn':'clarkjj', 'targetDeviceStateFqn':'H1:R1', 'targetValue':'1'};r;", ConfigItemCommandType.FEATURE_UPDATE_COMMAND);
            hmcs.createPredicate("1", "TestPredicate", "true;");
            hmcs.createContext("1", "SomeContext", CONFIG_ITEM_TYPE_HOUSE, "H1");
            assertTrue(hmcs.getRuleNames("1").contains("TestRule"));
            assertTrue(hmcs.getCommandNames("1").contains("TestMOC"));
            assertTrue(hmcs.getCommandNames("1").contains("TestFUC"));
            assertTrue(hmcs.getPredicateNames("1").contains("TestPredicate"));
            assertTrue(hmcs.getContextNames("1").contains("SomeContext"));
        } catch (UnauthorizedControllerException e) {
            e.printStackTrace();
        } catch (ControllerNamedItemExistsException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddAndRemoveRuleItems() {
        try {
            Rule r = hmcs.createRule("1", "TestRule2");
            Command moc = hmcs.createCommand("1", "TestMOC2", "var r = {'occupantId':'clarkjj', 'roomFqn':'H1:R1'};r;", ConfigItemCommandType.MOVE_OCCUPANT_COMMAND);
            Command fuc = hmcs.createCommand("1", "TestFUC2", "var r = {'targetDeviceFqn':'clarkjj', 'targetDeviceStateFqn':'H1:R1', 'targetValue':'1'};r;", ConfigItemCommandType.FEATURE_UPDATE_COMMAND);
            Predicate p = hmcs.createPredicate("1", "TestPredicate2", "true;");
            Context c = hmcs.createContext("1", "SomeContext2", CONFIG_ITEM_TYPE_HOUSE, "H1");
            assertTrue(hmcs.getRuleNames("1").contains("TestRule2"));
            assertTrue(hmcs.getCommandNames("1").contains("TestMOC2"));
            assertTrue(hmcs.getCommandNames("1").contains("TestFUC2"));
            assertTrue(hmcs.getPredicateNames("1").contains("TestPredicate2"));
            assertTrue(hmcs.getContextNames("1").contains("SomeContext2"));
            hmcs.addCommandToRule("1", "TestMOC2", "TestRule2");
            hmcs.addCommandToRule("1", "TestFUC2", "TestRule2");
            hmcs.addPredicateToRule("1", "TestPredicate2", "TestRule2");
            hmcs.addContextToRule("1", "SomeContext2", "TestRule2");
            assertTrue(r.getCommands().values().contains(moc));
            assertTrue(r.getCommands().values().contains(fuc));
            assertTrue(r.getPredicates().values().contains(p));
            assertTrue(r.getContexts().values().contains(c));
            hmcs.removeCommandFromRule("1", "TestMOC2", "TestRule2");
            hmcs.removeCommandFromRule("1", "TestFUC2", "TestRule2");
            hmcs.removePredicateFromRule("1", "TestPredicate2", "TestRule2");
            hmcs.removeContextFromRule("1", "SomeContext2", "TestRule2");
            assertFalse(r.getCommands().values().contains(moc));
            assertFalse(r.getCommands().values().contains(fuc));
            assertFalse(r.getPredicates().values().contains(p));
            assertFalse(r.getContexts().values().contains(c));
        } catch (UnauthorizedControllerException e) {
            e.printStackTrace();
        } catch (ControllerNamedItemExistsException e) {
            e.printStackTrace();
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testAttachDetachRules() {
        try {
            Rule r = hmcs.createRule("1", "TestRule3");
            Command moc = hmcs.createCommand("1", "MoveClarkJJToLivingRoom", "var r = {'occupantId':'clarkjj', 'roomFqn':'House1:LivingRoom'};r;", ConfigItemCommandType.MOVE_OCCUPANT_COMMAND);
            Command fuc = hmcs.createCommand("1", "BumpThermostatBy5", "var r = {'targetDeviceFqn':'House1:LivingRoom:Thermostat', 'targetDeviceStateFqn':'Setting:TargetTemp', 'targetValue':parseFloat(context['House1:LivingRoom:Thermostat:Measure:Temperature'].is_detecting) + (5.0)};r;", ConfigItemCommandType.FEATURE_UPDATE_COMMAND);
            Predicate p = hmcs.createPredicate("1", "CheckThermostatTempLessThan85", "var r = context['House1:LivingRoom:Thermostat:Measure:Temperature'].is_detecting < 85.0; r;");
            Context c = hmcs.createContext("1", "FullHouseContext", CONFIG_ITEM_TYPE_HOUSE, "House1");
            hmcs.addCommandToRule("1", "MoveClarkJJToLivingRoom", "TestRule3");
            hmcs.addCommandToRule("1", "BumpThermostatBy5", "TestRule3");
            hmcs.addPredicateToRule("1", "CheckThermostatTempLessThan85", "TestRule3");
            hmcs.addContextToRule("1", "FullHouseContext", "TestRule3");
            hmcs.attachRuleToFeature("1", "TestRule3", "House1:LivingRoom:Thermostat:Measure:Temperature");
            hmms.updateFeature("1", "House1:LivingRoom:Thermostat", "Measure:Temperature", "75");
            assertTrue(hmms.getOccupant("1", "Occupant:clarkjj").contains("Occupant:clarkjj is_in_room House1:LivingRoom"));
            assertEquals(hmms.getFeature("1", "House1:LivingRoom:Thermostat:Setting:TargetTemp").get(0), "House1:LivingRoom:Thermostat:Setting:TargetTemp is_set_to 80.0");
            hmcs.detachRuleFromFeature("1", "TestRule3", "House1:LivingRoom:Thermostat:Measure:Temperature");
            hmms.updateFeature("1", "House1:LivingRoom:Thermostat", "Measure:Temperature", "77");
            assertEquals(hmms.getFeature("1", "House1:LivingRoom:Thermostat:Setting:TargetTemp").get(0), "House1:LivingRoom:Thermostat:Setting:TargetTemp is_set_to 80.0");
        } catch (UnauthorizedControllerException e) {
            e.printStackTrace();
        } catch (ControllerNamedItemExistsException e) {
            e.printStackTrace();
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidDeviceStateValueException e) {
            e.printStackTrace();
        } catch (ImportException e) {
            e.printStackTrace();
        } catch (UnauthorizedException e) {
            e.printStackTrace();
        } catch (QueryEngineException e) {
            e.printStackTrace();
        } catch (UnsupportedFeatureException e) {
            e.printStackTrace();
        }
    }


}
