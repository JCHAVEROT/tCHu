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
    private final TextField gameHostField;
    private final TextField gamePortField;
    private final TextField chatHostField;
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

        // Game Server fields
        gameHostField = new TextField("localhost");
        gameHostField.setPromptText("Hôte (ex: domaine.com)");
        gamePortField = new TextField("5108");
        gamePortField.setPrefWidth(80);

        form.add(createLabel("Serveur de Jeu :"), 0, 0);
        form.add(gameHostField, 1, 0);
        form.add(gamePortField, 2, 0);

        // Chat Server fields
        chatHostField = new TextField("localhost");
        chatHostField.setPromptText("Hôte (ex: domaine.com)");
        chatPortField = new TextField("5109");
        chatPortField.setPrefWidth(80);

        form.add(createLabel("Serveur de Chat :"), 0, 1);
        form.add(chatHostField, 1, 1);
        form.add(chatPortField, 2, 1);

        // Player name field
        playerNameField = new TextField("Charles");
        playerNameField.setPromptText("Nom du joueur");
        form.add(createLabel("Votre Nom :"), 0, 2);
        form.add(playerNameField, 1, 2, 2, 1);

        // Apply styles
        StyleUtils.styleTextField(gameHostField, "#27ae60");
        StyleUtils.styleTextField(gamePortField, "#27ae60");
        StyleUtils.styleTextField(chatHostField, "#2980b9");
        StyleUtils.styleTextField(chatPortField, "#2980b9");
        StyleUtils.styleTextField(playerNameField, "#2c3e50");

        // Info label
        Label infoLabel = createInfoLabel(
                "Maintenant, collectez vos wagons et bâtissez votre empire ferroviaire."
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
     * Returns the configured game server host name.
     *
     * @return the game server host (trimmed)
     */
    public String getGameHostName() {
        return gameHostField.getText().trim();
    }

    /**
     * Returns the configured game port.
     *
     * @return the game port
     * @throws NumberFormatException if the port is not a valid number
     */
    public int getGamePort() {
        return Integer.parseInt(gamePortField.getText().trim());
    }

    /**
     * Returns the configured chat server host name.
     *
     * @return the chat server host (trimmed)
     */
    public String getChatHostName() {
        return chatHostField.getText().trim();
    }

    /**
     * Returns the configured chat port.
     *
     * @return the chat port
     * @throws NumberFormatException if the port is not a valid number
     */
    public int getChatPort() {
        return Integer.parseInt(chatPortField.getText().trim());
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
        if (getGameHostName().isEmpty() || getChatHostName().isEmpty()) {
            DialogUtils.showError("Veuillez entrer les adresses des deux serveurs");
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
        form.setHgap(15);
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