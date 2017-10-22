package cscie97.asn2.housemate.model;
import cscie97.asn1.knowledge.engine.ImportException;
import cscie97.asn1.knowledge.engine.KnowledgeGraph;
import cscie97.asn1.knowledge.engine.QueryEngineException;

import java.util.*;

public class HouseMateModelServiceImpl implements HouseMateModelService {

    private final String VALID_TOKEN = "1";

    //Singleton methods
    private static HouseMateModelService ourInstance = new HouseMateModelServiceImpl();
    public static HouseMateModelService getInstance() {
        return ourInstance;
    }

    //Private constructor, prevents duplicates
    private HouseMateModelServiceImpl() {
        this.configMap = new HashMap<String, ConfigurationItem>();
    }

    //Service-Level lists of items
    private HashMap<String, ConfigurationItem> configMap = new HashMap<String, ConfigurationItem>();

    public List<String> createHouse(String token, String name, String address, String numFloors) throws ItemExistsException, UnauthorizedException, QueryEngineException {

        //Authorize
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedException();
        }

        //Check if a house with the same fqn exists
        House newHouse = new House(name, address, numFloors);
        if (configMap.containsKey(newHouse.getFqn())){
            throw new ItemExistsException(newHouse.getFqn(), newHouse);
        }

        try {
            newHouse.saveState();
        }catch(ImportException ie){
            System.out.println(ie);
        }

        configMap.put(newHouse.getFqn(), newHouse);

        return newHouse.getState();

    }

    public List<String> createMeasure(String token, String name, String type) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, QueryEngineException {

        //Authorize
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedException();
        }

        //Check if a measure with the same fqn exists
        Measure newMeasure = new Measure(name, type);
        if (configMap.containsKey(newMeasure.getFqn())){
            throw new ItemExistsException(newMeasure.getFqn(), newMeasure);
        }

        try {
            newMeasure.saveState();
        }catch(ImportException ie){
            System.out.println(ie);
        }

        configMap.put(newMeasure.getFqn(), newMeasure);

        return newMeasure.getState();
    }

    public List<String> createSetting(String token, String name, String type) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, QueryEngineException {

        //Authorize
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedException();
        }

        //Check if a measure with the same fqn exists
        Setting newSetting = new Setting(name, type);
        if (configMap.containsKey(newSetting.getFqn())){
            throw new ItemExistsException(newSetting.getFqn(), newSetting);
        }

        try {
            newSetting.saveState();
        }catch(ImportException ie){
            System.out.println(ie);
        }

        configMap.put(newSetting.getFqn(), newSetting);

        return newSetting.getState();
    }

    public List<String> createRoom(String token, String houseFqn, String roomName, String floor) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, QueryEngineException {

        //Authorize
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedException();
        }

        //Check if a house with the same fqn exists
        if (!configMap.containsKey(houseFqn)){
            throw new ItemNotFoundException(houseFqn);
        }
        House targetHouse = (House)configMap.get(houseFqn);
        Room newRoom = targetHouse.addRoom(roomName, floor);
        try {
            targetHouse.saveState();
        }catch(ImportException ie){
            System.out.println(ie);
        }

        this.configMap.put(newRoom.getFqn(), (Room)newRoom);
        return newRoom.getState();

    }

    public List<String> createOccupant(String token, String occupantId, String name, String type) throws ItemExistsException, UnauthorizedException, ImportException {

        //Authorize
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedException();
        }

        //Check if an occupant with the same fqn exists
        String occupantFqn = "Occupant:" + occupantId;
        if (configMap.containsKey(occupantFqn)){
            throw new ItemExistsException(occupantFqn, configMap.get(occupantFqn));
        }

        //If we get here create occupant
        Occupant newOccupant = new Occupant(occupantId,name,type);
        this.configMap.put(newOccupant.getFqn(), newOccupant);
        newOccupant.saveState();
        return newOccupant.getState();

    }

    public List<String> addOccupantToHouse(String token, String occupantId, String houseFqn) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, ImportException {

        //Authorize
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedException();
        }

        //Check if an occupant with the same fqn exists
        String occupantFqn = "Occupant:" + occupantId;
        if (!configMap.containsKey(occupantFqn)){
            throw new ItemNotFoundException(occupantFqn);
        }

        //Check if the house exists
        if (!configMap.containsKey(houseFqn)){
            throw new ItemNotFoundException(houseFqn);
        }

        //If we get here, add the occupant to the house
        Occupant occupant = (Occupant)configMap.get(occupantFqn);
        House house = (House)configMap.get(houseFqn);
        occupant.addHouse(house);
        occupant.saveState();
        house.addOccupant(occupant);
        house.saveState();

        return occupant.getState();

    }

    public List<String> moveOccupant(String token, String occupantId, String roomFqn) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, ImportException {

        //Authorize
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedException();
        }

        //Check if an occupant with the same fqn exists
        String occupantFqn = "Occupant:" + occupantId;
        if (!configMap.containsKey(occupantFqn)){
            throw new ItemNotFoundException(occupantFqn);
        }

        //Check if the room exists
        if (!configMap.containsKey(roomFqn)){
            throw new ItemNotFoundException(roomFqn);
        }

        //OK then, move the occupant.
        Occupant occupant = (Occupant)configMap.get(occupantFqn);
        Room room = (Room)configMap.get(roomFqn);
        occupant.setRoom(room);
        room.addOccupant(occupant);
        occupant.saveState();
        room.saveState();

        return occupant.getState();

    }

    public List<String> createDevice(String token, String roomFqn, String deviceName, String deviceTypeName) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, QueryEngineException {

        //Authorize
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedException();
        }

        //Check if a room with the provided fqn exists
        if (!this.configMap.containsKey(roomFqn)){
            throw new ItemNotFoundException(roomFqn);
        }

        Room targetRoom = (Room)this.configMap.get(roomFqn);
        Device newDevice = targetRoom.addDevice(deviceName, deviceTypeName);

        try {
            targetRoom.saveState();
            newDevice.saveState();
        }catch(ImportException ie){
            System.out.println(ie);
        }

        this.configMap.put(newDevice.getFqn(), newDevice);
        return newDevice.getState();

    }

    public List<String> addFeature(String token, String deviceFqn, String deviceStateFqn) throws ItemExistsException, UnauthorizedException, ItemNotFoundException, QueryEngineException {

        //Authorize
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedException();
        }

        //Check for device
        if (!this.configMap.containsKey(deviceFqn)){
            throw new ItemNotFoundException(deviceFqn);
        }
        Device targetDevice = (Device)this.configMap.get(deviceFqn);

        //Check for available device state (either a setting or a measure)
        if (!this.configMap.containsKey(deviceStateFqn)){
            throw new ItemNotFoundException(deviceStateFqn);
        }
        DeviceState targetDeviceState = (DeviceState)this.configMap.get(deviceStateFqn);

        //Check that device isn't already supporting this feature
        String featureFqn = deviceFqn + ":" + deviceStateFqn;
        if (this.configMap.containsKey(featureFqn)){
            ConfigurationItem existingItem = this.configMap.get(featureFqn);
            if (existingItem.getClass() == Feature.class){
                System.out.println("Support for " + deviceStateFqn + " has already been enabled on " + deviceFqn + ". No action taken.");
                return existingItem.getState();
            }
        }

        //If we get here, add the feature to the device
        Feature newFeature = new Feature(targetDeviceState, targetDevice);
        targetDevice.addFeature(newFeature);
        try {
            newFeature.saveState();
        }catch(ImportException ie){
            System.out.println(ie);
        }
        this.configMap.put(newFeature.getFqn(), newFeature);
        return newFeature.getState();

    }

    public List<String> updateFeature(String token, String deviceFqn, String deviceStateFqn, String value) throws UnsupportedFeatureException, UnauthorizedException, ItemNotFoundException, ImportException, InvalidDeviceStateValueException, QueryEngineException {

        //Authorize
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedException();
        }

        //Check for device
        if (!this.configMap.containsKey(deviceFqn)){
            throw new ItemNotFoundException(deviceFqn);
        }
        Device targetDevice = (Device)this.configMap.get(deviceFqn);

        //Check that device supports this feature
        String featureFqn = deviceFqn + ":" + deviceStateFqn;
        if (!this.configMap.containsKey(featureFqn)){
            throw new UnsupportedFeatureException(deviceFqn, deviceStateFqn, targetDevice.getFeatures().keySet());
        }

        //If we get here, set the value
        Feature targetFeature = (Feature)this.configMap.get(featureFqn);
        targetFeature.setValue(value);
        targetFeature.saveState();

        return targetDevice.getState();


    }



    public List<String> getHouse(String token, String houseFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException{
        //Authorize
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedException();
        }

        //Check for device
        if (!this.configMap.containsKey(houseFqn)){
            throw new ItemNotFoundException(houseFqn);
        }
        return this.configMap.get(houseFqn).getState();
    }

    public List<String> getRoom(String token, String roomFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException{
        //Authorize
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedException();
        }

        //Check for device
        if (!this.configMap.containsKey(roomFqn)){
            throw new ItemNotFoundException(roomFqn);
        }
        return this.configMap.get(roomFqn).getState();
    }

    public List<String> getDevice(String token, String deviceFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException{
        //Authorize
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedException();
        }

        //Check for device
        if (!this.configMap.containsKey(deviceFqn)){
            throw new ItemNotFoundException(deviceFqn);
        }
        return this.configMap.get(deviceFqn).getState();
    }

    public List<String> getOccupant(String token, String occupantFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException{
        //Authorize
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedException();
        }

        //Check for device
        if (!this.configMap.containsKey(occupantFqn)){
            throw new ItemNotFoundException(occupantFqn);
        }
        return this.configMap.get(occupantFqn).getState();
    }

    public List<String> getDeviceState(String token, String deviceStateFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException{
        //Authorize
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedException();
        }

        //Check for device
        if (!this.configMap.containsKey(deviceStateFqn)){
            throw new ItemNotFoundException(deviceStateFqn);
        }
        return this.configMap.get(deviceStateFqn).getState();
    }

    public List<String> getFeature(String token, String featureFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException{

        //Authorize
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedException();
        }

        //Check for feature with this FQN
        if (!this.configMap.containsKey(featureFqn)){
            throw new ItemNotFoundException(featureFqn);
        }
        return this.configMap.get(featureFqn).getState();

    }

    public List<String> getFeature(String token, String deviceFqn, String deviceStateFqn) throws UnauthorizedException, ItemNotFoundException,QueryEngineException{

        //Authorize
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedException();
        }

        String featureFqn = deviceFqn + ":" + deviceStateFqn;
        return getFeature(token, featureFqn);

    }

    public void subscribeToFeature(String token, String featureFqn, Observer o) throws UnauthorizedException, ItemNotFoundException{
        //Authorize
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedException();
        }

        //Check for feature with this FQN
        if (!this.configMap.containsKey(featureFqn)){
            throw new ItemNotFoundException(featureFqn);
        }
        ConfigurationItem targetFeature = this.configMap.get(featureFqn);
        ((Feature)targetFeature).addObserver(o);
    }


    public void unsubscribeFromFeature(String token, String featureFqn, Observer o) throws UnauthorizedException, ItemNotFoundException{
        //Authorize
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedException();
        }

        //Check for feature with this FQN
        if (!this.configMap.containsKey(featureFqn)){
            throw new ItemNotFoundException(featureFqn);
        }
        ConfigurationItem targetFeature = this.configMap.get(featureFqn);
        ((Feature)targetFeature).deleteObserver(o);
    }


    public List<String> getAll(String token) throws UnauthorizedException, ItemNotFoundException,QueryEngineException{
        //Authorize
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedException();
        }

        List<String> allState = new ArrayList<String>();
        Iterator<ConfigurationItem> configItemIterator = configMap.values().iterator();
        while(configItemIterator.hasNext()){
            allState.addAll(configItemIterator.next().getState());
        }
        return allState;

    }

    public void removeAll(String token) throws UnauthorizedException {
        //Authorize
        if (!token.contentEquals(VALID_TOKEN)){
            throw new UnauthorizedException();
        }

        configMap.clear();
        KnowledgeGraph.getInstance().removeAllTriples();

    }

}
