package de.mortensenit.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Simple gui controller for the about dialogue
 * 
 * @author frederik.mortensen
 *
 */
public class EditProfileGuiController {

	private Logger logger = LoggerFactory.getLogger(getClass().getName());

	@FXML
	private String profileName;

	@FXML
	private String jarPath;

	@FXML
	private String dataRootClassName;

	@FXML
	private Button saveButton;

	@FXML
	private Button backButton;

	@FXML
	public void handleSaveButton(ActionEvent event) {
		
		logger.info("Persisting " + profileName + ", " + jarPath + ", " + dataRootClassName);
		
		Node source = (Node) event.getSource();
		Stage currentStage = (Stage) source.getScene().getWindow();
		new ChooseProfileGuiController().openProfileChooserDialogue(currentStage);
	}

	@FXML
	public void handleBackButton(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage currentStage = (Stage) source.getScene().getWindow();
		new ChooseProfileGuiController().openProfileChooserDialogue(currentStage);
	}

}
