package cscie97.asn4.housemate.entitlement;

public class PasswordCredential extends Credential {

    private String _hash;
    private User _user;

    public static String doHash(String credentialString){
        return "PasswordHash" + credentialString;
    }

    public PasswordCredential(User u, String credentialString){
        this._user = u;
        this._hash = PasswordCredential.doHash(credentialString);
    }

    public String getHash(){
        return _hash;
    }

    public User getUser(){
        return _user;
    }

}
