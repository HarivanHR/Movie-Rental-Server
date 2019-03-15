package bgu.spl181.net.srv.bidi;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;

import bgu.spl181.net.api.MessageEncoderDecoder;
import ustbpActions.AbstractUserServiceCommand;
import ustbpActions.CommandDoesntExistError;
import ustbpActions.ErrorCommand;
import ustbpActions.LoginCommand;
import ustbpActions.SignoutCommand;
import ustbpActions.UserServiceRegisterCommand;
import ustbpActions.UserServiceRequestCommand;

public class UTBSPEncoderDecoder implements MessageEncoderDecoder<AbstractUserServiceCommand>  {
	private byte[] bytes_so_far = new byte[1 << 10];
	private int length_of_message = 0;
	
	@Override
	public AbstractUserServiceCommand decodeNextByte(byte nextByte) {
        if (nextByte == '\n') {
            return create_command();
        }
        pushByte(nextByte);
        return null; //not a line yet
	}

	@Override
	public byte[] encode(AbstractUserServiceCommand message) {
		String temp_string = message.getMessage();
		byte[] after_converting_to_bytes = (temp_string + "\n").getBytes();
		return after_converting_to_bytes;
	}
	
    private void pushByte(byte nextByte) {
        if (length_of_message >= bytes_so_far.length) {
            bytes_so_far = Arrays.copyOf(bytes_so_far, length_of_message * 2);
        }
        bytes_so_far[length_of_message++] = nextByte;
    }
	protected AbstractUserServiceCommand create_command(){
		//Creates a string to be parsed and used to create a command.
		String pre_parsed_string = new String(bytes_so_far, 0, length_of_message, StandardCharsets.UTF_8);
        length_of_message = 0;
		String[] post_parsed_command = parse_command(pre_parsed_string);
		if (post_parsed_command.length == 0){
			return new CommandDoesntExistError("No command detected");
		}
		switch (post_parsed_command[0].toLowerCase()){
			case "login": {
				return new LoginCommand(post_parsed_command);
			}
			case "register": {
				return create_registration_command(post_parsed_command);
			}
			case "signout": {
				return new SignoutCommand();
			}	
			case "request": {
				
				return create_request(post_parsed_command);
			}
			default: {
				return new CommandDoesntExistError(post_parsed_command[0] + " failed");
			}
		}
	}

	protected AbstractUserServiceCommand create_request(String[] arguments){
		return new UserServiceRequestCommand();
	}
	
	protected AbstractUserServiceCommand create_registration_command(String[] arguments){
		return new UserServiceRegisterCommand(arguments);
	}
	
	public static String[] parse_command(String unparsed_arguemt){ //TODO Change signature
		String proccessedString = unparsed_arguemt.trim();
		LinkedList<String> returned_strings = new LinkedList<>();
		char[] string_char_array = proccessedString.toCharArray();
		int number_of_words = count_num_of_words_seperated_by_char(string_char_array, ' ');
		int curr_word_num = 0;
		String argument = "";
		boolean quotation_flag = false;
		for (int i = 0; i < string_char_array.length; i++){
			char curr_char = string_char_array[i];
			if (curr_char == ' '){
				if (quotation_flag){
					argument += curr_char;
				}//Quotation flag if
				else{
					if (argument.length() != 0){
						returned_strings.add(argument);
						curr_word_num++;
						argument = "";
					}
				}
			}  
			else{
				if (curr_char == '"'  /*curr_word_num > 3*/){
					if (quotation_flag){//Time to stop quotation.
						returned_strings.add(argument);
						curr_word_num++;
						argument = "";
						quotation_flag = false;
					}
					else{ //Found a quotation for the first time.
//						if (curr_word_num > 3){
							if (argument.length() != 0 ){
								returned_strings.add(argument);
								curr_word_num++;
								argument = "";
							}
							quotation_flag = true;
//						}
					}
				}
				else{
					argument += curr_char;
				}
			}
		}
		if (argument.length() != 0){//So that the last string was inserted correctly.
			returned_strings.add(argument);
		}
		if (quotation_flag){
			throw new IllegalArgumentException("Quotation mark wasn't closed");
		}
		
		String[] returned_string = new String[returned_strings.size()];
		int i = 0;
		for (String temp_string: returned_strings){
			returned_string[i++] = temp_string.trim();
		}
		return returned_string;
	}
	//Might not need this func, however it's neat.
	public static int count_num_of_words_seperated_by_char(char[] tested_string_as_char_array, char seperator){
		int location = 0;
		int num_of_words = 0;
		boolean word_flag = false; //Flag that indicates that we're currently reading a word.
		while (location < tested_string_as_char_array.length){
			if (tested_string_as_char_array[location] == seperator){
				if (word_flag){ //You were reading a word.
					num_of_words++;
					word_flag = false; //No longer reading a word
				}
			} else { //Found something that is not the seperator.
				word_flag = true;
			}
			location++;
		}
		if (word_flag){ //Means that a word was at the end.
			num_of_words++;
		}
		return num_of_words;
	}
}
