package cscie97.asn3.housemate.controller;

public class CommandExecuteException extends Throwable {

    public CommandExecuteException(Exception e) {
        super("There was an issue executing the command. The underlying issue was: " + e);
    }

}
