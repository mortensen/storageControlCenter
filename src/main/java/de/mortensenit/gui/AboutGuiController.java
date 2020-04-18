package de.mortensenit.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Simple gui controller for the about dialogue. Shows informations about the
 * author and software.
 * 
 * @author frederik.mortensen
 *
 */
public class AboutGuiController {

	private Logger logger = LoggerFactory.getLogger(getClass().getName());

	@FXML
	private Button closeButton;

	/**
	 * Closes the current modal window
	 * 
	 * @param event triggered by button
	 */
	@FXML
	public void handleCloseButton(ActionEvent event) {
		logger.debug("Closing about window...");
		Stage stage = JavaFXHelper.getStageFromEvent(event);
		stage.close();
	}

}