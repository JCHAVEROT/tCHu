package ch.epfl.tchu.gui.network;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.GraphicalPlayerAdapter;
import ch.epfl.tchu.net.RemoteChatProxy;
import ch.epfl.tchu.net.RemotePlayerProxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Manages the game server, handling player connections and game initialization.
 *
 * @author Jeremy Chaverot (315858)
 */
public final class GameServer {

    private static final List<String> DEFAULT_NAMES =
            List.of("Ada", "Charles", "Alice", "Bob", "Emma", "Romain", "Joseph", "Camille");

    private final int gamePort;
    private final int chatPort;
    private final int numberOfPlayers;
    private final String serverPlayerName;

    /**
     * Creates a new game server with the specified configuration.
     *
     * @param gamePort the port for game communication
     * @param chatPort the port for chat communication
     * @param numberOfPlayers the total number of players
     * @param serverPlayerName the name of the server player
     */
    public GameServer(int gamePort, int chatPort, int numberOfPlayers, String serverPlayerName) {
        this.gamePort = gamePort;
        this.chatPort = chatPort;
        this.numberOfPlayers = numberOfPlayers;
        this.serverPlayerName = serverPlayerName;
    }

    /**
     * Starts the game server and waits for all players to connect.
     * Once all players are connected, starts the game.
     * This method blocks until the game is finished.
     */
    public void start() {
        try (ServerSocket socketGame = new ServerSocket(gamePort);
             ServerSocket socketChat = new ServerSocket(chatPort)) {

            System.out.println("Server started. Waiting for " + (numberOfPlayers - 1) + " player(s)...");

            // Accept remote player connections
            List<PlayerConnection> connections = acceptPlayerConnections(socketGame);

            // Setup player names (using received names from clients)
            Map<PlayerId, String> playerNames = setupPlayerNames(connections);

            // Setup players (local + remote)
            Map<PlayerId, Player> players = setupPlayers(connections);

            // Get the local player adapter
            GraphicalPlayerAdapter localPlayer = (GraphicalPlayerAdapter) players.get(PlayerId.PLAYER_1);

            // Setup chat system
            RemoteChatProxy chatProxy = setupChatSystem(localPlayer, socketChat);

            // Start the game
            System.out.println("All players connected. Starting game...");
            Game.play(players, playerNames, SortedBag.of(UsaMap.tickets()), new Random());

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Accepts connections from remote players and receives their names.
     *
     * @param serverSocket the server socket for game connections
     * @return list of remote player proxies with their names
     * @throws IOException if a connection error occurs
     */
    private List<PlayerConnection> acceptPlayerConnections(ServerSocket serverSocket) throws IOException {
        List<PlayerConnection> connections = new ArrayList<>();

        for (int i = 0; i < numberOfPlayers - 1; i++) {
            System.out.println("Waiting for player " + (i + 2) + "...");
            Socket socket = serverSocket.accept();

            // Receive player name from client FIRST
            String playerName = receivePlayerName(socket);

            // Then create the proxy with the same socket
            RemotePlayerProxy proxy = new RemotePlayerProxy(socket);

            connections.add(new PlayerConnection(proxy, playerName));
            System.out.println("Player " + (i + 2) + " (" + playerName + ") connected!");
        }

        return connections;
    }

    /**
     * Receives the player name from a client socket.
     *
     * @param socket the client socket
     * @return the player name
     * @throws IOException if an I/O error occurs
     */
    private String receivePlayerName(Socket socket) throws IOException {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String name = in.readLine();
            System.out.println("Received player name: " + name);
            return (name != null && !name.trim().isEmpty()) ? name.trim() : DEFAULT_NAMES.get(1);
        } catch (IOException e) {
            System.err.println("Error receiving player name: " + e.getMessage());
            return DEFAULT_NAMES.get(1);
        }
    }

    /**
     * Helper class to store a player connection with its name.
     */
    private static class PlayerConnection {
        final RemotePlayerProxy proxy;
        final String name;

        PlayerConnection(RemotePlayerProxy proxy, String name) {
            this.proxy = proxy;
            this.name = name;
        }
    }

    /**
     * Sets up player names mapping using received client names.
     *
     * @param connections the player connections with their names
     * @return map of player IDs to names
     */
    private Map<PlayerId, String> setupPlayerNames(List<PlayerConnection> connections) {
        Map<PlayerId, String> playerNames = new HashMap<>();
        playerNames.put(PlayerId.PLAYER_1, serverPlayerName);

        for (int i = 0; i < connections.size(); i++) {
            playerNames.put(PlayerId.ALL.get(i + 1), connections.get(i).name);
        }

        return playerNames;
    }

    /**
     * Sets up the players map with local and remote players.
     *
     * @param connections the player connections
     * @return map of player IDs to player instances
     */
    private Map<PlayerId, Player> setupPlayers(List<PlayerConnection> connections) {
        Map<PlayerId, Player> players = new HashMap<>();

        // Local player (server)
        GraphicalPlayerAdapter localPlayer = new GraphicalPlayerAdapter();
        players.put(PlayerId.PLAYER_1, localPlayer);

        // Remote players
        for (int i = 0; i < connections.size(); i++) {
            players.put(PlayerId.ALL.get(i + 1), connections.get(i).proxy);
        }

        return players;
    }

    /**
     * Sets up the chat system for the game.
     *
     * @param localPlayer the local player adapter
     * @param chatSocket the server socket for chat connections
     * @return the chat proxy
     */
    private RemoteChatProxy setupChatSystem(GraphicalPlayerAdapter localPlayer,
                                            ServerSocket chatSocket) {
        RemoteChatProxy chatProxy = new RemoteChatProxy(localPlayer);
        localPlayer.setChatSystem(chatProxy);

        // Start chat server thread
        new Thread(() -> runChatServer(chatProxy, chatSocket)).start();

        return chatProxy;
    }

    /**
     * Runs the chat server, accepting incoming chat connections.
     *
     * @param chatProxy the chat proxy
     * @param chatSocket the server socket for chat
     */
    private void runChatServer(RemoteChatProxy chatProxy, ServerSocket chatSocket) {
        while (true) {
            try {
                Socket socket = chatSocket.accept();
                new Thread(() -> {
                    chatProxy.addClient(socket);
                    chatProxy.startReading(socket);
                }).start();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}