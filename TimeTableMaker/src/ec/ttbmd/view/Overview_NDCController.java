package ec.ttbmd.view;

import ec.ttbmd.MainApp;
import ec.ttbmd.model.Event;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Overview_NDCController implements Initializable {

    @FXML
    private BorderPane ContentDisplay;
    
    public Button addButton;
    
    private MainApp MainApp;
    
    private static Overview_NDCController overview_NDCController;
    
    private GraphicViewModeController graphicViewModeController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        overview_NDCController=this;
    }
    
    private void ModeManager(boolean stm) {
        if (stm) {
            TableMode();
        } else {
            GraphicMode();
        }
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
        
        ModeManager(mainApp.showTableMode);
    }
    
    public static Overview_NDCController getInstance() {
        return overview_NDCController;
    }
    
}
