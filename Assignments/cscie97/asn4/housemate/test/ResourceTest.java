package cscie97.asn4.housemate.test;

import cscie97.asn4.housemate.model.HouseMateModelService;
import cscie97.asn4.housemate.model.HouseMateModelServiceImpl;
import cscie97.asn4.housemate.entitlement.*;
import cscie97.asn4.housemate.test.TestBootstrapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.function.Predicate;
import static cscie97.asn4.housemate.controller.ConfigItemType.CONFIG_ITEM_TYPE_HOUSE;
import static org.junit.jupiter.api.Assertions.*;


public class ResourceTest {

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
    public void testResourceCreation(){
        Resource newResource = new Resource("TestResource", "House1:LivingRoom");
        assertEquals(newResource.getName(),"TestResource");
        assertEquals(newResource.getFqn(),"House1:LivingRoom");
    }


}