package cscie97.asn2.housemate.model;
import cscie97.asn1.knowledge.engine.*;

import java.util.*;
import java.util.function.Consumer;

/**
 * An Occupant within the system. Occupants are of various types, which can be entitled to different
 * interactions with the HouseMate System.
 */
public class Occupant implements ConfigurationItem {

    private String id = null;
    private String name = null;
    private String type = null;
    private HashMap<String, House> houses = new HashMap<String,House>();
    private Room room = null;
    private Fetcher fetcher = new StandardFetcher();
    private Boolean isActive = true;

    public Occupant(String id, String name, String type){
        this.id = id.replace(" ", "_").replace(".", "_");
        this.name = name.replace(" ", "_");
        this.type = type;
    }

    @Override
    public String getFqn() {
        return "Occupant:" + id;
    }

    @Override
    public ArrayList<String> getState() {
        return this.fetcher.getState(getFqn());
    }

    @Override
    /**
     * Clear out stale state, and save new state.
     * Saves all native attributes, as well as the room it's in and the houses its part of.
     */
    public void saveState() throws ImportException {

        HashSet<Triple> staleState = KnowledgeGraph.getInstance().executeQuery(getFqn(), "?", "?");
        Importer importer = new Importer();
        importer.removeTripleList(staleState);

        //Build the new state
        ArrayList<String> newState = new ArrayList<String>();
        newState.add(getFqn() + " has_id " + this.id);
        newState.add(getFqn() + " has_name " + this.name);
        newState.add(getFqn() + " is_type " + this.type);
        newState.add(getFqn() + " is_active " + this.isActive);
        if (this.room != null) {
            newState.add(getFqn() + " is_in_room " + this.room.getFqn());
        }
        Consumer<House> getHouseTriple = (house) -> {
            newState.add(getFqn() + " is_member_of " + house.getFqn());
        };
        this.houses.values().forEach(getHouseTriple);

        //Get the current state
        importer.importTripleList(newState);
    }

    protected String getName() {
        return this.name;
    }

    protected Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }


    public void addHouse(House house) {
        this.houses.put(house.getFqn(), house);
    }
}
