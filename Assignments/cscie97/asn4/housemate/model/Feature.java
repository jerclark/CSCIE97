package cscie97.asn4.housemate.model;

import cscie97.asn1.knowledge.engine.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;


/**
 * An association class representing the availability of a DeviceState on a Device.
 * It holds the state 'value' of a DeviceState on that Device. For example, if a Device measures "temperature",
 * a Device (probably named ‘thermometer’ but it can be named anything) has a Feature that relates a Measure object
 * with name=“temperature” as its DeviceState.
 * The current value of that feature would be the temperature of the thermometer.
 */
public class Feature extends Observable implements ConfigurationItem {

    private DeviceState deviceState = null;
    private Device device = null;
    private Fetcher fetcher = new StandardFetcher();
    private String value = null;

    public Feature(DeviceState ds, Device d) {
        this.deviceState = ds;
        this.device = d;
        this.value = "INITIAL_VALUE_UNDEFINED";

        Importer importer = new Importer();
        List<String> deviceSupportTriple = new ArrayList<String>();
        deviceSupportTriple.add(this.deviceState.getFqn() + " is_supported_by " + this.device.getFqn());
        try {
            importer.importTripleList(deviceSupportTriple);
        } catch (ImportException e) {
            System.err.println(e);
        }
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
        setChanged();
        notifyObservers();
    }

    public void setName(String name) throws ImportException { }

    protected String getName(){
        return this.getFqn();
    }

    public void setValue(String value) throws InvalidDeviceStateValueException {
        if (!this.deviceState.validateValue(value)){
            String availableFeatures = this.device.getFeatures().toString();
            throw new InvalidDeviceStateValueException(this.deviceState.getFqn(), this.deviceState.valueType, value);
        }
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

}
