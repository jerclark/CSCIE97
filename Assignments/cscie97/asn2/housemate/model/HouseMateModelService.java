package cscie97.asn2.housemate.model;

import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.QueryEngineException;

import java.util.List;

public interface HouseMateModelService {

    //public HouseMateModelService getInstance();

    public List<String> createHouse(String token, String name, String address, String numFloors) throws ItemExistsException, UnauthorizedException, QueryEngineException;
    public List<String> createRoom(String token, String houseFqn, String roomName, String floor) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, QueryEngineException;
    public List<String> createMeasure(String token, String name, String type) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, QueryEngineException;
    public List<String> createSetting(String token, String name, String type) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, QueryEngineException;
    public List<String> createDevice(String token, String roomFqn, String name, String type) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, QueryEngineException;
    public List<String> createOccupant(String token, String uniqueId, String name, String type) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, ImportException;
    public List<String> addOccupantToHouse(String token, String occupantId, String houseFqn) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, ImportException;
    public List<String> moveOccupant(String token, String occupantId, String roomFqn) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, ImportException;
    public List<String> addFeature(String token, String deviceFqn, String deviceStateFqn) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, ImportException, QueryEngineException;
    public List<String> updateFeature(String token, String deviceFqn, String deviceStateFqn, String value) throws UnsupportedFeatureException, UnauthorizedException, ItemNotFoundException, ImportException, InvalidDeviceStateValueException, QueryEngineException;
    public List<String> getHouse(String token, String houseFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException;
    public List<String> getRoom(String token, String roomFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException;
    public List<String> getDevice(String token, String deviceFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException;
    public List<String> getFeature(String token, String deviceFqn, String deviceStateFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException;
    public List<String> getDeviceState(String token, String deviceStateFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException;
    public List<String> getOccupant(String token, String occupantFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException;
    public List<String> getAll(String token) throws UnauthorizedException, ItemNotFoundException,QueryEngineException;


}
