package cscie97.asn3.housemate.controller;

public interface Command {

	public abstract void execute(Invoker i) throws CommandExecuteException;
	public abstract String getCommandId();

}
