package seedu.address.model.table;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.Objects;

import javafx.collections.ObservableList;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.table.exceptions.TableFullException;

/**
 * Represents a seating table in a wedding.
 * Each table has a unique ID, a seating capacity, and a list of assigned persons.
 * This class is immutable, meaning once a table is created, its properties cannot be modified.
 */
public final class Table {

    public static final String ID_CONSTRAINTS = "The table ID should be a positive integer";
    public static final String CAPACITY_CONSTRAINTS = "The table capacity should be a positive integer";
    public static final int MAX_CAPACITY = 10000;

    /** The unique identifier for the table. */
    private final int tableId;

    /** The maximum seating capacity of the table. */
    private final int capacity;

    private final UniquePersonList uniquePersonList;

    /**
     * Constructs a new {@code Table} with the given ID and capacity.
     * This constructor initializes an empty person list.
     *
     * @param tableId  The unique identifier for the table. Must be a positive integer.
     * @param capacity The seating capacity of the table. Must be a positive integer.
     * @throws IllegalArgumentException if {@code tableId} or {@code capacity} is not positive.
     */
    public Table(int tableId, int capacity) {
        if (tableId <= 0) {
            throw new IllegalArgumentException(ID_CONSTRAINTS);
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException(CAPACITY_CONSTRAINTS);
        }
        if (capacity > MAX_CAPACITY) {
            throw new IllegalArgumentException("The table capacity exceeds the allowed maximum of " + MAX_CAPACITY);
        }
        this.tableId = tableId;
        this.capacity = capacity;
        this.uniquePersonList = new UniquePersonList();
    }

    /**
     * Constructs a new {@code Table} with the given ID, capacity, and assigned persons.
     *
     * @param tableId The unique identifier for the table. Must be a positive integer.
     * @param capacity The seating capacity of the table. Must be a positive integer.
     * @param uniquePersonList The list of persons assigned to this table.
     * @throws IllegalArgumentException if {@code tableId} or {@code capacity} is not positive.
     */
    public Table(int tableId, int capacity, UniquePersonList uniquePersonList) {
        if (tableId <= 0) {
            throw new IllegalArgumentException(ID_CONSTRAINTS);
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException(CAPACITY_CONSTRAINTS);
        }

        this.tableId = tableId;
        this.capacity = capacity;
        this.uniquePersonList = new UniquePersonList(uniquePersonList);
    }

    /**
     * Copy constructor that creates a deep copy of the provided table.
     * All persons in the original table will be copied and assigned to this new table.
     *
     * @param t The table to be copied. Cannot be null.
     */
    public Table(Table t) {
        this.tableId = t.tableId;
        this.capacity = t.capacity;
        this.uniquePersonList = new UniquePersonList(t.uniquePersonList);
    }

    /**
     * Returns the unique identifier for this table.
     *
     * @return The table ID.
     */
    public int getTableId() {
        return tableId;
    }

    /**
     * Returns the seating capacity of this table.
     *
     * @return The table's capacity.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Adds the given person to this table.
     * @param p Person to add to this table
     */
    public void addPerson(Person p) {
        Person newPerson = new Person(p, tableId);

        if (uniquePersonList.contains(p)) {
            setPerson(p, newPerson);
        } else {
            if (getSize() == getCapacity()) {
                throw new TableFullException();
            }
            this.uniquePersonList.add(p);
        }
    }

    /**
     * Deletes the given person from this table
     * if the person exists in this table.
     * @param p Person to delete from this table
     */
    public void deletePerson(Person p) {
        Person newP = new Person(p);
        boolean askfjlaksd = uniquePersonList.contains(p);
        boolean aslkfjalskd2 = askfjlaksd;
        if (!uniquePersonList.contains(p)) {
            throw new PersonNotFoundException();
        }

        // Do a name check just in case
        this.uniquePersonList.delete(p);
    }

    /**
     * Finds the given person from this table's person list
     * @param person
     * @return The resulting person from this table's person list if exists
     */
    public Person findPerson(Person person) {
        return this.uniquePersonList.findPersonByName(person.getName());
    }

    /**
     * Finds the person with the given name from this table's person list
     * @param name
     * @return The resulting person from this table's person list if exists
     */
    public Person findPersonByName(Name name) {
        return this.uniquePersonList.findPersonByName(name);
    }

    /**
     * Checks whether or not a person with the given name sits at this table.
     * @param name
     * @return Boolean value for whether a person with the given name sits here
     */
    public boolean hasPersonByName(Name name) {
        try {
            findPersonByName(name);
            return true;
        } catch (PersonNotFoundException pnfe) {
            return false;
        }
    }

    public ObservableList<Person> getAllPersons() {
        return this.uniquePersonList.asUnmodifiableObservableList();
    }

    public void setPerson(Person target, Person editedPerson) {
        uniquePersonList.setPerson(target, editedPerson);
    }

    /**
     * Replaces the contents of this list with the contents of the given UniquePersonList.
     *
     * @param replacement The UniquePersonList containing the replacement persons
     */
    public void setPersons(UniquePersonList replacement) {
        requireNonNull(replacement);
        this.uniquePersonList.setPersons(replacement);
    }

    /**
     * Replaces the contents of this list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        requireAllNonNull(persons);
        this.uniquePersonList.setPersons(persons);
    }

    public int getSize() {
        return this.uniquePersonList.size();
    }

    /**
     * Returns an immutable list of persons' names assigned to this table.
     *
     * @return A list of all persons' names assigned to the table.
     */
    public List<Name> getAllPersonsNames() {
        return this.uniquePersonList.getAllPersonsNames();
    }

    /**
     * Checks whether this table is the same as another table based on the table ID.
     *
     * @param otherTable The table to compare.
     * @return {@code true} if both tables have the same ID, otherwise {@code false}.
     */
    public boolean isSameTable(Table otherTable) {
        return otherTable != null && this.tableId == otherTable.tableId && this.capacity == otherTable.capacity;
    }

    /**
     * Returns a string representation of this table, showing its ID, capacity, and assigned person names.
     *
     * @return A string representation of the table.
     */
    @Override
    public String toString() {
        return "Table{ID=" + tableId + ", Capacity=" + capacity + ", Person Names="
            + uniquePersonList + "}";
    }

    /**
     * Compares this table to another object for equality.
     * Two tables are considered equal if they have the same table ID, capacity, and person list.
     *
     * @param other The object to compare.
     * @return {@code true} if both tables are equal, otherwise {@code false}.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Table)) {
            return false;
        }
        Table table = (Table) other;
        return tableId == table.tableId && capacity == table.capacity
            && uniquePersonList.equals(table.uniquePersonList);
    }

    /**
     * Returns a hash code for this table, based on its ID, capacity, and person list.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(tableId, capacity, uniquePersonList);
    }
}
