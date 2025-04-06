package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.table.Table;
import seedu.address.model.wedding.Wedding;

public class AddTableCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
    }

    @Test
    public void execute_noWeddingSet_throwsCommandException() {
        AddTableCommand command = new AddTableCommand(1, 6);
        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(AddTableCommand.MESSAGE_NO_CURRENT_WEDDING, exception.getMessage());
    }

    @Test
    public void execute_duplicateTableId_throwsCommandException() throws Exception {
        Wedding wedding = new Wedding("Test Wedding");
        model.addWedding(wedding);
        model.setCurrentWedding(wedding);

        model.addTable(new Table(1, 6)); // add initial table

        AddTableCommand command = new AddTableCommand(1, 8); // duplicate ID

        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(String.format(AddTableCommand.MESSAGE_DUPLICATE_TABLE, 1), exception.getMessage());
    }

    @Test
    public void execute_invalidCapacity_throwsCommandException() throws Exception {
        Wedding wedding = new Wedding("Test Wedding");
        model.addWedding(wedding);
        model.setCurrentWedding(wedding);

        AddTableCommand command = new AddTableCommand(2, -5); // invalid capacity

        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals("Invalid table configuration: The table capacity should be a positive integer",
                exception.getMessage());
    }

    @Test
    public void execute_validInput_addsTableSuccessfully() throws Exception {
        Wedding wedding = new Wedding("Valid Wedding");
        model.addWedding(wedding);
        model.setCurrentWedding(wedding);

        AddTableCommand command = new AddTableCommand(3, 10);

        CommandResult result = command.execute(model);

        assertEquals(String.format(AddTableCommand.MESSAGE_SUCCESS, 3, 10), result.getFeedbackToUser());
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        AddTableCommand cmd1 = new AddTableCommand(1, 6);
        AddTableCommand cmd2 = new AddTableCommand(1, 6);
        assertEquals(cmd1, cmd2);
    }

    @Test
    public void equals_differentValues_returnsFalse() {
        AddTableCommand cmd1 = new AddTableCommand(1, 6);
        AddTableCommand cmd2 = new AddTableCommand(2, 6);
        AddTableCommand cmd3 = new AddTableCommand(1, 4);
        assert (!cmd1.equals(cmd2));
        assert (!cmd1.equals(cmd3));
    }

    @Test
    public void execute_tableIdZero_throwsCommandException() throws Exception {
        Wedding wedding = new Wedding("Zero ID Wedding");
        model.addWedding(wedding);
        model.setCurrentWedding(wedding);

        AddTableCommand command = new AddTableCommand(0, 6);
        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals("Invalid table configuration: The table ID should be a positive integer",
                exception.getMessage());
    }

    @Test
    public void execute_negativeTableId_throwsCommandException() throws Exception {
        Wedding wedding = new Wedding("Negative ID Wedding");
        model.addWedding(wedding);
        model.setCurrentWedding(wedding);

        AddTableCommand command = new AddTableCommand(-2, 6);
        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals("Invalid table configuration: The table ID should be a positive integer",
                exception.getMessage());
    }

    @Test
    public void execute_zeroCapacity_throwsCommandException() throws Exception {
        Wedding wedding = new Wedding("Zero Capacity Wedding");
        model.addWedding(wedding);
        model.setCurrentWedding(wedding);

        AddTableCommand command = new AddTableCommand(4, 0);
        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals("Invalid table configuration: The table capacity should be a positive integer",
                exception.getMessage());
    }

    // @Test
    // public void execute_maxIntegerCapacity_addsTableSuccessfully() throws Exception {
    //     Wedding wedding = new Wedding("Big Wedding");
    //     model.addWedding(wedding);
    //     model.setCurrentWedding(wedding);
    //
    //     AddTableCommand command = new AddTableCommand(99, Integer.MAX_VALUE);
    //     CommandResult result = command.execute(model);
    //     assertEquals(String.format(AddTableCommand.MESSAGE_SUCCESS, 99, Integer.MAX_VALUE),
    //     result.getFeedbackToUser());
    // }

    // @Test
    // public void execute_tableIdOne_valid_success() throws Exception {
    //     Wedding wedding = new Wedding("Boundary Test Wedding");
    //     model.addWedding(wedding);
    //     model.setCurrentWedding(wedding);
    //
    //     AddTableCommand command = new AddTableCommand(1, 5);
    //     CommandResult result = command.execute(model);
    //     assertEquals(String.format(AddTableCommand.MESSAGE_SUCCESS, 1, 5), result.getFeedbackToUser());
    // }
}
