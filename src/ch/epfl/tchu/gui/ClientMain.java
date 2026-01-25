package ch.epfl.tchu.gui;

import ch.epfl.tchu.gui.config.ClientConfigView;
import ch.epfl.tchu.gui.config.DialogUtils;
import ch.epfl.tchu.gui.network.GameClient;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for the game client.
 * Displays a configuration GUI before connecting to the server.
 *
 * @author Cristian Safta (324694)
 * @author Jeremy Chaverot (315858)
 */
public final class ClientMain extends Application {

    private static final String WINDOW_TITLE = "tCHu - Connexion au Serveur";
    private static final int WINDOW_WIDTH = 650;
    private static final int WINDOW_HEIGHT = 550;

    /**
     * Main entry point.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Prevent JavaFX from closing completely when window is hidden
        Platform.setImplicitExit(false);

        primaryStage.setTitle(WINDOW_TITLE);

        // Create configuration view
        ClientConfigView configView = new ClientConfigView();

        // Set action when connect button is clicked
        configView.setOnConnect(() -> handleConnect(primaryStage, configView));

        // Setup and show scene
        Scene scene = new Scene(configView.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Handles the connection action.
     * Validates inputs, hides the configuration window, and connects to the server.
     *
     * @param stage the primary stage to hide
     * @param view the configuration view containing user inputs
     */
    private void handleConnect(Stage stage, ClientConfigView view) {
        // Validate inputs
        if (!view.validate()) {
            return;
        }

        try {
            // Get connection parameters
            String hostName = view.getHostName();
            int gamePort = view.getGamePort();
            int chatPort = view.getChatPort();
            String playerName = view.getPlayerName();

            // Hide configuration window
            stage.hide();

            // Connect to server in separate thread
            GameClient client = new GameClient(hostName, gamePort, chatPort, playerName);
            new Thread(() -> {
                try {
                    client.connect();
                } catch (Exception e) {
                    DialogUtils.showConnectionError(e.getMessage());
                    e.printStackTrace();
                }
            }).start();

        } catch (NumberFormatException e) {
            DialogUtils.showError("Les ports doivent être des nombres valides");
        }
    }
}