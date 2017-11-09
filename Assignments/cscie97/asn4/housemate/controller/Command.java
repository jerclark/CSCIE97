package cscie97.asn4.housemate.controller;

/**
 * A command interface, exposing a single method: execute(). Abstraction used by various command
 * subtype, will be invoked by 'Invoker' objects.
 */

public interface Command {

	/**
	 * Called to execute the command. Actual command behavior will be defined by implemented by the implementors. Takes an Invoker object as a parameter so the invoker’s commandContext() method can be called to possible inform the execution behavior.
	 * @param i Inovker
	 * @throws CommandExecuteException
	 */
	public abstract void execute(Invoker i) throws CommandExecuteException;

	/**
	 * Returns commandId string as implemented by subtypes
	 * @throws CommandExecuteException
	 */
	public abstract String getCommandId();

}
