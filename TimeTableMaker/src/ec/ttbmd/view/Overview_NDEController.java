package ec.ttbmd.view;

import ec.ttbmd.MainApp;
import ec.ttbmd.model.Event;
import ec.ttbmd.util.TG_util;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

public class Overview_NDEController implements Initializable {

    @FXML
    private ToggleGroup view;
    
    @FXML
    private ToggleButton tableModeBtn;
    
    @FXML
    private ToggleButton graphicModeBtn;

    @FXML
    private BorderPane ContentDisplay;
    
    @FXML
    private TextField fileName;

    public Button addButton;
    
    private GraphicViewModeController graphicViewModeController;

    private MainApp MainApp;

    private static Overview_NDEController overview_NDEController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        overview_NDEController = this;

        System.out.println("inicializou NDE");
        TG_util.get().addAlwaysOneSelectedSupport(view);
        
    }

    public void TableMode() {
                
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ListViewMode.fxml"));
            AnchorPane TableMode = (AnchorPane) loader.load();

            ContentDisplay.setCenter(TableMode);

            ListViewModeController controller = loader.getController();
            controller.setMainApp(MainApp.getInstance());

            MainApp.showTableMode = true;

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    public void GraphicMode() {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/GraphicViewMode.fxml"));
            AnchorPane GraphicMode = (AnchorPane) loader.load();
            
            GraphicMode.prefWidthProperty().bind(ContentDisplay.widthProperty());
            GraphicMode.prefHeightProperty().bind(ContentDisplay.heightProperty());

            ContentDisplay.setCenter(GraphicMode);

            graphicViewModeController = loader.getController();
            graphicViewModeController.setMainApp(MainApp.getInstance());
            graphicViewModeController.updateCanvas();
            
            MainApp.showTableMode = false;
                      
        } catch (IOException e) {

            e.printStackTrace();

        }
    }
    
    public void updateCanvas() {
        if (!MainApp.showTableMode) {
            graphicViewModeController.updateCanvas();
        }
    }

    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(MainApp.getPrimaryStage());

        if (file != null) {
            MainApp.loadEventDataFromFile(file);
        }
        
        updateCanvas();
    }

    @FXML
    private void handleSave() {
        File eventFile = MainApp.getEventFilePath();
        if (eventFile != null) {
            MainApp.saveEventDataToFile(eventFile);
        } else {
            handleSaveAs();
        }
        
        updateCanvas();
    }

    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        
        if(fileName.getText()!=null){
            fileChooser.setInitialFileName(fileName.getText());
        }

        File file = fileChooser.showSaveDialog(MainApp.getPrimaryStage());

        if (file != null) {
            
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            MainApp.saveEventDataToFile(file);
        }
        
        updateCanvas();
    }

    @FXML
    private void handleNewEvent() {
        Event tempPerson = new Event();
        boolean okClicked = MainApp.showEventEditDialog(tempPerson);
        if (okClicked) {
            MainApp.getEventData().add(tempPerson);
        }
        updateCanvas();
    }

    public void setMainApp(MainApp mainApp) {
        MainApp = mainApp;
        
        if(mainApp.showTableMode) {
            view.selectToggle(tableModeBtn);
        } else {
            view.selectToggle(graphicModeBtn);
        }
        
        ModeManager(mainApp.showTableMode);
                
    }

    public static Overview_NDEController getInstance() {
        return overview_NDEController;
    }

    private void ModeManager(boolean stm) {
        if (stm) {
            TableMode();
        } else {
            GraphicMode();
        }
    }
    
}
