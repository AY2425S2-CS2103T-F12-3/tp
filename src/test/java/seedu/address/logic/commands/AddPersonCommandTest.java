package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.beans.PropertyChangeListener;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.exceptions.NoCurrentWeddingException;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.table.Table;
import seedu.address.model.wedding.Wedding;
import seedu.address.testutil.PersonBuilder;

public class AddPersonCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelStubAcceptingPersonAdded();
        Wedding wedding = new Wedding("john and jane"); // Create a new wedding
        model.setCurrentWedding(wedding); // Set the current wedding
    }

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddPersonCommand(null));
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        Person validPerson = new PersonBuilder().build();

        CommandResult commandResult = new AddPersonCommand(validPerson).execute(model);

        assertEquals(String.format(AddPersonCommand.MESSAGE_SUCCESS, validPerson),
            commandResult.getFeedbackToUser());

        assertEquals(Arrays.asList(validPerson).toString(), model
            .getCurrentWedding().getUniquePersonList().toString());
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person validPerson = new PersonBuilder().build();
        AddPersonCommand addPersonCommand = new AddPersonCommand(validPerson);
        model = new ModelStubWithPerson(validPerson);

        assertThrows(CommandException.class,
            AddPersonCommand.MESSAGE_DUPLICATE_PERSON, () -> addPersonCommand.execute(model));
    }

    @Test
    public void execute_noWedding_throwsCommandException() {
        model = new ModelStubWithoutWedding();
        Person validPerson = new PersonBuilder().build();
        AddPersonCommand addPersonCommand = new AddPersonCommand(validPerson);

        assertThrows(CommandException.class,
            AddPersonCommand.MESSAGE_NO_WEDDING, () -> addPersonCommand.execute(model));
    }

    @Test
    public void equals() {
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();
        AddPersonCommand addAliceCommand = new AddPersonCommand(alice);
        AddPersonCommand addBobCommand = new AddPersonCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddPersonCommand addAliceCommandCopy = new AddPersonCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        AddPersonCommand addPersonCommand = new AddPersonCommand(ALICE);
        String expected = AddPersonCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
        assertEquals(expected, addPersonCommand.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        private Wedding currentWedding;

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Person findPersonByName(Name name) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addTable(Table table) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteTable(Table table) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteTableById(int tableId) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPersonToTableById(Person p, int tableId) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPersonToTable(Person p, Table table) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasTable(int id) {
            return true;
        }

        @Override
        public void deletePersonFromTableById(Person p, int tableId) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePersonFromTable(Person p, Table table) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setTable(Table target, Table editedTable) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Table findTableById(int tableId) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteCurrentWedding() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteWedding(Wedding wedding) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteWeddingByName(String weddingName) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setCurrentWeddingByName(String weddingName) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setCurrentWedding(Wedding wedding) {
            this.currentWedding = wedding;
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Table> getFilteredTableList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredTableList(Predicate<Table> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addWedding(Wedding wedding) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Wedding getCurrentWedding() {
            return currentWedding;
        }

        @Override
        public Wedding findWeddingByName(String weddingName) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasWeddingWithName(String weddingName) {
            throw new AssertionError("This method should not be called.");
        }

        @Override

        public boolean hasCurrentWedding() {
            return true;
        }
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
            Wedding addingWedding = new Wedding("john and jane");
            addingWedding.addPerson(this.person); // Ensure a wedding is set
            setCurrentWedding(addingWedding); // Ensure a wedding is set
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
        }
        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            if (hasPerson(person)) {
                throw new DuplicatePersonException();
            }
            getCurrentWedding().addPerson(person);
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        ModelStubAcceptingPersonAdded() {
            setCurrentWedding(new Wedding("john and jane")); // Ensure a wedding is set
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            getCurrentWedding().addPerson(person);
            personsAdded.add(person);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }

    }
    private class ModelStubWithoutWedding extends ModelStub {
        private Wedding currentWedding;
        private final Person person = new PersonBuilder().withName("Bob").build();
        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
        }
        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            if (currentWedding == null) {
                throw new NoCurrentWeddingException();
            }
            if (hasPerson(person)) {
                throw new DuplicatePersonException();
            }

        }

        @Override
        public void setCurrentWedding(Wedding wedding) {
            this.currentWedding = wedding;
        }

        @Override
        public Wedding getCurrentWedding() {
            return currentWedding;
        }


        @Override
        public boolean hasCurrentWedding() {
            return currentWedding != null;
        }
    }

}
