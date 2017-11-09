package cscie97.asn4.housemate.test;

import cscie97.asn4.housemate.entitlement.*;
import cscie97.asn4.housemate.model.HouseMateModelService;
import cscie97.asn4.housemate.model.HouseMateModelServiceImpl;
import jdk.nashorn.internal.runtime.regexp.RegExp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.concurrent.TimeUnit;


public class AccessTokenTest {

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
    public void testAccessTokenCreation(){
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.MINUTE, 1);
        Date expirationDate = expiration.getTime();
        User newUser = new User("TestUser");
        AccessToken at = new AccessToken(expirationDate.getTime(), newUser);
        assertFalse(at.isExpired());
        assertEquals(at.getUser(), newUser);
        assertNotNull(at.getId());
    }


    @Test
    public void testAccessTokenExpiration(){
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.SECOND, 1);
        Date expirationDate = expiration.getTime();
        User newUser = new User("TestUser");
        AccessToken at = new AccessToken(expirationDate.getTime(), newUser);
        assertFalse(at.isExpired());
        try {
            TimeUnit.SECONDS.sleep(3); //The inactivity defaults to 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(at.isExpired());
    }


    @Test
    public void testAccessTokenInactvityTimeout(){
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.SECOND, 30);
        Date expirationDate = expiration.getTime();
        User newUser = new User("TestUser");
        AccessToken at = new AccessToken(expirationDate.getTime(), newUser);
        assertFalse(at.isExpired());
        try {
            TimeUnit.SECONDS.sleep(6); //The inactivity defaults to 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(at.isExpired());
    }



    @Test
    public void testAccessTokenId(){
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.SECOND, 1);
        Date expirationDate = expiration.getTime();
        User newUser = new User("TestUser");
        AccessToken at = new AccessToken(expirationDate.getTime(), newUser);
        assertTrue(at.getId().matches("\\p{XDigit}{8}?-\\p{XDigit}{4}?-\\p{XDigit}{4}?-\\p{XDigit}{4}?-\\p{XDigit}{12}?"));
    }


}
