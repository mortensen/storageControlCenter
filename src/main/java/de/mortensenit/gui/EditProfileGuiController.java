package de.mortensenit.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mortensenit.model.DataStorageProfile;
import de.mortensenit.persistence.PersistenceController;
import de.mortensenit.persistence.ProfileController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

	private DataStorageProfile profile;

	private ProfileController profileController = new ProfileController();

	/**
	 * 
	 * @param event
	 */
	@FXML
	public void handleSaveButton(ActionEvent event) {

		logger.info(
				"Updating " + profileName.getText() + ", " + jarPath.getText() + ", " + dataRootClassName.getText());

		DataStorageProfile profile = new DataStorageProfile();
		profile.setProfileName(profileName.getText());
		profile.setJarPath(jarPath.getText());
		profile.setDataRootClassName(dataRootClassName.getText());
		profileController.update(profile);

		Node source = (Node) event.getSource();
		Stage currentStage = (Stage) source.getScene().getWindow();
		new ChooseProfileGuiController().openProfileChooserDialogue(currentStage);
	}

	/**
	 * 
	 * @param event
	 */
	@FXML
	public void handleBackButton(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage currentStage = (Stage) source.getScene().getWindow();
		new ChooseProfileGuiController().openProfileChooserDialogue(currentStage);
	}

	public DataStorageProfile getProfile() {
		return profile;
	}

	public void setProfile(DataStorageProfile profile) {
		logger.info("Try: " + profile.getProfileName());
		this.profile = profile;
		DataStorageProfile persistentProfile = PersistenceController.getInstance().root().getProfiles().stream()
				.filter(e -> e.getProfileName().equals(profile.getProfileName())).findFirst().orElse(null);
		if (persistentProfile != null) {
			this.profileName.setText(persistentProfile.getProfileName());
			this.jarPath.setText(persistentProfile.getJarPath());
			this.dataRootClassName.setText(persistentProfile.getDataRootClassName());
		} else {
			logger.error("Not found");
		}
	}

}
