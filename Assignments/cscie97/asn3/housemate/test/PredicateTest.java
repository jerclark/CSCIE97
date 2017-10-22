package cscie97.asn3.housemate.test;

import cscie97.asn1.knowledge.engine.QueryEngineException;
import cscie97.asn2.housemate.model.*;
import cscie97.asn3.housemate.controller.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static cscie97.asn3.housemate.controller.PredicateType.PREDICATE_TYPE_JS;
import static org.junit.jupiter.api.Assertions.*;
import static cscie97.asn3.housemate.controller.ConfigItemType.*;

public class PredicateTest {

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
    public void testPredicate(){
        ConfigItemContext featureContext = new ConfigItemContext(CONFIG_ITEM_TYPE_FEATURE,"House1:LivingRoom:Thermostat:Measure:Temperature");
        Predicate<Context> p1 = PredicateCreator.createJSPredicate("context['House1:LivingRoom:Thermostat:Measure:Temperature'].is_detecting < 100");
        assertTrue(p1.test(featureContext));
        Predicate<Context> p2 = PredicateCreator.createJSPredicate("context['House1:LivingRoom:Thermostat:Measure:Temperature'].is_detecting > 100");
        assertFalse(p2.test(featureContext));
        Predicate<Context> p3 = PredicateCreator.createJSPredicate("context['House1:LivingRoom:Thermostat:Measure:Temperature'].is_detecting == context['House1:LivingRoom:Thermostat:Measure:Temperature'].is_detecting");
        assertTrue(p3.test(featureContext));
    }

}
