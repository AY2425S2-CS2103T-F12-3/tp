package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TABLE_ID;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Adds the guest to the specified table.
 * If the guest was previously allocated to another table, removes them from there.
 */
public class AddPersonToTableCommand extends Command {

    public static final String COMMAND_WORD = "addGuestToTable";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Adds the guest identified by name "
        + "to the table identified by the table ID.\n"
        + "If the guest was previously seated at another table, "
        + "they will forcefully be removed from that table "
        + "to be relocated to the given table.\n"
        + "Parameters: "
        + "[" + PREFIX_NAME + "NAME] "
        + "[" + PREFIX_TABLE_ID + "TABLE ID]\n"
        + "Example: " + COMMAND_WORD + " 1 "
        + PREFIX_NAME + "John Doe"
        + PREFIX_TABLE_ID + "2";

    public static final String MESSAGE_ADD_GUEST_TO_TABLE_SUCCESS = "Added Person: %s to Table: %d";
    public static final String MESSAGE_DUPLICATE_PERSON = "This guest already exists in the address book.";

    private final Name guestName;
    private final int newTableId;
    /**
     * @param guestName Name of the guest to add to the table
     * @param newTableId Int ID of the table to add the guest to
     */
    public AddPersonToTableCommand(Name guestName, int newTableId) {
        requireNonNull(guestName);
        requireNonNull(newTableId);

        this.guestName = guestName;
        this.newTableId = newTableId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person personToAdd = model.findPersonByName(guestName);

        if (personToAdd.isSeated()) {
            model.deletePersonFromTableById(personToAdd, personToAdd.getTableId());
        }

        model.addPersonToTableById(personToAdd, newTableId);

        return new CommandResult(String.format(MESSAGE_ADD_GUEST_TO_TABLE_SUCCESS,
            personToAdd.getName().fullName, newTableId));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddPersonToTableCommand)) {
            return false;
        }

        AddPersonToTableCommand otherAddPersonToTableCommand = (AddPersonToTableCommand) other;
        return guestName.equals(otherAddPersonToTableCommand.guestName)
            && newTableId == otherAddPersonToTableCommand.newTableId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("AddedGuest", guestName)
            .add("AddedTable", newTableId)
            .toString();
    }
}
