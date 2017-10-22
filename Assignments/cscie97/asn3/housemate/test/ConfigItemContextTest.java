package cscie97.asn3.housemate.test;

import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.QueryEngineException;
import cscie97.asn2.housemate.model.*;
import cscie97.asn3.housemate.controller.*;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static cscie97.asn3.housemate.controller.ConfigItemType.*;


public class ConfigItemContextTest {

    private final HouseMateModelService hmms = HouseMateModelServiceImpl.getInstance();

    @BeforeEach
    public void setUp(){
        TestBootstrapper.setUp();
    }

    @AfterEach
    public void tearDown(){
        TestBootstrapper.tearDown();
    }

    public void testContexts() {
        ConfigItemContext houseContext = new ConfigItemContext(CONFIG_ITEM_TYPE_HOUSE,"House1");
        ConfigItemContext roomContext = new ConfigItemContext(CONFIG_ITEM_TYPE_ROOM,"House1:LivingRoom");
        ConfigItemContext deviceContext = new ConfigItemContext(CONFIG_ITEM_TYPE_DEVICE,"House1:LivingRoom:Thermostat");
        ConfigItemContext featureContext = new ConfigItemContext(CONFIG_ITEM_TYPE_FEATURE,"House1:LivingRoom:Thermostat:Measure:Temperature");
        ConfigItemContext occupantContext = new ConfigItemContext(CONFIG_ITEM_TYPE_OCCUPANT,"Occupant:clarkjj");
        ConfigItemContextCollection allContexts = new ConfigItemContextCollection("AllContextCollection");
        ConfigItemContextCollection roomAndDevice = new ConfigItemContextCollection("RoomAndDevice");
        ConfigItemContextCollection occupantAndFeature = new ConfigItemContextCollection("OccupantAndFeature");
        occupantAndFeature.addContext(occupantContext);
        occupantAndFeature.addContext(featureContext);
        roomAndDevice.addContext(deviceContext);
        roomAndDevice.addContext(roomContext);
        allContexts.addContext(occupantAndFeature);
        allContexts.addContext(roomAndDevice);
        try {
            assertEquals("{House1={has_num_floors=3, has_name=House1, has_room=LivingRoom, has_address=123TerdSt.}, House1:LivingRoom:Thermostat:Measure:Temperature={is_detecting=75.0}, House1:LivingRoom:Thermostat={is_in_room=House1:LivingRoom, is_of_type=Thermostat}, House1:LivingRoom={has_name=LivingRoom, has_device=Thermostat, is_on_floor=1}}", houseContext.serialize().toString());
            assertEquals("{House1:LivingRoom:Thermostat:Measure:Temperature={is_detecting=75.0}, House1:LivingRoom:Thermostat={is_in_room=House1:LivingRoom, is_of_type=Thermostat}, House1:LivingRoom={has_name=LivingRoom, has_device=Thermostat, is_on_floor=1}}", roomContext.serialize().toString());
            assertEquals("{House1:LivingRoom:Thermostat:Measure:Temperature={is_detecting=75.0}, House1:LivingRoom:Thermostat={is_in_room=House1:LivingRoom, is_of_type=Thermostat}}", deviceContext.serialize().toString());
            assertEquals("{House1:LivingRoom:Thermostat:Measure:Temperature={is_detecting=75.0}}", featureContext.serialize().toString());
            assertEquals("{Occupant:clarkjj={has_name=Jeremy_Clark, is_type=Adult, has_id=clarkjj}}", occupantContext.serialize().toString());
            assertEquals("{House1:LivingRoom:Thermostat:Measure:Temperature={is_detecting=75.0}, House1:LivingRoom:Thermostat={is_in_room=House1:LivingRoom, is_of_type=Thermostat}, House1:LivingRoom={has_name=LivingRoom, has_device=Thermostat, is_on_floor=1}, Occupant:clarkjj={has_name=Jeremy_Clark, is_type=Adult, has_id=clarkjj}}", allContexts.serialize().toString());
        } catch (ContextFetchException e) {
            e.printStackTrace();
        }
    }



}
