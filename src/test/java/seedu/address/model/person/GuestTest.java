package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.category.DietaryRestriction;
import seedu.address.model.person.category.Rsvp;
import seedu.address.model.table.Table;

public class GuestTest {

    @Test
    public void constructor_validInputs_success() {
        Name name = new Name("John Doe");
        Email email = new Email("johndoe@example.com");
        Address address = new Address("123 Street");
        Phone phone = new Phone("12345678");
        DietaryRestriction dietaryRestriction = new DietaryRestriction(DietaryRestriction.TypicalRestriction.VEGAN);
        Rsvp rsvp = new Rsvp(Rsvp.Status.YES);
        Table table = new Table(1, 8);
        Guest guest = new Guest(name, phone, email, address, dietaryRestriction, rsvp, table);

        assertEquals(name, guest.getName());
        assertEquals(email, guest.getEmail());
        assertEquals(address, guest.getAddress());
        assertEquals(phone, guest.getPhone());
        assertEquals(dietaryRestriction, guest.getDietaryRestriction());
        assertEquals(rsvp, guest.getRsvp());
        assertEquals(table, guest.getTable());
        assertEquals(1, guest.getTableId());
        assertEquals("1", guest.getTableIdString());
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Name name = new Name("John Doe");
        Email email = new Email("johndoe@example.com");
        Address address = new Address("123 Street");
        Phone phone = new Phone("12345678");
        DietaryRestriction dietaryRestriction = new DietaryRestriction(DietaryRestriction.TypicalRestriction.VEGAN);
        Rsvp rsvp = new Rsvp(Rsvp.Status.YES);
        Table table = new Table(1, 8);
        Guest guest = new Guest(name, phone, email, address, dietaryRestriction, rsvp, table);
        assertEquals(guest, guest);
    }

    @Test
    public void equals_differentObject_returnsFalse() {
        Name name1 = new Name("John Doe");
        Email email1 = new Email("johndoe@example.com");
        Address address1 = new Address("123 Street");
        Phone phone1 = new Phone("12345678");
        DietaryRestriction dietaryRestriction1 = new DietaryRestriction(DietaryRestriction.TypicalRestriction.VEGAN);
        Rsvp rsvp1 = new Rsvp(Rsvp.Status.YES);
        Table table1 = new Table(1, 8);

        Guest guest1 = new Guest(name1, phone1, email1, address1, dietaryRestriction1, rsvp1, table1);

        Name name2 = new Name("Jane Doe");
        Email email2 = new Email("janedoe@example.com");
        Address address2 = new Address("456 Avenue");
        Phone phone2 = new Phone("87654321");
        DietaryRestriction dietaryRestriction2 = new DietaryRestriction(DietaryRestriction.TypicalRestriction.HALAL);
        Rsvp rsvp2 = new Rsvp(Rsvp.Status.NO);
        Table table2 = new Table(2, 8);

        Guest guest2 = new Guest(name2, phone2, email2, address2, dietaryRestriction2, rsvp2, table2);

        assertNotEquals(guest1, guest2);
    }

    @Test
    public void hashCode_sameObject_returnsSameHashCode() {
        Name name = new Name("John Doe");
        Email email = new Email("johndoe@example.com");
        Address address = new Address("123 Street");
        Phone phone = new Phone("12345678");
        DietaryRestriction dietaryRestriction = new DietaryRestriction(DietaryRestriction.TypicalRestriction.VEGAN);
        Rsvp rsvp = new Rsvp(Rsvp.Status.YES);
        Table table = new Table(1, 8);

        Guest guest = new Guest(name, phone, email, address, dietaryRestriction, rsvp, table);
        assertEquals(guest.hashCode(), guest.hashCode());
    }

    @Test
    public void toString_validInputs_returnsString() {
        Name name = new Name("John Doe");
        Email email = new Email("johndoe@example.com");
        Address address = new Address("123 Street");
        Phone phone = new Phone("12345678");
        DietaryRestriction dietaryRestriction = new DietaryRestriction(DietaryRestriction.TypicalRestriction.VEGAN);
        Rsvp rsvp = new Rsvp(Rsvp.Status.YES);
        Table table = new Table(1, 8);

        Guest guest = new Guest(name, phone, email, address, dietaryRestriction, rsvp, table);
        String expectedString =
            "Name: John Doe; Phone: 12345678; Email: johndoe@example.com; "
                + "Address: 123 Street; Tags: []; Dietary Restriction: VEGAN, RSVP: YES, Table: 1";
        assertEquals(expectedString, guest.toString());
    }

    @Test
    public void getTableId_nullTableValue_returnsNegativeOne() {
        Name name = new Name("John Doe");
        Email email = new Email("johndoe@example.com");
        Address address = new Address("123 Street");
        Phone phone = new Phone("12345678");
        DietaryRestriction dietaryRestriction = new DietaryRestriction(DietaryRestriction.TypicalRestriction.VEGAN);
        Rsvp rsvp = new Rsvp(Rsvp.Status.YES);

        Guest guest = new Guest(name, phone, email, address, dietaryRestriction, rsvp, null);

        assertEquals(-1, guest.getTableId());
    }

    @Test
    public void getTableIdString_nullTableValue_returnsUnassigned() {
        Name name = new Name("John Doe");
        Email email = new Email("johndoe@example.com");
        Address address = new Address("123 Street");
        Phone phone = new Phone("12345678");
        DietaryRestriction dietaryRestriction = new DietaryRestriction(DietaryRestriction.TypicalRestriction.VEGAN);
        Rsvp rsvp = new Rsvp(Rsvp.Status.YES);

        Guest guest = new Guest(name, phone, email, address, dietaryRestriction, rsvp, null);

        assertEquals("Unassigned", guest.getTableIdString());
    }
}
