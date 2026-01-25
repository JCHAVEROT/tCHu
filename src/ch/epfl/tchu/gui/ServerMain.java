package ch.epfl.tchu.gui;

import ch.epfl.tchu.gui.config.DialogUtils;
import ch.epfl.tchu.gui.config.ServerConfigView;
import ch.epfl.tchu.gui.network.GameServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for the game server.
 * Displays a configuration GUI before starting the server.
 *
 * @author Cristian Safta (324694)
 * @author Jeremy Chaverot (315858)
 */
public final class ServerMain extends Application {

    private static final String WINDOW_TITLE = "tCHu - Configuration Serveur";
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
        ServerConfigView configView = new ServerConfigView();

        // Set action when start button is clicked
        configView.setOnStart(() -> handleStartServer(primaryStage, configView));

        // Setup and show scene
        Scene scene = new Scene(configView.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Handles the server start action.
     * Validates inputs, hides the configuration window, and starts the server.
     *
     * @param stage the primary stage to hide
     * @param view the configuration view containing user inputs
     */
    private void handleStartServer(Stage stage, ServerConfigView view) {
        // Validate inputs
        if (!view.validate()) {
            return;
        }

        try {
            // Get configuration parameters
            int gamePort = view.getGamePort();
            int chatPort = view.getChatPort();
            int numberOfPlayers = view.getNumberOfPlayers();
            String serverPlayerName = view.getServerPlayerName();

            // Hide configuration window
            stage.hide();

            // Start server in separate thread
            GameServer server = new GameServer(gamePort, chatPort, numberOfPlayers, serverPlayerName);
            new Thread(() -> server.start()).start();

        } catch (NumberFormatException e) {
            DialogUtils.showError("Les ports doivent être des nombres valides");
        }
    }
}