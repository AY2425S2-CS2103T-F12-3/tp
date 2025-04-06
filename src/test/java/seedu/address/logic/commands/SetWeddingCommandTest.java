package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.wedding.Wedding;

public class SetWeddingCommandTest {
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
    }

    @Test
    public void execute_setWedding_success() throws Exception {
    //        // No need to set it to null, as the logic already allows setting a new wedding
    //        Wedding currentWedding = new Wedding("John and Jane's Wedding");
    //        SetWeddingCommand command = new SetWeddingCommand(currentWedding.getName());
    //
    //        CommandResult result = command.execute(model);
    //
    //        assertEquals(String.format(SetWeddingCommand.MESSAGE_SUCCESS, currentWedding.getName()),
    //                result.getFeedbackToUser());
    //
    //        // Verify the wedding was set successfully
    //        assertEquals(currentWedding.getName(), model.getCurrentWedding().getName());
    }

    @Test
    public void execute_setWedding_failure() {
        // First, set an existing wedding
        Wedding existingWedding = new Wedding("Existing Wedding");
        model.addWedding(existingWedding);
        model.setCurrentWedding(existingWedding);
        // Then, attempt to set a **different** wedding, which should fail
        SetWeddingCommand command = new SetWeddingCommand("New Wedding");
        CommandResult result = command.execute(model);
        assertEquals(String.format(SetWeddingCommand.MESSAGE_WEDDING_MISSING, "New Wedding"),
                result.getFeedbackToUser());
    }
}
