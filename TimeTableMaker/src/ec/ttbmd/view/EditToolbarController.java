package ec.ttbmd.view;

import ec.ttbmd.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class EditToolbarController {
    
    private MainApp MainApp;
    
    private final ListViewModeController listViewModeController = ListViewModeController.getInstance();
        
    public Button editButton;
    
    public Button burgerMenu;
                
    public void setMainApp(MainApp MainApp) {
        this.MainApp = MainApp;
    }
    
    public void initialize() {
        burgerMenu.setDisable(true);
    }
                           
    @FXML
    private void handleDeleteButton() {
        listViewModeController.DeleteEvents();
    }
    
    @FXML
    private void handleClearSelectionButton() {
                        
        MainApp.setSelection(false);
        
        listViewModeController.ListView.refresh();
        
        System.out.println("Clear Selection");
                  
    }
    
    @FXML
    private void handleSelectAll() {
                
        MainApp.setSelection(true);
        
        listViewModeController.ListView.refresh();
        
        System.out.println("Select All");
                  
    }
    
    @FXML
    private void handleEdit() {
                
        MainApp.showEventEditDialog(listViewModeController.ListView.getItems().get(MainApp.getSelectedInd().get(0)));
     
        MainApp.setSelection(false);
        
        listViewModeController.ListView.refresh();
        
        System.out.println("Edit was pressed");                
    }
    
    public void updateEditButton() {
        if (MainApp.getSelectedInd().size()==1) {
            editButton.setDisable(false);
        } else {
            editButton.setDisable(true);
        }
    }
            
}
