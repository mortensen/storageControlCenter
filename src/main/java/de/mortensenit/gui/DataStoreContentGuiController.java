package de.mortensenit.gui;

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

	@FXML
	private Button backButton;

	@FXML
	private MenuItem fileExitMenuItem;

	@FXML
	private MenuItem aboutMenuItem;

	@FXML
	private Button closeButton;

	/**
	 * 
	 * @param event
	 */
	@FXML
	private void handleBackButton(ActionEvent event) {
		new ChooseProfileGuiController().openProfileChooserDialogue(event);
	}

	/**
	 * quit program
	 * 
	 * @param event
	 */
	@FXML
	private void handleFileExitMenuItem(ActionEvent event) {
		System.exit(0);
	}

	/**
	 * show about dialogue
	 * 
	 * @param event
	 */
	@FXML
	private void handleAboutMenuItem(ActionEvent event) {
		new ChooseProfileGuiController().handleAboutMenuItem(event);
	}

	@FXML
	private void handleCloseButton(ActionEvent event) {
		System.exit(0);
	}

}
