package cscie97.asn3.housemate.controller;


import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.QueryEngineException;
import cscie97.asn2.housemate.model.*;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class SetOccupantActivityCommand extends ConfigItemCommand implements Command {

    public SetOccupantActivityCommand(String commandString){
        super(commandString);
    }

    public String getCommandId(){
        return _commandString;
    }

    public void execute(Invoker i) throws CommandExecuteException {
        HouseMateModelService hmms = HouseMateModelServiceImpl.getInstance();
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("js");
            if (i != null){
                engine.put("context", i.commandContext().serialize());
            }
            ScriptObjectMirror result = (ScriptObjectMirror)engine.eval(_commandString);
            if (!result.containsKey("occupantId") || !result.containsKey("activityState")){
                Exception e = new Exception("Set Occupant Command JS String doesn't return proper values: occupantId, activityState");
                throw new CommandExecuteException(e, this);
            }
            hmms.setOccupantActivity("1", (String)result.get("occupantId"),Boolean.parseBoolean((String)result.get("activityState")));
        } catch (ImportException e){
            System.err.println("Feature Update command failed to update feature because of: " + e);
            throw new CommandExecuteException(e, this);
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
            e.printStackTrace(); //Intentionally print stack trace to help with JS debugging.
            throw new CommandExecuteException(e, this);
        } catch (ContextFetchException e) {
            System.err.println("Context Fetch Exception Encountered: " + e);
            throw new CommandExecuteException(e, this);
        } catch (ItemExistsException e) {
            System.err.println("Item Exists Exception Encountered: " + e);
            throw new CommandExecuteException(e, this);
        }
    }

}
