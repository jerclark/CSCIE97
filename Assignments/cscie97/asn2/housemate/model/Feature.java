package cscie97.asn2.housemate.model;

import cscie97.asn1.knowledge.engine.*;
import org.omg.CORBA.DynAnyPackage.Invalid;

import java.util.ArrayList;
import java.util.HashSet;

public class Feature implements ConfigurationItem{

    private DeviceState deviceState = null;
    private Device device = null;
    private Fetcher fetcher = new StandardFetcher();
    private String value = null;

    public Feature(DeviceState ds, Device d){
        this.deviceState = ds;
        this.device = d;
        this.value = "INITIAL_VALUE_UNDEFINED";
    }

    @Override
    public String getFqn(){
        return this.device.getFqn() + ":" + this.deviceState.getFqn();
    };

    @Override
    public ArrayList<String> getState() throws QueryEngineException {
        return this.fetcher.getState(getFqn());
    };

    @Override
    public void saveState() throws ImportException {

        ArrayList<String> newState = new ArrayList<String>();
        String valuePredicate = " INVALID ";
        Class deviceStateClass = this.deviceState.getClass();
        if (deviceStateClass == Setting.class){
            valuePredicate = " is_set_to ";
        }else if (deviceStateClass == Measure.class){
            valuePredicate = " is_detecting ";
        }
        newState.add(getFqn() + valuePredicate + this.value);

        //Get the current (stale) state
        HashSet<Triple> staleState = KnowledgeGraph.getInstance().executeQuery(getFqn(), "?", "?");

        //Remove stale state and add new state
        Importer importer = new Importer();
        importer.removeTripleList(staleState);
        importer.importTripleList(newState);
    }

    public void setName(String name) throws ImportException { }

    protected String getName(){
        return this.getFqn();
    }

    public void setValue(String value) throws InvalidDeviceStateValueException {
        if (!this.deviceState.validateValue(value)){
            throw new InvalidDeviceStateValueException(this.deviceState.getFqn(), this.deviceState.valueType, value);
        }
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

}
