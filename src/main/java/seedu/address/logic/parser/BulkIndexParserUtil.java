package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.parser.exceptions.ParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Utility class for parsing bulk index inputs (e.g. "1 3 5-7").
 */
public class BulkIndexParserUtil {

    // Only allows: "1", "3-5"
    private static final Pattern VALID_TOKEN = Pattern.compile("^\\d+(-\\d+)?$");

    private static final String MESSAGE_EMPTY_INPUT = "No indices provided.";
    private static final String MESSAGE_INVALID_TOKEN = "Invalid input. Only numbers and ranges like 1 or 3-5 are allowed.";
    private static final String MESSAGE_INVALID_INDEX = "Invalid index. Index must be a non-zero positive number (1, 2, 3...).";
    private static final String MESSAGE_INVALID_RANGE = "Invalid range. Start index must be less than or equal to end index.";
    private static final String MESSAGE_RANGE_TOO_LARGE = "Range too large. Please specify a smaller range.";

    /**
     * Parses a string of indices and ranges into a list of {@code Index}.
     *
     * @param args user input arguments
     * @return list of unique, sorted {@code Index}
     * @throws ParseException if input is invalid
     */
    public static List<Index> parseBulkIndexes(String args) throws ParseException {
        if (args == null || args.trim().isEmpty()) {
            throw new ParseException(MESSAGE_EMPTY_INPUT);
        }

        String[] tokens = args.trim().split("\\s+");
        Set<Integer> indexSet = new HashSet<>();

        for (String token : tokens) {

            // Detect real negative numbers like "-1"
            if (token.matches("-\\d+")) {
                throw new ParseException(MESSAGE_INVALID_INDEX);
            }

            // Validate token format
            if (!VALID_TOKEN.matcher(token).matches()) {
                throw new ParseException(MESSAGE_INVALID_TOKEN);
            }

            if (token.contains("-")) {
                String[] parts = token.split("-");
                int start = Integer.parseInt(parts[0]);
                int end = Integer.parseInt(parts[1]);

                if (start <= 0 || end <= 0) {
                    throw new ParseException(MESSAGE_INVALID_INDEX);
                }

                if (start > end) {
                    throw new ParseException(MESSAGE_INVALID_RANGE);
                }

                // Prevent extremely large ranges (performance protection)
                if (end - start > 100) {
                    throw new ParseException(MESSAGE_RANGE_TOO_LARGE);
                }

                for (int i = start; i <= end; i++) {
                    indexSet.add(i);
                }

            } else {
                // Single index case
                int value = Integer.parseInt(token);

                if (value <= 0) {
                    throw new ParseException(MESSAGE_INVALID_INDEX);
                }
                indexSet.add(value);
            }
        }

        // Sort indices in ascending order
        List<Integer> sorted = new ArrayList<>(indexSet);
        Collections.sort(sorted);

        // Convert to Index objects
        List<Index> result = new ArrayList<>();
        for (int i : sorted) {
            result.add(Index.fromOneBased(i));
        }

        return result;
    }
}