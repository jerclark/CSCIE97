package cscie97.asn3.housemate.controller;

import com.sun.deploy.util.StringUtils;
import com.sun.tools.javac.util.ArrayUtils;
import cscie97.asn1.knowledge.engine.QueryEngineException;
import cscie97.asn2.housemate.model.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConfigItemNotificationContext implements Context, ConfigItemContextComponent{

    private ConfigurationItem _notifyingObject;

    private HouseMateModelService hmms = HouseMateModelServiceImpl.getInstance();

    public ConfigItemNotificationContext(ConfigurationItem notifyingObject){
        _notifyingObject = notifyingObject;
    }

    public String getContextId(){
        return _notifyingObject.getFqn();
    }

    public Map<String, Map<String, String>> serialize(){
        Map<String,Map<String,String>> result = new HashMap<String,Map<String,String>>();

        //Get the notifiying object FQN
        Map<String, String> fqnPairs = new HashMap<String, String>();
        fqnPairs.put("fqn", _notifyingObject.getFqn());

        //Get the notifiying object ROOM FQN
        Map<String, String> roomFqnPair = new HashMap<String, String>();
        String[] roomFqnParts = Arrays.copyOfRange(_notifyingObject.getFqn().split(":"), 0, 2);
        String roomFqn = StringUtils.join(Arrays.asList(roomFqnParts), ":");
        fqnPairs.put("roomFqn", roomFqn);

        result.put("notifyingObject", fqnPairs);
        return result;
    }

}
