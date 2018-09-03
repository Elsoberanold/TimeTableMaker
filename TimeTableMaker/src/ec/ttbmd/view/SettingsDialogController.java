package ec.ttbmd.view;

import ec.ttbmd.MainApp;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SettingsDialogController implements Initializable {

    @FXML
    private ComboBox widthComboBox;
    @FXML
    private ComboBox heightComboBox;
    @FXML
    private CheckBox showWKCheckBox;
    @FXML
    private CheckBox showHHCheckBox;
    
    private Stage dialogStage;
    
    private boolean okClicked = false;
    
    private MainApp mainApp;

    final ObservableList<String> widthChoiceBox = FXCollections.observableArrayList(
            
        "1920",
        "1366",
        "1280",
        "720",
        "480"            
    );
    
    final ObservableList<String> heightChoiceBox = FXCollections.observableArrayList(
            
        "1080",
        "768",
        "720",
        "360",
        "320"            
    );
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        widthComboBox.setItems(widthChoiceBox);
        widthComboBox.setPromptText("Set width");
        widthComboBox.setEditable(true);
        heightComboBox.setItems(widthChoiceBox);
        heightComboBox.setPromptText("Set height");
        heightComboBox.setEditable(true);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.initStyle(StageStyle.UNDECORATED);
    }
    
    public void setCurrentSettings(MainApp mainApp) {
        this.mainApp = mainApp;
        
        widthComboBox.setValue(mainApp.expCanvasWidth);
        heightComboBox.setValue(mainApp.expCanvasHeight);
        
        showWKCheckBox.setSelected(mainApp.showWeekEnd);
        showHHCheckBox.setSelected(mainApp.showHalfHour);
                     
    }
    
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            mainApp.showHalfHour = showHHCheckBox.isSelected();
            mainApp.showWeekEnd = showWKCheckBox.isSelected();
            
            mainApp.expCanvasHeight = Integer.parseInt(heightComboBox.getValue().toString());
            mainApp.expCanvasWidth = Integer.parseInt(widthComboBox.getValue().toString());
                        
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
        
        if (heightComboBox.getValue()==null || !isInt(heightComboBox)) {
            errorMessage += "Invalid height!\n";
        }
                
        if (widthComboBox.getValue()==null || !isInt(widthComboBox)) {
            errorMessage += "Invalid width!\n";
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
    
    private boolean isInt (ComboBox test) {
        try {
            Integer.parseInt(test.getValue().toString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
}
