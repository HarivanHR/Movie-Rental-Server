package bgu.spl181.net.srv.bidi;

import bbActions.AddBalanceCommand;
import bbActions.AddMovieAdminCommand;
import bbActions.BalanceInfoCommand;
import bbActions.BlockBusterRegisterCommand;
import bbActions.ChangePriceAdminCommand;
import bbActions.MovieInfoCommand;
import bbActions.RemoveMovieAdminCommand;
import bbActions.RentMovieCommand;
import bbActions.ReturnMovieCommand;
import ustbpActions.AbstractUserServiceCommand;
import ustbpActions.CommandDoesntExistError;
import ustbpActions.ErrorCommand;

public class BBPEncoderDecoder extends UTBSPEncoderDecoder {
	
	@Override
	protected AbstractUserServiceCommand create_request(String[] arguments){
		if (arguments.length < 2){
			return new CommandDoesntExistError(arguments[0] + "failed");
		}
		if (arguments.length >= 3){
			arguments[2] = arguments[2].replace('"', ' '); //TODO Check this shait.
			arguments[2] = arguments[2].trim();
		}
		String temp_args = arguments[1];
		switch (arguments[1].toLowerCase()){
		case "balance":{
			String balance_command = null;
			if (arguments.length >= 3){
				balance_command = arguments[2].toLowerCase();
			}
			if (balance_command == null){
				return new CommandDoesntExistError("no balance suffix provided"); //TODO edit here to correct print
			}
			if ("info".equals(balance_command)){
				return new BalanceInfoCommand();
			}
			else if ("add".equals(balance_command)){
				return new AddBalanceCommand(arguments);
			} 
			else{
				return new CommandDoesntExistError("Request balance "+ temp_args  +" doesn't exist");//TODO edit here to correct print
			}
		}
		case "info":{
			return new MovieInfoCommand(arguments);
		}
		case "rent":{
			return new RentMovieCommand(arguments);
		}
		case "return":{
			return new ReturnMovieCommand(arguments);
		}
		case "addmovie": {
			return new AddMovieAdminCommand(arguments);
		}
		case "remmovie": {
			return new RemoveMovieAdminCommand(arguments);
		}
		case "changeprice": {
			return new ChangePriceAdminCommand(arguments);
		}
		
		default: {
			return new CommandDoesntExistError("Request " + temp_args +  " doesn't exist.");
		}
		}
	}
	
	@Override
	protected BlockBusterRegisterCommand create_registration_command(String[] arguments){
		return new BlockBusterRegisterCommand(arguments);
	}
	
}
