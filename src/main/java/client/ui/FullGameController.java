package client.ui;

import client.UserPreferences;
import client.ui.controllers.LayoutCardController;
import game.GameStoreProvider;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.util.Duration;
import util.Observer;

import static client.UserPreferences.isColorBlind;

public class FullGameController implements Observer<UserPreferences.PreferencesContainer> {

    private Image image = new Image("/images/points.png");
    private ImageView iv1 = new ImageView();
    @FXML
    private Pane routesMap, rootPane, pauseMenu;
    @FXML
    private pauseMenuController pauseMenuController;
    @FXML
    private MessagesController messagesController;
    @FXML
    private LayoutBankController bankController;
    @FXML
    private LayoutCardController handController;
    @FXML
    private GameRoutesMapController routesMapController;
    @FXML
    private Pane initRouteCards;
    private boolean colorBlind = isColorBlind();


    public void initialize() {
        UserPreferences.addObserver(this);
        MessagesControllerProvider.setMessageController(messagesController);
        pauseMenuController.resumeLabel.setOnMouseClicked(e -> closePauseMenu());
        var screenInfo = Screen.getPrimary().getVisualBounds();
        iv1.setImage(image);
        iv1.setLayoutX((screenInfo.getWidth() / 2) - (image.getWidth() / 2));
        iv1.setLayoutY((screenInfo.getHeight() / 2) - (image.getHeight() / 2));
    }

    @FXML
    private void openPauseMenu() {
        TranslateTransition transAni = new TranslateTransition(Duration.millis(500), pauseMenu);
        transAni.setToX(-1920);
        transAni.play();
    }

    @FXML
    private void closePauseMenu() {
        TranslateTransition transAni = new TranslateTransition(Duration.millis(500), pauseMenu);
        transAni.setToX(0);
        transAni.play();
    }


    public void ScoreEntered() {
        iv1.setFitHeight(170);
        iv1.setFitWidth(300);
        rootPane.getChildren().addAll(iv1);
    }


    public void ScoreExited() {
        rootPane.getChildren().removeAll(iv1);
    }

    @Override
    public void onUpdate(UserPreferences.PreferencesContainer value) {
        if (colorBlind != isColorBlind()) {
            colorBlind = isColorBlind();
            handController.switchColorBlind(GameStoreProvider.getPlayer());
            bankController.updateCardImages(GameStoreProvider.getStore());
            routesMapController.switchColorBlind(colorBlind);
        }
    }
}
