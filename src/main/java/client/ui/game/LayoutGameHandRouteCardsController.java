package client.ui.game;

import client.ui.components.PlayerRouteCard;
import game.GameStore;
import game.GameStoreProvider;
import game.player.Player;
import game.routecards.RouteCard;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import util.Observable;
import util.Observer;

public class LayoutGameHandRouteCardsController implements Observer<GameStore> {

    private Observable<GameStore> storeObservable = GameStoreProvider.getInstance();
    @FXML
    private HBox routeCardContainer;
    private EventHandler<MouseEvent> enterHandler;
    private EventHandler<MouseEvent> exitHandler;

    public void initialize() {
        storeObservable.addObserver(this);
    }

    private void emptyRouteContainer() {
        routeCardContainer.getChildren().remove(0, routeCardContainer.getChildren().size());
    }

    @Override
    public void onUpdate(GameStore store) {
        Platform.runLater(() -> {
            emptyRouteContainer();
            Player player = GameStoreProvider.getPlayer();
            for (RouteCard routeCard : player.getRouteCards()) {
                routeCardContainer.getChildren().add(new PlayerRouteCard(routeCard, enterHandler, exitHandler));
            }
        });
    }

    public HBox getContainer() {
        return routeCardContainer;
    }

    public void setOnRouteCardHoverEnter(EventHandler<MouseEvent> handler) {
        this.enterHandler = handler;
    }

    public void setOnRouteCardHoverExit(EventHandler<MouseEvent> handler) {
        this.exitHandler = handler;
    }
}
