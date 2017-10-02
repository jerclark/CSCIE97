package cscie97.asn2.housemate.model;
import cscie97.asn1.knowledge.engine.*;

import java.util.*;
import java.util.function.Consumer;

public class Room implements ConfigurationItem {

    private String name;
    private int floor;
    private HashMap<String, Device> devices = new HashMap<String, Device>();
    private HashMap<String, Occupant> occupants = new HashMap<String, Occupant>();
    private House house;
    private Fetcher fetcher = new StandardFetcher();


    public Room(String name, int floor, House house) {
        this.name = name.replace(" ", "_");
        this.floor = floor;
        this.house = house;
    }

    @Override
    public String getFqn(){
        return this.house.getFqn() + ":" + this.name;
    };

    @Override
    public ArrayList<String> getState() throws QueryEngineException {
        ArrayList<String> roomState = this.fetcher.getState(getFqn());
        Collection<Device> devices = this.devices.values();
        Iterator<Device> deviceIterator = devices.iterator();
        while(deviceIterator.hasNext()){
            roomState.addAll(deviceIterator.next().getState());
        }
        return roomState;
    };

    @Override
    public void saveState() throws ImportException {
        Importer importer = new Importer();

        //Build the new state as a list of triple strings
        ArrayList<String> updatedState = new ArrayList<String>();;
        updatedState.add(getFqn() + " has_name " + this.name);
        updatedState.add(getFqn() + " is_on_floor " + this.floor);
        Consumer<Device> getDeviceTriple = (device) -> {
            updatedState.add(getFqn() + " has_device " + device.getName());
        };
        this.devices.values().forEach(getDeviceTriple);
        Consumer<Occupant> getOccupantTriple = (occupant) -> {
            updatedState.add(getFqn() + " has_occupant " + occupant.getName());
        };
        this.occupants.values().forEach(getOccupantTriple);

        //Get the stale state as the result of a query
        HashSet<Triple> currentState = KnowledgeGraph.getInstance().executeQuery(getFqn(), "?", "?");

        //Remove the 'stale state' and add the 'updated state'
        importer.removeTripleList(currentState);
        importer.importTripleList(updatedState);
    }

    public void setName(String name) throws ImportException {
        if (name.contentEquals(this.name)) {
            return;
        }
        this.name = name;
        saveState();
    }

    protected String getName(){
        return this.name;
    }


    protected Device addDevice(String deviceName, String deviceTypeName) throws ItemExistsException{
        Device newDevice = new Device(deviceName, deviceTypeName, this);
        if (this.devices.containsKey(newDevice.getFqn())){
            throw new ItemExistsException(newDevice.getFqn(), newDevice);
        }
        this.devices.put(newDevice.getFqn(), newDevice);
        return newDevice;
    }

    protected void addOccupant(Occupant occupant) throws ItemExistsException{
        if (this.occupants.containsKey(occupant.getFqn())){
            System.out.println("Occupant " + occupant.getFqn() + " is already located in room " + this.getFqn());
        }
        this.occupants.put(occupant.getFqn(), occupant);
    }

}