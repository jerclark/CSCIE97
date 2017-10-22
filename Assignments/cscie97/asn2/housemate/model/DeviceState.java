package cscie97.asn2.housemate.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Abstract class representing states that can be managed by Devices in the system.
 * There are two concrete subclasses: Setting and Measure;
 * Settings allow for devices to be controlled and Measures allow for Devices to collect facts about things they measure.
 * DeviceStates are added as Features to devices.
 * If a Device Features a Setting, it can be considered an appliance; Devices featuring Measures are Sensors.
 * Devices with both can be considered appliances for all intents and purposes.
 * DeviceStates are stored in the House Mate System being deployed, and can be reused to define Features across
 * Devices in different houses.
 */
public abstract class DeviceState implements ConfigurationItem {

    final String name;
    final String valueType;
    final String ENUM_DELIMITER = "|";

    public DeviceState(String name, String valueType){
        this.name = name;
        this.valueType = valueType;
    }

    /**
     *
     * @param aValue
     * @return boolean - is the value valid for this device state
     */
    protected boolean validateValue(String aValue) throws InvalidDeviceStateValueException {
        if (valueType.contentEquals("Integer")){
            try{
                Integer valueInt = Integer.valueOf(aValue);
                return true;
            }catch(NumberFormatException nfe){
                throw new InvalidDeviceStateValueException(this.getFqn(), this.valueType, aValue);
            }
        }
        if (valueType.contentEquals("Float")){
            try{
                Float valueFloat = Float.valueOf(aValue);
                return true;
            }catch(NumberFormatException nfe){
                throw new InvalidDeviceStateValueException(this.getFqn(), this.valueType, aValue);
            }
        }
        if (valueType.contentEquals("Boolean")){
            if (aValue.contentEquals("0") || aValue.contentEquals("1")){
                return true;
            }
            return false;
        }
        if (valueType.contains(ENUM_DELIMITER)){ //ENUM TYPE PASSED IN
            String[] validValueStrings = valueType.split(Pattern.quote(ENUM_DELIMITER));
            ArrayList<String> validValues = new ArrayList<String>(Arrays.asList(validValueStrings));
            if (validValues.contains(aValue)){
                return true;
            }else{
                throw new InvalidDeviceStateValueException(this.getFqn(), this.valueType, aValue);
            }
        }
        if (valueType.contentEquals("String")){
            return true;
        }
        return false;
    }




}
