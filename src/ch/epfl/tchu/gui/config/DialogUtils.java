package ch.epfl.tchu.gui.config;

import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Utility class for displaying dialog boxes.
 *
 * @author Jeremy Chaverot (315858)
 */
public final class DialogUtils {

    // Private constructor to prevent instantiation
    private DialogUtils() {}

    /**
     * Displays an error dialog with the specified message.
     *
     * @param message the error message to display
     */
    public static void showError(String message) {
        showAlert(Alert.AlertType.ERROR, "Erreur", message);
    }

    /**
     * Displays a connection error dialog with detailed error information.
     *
     * @param errorMessage the detailed error message
     */
    public static void showConnectionError(String errorMessage) {
        String fullMessage = "Vérifiez que le serveur est bien démarré et que les paramètres sont corrects.\n\n" +
                "Erreur: " + errorMessage;
        Platform.runLater(() ->
                showAlert(Alert.AlertType.ERROR, "Erreur de connexion",
                        "Impossible de se connecter au serveur", fullMessage)
        );
    }

    /**
     * Displays an alert dialog with the specified parameters.
     *
     * @param type the type of alert
     * @param title the title of the alert
     * @param message the message to display
     */
    private static void showAlert(Alert.AlertType type, String title, String message) {
        showAlert(type, title, null, message);
    }

    /**
     * Displays an alert dialog with the specified parameters including header.
     *
     * @param type the type of alert
     * @param title the title of the alert
     * @param header the header text (can be null)
     * @param content the content text
     */
    private static void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}