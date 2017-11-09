package cscie97.asn4.housemate.model;

import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.Importer;
import cscie97.asn1.knowledge.engine.QueryEngineException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;


/**
 * Root node of a House Mate configuration. All Rooms, Devices, and Features within the service are related to a single house.
 */
public class House implements ConfigurationItem {

    private String name;
    private final String address;
    private String numFloors;
    private HashMap<String, Room> rooms = new HashMap<String, Room>();
    private Fetcher fetcher = new StandardFetcher();
    private HashMap<String, Occupant> occupants = new HashMap<String, Occupant>();


    /**
     * Constructor. Will replace white space with "_" in the name and address.
     *
     * @param name
     * @param address
     * @param numFloors
     */
    public House(String name, String address, String numFloors){
        this.name = name.replace(" ", "_");
        this.address = address.replace(" ", "_");
        this.numFloors = numFloors;
    }

    @Override
    /**
     * Returns the name
     *
     * @param name
     * @param address
     * @param numFloors
     */
    public String getFqn(){
        return this.name;
    };

    @Override
    /**
     * Returns all attributes of the house, as well the state of all rooms
     */
    public ArrayList<String> getState() throws QueryEngineException{
        ArrayList<String> houseState = this.fetcher.getState(getFqn());
        Collection<Room> rooms = this.rooms.values();
        Iterator<Room> roomIterator= rooms.iterator();
        while(roomIterator.hasNext()){
            houseState.addAll(roomIterator.next().getState());
        }
        return houseState;
    };


    @Override
    /**
     * Clears out stale state and sets new state.
     * All attributes, as well as room names.
     */
    public void saveState() throws ImportException {
        Importer importer = new Importer();
        ArrayList<String> currentState = new ArrayList<String>();
        try {
             currentState = getState();
        }catch (QueryEngineException qe){
            System.out.println(qe);
        }
        ArrayList<String> triples = new ArrayList<String>();
        triples.add(getFqn() + " has_address " + this.address);
        triples.add(getFqn() + " has_name " + this.name);
        triples.add(getFqn() + " has_num_floors " + this.numFloors);
        Consumer<Room> getRoomTriple = (room) -> {
            triples.add(getFqn() + " has_room " + room.getName());
        };
        this.rooms.values().forEach(getRoomTriple);
        triples.removeAll(currentState);
        importer.importTripleList(triples);
    }

    /**
     * Adds an room to the house
     *
     * @param roomName
     * @param floor
     * @param windowCount
     * @return
     * @throws ItemExistsException
     */
    protected Room addRoom(String roomName, String floor, String windowCount) throws ItemExistsException{
        Room newRoom = new Room(roomName, floor, windowCount, this);
        if (this.rooms.containsKey(newRoom.getFqn())){
            throw new ItemExistsException(newRoom.getFqn(), newRoom);
        }
        this.rooms.put(newRoom.getFqn(), newRoom);
        return newRoom;
    }

    /**
     * Adds a transient occupant to the house
     *
     * @param occupant
     * @throws ItemExistsException
     */
    protected void addOccupant(Occupant occupant) throws ItemExistsException{
        if (this.occupants.containsKey(occupant.getFqn())){
            System.out.println(occupant.getFqn() + " is already a member of house " + this.getFqn());
        }
        this.occupants.put(occupant.getFqn(), occupant);
    }




}
