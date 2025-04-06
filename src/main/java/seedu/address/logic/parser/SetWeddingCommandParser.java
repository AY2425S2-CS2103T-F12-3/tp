package seedu.address.logic.parser;

import seedu.address.logic.commands.SetWeddingCommand;
import seedu.address.logic.parser.exceptions.ParseException;
/**
 * Parses input arguments and creates a new SetWeddingCommand object
 */
public class SetWeddingCommandParser implements Parser<SetWeddingCommand> {

    @Override
    public SetWeddingCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException("Wedding name cannot be empty.");
        }
        return new SetWeddingCommand(trimmedArgs);
    }
}
