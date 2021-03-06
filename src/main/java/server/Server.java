package server;

import client.GameStoreClient;
import game.GameState;
import game.GameStore;
import game.actions.Action;
import game.player.PlayerController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class Server extends UnicastRemoteObject implements GameStoreServer {
    public static final String REGISTRY_NAME = "TTRGameService";
    private static final Logger Log = LogManager.getLogger(Server.class);
    private static final int PORT = 1099;

    private List<GameStoreClient> clients = new ArrayList<>();
    private GameStore gameStore;

    /**
     * Starts an RMI service with the givens GameStore
     * @param store GameStore to use
     * @throws RemoteException RMI exception
     * @throws MalformedURLException registry name uses invalid chars
     * @throws UnknownHostException when localhost has no address
     */
    public Server(GameStore store) throws RemoteException, MalformedURLException, UnknownHostException {
        Log.debug("Starting server");

        String ip = InetAddress.getLocalHost().getHostAddress();
        gameStore = store == null ? new GameStore() : store;

        gameStore.setServerIp(ip);

        LocateRegistry.createRegistry(PORT);
        Naming.rebind(REGISTRY_NAME, this);
        Log.info("Server started at: " + ip);

        gameStore.getCardStackController().populateOpenCards();
        gameStore.getSelectableRouteCards().populatePickableCards();
        Log.debug("Initial open/pickable cards are set");
    }

    @Override
    public synchronized void registerObserver(GameStoreClient listener) throws RemoteException {
        clients.add(listener);
        listener.onConnect(gameStore);
    }

    @Override
    public synchronized void unregisterObserver(GameStoreClient listener) {
        clients.remove(listener);
    }

    /**
     * Notifies all clients with a new GameStore
     */
    @Override
    public synchronized void notifyListeners() {
        for (GameStoreClient client : clients) {
            try {
                client.onGameStoreReceived(gameStore);
            } catch (RemoteException e) {
                Log.error("Failed to update client.. removing him from listeners");
                unregisterObserver(client);
            }
        }
    }

    /**
     * Start a standalone server
     * @param args *unused* arguments
     */
    public static void main(String[] args) {
        try {
            new Server(null);
        } catch (RemoteException | MalformedURLException | UnknownHostException e) {
            Log.catching(e);
        }
    }

    /**
     * Execute side effects when an action was caused by a player
     * @param action the action to do side effects for
     */
    private void doSideEffects(Action action) {
        // Only do side effects for actions that are caused by a "player"
        if (action.getPlayerId() == -1) {
            return;
        }

        PlayerController playerController = gameStore.getPlayerController();

        if (playerController.shouldGoToLastTurn(action.getPlayerId())) {
            // When the player that did his turn has 2 ore less trainscarts we should set the last turn param
            Log.info("Player has 2 or less carts left.. Going to final round..");
            playerController.setLastTurn(action.getPlayerId());
        } else if (playerController.getFinalTurn() == action.getPlayerId()) {
            // Else if the action player id is the same as the id of the last turn player
            // We should go to finish.
            Log.info("Going to finished state!");
            gameStore.setGameState(GameState.FINISHED);
        }
    }

    /**
     * Executes the given action, does side effects and notifies the clients of the change
     * @param action the action to execute
     */
    @Override
    public synchronized void onActionReceived(Action action) {
        try {
            action.executeAction(gameStore);

            doSideEffects(action);

            Log.info("Executed action {}", action.getClass().getSimpleName());
            notifyListeners();
            Log.debug("Notified listeners");
        } catch (Exception ex) {
            Log.error("Server error while executing action", ex);
        }
    }
}
