package cscie97.asn2.housemate.model;
import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.QueryEngineException;

import java.util.List;


/**
 * Common Interface for items in the model. Defines methods that are used to identify items and store/retrieve state descriptions.
 * Very central to the design, each realizing class implements a "getFqn()" method.
 * An FQN (Fully Qualified Name) is a derived identifier that makes an ConfigurationItem unique within the system.
 */
interface ConfigurationItem {

    /**
     * Gets unique identifier for a House Mate Model configuration item. (Hosue/Room/Device/DeviceState/Occupant).
     * Fqns are colon-delimited strings whose form is dictated by the implementor.
     *
     * @return String fqnName
     */
    String getFqn();

    /**
     * Retrieves a line-delimited string of triples as a state representation of implementing entity.
     * Implementors can add specific logic. See getHouse, getRoom, getDevice, getDeviceState, getOccupant
     * in the HouseMateModelService Interface class dictionary.
     *
     * @return
     * @throws QueryEngineException
     */
    List<String> getState() throws QueryEngineException;


    /**
     * Saves the state of the ConfigurationItem to the KnowledgeGraph.
     * Implmentors will add any entity specific logic for storing state.
     *
     * @throws ImportException
     * @throws QueryEngineException
     */
    void saveState() throws ImportException, QueryEngineException;


}
