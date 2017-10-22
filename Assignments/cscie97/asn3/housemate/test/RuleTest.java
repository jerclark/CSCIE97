package cscie97.asn3.housemate.test;

import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.QueryEngineException;
import cscie97.asn2.housemate.model.*;
import cscie97.asn3.housemate.controller.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.function.Predicate;
import static cscie97.asn3.housemate.controller.ConfigItemType.CONFIG_ITEM_TYPE_HOUSE;
import static org.junit.jupiter.api.Assertions.*;

public class RuleTest {

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
    public void testRuleMatch() {
        try {

            Rule r = new Rule("Test Rule");
            hmms.subscribeToFeature("1", "House1:LivingRoom:Thermostat:Measure:Temperature", r);

            MoveOccupantCommand moc = new MoveOccupantCommand("var result = {'occupantId':'clarkjj', 'roomFqn':'House1:LivingRoom'};result;");
            r.addCommand("TestMOC", moc);


            FeatureUpdateCommand fuc = new FeatureUpdateCommand("var result = {'targetDeviceFqn':'House1:LivingRoom:Thermostat', 'targetDeviceStateFqn':'Setting:TargetTemp', 'targetValue':Number(context['House1:LivingRoom:Thermostat:Measure:Temperature'].is_detecting) + (10)};result;");
            r.addCommand("TestFUC", fuc);

            ConfigItemContext featureContext = new ConfigItemContext(CONFIG_ITEM_TYPE_HOUSE,"House1");
            r.addContext("Test", featureContext);

            Predicate<Context> p1 = PredicateCreator.createJSPredicate("context['House1:LivingRoom:Thermostat:Measure:Temperature'].is_detecting == 101");
            r.addPredicate("TempEquals101", p1);

            //Now update the feature, to trigger the rule, and test that the rule works
            hmms.updateFeature("1", "House1:LivingRoom:Thermostat", "Measure:Temperature", "101");
            assertTrue(hmms.getOccupant("1", "Occupant:clarkjj").contains("Occupant:clarkjj is_in_room House1:LivingRoom"));
            assertTrue(hmms.getFeature("1", "House1:LivingRoom:Thermostat:Setting:TargetTemp").contains("House1:LivingRoom:Thermostat:Setting:TargetTemp is_set_to 111.0"));




        } catch (UnauthorizedException e) {
            e.printStackTrace();
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
        } catch (ImportException e) {
            e.printStackTrace();
        } catch (UnsupportedFeatureException e) {
            e.printStackTrace();
        } catch (InvalidDeviceStateValueException e) {
            e.printStackTrace();
        } catch (QueryEngineException e) {
            e.printStackTrace();
        }


    }


}
