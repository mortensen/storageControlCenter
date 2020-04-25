package de.mortensenit.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.mortensenit.model.DataStorageProfile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;

/**
 * The main class that shows the content of a microstream datastore in a tree
 * view.
 * 
 * @author frederik.mortensenn
 *
 */
public class DataStoreContentGuiController {

	private Logger logger = LogManager.getLogger(getClass());

	@FXML
	private Button backButton;

	@FXML
	private MenuItem fileExitMenuItem;

	@FXML
	private MenuItem aboutMenuItem;

	@FXML
	private Button closeButton;

	private DataStorageProfile dataStoreaDataStorageProfile;

	/**
	 * Navigates back to chooseProfile dialogue.
	 * 
	 * @param event triggered by button
	 */
	@FXML
	private void handleBackButton(ActionEvent event) {
		logger.debug("Opening chooseProfile dialogue");
		new ChooseProfileGuiController().openProfileChooserDialogue(event);
	}

	/**
	 * Menu item that quits the program.
	 * 
	 * @param event
	 */
	@FXML
	private void handleFileExitMenuItem(ActionEvent event) {
		JavaFXHelper.quit();
	}

	/**
	 * Show the about dialogue.
	 * 
	 * @param event triggered by button
	 */
	@FXML
	private void handleAboutMenuItem(ActionEvent event) {
		logger.debug("Opening about dialogue");
		new ChooseProfileGuiController().handleAboutMenuItem(event);
	}

	/**
	 * Quit the application.
	 * 
	 * @param event triggered by button
	 */
	@FXML
	private void handleCloseButton(ActionEvent event) {
		JavaFXHelper.quit();
	}

	public DataStorageProfile getDataStoreaDataStorageProfile() {
		return dataStoreaDataStorageProfile;
	}

	public void setDataStoreaDataStorageProfile(DataStorageProfile dataStoreaDataStorageProfile) {
		this.dataStoreaDataStorageProfile = dataStoreaDataStorageProfile;
	}

}
