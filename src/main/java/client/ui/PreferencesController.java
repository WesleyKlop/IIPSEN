package client.ui;

import game.GameStoreProvider;
import game.actions.AddPlayerAction;
import game.player.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class PreferencesController implements Initializable {
    private static final Logger Log = LogManager.getLogger(PreferencesController.class);

    public Pane rootPane;
    public TextField nameField, IPinput;
    public Label createButton, joinButton, backButton, nameLabel, ipLabel;
    public HBox buttons;
    public VBox ipBox;
    @FXML
    private ColorPreferenceController colorPreferenceController;
    private MainMenuController mainMenuController = new MainMenuController();

    public void initialize(URL url, ResourceBundle bundle) {
        style(buttons);
    }

    private void style(HBox hbox) {
        mainMenuController.style(hbox);
    }

    public void entered(MouseEvent mouseEvent) {
        mainMenuController.hoverEnter(mouseEvent);
    }

    public void exited(MouseEvent mouseEvent) {
        mainMenuController.hoverExit(mouseEvent);
    }

    public void submitPreferences() throws RemoteException {
        Log.debug("Got prefs");
        String name = nameField.getText();
        Color color = colorPreferenceController.getSelectedColor();
        var action = new AddPlayerAction(name, color);
        GameStoreProvider.sendAction(action);
    }

    public boolean checkName() {
        boolean allowed = false;
        String name = nameField.getText();
        String permitted = "abcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < name.length(); i++) {
            for (int j = 0; j < permitted.length(); j++) {
                if (name.charAt(i) == permitted.charAt(j)) {
                    allowed = true;
                }
            }
        }

        if (name.length() > 20) {
            allowed = false;
        }

        if (allowed) {
            return allowed;
        } else {
            nameLabel.setText("Your name must consist of atleast 1 letter of number");
            return allowed;
        }
    }


    public boolean checkNameDouble() {
        String name = nameField.getText();
        for (Player player : GameStoreProvider.getStore().getPlayers()) {
            if (name.equalsIgnoreCase(player.getPlayerName())) {
                nameLabel.setText("Name is already taken, please choose another name");
                return false;
            }
        }
        return true;
    }

}
