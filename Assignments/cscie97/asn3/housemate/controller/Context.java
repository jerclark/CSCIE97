package cscie97.asn3.housemate.controller;

import cscie97.asn1.knowledge.engine.QueryEngineException;

public interface Context {

	public abstract Object serialize() throws QueryEngineException, ContextFetchException;
	public abstract String getContextId();

}
