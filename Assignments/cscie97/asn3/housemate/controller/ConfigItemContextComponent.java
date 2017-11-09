package cscie97.asn3.housemate.controller;

import cscie97.asn1.knowledge.engine.QueryEngineException;

/**
 * Abstract interface type that is used as the component class for a composite structure of ConfigItemContexts. Implemented by ConfigItemContext and ConfigItemContextCollection. There's currently no internal state or behavior, just a type that defines that can
 * be used by the Composite structure of the ConfigItemContextCollection
 */
public interface ConfigItemContextComponent extends Context {

}
