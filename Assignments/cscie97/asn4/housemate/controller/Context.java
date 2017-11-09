package cscie97.asn4.housemate.controller;

import cscie97.asn1.knowledge.engine.QueryEngineException;

/**
 * Interface that provides methods for accessing data ‘Contexts’ that can be used with Predicate objects. Has a generic type parameter that will be provided by implementors that determines what kind of data will be returned from “getContext()”
 */
public interface Context {

	/**
	 * Returns the populated context data. The Object return types will be made concrete by the subclasses.
	 * @return
	 * @throws QueryEngineException
	 * @throws ContextFetchException
	 */
	public abstract Object serialize() throws QueryEngineException, ContextFetchException;

	/**
	 * Overriden by impelementors to return a string identifier for the context.
	 * @return String, the contextId()
	 */
	public abstract String getContextId();

}
