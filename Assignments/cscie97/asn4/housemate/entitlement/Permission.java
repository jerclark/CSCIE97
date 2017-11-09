package cscie97.asn4.housemate.entitlement;


public class Permission extends Entitlement {

    private PermissionType _mode;

    public Permission(String name, Resource resource, PermissionType mode){
        super(name, resource);
        _mode = mode;
    }

    public void acceptVisitor(Visitor v){
        v.visit(this);
    }

    public PermissionType getMode() {
        return _mode;
    }

    public boolean permits(String fqn, PermissionType mode){
        boolean result = false;
        if (checkMode(mode) && checkFqn(fqn)){
            result = true;
        }
        return result;
    }

    private boolean checkMode(PermissionType requestedMode){
        boolean result = false;
        if (getMode() == PermissionType.PERMISSION_TYPE_WRITE){
            result = true;
        }else if (requestedMode == PermissionType.PERMISSION_TYPE_READ){
            result = true;
        }
        return result;
    }

    private boolean checkFqn(String requestedFqn){
        boolean result = false;
        if (requestedFqn.indexOf(getResource().getFqn()) == 0){
            result = true;
        }
        return result;
    }

}
