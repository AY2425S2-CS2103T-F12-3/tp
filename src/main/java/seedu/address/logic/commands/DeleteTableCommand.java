package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
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
    public static final String MESSAGE_NO_CURRENT_WEDDING =
            "No current wedding set. Use 'setWedding' before deleting a table.";
    public static final String MESSAGE_INVALID_TABLE_ID = "Table ID must be a positive integer.";
    public static final String MESSAGE_TABLE_NOT_FOUND = "Table with ID %d not found in the current wedding.";

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

        if (!model.hasCurrentWedding()) {
            throw new CommandException(MESSAGE_NO_CURRENT_WEDDING);
        }

        if (tableId <= 0) {
            throw new CommandException(MESSAGE_INVALID_TABLE_ID);
        }

        try {
            model.deleteTableById(tableId);
        } catch (TableNotFoundException tnfe) {
            throw new CommandException(String.format(MESSAGE_TABLE_NOT_FOUND, tableId));
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

