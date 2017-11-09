package cscie97.asn4.housemate.test;

import cscie97.asn4.housemate.entitlement.*;
import cscie97.asn4.housemate.model.HouseMateModelService;
import cscie97.asn4.housemate.model.HouseMateModelServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;


public class RoleTest {

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
    public void testRoleCreation(){
        Resource newResource = new Resource("TestResource", "House1");
        Role newRole = new Role("New Role", newResource);
        assertEquals(newRole.getName(), "New Role");
        assertEquals(newRole.getResource(), newResource);
        assertIterableEquals(newRole.getEntitlements(), new ArrayList<Entitlement>());
    }

    @Test
    public void testAddRole(){
        Resource newResource = new Resource("TestResource", "House1");
        Role newRole = new Role("New Role", newResource);
        Role childRole = new Role("Child Role", newResource);
        newRole.addEntitlement(childRole);
        assertEquals(newRole.getEntitlements().get(0), childRole);
    }

    @Test
    public void testRemoveRole(){
        Resource newResource = new Resource("TestResource", "House1");
        Role newRole = new Role("New Role", newResource);
        Role childRole = new Role("Child Role", newResource);
        newRole.addEntitlement(childRole);
        assertEquals(newRole.getEntitlements().get(0), childRole);
        newRole.removeEntitlement(childRole);
        assertEquals(newRole.getEntitlements().size(), 0);
    }


    @Test
    public void testAddPermission(){
        Resource newResource = new Resource("TestResource", "House1");
        Role newRole = new Role("New Role", newResource);
        Permission newPermission = new Permission("New Permission", newResource, PermissionType.PERMISSION_TYPE_WRITE);
        newRole.addEntitlement(newPermission);
        assertEquals(newRole.getEntitlements().get(0), newPermission);
    }

    @Test
    public void testRemovePermission(){
        Resource newResource = new Resource("TestResource", "House1");
        Role newRole = new Role("New Role", newResource);
        Permission newPermission = new Permission("New Permission", newResource, PermissionType.PERMISSION_TYPE_WRITE);
        newRole.addEntitlement(newPermission);
        assertEquals(newRole.getEntitlements().get(0), newPermission);
        newRole.removeEntitlement(newPermission);
        assertEquals(newRole.getEntitlements().size(), 0);
    }



}