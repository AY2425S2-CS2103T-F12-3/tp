package seedu.address.model.table;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.UniqueList;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.table.exceptions.TableNotFoundException;

/**
 * Represents a unique list of tables in a wedding.
 * <p>
 * This class ensures that tables remain unique within the list.
 * It provides methods for adding, deleting, searching, and managing persons within tables.
 * </p>
 * <p>
 * This list does not allow duplicate tables based on {@code Table#isSameTable(Table)}.
 * </p>
 */
public class UniqueTableList implements Iterable<Table>, UniqueList<Table> {

    private final ObservableList<Table> internalList = FXCollections.observableArrayList();
    private final ObservableList<Table> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Creates a new empty UniqueTableList.
     */
    public UniqueTableList() {

    }

    /**
     * Creates a new UniqueTableList containing copies of the Tables in the given list.
     *
     * @param other The UniqueTableList to copy tables from
     */
    public UniqueTableList(UniqueTableList other) {
        requireNonNull(other);
        for (Table t : other) {
            addTable(new Table(t));
        }
    }

    /**
     * Checks if a table with the same identity as {@code toCheck} exists in the list.
     *
     * @param toCheck The table to check.
     * @return {@code true} if the table exists, otherwise {@code false}.
     */
    public boolean contains(Table toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameTable);
    }

    /**
     * Returns an iterator over the tables in this list.
     *
     * @return An iterator over tables in this list.
     */
    public Iterator<Table> iterator() {
        return internalList.iterator();
    }

    /**
     * Adds a new table to the list.
     * Ensures that the table does not already exist in the list.
     *
     * @param toAdd The table to add.
     * @throws IllegalArgumentException if a table with the same ID already exists.
     */
    public void addTable(Table toAdd) {
        requireNonNull(toAdd);

        if (hasTableById(toAdd.getTableId())) {
            throw new IllegalArgumentException("Table with ID " + toAdd.getTableId() + " already exists.");
        }
        internalList.add(toAdd);
    }

    /**
     * Deletes a table from the list by its ID.
     *
     * @param tableId The ID of the table to be deleted.
     * @throws TableNotFoundException if the table does not exist.
     */
    public void deleteTableById(int tableId) {
        requireNonNull(tableId);
        Table table = findTableById(tableId);
        if (table != null) {
            internalList.remove(table);
            return;
        }
        throw new TableNotFoundException();
    }

    /**
     * Deletes a table from the list.
     *
     * @param table The table to be deleted.
     */
    public void deleteTable(Table table) {
        requireNonNull(table);
        internalList.remove(table);
    }

    /**
     * Finds a table in the list by its ID.
     *
     * @param tableId The ID of the table to find.
     * @return An {@code Optional} containing the table if found, otherwise an empty {@code Optional}.
     */
    public Table findTableById(int tableId) throws TableNotFoundException {
        requireNonNull(tableId);
        try {
            return internalList.stream().filter(table -> table.getTableId() == tableId)
                .findFirst().get();
        } catch (NoSuchElementException nsee) {
            throw new TableNotFoundException();
        }
    }

    /**
     * Finds a table in the list with the same table ID as the provided table.
     *
     * @param table The table containing the ID to find.
     * @return The table if found, otherwise null.
     */
    public Table findTable(Table table) {
        requireNonNull(table);
        try {
            return internalList.stream().filter(t -> t.getTableId() == table.getTableId())
                .findFirst().get();
        } catch (NoSuchElementException nsee) {
            return null;
        }
    }

    /**
     * Returns whether the given table is within this table list.
     * @param table to check if it exists
     * @return boolean value for whether the given table exists in the list
     */
    public boolean hasTable(Table table) {
        requireNonNull(table);
        return internalList.contains(table);
    }

    /**
     * Checks whether a table with the given ID exists in the list.
     *
     * @param tableId The ID of the table to check.
     * @return {@code true} if the table exists, otherwise {@code false}.
     */
    public boolean hasTableById(int tableId) {
        return internalList.stream().anyMatch(table -> table.getTableId() == tableId);
    }

    /**
     * Assigns a person to a table based on the table id.
     *
     * @param tableId  The ID of the table.
     * @param person The new person to be added in this table.
     * @throws TableNotFoundException if the table does not exist.
     */
    public void addPersonToTableById(Person person, int tableId) {
        requireAllNonNull(tableId, person);

        Table table = findTableById(tableId);
        if (table == null) {
            throw new TableNotFoundException();
        }

        addPersonToTable(person, table);
    }

    /**
     * Assigns a person to a specified table.
     * Updates the internal list as adding a person to a table should techincally
     * 'update' the table.
     *
     * @param table The table to assign the person to.
     * @param person The person to be assigned.
     * @throws TableNotFoundException if the table does not exist.
     */
    public void addPersonToTable(Person person, Table table) {
        requireAllNonNull(table, person);

        if (!hasTable(table)) {
            throw new TableNotFoundException();
        }
        Table updatedTable = new Table(table);

        updatedTable.addPerson(person);

        internalList.set(internalList.indexOf(table), updatedTable);
    }

    /**
     * Removes a person from a table.
     *
     * @param tableId  The ID of the table.
     * @param person  The person to be added.
     * @throws TableNotFoundException if the table does not exist.
     */
    public void deletePersonFromTableById(Person person, int tableId) {
        requireAllNonNull(tableId, person);

        Table table = findTableById(tableId);

        if (table == null) {
            throw new TableNotFoundException();
        }

        deletePersonFromTable(person, table);
    }

    /**
     * Deletes the given person from the given table
     * if the person exists in the given table.
     * @param person to be deleted
     * @param table to be deleted from
     */
    public void deletePersonFromTable(Person person, Table table) {
        requireAllNonNull(table, person);

        Table tableWithSameId = findTableById(table.getTableId());
        int index = internalList.indexOf(tableWithSameId);

        if (index == -1) {
            throw new TableNotFoundException();
        }

        tableWithSameId.deletePerson(person);

        Table updatedTable = new Table(tableWithSameId);
        internalList.set(index, updatedTable);
    }

    /**
     * Sets a target table to an edited table with the same identity.
     * The target table must exist in the list.
     *
     * @param target The table to be replaced.
     * @param editedTable The edited version of the table.
     * @throws TableNotFoundException if the target table is not found.
     * @throws DuplicatePersonException if the edited table is a duplicate of an existing table.
     */
    public void setTable(Table target, Table editedTable) {
        requireAllNonNull(target, editedTable);

        Table tableWithSameId = findTableById(target.getTableId());
        int index = internalList.indexOf(tableWithSameId);
        if (index == -1) {
            throw new TableNotFoundException();
        }

        if (!tableWithSameId.isSameTable(editedTable) && contains(editedTable)) {
            throw new DuplicatePersonException();
        }

        internalList.set(index, editedTable);
    }

    /**
     * Replaces the contents of this list with {@code tables}.
     * {@code tables} must not contain duplicate persons.
     */
    public void setTables(List<Table> tables) {
        requireAllNonNull(tables);
        if (!tablesAreUnique(tables)) {
            throw new DuplicatePersonException();
        }
        internalList.setAll(tables);
    }

    /**
     * Returns the list of tables as an unmodifiable {@code ObservableList}.
     * This ensures that the list cannot be modified externally.
     *
     * @return An unmodifiable list of tables.
     */
    public ObservableList<Table> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UniqueTableList)) {
            return false;
        }
        UniqueTableList otherUniqueTableList = (UniqueTableList) other;

        return internalList.equals(otherUniqueTableList.internalList);
    }

    /**
     * Returns the number of tables in this list.
     *
     * @return The number of tables in this list.
     */
    public int size() {
        return internalList.size();
    }


    @Override
    public Iterable<Table> getListItems() {
        return this;
    }

    @Override
    public void clear() {
        this.internalList.clear();
        this.internalUnmodifiableList.clear();
    }

    @Override
    public void loadData(UniqueList<Table> other) {
        this.clear();

        for (Table t : other.getListItems()) {
            this.addTable(t);
        }
    }

    /**
     * Returns true if {@code tables} contains only unique persons.
     */
    private boolean tablesAreUnique(List<Table> tables) {
        for (int i = 0; i < tables.size() - 1; i++) {
            for (int j = i + 1; j < tables.size(); j++) {
                if (tables.get(i).isSameTable(tables.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
