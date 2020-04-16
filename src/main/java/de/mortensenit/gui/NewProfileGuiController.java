package de.mortensenit.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mortensenit.model.DataStorageProfile;
import de.mortensenit.persistence.ProfileController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Simple gui controller for the new profile dialogue
 * 
 * @author frederik.mortensen
 *
 */
public class NewProfileGuiController {

	private Logger logger = LoggerFactory.getLogger(getClass().getName());

	@FXML
	private TextField profileName;

	@FXML
	private TextField jarPath;

	@FXML
	private TextField dataRootClassName;

	@FXML
	private Button saveButton;

	@FXML
	private Button backButton;
	
	private ProfileController profileController = new ProfileController();
	
	@FXML
	public void handleSaveButton(ActionEvent event) {

		logger.info(
				"Inserting " + profileName.getText() + ", " + jarPath.getText() + ", " + dataRootClassName.getText());
		
		DataStorageProfile profile = new DataStorageProfile();
		profile.setProfileName(profileName.getText());
		profile.setJarPath(jarPath.getText());
		profile.setDataRootClassName(dataRootClassName.getText());
		profileController.save(profile);

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
