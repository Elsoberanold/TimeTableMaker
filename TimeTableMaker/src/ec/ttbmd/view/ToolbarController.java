package ec.ttbmd.view;

import ec.ttbmd.MainApp;
import javafx.fxml.FXML;


public class ToolbarController {
        
    private MainApp MainApp;   
    
    public void setMainApp(MainApp MainApp) {
        this.MainApp = MainApp;
    }
       
    @FXML
    private void handleBurgerMenu() {
                
        MainApp.showDrawer = !MainApp.showDrawer;          
        MainApp.layoutState(MainApp.showDrawer);
            
    }
    
    @FXML
    private void exportCanvasAsPNG() {
        MainApp.getCanvas();
        
    }
    
    @FXML
    private void settings() {
        boolean isOkClicked = MainApp.showSettingsDialog();
        MainApp.updateCanvas();
    }
    
    @FXML
    private void sobre() {
        MainApp.getCanvas();
        
    }
}
