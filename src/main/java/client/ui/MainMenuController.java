package client.ui;

/**
 */

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    public Pane rootPane;

    public VBox VBoxMain;
    public Label playLabel;
    public Label loadLabel;
    public Label ruleLabel;
    public Label optionLabel;
    public Label quitLabel;
    public VBox VBoxPlay;
    public VBox VBoxLoad;
    public VBox VBoxRule;
    public VBox VBoxOption;
    public Pane snowPane;
    public Pane snowPaneFront;

    public MediaPlayer player;

    public MainMenuPlayController VBoxPlayController;
    public MainMenuLoadController VBoxLoadController;
    @FXML
    private MainMenuOptionController VBoxOptionController;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        style(VBoxMain);
        style(VBoxPlay);
        style(VBoxLoad);
        rootPane.setStyle("-fx-background-color: linear-gradient(to bottom, #bfe8f9 0%,#0082ED 70%);");
        snow();

    }

    void style(Label label) {
        Font font = Font.loadFont(getClass().getResourceAsStream("/fonts/MavenPro-Medium.ttf"), 25);
        label.setFont(font);
        label.setStyle("-fx-background-color: white;" + "-fx-background-radius: 10;");
        label.setAlignment(Pos.CENTER);
        label.setTextFill(Color.BLACK);
        label.setPadding(new Insets(10));
        label.setPrefHeight(50);
        label.setPrefWidth(250);
    }
    private void style(VBox menu) {
        for (int i = 1; i < menu.getChildren().size(); i++) {
            style((Label) menu.getChildren().get(i));
        }
    }

    public void style(HBox menu) {
        for (int i = 0; i < menu.getChildren().size(); i++) {
            style((Label) menu.getChildren().get(i));
        }
    }

    public void hoverEnter(MouseEvent mouseEvent) {
        Label label = (Label) mouseEvent.getSource();
        label.setTextFill(Color.RED);
        label.setCursor(Cursor.HAND);
    }
    public void hoverExit(MouseEvent mouseEvent) {
        Label label = (Label) mouseEvent.getSource();
        label.setTextFill(Color.BLACK);
        label.setCursor(Cursor.DEFAULT);
    }

    /**
     * Manages menu opening and closing.
     *
     * @param mouseEvent Label source
     */
    public void openMenuSequence(MouseEvent mouseEvent) {
//        VBoxOptionController.playFX();
        Label label = (Label) mouseEvent.getSource();
        VBox menu = getMenu(label);
        int disabledMenu = getDisabledInt();
        closeMenu();
        openMenu(label, menu);
        enableMain(disabledMenu);
    }

    /**
     * Opens menu, disables opened menu
     *
     * @param label Label source
     * @param menu  Menu to be opened
     */
    private void openMenu(Label label, VBox menu) {
        label.setDisable(true);
        TranslateTransition ani = new TranslateTransition(Duration.millis(500), menu);
        ani.setToY(-1480);
        ani.play();
    }

    /**
     * Closes disabled menu
     */
    private void closeMenu() {
        VBox menu = getDisabledMenu();
        TranslateTransition ani = new TranslateTransition(Duration.millis(500), menu);
        ani.setToY(20);
        ani.play();
    }

    private void enableMain(int disabled) {
        VBoxMain.getChildren().get(disabled).setDisable(false);
    }

    /**
     * Searches trough the main labels
     *
     * @return disabled main label index number
     */
    private int getDisabledInt() {
        for (int i = 0; i < 5; i++) {
            if (VBoxMain.getChildren().get(i).isDisable()) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Matches disabled menu label with menu
     *
     * @return disabled menu
     */
    private VBox getDisabledMenu() {
        VBox menu = null;
        switch (getDisabledInt()) {
            case 1:
                menu = VBoxPlay;
                break;
            case 2:
                menu = VBoxLoad;
                break;
            case 3:
                menu = VBoxRule;
                break;
            case 4:
                menu = VBoxOption;
                break;
            default:
                break;
        }
        return menu;
    }

    /**
     * Matches label
     *
     * @param label main menu label
     * @return VBox corresponding the param label
     */
    private VBox getMenu(Label label) {
        VBox menu = null;
        switch (label.getId()) {
            case "playLabel":
                menu = VBoxPlay;
                break;
            case "loadLabel":
                menu = VBoxLoad;
                break;
            case "ruleLabel":
                menu = VBoxRule;
                break;
            case "optionLabel":
                menu = VBoxOption;
                break;
            default:
                break;
        }
        return menu;
    }

    public void quitGame() {
        System.exit(0);
    }

    private void snow() {
        int imageXAmount = 10;
        int imageYAmount = 4;
        ImageView[][] snow = new ImageView[imageXAmount][imageYAmount];
        Image snowGif = new Image(getClass().getResourceAsStream("/images/snow.gif"));
        for (int i = 0; i < imageXAmount; i++) {
            for (int j = 0; j < imageYAmount; j++) {
                snow[i][j] = new ImageView(snowGif);
                snow[i][j].setLayoutX(i * 400);
                snow[i][j].setLayoutY((j % 2) * 300);
                if (j >= 2) {
                    snowPaneFront.getChildren().add(snow[i][j]);
                } else {
                    snowPane.getChildren().add(snow[i][j]);
                }
            }
        }
    }


}
