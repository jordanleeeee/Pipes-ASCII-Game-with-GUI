package views.panes;

import controllers.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.Config;
import models.FXGame;
import models.FlowTimer;
import views.*;

import java.util.Arrays;

/**
 * Pane for the Level Editor.
 */
public class LevelEditorPane extends GamePane {

    private final LevelEditorCanvas levelEditor = new LevelEditorCanvas(FXGame.getDefaultRows(), FXGame.getDefaultCols(), FlowTimer.getDefaultDelay());
    private final VBox leftContainer = new SideMenuVBox();

    private final Button returnButton = new BigButton("Return");

    private Label rowText = new Label("Rows");
    private NumberTextField rowField = new NumberTextField(String.valueOf(levelEditor.getNumOfRows()));
    private BorderPane rowBox = new BorderPane(null, null, rowField, null, rowText);

    private Label colText = new Label("Columns");
    private NumberTextField colField = new NumberTextField(String.valueOf(levelEditor.getNumOfCols()));
    private BorderPane colBox = new BorderPane(null, null, colField, null, colText);

    private Button newGridButton = new BigButton("New Grid");

    private Label delayText = new Label("Delay");
    private NumberTextField delayField = new NumberTextField(String.valueOf(levelEditor.getAmountOfDelay()));
    private BorderPane delayBox = new BorderPane(null, null, delayField, null, delayText);

    private ObservableList<LevelEditorCanvas.CellSelection> cellList = FXCollections.observableList(Arrays.asList(LevelEditorCanvas.CellSelection.values()));
    private ListView<LevelEditorCanvas.CellSelection> selectedCell = new ListView<>(); //????

    private Button toggleRotationButton = new BigButton("Toggle Source Rotation");
    private Button loadButton = new BigButton("Load");
    private Button saveButton = new BigButton("Save As");

    private VBox centerContainer = new BigVBox();

    public LevelEditorPane() {
        connectComponents();
        styleComponents();
        setCallbacks();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void connectComponents() {
        // TODO
        leftContainer.getChildren().add(returnButton);
        leftContainer.getChildren().add(rowBox);
        leftContainer.getChildren().add(colBox);
        leftContainer.getChildren().add(newGridButton);
        leftContainer.getChildren().add(delayBox);
        selectedCell.setItems(cellList);
        leftContainer.getChildren().add(selectedCell);
        leftContainer.getChildren().add(toggleRotationButton);
        leftContainer.getChildren().add(loadButton);
        leftContainer.getChildren().add(saveButton);
        this.setLeft(leftContainer);
        centerContainer.getChildren().add(levelEditor);
        centerContainer.setAlignment(Pos.CENTER);
        this.setCenter(centerContainer);
    }

    /**
     * {@inheritDoc}
     *
     * {@link LevelEditorPane#selectedCell} should have cell heights of {@link Config#LIST_CELL_HEIGHT}.
     */
    @Override
    void styleComponents() {
        // TODO
        selectedCell.setMinHeight(Config.LIST_CELL_HEIGHT*6);
        selectedCell.setMaxHeight(Config.LIST_CELL_HEIGHT*6);
        levelEditor.setStyle("-fx-background-color: rgb(52, 235, 235);");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setCallbacks() {
        // TODO
        returnButton.setOnAction(e->SceneManager.getInstance().showPane(MainMenuPane.class));
        newGridButton.setOnAction(e->levelEditor.changeAttributes(rowField.getValue(),
               colField.getValue(), delayField.getValue()));
        newGridButton.setOnAction(e->levelEditor.changeAttributes(rowField.getValue(), colField.getValue(), delayField.getValue()));
        toggleRotationButton.setOnAction(e->levelEditor.toggleSourceTileRotation());
        loadButton.setOnAction(e->{
            if(levelEditor.loadFromFile()){
                rowField.setText(String.valueOf(levelEditor.getNumOfRows()));
                colField.setText(String.valueOf(levelEditor.getNumOfCols()));
                delayField.setText(String.valueOf(levelEditor.getAmountOfDelay()));
            }
        });
        saveButton.setOnAction(e->levelEditor.saveToFile());
        levelEditor.setOnMouseClicked(e-> {
            int editingIndex = selectedCell.getSelectionModel().getSelectedIndex();
            if(editingIndex != -1) {    //if some cell is selected
                levelEditor.setTile(cellList.get(editingIndex), e.getX(), e.getY());
            }
        });
    }
}
