package de.mortensenit.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.mortensenit.model.DataStorageProfile;
import de.mortensenit.persistence.ProfileController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

	private Logger logger = LogManager.getLogger(getClass());

	@FXML
	private TextField profileName;

	@FXML
	private TextField jarPath;

	@FXML
	private TextField dataRootClassName;

	@FXML
	private TextField dataStorePath;

	@FXML
	private Button saveButton;

	@FXML
	private Button backButton;

	private ProfileController profileController = new ProfileController();

	/**
	 * Adding a new profile entry and persisting it via controller.
	 * 
	 * @param event triggerd by button
	 */
	@FXML
	public void handleSaveButton(ActionEvent event) {
		StringBuffer logOutput = new StringBuffer();
		logOutput.append("Inserting profile ");
		logOutput.append(profileName.getText());
		logOutput.append(" and jar path ");
		logOutput.append(jarPath.getText());
		logOutput.append(" and root class ");
		logOutput.append(dataRootClassName.getText());
		logOutput.append(" and dataStore path ");
		logOutput.append(dataStorePath.getText());
		logger.info(logOutput.toString());

		DataStorageProfile profile = new DataStorageProfile();
		profile.setProfileName(profileName.getText());
		profile.setJarPath(jarPath.getText());
		profile.setDataRootClassName(dataRootClassName.getText());
		profile.setDataStorePath(dataStorePath.getText());
		profileController.save(profile);

		Stage currentStage = JavaFXHelper.getStageFromEvent(event);
		new ChooseProfileGuiController().openProfileChooserDialogue(currentStage);
	}

	/**
	 * Navigate back to the chooseProfile dialogue.
	 * 
	 * @param event triggered by button
	 */
	@FXML
	public void handleBackButton(ActionEvent event) {
		Stage currentStage = JavaFXHelper.getStageFromEvent(event);
		new ChooseProfileGuiController().openProfileChooserDialogue(currentStage);
	}

}
