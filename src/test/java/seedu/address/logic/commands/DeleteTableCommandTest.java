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

/**
 * Unit tests for DeleteTableCommand.
 */
public class DeleteTableCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
    }

    @Test
    public void execute_noWeddingSet_throwsCommandException() {
        DeleteTableCommand command = new DeleteTableCommand(1);

        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(DeleteTableCommand.MESSAGE_NO_CURRENT_WEDDING, exception.getMessage());
    }

    @Test
    public void execute_invalidTableId_throwsCommandException() {
        Wedding wedding = new Wedding("Test Wedding");
        model.addWedding(wedding);
        model.setCurrentWedding(wedding);

        DeleteTableCommand command = new DeleteTableCommand(-2); // invalid table ID

        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(DeleteTableCommand.MESSAGE_INVALID_TABLE_ID, exception.getMessage());
    }

    @Test
    public void execute_tableNotFound_throwsCommandException() {
        Wedding wedding = new Wedding("Test Wedding");
        model.addWedding(wedding);
        model.setCurrentWedding(wedding);

        DeleteTableCommand command = new DeleteTableCommand(10); // table not added

        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(String.format(DeleteTableCommand.MESSAGE_TABLE_NOT_FOUND, 10), exception.getMessage());
    }

    @Test
    public void execute_validTableId_deletesSuccessfully() throws Exception {
        Wedding wedding = new Wedding("Test Wedding");
        model.addWedding(wedding);
        model.setCurrentWedding(wedding);

        Table table = new Table(1, 5);
        model.addTable(table);

        DeleteTableCommand command = new DeleteTableCommand(1);
        CommandResult result = command.execute(model);

        assertEquals(String.format(DeleteTableCommand.MESSAGE_SUCCESS, 1), result.getFeedbackToUser());
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        DeleteTableCommand cmd1 = new DeleteTableCommand(1);
        DeleteTableCommand cmd2 = new DeleteTableCommand(1);
        assertEquals(cmd1, cmd2);
    }

    @Test
    public void equals_differentValues_returnsFalse() {
        DeleteTableCommand cmd1 = new DeleteTableCommand(1);
        DeleteTableCommand cmd2 = new DeleteTableCommand(2);
        assert (!cmd1.equals(cmd2));
    }
}
