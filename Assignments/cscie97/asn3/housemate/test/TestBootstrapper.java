package cscie97.asn3.housemate.test;

import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.QueryEngineException;
import cscie97.asn2.housemate.model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import sun.security.ssl.HandshakeOutStream;

public class TestBootstrapper {

    public static void tearDown(){
        HouseMateModelService hmms = HouseMateModelServiceImpl.getInstance();
        try {
            hmms.removeAll("1");
        } catch (UnauthorizedException e) {
            e.printStackTrace();
        }
    }

    public static void setUp(){

        HouseMateModelService hmms = HouseMateModelServiceImpl.getInstance();

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
        }
    }


}
