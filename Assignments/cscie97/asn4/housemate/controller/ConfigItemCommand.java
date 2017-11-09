package cscie97.asn4.housemate.controller;

public abstract class ConfigItemCommand implements Command {

    String _commandString;

    public ConfigItemCommand(String commandString){
        _commandString = commandString;
    }

    public void execute(Invoker i) throws CommandExecuteException {
    }

    public String getCommandId() {
        return null;
    }
}
