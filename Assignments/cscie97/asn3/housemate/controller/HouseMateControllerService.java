package cscie97.asn3.housemate.controller;

import cscie97.asn2.housemate.model.ItemExistsException;
import cscie97.asn2.housemate.model.ItemNotFoundException;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Public API for configuring the House Mate Controller.
 */
public interface HouseMateControllerService {

	public abstract Rule createRule(String token, String ruleName) throws UnauthorizedControllerException, ControllerNamedItemExistsException;

	public abstract Predicate createPredicate(String token, String name, String predicateString) throws UnauthorizedControllerException, ControllerNamedItemExistsException;

	public abstract Command createCommand(String token, String commandName, String commandString, ConfigItemCommandType type) throws UnauthorizedControllerException, ControllerNamedItemExistsException;

	public abstract Context createContext(String token, String contextName, ConfigItemType contextType, String contextFqn) throws UnauthorizedControllerException, ControllerNamedItemExistsException;

	public abstract Set<String> getRuleNames(String token) throws UnauthorizedControllerException;

	public abstract Set<String> getPredicateNames(String token) throws UnauthorizedControllerException;

    public abstract Set<String> getCommandNames(String token) throws UnauthorizedControllerException;

    public abstract Set<String> getContextNames(String token) throws UnauthorizedControllerException;

	public abstract Rule getRule(String token, String ruleName) throws ItemNotFoundException, UnauthorizedControllerException;

	public abstract Command getCommand(String token, String commandName) throws ItemNotFoundException, UnauthorizedControllerException;

	public abstract Predicate getPredicate(String token, String predicateName) throws ItemNotFoundException, UnauthorizedControllerException;

	public abstract Context getContext(String token, String contextName) throws ItemNotFoundException, UnauthorizedControllerException;

	public abstract void addPredicateToRule(String token, String predicateName, String ruleName) throws UnauthorizedControllerException, ItemNotFoundException;

    public abstract void addCommandToRule(String token, String commandName, String ruleName) throws UnauthorizedControllerException, ItemNotFoundException;

	public abstract void addContextToRule(String token, String contextName, String ruleName) throws UnauthorizedControllerException, ItemNotFoundException;

	public abstract void removePredicateFromRule(String token, String predicateName, String ruleName) throws UnauthorizedControllerException, ItemNotFoundException;

	public abstract void removeCommandFromRule(String token, String commandName,  String ruleName) throws UnauthorizedControllerException, ItemNotFoundException;

	public abstract void removeContextFromRule(String token, String contextName, String ruleName) throws UnauthorizedControllerException,ItemNotFoundException;

    public abstract void attachRuleToFeature(String token, String ruleName, String featureFqn) throws UnauthorizedControllerException, ItemNotFoundException;

    public abstract void detachRuleFromFeature(String token, String ruleName, String featureFqn) throws UnauthorizedControllerException, ItemNotFoundException;


}
