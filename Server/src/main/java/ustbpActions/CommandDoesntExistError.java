package ustbpActions;

public class CommandDoesntExistError extends AbstractUserServiceCommand {
	String message;
	public CommandDoesntExistError(String add_to_error) {
		message = add_to_error;
	}
	@Override
	public void runCommand() {
		AbstractUserServiceCommand error = new ErrorCommand(message);
		_myUSTBP_protocol.get_connections().send(_myUSTBP_protocol.get_connectionID(), error);
	}
}