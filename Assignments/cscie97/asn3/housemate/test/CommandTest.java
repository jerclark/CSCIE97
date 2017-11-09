package cscie97.asn3.housemate.test;

import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.QueryEngineException;
import cscie97.asn2.housemate.model.*;
import cscie97.asn3.housemate.controller.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static cscie97.asn3.housemate.controller.ConfigItemType.*;


public class CommandTest {

    private final HouseMateModelService hmms = HouseMateModelServiceImpl.getInstance();

    @BeforeEach
    public void setUp(){
        TestBootstrapper.setUp();
    }

    @AfterEach
    public void tearDown(){
        TestBootstrapper.tearDown();
    }


    @Test
    public void testFeatureUpdateCommand(){
        try {
            List<String> expected = new ArrayList<String>();
            expected.add("House1:LivingRoom:Thermostat:Measure:Temperature is_detecting 100");
            Command fuc = new FeatureUpdateCommand("var obj = {'targetDeviceFqn':'House1:LivingRoom:Thermostat', 'targetDeviceStateFqn':'Measure:Temperature', 'targetValue':'100'}; obj;");
            fuc.execute(null);
            assertEquals(expected, hmms.getFeature("1","House1:LivingRoom:Thermostat:Measure:Temperature"));
        } catch (UnauthorizedException e) {
            e.printStackTrace();
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
        } catch (QueryEngineException e) {
            e.printStackTrace();
        } catch (CommandExecuteException e) {
            e.printStackTrace();
        }
        //assertTrue(hmms.getDevice);
    }

    private class TestInvoker implements Invoker {

        public TestInvoker(){
        }

        public Context commandContext(){
            ConfigItemContext featureContext = new ConfigItemContext(CONFIG_ITEM_TYPE_FEATURE,"House1:LivingRoom:Thermostat:Measure:Temperature");
            return featureContext;
        }
    }

    @Test
    public void testMoveOccupantCommand(){
        try {
            String expected = "Occupant:clarkjj is_in_room House1:LivingRoom";
            Command fuc = new MoveOccupantCommand("var result = {'occupantId':'clarkjj', 'roomFqn':'House1:LivingRoom'}; result;");
            fuc.execute(new TestInvoker());
            assertTrue(hmms.getOccupant("1","Occupant:clarkjj").contains(expected));
        } catch (UnauthorizedException e) {
            e.printStackTrace();
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
        } catch (QueryEngineException e) {
            e.printStackTrace();
        } catch (CommandExecuteException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testSetActivityUpdateCommand(){
        try {
            String expected = "Occupant:clarkjj is_active true";
            Command fuc = new SetOccupantActivityCommand("var result = {'occupantId':'clarkjj', 'activityState':'true'}; result;");
            fuc.execute(new TestInvoker());
            assertTrue(hmms.getOccupant("1","Occupant:clarkjj").contains(expected));
        } catch (UnauthorizedException e) {
            e.printStackTrace();
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
        } catch (QueryEngineException e) {
            e.printStackTrace();
        } catch (CommandExecuteException e) {
            e.printStackTrace();
        }
    }



}
