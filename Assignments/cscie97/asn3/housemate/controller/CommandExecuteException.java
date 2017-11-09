package cscie97.asn3.housemate.controller;

/**
 * Thrown when trying to execute a command that fails. Since commands have dependencies on the HMMS, this will wrap exceptions coming from that service into a CommandExecutionException and display the commandId of the failing command.
 */
public class CommandExecuteException extends Throwable {

    /**
     *
     * @param e - the exception that this exception is wrapping
     * @param c - The command that threw this exception. Used to display the command ID.
     */
    public CommandExecuteException(Exception e, Command c) {
        super("There was an issue executing the command: " + c.getCommandId() + ". The underlying issue was: " + e);
    }

}
