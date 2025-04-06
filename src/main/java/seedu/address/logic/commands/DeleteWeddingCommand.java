package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.wedding.exceptions.WeddingNotFoundException;

/**
 * Deletes the current wedding in the model.
 */
public class DeleteWeddingCommand extends Command {
    public static final String COMMAND_WORD = "deleteWedding";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes the current wedding.\n"
        + "Parameters: n/NAME"
            + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + " Jon's Wedding";

    public static final String MESSAGE_SUCCESS = "Wedding deleted: %1$s";
    public static final String MESSAGE_NO_WEDDING = "No wedding with the name '%s' exists.";

    private final String weddingName;

    public DeleteWeddingCommand(String weddingName) {
        this.weddingName = weddingName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        try {
            model.deleteWeddingByName(weddingName);
            return new CommandResult(String.format(MESSAGE_SUCCESS, weddingName));
        } catch (WeddingNotFoundException wnfe) {
            throw new CommandException(String.format(MESSAGE_NO_WEDDING, weddingName));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof DeleteWeddingCommand;
    }
}
