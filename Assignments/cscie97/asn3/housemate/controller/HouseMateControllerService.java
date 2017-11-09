package cscie97.asn3.housemate.controller;

import cscie97.asn2.housemate.model.ItemExistsException;
import cscie97.asn2.housemate.model.ItemNotFoundException;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Public API for configuring the House Mate Controller.
 */
public interface HouseMateControllerService {

	/**
	 * Creates a Rule that can be attached to an observer. Predicates, Context and Commands will be attached to a rule.

	 Adds created rule to the managed collection of Rules.
	 * @param token
	 * @param ruleName
	 * @return
	 * @throws UnauthorizedControllerException
	 * @throws ControllerNamedItemExistsException
	 */
	public abstract Rule createRule(String token, String ruleName) throws UnauthorizedControllerException, ControllerNamedItemExistsException;

	/**
	 * Creates a Predicate and adds it to the collection of predicates. A predicate is a snippet of code that will test a condition in for a given context. The predicates will be attached to rules, and tested based on the context associated wit that rule. The predicate string can be any snippet of code that is supported by the Predicate Factory.

	 Initially, the only supported predicate string type will be snippets of JavaScript that will accept ConfigItemContext objects as their parameters.
	 * @param token
	 * @param name
	 * @param predicateString
	 * @return
	 * @throws UnauthorizedControllerException
	 * @throws ControllerNamedItemExistsException
	 */
	public abstract Predicate createPredicate(String token, String name, String predicateString) throws UnauthorizedControllerException, ControllerNamedItemExistsException;


	/**
	 * Adds a Command to the controller. The commandString is a valid command string that will be supported by the Command Factory to create a command. The commands are attached to Rules and executed when all Predicates attached to the rule are true.

	 Initially, the only supported command string type will be HouseMateModelService::CommandInterpreter command strings.
	 * @param token
	 * @param commandName
	 * @param commandString
	 * @param type
	 * @return
	 * @throws UnauthorizedControllerException
	 * @throws ControllerNamedItemExistsException
	 */
	public abstract Command createCommand(String token, String commandName, String commandString, ConfigItemCommandType type) throws UnauthorizedControllerException, ControllerNamedItemExistsException;

	/**
	 * Creates a context and adds to the context collection. Contexts are added to Rules to provide facts to the Predicates. The Context object created will have a getContext() method that will return facts that can be used by predicates. The contextString can be any string that the ContextFactory knows how to produce. The contextString could be a “sql” query or a webservice URL. In the HMCS, the context objects will use HMMS FQNs to find facts in the KnowledgeGraph that will be assessed by the predicates to determine whether system state dictates further action.

	 Initially, the only valid contextString will be a House Mate Model FQN.
	 * @param token auth token
	 * @param contextName the name of the context, used to store in the context map
	 * @param contextType the type of the context, ConfigItemType
	 * @param contextFqn the FQN of the context object to retrieve
	 * @return A context object
	 * @throws UnauthorizedControllerException -
	 * @throws ControllerNamedItemExistsException -
	 */
	public abstract Context createContext(String token, String contextName, ConfigItemType contextType, String contextFqn) throws UnauthorizedControllerException, ControllerNamedItemExistsException;

	/**
	 * Returns a set of all rule names
	 * @param token
	 * @return
	 * @throws UnauthorizedControllerException
	 */
	public abstract Set<String> getRuleNames(String token) throws UnauthorizedControllerException;

	/**
	 * @param token auth
	 * @return Set of all predicate names
	 * @throws UnauthorizedControllerException
	 */
	public abstract Set<String> getPredicateNames(String token) throws UnauthorizedControllerException;

	/**
=	 * @param token auth
	 * @return a set of all command names
	 * @throws UnauthorizedControllerException
	 */
    public abstract Set<String> getCommandNames(String token) throws UnauthorizedControllerException;

	/**
	 * @param token auth
	 * @return Returns a set of all context names
	 * @throws UnauthorizedControllerException
	 */
    public abstract Set<String> getContextNames(String token) throws UnauthorizedControllerException;


	/**
	 *
	 * @param token auth
	 * @param ruleName the name of the rule to find
	 * @return The found rule
	 * @throws ItemNotFoundException - when the name not found
	 * @throws UnauthorizedControllerException
	 */
	public abstract Rule getRule(String token, String ruleName) throws ItemNotFoundException, UnauthorizedControllerException;

	/**
	 *
	 * @param token auth
	 * @param commandName the name of the command to find
	 * @return The found command
	 * @throws ItemNotFoundException - when the name not found
	 * @throws UnauthorizedControllerException
	 */
	public abstract Command getCommand(String token, String commandName) throws ItemNotFoundException, UnauthorizedControllerException;

	/**
	 *
	 * @param token auth
	 * @param predicateName the name of the predicate to find
	 * @return the found predicate
	 * @throws ItemNotFoundException
	 * @throws UnauthorizedControllerException
	 */
	public abstract Predicate getPredicate(String token, String predicateName) throws ItemNotFoundException, UnauthorizedControllerException;

	/**
	 *
	 * @param token auth
	 * @param contextName the name of the context to find
	 * @return the found context
	 * @throws ItemNotFoundException
	 * @throws UnauthorizedControllerException
	 */
	public abstract Context getContext(String token, String contextName) throws ItemNotFoundException, UnauthorizedControllerException;

	/**
	 * Attaches a Predicate to a Rule so that the predicate.test() will be called during the Rules match routine. All attached predicates will be tested.
	 * @param token auith
	 * @param predicateName the name of the predicate to to add to the rule
	 * @param ruleName the name of the rule to add predicate to
	 * @throws UnauthorizedControllerException
	 * @throws ItemNotFoundException
	 */
	public abstract void addPredicateToRule(String token, String predicateName, String ruleName) throws UnauthorizedControllerException, ItemNotFoundException;

	/**
	 * Adds a Command to a Rule. If all predicates are true in a rule, ALL attached commands will be executed.
	 * @param token auth
	 * @param commandName
	 * @param ruleName
	 * @throws UnauthorizedControllerException
	 * @throws ItemNotFoundException
	 */
    public abstract void addCommandToRule(String token, String commandName, String ruleName) throws UnauthorizedControllerException, ItemNotFoundException;

	/**
	 * Adds a Context object to a Rule. The result of the Context.getContext() method will be passed to Predicates as facts to test the Predicates against.
	 * @param token auth
	 * @param contextName name of the context to add
	 * @param ruleName name of rule to add to
	 * @throws UnauthorizedControllerException
	 * @throws ItemNotFoundException
	 */
	public abstract void addContextToRule(String token, String contextName, String ruleName) throws UnauthorizedControllerException, ItemNotFoundException;

	/**
	 * Removes a Predicate from a rule.
	 * @param token auth
	 * @param predicateName name of predicate to remove
	 * @param ruleName name of rule to remove predicate from
	 * @throws UnauthorizedControllerException
	 * @throws ItemNotFoundException
	 */
	public abstract void removePredicateFromRule(String token, String predicateName, String ruleName) throws UnauthorizedControllerException, ItemNotFoundException;

	/**
	 * Removes a command from a rule.
	 * @param token auth
	 * @param commandName name of command to remove
	 * @param ruleName name of rule to remove command from
	 * @throws UnauthorizedControllerException
	 * @throws ItemNotFoundException
	 */
	public abstract void removeCommandFromRule(String token, String commandName,  String ruleName) throws UnauthorizedControllerException, ItemNotFoundException;

	/**
	 * Removes a context from a rule.
	 * @param token auth
	 * @param contextName context to remove
	 * @param ruleName rule to remove from
	 * @throws UnauthorizedControllerException
	 * @throws ItemNotFoundException
	 */
	public abstract void removeContextFromRule(String token, String contextName, String ruleName) throws UnauthorizedControllerException,ItemNotFoundException;

	/**
	 * Attaches a Rule to a feature. ruleName is the name passed in when using createRule, and featureFqn is the fully qualified name of an HMMS Feature object.
	 * @param token auth
	 * @param ruleName rule to attach
	 * @param featureFqn feature to attach to.
	 * @throws UnauthorizedControllerException
	 * @throws ItemNotFoundException
	 */
    public abstract void attachRuleToFeature(String token, String ruleName, String featureFqn) throws UnauthorizedControllerException, ItemNotFoundException;

	/**
	 * Detaches the rule as an observer of a Feature. ruleName is the name passed in when using createRule, and featureFqn is the fully qualified name of an HMMS Feature object.
	 * @param token auth
	 * @param ruleName rule to attach
	 * @param featureFqn feature to attach to
	 * @throws UnauthorizedControllerException
	 * @throws ItemNotFoundException
	 */
    public abstract void detachRuleFromFeature(String token, String ruleName, String featureFqn) throws UnauthorizedControllerException, ItemNotFoundException;


}
