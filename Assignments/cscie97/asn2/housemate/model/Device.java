package cscie97.asn2.housemate.model;

import cscie97.asn1.knowledge.engine.*;

import javax.management.Query;
import java.util.*;
import java.util.function.Consumer;

public class Device implements ConfigurationItem{

    private String name;
    private String typeName;
    private Room room;
    private Fetcher fetcher = new StandardFetcher();
    private HashMap<String, Feature> features = new HashMap<String, Feature>();


    public Device(String name, String typeName, Room room) {
        this.name = name.replace(" ", "_");
        this.typeName = name.replace(" ", "_");
        this.room = room;
    }

    @Override
    public String getFqn(){
        return this.room.getFqn() + ":" + this.name;
    };

    @Override
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

    public void setName(String name) throws ImportException {
        if (name.contentEquals(this.name)) {
            return;
        }
        this.name = name;
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
