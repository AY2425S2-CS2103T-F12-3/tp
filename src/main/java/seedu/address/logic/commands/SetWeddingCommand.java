package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;
import seedu.address.model.wedding.Wedding;
import seedu.address.model.wedding.exceptions.WeddingNotFoundException;

/**
 * Sets a new wedding in the model.
 */
public class SetWeddingCommand extends Command {

    public static final String COMMAND_WORD = "setWedding";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets the current wedding.\n"
            + "Parameters: WEDDING_NAME\n"
            + "Example: " + COMMAND_WORD + " John and Jane's Wedding";

    public static final String MESSAGE_SUCCESS = "Current wedding set to: %1$s";
    public static final String MESSAGE_WEDDING_ALREADY_EXISTS =
            "A different wedding exists. Delete the current wedding first.";
    public static final String MESSAGE_WEDDING_ALREADY_SET =
            "The wedding is already set to: %1$s";
    public static final String MESSAGE_WEDDING_MISSING =
        "There is no wedding with the name: %s";
    public static final String MESSAGE_INVALID_NAME = "Wedding name cannot be empty or just spaces.";


    private final String weddingName;

    /**
     * Instantiates the command with the given wedding name.
     * The wedding name should contain a non-empty string.
     * @param weddingName
     */
    public SetWeddingCommand(String weddingName) {
        requireNonNull(weddingName, "Wedding name cannot be null");
        if (weddingName.trim().isEmpty()) {
            throw new IllegalArgumentException(MESSAGE_INVALID_NAME);
        }
        this.weddingName = weddingName;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        try {
            Wedding existingWedding = model.findWeddingByName(weddingName.strip());
            model.setCurrentWedding(existingWedding);
            return new CommandResult(String.format(MESSAGE_SUCCESS, existingWedding.getName()));

        } catch (WeddingNotFoundException wnfe) {
            return new CommandResult(String.format(MESSAGE_WEDDING_MISSING, weddingName));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof SetWeddingCommand
                && weddingName.equals(((SetWeddingCommand) other).weddingName));
    }
}
