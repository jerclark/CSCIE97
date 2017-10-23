package cscie97.asn3.housemate.controller;


import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.QueryEngineException;
import cscie97.asn2.housemate.model.*;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.ScriptObject;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MoveOccupantCommand extends ConfigItemCommand implements Command {

    public MoveOccupantCommand(String commandString){
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
            if (!result.containsKey("occupantId") || !result.containsKey("roomFqn")){
                Exception e = new Exception("Move Occupant Command JS String doesn't return proper values: occupantId, roomFqn");
                throw new CommandExecuteException(e);
            }
            hmms.moveOccupant("1", (String)result.get("occupantId"),(String)result.get("roomFqn"));
        } catch (ItemExistsException e) {
            e.printStackTrace();
        } catch (UnauthorizedException e) {
            e.printStackTrace();
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
        } catch (ImportException e) {
            e.printStackTrace();
        } catch (ContextFetchException e) {
            e.printStackTrace();
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (QueryEngineException e) {
            e.printStackTrace();
        }
    }

}