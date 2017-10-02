package cscie97.asn2.housemate.model;
import com.sun.tools.internal.ws.wsdl.document.Import;
import cscie97.asn1.knowledge.engine.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;


public class House implements ConfigurationItem {

    private String name;
    private final String address;
    private int numFloors;
    private HashMap<String, Room> rooms = new HashMap<String, Room>();
    private Fetcher fetcher = new StandardFetcher();
    private HashMap<String, Occupant> occupants = new HashMap<String, Occupant>();



    public House(String name, String address, int numFloors){
        this.name = name.replace(" ", "_");
        this.address = address.replace(" ", "_");
        this.numFloors = numFloors;
    }

    @Override
    public String getFqn(){
        return this.name + this.address;
    };

    @Override
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
        Consumer<Room> getRoomTriple = (room) -> {
            triples.add(getFqn() + " has_room " + room.getName());
        };
        this.rooms.values().forEach(getRoomTriple);
        triples.removeAll(currentState);
        importer.importTripleList(triples);
    }

    public void setName(String name) throws ImportException {
        if (name.contentEquals(this.name)) {
            return;
        }
        this.name = name;
        saveState();
    }

    protected Room addRoom(String roomName, int floor) throws ItemExistsException{
        Room newRoom = new Room(roomName, floor, this);
        if (this.rooms.containsKey(newRoom.getFqn())){
            throw new ItemExistsException(newRoom.getFqn(), newRoom);
        }
        this.rooms.put(newRoom.getFqn(), newRoom);
        return newRoom;
    }

    protected void addOccupant(Occupant occupant) throws ItemExistsException{
        if (this.occupants.containsKey(occupant.getFqn())){
            System.out.println(occupant.getFqn() + " is already a member of house " + this.getFqn());
        }
        this.occupants.put(occupant.getFqn(), occupant);
    }




}
