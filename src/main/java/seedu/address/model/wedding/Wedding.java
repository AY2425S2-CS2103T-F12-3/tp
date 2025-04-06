package seedu.address.model.wedding;

import java.util.Objects;

import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.table.Table;
import seedu.address.model.table.UniqueTableList;
import seedu.address.model.table.exceptions.TableNotFoundException;

/**
 * Represents a Wedding in the address book.
 */
public class Wedding {
    private final String name;
    private final UniquePersonList uniquePersonList;
    private final UniqueTableList tableList; // All tables for seating

    /**
     * Constructs a new {@code Wedding} with the given name.
     * Initializes empty person and table lists.
     *
     * @param name Name of the wedding.
     */
    public Wedding(String name) {
        this.name = name;
        this.uniquePersonList = new UniquePersonList();
        this.tableList = new UniqueTableList();
    }

    /**
     * Constructs a deep copy of the given wedding.
     *
     * @param wedding The wedding to be copied.
     */
    public Wedding(Wedding wedding) {
        this.name = wedding.name;
        this.uniquePersonList = new UniquePersonList(wedding.uniquePersonList);
        this.tableList = new UniqueTableList(wedding.tableList);
    }

    /**
     * Returns the name of the wedding.
     *
     * @return The name of the wedding.
     */
    public String getName() {
        return name;
    }

    public String getNameLower() {
        return name.toLowerCase();
    }

    /**
     * Returns the list of unique persons in this wedding.
     *
     * @return The unique person list.
     */
    public UniquePersonList getUniquePersonList() {
        return uniquePersonList;
    }

    /**
     * Returns the list of tables in this wedding.
     *
     * @return The unique table list.
     */
    public UniqueTableList getTableList() {
        return tableList;
    }

    // =========== Persons =====================================================

    /**
     * Adds a person to the wedding's guest list.
     *
     * @param person The person to add.
     */
    public void addPerson(Person person) {
        uniquePersonList.add(person);
    }

    /**
     * Removes a person from the wedding's guest list.
     *
     * @param person The person to delete.
     */
    public void deletePerson(Person person) {
        uniquePersonList.delete(person);
    }

    /**
     * Replaces the specified person with the edited person.
     *
     * @param target The person to be replaced.
     * @param editedPerson The replacement person.
     */
    public void setPerson(Person target, Person editedPerson) {
        uniquePersonList.setPerson(target, editedPerson);
    }

    /**
     * Replaces the existing person list with a new one.
     *
     * @param replacement The new person list to replace the existing one.
     */
    public void setPersons(UniquePersonList replacement) {
        this.uniquePersonList.setPersons(replacement);
    }

    /**
     * Finds a person from the table with the matching name. Throws a PersonNotFoundException if there isn't any
     *
     * @param name Name of the person to find
     * @return A Person object with the corresponding name
     * @throws PersonNotFoundException
     */
    public Person findPersonByName(Name name) throws PersonNotFoundException {
        return uniquePersonList.findPersonByName(name);
    }

    /**
     * Checks if the given person exists in the wedding's guest list.
     *
     * @param person The person to check.
     * @return True if the person exists in the wedding's guest list, false otherwise.
     */
    public boolean hasPerson(Person person) {
        return uniquePersonList.contains(person);
    }

    // =========== Tables =====================================================

    /**
     * Adds a table to the wedding's seating arrangement.
     *
     * @param table The table to add.
     */
    public void addTable(Table table) {
        this.tableList.addTable(table);
    }

    /**
     * Removes a table from the wedding's seating arrangement.
     *
     * @param table The table to delete.
     */
    public void deleteTable(Table table) {
        tableList.deleteTable(table);
    }

    /**
     * Removes a table with the specified ID from the wedding's seating arrangement.
     *
     * @param tableId The ID of the table to delete.
     */
    public void deleteTableById(int tableId) {
        Table table = tableList.findTableById(tableId);
        deleteTable(table);
    }

    /**
     * Finds a table that matches the given table.
     *
     * @param table The table to find.
     * @return The matching table if found.
     */
    public Table findTable(Table table) {
        return tableList.findTable(table);
    }

    /**
     * Finds a table with the specified ID.
     *
     * @param tableId The ID of the table to find.
     * @return The table with the matching ID if found.
     */
    public Table findTableById(int tableId) {
        return tableList.findTableById(tableId);
    }

    /**
     * Checks if the given table exists in the wedding's seating arrangement.
     *
     * @param table The table to check.
     * @return True if the table exists in the wedding's seating arrangement, false otherwise.
     */
    public boolean hasTable(Table table) {
        return tableList.contains(table);
    }

    /**
     * Checks if a table with the specified ID exists in the wedding's seating arrangement.
     *
     * @param tableId The ID of the table to check.
     * @return True if a table with the specified ID exists, false otherwise.
     */
    public boolean hasTableById(int tableId) {
        return tableList.hasTableById(tableId);
    }

    /**
     * Replaces the specified table with the edited table.
     *
     * @param target The table to be replaced.
     * @param editedPerson The replacement table.
     */
    public void setTable(Table target, Table editedPerson) {
        tableList.setTable(target, editedPerson);
    }

    /**
     * Assigns a person to a specific table.
     *
     * @param p The person to be assigned.
     * @param table The table to assign the person to.
     */
    public void addPersonToTable(Person p, Table table) {
        if (!hasPerson(p)) {
            throw new PersonNotFoundException();
        }

        if (p.isSeated()) {
            deletePersonFromTableById(p, p.getTableId());
        }

        Person seatedPerson = new Person(p, table.getTableId());

        tableList.addPersonToTable(seatedPerson, table);
        this.uniquePersonList.setPerson(p, seatedPerson);
    }

    /**
     * Assigns a person to a table with the specified ID.
     *
     * @param p The person to be assigned.
     * @param tableId The ID of the table to assign the person to.
     */
    public void addPersonToTableById(Person p, int tableId) {
        Table t = findTableById(tableId);
        addPersonToTable(p, t);
    }

    /**
     * Removes a person from a specific table.
     *
     * @param p The person to be removed.
     * @param table The table to remove the person from.
     * @throws TableNotFoundException If the table does not exist.
     */
    public void deletePersonFromTable(Person p, Table table) {
        if (!hasPerson(p)) {
            throw new PersonNotFoundException();
        }
        tableList.deletePersonFromTable(p, table);
        this.uniquePersonList.setPerson(p, new Person(p, -1));
    }

    /**
     * Removes a person from a table with the specified ID.
     *
     * @param p The person to be removed.
     * @param tableId The ID of the table to remove the person from.
     * @throws TableNotFoundException If no table with the specified ID exists.
     */
    public void deletePersonFromTableById(Person p, int tableId) {
        tableList.deletePersonFromTableById(p, tableId);
    }

    // =========== Utils ======================================================

    /**
     * Checks if two weddings are equal.
     *
     * @param other The other object to compare with.
     * @return True if equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Wedding)) {
            return false;
        }
        Wedding otherWedding = (Wedding) other;

        String weddingNameLower = name.toLowerCase();
        String otherWeddingNameLower = otherWedding.name.toLowerCase();

        return weddingNameLower.equals(otherWeddingNameLower)
                && uniquePersonList.equals(otherWedding.uniquePersonList)
                && tableList.equals(otherWedding.tableList);
    }

    /**
     * Returns the hash code for the wedding.
     *
     * @return Hash code integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, uniquePersonList, tableList);
    }

    /**
     * Returns a string representation of the wedding.
     *
     * @return String describing the wedding.
     */
    @Override
    public String toString() {
        return "Wedding{name='" + name + "', people= " + uniquePersonList + '}';
    }

    /**
     * Checks if this wedding is the same as another wedding. Two weddings are considered the same if they have the same
     * name.
     *
     * @param otherWedding The wedding to compare with.
     * @return true if both weddings have the same name, false otherwise.
     */
    public boolean isSameWedding(Wedding otherWedding) {
        if (otherWedding == this) {
            return true;
        }

        return otherWedding != null && otherWedding.getNameLower().equals(getNameLower());
    }
}
