package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SetWeddingCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class SetWeddingCommandParserTest {

    private final SetWeddingCommandParser parser = new SetWeddingCommandParser();

    @Test
    public void parse_validArgs_returnsSetWeddingCommand() throws Exception {
        String validArgs = "setWedding n/John and Jane's Wedding";
        SetWeddingCommand command = parser.parse(validArgs);
        assertEquals(new SetWeddingCommand("John and Jane's Wedding"), command);
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        String emptyArgs = "";
        assertThrows(ParseException.class, () -> parser.parse(emptyArgs));
    }
}
