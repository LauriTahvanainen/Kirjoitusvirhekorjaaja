package kvk.utils;

import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.stage.StageStyle;
import kvk.enums.EtaisyysFunktio;
import kvk.enums.Korjaaja;
import kvk.enums.Sanasto;

/**
 * Staattisia metodeja erilaisten UI-komponenttien luomiseen.
 */
public class UiUtils {

    public static ListView<Sanasto> luoSanastoValitsin(ObservableList<Sanasto> sanastoLista, SelectionMode sanastoValinta) {
        ListView valitseSanastonKoot = new ListView();
        valitseSanastonKoot.setItems(sanastoLista);
        valitseSanastonKoot.getSelectionModel().setSelectionMode(sanastoValinta);
        valitseSanastonKoot.setOrientation(Orientation.HORIZONTAL);
        valitseSanastonKoot.setMaxHeight(40);
        return valitseSanastonKoot;
    }

    public static ChoiceBox<EtaisyysFunktio> luoEtaisyysFunktionValintaLaatikko() {
        ChoiceBox<EtaisyysFunktio> valintaLaatikko = new ChoiceBox<>();
        valintaLaatikko.getItems().addAll(EtaisyysFunktio.values());
        valintaLaatikko.setValue(EtaisyysFunktio.LEVENSHTEIN);
        return valintaLaatikko;
    }

    public static ChoiceBox<Korjaaja> luoKorjaajanValintaLaatikko() {
        ChoiceBox<Korjaaja> valitsin = new ChoiceBox<>();
        valitsin.getItems().addAll(Korjaaja.values());
        valitsin.setValue(Korjaaja.TRIE_BK);
        return valitsin;
    }

    /**
     * Luo ja palauttaa uuden slider objectin
     *
     * @param min minimi
     * @param maks maksimi
     * @param prefWidth leveys
     * @param kasvu kasvu liikuttaessa
     * @param oletus oletusarvo
     * @param tickUnit
     * @return new Slider
     */
    public static Slider luoSlideri(int min, int maks, int prefWidth, int kasvu, int oletus, int tickUnit) {
        Slider slider = new Slider(min, maks, oletus);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(tickUnit);
        slider.setMinorTickCount(0);
        slider.setSnapToTicks(true);
        slider.setBlockIncrement(kasvu);
        slider.setPrefWidth(prefWidth);
        return slider;
    }

    public static Alert luoProsessiKeskenIlmoitus(String title, String headerText, String contextText, boolean keskeytaNapilla) {
        Alert ilmoitus = new Alert(Alert.AlertType.CONFIRMATION);
        ilmoitus.setTitle(title);
        ilmoitus.setHeaderText(headerText);
        ilmoitus.setContentText(contextText);
        ilmoitus.initStyle(StageStyle.UNDECORATED);
        ilmoitus.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
        if (keskeytaNapilla) {
            ((Button) ilmoitus.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Keskeytä");
        } else {
            ilmoitus.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);
        }
        return ilmoitus;
    }

    public static void naytaVirheIlmoitus(String viesti) {
        Alert ilmoitus = new Alert(Alert.AlertType.ERROR);
        ilmoitus.setTitle("Virhe");
        ilmoitus.setContentText(viesti);
        ilmoitus.setHeaderText("Virhe!");
        ilmoitus.showAndWait();
    }

    public static void naytaInformaatioIlmoitus(String viesti) {
        Alert ilmoitus = new Alert(Alert.AlertType.WARNING);
        ilmoitus.setTitle("Huomio!");
        ilmoitus.setContentText(viesti);
        ilmoitus.setHeaderText("Huomio!");
        ilmoitus.showAndWait();
    }
}
