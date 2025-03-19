package seedu.address.model.person;

import java.util.HashSet;
import java.util.Objects;

import seedu.address.model.person.category.DietaryRestriction;
import seedu.address.model.person.category.Rsvp;

/**
 * Represents a Guest in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Guest extends Person {
    private final DietaryRestriction dietaryRestriction;
    private final Rsvp rsvp;
    private final int guestId;

    /**
    * Every field must be present and not null, a person class is created with
    * name, phone, email, address, and an EMPTY set of tags
     */
    public Guest(Name name, Phone phone, Email email, Address address,
                 DietaryRestriction dietaryRestriction,
                 Rsvp rsvp) {
        super(name, phone, email, address, new HashSet<>());
        this.dietaryRestriction = dietaryRestriction;
        this.rsvp = rsvp;
        this.guestId = generateGuestId(phone);
    }

    /**
     * Every field must be present and not null, a person class is created with
     * name, phone, email, address, and an EMPTY set of tags
     * this is needed to make the JSON object make into an object in memory
     * TO BE CHANGED #ID = STRING?
     */

    public Guest(int guestId, Name name, Phone phone, Email email, Address address,
                 DietaryRestriction dietaryRestriction,
                 Rsvp rsvp) {
        super(name, phone, email, address, new HashSet<>());
        this.dietaryRestriction = dietaryRestriction;
        this.rsvp = rsvp;
        this.guestId = guestId;

    }

    private int generateGuestId(Phone phone) {
        return phone.hashCode();
    }

    public DietaryRestriction getDietaryRestriction() {
        return dietaryRestriction;
    }

    public Rsvp getRsvp() {
        return rsvp;
    }

    public Integer getGuestId() {
        return this.guestId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Guest)) {
            return false;
        }
        Guest otherGuest = (Guest) other;
        return super.equals(other)
            && Objects.equals(dietaryRestriction, otherGuest.dietaryRestriction)
            && rsvp == otherGuest.rsvp
            && guestId == otherGuest.guestId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dietaryRestriction, rsvp);
    }

    @Override
    public String toString() {
        return "Name: " + getName()
            + "; Phone: " + getPhone()
            + "; Email: " + getEmail()
            + "; Address: " + getAddress()
            + "; Tags: " + getTags()
            + "; Dietary Restriction: " + dietaryRestriction
            + ", RSVP: " + rsvp;
    }

}
