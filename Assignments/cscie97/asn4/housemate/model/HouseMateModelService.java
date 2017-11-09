package cscie97.asn4.housemate.model;

import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.QueryEngineException;

import java.util.List;
import java.util.Observer;


/**
 * Public API that allows access to the House Mate Model Service. All operations will require an auth token, and throw
 * “UnauthorizedException” if an invalid token is passed for the requested API call.
 */
public interface HouseMateModelService {

    /**
     * Adds a house to the system.
     *
     * @param token
     * @param name
     * @param address
     * @param numFloors
     * @return
     * @throws ItemExistsException
     * @throws UnauthorizedException
     * @throws QueryEngineException
     */
    public List<String> createHouse(String token, String name, String address, String numFloors) throws ItemExistsException, UnauthorizedException, QueryEngineException;

    /**
     * Adds a room to a specific house in the system
     *
     * @param token
     * @param houseFqn
     * @param roomName
     * @param floor
     * @return
     * @throws ItemExistsException
     * @throws UnauthorizedException
     * @throws ItemNotFoundException
     * @throws QueryEngineException
     */
    public List<String> createRoom(String token, String houseFqn, String roomName, String floor, String windowCount) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, QueryEngineException;

    /**
     * Adds a mesure to the system
     *
     * @param token
     * @param name
     * @param type
     * @return
     * @throws ItemExistsException
     * @throws UnauthorizedException
     * @throws ItemNotFoundException
     * @throws QueryEngineException
     */
    public List<String> createMeasure(String token, String name, String type) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, QueryEngineException;

    /**
     * Adds a setting to the system
     * For example:
     * createSetting(auth_token, TargetTemperature, int)
     * createSetting(auth_token, DoorLock, bool)
     * createSetting(auth_token, burner_1_heat_level, “HIGH | MED | LOW”)
     *
     * @param token
     * @param name
     * @param type
     * @return
     * @throws ItemExistsException
     * @throws UnauthorizedException
     * @throws ItemNotFoundException
     * @throws QueryEngineException
     */
    public List<String> createSetting(String token, String name, String type) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, QueryEngineException;

    /**
     * Adds a device to a room.
     *
     * @param token
     * @param roomFqn
     * @param name
     * @param type
     * @return
     * @throws ItemExistsException if room already exists
     * @throws UnauthorizedException
     * @throws ItemNotFoundException if house isn't found
     * @throws QueryEngineException
     */
    public List<String> createDevice(String token, String roomFqn, String name, String type) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, QueryEngineException;


    /**
     * Creates an occupant in the system
     *
     * @param token
     * @param uniqueId
     * @param name
     * @param type
     * @return
     * @throws ItemExistsException - if occupant already exists
     * @throws UnauthorizedException
     * @throws ImportException
     */
    public List<String> createOccupant(String token, String uniqueId, String name, String type) throws ItemExistsException, UnauthorizedException, ImportException;

    /**
     * Associates an occupant with a house
     *
     * @param token
     * @param occupantId
     * @param houseFqn
     * @return
     * @throws ItemExistsException
     * @throws UnauthorizedException
     * @throws ItemNotFoundException
     * @throws ImportException
     */
    public List<String> addOccupantToHouse(String token, String occupantId, String houseFqn) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, ImportException;

    /**
     * Moves an occupatn to another room
     * @param token
     * @param occupantId
     * @param roomFqn
     * @return
     * @throws ItemExistsException
     * @throws UnauthorizedException
     * @throws ItemNotFoundException
     * @throws ImportException
     */
    public List<String> moveOccupant(String token, String occupantId, String roomFqn) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, ImportException;

    /**
     * Set activty state for an occupant
     * @param token
     * @param occupantId
     * @param activityState
     * @return
     * @throws ItemExistsException
     * @throws UnauthorizedException
     * @throws ItemNotFoundException
     * @throws ImportException
     */
    public List<String> setOccupantActivity(String token, String occupantId, Boolean activityState) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, ImportException;

    /**
     * Adds the DeviceState (Setting or Measure) at deviceStateFqn to the Device at deviceFqn as a behavior.
     * For example:
     * addFeature(auth_token, “Setting:TargetTemperature”, “House1:Room1:Oven1”)
     *
     * @param token
     * @param deviceFqn
     * @param deviceStateFqn
     * @return
     * @throws ItemExistsException
     * @throws UnauthorizedException
     * @throws ItemNotFoundException
     * @throws ImportException
     * @throws QueryEngineException
     */
    public List<String> addFeature(String token, String deviceFqn, String deviceStateFqn) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, ImportException, QueryEngineException;

    /**
     * Set the value of the deviceStateFqn on deviceFqn to value
     * For example:
     * Updating a the target temperature on an Oven:
     * updateFeature(auth_token, “House1:Room1:Oven1”, “Setting:TargetTemperature”, 350)
     * Updating a the currently detected temperature of an Oven:
     * updateFeature(auth_token, “House1:Room1:Oven1” “Measure:OvenTemperature”, 228.4)
     *
     * @param token
     * @param deviceFqn
     * @param deviceStateFqn
     * @param value
     * @return
     * @throws UnsupportedFeatureException
     * @throws UnauthorizedException
     * @throws ItemNotFoundException
     * @throws ImportException
     * @throws InvalidDeviceStateValueException
     * @throws QueryEngineException
     */
    public List<String> updateFeature(String token, String deviceFqn, String deviceStateFqn, String value) throws UnsupportedFeatureException, UnauthorizedException, ItemNotFoundException, ImportException, InvalidDeviceStateValueException, QueryEngineException;


    /**
     * Set the value of the deviceStateFqn on deviceFqn to value
     * For example:
     * Updating a the target temperature on an Oven:
     * updateFeature(auth_token, “House1:Room1:Oven1”, “Setting:TargetTemperature”, 350)
     * Updating a the currently detected temperature of an Oven:
     * updateFeature(auth_token, “House1:Room1:Oven1” “Measure:OvenTemperature”, 228.4)
     *
     * @param token
     * @param featureFqn
     * @param value
     * @return
     * @throws UnsupportedFeatureException
     * @throws UnauthorizedException
     * @throws ItemNotFoundException
     * @throws ImportException
     * @throws InvalidDeviceStateValueException
     * @throws QueryEngineException
     */
    //public List<String> updateFeature(String token, String featureFqn, String value) throws UnsupportedFeatureException, UnauthorizedException, ItemNotFoundException, ImportException, InvalidDeviceStateValueException, QueryEngineException;


    /**
     * Gets all state for a house
     *
     * @param token
     * @param houseFqn
     * @return
     * @throws UnauthorizedException
     * @throws ItemNotFoundException
     * @throws QueryEngineException
     */
    public List<String> getHouse(String token, String houseFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException;

    /**
     * Gets all state for a room as a list of triples
     * @param token
     * @param roomFqn
     * @return
     * @throws UnauthorizedException
     * @throws ItemNotFoundException
     * @throws QueryEngineException
     */
    public List<String> getRoom(String token, String roomFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException;

    /**
     * Gets all state for a device as a list of triples
     * @param token
     * @param deviceFqn
     * @return
     * @throws UnauthorizedException
     * @throws ItemNotFoundException
     * @throws QueryEngineException
     */
    public List<String> getDevice(String token, String deviceFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException;

    /**
     * Gets all state for a feature as a list of triples
     * @param token
     * @param deviceFqn
     * @param deviceStateFqn
     * @return
     * @throws UnauthorizedException
     * @throws ItemNotFoundException
     * @throws QueryEngineException
     */
    public List<String> getFeature(String token, String deviceFqn, String deviceStateFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException;


    /**
     * Gets all state for a feature as a list of triples
     * @param token
     * @param featureFqn
     * @return
     * @throws UnauthorizedException
     * @throws ItemNotFoundException
     * @throws QueryEngineException
     */
    public List<String> getFeature(String token, String featureFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException;


    /**
     * Gets all state for a deviceState as a list of triples
     * @param token
     * @param deviceStateFqn
     * @return
     * @throws UnauthorizedException
     * @throws ItemNotFoundException
     * @throws QueryEngineException
     */
    public List<String> getDeviceState(String token, String deviceStateFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException;

    /**
     * Gets all state for a occupant as a list of triples
     * @param token
     * @param occupantFqn
     * @return
     * @throws UnauthorizedException
     * @throws ItemNotFoundException
     * @throws QueryEngineException
     */
    public List<String> getOccupant(String token, String occupantFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException;

    /**
     * Gets all state for all houses as a list of triples
     * @param token
     * @return
     * @throws UnauthorizedException
     * @throws ItemNotFoundException
     * @throws QueryEngineException
     */
    public List<String> getAll(String token) throws UnauthorizedException, ItemNotFoundException,QueryEngineException;


    /**
     * Gets all state for all houses as a list of triples
     * @param token
     * @param featureFqn
     * @param observer - the Observer object to attach
     * @return
     * @throws UnauthorizedException
     * @throws ItemNotFoundException
     * @throws QueryEngineException
     */
    public void subscribeToFeature(String token, String featureFqn, Observer observer) throws UnauthorizedException, ItemNotFoundException;


    /**
     * Gets all state for all houses as a list of triples
     * @param token
     * @param featureFqn
     * @param observer - the Observer object to detach
     * @return
     * @throws UnauthorizedException
     * @throws ItemNotFoundException
     * @throws QueryEngineException
     */
    public void unsubscribeFromFeature(String token, String featureFqn, Observer observer) throws UnauthorizedException, ItemNotFoundException;


    /**
     * Removes all state for all houses
     * @param token
     * @return
     * @throws UnauthorizedException
     */
    public void removeAll(String token) throws UnauthorizedException;


}
