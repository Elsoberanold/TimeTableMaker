package ec.ttbmd.view;

import ec.ttbmd.MainApp;
import ec.ttbmd.model.Event;
import ec.ttbmd.util.ColorUtil;
import ec.ttbmd.util.TimeUtil;
import java.net.URL;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class ListViewModeController implements Initializable {

    @FXML
    public ListView<Event> ListView;

    private MainApp MainApp;

    private static ListViewModeController listViewModeController;

    private ObservableList<Event> events = FXCollections.observableArrayList();

    private final Overview_NDEController overview_NDEController = Overview_NDEController.getInstance();

    private final Overview_NDCController overview_NDCController = Overview_NDCController.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        listViewModeController = this;

        ListView.setCellFactory(new Callback<ListView<Event>, ListCell<Event>>() {
            @Override
            public ListCell<Event> call(ListView<Event> list) {
                return new SetCellFormat();
            }
        }
        );
    }

    public class SetCellFormat extends ListCell<Event> {

        private final GridPane RootElement;
        private final HBox LeftC;
        private final HBox RightC;
        private final ToggleButton TickButton;
        private final VBox MiddleC;
        private final Label HeadLB;
        private final Label SubHeadLB;
        private final Label NormalLB;
        private final Label RoomLB;

        {
            //RootElement
            RootElement = new GridPane();
            RootElement.setPrefHeight(88);
            RootElement.setPadding(new Insets(0));
            RootElement.setStyle("-fx-background-color: white;"
                    + "-fx-border-width: 0 0 0 0;" + "-fx-border-color: #0000001a;");

            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(70);
            column.setHalignment(HPos.LEFT);
            RootElement.getColumnConstraints().add(column);
            column = new ColumnConstraints();
            column.setPercentWidth(30);
            column.setHalignment(HPos.RIGHT);
            RootElement.getColumnConstraints().add(column);

            //Left side
            LeftC = new HBox();
            LeftC.setAlignment(Pos.TOP_LEFT);
            LeftC.setPadding(new Insets(12, 0, 0, 12));
            LeftC.setSpacing(12);

            MiddleC = new VBox();
            MiddleC.setAlignment(Pos.TOP_LEFT);
            MiddleC.setSpacing(2);

            TickButton = new ToggleButton();
            TickButton.setPrefSize(40, 40);

            TickButton.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {

                MainApp.Selection.set(getIndex(), isNowSelected);

                if (MainApp.Selection.get(getIndex())) {
                    RootElement.setStyle("-fx-background-color: #0000001a;"
                            + "-fx-border-width: 0 0 0 0;" + "-fx-border-color: #0000001a;");
                } else {
                    RootElement.setStyle("-fx-background-color: white;"
                            + "-fx-border-width: 0 0 0 0;" + "-fx-border-color: #0000001a;");
                }

                UpdateAddButton(MainApp.showDrawer);

                MainApp.ToolbarManager();

            });

            //Right side
            RightC = new HBox();
            RightC.setAlignment(Pos.TOP_RIGHT);
            RightC.setPadding(new Insets(12));

            //Labels
            HeadLB = createLabel(16, 400, "#263238ff");
            SubHeadLB = createLabel(14, 400, "#263238ff");
            NormalLB = createLabel(14, 400, "#00000099");
            RoomLB = createLabel(14, 600, "#263238ff");

            MiddleC.getChildren().addAll(HeadLB, SubHeadLB, NormalLB);

            LeftC.getChildren().addAll(TickButton, MiddleC);
            RightC.getChildren().addAll(RoomLB);

            RootElement.add(LeftC, 0, 0);
            RootElement.add(RightC, 1, 0);

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        }

        private final Label createLabel(Integer size, Integer wheight, String color) {
            Label label = new Label();
            label.setMaxWidth(Double.MAX_VALUE);
            label.setStyle(
                    "-fx-font-family: Roboto, Helvetica, Arial, sans-serif;"
                    + "-fx-font-size: +" + size + "px;"
                    + "-fx-font-weight: " + wheight + ";"
                    + "-fx-text-fill: " + color + ";"
                    + "-fx-label-padding: 0px;"
            );
            label.setAlignment(Pos.CENTER_LEFT);
            return label;
        }

        @Override
        public void updateItem(Event event, boolean empty) {
            super.updateItem(event, empty);

            if (ListView.getItems().size() != MainApp.Selection.size()) {

                MainApp.Selection.clear();

                for (int i = 0; i < ListView.getItems().size(); i++) {
                    MainApp.Selection.add(false);
                }

            }

            if (event == null) {
                setText(null);
                setGraphic(null);

            } else {

                TickButton.setSelected(MainApp.Selection.get(getIndex()));

                TickButton.setId("round-tick");

                TickButton.setStyle("color-main-property: " + ColorUtil.getHex(event.getCOLOR()) + ";");

                TickButton.setText(event.getTYPE());

                HeadLB.setText(event.getSHORTNAME());
                SubHeadLB.setText(event.getNAME());
                NormalLB.setText(
                        (event.getDAY()).getDisplayName(TextStyle.FULL, Locale.forLanguageTag("en-US"))
                        + "s from " + TimeUtil.new_format(event.getSTART()) + " to " + TimeUtil.new_format(event.getEND())
                );
                RoomLB.setText(event.getROOM());
                setGraphic(RootElement);
                setText(null);
            }
        }
    }

    public void UpdateAddButton(boolean showDrawer) {

        if (showDrawer) {

            if (MainApp.getSelectedInd().isEmpty()) {
                overview_NDEController.addButton.setVisible(true);
            } else {
                overview_NDEController.addButton.setVisible(false);
            }

        } else {
            if (MainApp.getSelectedInd().isEmpty()) {
                overview_NDCController.addButton.setVisible(true);
            } else {
                overview_NDCController.addButton.setVisible(false);
            }
        }
    }

    public void DeleteEvents() {

        Collection<Event> selEv = new ArrayList<Event>();

        MainApp.getSelectedInd().forEach((Integer i) -> {
            selEv.add(ListView.getItems().get(i));
        });

        ListView.getItems().removeAll(selEv);

        MainApp.Selection.clear();
        UpdateAddButton(MainApp.showDrawer);
        MainApp.ToolbarManager();

    }

    public void setMainApp(MainApp mainApp) {
        this.MainApp = mainApp;

        events = mainApp.getEventData();

        sortEventsList();

        ListView.setItems(events);

    }

    public static ListViewModeController getInstance() {
        return listViewModeController;
    }

    private void sortEventsList() {
        FXCollections.sort(events, (Event e1, Event e2) -> {
            if (e1.getDAY().getValue() < e2.getDAY().getValue()) {
                return -1;
            }
            if (e1.getDAY().getValue() > e2.getDAY().getValue()) {
                return 1;
            }

            if (TimeUtil.toMinutes(e1.getSTART()) < TimeUtil.toMinutes(e2.getSTART())) {
                return -1;
            }
            if (TimeUtil.toMinutes(e1.getSTART()) > TimeUtil.toMinutes(e2.getSTART())) {
                return 1;
            }

            if (TimeUtil.toMinutes(e1.getEND()) < TimeUtil.toMinutes(e2.getEND())) {
                return -1;
            }
            if (TimeUtil.toMinutes(e1.getEND()) > TimeUtil.toMinutes(e2.getEND())) {
                return 1;
            }

            return 0;
        });
    }

}
