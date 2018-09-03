package ec.ttbmd;

import ec.ttbmd.model.Event;
import ec.ttbmd.model.EventListWrapper;
import ec.ttbmd.util.CanvasUtil;
import ec.ttbmd.view.EditToolbarController;
import ec.ttbmd.view.EventEditDialogController;
import ec.ttbmd.view.Overview_NDCController;
import ec.ttbmd.view.Overview_NDEController;
import ec.ttbmd.view.SettingsDialogController;
import ec.ttbmd.view.ToolbarController;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class MainApp extends Application {

    private Stage primaryStage;
    private final BorderPane rootLayout = new BorderPane();

    private final StackPane baseOfAll = new StackPane();

    private final Region opLayer = new Region();

    private static MainApp instance;

    public boolean showDrawer = true;

    public int expCanvasHeight = 720;

    public int expCanvasWidth = 1280;

    public boolean showEditToolbar = false;
    
    public boolean showWeekEnd = false;
    
    public boolean showHalfHour = false;

    public boolean showTableMode = true;

    private EditToolbarController editToolbarController;
    
    private Overview_NDCController overview_NDCController;
    
    private Overview_NDEController overview_NDEController;

    private final ObservableList<Event> eventData = FXCollections.observableArrayList();

    public List<Boolean> Selection = new ArrayList<Boolean>();

    public List<Double> ContentSize = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("TimeTableMD");

        opLayer.setStyle("-fx-background-color: rgba(0,0,0,0.38);");
        opLayer.setVisible(false);

        baseOfAll.getChildren().add(rootLayout);
        baseOfAll.getChildren().add(opLayer);

        Scene scene = new Scene(baseOfAll, 960, 630);

        //this.primaryStage.getIcons().add(new Image("Icon64_2"));
        //Image image = new Image("assets/Icon64_2.png");
        //this.primaryStage.getIcons().add(image);
        this.primaryStage.setScene(scene);
        this.primaryStage.show();

        initMainToolbar();

        ExpandNavigationDrawer();

    }

    public MainApp() {

        instance = this;
        // Add some sample data
        /*
        eventData.add(new Event("Math I", "MI", "lightblue", "TP"));*/

    }

    public static MainApp getInstance() {
        return instance;
    }

    public void layoutState(boolean ShowNavigationDrawer) {
        if (ShowNavigationDrawer) {
            ExpandNavigationDrawer();
        } else {
            CollapseNavigationDrawer();
        }
    }

    public void ToolbarManager() {
        if (!getSelectedInd().isEmpty()) {
            if (!showEditToolbar) {
                initEditToolbar();
            }
            editToolbarController.updateEditButton();
        } else {
            if (showEditToolbar) {
                initMainToolbar();
            }
        }
    }

    public void ExpandNavigationDrawer() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Overview_NDE.fxml"));
            AnchorPane overview_NDE = (AnchorPane) loader.load();

            rootLayout.setCenter(overview_NDE);

            overview_NDEController = loader.getController();
            overview_NDEController.setMainApp(this);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
        
    public void CollapseNavigationDrawer() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Overview_NDC.fxml"));
            AnchorPane overview_NDC = (AnchorPane) loader.load();

            rootLayout.setCenter(overview_NDC);

            overview_NDCController = loader.getController();
            overview_NDCController.setMainApp(this);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public void initEditToolbar() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/EditToolbar.fxml"));
            GridPane Toolbar = (GridPane) loader.load();

            rootLayout.setTop(Toolbar);

            editToolbarController = loader.getController();

            editToolbarController.setMainApp(this);

            showEditToolbar = true;

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
    
    public void updateCanvas() {
        if(showDrawer){
            overview_NDEController.updateCanvas();
            System.out.println("Atualizando canvas");
        } else {
            overview_NDCController.updateCanvas();
            System.out.println("Atualizando canvas");
        }
    }

    public void initMainToolbar() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Toolbar.fxml"));
            GridPane Toolbar = (GridPane) loader.load();

            rootLayout.setTop(Toolbar);

            ToolbarController controller = loader.getController();

            controller.setMainApp(this);

            showEditToolbar = false;

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public void setSelection(boolean param) {
        for (int i = 0; i < Selection.size(); i++) {
            Selection.set(i, param);
        }

        ToolbarManager();
    }

    public List<Integer> getSelectedInd() {
        List<Integer> SelectionIndices = new ArrayList<Integer>();

        int count = 0;

        for (boolean item : Selection) {

            if (item) {
                SelectionIndices.add(count);
            }

            count++;
        }

        return SelectionIndices;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public ObservableList<Event> getEventData() {
        return eventData;
    }

    public File getEventFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    public void setEventFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            primaryStage.setTitle("Table - " + file.getName());
        } else {
            prefs.remove("filePath");

            primaryStage.setTitle("New Table");
        }
    }

    public void loadEventDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(EventListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            EventListWrapper wrapper = (EventListWrapper) um.unmarshal(file);

            eventData.clear();
            eventData.addAll(wrapper.getEvents());

            setEventFilePath(file);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    public void saveEventDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(EventListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            EventListWrapper wrapper = new EventListWrapper();
            wrapper.setEvents(eventData);

            m.marshal(wrapper, file);

            setEventFilePath(file);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    public boolean showEventEditDialog(Event event) {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/EventEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            opLayer.setVisible(true);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Event");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            scene.setFill(Paint.valueOf("rgba(0,0,0,0.38)"));
            dialogStage.setScene(scene);

            EventEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setEvent(event);

            dialogStage.showAndWait();

            opLayer.setVisible(false);

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void getCanvas() {

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            try {
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(getSnapShot(), null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    private WritableImage getSnapShot() {
        ObservableList<Event> eventsList;

        eventsList = getEventData();

        CanvasUtil.updateEventsList(eventsList);

        WritableImage canvasSnapShot;
        canvasSnapShot = new WritableImage(expCanvasWidth, expCanvasHeight);

        Canvas expCanvas;

        expCanvas = new Canvas(expCanvasWidth, expCanvasHeight);

        GraphicsContext expGC;
        expGC = expCanvas.getGraphicsContext2D();

        expGC.clearRect(0, 0, expCanvasWidth, expCanvasHeight);
        expGC.setFill(Color.WHITE);
        expGC.fillRect(0, 0, expCanvasWidth, expCanvasHeight);

        CanvasUtil.drawBaseTable(expGC, 29, expCanvasHeight, expCanvasWidth, showHalfHour, showWeekEnd);
        CanvasUtil.drawAllEvents(expGC, eventsList, expCanvasHeight, expCanvasWidth, 29, showWeekEnd);

        expCanvas.snapshot(null, canvasSnapShot);

        return canvasSnapShot;
    }

    public boolean showSettingsDialog() {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/SettingsDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            opLayer.setVisible(true);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Definições");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            scene.setFill(Paint.valueOf("rgba(0,0,0,0.38)"));
            dialogStage.setScene(scene);

            SettingsDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCurrentSettings(this);

            dialogStage.showAndWait();

            opLayer.setVisible(false);

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
