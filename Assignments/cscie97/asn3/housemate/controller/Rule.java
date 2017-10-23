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
        match(o);
    }

    /**
     * Will test all predicates, and if all are true will execute each command.
     * @param o
     */
	public void match(Observable o) {

	    //Create the seed context
        ConfigurationItem notifyingConfigItem = (ConfigurationItem)o;
        notifyingContext = new ConfigItemNotificationContext(notifyingConfigItem);

        Predicate<Context> seedPredicate = (context) -> {return true;};
        Predicate<Context> composedPredicate = predicateMap.values().stream().reduce(seedPredicate, (tmp, nextPredicate) -> tmp.and(nextPredicate));

        if (composedPredicate.test(commandContext())){
            commandMap.values().forEach((command) -> {
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

	public void addPredicate(String name, Predicate<Context> p) {
        predicateMap.put(name, p);
	}

	public void removePredicate(String predicateName) {
        predicateMap.remove(predicateName);
	}

	public Map<String, Predicate<Context>> getPredicates(){ return predicateMap; };

	public void addCommand(String name, Command c) {
        commandMap.put(c.getCommandId(), c);
	}

	public void removeCommand(Command c) {
        commandMap.remove(c.getCommandId());
	}

    public Map<String, Command> getCommands(){ return commandMap; };

    public void addContext(String name, Context context) {
        contextMap.put(context.getContextId(), context);
    }

	public void removeContext(Context context) {
        contextMap.remove(context.getContextId());
    }

    public Map<String, Context> getContexts(){ return contextMap;};

    public Context commandContext() {
        Context result = new ConfigItemContextCollection(getName() + "_Context");
        if (notifyingContext != null) {
            ((ConfigItemContextCollection) result).addContext((ConfigItemContextComponent) notifyingContext);
        }
		contextMap.values().forEach( (context) -> ((ConfigItemContextCollection)result).addContext((ConfigItemContextComponent)context) );
		return result;
	}

}
