package cscie97.asn4.housemate.entitlement;

public interface Visitor {

    void visit(Resource r);
    void visit(Role r);
    void visit(Permission p);
    void visit(User u);
    void visit(AccessToken t);
}
