package cscie97.asn4.housemate.controller;


import cscie97.asn1.knowledge.engine.QueryEngineException;
import cscie97.asn4.housemate.model.HouseMateModelService;
import cscie97.asn4.housemate.model.HouseMateModelServiceImpl;
import cscie97.asn4.housemate.model.ItemNotFoundException;
import cscie97.asn4.housemate.model.UnauthorizedException;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;
import java.util.stream.Stream;


/**
 * This class encapsulates the data needed to perform a Feature update in the HouseMateModelService. One of these objects will be created for a command with a pre-known discrete value. For example, to create a “Turn Oven Off” command. This would be constructed with a final value of “OFF”.
 */
public class FeatureUpdateCommand extends ConfigItemCommand implements Command {


    /**
     * The parameter command string is
     * A javascript snippet that returns an object with of the form:
     {
     targetDeviceScope: “some_fqn”,
     targetDeviceState: “some_device_state”,
     targetValue: anyValue
     }

     The target device scope can be any portion of an FQN. And the device state is any “FeatureFQN”. This is a dynamic construct that allows for determining at runtime, what feature should be sent to which devices. Remember, the command has the “commandContext()” that it got sent from the Invoker, so it can use that to determine what device scope and features to update. It can also use that context to dynamically determine a value. For example, this could be hard coded to something like:
     {
     targetDeviceScope: “House:LivingRoom”,
     targetDeviceState: “window_position”,
     targetValue: “OPEN”
     }

     This would tell all devices in the living room that support a window_position feature to Open. This way, commands can be sent based on feature, and not just ‘device type’.
     This could be interesting, for example:
     {
     targetDeviceScope: “House”,
     targetDeviceState: “text_to_speech”,
     targetValue: “Get OUT!”
     }

     This will tell all devices that support the ‘text_to_speech’ feature to say the phrase “Get Out!” - even if they’re not Ava devices.

     Also, by using the command context, you can dynamically derive the scope:
     {
     targetDeviceScope: commandContext().roomFqn,
     targetDeviceState: “text_to_speech”,
     targetValue: “Get OUT!”
     }
     * @param commandString
     */
	public FeatureUpdateCommand(String commandString){
        super(commandString);
    }

    @Override
    public String getCommandId(){
	    return _commandString;
    }


    /**
     * The execute() method will fetch the passed in Invoker’s context and run the javascript to derive the update value.
     Once it has the scope and target state, it will run:
     updateFeature(deviceFqn, deviceStateFqn, value) for every in-scope device in the HouseMateModelService. This will handle most needs of the controller, in terms of meeting requirements to ‘update state’ in the system as a result of evaluated rules.
     * @param i Inovker
     * @throws CommandExecuteException
     */
    @Override
	public void execute(Invoker i) throws CommandExecuteException {
        HouseMateModelService hmms = HouseMateModelServiceImpl.getInstance();
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("js");
            if (i != null) {
                engine.put("context", i.commandContext().serialize());
            }
            ScriptObjectMirror result = (ScriptObjectMirror) engine.eval(_commandString);
            if (!result.containsKey("targetDeviceScope") || !result.containsKey("targetDeviceStateFqn") || !result.containsKey("targetValue")) {
                Exception e = new Exception("Feature Update Command JS String doesn't return proper values: targetDeviceScope, targetDeviceStateFqn, targetValue");
                throw new CommandExecuteException(e, this);
            }
            String targetDeviceState = (String) result.get("targetDeviceStateFqn");
            String targetDeviceScope = (String) result.get("targetDeviceScope");
            List<String> targetDeviceStateStatus = hmms.getDeviceState("1", targetDeviceState);
            Stream<String> supportedDevicesStream = targetDeviceStateStatus.stream().filter((tripleString) -> (tripleString.split(" ")[1].contentEquals("is_supported_by")) && (tripleString.split(" ")[2].startsWith(targetDeviceScope)));
            supportedDevicesStream.forEach((supportedDeviceTriple) -> {
                String targetDeviceFqn = supportedDeviceTriple.split(" ")[2];
                try {
                    hmms.updateFeature("1", targetDeviceFqn, targetDeviceState, (String) result.get("targetValue").toString());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RuntimeException e){
            System.err.println("Feature Update command failed to update feature because of: " + e);
            throw e;
        } catch (UnauthorizedException e) {
            System.err.println("Unauthorized Exception Encountered: " + e);
            throw new CommandExecuteException(e, this);
        } catch (ItemNotFoundException e) {
            System.err.println("Item Not Found Exception Encountered: " + e);
            throw new CommandExecuteException(e, this);
        } catch (QueryEngineException e) {
            System.err.println("Query Engine Execution Exception Encountered: " + e);
            throw new CommandExecuteException(e, this);
        } catch (ScriptException e) {
            System.err.println("Script Execution Exception Encountered: " + e);
            throw new CommandExecuteException(e, this);
        } catch (ContextFetchException e) {
            System.err.println("Context Fetch Exception Encountered: " + e);
            throw new CommandExecuteException(e, this);
        }
    }


}
