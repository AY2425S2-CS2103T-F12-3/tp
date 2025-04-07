package seedu.address.logic.commands;

import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.table.UniqueTableList;
import seedu.address.model.wedding.Wedding;

/**
 * Command to provide an overview of the current wedding.
 */
public class WeddingOverviewCommand extends Command {

    public static final String COMMAND_WORD = "weddingOverview";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows an overview of the current wedding.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = """
        Overview of %1$s's Wedding:
        Number of tables: %2$d
        Number of guests invited: %3$d
        Person list:
        %4$s
        """;

    public static final String MESSAGE_NO_WEDDING = "No wedding is currently set.";

    public WeddingOverviewCommand() {
        // No parameters needed
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Wedding wedding = model.getCurrentWedding();
        if (wedding == null) {
            throw new CommandException(MESSAGE_NO_WEDDING);
        }
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        model.updateFilteredTableList(Model.PREDICATE_SHOW_ALL_TABLES);
        // ✅ Ensure TableList and RsvpList are properly initialized
        UniqueTableList tableList = model.getCurrentWedding().getTableList();

        if (tableList == null) {
            tableList = new UniqueTableList();
        }

        int tableCount = tableList.asUnmodifiableObservableList().size();
        List<Person> guests = model.getFilteredPersonList();
        int guestCount = guests.size();


        StringBuilder guestListFormatted = new StringBuilder();
        if (guests.isEmpty()) {
            guestListFormatted.append("No guests added yet.");
        } else {
            for (Person guest : guests) {
                guestListFormatted.append(guest.toString()).append("\n");
            }
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS,
                wedding.getName(),
                tableCount,
                guestCount,
                guestListFormatted.toString().trim()));
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof WeddingOverviewCommand;
    }
}
