package game.actions;

import client.GameStoreClient;
import game.GameStore;
import game.player.Player;

import java.rmi.RemoteException;

/**
 * @author Wesley Klop
 */
public class AttachPlayerAction implements Action {

    private final GameStoreClient client;
    private final int playerId;

    public AttachPlayerAction(GameStoreClient client, int playerId) {
        this.client = client;
        this.playerId = playerId;
    }


    @Override
    public void executeAction(GameStore store) throws RemoteException {
        Player player = store.getPlayerController().getPlayerById(playerId);
        client.setPlayer(player);
        player.setHasClient(true);
    }

    @Override
    public int getPlayerId() {
        return playerId;
    }
}
