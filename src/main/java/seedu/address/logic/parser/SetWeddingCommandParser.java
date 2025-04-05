package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.logic.commands.SetWeddingCommand;
import seedu.address.logic.parser.exceptions.ParseException;
/**
 * Parses input arguments and creates a new SetWeddingCommand object
 */
public class SetWeddingCommandParser implements Parser<SetWeddingCommand> {

    @Override
    public SetWeddingCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(args, PREFIX_NAME);

        if (!argMultimap.getValue(PREFIX_NAME).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetWeddingCommand.MESSAGE_USAGE));
        }

        String weddingName = argMultimap.getValue(PREFIX_NAME).get().trim();
        return new SetWeddingCommand(weddingName);
    }
}
