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
3. The command can communicate with the `Model` when it is executed (e.g., to delete a person).<br>
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

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g., the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` object.
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

This section describes some noteworthy details on how certain features are implemented.

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

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>
**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.
</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

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

**Value proposition**: Wedding Hero is an all-in-one platform for wedding professionals. Consolidate guest lists, vendors, budgets, and relationships in one intuitive dashboard. Simplify planning, reduce stress, and craft unforgettable celebrations. Experience total control and unwavering confidence with Wedding Hero’s reliable, centralised, and streamlined solution.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a...                                                 | I want to...                                                                                 | So that I can                                                                                                  |
|----------|---------------------------------------------------------|------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|
| ***      | wedding planner                                         | make changes to my clients plans                                                               |                                                                                                                |
| ***      | wedding planner of a couple that can’t make up their mind | add and delete the guest list                                                                  | should the couple decide to make changes                                                                       |
| ***      | wedding planner of a couple with lots of guests         | indicate whether a guest is confirmed, unconfirmed, or cancelled                               | cater enough food for them                                                                                     |
| ***      | wedding planner                                         | keep track of dietary restrictions for guests                                                  | ensure the catering meets everyone’s needs                                                                     |
| ***      | wedding planner                                         | track guest RSVPs and their meal preferences                                                   | finalise catering and seating arrangements efficiently                                                         |
| ***      | wedding planner                                         | assign guests to tables                                                                        | ensure no seats are left empty within a table                                                                  |
| ***      | wedding planner of high profile clients                 | store information about guests like contact details, address                                   | send out personalised invitation cards                                                                         |
| ***      | wedding planner                                         | see a quick overview of a specific wedding                                                     | easily determine the number of guests and tables currently assigned to my client's wedding                    |
| ***      | wedding planner with multiple clients                   | view the entire guest list assigned to one of my weddings easily                               | quickly look through guests’ information                                                                       |
| ***      | experienced wedding planner                             | set the number of tables early to gauge the number of guests my client has                     | easily manage other logistics such as venue decision, food catering etc.                                      |
| ***      | wedding planner                                         | decide how many guests should be seated at one table                                           | customise it to my clients’ needs                                                                              |
| ***      | wedding planner                                         | view the entire table list quickly                                                             | quickly see the list of tables and their capacities                                                           |
| **       | organised wedding planner                               | filter guests based on their dietary restrictions and RSVP status                              | view guests based on a specific category                                                                       |
| *        | forgetful wedding planner                               | mark the status of the vendor list                                                             | keep track of whether a vendor has confirmed                                                                   |
| *        | detailed wedding planner                                | see a list of upcoming tasks that are most urgent                                              | pay attention to them first                                                                                    |
| *        | wedding planner                                         | create a library of preferred vendors and pricing details                                      | easily recommend the best options to my clients                                                                |
| *        | wedding planner                                         | send scheduled messages to the guests, vendors, and photographers                              | they can be reminded of the upcoming schedule and deadlines                                                    |
| *        | wedding planner                                         | get a contact list of wedding-related vendors nearby                                           | I don’t have to worry about looking them up personally                                                         |
| *        | wedding planner                                         | track expenses against a set budget                                                            | I stay informed of the wedding costs                                                                           |
| *        | wedding planner                                         | share a to-do list with my clients                                                             | we can stay on the same page about what needs to be done                                                       |

## Use cases

(For all use cases below, the **System** is the `WeddingHero` and the **Actor** is the `user`, unless specified otherwise)

### Use case: Create a wedding

**MSS**

1. User wants to create a new wedding.
2. User enters the wedding name using the command, e.g., `createWedding John & Jane Wedding`.
3. WeddingHero validates the input details.
4. WeddingHero creates a new wedding entry in the system.
5. WeddingHero confirms the successful creation of the wedding.
   Use case ends.

---

### Use case: Set a wedding

**Preconditions:**
- At least one wedding has been created in WeddingHero.

**MSS**

1. User wants to set a wedding as active.
2. User enters the command `setWedding Jack and Jill's wedding` with the desired wedding's name.
3. WeddingHero verifies that the specified wedding exists.
4. WeddingHero sets the wedding as the active wedding.
5. WeddingHero displays a confirmation message that the wedding is now active.
   Use case ends.

**Extensions:**

2a. **Missing Wedding Name**
   2a1. If the user issues the command without providing a wedding name, WeddingHero displays an error message indicating that the wedding name is required.
   2a2. WeddingHero prompts the user to re-enter the command with the correct format.
   2a3. Upon receiving the correct input, the process resumes at step 3.

2b. **Non-Existent Wedding**
   2b1. If the specified wedding name does not match any existing wedding, WeddingHero informs the user that no such wedding was found.
   2b2. WeddingHero prompts the user to either re-enter a valid wedding name or cancel the operation.
   2b3. If the user provides a valid wedding name, the process resumes at step 3; if the user cancels, the use case ends.

2c. **Invalid Input Format**
   2c1. If the user enters invalid text or an unsupported format for the wedding name, WeddingHero displays an error message indicating the input is invalid.
   2c2. WeddingHero prompts the user to re-enter the command in the correct format.
   2c3. Once valid input is provided, the process resumes at step 3.

---

### Use case: Wedding Overview

**Preconditions:**
- A wedding has been created.
- A wedding has been set as the active wedding in WeddingHero.

**MSS**

1. User wants to view the wedding overview.
2. WeddingHero retrieves the overview details of the current active wedding, including the number of tables, guests
   and list of guests.
3. WeddingHero displays the wedding overview information.
4. Use case ends.

**Extensions:**

2a. **No Active Wedding Set**
    2a1. If no active wedding is set, WeddingHero informs the user that an active wedding must be set before viewing an overview.
    2a2. WeddingHero prompts the user to set a wedding.
    2a3. Once an active wedding is set, the user may reissue the wedding overview command, and the process resumes at
    step 2.

---

### Use case: Add a guest

**Preconditions:**
- A wedding has been created.
- A wedding has been set as the active wedding in WeddingHero.

**MSS**

1. User decides to add a new guest.
2. User to enters the guest details in the following correct format:
   `addGuest n/john Doe p/81231234 e/JohnDoe@gmail.com a/123 Kent Ridge d/None r/YES`
3. WeddingHero validates the entered details.
4. WeddingHero adds the guest to the current wedding's guest list.
5. WeddingHero displays a confirmation message that the guest has been successfully added.
   Use case ends.

**Extensions:**

2a. **Missing Required Tag**
    2a1. If the user omits one or more required identifier tags (e.g. `n/`, `p/`, etc.).
    2a2. WeddingHero displays an error message displaying the correct format, along with an example.
    2a3. Upon receiving the correct input, the process resumes at step 3.

2b. **Invalid Phone number**
    2b1. If the user enters an invalid phone number containing non numbers (e.g. 8124123A).
    2b2. WeddingHero displays an error message, informing user that numbers are only allowed for phone number.
    2b3. Once valid input is provided, the process resumes at step 3.

2b. **Invalid Email address**
    2c1. If the user enters an invalid email address (e.g. joe@).
    2c2. WeddingHero displays an error message, informing user the requirements for a valid email address.
    2c3. Once valid input is provided, the process resumes at step 3.

3c. **Duplicate Person**
    3c1. If WeddingHero detects that a guest with the same identifier (e.g. same name) already exists,
    it notifies the user of the duplicate.
    3c2. WeddingHero prompts the user that guest has already been added.
    Use case ends.

---

### Use case: Delete a guest

**Preconditions:**
- A wedding has been created.
- A wedding has been set as the active wedding in WeddingHero.

**MSS**

1. User requests to view the current guest list.
2. WeddingHero displays the list of guests.
3. User selects a guest to delete by providing an identifier (guest name), e.g., `deleteGuest n/John Doe`.
4. WeddingHero searches for a guest matching the provided name or phone number.
5. WeddingHero deletes the guest.
   Use case ends.

**Extensions:**

3a. **Alternative Identifier: Phone Number**
    3a1. Instead of using the guest name, the user issues the delete command with a phone number identifier (e.g., `deleteGuest p/81231234`).
    3a2. WeddingHero searches for a guest matching the provided phone number.
    3a3. WeddingHero then proceeds to step 5 of the MSS.

3b. **Missing Identifier Tag**
    3b1. The user issues the delete command without using the required identifier tag (e.g., `deleteGuest John Doe` without `n/` or `p/`).
    3b2. WeddingHero detects the missing tag and displays an error message instructing the user to use the correct identifier format.
    3b3. WeddingHero prompts the user to re-enter the command with either of the proper tags.
    3b4. If the user re-enters the command with a valid identifier, the process resumes at step 4.

3c. **Invalid Input Text**
    3c1. The user enters invalid text that does not conform to the expected format (e.g., contains non-alphanumeric characters).
    3c2. WeddingHero identifies the invalid input and displays an error message indicating that the input is invalid.
    3c3. WeddingHero prompts the user to re-enter a valid identifier.
    3c4. If the user provides a valid input, the process resumes at step 4.

5a. **No Matching Person Found**
    5a1. WeddingHero is unable to find any guest matching the provided identifier (name or phone number).
    5a2. WeddingHero informs the user that no matching guest was found.
    5a3. If the user re-enters a valid tag with a different value as an identifier, the process resumes at step 4.
    5a4. If the user changes commands, the use case ends.

---

### Use case: Add a table

**Preconditions:**
- A wedding has been created.
- A wedding has been set as the active wedding in WeddingHero.

**MSS**

1. User decides to add a new table.
2. User enters the command `addTable tid/4 8` with the desired table's ID and capacity.
3. WeddingHero validates that the table ID is unique and that the capacity is a valid positive integer.
4. WeddingHero adds the table to the current wedding selected by `setWedding`.
5. WeddingHero displays a confirmation message indicating that the table has been successfully added.
6. Use case ends.

**Extensions:**

2a. **Missing Required Details**
   2a1. If the user omits the table ID or capacity, WeddingHero displays an error message indicating that both parameters are required.
   2a2. WeddingHero prompts the user to re-enter the command with the correct format.
   2a3. Once the correct input is provided, the process resumes at step 3.

2b. **Invalid Capacity**
   2b1. If the user enters an invalid capacity (e.g. a non-numeric value or a negative number), WeddingHero displays an error message specifying that the capacity must be a positive integer.
   2b2. WeddingHero prompts the user to re-enter the command with a valid capacity (e.g. positive numeric number).
   2b3. Once a valid capacity is provided, the process resumes at step 3.

2c. **Duplicate Table ID**
   2c1. If a table with the provided table ID already exists in the current wedding layout, WeddingHero notifies the user of the duplicate.
   2c2. WeddingHero prompts the user to enter a new unique table ID.
   2c3. If a new, unique table ID is provided, the process resumes at step 3; if the user cancels, the use case ends.

---

### Use case: Delete a table

**Preconditions:**
- A wedding has been created.
- A wedding has been set as the active wedding in WeddingHero.

**MSS**

1. User decides to delete an existing table.
2. User enters the command `deleteTable tid/3` with the desired table's ID.
3. WeddingHero validates that the table ID is provided.
4. WeddingHero searches for the table matching the provided ID.
5. If a matching table is found, WeddingHero prompts the user to confirm the deletion.
6. User confirms the deletion.
7. WeddingHero deletes the table from the wedding layout.
8. WeddingHero displays a confirmation message that the table has been successfully deleted.
   Use case ends.

**Extensions:**

2a. **Missing Table ID**
   2a1. If the user omits the table ID when entering the command, WeddingHero displays an error message indicating that the table ID is required.
   2a2. WeddingHero prompts the user to re-enter the command with the correct format.
   2a3. Once the correct input is provided, the process resumes at step 3.

2b. **Table Not Found**
   2b1. If WeddingHero is unable to locate a table matching the provided ID, it informs the user that no matching table was found.
   2b2. WeddingHero prompts the user to either re-enter a valid table ID.
   2b3. If the user provides a valid table ID, the process resumes at step 3. Otherwise, use case ends.

---

*{More to be added}*

### Non-Functional Requirements

1. Should work on any mainstream OS as long as it has Java 17 or above installed.
2. Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3. A user with above-average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. The command syntax should follow a consistent pattern, so users can easily learn new commands without extensive documentation.
5. The codebase should be modular, ensuring that future features can be implemented without major refactoring.
6. The app should support the management of multiple weddings concurrently, allowing a single user to effortlessly switch between different events
7. The app must ensure data accuracy by validating inputs (e.g., checking valid phone numbers) before updating the database.

# Glossary

- **Person:** An individual invited to attend the wedding.
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
   1. Download the jar file and copy it into an empty folder.
   2. Double-click the jar file.
      Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences
   1. Resize the window to an optimum size. Move the window to a different location. Close the window.
   2. Re-launch the app by double-clicking the jar file.
      Expected: The most recent window size and location is retained.

3. _{ more test cases … }_

### Saving data

1. Dealing with missing/corrupted data files
   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

2. _{ more test cases … }_
