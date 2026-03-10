package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ListCommandParser implements Parser<ListCommand> {

    @Override
    public ListCommand parse(String args) {

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_SORT);

        String sortField = argMultimap.getValue(PREFIX_SORT)
                .map(String::trim)
                .orElse("");

        return new ListCommand(sortField);
    }
}