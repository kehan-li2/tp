package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes one or more persons by index.\n"
            + "Parameters: INDEX [MORE INDEXES or RANGES]\n"
            + "Example: delete 1 3 6-9";

    public static final String MESSAGE_DELETE_PERSONS_SUCCESS = "Deleted persons:\n%1$s";

    private final List<Index> targetIndexes;

    public DeleteCommand(List<Index> targetIndexes) {
        this.targetIndexes = Collections.unmodifiableList(new ArrayList<>(targetIndexes));
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        // Validate ALL indices first (atomic safety)
        List<Integer> invalidIndexes = new ArrayList<>();

        for (Index index : targetIndexes) {
            if (index.getZeroBased() < 0 || index.getZeroBased() >= lastShownList.size()) {
                invalidIndexes.add(index.getOneBased());
            }
        }

        if (!invalidIndexes.isEmpty()) {
            String joined = invalidIndexes.stream()
                    .map(String::valueOf)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");

            throw new CommandException(
                    "Invalid index: " + joined + ". \nPerson does not exist in current list."
            );
        }

        // Sort descending to prevent index shifting issues
        List<Index> sortedIndexes = new ArrayList<>(targetIndexes);
        sortedIndexes.sort((a, b) -> b.getZeroBased() - a.getZeroBased());

        StringBuilder deletedPersons = new StringBuilder();

        // Perform deletion
        for (Index index : sortedIndexes) {
            Person personToDelete = lastShownList.get(index.getZeroBased());
            model.deletePerson(personToDelete);
            deletedPersons.append(Messages.format(personToDelete)).append("\n");
        }

        return new CommandResult(
                String.format(MESSAGE_DELETE_PERSONS_SUCCESS, deletedPersons)
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndexes.equals(otherDeleteCommand.targetIndexes);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndexes", targetIndexes)
                .toString();
    }
}
