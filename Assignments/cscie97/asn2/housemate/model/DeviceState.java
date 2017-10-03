package cscie97.asn2.housemate.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

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
        if (valueType == "String"){
            return true;
        }
        return false;
    }


    /**
     *
     * @param aValue
     * @return String - Prepares the value for display based on the regiestered type
     */
//    public String prepareValue(String aValue) throws InvalidDeviceStateValueException {
//        if (valueType.contentEquals("Integer")){
//            try{
//                Integer valueInt = Integer.valueOf(aValue);
//                return true;
//            }catch(NumberFormatException nfe){
//                throw new InvalidDeviceStateValueException(this.getFqn(), this.valueType, aValue);
//            }
//        }
//        if (valueType.contentEquals("Float")){
//            try{
//                Float valueFloat = Float.valueOf(aValue);
//                return true;
//            }catch(NumberFormatException nfe){
//                throw new InvalidDeviceStateValueException(this.getFqn(), this.valueType, aValue);
//            }
//        }
//        if (valueType.contentEquals("Boolean")){
//            if (aValue.contentEquals("0") || aValue.contentEquals("1")){
//                return true;
//            }
//            return false;
//        }
//        if (valueType.contains(ENUM_DELIMITER)){ //ENUM TYPE PASSED IN
//            String[] validValueStrings = valueType.split(Pattern.quote(ENUM_DELIMITER));
//            ArrayList<String> validValues = new ArrayList<String>(Arrays.asList(validValueStrings));
//            if (validValues.contains(aValue)){
//                return true;
//            }else{
//                throw new InvalidDeviceStateValueException(this.getFqn(), this.valueType, aValue);
//            }
//        }
//        if (valueType == "String"){
//            return true;
//        }
//        return false;
//    }


}
