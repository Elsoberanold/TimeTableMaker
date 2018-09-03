package ec.ttbmd.view;

import ec.ttbmd.MainApp;
import ec.ttbmd.model.Event;
import ec.ttbmd.util.ColorUtil;
import ec.ttbmd.util.TimeUtil;
import java.io.IOException;
import java.time.format.TextStyle;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;

public class EventListViewCell extends ListCell<Event>{

    private FXMLLoader mLLoader;
    
    private MainApp mainApp;
    
    private int ListSize;
    
    @FXML
    private GridPane rootPane;

    @FXML
    private ToggleButton tickButton;

    @FXML
    private Label headLabel;

    @FXML
    private Label subHeadLabel;

    @FXML
    private Label normalLabel;

    @FXML
    private Label roomLabel;
    
    @Override
    protected void updateItem(Event event, boolean empty) {
        super.updateItem(event, empty);
        
        if(empty || event == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("view/EventListViewCell.fxml"));;
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            
            tickButton.setSelected(mainApp.Selection.get(getIndex()));
            
            tickButton.setStyle("color-main-property: "+ColorUtil.getHex(event.getCOLOR())+";");
                                                               
            tickButton.setText(event.getTYPE());
            
            headLabel.setText(event.getSHORTNAME());
            subHeadLabel.setText(event.getNAME());
            normalLabel.setText(
                    (event.getDAY()).getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt-PT"))+
                        " das "+TimeUtil.format(event.getSTART())+" às "+TimeUtil.format(event.getEND())
                );
            roomLabel.setText(event.getROOM());

            setText(null);
            setGraphic(rootPane);
        }

    }
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;                                    
    }
    
}
