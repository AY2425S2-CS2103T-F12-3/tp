package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Address;
import seedu.address.model.person.DietaryRestriction;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Rsvp;
import seedu.address.model.table.Table;
import seedu.address.model.wedding.Wedding;

public class AddPersonToTableCommandTest {

    private Model model;
    private Person person;
    private Name guestName;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        Wedding wedding = new Wedding("Wedding Test");
        model.addWedding(wedding);
        model.setCurrentWeddingByName("Wedding Test");

        guestName = new Name("Alice");

        person = new Person(
                guestName,
                new Phone("91234567"),
                new Email("alice@example.com"),
                new Address("123 Street"),
                new HashSet<>(),
                new DietaryRestriction(DietaryRestriction.TypicalRestriction.NONE),
                new Rsvp(Rsvp.Status.YES),
                -1
        );

        model.addPerson(person);
        model.addTable(new Table(1, 2));
        model.addTable(new Table(2, 1));
    }

    @Test
    public void execute_addToEmptyTable_success() throws Exception {
        AddPersonToTableCommand command = new AddPersonToTableCommand(guestName, 1);
        CommandResult result = command.execute(model);

        assertEquals(String.format(AddPersonToTableCommand.MESSAGE_ADD_GUEST_TO_TABLE_SUCCESS,
                guestName.fullName, 1), result.getFeedbackToUser());
    }

    @Test
    public void execute_reassignFromOneTableToAnother_success() throws Exception {
        model.addPersonToTableById(person, 1);

        AddPersonToTableCommand command = new AddPersonToTableCommand(guestName, 2);
        CommandResult result = command.execute(model);

        assertEquals(String.format(AddPersonToTableCommand.MESSAGE_ADD_GUEST_TO_TABLE_SUCCESS,
                guestName.fullName, 2), result.getFeedbackToUser());
    }

    @Test
    public void execute_personNotFound_throwsCommandException() {
        Name unknownName = new Name("Ghost");

        AddPersonToTableCommand command = new AddPersonToTableCommand(unknownName, 1);
        CommandException ex = assertThrows(CommandException.class, () -> command.execute(model));

        assertEquals("Person 'Ghost' not found in the guest list.", ex.getMessage());
    }

    @Test
    public void execute_tableNotFound_throwsCommandException() {
        AddPersonToTableCommand command = new AddPersonToTableCommand(guestName, 999);
        CommandException ex = assertThrows(CommandException.class, () -> command.execute(model));

        assertEquals("Table with ID 999 does not exist.", ex.getMessage());
    }

    @Test
    public void execute_tableFull_throwsCommandException() throws Exception {
        // Fill the table with capacity 1
        Person other = new Person(
                new Name("Bob"),
                new Phone("99999999"),
                new Email("bob@example.com"),
                new Address("456 Lane"),
                new HashSet<>(),
                new DietaryRestriction(DietaryRestriction.TypicalRestriction.NONE),
                new Rsvp(Rsvp.Status.YES),
                -1
        );

        model.addPerson(other);
        model.addPersonToTableById(other, 2);

        AddPersonToTableCommand command = new AddPersonToTableCommand(guestName, 2);
        CommandException ex = assertThrows(CommandException.class, () -> command.execute(model));

        assertEquals("Table with ID 2 is full.", ex.getMessage());
    }

    @Test
    public void execute_noWeddingSet_throwsCommandException() {
        Model newModel = new ModelManager(); // no wedding

        AddPersonToTableCommand command = new AddPersonToTableCommand(guestName, 1);
        CommandException ex = assertThrows(CommandException.class, () -> command.execute(newModel));

        assertEquals("No current wedding set. Please use 'setWedding' first.", ex.getMessage());
    }

    @Test
    public void execute_tableIdMinimum_success() throws Exception {
        AddPersonToTableCommand command = new AddPersonToTableCommand(guestName, 1);
        CommandResult result = command.execute(model);

        assertEquals(String.format(AddPersonToTableCommand.MESSAGE_ADD_GUEST_TO_TABLE_SUCCESS,
                guestName.fullName, 1), result.getFeedbackToUser());
    }

    @Test
    public void execute_tableIdMax_success() throws Exception {
        model.addTable(new Table(Integer.MAX_VALUE, 3));
        AddPersonToTableCommand command = new AddPersonToTableCommand(guestName, Integer.MAX_VALUE);

        CommandResult result = command.execute(model);
        assertEquals(String.format(AddPersonToTableCommand.MESSAGE_ADD_GUEST_TO_TABLE_SUCCESS,
                guestName.fullName, Integer.MAX_VALUE), result.getFeedbackToUser());
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        AddPersonToTableCommand cmd1 = new AddPersonToTableCommand(guestName, 1);
        AddPersonToTableCommand cmd2 = new AddPersonToTableCommand(guestName, 1);

        assertEquals(cmd1, cmd2);
    }

    @Test
    public void equals_differentValues_returnsFalse() {
        AddPersonToTableCommand cmd1 = new AddPersonToTableCommand(guestName, 1);
        AddPersonToTableCommand cmd2 = new AddPersonToTableCommand(new Name("Bob"), 1);
        AddPersonToTableCommand cmd3 = new AddPersonToTableCommand(guestName, 2);

        assert (!cmd1.equals(cmd2));
        assert (!cmd1.equals(cmd3));
    }
}
