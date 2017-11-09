package cscie97.asn3.housemate.controller;

import cscie97.asn2.housemate.model.ConfigurationItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Predicate;

/**
 * Workhorse of the controller dynamics. When the "match(o)" method is called, the Rule will populate all registered contexts, and test all registered predicates against the populated context. If all predicates return true, all registered commands will be executed.
 * Predicates, Commands and Contexts can be added and removed.
 */
public class Rule implements Invoker, Observer {

    private String _name;
    private Map<String, Predicate<Context>> predicateMap = new HashMap<String,Predicate<Context>>();
    private Map<String, Command> commandMap = new HashMap<String,Command>();
    private Map<String, Context> contextMap = new HashMap<String,Context>();
    private Context notifyingContext = null;

    public Rule(String name){
        _name = name;
    }

    public String getName(){
        return _name;
    }

    public void update(Observable o, Object data){
        System.out.println("CONTROLLER: Rule '" + getName() + "' received update notification from " + ((ConfigurationItem)o).getFqn());
        match(o);
    }

    /**
     * Performs the match routine, which will populate contexts, test predicates and execute commands if all predicates return true.
     * @param o
     */
	public void match(Observable o) {

        //Log the observed nofication

	    //Create the seed context
        ConfigurationItem notifyingConfigItem = (ConfigurationItem)o;
        notifyingContext = new ConfigItemNotificationContext(notifyingConfigItem);

        //Compose all resident predicates into an 'and' (inclusive) predicate. All predicates must pass for the commands to
        //be executed
        Predicate<Context> seedPredicate = (context) -> {return true;};
        Predicate<Context> composedPredicate = predicateMap.values().stream().reduce(seedPredicate, (tmp, nextPredicate) -> tmp.and(nextPredicate));

        //If all predicate tests pass, then execute each command
        if (composedPredicate.test(commandContext())){
            System.out.println("CONTROLLER: Rule '" + getName() + "' all predicates pass. Executing commands.");
            commandMap.values().forEach((command) -> {
                System.out.println("CONTROLLER: Rule '" + getName() + "' executing command " + command.getCommandId());
                try {
                    command.execute(this);
                } catch (CommandExecuteException e) {
                    notifyingContext = null;
                    System.err.println(e);
                }
            });
        }
        notifyingContext = null;

	}

    /**
     *
     * @param name the name of the predicat eto add
     * @param p The predicate object to add
     */
	public void addPredicate(String name, Predicate<Context> p) {
        predicateMap.put(name, p);
	}

    /**
     *
     * @param predicateName the name of the predicate to remove
     */
	public void removePredicate(String predicateName) {
        predicateMap.remove(predicateName);
	}

    /**
     *
     * @return Returns all predicates as a map of names/predicates
     */
	public Map<String, Predicate<Context>> getPredicates(){ return predicateMap; };

    /**
     *
     * @param name The name of the command to add
     * @param c the command to add
     */
	public void addCommand(String name, Command c) {
        commandMap.put(c.getCommandId(), c);
	}

    /**
     *
     * @param c The command to remove
     */
	public void removeCommand(Command c) {
        commandMap.remove(c.getCommandId());
	}

    /**
     *
     * @return Returns all commands as a map of name/command
     */
    public Map<String, Command> getCommands(){ return commandMap; };

    /**
     *Adds a context to the rule. For my tests most rules are simply passed the entire HouseMateModelContext (fetched by calling “HSSM:getAll()”. However, by adding multiple smaller contexts, the  clients can compose contexts of discrete context elements. This would be desirable if the entire context stored in the HouseMate system was too large to efficiently pass between objects, or if the context was fetched from a remote service where large data took a long time to retrieve.
     * @param name the name of the context to add
     * @param context The context object to add
     */
    public void addContext(String name, Context context) {
        contextMap.put(context.getContextId(), context);
    }

    /**
     *
     * @param context the context to remove
     */
	public void removeContext(Context context) {
        contextMap.remove(context.getContextId());
    }

    /**
     *
     * @return All contexts as a map of name/context pairs
     */
    public Map<String, Context> getContexts(){ return contextMap;};

    /**
     *Override of Invoker interface method.  Will return a ConfigItemsContext object when called. This way, the command that gets invoked will have all of the context of the Rule when it executes. The command context will be a map of KnowledgeGraph triples representing the state: for example:
     {House1:Room1={has_occupant_count=3, has_window_count=4}, House1:Room1:Ava:Setting:text_to_speech={is_set_to=‘some value’}}. See the ConfigItemContextCollection dictionary entry for more information.
     * @return the context
     * @see ConfigItemContextCollection
     */
    public Context commandContext() {
        Context result = new ConfigItemContextCollection(getName() + "_Context");
        if (notifyingContext != null) {
            ((ConfigItemContextCollection) result).addContext((ConfigItemContextComponent) notifyingContext);
        }
		contextMap.values().forEach( (context) -> ((ConfigItemContextCollection)result).addContext((ConfigItemContextComponent)context) );
		return result;
	}

}
