package ustbpActions;

public class ErrorCommand extends AbstractUserServiceCommand {
	
	public ErrorCommand(String message_info) {
		_message = "ERROR ";
		_message += message_info;
	}
		
		@Override
		public void runCommand() {}
}
