package ch.epfl.tchu.gui.config;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * View component for client configuration.
 * Displays a form to configure connection parameters before joining the game.
 *
 * @author Jeremy Chaverot (315858)
 */
public final class ClientConfigView {

    private final VBox root;
    private final TextField ipField;
    private final TextField gamePortField;
    private final TextField chatPortField;
    private final TextField playerNameField;
    private final Button connectButton;

    /**
     * Creates a new client configuration view with default values.
     */
    public ClientConfigView() {
        root = new VBox(25);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Title
        Label titleLabel = createTitle("Rejoindre une Partie");

        // Form
        GridPane form = createForm();

        // IP address field
        ipField = new TextField("localhost");
        ipField.setPromptText("Adresse IP du serveur");
        Label ipLabel = createLabel("Adresse IP:");
        form.add(ipLabel, 0, 0);
        form.add(ipField, 1, 0);

        // Game port field
        gamePortField = new TextField("5108");
        gamePortField.setPromptText("Port du serveur de jeu");
        Label gamePortLabel = createLabel("Port de jeu:");
        form.add(gamePortLabel, 0, 1);
        form.add(gamePortField, 1, 1);

        // Chat port field
        chatPortField = new TextField("5109");
        chatPortField.setPromptText("Port du serveur de chat");
        Label chatPortLabel = createLabel("Port de chat:");
        form.add(chatPortLabel, 0, 2);
        form.add(chatPortField, 1, 2);

        // Player name field
        playerNameField = new TextField("Charles");
        playerNameField.setPromptText("Nom du joueur");
        Label playerNameLabel = createLabel("Votre nom:");
        form.add(playerNameLabel, 0, 3);
        form.add(playerNameField, 1, 3);

        // Apply styles
        StyleUtils.styleTextField(ipField, "#27ae60");
        StyleUtils.styleTextField(gamePortField, "#27ae60");
        StyleUtils.styleTextField(chatPortField, "#27ae60");
        StyleUtils.styleTextField(playerNameField, "#27ae60");

        // Info label
        Label infoLabel = createInfoLabel(
                "Assurez-vous que le serveur est démarré avant de vous connecter"
        );

        // Connect button
        connectButton = createConnectButton();

        root.getChildren().addAll(titleLabel, form, infoLabel, connectButton);
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
     * Sets the action to perform when the connect button is clicked.
     *
     * @param action the action to perform
     */
    public void setOnConnect(Runnable action) {
        connectButton.setOnAction(e -> action.run());
    }

    /**
     * Returns the configured server IP address.
     *
     * @return the server IP address (trimmed)
     */
    public String getHostName() {
        return ipField.getText().trim();
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
     * Returns the player name.
     *
     * @return the player name (trimmed)
     */
    public String getPlayerName() {
        return playerNameField.getText().trim();
    }

    /**
     * Validates the form inputs.
     *
     * @return true if all inputs are valid, false otherwise
     */
    public boolean validate() {
        if (getHostName().isEmpty()) {
            DialogUtils.showError("Veuillez entrer l'adresse IP du serveur");
            return false;
        }

        if (getPlayerName().isEmpty()) {
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

    private Button createConnectButton() {
        Button button = new Button("Se connecter");
        button.setFont(Font.font("System", FontWeight.BOLD, 14));
        button.setPrefWidth(250);
        button.setPrefHeight(45);
        StyleUtils.styleButton(button, "#27ae60", "#229954");
        return button;
    }
}