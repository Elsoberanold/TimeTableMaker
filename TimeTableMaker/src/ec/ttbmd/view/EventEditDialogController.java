package ec.ttbmd.view;

import ec.ttbmd.model.Event;
import ec.ttbmd.util.ColorUtil;
import ec.ttbmd.util.TimeUtil;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class EventEditDialogController {

    final ObservableList<String> dayListChoiceBox = FXCollections.observableArrayList(
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday",
            "Sunday"
    );

    final ObservableList<String> colorListChoiceBox = FXCollections.observableArrayList(
            "purple",
            "darkblue",
            "lightblue",
            "darkgreen",
            "softgreen",
            "lightgreen",
            "yellow",
            "lightorange",
            "darkorange",
            "red",
            "pink"
    );

    final ObservableList<String> typeListChoiceBox = FXCollections.observableArrayList("T", "TP", "P");

    final ObservableList<String> timeListChoiceBox = FXCollections.observableArrayList(
            "08:00",
             "08:30",
             "09:00",
             "09:30",
             "10:00",
             "10:30",
             "11:00",
             "11:30",
             "12:00",
             "12:30",
             "13:00",
             "13:30",
             "14:00",
             "14:30",
             "15:00",
             "15:30",
             "16:00",
             "16:30",
             "17:00",
             "17:30",
             "18:00",
             "18:30",
             "19:00",
             "19:30",
             "20:00"
    );

    @FXML
    private TextField NameField;

    @FXML
    private TextField TagField;

    @FXML
    private ComboBox typeComboBox;

    @FXML
    private TextField RoomField;

    @FXML
    private ComboBox dayComboBox;

    @FXML
    private ComboBox startTimeComboBox;

    @FXML
    private ComboBox endTimeComboBox;

    @FXML
    private ComboBox<String> colorComboBox;

    private Stage dialogStage;
    private Event event;
    private boolean okClicked = false;

    @FXML
    private void initialize() {

        dayComboBox.setItems(dayListChoiceBox);
        dayComboBox.setEditable(false);
        dayComboBox.setPromptText("Pick a day");
        typeComboBox.setItems(typeListChoiceBox);
        typeComboBox.setEditable(false);
        typeComboBox.setPromptText("Type");
        colorComboBox.setItems(colorListChoiceBox);

        colorComboBox.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> p) {
                return new ListCell<String>() {

                    private final Circle colorIcon;

                    {
                        colorIcon = new Circle(8);
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            colorIcon.setFill(Paint.valueOf(ColorUtil.getHex(item)));
                            setGraphic(colorIcon);
                            setText(item.substring(0, 1).toUpperCase() + item.substring(1).toLowerCase());
                        }
                    }
                };
            }
        });

        colorComboBox.setButtonCell(new ListCell<String>() {

            private final Circle colorIcon;

            {
                colorIcon = new Circle(8);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    colorIcon.setFill(Paint.valueOf(ColorUtil.getHex(item)));
                    setGraphic(colorIcon);
                    setText(item.substring(0, 1).toUpperCase() + item.substring(1).toLowerCase());
                }
            }
        });

        colorComboBox.setEditable(false);
        colorComboBox.setPromptText("Pick a color");

        endTimeComboBox.setItems(timeListChoiceBox);
        startTimeComboBox.setItems(timeListChoiceBox);
        endTimeComboBox.setPromptText("hh:mm");
        endTimeComboBox.setEditable(true);
        startTimeComboBox.setPromptText("hh:mm");
        startTimeComboBox.setEditable(true);

    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.initStyle(StageStyle.UNDECORATED);
    }

    public void setEvent(Event event) {
        this.event = event;

        NameField.setText(event.getNAME());
        TagField.setText(event.getSHORTNAME());
        RoomField.setText(event.getROOM());

        typeComboBox.setValue(event.getTYPE());
        colorComboBox.setValue(event.getCOLOR());

        dayComboBox.setValue((event.getDAY()).getDisplayName(TextStyle.FULL, Locale.forLanguageTag("en-US")));

        startTimeComboBox.setValue(TimeUtil.new_format(event.getSTART()));
        endTimeComboBox.setValue(TimeUtil.new_format(event.getEND()));

    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            event.setNAME(NameField.getText());
            event.setSHORTNAME(TagField.getText());
            event.setROOM(RoomField.getText());

            event.setTYPE((String) typeComboBox.getValue());
            event.setCOLOR((String) colorComboBox.getValue());

            event.setDAY(DayOfWeek.of(dayComboBox.getSelectionModel().getSelectedIndex() + 1));

            event.setSTART(TimeUtil.new_parse(startTimeComboBox.getValue().toString()));
            event.setEND(TimeUtil.new_parse(endTimeComboBox.getValue().toString()));

            okClicked = true;

            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (NameField.getText() == null || NameField.getText().length() == 0) {
            errorMessage += "Invalid Name!\n";
        }
        if (TagField.getText() == null || TagField.getText().length() == 0) {
            errorMessage += "Invalid Tag!\n";
        }
        if (RoomField.getText() == null || RoomField.getText().length() == 0) {
            errorMessage += "Invalid Room!\n";
        }

        if (typeComboBox.getValue() == null) {
            errorMessage += "Invalid Type!\n";
        }

        if (dayComboBox.getValue() == null) {
            errorMessage += "Invalid Day!\n";
        }

        if (!TimeUtil.validTime(startTimeComboBox.getValue().toString())) {
            errorMessage += "Invalid Start!\n";
        }

        if (!TimeUtil.validTime(endTimeComboBox.getValue().toString())) {
            errorMessage += "Invalid End!\n";
        }

        if (colorComboBox.getValue() == null) {
            errorMessage += "Pick a color!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }
}
