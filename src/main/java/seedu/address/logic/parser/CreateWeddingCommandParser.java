package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.CreateWeddingCommand;
import seedu.address.logic.parser.exceptions.ParseException;
/**
 * Parses input arguments and creates a new CreateWeddingCommand object
 */
public class CreateWeddingCommandParser {
    /**
     * Parses the given {@code String} of arguments in the context of the CreateWeddingCommand
     * and returns a CreateWeddingCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public CreateWeddingCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CreateWeddingCommand.MESSAGE_USAGE));
        }
        return new CreateWeddingCommand(trimmedArgs);
    }
}
