package cscie97.asn4.housemate.entitlement;

public class CLIUserFactory implements UserFactory {

    public User createUser(String username, String credential){
        return new User("Test");
    }

}
