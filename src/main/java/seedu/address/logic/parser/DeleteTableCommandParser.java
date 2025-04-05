package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_TABLE_ID;

import seedu.address.logic.commands.DeleteTableCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteTableCommand object.
 */
public class DeleteTableCommandParser implements Parser<DeleteTableCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the DeleteTableCommand
     * and returns a DeleteTableCommand object for execution.
     * @throws ParseException if the user input does not conform to expected format.
     */
    @Override
    public DeleteTableCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TABLE_ID);

        // Handle prefixed format: deleteTable tableId/2
        if (argMultimap.getValue(PREFIX_TABLE_ID).isPresent()) {
            return parseWithPrefix(argMultimap);
        }

        // Handle raw format: deleteTable 2
        String[] tokens = args.trim().split("\\s+");

        if (tokens.length != 1) {
            throw new ParseException(
                    "Too many arguments provided.\n\n"
                            + "Usage: " + DeleteTableCommand.MESSAGE_USAGE
            );
        }

        int tableId = ParserUtil.parseTableId(tokens[0]);
        return new DeleteTableCommand(tableId);
    }

    private DeleteTableCommand parseWithPrefix(ArgumentMultimap argMultimap) throws ParseException {
        int tableId = ParserUtil.parseTableId(argMultimap.getValue(PREFIX_TABLE_ID).get());
        return new DeleteTableCommand(tableId);
    }
}

