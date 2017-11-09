package cscie97.asn3.housemate.controller;

/**
 * Valid types to pass as the type parameter when creating a new command with HouseMateControllerService
 * @see HouseMateControllerServiceImpl createCommand
 */
public enum ConfigItemCommandType {

    FEATURE_UPDATE_COMMAND, MOVE_OCCUPANT_COMMAND, SET_OCCUPANT_ACTIVITY_COMMAND;

}
