package cscie97.asn3.housemate.controller;


import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.QueryEngineException;
import cscie97.asn2.housemate.model.*;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FeatureUpdateCommand extends ConfigItemCommand implements Command {


	public FeatureUpdateCommand(String commandString){
        super(commandString);
    }

    @Override
    public String getCommandId(){
	    return _commandString;
    }

    @Override
	public void execute(Invoker i) throws CommandExecuteException {
        HouseMateModelService hmms = HouseMateModelServiceImpl.getInstance();
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("js");
            if (i != null){
                engine.put("context", i.commandContext().serialize());
            }
            ScriptObjectMirror result = (ScriptObjectMirror) engine.eval(_commandString);
            if (!result.containsKey("targetDeviceScope") || !result.containsKey("targetDeviceStateFqn") || !result.containsKey("targetValue")){
                Exception e = new Exception("Feature Update Command JS String doesn't return proper values: targetDeviceFqn, targetDeviceStateFqn, targetValue");
                throw new CommandExecuteException(e);
            }
            String targetDeviceState = (String)result.get("targetDeviceStateFqn");
            String targetDeviceScope = (String)result.get("targetDeviceScope");
            List<String> targetDeviceStateStatus = hmms.getDeviceState("1", targetDeviceState);
            Stream<String> supportedDevicesStream = targetDeviceStateStatus.stream().filter( (tripleString) -> (tripleString.split(" ")[1].contentEquals("is_supported_by")) && (tripleString.split(" ")[2].startsWith(targetDeviceScope)) );
            supportedDevicesStream.forEach( (supportedDeviceTriple) -> {
                String targetDeviceFqn = supportedDeviceTriple.split(" ")[2];
                try {
                    hmms.updateFeature("1", targetDeviceFqn, targetDeviceState, (String)result.get("targetValue").toString());
                } catch (UnsupportedFeatureException e) {
                    e.printStackTrace();
                } catch (UnauthorizedException e) {
                    e.printStackTrace();
                } catch (ItemNotFoundException e) {
                    e.printStackTrace();
                } catch (ImportException e) {
                    e.printStackTrace();
                } catch (InvalidDeviceStateValueException e) {
                    e.printStackTrace();
                } catch (QueryEngineException e) {
                    e.printStackTrace();
                }
            });
        } catch (UnauthorizedException e) {
            new CommandExecuteException(e);
        } catch (ItemNotFoundException e) {
            new CommandExecuteException(e);
        } catch (QueryEngineException e) {
            new CommandExecuteException(e);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (ContextFetchException e) {
            e.printStackTrace();
        }
    }


}
