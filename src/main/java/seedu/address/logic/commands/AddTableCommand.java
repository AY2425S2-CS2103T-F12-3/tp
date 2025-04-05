package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_NO_CURRENT_WEDDING;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.exceptions.NoCurrentWeddingException;
import seedu.address.model.table.Table;
import seedu.address.model.table.exceptions.DuplicateTableException;

/**
 * Represents a command to add a table to the wedding seating plan.
 */
public class AddTableCommand extends Command {

    public static final String COMMAND_WORD = "addTable";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a table to the current wedding.\n"
            + "Parameters: tid/TABLE_ID c/CAPACITY\n"
            + "Example: " + COMMAND_WORD + " tid/1 c/6";

    public static final String MESSAGE_SUCCESS = "Table added: Table ID: %1$d, Capacity: %2$d";
    public static final String MESSAGE_DUPLICATE_TABLE = "A table with ID %1$d already exists. "
            + "Please choose a different ID.";

    private final int tableId;
    private final int capacity;

    /**
     * Creates an AddTableCommand with the specified table ID and capacity.
     *
     * @param tableId  The unique identifier for the table.
     * @param capacity The seating capacity of the table.
     */
    public AddTableCommand(int tableId, int capacity) {
        this.tableId = tableId;
        this.capacity = capacity;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        try {
            Table table = new Table(tableId, capacity);
            model.addTable(table);
            return new CommandResult(String.format(MESSAGE_SUCCESS, tableId, capacity));
        } catch (DuplicateTableException dte) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_TABLE, tableId));
        } catch (NoCurrentWeddingException ncwe) {
            throw new CommandException(MESSAGE_NO_CURRENT_WEDDING);
        } catch (IllegalArgumentException iae) {
            throw new CommandException(iae.getMessage());
        }
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof AddTableCommand
                && tableId == ((AddTableCommand) other).tableId
                && capacity == ((AddTableCommand) other).capacity);
    }
}
