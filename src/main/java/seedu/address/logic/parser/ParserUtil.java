package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.category.Category;
import seedu.address.model.person.category.DietaryRestriction;
import seedu.address.model.person.category.Rsvp;
import seedu.address.model.table.Table;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String rsvp} into a {@code RSVP}.
     * Leading and trailing whitespaces will be trimmed.
     * @throws ParseException if the given {@code rsvp} is invalid if the status is not one of the valid statuses.
     */
    public static Rsvp parseRsvp(String rsvp) throws ParseException {
        requireNonNull(rsvp);
        String trimmedRsvp = rsvp.trim().toUpperCase();
        try {
            Rsvp.Status status = Rsvp.Status.valueOf(trimmedRsvp);
            return new Rsvp(status);
        } catch (IllegalArgumentException e) {
            throw new ParseException("Invalid RSVP status: " + rsvp + "It should be YES, NO or NO_RESPONSE.");
        }
    }
    /**
     * Parses a {@code String dietaryRestriction} into a {@code DietaryRestriction}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * Tries to parse the dietary restriction as a typical restriction first.
     * If not possible, treats it as a custom restriction.
     */
    public static DietaryRestriction parseDietaryRestriction(String dietaryRestriction) throws ParseException {
        requireNonNull(dietaryRestriction);
        // Trim the restriction to be a typical restriction like halal etc...
        String trimmedTypicalRestriction = dietaryRestriction.trim().toUpperCase();
        try {
            // See if it's a typical restriction
            DietaryRestriction.TypicalRestriction typicalRestriction =
                DietaryRestriction.TypicalRestriction.valueOf(trimmedTypicalRestriction);
            return new DietaryRestriction(typicalRestriction);
        } catch (IllegalArgumentException e) {
            // If it's not a typical one, treat it as a custom restriction
            return new DietaryRestriction(dietaryRestriction.trim());
        }
    }

    /**
     * Parses a {@code String tableId} into a {@code int tableId}.
     *
     * @param tableId A string representation of the table id
     * @return An integer representation of the table id
     * @throws ParseException
     */
    public static int parseTableId(String tableId) throws ParseException {
        String trimmedTableId = tableId.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedTableId)) {
            throw new ParseException(Table.ID_CONSTRAINTS);
        }

        return Integer.parseInt(trimmedTableId);
    }

    /**
     * Parses a {@code String category} and {@code String field} into a {@code Category}.
     * The category determines which subclass to create.
     * - If category is "RSVP", the field is parsed as an RSVP status. If the field is empty, returns a default RSVP.
     * - If category is "Dietary Restrictions", the field is parsed as a DietaryRestriction.
     * Leading and trailing whitespaces will be trimmed.
     * @throws ParseException if the given {@code category} or {@code field} is invalid.
     */
    public static Category parseFilter(String category, String field) throws ParseException {
        requireNonNull(category);
        requireNonNull(field);
        String trimmedCategory = category.trim();
        String trimmedField = field.trim();

        try {
            if (trimmedCategory.equalsIgnoreCase("RSVP")) {
                if (trimmedField.isEmpty()) {
                    // Return default RSVP (with NO_RESPONSE) if field is empty.
                    return new Rsvp();
                } else {
                    return parseRsvp(trimmedField);
                }
            } else if (trimmedCategory.equalsIgnoreCase("Dietary Restrictions")) {
                if (trimmedField.isEmpty()) {
                    throw new ParseException("Dietary Restriction field cannot be empty.");
                }
                return parseDietaryRestriction(trimmedField);
            } else {
                throw new ParseException("Unknown category: " + trimmedCategory);
            }
        } catch (ParseException pe) {
            throw new ParseException("Error parsing filter for category '" + trimmedCategory + "': " + pe.getMessage());
        } catch (Exception e) {
            throw new ParseException("Unexpected error parsing filter for category '" + trimmedCategory + "': "
                    + e.getMessage());
        }
    }

}
