package cscie97.asn4.housemate.model;

import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.Importer;
import cscie97.asn1.knowledge.engine.QueryEngineException;

import java.util.ArrayList;

/**
 * Represents measured or detected "State" of a Device - effectively making it a Sensor.
 * The key difference from sibling subclass "Setting" is in the implementation of "saveState()" as it uses a
 * different predicate to represent the facts in the KnowledgeBase.
 */
public class Measure extends DeviceState {

    private Fetcher fetcher = new StandardFetcher();

    public Measure(String name, String valueType){
        super(name, valueType);
    }

    /**
     * * Returns "Measure:" + setting name
     * @return
     */
    public String getFqn(){
        return "Measure:" + this.name;
    };

    public ArrayList<String> getState() throws QueryEngineException{
        return fetcher.getState(getFqn());
    };

    /**
     * Saves the state of the measure
     * @throws ImportException
     */
    public void saveState() throws ImportException{
        Importer importer = new Importer();
        ArrayList<String> triples = new ArrayList<String>();
        triples.add(getFqn() + " has_name " + this.name);
        triples.add(getFqn() + " accepts_values " + this.valueType.toString());
        importer.importTripleList(triples);
    };



}
