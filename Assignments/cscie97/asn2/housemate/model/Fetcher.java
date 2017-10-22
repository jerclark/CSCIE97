package cscie97.asn2.housemate.model;
import cscie97.asn1.knowledge.engine.KnowledgeGraph;
import cscie97.asn1.knowledge.engine.Triple;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Interface to define "fetching" behavior when a ConfigItem calls getState()
 *
 * @see ConfigurationItem
 * @see StandardFetcher
 */
public interface Fetcher {
    public ArrayList<String> getState(String subject);
}



