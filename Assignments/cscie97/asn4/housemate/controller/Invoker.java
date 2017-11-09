package cscie97.asn4.housemate.controller;

/**
 * Represents an ‘Invoker’ of a command. The “Command::execute()” method will take an Invoker as a parameter. Invokers implement 1 method “commandContext()”, which will return an object that implements Context, so the command can have some data if the invoker chooses to provide some. The commandContext() provides House Mate state to commands in order to meet system requirements dictating that certain commands be dynamic (For example, to execute a command that updates all windows in a room where a voice_command was issued, we need to tell the command what room the occupant is in).
 */
public interface Invoker {

	/**
	 * Returns a “Context” object (see Interface::Context) which can provide data to the invoked command.
	 * @return a Context object to pass to commands and predicates
	 */
	public abstract Context commandContext();

}
