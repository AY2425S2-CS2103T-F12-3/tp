package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.exceptions.NoCurrentWeddingException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;

/**
 * Adds a person to the current wedding.
 */
public class AddPersonCommand extends Command {
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";
    public static final String COMMAND_WORD = "addPerson";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the current wedding.\n"
        + "Parameters: "
        + "n/NAME p/PHONE e/EMAIL a/ADDRESS d/DIETARY_RESTRICTION r/RSVP\n"
        + "Example: " + COMMAND_WORD + " "
        + "n/John Doe p/12345678 e/johndoe@example.com a/123 Street d/None r/YES";

    public static final String MESSAGE_SUCCESS = "Person added to wedding: %1$s";

    public static final String MESSAGE_NO_WEDDING = "No wedding is currently set. Please set a wedding before adding a "
        + "person.";
    private final Person person;
    /**
     * Creates an AddGuestCommand to add the specified {@code Person} to the rsvp list in the current wedding.
     */
    public AddPersonCommand(Person person) {
        requireNonNull(person);
        this.person = person;
    }
    /**
     * Executes the AddGuestCommand.
     * @param model {@code Model} which the command should operate on.
     * @return {@code CommandResult} that describes the result of executing the command.
     * @throws CommandException if the current wedding is not set.
     */

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        try {
            model.addPerson(person);
        } catch (NoCurrentWeddingException e) {
            throw new CommandException(MESSAGE_NO_WEDDING);
        } catch (DuplicatePersonException e) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, person));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AddPersonCommand)) {
            return false;
        }
        AddPersonCommand otherCommand = (AddPersonCommand) other;
        return person.toString().equals(otherCommand.person.toString());
    }

    @Override
    public int hashCode() {
        return person.hashCode();
    }

    @Override
    public String toString() {
        return "seedu.address.logic.commands.AddPersonCommand{toAdd=" + person + "}";
    }
}
