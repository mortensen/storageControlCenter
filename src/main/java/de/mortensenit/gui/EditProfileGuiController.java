package de.mortensenit.gui;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.mortensenit.model.DataStorageProfile;
import de.mortensenit.persistence.PersistenceController;
import de.mortensenit.persistence.ProfileController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Simple gui controller for the edit profile dialogue
 * 
 * @author frederik.mortensen
 *
 */
public class EditProfileGuiController {

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
	private TextField packageName;

	@FXML
	private Button saveButton;

	@FXML
	private Button backButton;

	private DataStorageProfile profile;

	private ProfileController profileController = new ProfileController();

	/**
	 * Collect the input and send it to the controller to update the profile.
	 * 
	 * @param event triggered by button
	 */
	@FXML
	public void handleSaveButton(ActionEvent event) {

		StringBuffer logOutput = new StringBuffer();
		logOutput.append("Updating profile ");
		logOutput.append(profileName.getText());
		logOutput.append(" and jar path ");
		logOutput.append(jarPath.getText());
		logOutput.append(" and root class ");
		logOutput.append(dataRootClassName.getText());
		logOutput.append(" and dataStore path ");
		logOutput.append(dataStorePath.getText());
		logOutput.append(" and package name ");
		logOutput.append(packageName.getText());
		logger.info(logOutput.toString());

		profile.setProfileName(profileName.getText());
		profile.setJarPath(jarPath.getText());
		profile.setDataRootClassName(dataRootClassName.getText());
		profile.setDataStorePath(dataStorePath.getText());
		profile.setPackageName(packageName.getText());
		profileController.update(profile);

		Stage currentStage = JavaFXHelper.getStageFromEvent(event);
		new ChooseProfileGuiController().openProfileChooserDialogue(currentStage);
	}

	/**
	 * Navigate back to chooseProfile dialogue.
	 * 
	 * @param event triggered by button
	 */
	@FXML
	public void handleBackButton(ActionEvent event) {
		Stage currentStage = JavaFXHelper.getStageFromEvent(event);
		new ChooseProfileGuiController().openProfileChooserDialogue(currentStage);
	}

	/**
	 * Gets the current edited profile.
	 * 
	 * @return the profile that is currently edited
	 */
	public DataStorageProfile getProfile() {
		return profile;
	}

	/**
	 * This method is called by the chooseProfile dialogue to transfer the selected
	 * item from the treeview to this edit dialogoue.
	 * 
	 * @param profile the profile that is selected for editing
	 */
	public void setProfile(DataStorageProfile profile) {
		this.profile = profile;

		if (PersistenceController.getInstance().root() == null) {
			logger.error("Error while trying to retrieve dataRoot");
			JavaFXHelper.quit();
		}

		List<DataStorageProfile> profiles = PersistenceController.getInstance().root().getProfiles();
		DataStorageProfile persistentProfile = profiles.stream()
				.filter(e -> e.getProfileName().equals(profile.getProfileName())).findFirst().orElse(null);

		if (persistentProfile != null) {
			this.profileName.setText(persistentProfile.getProfileName());
			this.jarPath.setText(persistentProfile.getJarPath());
			this.dataRootClassName.setText(persistentProfile.getDataRootClassName());
			this.dataStorePath.setText(persistentProfile.getDataStorePath());
			this.packageName.setText(persistentProfile.getPackageName());
		} else {
			logger.error("Could not find the persistent profile in the datastore!", profile.toString());
		}
	}

}