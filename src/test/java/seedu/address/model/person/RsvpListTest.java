package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.category.DietaryRestriction;
import seedu.address.model.person.category.Rsvp;

public class RsvpListTest {

    private RsvpList rsvpList;
    private Guest guest;

    @BeforeEach
    public void setUp() {
        rsvpList = new RsvpList();
        guest = new Guest(
            new Name("John Doe"),
            new Phone("12345678"),
            new Email("johndoe@example.com"),
            new Address("123 Street"),
            new DietaryRestriction(DietaryRestriction.TypicalRestriction.VEGAN),
            new Rsvp(Rsvp.Status.YES),
            null
        );
    }

    @Test
    public void add_validGuest_success() {
        rsvpList.add(guest);
        assertEquals(1, rsvpList.getAllGuests().size());
    }

    @Test
    public void add_invalidPerson_throwsIllegalArgumentException() {
        Person person = new Person(
            new Name("Jane Doe"),
            new Phone("87654321"),
            new Email("janedoe@example.com"),
            new Address("456 Avenue"),
            new HashSet<>()
        );
        assertThrows(IllegalArgumentException.class, () -> rsvpList.add(person));
    }

    @Test
    public void contains_validGuest_returnsTrue() {
        rsvpList.add(guest);
        assertTrue(rsvpList.contains(guest));
    }

    @Test
    public void contains_invalidPerson_returnsFalse() {
        Person person = new Person(
            new Name("Jane Doe"),
            new Phone("87654321"),
            new Email("janedoe@example.com"),
            new Address("456 Avenue"),
            new HashSet<>()
        );
        assertFalse(rsvpList.contains(person));
    }

    @Test
    public void remove_validGuest_success() {
        rsvpList.add(guest);
        rsvpList.remove(guest);
        assertEquals(0, rsvpList.getAllGuests().size());
    }

    @Test
    public void remove_invalidPerson_throwsIllegalArgumentException() {
        Person person = new Person(
            new Name("Jane Doe"),
            new Phone("87654321"),
            new Email("janedoe@example.com"),
            new Address("456 Avenue"),
            new HashSet<>()
        );
        assertThrows(IllegalArgumentException.class, () -> rsvpList.remove(person));
    }

    @Test
    public void getGuestByPhone_existingPhone_returnsGuest() throws CommandException {
        rsvpList.add(guest);
        assertEquals(guest, rsvpList.getGuestByPhone(new Phone("12345678")));
    }

    @Test
    public void getGuestByPhone_nonExistingPhone_throwsException() {
        assertThrows(CommandException.class, () -> rsvpList.getGuestByPhone(new Phone("87654321")));
    }

}
