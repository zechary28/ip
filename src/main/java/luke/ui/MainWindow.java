package luke.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Luke luke;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/wooper.png"));
    private Image dukeImage = new Image(this.getClass().getResourceAsStream("/images/manaphy.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Duke instance */
    public void setDuke(Luke d) {
        luke = d;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = luke.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDukeDialog(response, dukeImage)
        );
        userInput.clear();
    }

    public void handleStartUp() {
        dialogContainer.getChildren().addAll(
                DialogBox.getDukeDialog(luke.getStartUp(), dukeImage)
        );
    }

    public void handleShutDown() {
        System.out.println("HANDLING SHUTDOWN");
        dialogContainer.getChildren().addAll(
                DialogBox.getDukeDialog(luke.getShutDown(), dukeImage)
        );
    }
}
