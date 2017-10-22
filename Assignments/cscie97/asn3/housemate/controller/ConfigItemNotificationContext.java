package cscie97.asn3.housemate.controller;

import java.util.HashMap;
import java.util.Map;

public class ConfigItemNotificationContext implements Context, ConfigItemContextComponent{

    private String _notifyingFqn;

    public ConfigItemNotificationContext(String notifyingFqn){
        _notifyingFqn = notifyingFqn;
    }

    public String getContextId(){
        return _notifyingFqn;
    }

    public Map<String, Map<String, String>> serialize(){
        Map<String,Map<String,String>> result = new HashMap<String,Map<String,String>>();
        Map<String, String> fqnPair = new HashMap<String, String>();
        fqnPair.put("fqn", _notifyingFqn);
        result.put("notifyingObject", fqnPair);
        return result;
    }

}
