package cscie97.asn2.housemate.model;

import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.QueryEngineException;

import java.util.List;

public interface HouseMateModelService {

    //public HouseMateModelService getInstance();

    public House createHouse(String token, String name, String address, int numFloors) throws ItemExistsException, UnauthorizedException;
    public Room createRoom(String token, String houseFqn, String roomName, int floor) throws ItemExistsException, UnauthorizedException, ItemNotFoundException;
    public Measure createMeasure(String token, String name, String type) throws ItemExistsException, UnauthorizedException, ItemNotFoundException;
    public Setting createSetting(String token, String name, String type) throws ItemExistsException, UnauthorizedException, ItemNotFoundException;
    public Device createDevice(String token, String roomFqn, String name, String type) throws ItemExistsException, UnauthorizedException, ItemNotFoundException;
    public Occupant createOccupant(String token, String uniqueId, String name, String type) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, ImportException;
    public void addOccupantToHouse(String token, String occupantId, String houseFqn) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, ImportException;
    public void moveOccupant(String token, String occupantId, String roomFqn) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, ImportException;
    public Feature addFeature(String token, String deviceFqn, String deviceStateFqn) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, ImportException;
    public void updateFeature(String token, String deviceFqn, String deviceStateFqn, String value) throws UnsupportedFeatureException, UnauthorizedException, ItemNotFoundException, ImportException,InvalidDeviceStateValueException;
    public List<String> getHouse(String token, String houseFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException;
    public List<String> getRoom(String token, String roomFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException;
    public List<String> getDevice(String token, String deviceFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException;
    public List<String> getDeviceState(String token, String deviceStateFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException;


}
