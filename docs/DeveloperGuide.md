---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# WeddingHero Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**
- The project is based on the AddressBook-Level3 project created by the SE-EDU initiative
- Github CoPilot was used by Bhavina Sathish Kumar to write trivial test cases and the JavaDocs for some trivial methods


_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [ `Main` ](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [ `MainApp` ](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point).

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside components being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [ `Ui.java` ](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g. `CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [ `MainWindow` ](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [ `MainWindow.fxml` ](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` objects residing in the `Model`.

### Logic component

**API**: [ `Logic.java` ](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>
**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
2. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
3. The command can communicate with the `Model` when it is executed (e.g., to delete a guest).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
4. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g., during testing.

### Model component

**API**: [ `Model.java` ](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />

The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object). Each `Person` object represents a guest in the wedding.
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g., the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user's preferences. This is exposed to the outside as a `ReadOnlyUserPref` object.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components).

<box type="info" seamless>
**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>
</box>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

### Storage component

**API**: [ `Storage.java` ](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`).

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

### `addPerson` Command
The `addPerson` command allows a user to add a guest to the currently active wedding's guest list. This command is essential for wedding planners to build and manage their client's guest list.

The implementation involves several key steps and validation checks:

1. The command creates a new `Person` object with the provided details (name, phone, email, tags, address, dietary restrictions, and RSVP status)
2. It then attempts to add this person to the current wedding through the model
3. The implementation includes the following validation checks:
   - Verifying that there is a current wedding set (throws `NoCurrentWeddingException` if not)
   - Ensuring the person is not already in the guest list (throws `DuplicatePersonException` if duplicate found)
4. If successful, it returns a success message with the person's details
5. If an exception is caught, it provides appropriate error feedback to the user

The sequence diagram below illustrates how the `addPerson` command is processed:

<puml src="diagrams/AddPersonActivityDiagram.puml" alt="Sequence Diagram for AddPerson Command" />

This command follows the Command pattern, where the `AddPersonCommand` encapsulates a request as an object, allowing for parameterization of clients with different requests and queue or log requests.

<div style="page-break-after: always;"></div>

### `deletePerson` Command
The `deletePerson` command allows users to remove a guest from the currently active wedding's guest list using their displayed index number.

The implementation involves several key steps and validation checks:

1. The command takes an index parameter to identify which person to delete
2. It retrieves the list of currently displayed persons from the model
3. The implementation includes the following validation checks:
   - Verifying that there is a current wedding set
   - Ensuring the provided index is within valid bounds of the displayed list
4. If the index is valid, it retrieves the person at that index and removes them from the wedding
5. If the person was assigned to a table, they are also removed from that table
6. A success message is returned with details of the deleted person

The activity diagram below illustrates the control flow of this command:

<puml src="diagrams/DeletePersonActivityDiagram.puml" alt="Activity Diagram for DeletePerson Command" />

This command supports the need for wedding planners to be able to update guest lists as clients make changes to their wedding plans.

<div style="page-break-after: always;"></div>

### `filterPersons` Command
The `filterPersons` command allows users to filter the guest list based on dietary restrictions and/or RSVP status. This feature is particularly useful for wedding planners who need to quickly identify specific groups of guests, such as those with special dietary needs or those who have not yet responded to invitations.

The implementation involves several key steps and validation checks:

1. The command can accept one or both filter types: dietary restrictions (d/) and RSVP status (r/)
2. It validates that at least one filter type is specified and prevents multiple filters of the same type
3. It creates the appropriate filter objects based on the specified criteria
4. For cases where both dietary restriction and RSVP filters are specified, it combines them using a logical AND operation
5. It then applies the combined filter to the guest list in the model
6. Finally, it returns a result message showing the number of persons who matched the filter criteria

The activity diagram below illustrates the control flow of this command:

<puml src="diagrams/FilterPersonsActivityDiagram.puml" alt="Activity Diagram for FilterPersons Command" />

This filtering functionality helps wedding planners efficiently organize guests by important attributes, allowing for better catering planning and follow-up on outstanding RSVPs.

<div style="page-break-after: always;"></div>

### `addTable` Command
The `addTable` command allows users to add a new table to the currently active wedding with a specified table ID and capacity.

The implementation involves two main components:

1. **Command Parser (`AddTableCommandParser`)**
   - Validates the command format and required prefixes (tid/ and c/)
   - Extracts and parses the table ID and capacity values
   - Creates a new `AddTableCommand` object with the parsed values

2. **Command Execution (`AddTableCommand`)**
   - Validates that there is a current wedding set
   - Creates a new `Table` object with the specified ID and capacity
   - Attempts to add the table to the wedding
   - Handles various exceptions:
     - `DuplicateTableException`: If a table with the same ID already exists
     - `NoCurrentWeddingException`: If no wedding is currently set
     - `IllegalArgumentException`: If the table ID or capacity is invalid

The activity diagram below illustrates the control flow of this command:

<puml src="diagrams/AddTableActivityDiagram.puml" alt="Activity Diagram for AddTable Command" />

This command is essential for wedding planners to set up the initial table layout for their events.

<div style="page-break-after: always;"></div>

### `deleteTable` Command
The `deleteTable` command allows users to remove a table from the currently active wedding using its table ID.

The implementation involves two main components:

1. **Command Parser (`DeleteTableCommandParser`)**
   - Supports two formats:
     - Prefixed format: `deleteTable tid/1`
     - Raw format: `deleteTable 1`
   - Validates the table ID format and value
   - Creates a new `DeleteTableCommand` object

2. **Command Execution (`DeleteTableCommand`)**
   - Validates that there is a current wedding set
   - Attempts to delete the table with the specified ID
   - Handles exceptions:
     - `TableNotFoundException`: If the table doesn't exist
     - `NoCurrentWeddingException`: If no wedding is currently set

The activity diagram below illustrates the control flow of this command:

<puml src="diagrams/DeleteTableActivityDiagram.puml" alt="Activity Diagram for DeleteTable Command" />

This command helps wedding planners manage their table arrangements by removing tables that are no longer needed.

<div style="page-break-after: always;"></div>

This section describes some noteworthy details on how certain features are implemented.

### `addPersonToTable` Command

The `addPersonToTable`  allows a user to assign a specific guest to a specific table within the currently active wedding.

The implementation involves several validation steps:
- Checking that the input format is correct
- Checking that the specified person exists in the current guest list
- Checking that the table exists
- Ensuring the table has available capacity

Once all conditions are met, the guest is assigned to the table and the system state is saved.

The activity diagram below illustrates the control flow of this command

<puml src="diagrams/AddPersonToTableActivityDiagram.puml" width="600" alt="Activity diagram for addPersonToTable command" />
This command assigns a guest to a specific table in the active wedding.

<div style="page-break-after: always;"></div>

### `deletePersonFromTable` Command
The `deletePersonFromTable` command allows users to remove a person from their assigned table in the currently active wedding. This feature helps wedding planners manage seating arrangements efficiently when plans change.

The implementation involves several key steps and validation checks:

1. The command takes two required parameters: the guest's name and the table ID
2. It first validates that there is a current wedding set
3. It then checks that the specified person exists in the wedding's guest list
4. Next, it verifies that the table with the given ID exists
5. It also confirms that the person is actually seated at the specified table
6. If all validations pass, the person is removed from the table and their table assignment status is updated
7. Finally, a success message is displayed, confirming that the guest has been removed from the specified table

The activity diagram below illustrates the control flow of this command:

<puml src="diagrams/DeletePersonFromTableActivityDiagram.puml" alt="Activity Diagram for DeletePersonFromTable Command" />

This command is useful when guests need to be reassigned to different tables or when a guest cancels their attendance but the planner wishes to retain their information in the guest list without a table assignment.

<div style="page-break-after: always;"></div>

### `findTable` Command
The `findTable` command allows users to search for a specific table in the currently active wedding using its table ID.

The implementation involves two main components:

1. **Command Parser (`FindTableCommandParser`)**
   - Validates that the input starts with "tid/"
   - Parses the table ID value
   - Creates a new `FindTableCommand` object

2. **Command Execution (`FindTableCommand`)**
   - Validates that there is a current wedding set
   - Searches for the table with the specified ID
   - Displays the table's details including:
     - Table ID
     - Capacity
     - Number of assigned guests
   - Handles exceptions:
     - `TableNotFoundException`: If the table doesn't exist
     - `NoCurrentWeddingException`: If no wedding is currently set

The activity diagram below illustrates the control flow of this command:

<puml src="diagrams/FindTableActivityDiagram.puml" alt="Activity Diagram for FindTable Command" />

This command is useful for quickly locating specific tables in large wedding layouts.

<div style="page-break-after: always;"></div>

### `getTables` Command
The `getTables` command allows users to view all tables in the currently active wedding.

The implementation involves two main components:

1. **Command Parser (`GetAllTablesCommandParser`)**
   - Validates that no arguments are provided
   - Creates a new `GetAllTablesCommand` object

2. **Command Execution (`GetAllTablesCommand`)**
   - Validates that there is a current wedding set
   - Retrieves all tables from the wedding
   - Displays each table's details including:
     - Table ID
     - Capacity
     - Number of guests currently assigned
     - List of assigned guests (if any)
   - Handles exceptions:
     - `NoCurrentWeddingException`: If no wedding is currently set

The activity diagram below illustrates the control flow of this command:

<puml src="diagrams/GetTablesActivityDiagram.puml" alt="Activity Diagram for GetTables Command" />

This command provides an overview of the entire table arrangement, helping wedding planners manage their seating plans effectively.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()`, and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th guest in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …` to add a new guest. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>
**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.
</box>

Step 4. The user now decides that adding the guest was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />

<box type="info" seamless>
**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the undo.
</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>
**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>
**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.
</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()`, or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  + Pros: Easy to implement.
  + Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by itself.
  + Pros: Will use less memory (e.g., for `delete`, just save the person being deleted).
  + Cons: We must ensure that the implementation of each individual command is correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile/ user persona**:

Olivia is a professional wedding planner with 5 years of experience organising client weddings. She has to manage a significant number of wedding guests as well as the seating arrangement for the wedding. Olivia needs an efficient way to manage information without distractions.

* professional wedding planner
* has a significant number of wedding guests
* needs to manage guest list and seating arrangements
* needs an efficient way to manage information without distractions

**Value proposition**: Wedding Hero is an all-in-one platform for wedding professionals. Consolidate guest lists, vendors, budgets, and relationships in one intuitive dashboard. Simplify planning, reduce stress, and craft unforgettable celebrations. Experience total control and unwavering confidence with Wedding Hero's reliable, centralised, and streamlined solution.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`


| Priority | As a...                                                 | I want to...                                                                                 | So that I can                                                                              |
|----------|---------------------------------------------------------|------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------|
| ***      | wedding planner                                         | make changes to my clients plans                                                               |                                                                                            |
| ***      | wedding planner of a couple that can’t make up their mind | add and delete the guest list                                                                  | change the guest list should the couple decide to make changes                             |
| ***      | wedding planner of a couple with lots of guests         | indicate whether a guest is confirmed, unconfirmed, or cancelled                               | cater enough food for them                                                                 |
| ***      | wedding planner                                         | keep track of dietary restrictions for guests                                                  | ensure the catering meets everyone’s needs                                                 |
| ***      | wedding planner                                         | track guest RSVPs and their meal preferences                                                   | finalise catering and seating arrangements efficiently                                     |
| ***      | wedding planner                                         | assign guests to tables                                                                        | ensure no seats are left empty within a table                                              |
| ***      | wedding planner of high profile clients                 | store information about guests like contact details, address                                   | send out personalised invitation cards                                                     |
| ***      | wedding planner                                         | see a quick overview of a specific wedding                                                     | easily determine the number of guests and tables currently assigned to my client's wedding |
| ***      | wedding planner with multiple clients                   | view the entire guest list assigned to one of my weddings easily                               | quickly look through guests’ information                                                   |
| ***      | experienced wedding planner                             | set the number of tables early to gauge the number of guests my client has                     | easily manage other logistics such as venue decision, food catering etc.                   |
| ***      | wedding planner                                         | decide how many guests should be seated at one table                                           | customise it to my clients’ needs                                                          |
| ***      | wedding planner                                         | view the entire table list quickly                                                             | quickly see the list of tables and their capacities                                        |
| **       | organised wedding planner                               | filter guests based on their dietary restrictions and RSVP status                              | view guests based on a specific category                                                   |
| *        | forgetful wedding planner                               | mark the status of the vendor list                                                             | keep track of whether a vendor has confirmed                                               |
| *        | detailed wedding planner                                | see a list of upcoming tasks that are most urgent                                              | pay attention to them first                                                                |
| *        | wedding planner                                         | create a library of preferred vendors and pricing details                                      | easily recommend the best options to my clients                                            |
| *        | wedding planner                                         | get a contact list of wedding-related vendors nearby                                           | I don’t have to worry about looking them up personally                                     |
| *        | wedding planner                                         | track expenses against a set budget                                                            | I stay informed of the wedding costs                                                       |

## Use cases (UC)

(For all use cases below, the **System** is the `WeddingHero` and the **guest** is the guest invited to the wedding, unless specified otherwise)

### UC1: Create a wedding

**MSS**
1. User requests to create a new wedding.
2. User provides the wedding name.
3. System validates the wedding name.
4. System creates the new wedding.
5. System confirms the wedding has been created.
   Use case ends.

**Extensions:**

2a. **Invalid Wedding Name**
    2a1. System detects an invalid wedding name.
    2a2. System informs the user that the wedding name is invalid.
    2a3. System prompts the user to provide a valid wedding name.
    2a4. If the user provides a valid name, the process resumes at step 3.

2b. **Duplicate Wedding Name**
    2b1. System detects that a wedding with the same name already exists.
    2b2. System informs the user that the wedding name is already in use.
    2b3. System prompts the user to provide a different wedding name.
    2b4. If the user provides a unique name, the process resumes at step 3.

---

### UC2: Set a wedding

**Preconditions:**
- At least one wedding has been created in the system.

**System Behavior:**
- The active wedding setting is not preserved between application sessions.
- When the application is restarted, no wedding is set as active.
- Users must explicitly set their working wedding after each application launch.

**Design Rationale:**
- The active wedding setting is designed for the current session only.
- This design choice provides several benefits:
  1. **Explicit Context**: Users must consciously choose which wedding they want to work with at the start of each session.
  2. **Safety**: Prevents accidental modifications to the wrong wedding by requiring explicit selection.
  3. **Clarity**: Ensures users are always aware of which wedding they are currently viewing and modifying.
  4. **Fresh Start**: Each session begins with a clean slate, reducing the chance of confusion from previous sessions.
  5. **Intentional Workflow**: Encourages users to be deliberate about which wedding they are working on.

**MSS**

1. User requests to set a wedding as active.
2. User provides the name of the wedding they want to work with.
3. System verifies that the specified wedding exists.
4. System sets the wedding as the active wedding.
5. System confirms that the wedding is now active.
   Use case ends.

**Extensions:**

2a. **Missing Wedding Name**
- 2a1. System detects that no wedding name was provided.
- 2a2. System informs the user that a wedding name is required.
- 2a3. System prompts the user to provide a wedding name.
- 2a4. If the user provides a wedding name, the process resumes at step 3.

2b. **Non-Existent Wedding**
- 2b1. System cannot find the specified wedding.
- 2b2. System informs the user that the wedding does not exist.
- 2b3. System prompts the user to provide a valid wedding name.
- 2b4. If the user provides a valid wedding name, the process resumes at step 3.

---

### UC3: Wedding Overview

**Preconditions:**
- The wedding has been set as the active wedding in WeddingHero.


**MSS**
1. User requests to view the wedding overview.
2. System retrieves the overview details of the current active wedding, including:
   - Number of tables
   - Number of guests
   - List of guests
3. System displays the wedding overview information.
   Use case ends.

**Extensions:**

2a. **No Active Wedding Set**
- 2a1. System detects that no wedding is currently set.
- 2a2. System informs the user that an active wedding must be set first.
- 2a3. System prompts the user to set a wedding as active.
- 2a4. Once a wedding is set as active, the user may request the overview again.

---

### UC4: Add a Guest

**Preconditions:**
- A wedding has been created.
- A wedding has been set as the active wedding.

**MSS**

1. User requests to add a new guest.
2. User provides the guest's details including:
   - Name
   - Phone number
   - Email address
   - Address
   - Dietary restrictions
   - RSVP status
3. System validates the provided details.
4. System adds the guest to the current wedding's guest list.
5. System confirms that the guest has been added successfully.
   Use case ends.

**Extensions:**

2a. **Missing Required Information**
- 2a1. System detects that required guest information is missing.
- 2a2. System informs the user which information is required.
- 2a3. System prompts the user to provide the missing information.
- 2a4. If the user provides the required information, the process resumes at step 3.

2b. **Invalid Information Format**
- 2b1. System detects that some information is in an invalid format.
- 2b2. System informs the user which information is invalid and why.
- 2b3. System prompts the user to correct the invalid information.
- 2b4. If the user provides valid information, the process resumes at step 3.

2c. **Duplicate Guest**
- 2c1. System detects that a guest with the same name already exists.
- 2c2. System informs the user that the guest already exists.
- 2c3. System prompts the user to either modify the guest's name or cancel.
- 2c4. If the user provides unique name, the process resumes at step 3.

---

### UC5: Delete a Guest

**Preconditions:**
- A wedding has been created.
- A wedding has been set as the active wedding.

**MSS**

1. User requests to view the guest list.
2. System displays the list of guests.
3. User selects a guest to delete.
4. System removes the selected guest from the guest list.
5. System confirms that the guest has been deleted.
   Use case ends.

**Extensions:**
3a. **Invalid Guest Selection**
- 3a1. System detects that the selected guest does not exist.
- 3a2. System informs the user that the selection is invalid.
- 3a3. System prompts the user to make a valid selection.
- 3a4. If the user makes a valid selection, the process resumes at step 4.

---

### UC6: Edit a guest

**Preconditions:**
- A wedding has been created.
- A wedding has been set as the active wedding.
- The guest to be edited exists in the current wedding's guest list.

**MSS**

1. User requests to view the guest list.
2. System displays the list of guests.
3. User selects a guest to edit.
4. User provides new information for the selected guest.
5. System validates the new information.
6. System updates the guest's details.
7. System confirms that the guest's information has been updated.
   Use case ends.

**Extensions:**

3a. **Invalid Guest Selection**
- 3a1. System detects that the selected guest does not exist.
- 3a2. System informs the user that the selection is invalid.
- 3a3. System prompts the user to make a valid selection.
- 3a4. If the user makes a valid selection, the process resumes at step 4.

4a. **Invalid Information Format**
- 4a1. System detects that some new information is in an invalid format.
- 4a2. System informs the user which information is invalid and why.
- 4a3. System prompts the user to correct the invalid information.
- 4a4. If the user provides valid information, the process resumes at step 5.

4b. **Duplicate Guest**
- 4b1. System detects that the updated information would create a duplicate guest.
- 4b2. System informs the user that the changes would create a duplicate.
- 4b3. System prompts the user to modify the information to avoid duplication.
- 4b4. If the user provides unique information, the process resumes at step 5.

---

### UC7: Add a table

**Preconditions:**
- A wedding has been created.
- A wedding has been set as the active wedding.

**MSS**
1. User decides to add a new table.
2. User enters the command the table ID.
3. WeddingHero validates that the table ID is unique and that the capacity is a valid positive integer.
4. WeddingHero adds the table to the current wedding 
5. WeddingHero displays a confirmation message indicating that the table has been successfully added. 

Use case ends.

**Extensions:**

2a. **Missing Required Information**
- 2a1. System detects that required table information is missing.
- 2a2. System informs the user which information is required.
- 2a3. System prompts the user to provide the missing information.
- 2a4. If the user provides the required information, the process resumes at step 3.

2b. **Invalid Information Format**
- 2b1. System detects that some information is in an invalid format.
- 2b2. System informs the user which information is invalid and why.
- 2b3. System prompts the user to correct the invalid information.
- 2b4. If the user provides valid information, the process resumes at step 3.

2c. **Duplicate Table ID**
- 2c1. System detects that a table with the same ID already exists.
- 2c2. System informs the user that the table ID is already in use.
- 2c3. System prompts the user to provide a different table ID.
- 2b4. If the user provides a unique table ID, the process resumes at step 3.

---

### UC8: Delete a table

**Preconditions:**
- A wedding has been created.
- A wedding has been set as the active wedding in WeddingHero.
- The table to be deleted is created

**MSS**
1. User requests to delete a table.
2. User provides the table ID.
3. WeddingHero checks if the table exists.
4. WeddingHero deletes the table from the layout.
5. WeddingHero confirms that the table has been deleted.
Use case ends.

**Extensions:**

2a. **Missing Table ID**
- 2a1. If the user omits the table ID when entering the command, WeddingHero displays an error message indicating that the table ID is required.
- 2a2. WeddingHero prompts the user to re-enter the command with the correct format.
- 2a3. Once the correct input is provided, the process resumes at step 3.

2b. **Table Not Found**
- 2b1. If WeddingHero is unable to locate a table matching the provided ID, it informs the user that no matching table was found.
- 2b2. WeddingHero prompts the user to either re-enter a valid table ID.
- 2b3. If the user provides a valid table ID, the process resumes at step 3. Otherwise, use case ends.

---
### UC9: Assign a Guest to a table

**Preconditions:**
- A wedding has been created.
- The wedding has been set as the active wedding in WeddingHero.
- At least one table has been created.
- The guest to be added has already been added to the wedding

**MSS**

1. User requests to assign a guest to a table.
2. User adds the table and the guest
3. WeddingHero verifies that the guest and table exist.
4. WeddingHero assigns the guest to the specified table.
5. WeddingHero displays a confirmation message.
Use case ends.

**Extensions:**

3a. **Person Not Found**
- 3a1. If the specified guest does not exist, WeddingHero informs the user.
- 3a2. User is prompted to re-enter a valid guest name.
- 3a3. Upon valid input, the process resumes at step 3.

4a. **Table Not Found**
- 4a1. If the table ID does not match any existing table, WeddingHero informs the user.
- 4a2. User is prompted to re-enter a valid table ID.
- 4a3. Upon valid input, the process resumes at step 4.

4b. **Table Full**
- 4b1. If the table has reached its maximum capacity, WeddingHero displays an error message.
- 4b2. User is prompted to select another table or modify the table capacity.
- 4b3. If the user selects another table, the process resumes at step 4.

2a. **Invalid Input Format**
- 2a1. If the input is missing required fields or incorrectly formatted, WeddingHero displays an error message showing the correct format.
- 2a2. Upon receiving the correct input, the process resumes at step 3.

---

### UC10: Remove a Guest from a table

**Preconditions:**
- A wedding has been created.
- The wedding has been set as the active wedding in WeddingHero.
- A person has already been assigned to a table.

**MSS**
1. User decides to remove a guest from a table.
2. User enters the table and the guest to be removed
3. WeddingHero checks if the specified person exists and is assigned to the specified table.
4. WeddingHero removes the person from the table.
5. WeddingHero displays a confirmation message that the guest has been successfully removed from the table.  
   Use case ends.

**Extensions:**

3a. **Guest Not Found**
- 3a1. If the specified guest does not exist, WeddingHero informs the user.
- 3a2. User is prompted to re-enter a valid person name.
- 3a3. Upon valid input, the process resumes at step 3.

3b. **Table Not Found**
- 3b1. If the specified table does not exist, WeddingHero informs the user.
- 3b2. User is prompted to re-enter a valid table ID.
- 3b3. Upon valid input, the process resumes at step 3.

3c. **Guest Not Assigned to Table**
- 3c1. If the person is not assigned to the specified table, WeddingHero informs the user.
- 3c2. User may choose to cancel or try another table ID.

2a. **Invalid Input Format**
- 2a1. If the input is missing required fields or incorrectly formatted, WeddingHero displays an error message showing the correct format.
- 2a2. Upon receiving the correct input, the process resumes at step 3.

### Non-Functional Requirements

1. Should work on any mainstream OS as long as it has Java 17 or above installed.
2. Should be able to hold up to 1000 guests without a noticeable sluggishness in performance for typical usage.
3. A user with above-average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. The command syntax should follow a consistent pattern, so users can easily learn new commands without extensive documentation.
5. The codebase should be modular, ensuring that future features can be implemented without major refactoring.
6. The app should support the management of multiple weddings concurrently, allowing a single user to effortlessly switch between different events
7. The app must ensure data accuracy by validating inputs (e.g., checking valid phone numbers) before updating the database.

# Glossary
- **Guest:** An individual invited to attend the wedding.
- **Table:** A designated seating area at the wedding venue, typically used to group guests together.
- **Dietary Restriction:** A limitation or specific requirement regarding food consumption, often due to allergies, health conditions, or personal preferences.
- **RSVP Status:** The response provided by an invited guest indicating whether they will attend the event.
- **Wedding Planner:** A professional responsible for organizing, coordinating, and managing all aspects of a wedding event.
- **Vendor (Extension):** A business or individual that supplies services or products—such as catering, photography, or decor—for the wedding.

--------------------------------------------------------------------------------------------------------------------


## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>
**Note:** These instructions only provide a starting point for testers to work on; testers are expected to do more *exploratory* testing.
</box>

### Launch and shutdown

1. Initial launch
   1a. Download the jar file and copy it into an empty folder.
   1b. Double-click the jar file.
      Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences
   2a.Resize the window to an optimum size. Move the window to a different location. Close the window.
   2b.Re-launch the app by double-clicking the jar file.
      Expected: The most recent window size and location is retained.


### Creating a New Wedding
Creates a new wedding where Persons and Tables can be added after it has been set.

**Prerequisites:** None

**Test case:** `createWedding n/John & Jane Wedding`  
**Expected Result:** New Wedding is added to the list. A message that a new wedding has been created is shown.

**Test case:** `createWedding n/John & Jane Wedding`, then do the command`createWedding n/John & Jane Wedding` again

**Expected Result:** A message that a wedding already exists with the name is shown.

**Incorrect Test Commands:**
- `createWedding John`
- `createWedding`  
  **Expected Result:** Error details of incorrect format and right usage are shown in the status message.

---

### Set Wedding
Sets a wedding that has been created.

**Prerequisites:** Wedding John & Jane has been created

**Test case:** `setWedding n/John & Jane`  
**Expected Result:** A message would be shown to indicate that the wedding has been set, and this would be reflected in the GUI.

**Test case:** `setWedding n/Aikeen & Dueet`  
**Expected Result:** An error message would be shown to indicate there is no such wedding.

**Incorrect Test Commands:**
- `setWedding John`
- `setWedding`

---

### Wedding Overview
Shows an overview of the current wedding including the number of people invited, the number of tables, and the guest names.

**Prerequisites:** A wedding has been set

**Test case:** `weddingOverview`  
**Expected Result:** A message showing the number of people attending, the number of tables present and the guest names would be shown.

**Incorrect Test Command:** `weddingOverview n/`  
**Expected Result:** An error message showing that it is an incorrect command is shown.

---

### Delete Wedding
Deletes the Wedding and removes all instances of the People and the Tables in the wedding.

**Prerequisites:** A Wedding named John & Jane has been created

**Test case:** `deleteWedding n/John & Jane`  
**Expected Result:** The wedding of John & Jane is deleted. A message is shown that the wedding has been deleted.

**Test case:** `deleteWedding n/Aikeen & Dueet`  
**Expected Result:** No such error message is shown.

**Incorrect Test Command:** `deleteWedding`  
**Expected Result:** A message showing the correct error message is shown.

---

### Adding a Person
Allows you to add people to the currently setWedding.

**Prerequisites:** A Wedding named John & Jane has been created, and the wedding has been set

**Test case:** `addPerson n/John Doe p/12345678 e/johndoe@example.com a/123 Street d/NONE r/YES`  
**Expected Result:** The person John Doe is added to the current wedding, and is reflected in the GUI.

**Incorrect Test Command:** `addPerson n/John Doe p/12345678 e/johndoe@example.com a/123 Street d/NONE r/WHAT_HELP`  
**Expected Result:** An error message is shown.

---

### Deleting a Person
Deletes a person from the currently active wedding's person list, using their displayed index number.

**Prerequisites:** A Wedding named John & Jane has been created, and the wedding has been set. John Doe has been added, and is the first person shown in the list

**Test case:** `deletePerson 1`  
**Expected Result:** The first person on the list, eg. John Doe would be deleted.

**Incorrect Test Commands:**
- `deletePerson -8`
- `deletePerson a`  
  **Expected Result:** An error message showing the correct input format is given.

---

### Filtering Persons
Allows you to filter your list of persons by applying DIETARYRESTRICTION and/or RSVP status filters.

**Prerequisites:** You have created and set a wedding, with a person with `n/John Doe` added with the HALAL dietary restriction

**Test case:** `filterPersons r/Halal`  
**Expected Result:** You would have a filtered list of persons with the HALAL tag, including John Doe.

---

### Adding a Table
Adds a table with the specified table ID and capacity to the current wedding.

**Prerequisites:** A Wedding has been created and set

**Test case:** `addTable tid/12 c/8`  
**Expected Result:** Will add a table, and would be reflected in the GUI.

**Test case:** `addTable tid/120 c/8`  
**Expected Result:** Would show that the ID is above the set limit.

**Incorrect Test Commands:**
- `addTable 120 8`
- `addTable tid/ss c/3`  
  **Expected Result:** An error message showing that it is an incorrect error message is shown.

---

### Deleting a Table
Deletes a table by its table ID.

**Prerequisites:** A Wedding has been created and set. Only a table with table id 1 has been added.

**Test case:** `deleteTable tid/1`  
**Expected Result:** The table with tableID one has been deleted and it would be removed from the list.

**Test case:** `deleteTable tid/199`  
**Expected Result:** An error message would be shown that the table does not exist.

**Incorrect Test Commands:**
- `deleteTable 2`
- `deleteTable`
- `deleteTable 3a`

---

### Listing Tables
Lists all tables currently added to the wedding layout.

**Prerequisites:** A Wedding has been created and set. Only a table with table id 1 has been added.

**Test case:** `getTables`  
**Expected Result:** The list of current tables is shown.

**Incorrect Test Command:** `getTables a`  
**Expected Result:** Would give an error message regarding the input.

---

### Finding Tables
Finds a table by its ID.

**Test case:** `findTable tid/12`  
**Expected Result:** Displays the table with the id 12.

**Incorrect Test Commands:**
- `findTables 1`
- `findTables a`  
  **Expected Result:** Would give an error message regarding the input.

---

### Assigning a Person to a Table
Assigns a Person to a specified Table within the currently active wedding.

**Prerequisites:** A Wedding has been created and set. Only a table with table id 1 has been added. A person with the name John Doe has been added.

**Test case:** `addPersonToTable n/John Doe tid/1`  
**Expected Result:** The table 5 has a person named John Doe, and the person has the corresponding ID.

**Test case:** `addPersonToTable n/John x Doe tid/1`  
**Expected Result:** Won’t add the person to the table, as the person does not exist.

**Test case:** `addPersonToTable n/John Doe tid/3`  
**Expected Result:** Won’t add the person to the table, as the table does not exist.

**Incorrect Test Command:** `addPersonToTable John Doe t1`  
**Expected Result:** Would give an error message regarding the input.

---

### Deleting a Person from a Table
Removes a person from a table in the currently active wedding.

**Prerequisites:** A Wedding has been created and set. Only a table with table id 1 has been added. A person with the name John Doe has been added and assigned to the table.

**Test case:** `deletePersonFromTable n/John Doe tid/5`  
**Expected Result:** The person is deleted from the table.

**Test case:** `deletePersonFromTable n/John Doee tid/5`  
**Expected Result:** A message that the person does not exist would be shown.

**Test case:** `deletePersonFromTable n/John Doe tid/3`  
**Expected Result:** A message that the table does not exist would be shown.

---

### Saving data

1. Dealing with missing/corrupted data files
   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

2. _{ more test cases … }_


## **Appendix: Planned Enhancements**

Team size: 5
