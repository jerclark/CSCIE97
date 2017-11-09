package cscie97.asn4.housemate.entitlement;

public class VoiceUserFactory implements UserFactory {

    public User createUser(String username, String authentication){
        return new User("Test");
    }

}
