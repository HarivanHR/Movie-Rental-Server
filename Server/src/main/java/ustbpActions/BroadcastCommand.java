package ustbpActions;


public class BroadcastCommand extends AbstractUserServiceCommand {

	public BroadcastCommand(String message_info) {
			_message = "BROADCAST ";
			_message += message_info;
		}
		
		@Override
		public void runCommand() {}
}
