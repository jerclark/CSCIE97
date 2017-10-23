package cscie97.asn3.housemate.controller;

import cscie97.asn1.knowledge.engine.QueryEngineException;
import cscie97.asn2.housemate.model.*;

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
        Map<String, String> fqnPair = new HashMap<String, String>();

//
//        Map<String, String> roomFqnPair = new HashMap<String, String>();
//        houseFqnPair.put("roomFqn", null);
//
//        Map<String, String> deviceFqnPair = new HashMap<String, String>();
//        houseFqnPair.put("deviceFqn", null);
//
//        Class notfiyingConfigItemClass = _notifyingObject.getClass();
//
//            try {
//                if (notfiyingConfigItemClass.getName().contentEquals("Feature")) {
//                    hmms.getFeature("1", _notifyingObject.getFqn());
//                    houseFqnPair.put("houseFqn", _.notifyingObject.getFqn().split())
//                }
//            } catch (UnauthorizedException e) {
//                e.printStackTrace();
//            } catch (ItemNotFoundException e) {
//                e.printStackTrace();
//            } catch (QueryEngineException e) {
//                e.printStackTrace();
//            }
//        }

        fqnPair.put("fqn", _notifyingObject.getFqn());
        result.put("notifyingObject", fqnPair);
        return result;
    }

}
