package cscie97.asn4.housemate.test;

import cscie97.asn4.housemate.entitlement.*;
import cscie97.asn4.housemate.model.HouseMateModelService;
import cscie97.asn4.housemate.model.HouseMateModelServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;


public class PermissionTest {

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
    public void testPermissionCreation(){
        Resource newResource = new Resource("TestResource", "House1");
        Permission newPermission = new Permission("New Permission", newResource, PermissionType.PERMISSION_TYPE_WRITE);
        assertEquals(newPermission.getName(), "New Permission");
        assertEquals(newPermission.getResource(), newResource);
        assertEquals(newPermission.getMode(), PermissionType.PERMISSION_TYPE_WRITE);
    }


    @Test
    public void testPermits(){
        Resource newResource = new Resource("TestResource", "House1");
        Permission newPermission = new Permission("New Permission", newResource, PermissionType.PERMISSION_TYPE_WRITE);
        assertTrue(newPermission.permits("House1", PermissionType.PERMISSION_TYPE_WRITE));
        assertTrue(newPermission.permits("House1:LivingRoom", PermissionType.PERMISSION_TYPE_WRITE));
        assertTrue(newPermission.permits("House1:LivingRoom:Device1", PermissionType.PERMISSION_TYPE_WRITE));
        assertTrue(newPermission.permits("House1", PermissionType.PERMISSION_TYPE_READ));
        assertTrue(newPermission.permits("House1:LivingRoom", PermissionType.PERMISSION_TYPE_READ));
        assertTrue(newPermission.permits("House1:LivingRoom:Device1", PermissionType.PERMISSION_TYPE_READ));
    }


    @Test
    public void testDoesNotPermit(){

        Resource house = new Resource("TestResource", "House1:LivingRoom");
        Permission newPermission = new Permission("New Permission", house, PermissionType.PERMISSION_TYPE_READ);
        assertFalse(newPermission.permits("House1:LivingRoom", PermissionType.PERMISSION_TYPE_WRITE));
        assertFalse(newPermission.permits("House1", PermissionType.PERMISSION_TYPE_READ));
        assertFalse(newPermission.permits("House1:LivingRoom:Device1", PermissionType.PERMISSION_TYPE_WRITE));
        assertFalse(newPermission.permits("House1:LivingRoom:Device2", PermissionType.PERMISSION_TYPE_WRITE));
        assertTrue(newPermission.permits("House1:LivingRoom:Device1", PermissionType.PERMISSION_TYPE_READ));

        Resource device = new Resource("TestResource", "House1:LivingRoom:Device1");
        Permission newDeviceWritePermission = new Permission("New DW Permission", device, PermissionType.PERMISSION_TYPE_WRITE);
        assertTrue(newDeviceWritePermission.permits("House1:LivingRoom:Device1", PermissionType.PERMISSION_TYPE_WRITE));
        assertFalse(newDeviceWritePermission.permits("House1:LivingRoom:Device2", PermissionType.PERMISSION_TYPE_READ));
        assertFalse(newDeviceWritePermission.permits("House1:LivingRoom", PermissionType.PERMISSION_TYPE_READ));
        assertFalse(newDeviceWritePermission.permits("House1:LivingRoom", PermissionType.PERMISSION_TYPE_WRITE));
    }



}
