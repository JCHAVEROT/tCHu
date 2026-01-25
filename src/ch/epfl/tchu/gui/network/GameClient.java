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

    private final String hostName;
    private final int gamePort;
    private final int chatPort;
    private final String playerName;

    /**
     * Creates a new game client with the specified connection parameters.
     *
     * @param hostName the server hostname or IP address
     * @param gamePort the port for game communication
     * @param chatPort the port for chat communication
     * @param playerName the name of this player
     */
    public GameClient(String hostName, int gamePort, int chatPort, String playerName) {
        this.hostName = hostName;
        this.gamePort = gamePort;
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
        System.out.println("Connecting to server " + hostName + ":" + gamePort + "...");

        // Create player adapter
        GraphicalPlayerAdapter playerAdapter = new GraphicalPlayerAdapter();

        // Setup chat client
        RemoteChatClient chatClient = new RemoteChatClient(playerAdapter, hostName, chatPort);
        playerAdapter.setChatSystem(chatClient);

        // Setup game client with player name (using the new constructor)
        RemotePlayerClient playerClient = new RemotePlayerClient(playerAdapter, hostName, gamePort, playerName);

        // Start chat client in separate thread
        new Thread(() -> chatClient.run()).start();

        // Start game client (blocks until game ends)
        System.out.println("Connected! Player name: " + playerName);
        System.out.println("Waiting for game to start...");
        playerClient.run();
    }
}