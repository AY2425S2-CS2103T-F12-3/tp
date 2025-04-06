package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteTableCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class DeleteTableCommandParserTest {
    private final DeleteTableCommandParser parser = new DeleteTableCommandParser();

    // @Test
    // public void parse_validPrefixed_success() throws Exception {
    //     DeleteTableCommand command = parser.parse(" tableId/4");
    //     assertEquals(new DeleteTableCommand(4), command);
    // }

    @Test
    public void parse_validRaw_success() throws Exception {
        DeleteTableCommand command = parser.parse("4");
        assertEquals(new DeleteTableCommand(4), command);
    }

    @Test
    public void parse_missingValuePrefixed_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("tableId/"));
    }

    @Test
    public void parse_nonIntegerPrefixed_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("tableId/abc"));
    }

    @Test
    public void parse_negativeIntegerPrefixed_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("tableId/-1"));
    }

    @Test
    public void parse_zeroTableIdRaw_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("0"));
    }

    @Test
    public void parse_negativeTableIdRaw_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("-5"));
    }

    @Test
    public void parse_nonIntegerRaw_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("abc"));
    }

    @Test
    public void parse_extraArguments_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("2 3"));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("   "));
    }
}
