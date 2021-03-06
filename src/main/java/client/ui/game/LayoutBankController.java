package client.ui.game;

import client.ui.dialogs.MessagesControllerProvider;
import game.GameStore;
import game.GameStoreProvider;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Observable;
import util.Observer;

public class LayoutBankController implements Observer<GameStore> {

    private static final Logger Log = LogManager.getLogger(LayoutBankController.class);
    private static final int NOT_SELECTED = -1;
    private Observable<GameStore> storeObservable = GameStoreProvider.getInstance();

    @FXML
    private VBox rootBox;
    private int selectedIndex = NOT_SELECTED;

    @FXML
    public void initialize() {
        setCardImages(storeObservable.getValue());
        storeObservable.addObserver(this);
    }

    public void updateCardImages(GameStore store) {
        setCardImages(store);
    }

    private void setCardImages(GameStore store) {
        for (int i = 1; i < rootBox.getChildren().size(); i++) {
            updateCard(store, i);
        }
    }

    private void updateCard(GameStore store, int index) {
        ImageView imageView = (ImageView) rootBox.getChildren().get(index);
        imageView.setImage(new Image(getClass().getResourceAsStream(store.getCardStackController().getOpenCards()[index - 1].getPath())));
    }

    @FXML
    private void onMouseAction(MouseEvent mE) {
        ImageView source = (ImageView) mE.getSource();
        int index = Integer.parseInt(source.getId());
        if (isSelected()) {
            if (isSelectedCurrent(index)) {
                if (isSelectedCurrent(0)) {
                    int[] indexes = {selectedIndex, index};
                    MessagesControllerProvider.getMessageController().openTrainCardMessage(indexes);
                    deselectSelected();
                } else {
                    deselectSelected();
                }
            } else {
                int[] indexes = {selectedIndex, index};
                MessagesControllerProvider.getMessageController().openTrainCardMessage(indexes);
                deselectSelected();
            }
        } else {
            setSelected(index);
        }
        //1. Is there already a card selected? Yes: Do 2. No: Do 3.
        //2. Is the selected the same as the card you clicked? Yes: Do 4. No: Do 5.
        //3. Select clicked cardIndex. (index = Integer.parseInt(mouseEvent.getSource.getId())
        //4. Deselect clicked cardIndex (index = null)
        //5. 2 Cards are selected. Open "Are you sure" message with the 2 cards. Do 6.
        //6. Deselect both cards.
    }

    private boolean isSelected() {
        return selectedIndex != NOT_SELECTED;
    }

    private void setSelected(int index) {
        if (isSelected()) {
            takeSelectedEffect();
        }
        selectedIndex = index;
        selectedEffect();
    }

    private boolean isSelectedCurrent(int index) {
        return index == selectedIndex;
    }

    private void deselectSelected() {
        takeSelectedEffect();
        selectedIndex = NOT_SELECTED;
    }

    private void selectedEffect() {
        ScaleTransition scaleTrans = new ScaleTransition(Duration.millis(200), rootBox.getChildren().get(selectedIndex));
        scaleTrans.setToX(1.1);
        scaleTrans.setToY(1.1);
        scaleTrans.play();
    }

    private void takeSelectedEffect() {
        ScaleTransition scaleTrans = new ScaleTransition(Duration.millis(200), rootBox.getChildren().get(selectedIndex));
        scaleTrans.setToX(1);
        scaleTrans.setToY(1);
        scaleTrans.play();
    }


    @Override
    public void onUpdate(GameStore store) {
        Platform.runLater(() -> setCardImages(store));
    }
}
