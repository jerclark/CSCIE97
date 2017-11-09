package cscie97.asn4.housemate.entitlement;

import java.util.ArrayList;
import java.util.List;

public class Role extends Entitlement {

    private List<Entitlement> entitlements;

    public Role(String name, Resource resource){
        super(name, resource);
        entitlements = new ArrayList<Entitlement>();
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
