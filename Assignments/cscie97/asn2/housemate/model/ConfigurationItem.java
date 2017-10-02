package cscie97.asn2.housemate.model;
import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.QueryEngineException;

import java.util.List;

public interface ConfigurationItem {

    String getFqn();
    List<String> getState() throws QueryEngineException;
    void saveState() throws ImportException, QueryEngineException;
    void setName(String newName) throws ImportException;

}
