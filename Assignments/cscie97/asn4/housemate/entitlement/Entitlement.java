package cscie97.asn4.housemate.entitlement;

public abstract class Entitlement implements Visitable {

    private Resource _resource;
    private String _name;

    public Entitlement(String name, Resource resource){
        _name = name;
        _resource = resource;
    }


    public Resource getResource(){
        return _resource;
    }

    public String getName(){
        return _name;
    }
}
