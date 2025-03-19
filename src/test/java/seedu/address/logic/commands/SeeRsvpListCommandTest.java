package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Address;
import seedu.address.model.person.Remark;
import seedu.address.model.person.category.DietaryRestriction;
import seedu.address.model.person.Email;
import seedu.address.model.person.Guest;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.category.Rsvp;
import seedu.address.model.wedding.Wedding;

public class SeeRsvpListCommandTest {
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        Wedding currentWedding = new Wedding("John and Jane's Wedding");
        model.addWedding(currentWedding);
        model.setCurrentWedding(currentWedding);
    }

    @Test
    public void execute_seeRsvpList_success() throws Exception {
        Guest guest = new Guest(
            new Name("John Doe"),
            new Phone("12345678"),
            new Email("johndoe@example.com"),
            new Address("123 Street"),
                new Remark("remark"),
            new DietaryRestriction("None"),
            new Rsvp(Rsvp.Status.YES));
        model.getCurrentWedding().addGuest(guest);
        SeeRsvpListCommand command = new SeeRsvpListCommand();

        CommandResult result = command.execute(model);

        assertEquals(String.format(SeeRsvpListCommand.MESSAGE_SUCCESS, guest), result.getFeedbackToUser());
    }
}
