package views.panes;

import controllers.AudioManager;
import controllers.SceneManager;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import models.Config;
import models.FXGame;
import models.FlowTimer;
import org.jetbrains.annotations.NotNull;
import views.BigButton;
import views.BigVBox;
import views.NumberTextField;
import views.SideMenuVBox;

import java.util.Optional;

public class SettingsPane extends GamePane {

    @NotNull
    private static final String MSG_BAD_ROW_NUM = "Row number should be at least 2!";
    @NotNull
    private static final String MSG_BAD_COL_NUM = "Column number should be at least 2!";
    @NotNull
    private static final String MSG_BAD_DELAY_NUM = "Delay value should be a positive integer!";
    @NotNull
    private static final String MSG_BAD_FLOW_NUM = "Flow rate should be a positive integer!";

    @NotNull
    private final VBox leftContainer = new SideMenuVBox();
    @NotNull
    private final Button saveButton = new BigButton("Save");
    @NotNull
    private final Button returnButton = new BigButton("Return");
    @NotNull
    private final Button toggleSoundButton = new BigButton("Sound FX: Enabled");
    @NotNull
    private final Button countDownModeButton = new BigButton("Counting Down: Disabled");
    /**
     * Text field for modifying the number of rows for generated maps.
     *
     * @see FXGame#getDefaultRows()
     * @see FXGame#setDefaultRows(int)
     */
    @NotNull
    private final NumberTextField rowsField = new NumberTextField(String.valueOf(FXGame.getDefaultRows()));
    @NotNull
    private final BorderPane rowBox = new BorderPane(null, null, rowsField, null, new Label("Default Rows"));
    /**
     * Text field for modifying the number of columns for generated maps.
     *
     * @see FXGame#getDefaultCols()
     * @see FXGame#setDefaultCols(int)
     */
    @NotNull
    private final NumberTextField colsField = new NumberTextField(String.valueOf(FXGame.getDefaultCols()));
    @NotNull
    private final BorderPane colBox = new BorderPane(null, null, colsField, null, new Label("Default Columns"));
    /**
     * Text field for modifying the default flow delay for generated maps.
     *
     * @see FlowTimer#getDefaultDelay()
     * @see FlowTimer#setDefaultDelay(int)
     */
    @NotNull
    private final NumberTextField delayField = new NumberTextField(String.valueOf(FlowTimer.getDefaultDelay()));
    @NotNull
    private final BorderPane delayBox = new BorderPane(null, null, delayField, null, new Label("Default Delay"));
    /**
     * Text field for modifying the default flow rate.
     *
     * @see FlowTimer#getDefaultFlowDuration()
     * @see FlowTimer#setDefaultFlowDuration(int)
     */
    @NotNull
    private final NumberTextField flowField = new NumberTextField(String.valueOf(FlowTimer.getDefaultFlowDuration()));
    @NotNull
    private final BorderPane flowBox = new BorderPane(null, null, flowField, null, new Label("Flow Rate (s)"));
    @NotNull
    private final VBox centerContainer = new BigVBox();
    @NotNull
    private final TextArea infoText = new TextArea(Config.getAboutText());

    public SettingsPane() {
        connectComponents();
        styleComponents();
        setCallbacks();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connectComponents() {
        // TODO
        leftContainer.getChildren().add(returnButton);
        leftContainer.getChildren().add(saveButton);
        leftContainer.getChildren().add(rowBox);
        leftContainer.getChildren().add(colBox);
        leftContainer.getChildren().add(delayBox);
        leftContainer.getChildren().add(flowBox);
        leftContainer.getChildren().add(toggleSoundButton);
        leftContainer.getChildren().add(countDownModeButton);
        this.setLeft(leftContainer);
        centerContainer.getChildren().add(infoText);
        this.setCenter(centerContainer);
    }

    /**
     * {@inheritDoc}
     *
     * In particular, the text box should be not editable, text should be wrapped around, "text-area" style should
     * be applied, and preferred height should be {@link Config#HEIGHT}.
     */
    @Override
    void styleComponents() {
        // TODO
        infoText.setEditable(false);
        infoText.setWrapText(true);
        infoText.setStyle("-fx-font-size: 18;");
        infoText.setPrefHeight(Config.HEIGHT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setCallbacks() {
        // TODO
        returnButton.setOnAction((e)->returnToMainMenu(false));
        saveButton.setOnAction((e)->returnToMainMenu(true));
        toggleSoundButton.setOnAction(e->{
            if(toggleSoundButton.getText() == "Sound FX: Enabled"){
                toggleSoundButton.setText("Sound FX: Disabled");
                AudioManager.getInstance().setEnabled(false);
            }
            else{
                toggleSoundButton.setText("Sound FX: Enabled");
                AudioManager.getInstance().setEnabled(true);
            }
        });
        countDownModeButton.setOnAction(e->{
            if(countDownModeButton.getText() == "Counting Down: Disabled"){
                countDownModeButton.setText("Counting Down: Enabled");
                FXGame.setDefaultCountDownEnabled(true);
            }
            else{
                countDownModeButton.setText("Counting Down: Disabled");
                FXGame.setDefaultCountDownEnabled(false);
            }
        });

    }

    /**
     * Fill in the default values for all editable fields.
     */
    private void fillValues() {
        // TODO
        FXGame.setDefaultRows(rowsField.getValue());
        FXGame.setDefaultCols(colsField.getValue());
        FlowTimer.setDefaultDelay(delayField.getValue());
        FlowTimer.setDefaultFlowDuration(flowField.getValue());
    }

    /**
     * Switches back to the {@link MainMenuPane}.
     *
     * @param writeback Whether to save the values present in the text fields to their respective classes.
     */
    private void returnToMainMenu(final boolean writeback) {
        // TODO
        if(writeback){
            if(!validate().isEmpty()){
                Alert error = new Alert(Alert.AlertType.ERROR, validate().get(), ButtonType.OK);
                error.setHeaderText("Validation Failed");
                error.show();
            }
            else{
                fillValues();
                SceneManager.getInstance().showPane(MainMenuPane.class);
            }
        }
        else{
            SceneManager.getInstance().showPane(MainMenuPane.class);
        }
    }

    /**
     * Validates on all the input {@link javafx.scene.control.TextField}.
     *
     * <p>
     * There are three things to check in this method:
     * <ul>
     * <li>Whether the value of {@link SettingsPane#delayField} is a positive integer</li>
     * <li>Whether the value of {@link SettingsPane#rowsField} is bigger than 1</li>
     * <li>Whether the value of {@link SettingsPane#colsField} is bigger than 1</li>
     * </ul>
     * </p>
     *
     * @return If validation failed, {@link Optional} containing the reason message; An empty {@link Optional}
     * otherwise.
     */
    @NotNull
    private Optional<String> validate() {
        // TODO
        if(delayField.getValue() < 1){;
            return Optional.of(MSG_BAD_DELAY_NUM);
        }
        if(rowsField.getValue() <= 1){
            return Optional.of(MSG_BAD_ROW_NUM);
        }
        if(colsField.getValue() <= 1){
           return Optional.of(MSG_BAD_COL_NUM);
        }
        if(flowField.getValue() < 1){
            return Optional.of(MSG_BAD_FLOW_NUM);
        }
        else{
            return Optional.empty();
        }
    }

    public void reset(){
        rowsField.setText(String.valueOf(FXGame.getDefaultRows()));
        colsField.setText(String.valueOf(FXGame.getDefaultCols()));
        delayField.setText(String.valueOf(FlowTimer.getDefaultDelay()));
        flowField.setText(String.valueOf(FlowTimer.getDefaultFlowDuration()));
    }
}
