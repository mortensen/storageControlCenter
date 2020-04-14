package de.mortensenit.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Window;

/**
 * Simple gui controller for the about dialogue
 * @author frederik.mortensen
 *
 */
public class AboutGuiController {
	
	private Logger logger = LoggerFactory.getLogger(getClass().getName());

	@FXML
	private Button closeButton;

	@FXML
	public void handleCloseButton(ActionEvent event) {
		Node source = (Node) event.getSource();
		Window stage = source.getScene().getWindow();
		stage.hide();
	}

}
