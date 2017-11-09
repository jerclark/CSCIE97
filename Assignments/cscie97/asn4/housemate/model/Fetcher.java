package cscie97.asn4.housemate.model;

import java.util.ArrayList;

/**
 * Interface to define "fetching" behavior when a ConfigItem calls getState()
 *
 * @see ConfigurationItem
 * @see StandardFetcher
 */
public interface Fetcher {
    public ArrayList<String> getState(String subject);
}



