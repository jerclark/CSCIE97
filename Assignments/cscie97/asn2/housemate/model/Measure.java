package cscie97.asn2.housemate.model;

import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.Importer;
import cscie97.asn1.knowledge.engine.QueryEngineException;

import java.util.ArrayList;

public class Measure extends DeviceState {

    private Fetcher fetcher = new StandardFetcher();

    public Measure(String name, String valueType){
        super(name, valueType);
    }

    public String getFqn(){
        return "Measure:" + this.name;
    };

    public ArrayList<String> getState() throws QueryEngineException{
        return fetcher.getState(getFqn());
    };

    public void saveState() throws ImportException{
        Importer importer = new Importer();
        ArrayList<String> triples = new ArrayList<String>();
        triples.add(getFqn() + " has_name " + this.name);
        triples.add(getFqn() + " accepts_values " + this.valueType.toString());
        importer.importTripleList(triples);
    };

    public void setName(String newName) throws ImportException{
    };


}
