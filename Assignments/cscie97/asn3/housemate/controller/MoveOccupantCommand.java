package cscie97.asn3.housemate.controller;


import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.QueryEngineException;
import cscie97.asn2.housemate.model.*;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * This class encapsulates the data needed to perform a moveOccupant() command in the HMMS.
 */
public class MoveOccupantCommand extends ConfigItemCommand implements Command {

    /**
     * The commandString A javascript snippet that returns an object with of the form:
     {
     occupantId: “some_occupant_id”,
     roomFqn: “some_room_fqn”
     }

     Like the FeatureUpdateCommand, the values can be passed in directly or derived from the commandContext() in the execute() function. For example:
     {
     occupantId: “Jeremy”,
     roomFqn: “House1:LivingRoom”
     }

     Or more dynamically (note, the code below is just representative, the actual codes can be seen in the included script files):
     {
     occupantId: commandContext().occupantWhoEntered,
     roomFqn: commandContext().camera.room
     }

     * @param commandString
     */
    public MoveOccupantCommand(String commandString){
        super(commandString);
    }

    public String getCommandId(){
	    return _commandString;
    }

    /**
     * This will evaluate the passed in commandString and pass the results to “moveOccupant(token, occupantFqn, roomFqn)”
     * @param i Inovker
     * @throws CommandExecuteException
     */
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
                throw new CommandExecuteException(e, this);
            }
            hmms.moveOccupant("1", (String)result.get("occupantId"),(String)result.get("roomFqn"));
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
