package seedu.address.ui;

import java.util.logging.Logger;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.NoteCommand;
import seedu.address.logic.commands.TagCommand;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2526s2-cs2103-f11-1.github.io/tp/UserGuide.html";
    public static final String HELP_MESSAGE = "Refer to the user guide: " + USERGUIDE_URL;
    private static final String COPY_BUTTON_DEFAULT_TEXT = "Copy URL";
    private static final String COPY_BUTTON_COPIED_TEXT = "Copied!";

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    @FXML
    private ScrollPane commandScrollPane;

    @FXML
    private VBox commandContainer;

    private final PauseTransition copyFeedbackReset = new PauseTransition(Duration.seconds(1.2));

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(HELP_MESSAGE);
        copyButton.setText(COPY_BUTTON_DEFAULT_TEXT);
        copyButton.setOnAction(event -> copyUrl());
        copyFeedbackReset.setOnFinished(event -> copyButton.setText(COPY_BUTTON_DEFAULT_TEXT));
        populateCommandHelp();
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);

        copyButton.setText(COPY_BUTTON_COPIED_TEXT);
        copyFeedbackReset.stop();
        copyFeedbackReset.playFromStart();
    }

    /**
     * Populates the command help section with styled command information.
     */
    private void populateCommandHelp() {
        commandContainer.getChildren().clear();
        commandContainer.setSpacing(15);
        commandContainer.setPadding(new Insets(10));
        commandContainer.setFillWidth(true);

        addCommandSection(AddCommand.COMMAND_WORD, AddCommand.MESSAGE_USAGE);
        addCommandSection(EditCommand.COMMAND_WORD, EditCommand.MESSAGE_USAGE);
        addCommandSection(DeleteCommand.COMMAND_WORD, DeleteCommand.MESSAGE_USAGE);
        addCommandSection(ListCommand.COMMAND_WORD, ListCommand.MESSAGE_USAGE);
        addCommandSection(FindCommand.COMMAND_WORD, FindCommand.MESSAGE_USAGE);
        addCommandSection(TagCommand.COMMAND_WORD, TagCommand.MESSAGE_USAGE);
        addCommandSection(NoteCommand.COMMAND_WORD, NoteCommand.MESSAGE_USAGE);
        addCommandSection(ClearCommand.COMMAND_WORD, "Usage: " + ClearCommand.COMMAND_WORD);
        addCommandSection(HelpCommand.COMMAND_WORD, "Usage: " + HelpCommand.COMMAND_WORD);
        addCommandSection(ExitCommand.COMMAND_WORD, "Usage: " + ExitCommand.COMMAND_WORD);
    }

    /**
     * Adds a command section to the help container.
     *
     * @param commandName Name of the command
     * @param usageAndExamples Usage details and optional examples
     */
    private void addCommandSection(String commandName, String usageAndExamples) {
        Label nameLabel = createCommandNameLabel(commandName);
        ParsedHelpText parsedHelpText = parseHelpText(usageAndExamples);
        Label descriptionLabel = createDescriptionLabel(parsedHelpText.description());
        Label usageLabel = createCodeBlockLabel(parsedHelpText.usage(), "#9ad0ff");
        Label examplesLabel = createCodeBlockLabel(parsedHelpText.examples(), "#ffd479");

        VBox commandBox = createCommandBox(nameLabel, descriptionLabel, usageLabel, examplesLabel);
        commandContainer.getChildren().add(commandBox);
    }

    private Label createCommandNameLabel(String commandName) {
        Label nameLabel = new Label(commandName.toUpperCase());
        nameLabel.getStyleClass().add("help-command-name");
        return nameLabel;
    }

    private Label createDescriptionLabel(String description) {
        Label descriptionLabel = new Label(description);
        descriptionLabel.getStyleClass().add("help-description");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setManaged(!description.isEmpty());
        descriptionLabel.setVisible(!description.isEmpty());
        return descriptionLabel;
    }

    private record ParsedHelpText(String description, String usage, String examples) {}

    /**
     * Parses the usage and examples from the command's usage string.
     *
     * @param usageAndExamples The full usage string containing both usage and examples.
     * @return A ParsedHelpText record containing separated description, usage, and examples.
     */
    private ParsedHelpText parseHelpText(String usageAndExamples) {
        ParsedHelpText baseText = splitExamples(usageAndExamples);
        int parametersIndex = baseText.usage().indexOf("Parameters:");

        if (parametersIndex < 0) {
            return baseText;
        }

        String descriptionPrefix = baseText.usage().substring(0, parametersIndex).trim();
        String usageText = baseText.usage().substring(parametersIndex).trim();
        String descriptionText = extractDescription(descriptionPrefix);

        return new ParsedHelpText(descriptionText, usageText, baseText.examples());
    }

    private ParsedHelpText splitExamples(String usageAndExamples) {
        int firstExampleIndex = usageAndExamples.indexOf("\nExample:");
        if (firstExampleIndex < 0) {
            return new ParsedHelpText("", usageAndExamples.trim(), "");
        }

        String usageWithoutExamples = usageAndExamples.substring(0, firstExampleIndex).trim();
        String examplesText = usageAndExamples.substring(firstExampleIndex + 1).trim();
        return new ParsedHelpText("", usageWithoutExamples, examplesText);
    }

    private String extractDescription(String descriptionPrefix) {
        int colonIndex = descriptionPrefix.indexOf(':');
        if (colonIndex >= 0 && colonIndex + 1 < descriptionPrefix.length()) {
            descriptionPrefix = descriptionPrefix.substring(colonIndex + 1).trim();
        }
        return descriptionPrefix.replace("\n", " ").trim();
    }

    private Label createCodeBlockLabel(String text, String textColor) {
        Label codeBlockLabel = new Label(text);
        codeBlockLabel.getStyleClass().add("help-code-block");
        codeBlockLabel.setStyle("-help-code-color: " + textColor + ";");
        codeBlockLabel.setWrapText(true);
        codeBlockLabel.setMaxWidth(Double.MAX_VALUE);
        codeBlockLabel.setManaged(!text.isEmpty());
        codeBlockLabel.setVisible(!text.isEmpty());
        return codeBlockLabel;
    }

    private VBox createCommandBox(Label nameLabel, Label descriptionLabel, Label usageLabel, Label examplesLabel) {
        VBox commandBox = new VBox(5);
        commandBox.getStyleClass().add("help-command-box");
        commandBox.setMaxWidth(Double.MAX_VALUE);
        commandBox.getChildren().addAll(nameLabel, descriptionLabel, usageLabel, examplesLabel);
        return commandBox;
    }
}
