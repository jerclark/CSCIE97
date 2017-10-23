package cscie97.asn2.housemate.model;
import cscie97.asn1.knowledge.engine.*;

import java.util.*;
import java.util.function.Consumer;

/**
 * Room within a house configuration.
 */
public class Room implements ConfigurationItem {

    private String name;
    private String floor;
    private HashMap<String, Device> devices = new HashMap<String, Device>();
    private HashMap<String, Occupant> occupants = new HashMap<String, Occupant>();
    private House house;
    private Fetcher fetcher = new StandardFetcher();

    /**
     * Constructor. Will replace " " with "_" in room name.
     * @param name
     * @param floor
     * @param house
     */
    public Room(String name, String floor, House house) {
        this.name = name.replace(" ", "_");
        this.floor = floor;
        this.house = house;
    }

    @Override
    /**
     * Returns the house:room as an FQN
     */
    public String getFqn(){
        return this.house.getFqn() + ":" + this.name;
    };

    @Override
    /**
     * Gets all attributes of the room, as well as its devices state as a list of triples
     */
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
    /**
     * Clears stale state and updates with new state
     * Sets state of all attributes as well as device and occupant information
     */
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
        updatedState.add(getFqn() + " has_occupant_count " + this.occupants.size());

        //Get the stale state as the result of a query
        HashSet<Triple> currentState = KnowledgeGraph.getInstance().executeQuery(getFqn(), "?", "?");

        //Remove the 'stale state' and add the 'updated state'
        importer.removeTripleList(currentState);
        importer.importTripleList(updatedState);
    }


    protected String getName(){
        return this.name;
    }


    /**
     * Adds a device to the room
     * @param deviceName
     * @param deviceTypeName
     * @return
     * @throws ItemExistsException
     */
    protected Device addDevice(String deviceName, String deviceTypeName) throws ItemExistsException{
        Device newDevice = new Device(deviceName, deviceTypeName, this);
        if (this.devices.containsKey(newDevice.getFqn())){
            throw new ItemExistsException(newDevice.getFqn(), newDevice);
        }
        this.devices.put(newDevice.getFqn(), newDevice);
        return newDevice;
    }

    /**
     * Adds an occupant to the room
     * @param occupant
     * @throws ItemExistsException
     */
    protected void addOccupant(Occupant occupant) throws ItemExistsException{
        if (this.occupants.containsKey(occupant.getFqn())){
            System.out.println("Occupant " + occupant.getFqn() + " is already located in room " + this.getFqn());
        }
        this.occupants.put(occupant.getFqn(), occupant);
    }

    /**
     * Adds an occupant to the room
     * @param occupant
     * @throws ItemNotFoundException
     */
    protected void removeOccupant(Occupant occupant) throws ItemExistsException{
        if (!this.occupants.containsKey(occupant.getFqn())){
            System.out.println("Occupant " + occupant.getFqn() + " is not found in room " + this.getFqn());
        }
        this.occupants.remove(occupant.getFqn());
    }

}