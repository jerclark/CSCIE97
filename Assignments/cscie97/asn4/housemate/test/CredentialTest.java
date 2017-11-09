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


public class CredentialTest {

    private final HouseMateModelService hmms = HouseMateModelServiceImpl.getInstance();

    @BeforeEach
    public void setUp() {
        TestBootstrapper.setUp();
    }

    @AfterEach
    public void tearDown() {
        TestBootstrapper.tearDown();
    }

    @Test
    public void testPasswordCredentialCreate(){
        User newUser = new User("Test");
        Credential newPasswordCredential = new PasswordCredential(newUser, "12345");
        assertEquals(PasswordCredential.doHash("12345"), newPasswordCredential.getHash());
    }


}