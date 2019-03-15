package ustbpActions;


public class AckCommand extends AbstractUserServiceCommand {

	
	public AckCommand(String message_info) {
		_message = "ACK ";
		_message += message_info;
	}
	
	@Override
	public void runCommand() {
		
	}

}
