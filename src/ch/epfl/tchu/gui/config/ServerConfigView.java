package ch.epfl.tchu.gui.config;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import ch.epfl.tchu.game.PlayerId;

/**
 * View component for server configuration.
 * Displays a form to configure server parameters before starting the game.
 *
 * @author Jeremy Chaverot (315858)
 */
public final class ServerConfigView {

    private final VBox root;
    private final TextField gamePortField;
    private final TextField chatPortField;
    private final Spinner<Integer> playersSpinner;
    private final TextField serverNameField;
    private final Button startButton;

    /**
     * Creates a new server configuration view with default values.
     */
    public ServerConfigView() {
        root = new VBox(25);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Title
        Label titleLabel = createTitle("Configuration du Serveur");

        // Form
        GridPane form = createForm();

        // Game port field
        gamePortField = new TextField("5108");
        gamePortField.setPromptText("Port du serveur de jeu");
        Label gamePortLabel = createLabel("Port de jeu:");
        form.add(gamePortLabel, 0, 0);
        form.add(gamePortField, 1, 0);

        // Chat port field
        chatPortField = new TextField("5109");
        chatPortField.setPromptText("Port du serveur de chat");
        Label chatPortLabel = createLabel("Port de chat:");
        form.add(chatPortLabel, 0, 1);
        form.add(chatPortField, 1, 1);

        // Number of players spinner
        playersSpinner = new Spinner<>(2, PlayerId.COUNT, 2);
        playersSpinner.setEditable(true);
        Label playersLabel = createLabel("Nombre de joueurs:");
        form.add(playersLabel, 0, 2);
        form.add(playersSpinner, 1, 2);

        // Server player name field
        serverNameField = new TextField("Ada");
        serverNameField.setPromptText("Nom du joueur hôte");
        Label serverNameLabel = createLabel("Votre nom:");
        form.add(serverNameLabel, 0, 3);
        form.add(serverNameField, 1, 3);

        // Apply styles
        StyleUtils.styleTextField(gamePortField, "#3498db");
        StyleUtils.styleTextField(chatPortField, "#3498db");
        StyleUtils.styleTextField(serverNameField, "#3498db");
        StyleUtils.styleSpinner(playersSpinner);

        // Info label
        Label infoLabel = createInfoLabel(
                "Les autres joueurs devront se connecter avec ces paramètres"
        );

        // Start button
        startButton = createStartButton();

        root.getChildren().addAll(titleLabel, form, infoLabel, startButton);
    }

    /**
     * Returns the root node of this view.
     *
     * @return the root VBox
     */
    public VBox getRoot() {
        return root;
    }

    /**
     * Sets the action to perform when the start button is clicked.
     *
     * @param action the action to perform
     */
    public void setOnStart(Runnable action) {
        startButton.setOnAction(e -> action.run());
    }

    /**
     * Returns the configured game port.
     *
     * @return the game port
     * @throws NumberFormatException if the port is not a valid number
     */
    public int getGamePort() {
        return Integer.parseInt(gamePortField.getText());
    }

    /**
     * Returns the configured chat port.
     *
     * @return the chat port
     * @throws NumberFormatException if the port is not a valid number
     */
    public int getChatPort() {
        return Integer.parseInt(chatPortField.getText());
    }

    /**
     * Returns the configured number of players.
     *
     * @return the number of players
     */
    public int getNumberOfPlayers() {
        return playersSpinner.getValue();
    }

    /**
     * Returns the server player name.
     *
     * @return the server player name (trimmed)
     */
    public String getServerPlayerName() {
        return serverNameField.getText().trim();
    }

    /**
     * Validates the form inputs.
     *
     * @return true if all inputs are valid, false otherwise
     */
    public boolean validate() {
        if (getServerPlayerName().isEmpty()) {
            DialogUtils.showError("Veuillez entrer votre nom");
            return false;
        }

        try {
            getGamePort();
            getChatPort();
        } catch (NumberFormatException e) {
            DialogUtils.showError("Les ports doivent être des nombres valides");
            return false;
        }

        return true;
    }

    private Label createTitle(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.BOLD, 28));
        label.setStyle("-fx-text-fill: #2c3e50;");
        return label;
    }

    private GridPane createForm() {
        GridPane form = new GridPane();
        form.setHgap(20);
        form.setVgap(15);
        form.setAlignment(Pos.CENTER);
        form.setMaxWidth(500);
        return form;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        return label;
    }

    private Label createInfoLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");
        label.setWrapText(true);
        label.setMaxWidth(500);
        label.setAlignment(Pos.CENTER);
        return label;
    }

    private Button createStartButton() {
        Button button = new Button("Démarrer le serveur");
        button.setFont(Font.font("System", FontWeight.BOLD, 14));
        button.setPrefWidth(250);
        button.setPrefHeight(45);
        StyleUtils.styleButton(button, "#3498db", "#2980b9");
        return button;
    }
}