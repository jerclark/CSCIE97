package cscie97.asn2.housemate.model;

import cscie97.asn1.knowledge.engine.*;

import javax.management.Query;
import java.util.*;
import java.util.function.Consumer;


/**
 *
 * Smart 'Device' within a home configuration. A device has a collection of "Features" which associate DeviceStates
 * with the Device. DeviceStates have two subtypes: Setting and Measure.
 * A device that supports "Settings" can be considered an Appliance and a device that supports "Measures"
 * can be considered a Sensor. A device can support both Measures and Settings, in which case it might be called an
 * Appliance, but technically it's just a Device that BOTH measures/detects things in its environment and is also
 * takes commands to perform actions.
 */
public class Device implements ConfigurationItem{

    private String name;
    private String typeName;
    private Room room;
    private Fetcher fetcher = new StandardFetcher();
    private HashMap<String, Feature> features = new HashMap<String, Feature>();


    /**
     * Constructor. Will replace white space with "_" in the name and address.
     *
     * @param name
     * @param typeName
     * @param room
     */
    public Device(String name, String typeName, Room room) {
        this.name = name.replace(" ", "_");
        this.typeName = name.replace(" ", "_");
        this.room = room;
    }

    @Override
    /**
     * Returns the room and the name of the device concatenated
     *
     * @param name
     * @param address
     * @param numFloors
     */
    public String getFqn(){
        return this.room.getFqn() + ":" + this.name;
    };

    @Override
    /**
     * Returns all attributes of the Device, as well as all of its current feature values
     */
    public ArrayList<String> getState() throws QueryEngineException {
        ArrayList<String> deviceState = this.fetcher.getState(getFqn());
        Collection<Feature> features = this.features.values();
        Iterator<Feature> featureIterator = features.iterator();
        while(featureIterator.hasNext()){
            deviceState.addAll(featureIterator.next().getState());
        }
        return deviceState;
    };

    @Override
    /**
     * Clears out stale state and sets new state.
     * All native attributes of the device.
     *
     */
    public void saveState() throws ImportException {
        Importer importer = new Importer();

        //Build the new state
        ArrayList<String> newState = new ArrayList<String>();
        newState.add(getFqn() + " is_of_type " + this.typeName);
        newState.add(getFqn() + " is_in_room " + this.room.getFqn());

        //Get the current (stale) state
        HashSet<Triple> staleState = KnowledgeGraph.getInstance().executeQuery(getFqn(), "?", "?");

        importer.removeTripleList(staleState);
        importer.importTripleList(newState);
    }

    protected String getName(){
        return this.name;
    }

    protected void addFeature(Feature feature){
        this.features.put(feature.getFqn(), feature);
    }

    protected HashMap<String, Feature> getFeatures(){
        return this.features;
    }

}
