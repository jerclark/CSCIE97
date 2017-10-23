package cscie97.asn3.housemate.controller;
import cscie97.asn2.housemate.model.HouseMateModelService;
import cscie97.asn2.housemate.model.HouseMateModelServiceImpl;
import cscie97.asn2.housemate.model.ItemNotFoundException;
import cscie97.asn2.housemate.model.UnauthorizedException;

import java.util.HashMap;
import java.util.Map;
import java.util.Observer;
import java.util.Set;
import java.util.function.Predicate;

public class HouseMateControllerServiceImpl implements HouseMateControllerService {

    private final String VALID_TOKEN = "1";

	private Map<String, Predicate> predicates;

	private Map<String,Rule> rules;

	private Map<String,Command> commands;

	private Map<String,Context> contexts;

	private HouseMateModelService hmms = HouseMateModelServiceImpl.getInstance();

    //Singleton methods
    private static HouseMateControllerService ourInstance = new HouseMateControllerServiceImpl();
    public static HouseMateControllerService getInstance() {
        return ourInstance;
    }

    //Private constructor, prevents duplicates
    private HouseMateControllerServiceImpl() {
        this.predicates = new HashMap<String, Predicate>();
        this.rules = new HashMap<String, Rule>();
        this.commands = new HashMap<String, Command>();
        this.contexts = new HashMap<String, Context>();
    }


    @Override
    public Rule createRule(String token, String ruleName) throws UnauthorizedControllerException, ControllerNamedItemExistsException {
        checkToken(token);
        if (rules.containsKey(ruleName)){
            throw new ControllerNamedItemExistsException(ruleName, "Rule");
        }
        Rule newRule = new Rule(ruleName);
        rules.put(ruleName, newRule);
        return newRule;
    }

    @Override
    public Predicate createPredicate(String token, String name, String predicateString) throws UnauthorizedControllerException, ControllerNamedItemExistsException {
        checkToken(token);
        if (predicates.containsKey(name)){
            throw new ControllerNamedItemExistsException(name, "Predicate");
        }
        Predicate newPredicate = PredicateCreator.createJSPredicate(predicateString);
        predicates.put(name, newPredicate);
        return newPredicate;
    }

    @Override
    public Command createCommand(String token, String commandName, String commandString, ConfigItemCommandType type) throws UnauthorizedControllerException, ControllerNamedItemExistsException {
        checkToken(token);
        if (commands.containsKey(commandName)){
            throw new ControllerNamedItemExistsException(commandName, "Command");
        }
        Command newCommand = null;
        switch(type){
            case FEATURE_UPDATE_COMMAND:
                newCommand = new FeatureUpdateCommand(commandString);
                break;
            case MOVE_OCCUPANT_COMMAND:
                newCommand = new MoveOccupantCommand(commandString);
                break;
            case SET_OCCUPANT_ACTIVITY_COMMAND:
                newCommand = new SetOccupantActivityCommand(commandString);
                break;

        }
        commands.put(commandName, newCommand);
        return newCommand;
    }

    @Override
    public Context createContext(String token, String contextName, ConfigItemType contextType, String contextFqn) throws UnauthorizedControllerException, ControllerNamedItemExistsException {
        checkToken(token);
        if (contexts.containsKey(contextName)){
            throw new ControllerNamedItemExistsException(contextName, "Context");
        }
        Context newContext = new ConfigItemContext(contextType, contextFqn);
        contexts.put(contextName, newContext);
        return newContext;
    }

    @Override
    public Set<String> getRuleNames(String token) throws UnauthorizedControllerException {
        checkToken(token);
        return rules.keySet();
    }

    @Override
    public Set<String> getPredicateNames(String token) throws UnauthorizedControllerException {
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedControllerException();
        }
        return predicates.keySet();
    }

    @Override
    public Set<String> getCommandNames(String token) throws UnauthorizedControllerException {
        checkToken(token);
        return commands.keySet();
    }

    @Override
    public Set<String> getContextNames(String token) throws UnauthorizedControllerException {
        checkToken(token);
        return contexts.keySet();
    }

    @Override
    public void addPredicateToRule(String token, String predicateName, String ruleName) throws UnauthorizedControllerException, ItemNotFoundException {
        checkToken(token);
        if ( !predicates.containsKey(predicateName)){
            throw new ItemNotFoundException(predicateName);
        }
        if ( !rules.containsKey(ruleName)){
            throw new ItemNotFoundException(predicateName);
        }
        Rule r = rules.get(ruleName);
        Predicate p = predicates.get(predicateName);
        r.addPredicate(predicateName, p);
    }

    @Override
    public void addCommandToRule(String token, String commandName, String ruleName) throws UnauthorizedControllerException, ItemNotFoundException {
        checkToken(token);
        if ( !commands.containsKey(commandName)){
            throw new ItemNotFoundException(commandName);
        }
        if ( !rules.containsKey(ruleName)){
            throw new ItemNotFoundException(ruleName);
        }
        Rule r = rules.get(ruleName);
        Command c = commands.get(commandName);
        r.addCommand(commandName, c);
    }

    @Override
    public void addContextToRule(String token, String contextName, String ruleName) throws UnauthorizedControllerException, ItemNotFoundException {
        checkToken(token);
        if ( !contexts.containsKey(contextName)){
            throw new ItemNotFoundException(contextName);
        }
        if ( !rules.containsKey(ruleName)){
            throw new ItemNotFoundException(ruleName);
        }
        Rule r = rules.get(ruleName);
        Context c = contexts.get(contextName);
        r.addContext(contextName, c);
    }

    @Override
    public void removePredicateFromRule(String token, String predicateName, String ruleName) throws UnauthorizedControllerException, ItemNotFoundException {
        checkToken(token);
        if ( !predicates.containsKey(predicateName)){
            throw new ItemNotFoundException(predicateName);
        }
        if ( !rules.containsKey(ruleName)){
            throw new ItemNotFoundException(predicateName);
        }
        Rule r = rules.get(ruleName);
        r.removePredicate(predicateName);
    }

    @Override
    public void removeCommandFromRule(String token, String commandName, String ruleName) throws UnauthorizedControllerException, ItemNotFoundException {
        checkToken(token);
        checkToken(token);
        if ( !commands.containsKey(commandName)){
            throw new ItemNotFoundException(commandName);
        }
        if ( !rules.containsKey(ruleName)){
            throw new ItemNotFoundException(ruleName);
        }
        Rule r = rules.get(ruleName);
        Command c = commands.get(commandName);
        r.removeCommand(c);
    }

    @Override
    public void removeContextFromRule(String token, String contextName, String ruleName) throws UnauthorizedControllerException, ItemNotFoundException {
        checkToken(token);
        if ( !contexts.containsKey(contextName)){
            throw new ItemNotFoundException(contextName);
        }
        if ( !rules.containsKey(ruleName)){
            throw new ItemNotFoundException(ruleName);
        }
        Rule r = rules.get(ruleName);
        Context c = contexts.get(contextName);
        r.removeContext(c);
    }

    @Override
    public void attachRuleToFeature(String token, String ruleName, String featureFqn) throws UnauthorizedControllerException, ItemNotFoundException {
        checkToken(token);
        if ( !rules.containsKey(ruleName)){
            throw new ItemNotFoundException(ruleName);
        }
        try {
            hmms.subscribeToFeature(token, featureFqn, getRule(token, ruleName));
        } catch (UnauthorizedException e) {
            throw new UnauthorizedControllerException();
        }
    }

    @Override
    public void detachRuleFromFeature(String token, String ruleName, String featureFqn) throws UnauthorizedControllerException, ItemNotFoundException {
        checkToken(token);
        if ( !rules.containsKey(ruleName)){
            throw new ItemNotFoundException(ruleName);
        }
        try {
            hmms.unsubscribeFromFeature(token, featureFqn, getRule(token, ruleName));
        } catch (UnauthorizedException e) {
            throw new UnauthorizedControllerException();
        }
    }

    public Rule getRule(String token, String ruleName) throws ItemNotFoundException, UnauthorizedControllerException {
        checkToken(token);
        if (!rules.containsKey(ruleName)){
            throw new ItemNotFoundException(ruleName);
        }
        return rules.get(ruleName);
    }

    public Command getCommand(String token, String commandName) throws ItemNotFoundException, UnauthorizedControllerException {
        checkToken(token);
        if (!commands.containsKey(commandName)){
            throw new ItemNotFoundException(commandName);
        }
        return commands.get(commandName);
    }

    public Predicate getPredicate(String token, String predicateName) throws ItemNotFoundException, UnauthorizedControllerException {
        checkToken(token);
        if (!predicates.containsKey(predicateName)){
            throw new ItemNotFoundException(predicateName);
        }
        return predicates.get(predicateName);
    }

    public Context getContext(String token, String contextName) throws ItemNotFoundException, UnauthorizedControllerException {
        checkToken(token);
        if (!contexts.containsKey(contextName)){
            throw new ItemNotFoundException(contextName);
        }
        return contexts.get(contextName);
    }

    private void checkToken(String token) throws UnauthorizedControllerException {
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedControllerException();
        }
    }


}
