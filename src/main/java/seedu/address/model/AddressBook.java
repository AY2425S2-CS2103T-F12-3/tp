package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.exceptions.NoCurrentWeddingException;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.table.Table;
import seedu.address.model.table.UniqueTableList;
import seedu.address.model.wedding.UniqueWeddingList;
import seedu.address.model.wedding.Wedding;
import seedu.address.model.wedding.exceptions.WeddingNotFoundException;

/**
 * Wraps all data at the address-book level Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    /**
     * Manages the list of weddings in the address book.
     */
    private Wedding currentWedding;

    /**
     * Initializes the address book with a default wedding and sets it as the current wedding.
     */
    private final UniqueWeddingList uniqueWeddingList;
    private final UniquePersonList personList;
    private final UniqueTableList tableList;

    /**
     * Initializes the AddressBook object
     * with blank UniqueLists
     */
    public AddressBook() {
        uniqueWeddingList = new UniqueWeddingList();
        personList = new UniquePersonList();
        tableList = new UniqueTableList();
    }

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     *
     * @param toBeCopied the ReadOnlyAddressBook to copy from
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    @Override
    public ObservableList<Wedding> getWeddingList() {
        return this.uniqueWeddingList.asUnmodifiableObservableList();
    }

    /**
    * Replaces the contents of the person list with {@code persons}.
    * {@code persons} must not contain duplicate persons.
    *
    * @param persons the UniquePersonList to set
    */
    public void setPersons(UniquePersonList persons) {
        this.currentWedding.setPersons(persons);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     *
     * @param newData the new data to reset to
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        // Add all weddings from newData, ensuring no duplicates
        for (Wedding wedding : newData.getWeddingList()) {
            if (!hasWedding(wedding)) {
                Wedding toBeAdded = new Wedding(wedding);
                this.uniqueWeddingList.addWedding(toBeAdded);
            }
        }

        // Set current wedding if available
        if (newData.hasCurrentWedding() && !uniqueWeddingList.asUnmodifiableObservableList().isEmpty()) {
            setCurrentWeddingByName(newData.getCurrentWedding().getName());
        }
    }

    // =========== Weddings
    // ================================================================================
    /**
     * Adds a wedding to the address book.
     * The wedding must not already exist in the address book.
     *
     * @param wedding The wedding to be added
     */
    public void addWedding(Wedding wedding) {
        this.uniqueWeddingList.addWedding(wedding);
    }

    /**
     * Creates a new wedding with the given name and adds it to the address book.
     *
     * @param weddingName The name of the wedding to be created
     */
    public void createWeddingWithName(String weddingName) {
        Wedding newWedding = new Wedding(weddingName);
        addWedding(newWedding);
    }

    /**
     * Sets the active wedding in the address book. Replaces any existing wedding.
     */
    public void setCurrentWedding(Wedding wedding) {
        requireNonNull(wedding);

        if (this.uniqueWeddingList.findWeddingByName(wedding.getName()) == null) {
            throw new WeddingNotFoundException();
        }

        this.currentWedding = wedding;

        personList.loadData(wedding.getUniquePersonList());
        tableList.loadData(wedding.getTableList());
    }

    /**
     * Sets the current wedding by its name.
     *
     * @param weddingName the name of the wedding to set as current
     */
    public void setCurrentWeddingByName(String weddingName) {
        requireNonNull(weddingName);

        Wedding weddingWithMatchingName = uniqueWeddingList.findWeddingByName(weddingName);

        setCurrentWedding(weddingWithMatchingName);
    }

    /**
     * Retrieves the currently active wedding.
     *
     * @return the current Wedding
     */
    public Wedding getCurrentWedding() {
        return currentWedding;
    }

    public Wedding getWeddingByName(String weddingName) {
        return this.uniqueWeddingList.findWeddingByName(weddingName);
    }

    /**
     * Deletes the specified wedding from the address book.
     * If there is more than one wedding and the deleted wedding is the current wedding,
     * another wedding will be set as current.
     * If there is only one wedding, the current wedding will be set to null after deletion.
     *
     * @param wedding The wedding to be deleted
     */
    public void deleteWedding(Wedding wedding) {

        if (!uniqueWeddingList.hasWedding(wedding)) {
            throw new WeddingNotFoundException();
        }

        uniqueWeddingList.deleteWedding(wedding);

        if (wedding == currentWedding) {
            personList.clear();
            tableList.clear();
            currentWedding = null;
        }
    }

    /**
     * Deletes the current wedding from the address book.
     * Delegates to deleteWedding method with the current wedding as parameter.
     */
    public void deleteCurrentWedding() {
        deleteWedding(currentWedding);
    }

    /**
     * Deletes a wedding by its name from the address book.
     * First finds the wedding with the matching name, then deletes it.
     * If the wedding with the given name is not found, a WeddingNotFoundException will be thrown.
     *
     * @param weddingName The name of the wedding to be deleted
     * @throws WeddingNotFoundException if no wedding with the given name exists
     */
    public void deleteWeddingByName(String weddingName) {
        Wedding weddingWithMatchingName = uniqueWeddingList.findWeddingByName(weddingName);

        if (weddingWithMatchingName == null) {
            throw new WeddingNotFoundException();
        }

        deleteWedding(weddingWithMatchingName);
    }

    /**
     * Checks if a wedding currently exists in the address book.
     *
     * @return true if a current wedding exists, false otherwise
     */
    @Override
    public boolean hasCurrentWedding() {
        return currentWedding != null;
    }

    /**
     * Checks if a specific wedding exists in the address book.
     *
     * @param wedding the Wedding to check for
     * @return true if the wedding exists, false otherwise
     */
    public boolean hasWedding(Wedding wedding) {
        return uniqueWeddingList.contains(wedding);
    }

    /**
     * Sets a wedding in the address book.
     *
     * @param wedding the Wedding to set
     * @param target the Wedding to be replaced
     */
    public void setWedding(Wedding wedding, Wedding target) {
        this.uniqueWeddingList.setWedding(wedding, target);
    }

    /**
     * Sets multiple weddings in the address book.
     *
     * @param weddings the List of Weddings to set
     */
    public void setWeddings(List<Wedding> weddings) {
        this.uniqueWeddingList.setWeddings(weddings);
    }

    /**
     * Sets multiple weddings from a UniqueWeddingList.
     *
     * @param uniqueWeddingList the UniqueWeddingList to set weddings from
     */
    public void setWeddings(UniqueWeddingList uniqueWeddingList) {
        this.uniqueWeddingList.setWeddings(uniqueWeddingList);
    }

    // =========== Persons =====================================================

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     *
     * @param person the Person to check for
     * @return true if the person exists, false otherwise
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return personList.contains(person) && getCurrentWedding().hasPerson(person);
    }

    /**
     * Adds a person to the address book. The person must not already exist in the address book.
     */
    public void addPerson(Person person) {
        if (currentWedding == null) {
            throw new NoCurrentWeddingException();
        }
        if (hasPerson(person)) {
            throw new DuplicatePersonException();
        }
        currentWedding.addPerson(person);
        personList.add(person);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}. {@code target} must exist in the
     * address book. The person identity of {@code editedPerson} must not be the same as another existing person in the
     * address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        personList.setPerson(target, editedPerson);
        getCurrentWedding().setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}. {@code key} must exist in the address book.
     */
    public void deletePerson(Person key) {
        personList.delete(key);
        getCurrentWedding().deletePerson(key);
    }

    /**
     * Finds a person by their name.
     *
     * @param name the Name of the person to find
     * @return the Person with the matching name
     */
    public Person findPersonByName(Name name) {
        return getCurrentWedding().findPersonByName(name);
    }

    // =========== Tables =====================================================

    /**
     * Checks if a table exists in the current wedding.
     *
     * @param table the Table to check for
     * @return true if the table exists, false otherwise
     */
    public boolean hasTable(Table table) {
        return this.tableList.hasTable(table) && getCurrentWedding().hasTable(table);
    }

    /**
     * Checks if a table exists by its ID.
     *
     * @param tableId the ID of the table to check for
     * @return true if the table exists, false otherwise
     */
    public boolean hasTable(int tableId) {
        return this.tableList.hasTableById(tableId) && getCurrentWedding().hasTableById(tableId);
    }

    /**
     * Adds a table to the current wedding.
     *
     * @param table the Table to add
     */
    public void addTable(Table table) {
        tableList.addTable(table);
        getCurrentWedding().addTable(table);
    }

    /**
     * Deletes a specified table from the current wedding.
     *
     * @param table the Table to delete
     */
    public void deleteTable(Table table) {
        for (Person p : table.getAllPersons()) {
            Person removedFromTable = new Person(p, -1);
            currentWedding.setPerson(p, removedFromTable);
            this.personList.setPerson(p, removedFromTable);
        }

        tableList.deleteTable(table);

        getCurrentWedding().deleteTable(table);
    }

    /**
     * Deletes a table from the current wedding by its ID.
     *
     * @param tableId the ID of the table to delete
     */
    public void deleteTableById(int tableId) {
        Table tableWithId = getTableById(tableId);
        deleteTable(tableWithId);
    }

    /**
     * Sets a table in the current wedding.
     *
     * @param target the Table to be replaced
     * @param editedTable the new Table to replace the target
     */
    public void setTable(Table target, Table editedTable) {
        requireNonNull(editedTable);
        tableList.setTable(target, editedTable);
        getCurrentWedding().setTable(target, editedTable);
    }

    /**
     * Adds a person to a specified table.
     *
     * @param p the Person to add
     * @param table the Table to add the person to
     */
    public void addPersonToTable(Person p, Table table) {
        int tableId = table.getTableId();
        getCurrentWedding().addPersonToTable(p, table);
        personList.setPerson(p, new Person(p, tableId));
        tableList.setTable(table, getCurrentWedding().findTableById(tableId));
    }

    /**
     * Adds a person to a specified table by its ID.
     *
     * @param p the Person to add
     * @param tableId the ID of the Table to add the person to
     */
    public void addPersonToTableById(Person p, int tableId) {
        Table t = this.tableList.findTableById(tableId);
        addPersonToTable(p, t);
    }

    /**
     * Deletes a person from a specified table.
     *
     * @param p the Person to delete
     * @param table the Table to delete the person from
     */
    public void deletePersonFromTable(Person p, Table table) {
        personList.setPerson(p, new Person(p, -1));
        this.getCurrentWedding().deletePersonFromTable(p, table);
        tableList.setTable(table, this.currentWedding.findTableById(table.getTableId()));
    }

    /**
     * Deletes a person from a specified table by its ID.
     *
     * @param p the Person to delete
     * @param tableId the ID of the Table to delete the person from
     */
    public void deletePersonFromTable(Person p, int tableId) {
        Table tableWithId = this.getCurrentWedding().findTableById(tableId);
        deletePersonFromTable(p, tableWithId);
    }

    /**
     * Retrieves a table by its ID.
     *
     * @param tableId the ID of the table to retrieve
     * @return the Table with the matching ID
     */
    public Table getTableById(int tableId) {
        return getCurrentWedding().findTableById(tableId);
    }

    // =========== Utils ======================================================

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("weddings", uniqueWeddingList).add("current wedding", currentWedding)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return this.personList.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Table> getTableList() {
        return this.tableList.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return uniqueWeddingList.equals(otherAddressBook.uniqueWeddingList);

    }

    @Override
    public int hashCode() {
        return uniqueWeddingList.hashCode();
    }

}
