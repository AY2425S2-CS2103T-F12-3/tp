package seedu.address.model.table;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.table.exceptions.TableNotFoundException;
import seedu.address.testutil.PersonBuilder;

public class UniqueTableListTest {
    private UniqueTableList uniqueTableList;
    private Table table1;
    private Table table2;
    private Person alice;
    private Person bob;

    @BeforeEach
    public void setUp() {
        uniqueTableList = new UniqueTableList();
        table1 = new Table(1, 2);
        table2 = new Table(2, 3);
        alice = new PersonBuilder(ALICE).build();
        bob = new PersonBuilder(BOB).build();
    }

    @Test
    public void addTable_success() {
        uniqueTableList.addTable(table1);
        assertEquals(1, uniqueTableList.size());
        assertTrue(uniqueTableList.hasTable(table1));
    }

    @Test
    public void addTable_duplicateTable_throwsException() {
        uniqueTableList.addTable(table1);
        assertThrows(IllegalArgumentException.class, () -> uniqueTableList.addTable(new Table(1, 5)));
    }

    @Test
    public void deleteTable_success() {
        uniqueTableList.addTable(table1);
        uniqueTableList.deleteTable(table1);
        assertFalse(uniqueTableList.hasTable(table1));
    }

    @Test
    public void deleteTableById_success() {
        uniqueTableList.addTable(table1);
        uniqueTableList.deleteTableById(1);
        assertFalse(uniqueTableList.hasTableById(1));
    }

    @Test
    public void deleteTableById_tableNotFound_throwsException() {
        assertThrows(TableNotFoundException.class, () -> uniqueTableList.deleteTableById(99));
    }

    @Test
    public void findTableById_success() {
        uniqueTableList.addTable(table1);
        assertEquals(table1, uniqueTableList.findTableById(1));
    }

    @Test
    public void findTableById_notFound_throwsException() {
        assertThrows(TableNotFoundException.class, () -> uniqueTableList.findTableById(100));
    }

    @Test
    public void addPersonToTable_success() {
        uniqueTableList.addTable(table1);
        uniqueTableList.addPersonToTable(alice, table1);
        Table updated = uniqueTableList.findTableById(1);
        assertEquals(1, updated.getSize());
        assertTrue(updated.getAllPersons().contains(alice));
    }

    @Test
    public void deletePersonFromTable_success() {
        uniqueTableList.addTable(table1);
        uniqueTableList.addPersonToTable(alice, table1);
        uniqueTableList.deletePersonFromTable(alice, table1);
        assertEquals(0, uniqueTableList.findTableById(1).getSize());
    }

    @Test
    public void setTable_success() {
        uniqueTableList.addTable(table1);
        Table edited = new Table(1, 10);
        uniqueTableList.setTable(table1, edited);
        assertEquals(10, uniqueTableList.findTableById(1).getCapacity());
    }

    @Test
    public void setTables_success() {
        uniqueTableList.setTables(Arrays.asList(table1, table2));
        assertEquals(2, uniqueTableList.size());
    }

    @Test
    public void setTables_duplicateTables_throwsException() {
        List<Table> listWithDuplicates = Arrays.asList(table1, new Table(1, 2));
        assertThrows(DuplicatePersonException.class, () -> uniqueTableList.setTables(listWithDuplicates));
    }

    @Test
    public void equals_sameContent_returnsTrue() {
        UniqueTableList otherList = new UniqueTableList();
        otherList.addTable(table1);
        uniqueTableList.addTable(table1);
        assertEquals(uniqueTableList, otherList);
    }

    @Test
    public void equals_differentContent_returnsFalse() {
        UniqueTableList otherList = new UniqueTableList();
        otherList.addTable(table2);
        uniqueTableList.addTable(table1);
        assertNotEquals(uniqueTableList, otherList);
    }
}
