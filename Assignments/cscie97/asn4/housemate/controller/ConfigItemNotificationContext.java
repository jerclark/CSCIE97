package cscie97.asn4.housemate.controller;

import cscie97.asn4.housemate.model.ConfigurationItem;
import cscie97.asn4.housemate.model.HouseMateModelService;
import cscie97.asn4.housemate.model.HouseMateModelServiceImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * A special Context that reflects the context of a notification object that passed to an Observer (Rule). See more details in the serialze() doc.
 */
public class ConfigItemNotificationContext implements Context, ConfigItemContextComponent {

    /**
     * The notifying object, stored as an HMMS Configuration Item.
     */
    private ConfigurationItem _notifyingObject;

    private HouseMateModelService hmms = HouseMateModelServiceImpl.getInstance();

    public ConfigItemNotificationContext(ConfigurationItem notifyingObject){
        _notifyingObject = notifyingObject;
    }

    /**
     * The context ID of the context
     * @return
     */
    public String getContextId(){
        return _notifyingObject.getFqn();
    }


    /**
     * Will return a Map<String, Map<String, String>> of context for the FQN of the notifyingObject
     * Specifically, will return an Structure that looks like:
     *
     * {notifyingObject:{fqn:"notifying:object:fqn", roomFqn:"nofitying:object:room:fqn"}}
     *
     * @return
     */
    public Map<String, Map<String, String>> serialize(){
        Map<String,Map<String,String>> result = new HashMap<String,Map<String,String>>();

        //Get the notifiying object FQN
        Map<String, String> fqnPairs = new HashMap<String, String>();
        fqnPairs.put("fqn", _notifyingObject.getFqn());

        //Get the notifiying object ROOM FQN
        Map<String, String> roomFqnPair = new HashMap<String, String>();
        String[] roomFqnParts = Arrays.copyOfRange(_notifyingObject.getFqn().split(":"), 0, 2);
        String roomFqn = String.join(":", Arrays.asList(roomFqnParts));
        fqnPairs.put("roomFqn", roomFqn);

        result.put("notifyingObject", fqnPairs);
        return result;
    }

}
