// package seedu.address.model.table;

// import static java.util.Objects.requireNonNull;
// import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

// import java.util.Iterator;
// import java.util.List;

// import javafx.collections.FXCollections;
// import javafx.collections.ObservableList;
// //import seedu.address.model.Table.exceptions.DuplicateTableException;
// //import seedu.address.model.Table.exceptions.TableNotFoundException;

// /**
//  * A list of Tables that enforces uniqueness between its elements and does not allow nulls.
//  * A Table is considered unique by comparing using {@code Table#isSameTable(Table)}. As such, adding and updating of
//  * Tables uses Table#isSameTable(Table) for equality so as to ensure that the Table being added or updated is
//  * unique in terms of identity in the UniqueTableList. However, the removal of a Table uses Table#equals(Object) so
//  * as to ensure that the Table with exactly the same fields will be removed.
//  *
//  * Supports a minimal set of list operations.
//  *
//  * @see Table#isSameTable(Table)
//  */
// public class UniqueTableList implements Iterable<Table> {

//     private final ObservableList<Table> internalList = FXCollections.observableArrayList();
//     private final ObservableList<Table> internalUnmodifiableList =
//             FXCollections.unmodifiableObservableList(internalList);

//     /**
//      * Returns true if the list contains an equivalent Table as the given argument.
//      */
//     public boolean contains(Table toCheck) {
//         requireNonNull(toCheck);
//         return internalList.stream().anyMatch(toCheck::isSameTable);
//     }

//     /**
//      * Adds a Table to the list.
//      * The Table must not already exist in the list.
//      */
//     public void add(Table toAdd) {
//         requireNonNull(toAdd);
//         if (contains(toAdd)) {
//             throw new DuplicateTableException();
//         }
//         internalList.add(toAdd);
//     }

//     /**
//      * Replaces the Table {@code target} in the list with {@code editedTable}.
//      * {@code target} must exist in the list.
//      * The Table identity of {@code editedTable} must not be the same as another existing Table in the list.
//      */
//     public void setTable(Table target, Table editedTable) {
//         requireAllNonNull(target, editedTable);

//         int index = internalList.indexOf(target);
//         if (index == -1) {
//             throw new TableNotFoundException();
//         }

//         if (!target.isSameTable(editedTable) && contains(editedTable)) {
//             throw new DuplicateTableException();
//         }

//         internalList.set(index, editedTable);
//     }

//     /**
//      * Removes the equivalent Table from the list.
//      * The Table must exist in the list.
//      */
//     public void remove(Table toRemove) {
//         requireNonNull(toRemove);
//         if (!internalList.remove(toRemove)) {
//             throw new TableNotFoundException();
//         }
//     }

//     public void setTables(UniqueTableList replacement) {
//         requireNonNull(replacement);
//         internalList.setAll(replacement.internalList);
//     }

//     /**
//      * Replaces the contents of this list with {@code Tables}.
//      * {@code Tables} must not contain duplicate Tables.
//      */
//     public void setTables(List<Table> Tables) {
//         requireAllNonNull(Tables);
//         if (!TablesAreUnique(Tables)) {
//             throw new DuplicateTableException();
//         }

//         internalList.setAll(Tables);
//     }

//     /**
//      * Returns the backing list as an unmodifiable {@code ObservableList}.
//      */
//     public ObservableList<Table> asUnmodifiableObservableList() {
//         return internalUnmodifiableList;
//     }

//     @Override
//     public Iterator<Table> iterator() {
//         return internalList.iterator();
//     }

//     @Override
//     public boolean equals(Object other) {
//         if (other == this) {
//             return true;
//         }

//         // instanceof handles nulls
//         if (!(other instanceof UniqueTableList)) {
//             return false;
//         }

//         UniqueTableList otherUniqueTableList = (UniqueTableList) other;
//         return internalList.equals(otherUniqueTableList.internalList);
//     }

//     @Override
//     public int hashCode() {
//         return internalList.hashCode();
//     }

//     @Override
//     public String toString() {
//         return internalList.toString();
//     }

//     /**
//      * Returns true if {@code Tables} contains only unique Tables.
//      */
//     private boolean TablesAreUnique(List<Table> Tables) {
//         for (int i = 0; i < Tables.size() - 1; i++) {
//             for (int j = i + 1; j < Tables.size(); j++) {
//                 if (Tables.get(i).isSameTable(Tables.get(j))) {
//                     return false;
//                 }
//             }
//         }
//         return true;
//     }
// }
