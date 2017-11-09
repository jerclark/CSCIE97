package cscie97.asn4.housemate.entitlement;

import java.util.ArrayList;
import java.util.List;

public class User implements Visitable{

    private final String _id;
    private AccessToken _accessToken = null;
    private List<Entitlement> entitlements;
    private List<Credential> credentials;


    public User(String id){
        _id = id;
        entitlements = new ArrayList<Entitlement>();
    }


    public String getId() {
        return _id;
    }

    public AccessToken getAccessToken() {
        return _accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this._accessToken = accessToken;
    }

    public void addEntitlement(Entitlement e){
        entitlements.add(e);
    }

    public void removeEntitlement(Entitlement e){
        entitlements.remove(e);
    }

    public List<Entitlement> getEntitlements(){
        return entitlements;
    }

    public void acceptVisitor(Visitor v){
        v.visit(this);
    }


}
