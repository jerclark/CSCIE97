package cscie97.asn4.housemate.test;

import cscie97.asn4.housemate.entitlement.*;
import cscie97.asn4.housemate.model.HouseMateModelService;
import cscie97.asn4.housemate.model.HouseMateModelServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;


public class UserTest {

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
    public void testUserCreation(){
        User newUser = new User("TestUserID");
        assertEquals(newUser.getId(), "TestUserID");
        assertEquals(newUser.getEntitlements(), new ArrayList<Entitlement>());
        assertNull(newUser.getAccessToken());
    }

    @Test
    public void testAddRole(){
        User newUser = new User("TestUserID");
        Resource newResource = new Resource("TestResource", "House1");
        Role newRole = new Role("New Role", null);
        newUser.addEntitlement(newRole);
        assertEquals(newUser.getEntitlements().get(0), newRole);
    }

    @Test
    public void testRemoveRole(){
        User newUser = new User("TestUserID");
        Resource newResource = new Resource("TestResource", "House1");
        Role newRole = new Role("New Role", null);
        newUser.addEntitlement(newRole);
        assertEquals(newUser.getEntitlements().get(0), newRole);
        newUser.removeEntitlement(newRole);
        assertEquals(newUser.getEntitlements().size(), 0);
    }


    @Test
    public void testAddPermission(){
        User newUser = new User("TestUserID");
        Resource newResource = new Resource("TestResource", "House1");
        Role newRole = new Role("New Role", newResource);
        Permission newPermission = new Permission("New Permission", newResource, PermissionType.PERMISSION_TYPE_WRITE);
        newUser.addEntitlement(newPermission);
        assertEquals(newUser.getEntitlements().get(0), newPermission);
    }

    @Test
    public void testRemovePermission(){
        User newUser = new User("TestUserID");
        Resource newResource = new Resource("TestResource", "House1");
        Role newRole = new Role("New Role", newResource);
        Permission newPermission = new Permission("New Permission", newResource, PermissionType.PERMISSION_TYPE_WRITE);
        newUser.addEntitlement(newPermission);
        assertEquals(newUser.getEntitlements().get(0), newPermission);
        newUser.removeEntitlement(newPermission);
        assertEquals(newUser.getEntitlements().size(), 0);
    }



}