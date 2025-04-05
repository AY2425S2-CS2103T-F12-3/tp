package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_NO_CURRENT_WEDDING;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_TABLE_ID;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.exceptions.NoCurrentWeddingException;
import seedu.address.model.table.exceptions.TableNotFoundException;

/**
 * Deletes a table from the current wedding.
 */
public class DeleteTableCommand extends Command {
    public static final String COMMAND_WORD = "deleteTable";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes a table from the current wedding.\n"
            + "Parameters: tid/TABLE_ID or just the ID\n"
            + "Example: " + COMMAND_WORD + " tid/1 or 1";

    public static final String MESSAGE_SUCCESS = "Table deleted: Table ID %d";
    public static final String MESSAGE_INVALID_TABLE_ID = "Table ID must be a positive integer.";

    private final int tableId;

    /**
     * Creates a DeleteTableCommand to delete the table with the given ID.
     *
     * @param tableId ID of the table to delete
     */
    public DeleteTableCommand(int tableId) {
        this.tableId = tableId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        try {
            model.deleteTableById(tableId);
        } catch (TableNotFoundException tnfe) {
            throw new CommandException(String.format(MESSAGE_UNKNOWN_TABLE_ID, tableId));
        } catch (NoCurrentWeddingException ncwe) {
            throw new CommandException(MESSAGE_NO_CURRENT_WEDDING);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, tableId));
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof DeleteTableCommand
                && tableId == ((DeleteTableCommand) other).tableId);
    }
}

