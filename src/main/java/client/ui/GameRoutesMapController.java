package client.ui;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @author Thom
 * @since 6-6-2018
 */
public class GameRoutesMapController {

    private static final Logger Log = LogManager.getLogger(GameRoutesMapController.class);

    private final int CART_LENGTH = 30;
    private final int CART_WIDTH = 13;
    private final int CART_SPACING = 1;
    @FXML
    private Pane routes;


    public void initialize() {
        organize();
    }


    /**
     * This method reads every single route node from "/string/gameRoutes.xml"
     * Currently manages the styling of the route as well.
     */
    private void organize() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document routesDoc = builder.parse(getClass().getResourceAsStream("/string/gameRoutes.xml"));
            routesDoc.getDocumentElement().normalize();

            NodeList nRouteList = routesDoc.getElementsByTagName("route");

            for (int j = 0; j < nRouteList.getLength(); j++) {

                Node routeNode = nRouteList.item(j);
                Element routeElement = (Element) routeNode;

                NodeList nCartList = routeElement.getElementsByTagName("cart");

                int routeX = Integer.decode(routeElement.getElementsByTagName("baseX").item(0).getTextContent());
                int routeY = Integer.decode(routeElement.getElementsByTagName("baseY").item(0).getTextContent());
                int baseRot = Integer.decode(routeElement.getElementsByTagName("baseRot").item(0).getTextContent());
                String id = routeElement.getAttribute("id");

                VBox route = new VBox();
                route.setId(id);
                route.setLayoutX(routeX);
                route.setLayoutY(routeY);
                route.setRotate(baseRot);
                route.setSpacing(CART_SPACING);
                route.setAlignment(Pos.CENTER);
                routes.getChildren().add(route);
                route.setOnMouseClicked(this::soutRouteInformation);

                int locomotiveAmount = Integer.decode(routeElement.getElementsByTagName("locomotive").item(0).getTextContent());

                Color routeColor = typeToColor(routeElement.getElementsByTagName("cartType").item(0).getTextContent());

                for (int i = 0; i < nCartList.getLength(); i++) {
                    Element eElement = (Element) nCartList.item(i);
                    int x = Integer.decode(eElement.getAttribute("x"));
                    int y = Integer.decode(eElement.getAttribute("y"));
                    int rot = Integer.decode(eElement.getAttribute("rot"));

                    Rectangle cart = new Rectangle();
                    if (i < locomotiveAmount) {
                        cart.setWidth(CART_WIDTH + 5);
                    } else {
                        cart.setWidth(CART_WIDTH);
                    }

                    cart.setHeight(CART_LENGTH);
                    cart.setTranslateX(x);
                    cart.setTranslateY(y);
                    cart.setRotate(rot);
                    cart.setFill(routeColor);
                    cart.setStroke(Color.BLACK);
                    cart.setStrokeWidth(0.5);
                    route.getChildren().add(cart);
                }
            }


        } catch (Exception e) {
            Log.debug(e.toString());
        }
    }

    private Color typeToColor(String type) {
        switch (type) {
            case "CART_PURPLE":
                return Color.PURPLE;
            case "CART_GREEN":
                return Color.GREEN;
            case "CART_RED":
                return Color.RED;
            case "CART_ORANGE":
                return Color.ORANGE;
            case "CART_BLACK":
                return Color.BLACK;
            case "CART_WHITE":
                return Color.WHITE;
            case "CART_YELLOW":
                return Color.YELLOW;
            case "CART_BLUE":
                return Color.BLUE;
            case "CART_ANY":
                return Color.GRAY;
            default:
                break;
        }

        return null;
    }

    private void soutRouteInformation(MouseEvent mouseEvent) {
        VBox source = (VBox) mouseEvent.getSource();
        String id = source.getId();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document routesDoc = builder.parse(getClass().getResourceAsStream("/string/gameRoutes.xml"));
            routesDoc.getDocumentElement().normalize();

            NodeList nListRoutes = routesDoc.getElementsByTagName("route");
            for (int i = 0; i < nListRoutes.getLength(); i++) {
                if (nListRoutes.item(i).getAttributes().getNamedItem("id").getTextContent().equalsIgnoreCase(id)) {
                    Element eRoute = (Element) nListRoutes.item(i);

                    String Location1 = eRoute.getElementsByTagName("location1").item(0).getTextContent();
                    String Location2 = eRoute.getElementsByTagName("location2").item(0).getTextContent();
                    String length = eRoute.getElementsByTagName("length").item(0).getTextContent();
                    String locomotives = eRoute.getElementsByTagName("locomotive").item(0).getTextContent();
                    String cartType = eRoute.getElementsByTagName("cartType").item(0).getTextContent();
                    String type = eRoute.getElementsByTagName("type").item(0).getTextContent();
                    Log.debug("This route is a " + type + " connects: " + Location1 + " to " + Location2 + " with " + length + cartType + " of which " + locomotives + " a locomotive");

                    break;
                }
            }

        } catch (Exception e) {
            Log.debug(e.toString());
        }

    }


}