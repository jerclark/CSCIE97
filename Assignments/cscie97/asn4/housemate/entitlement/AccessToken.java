package cscie97.asn4.housemate.entitlement;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class AccessToken {

    private final long INACTIVITY_TIMEOUT = 5000;

    private final User _user;
    private final long _expiration;
    private long _lastAccess;
    private final String _id;

    public AccessToken(long expirationTime, User user){
        _user = user;
        _expiration = expirationTime;
        _lastAccess = now();
        _id = UUID.randomUUID().toString();
    }

    public boolean isExpired(){
        return ( (now() > _expiration) || (sinceLastActivity() > INACTIVITY_TIMEOUT) );
    }

    public User getUser(){
        return _user;
    }

    public String getId(){
        return _id;
    }

    private long sinceLastActivity(){
        return (now() - _lastAccess);
    }

    private long now(){
        return Calendar.getInstance().getTime().getTime();
    }

}
