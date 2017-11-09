package cscie97.asn4.housemate.entitlement;

public class Resource implements Visitable {



    private String _name;
    private String _fqn;

    public Resource(String name, String fqn){
        _name = name;
        _fqn = fqn;
    }

    public void acceptVisitor(Visitor v){
        v.visit(this);
    }

    public String getFqn() {
        return _fqn;
    }

    public String getName() {
        return _name;
    }
}
