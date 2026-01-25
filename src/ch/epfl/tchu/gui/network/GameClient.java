package ch.epfl.tchu.gui.network;

import ch.epfl.tchu.gui.GraphicalPlayerAdapter;
import ch.epfl.tchu.net.RemoteChatClient;
import ch.epfl.tchu.net.RemotePlayerClient;

/**
 * Manages the game client, handling connection to the server.
 *
 * @author Jeremy Chaverot (315858)
 */
public final class GameClient {

    private final String gameHost;
    private final int gamePort;
    private final String chatHost;
    private final int chatPort;
    private final String playerName;

    /**
     * Creates a new game client with the specified connection parameters.
     *
     * @param gameHost   the game server hostname or IP address
     * @param gamePort   the port for game communication
     * @param chatHost   the chat server hostname or IP address
     * @param chatPort   the port for chat communication
     * @param playerName the name of this player
     */
    public GameClient(String gameHost, int gamePort, String chatHost, int chatPort, String playerName) {
        this.gameHost = gameHost;
        this.gamePort = gamePort;
        this.chatHost = chatHost;
        this.chatPort = chatPort;
        this.playerName = playerName;
    }

    /**
     * Connects to the game server and starts the game client.
     * This method blocks until the game is finished or a connection error occurs.
     *
     * @throws Exception if a connection error occurs
     */
    public void connect() throws Exception {
        System.out.println("Connecting to Game Server at " + gameHost + ":" + gamePort + "...");
        System.out.println("Connecting to Chat Server at " + chatHost + ":" + chatPort + "...");

        // Create player adapter
        GraphicalPlayerAdapter playerAdapter = new GraphicalPlayerAdapter();

        // Setup chat client using the specific chat host and port
        RemoteChatClient chatClient = new RemoteChatClient(playerAdapter, chatHost, chatPort);
        playerAdapter.setChatSystem(chatClient);

        // Setup game client with the specific game host and port
        RemotePlayerClient playerClient = new RemotePlayerClient(playerAdapter, gameHost, gamePort, playerName);

        // Start chat client in separate thread
        Thread chatThread = new Thread(chatClient::run);
        chatThread.setDaemon(true); // Ensures chat thread closes when the app closes
        chatThread.start();

        // Start game client (blocks until game ends)
        System.out.println("Connected! Player name: " + playerName);
        System.out.println("Waiting for game to start...");
        playerClient.run();
    }
}