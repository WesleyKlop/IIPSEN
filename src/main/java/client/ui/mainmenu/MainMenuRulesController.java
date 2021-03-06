package client.ui.mainmenu;

import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.ResourceBundle;

/**
 */
public class MainMenuRulesController implements Initializable {

    public Text ruleRules;
    public HBox HBoxRule1, HBoxRule2, HBoxRule3;
    public Slider optionFontSize;

    public void initialize(URL url, ResourceBundle bundle) {
        ruleRules.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/MavenPro-Regular.ttf"), optionFontSize.getValue()));
        ruleRules.styleProperty().bind(Bindings.format("-fx-font-size: %2fpt;", optionFontSize.valueProperty()));
    }

    public void changeFont() {
        ruleRules.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/MavenPro-Regular.ttf"), optionFontSize.getValue()));
    }

    public void manageRules(MouseEvent mouseEvent) {
        Label label = (Label) mouseEvent.getSource();
        String rules = getRule(label);
        ruleRules.setText(rules);
    }

    /**
     * Searches corresponding rule to label in rules.xml
     *
     * @param label Source Label
     * @return Found rule
     */
    private String getRule(Label label) {
        String labelId = label.getId();
        String rules = "";
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document rulesDoc = builder.parse(getClass().getResourceAsStream("/string/rules.xml"));
            rulesDoc.getDocumentElement().normalize();

            String[] arr = rulesDoc.getElementsByTagName(labelId).item(0).getTextContent().split("\n");
            StringBuilder buffer = new StringBuilder();
            for (String line : arr) {
                buffer.append(line.trim());
                buffer.append("\n");
            }
            rules = buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rules;
    }
}
