package ec.ttbmd.view;

import ec.ttbmd.MainApp;
import ec.ttbmd.model.Event;
import ec.ttbmd.util.CanvasUtil;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class GraphicViewModeController implements Initializable {

    private MainApp MainApp;

    @FXML
    private AnchorPane basePane;

    @FXML
    private Canvas myCanvas;

    @FXML
    private GraphicsContext gc;

    private ObservableList<Event> events = FXCollections.observableArrayList();

    private double Hdist;

    private double Wdist;

    public final int nTableLines = 29;

    private boolean showWeekEnd = false;
    
    private boolean showHalfHour = false;

    public void initGraphics() {
        gc = myCanvas.getGraphicsContext2D();
        Hdist = myCanvas.getHeight();
        Wdist = myCanvas.getWidth();
    }

    public void draw() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        
        CanvasUtil.drawBaseTable(gc, nTableLines, Hdist, Wdist, showHalfHour, showWeekEnd);
        CanvasUtil.drawAllEvents(gc, events, Hdist, Wdist, nTableLines, showWeekEnd);
                
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        updateCanvas();

        basePane.prefWidthProperty().addListener((ov, oldValue, newValue) -> {
            myCanvas.setWidth(newValue.doubleValue());
            Wdist = myCanvas.getWidth();
            draw();
        });

        basePane.prefHeightProperty().addListener((ov, oldValue, newValue) -> {
            myCanvas.setHeight(newValue.doubleValue());
            Hdist = myCanvas.getHeight();
            draw();
        });

    }

    public void setMainApp(MainApp mainApp) {

        this.MainApp = mainApp;

        events = mainApp.getEventData();
        
        showWeekEnd = mainApp.showWeekEnd;
                
        showHalfHour = mainApp.showHalfHour;

        CanvasUtil.updateEventsList(events);
        
    }

    public void updateCanvas() {
        if(MainApp!=null) {
            showWeekEnd = MainApp.showWeekEnd;
            showHalfHour = MainApp.showHalfHour;
        }
        CanvasUtil.updateEventsList(events);
        initGraphics();
        draw();
    }

}
