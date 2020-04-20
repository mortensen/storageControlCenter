package de.mortensenit.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mortensenit.persistence.PersistenceController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Collection of the most common use cases for JavaFX
 * 
 * @author frederik.mortensen
 *
 */
public class JavaFXHelper {
	
	private static Logger logger = LoggerFactory.getLogger(JavaFXHelper.class);
	
	/**
	 * Extracts the stage from the event and returns it. This method helps using the DRY pattern.
	 * @param event
	 * @return the stage this event was triggered from
	 */
	public static Stage getStageFromEvent(ActionEvent event) {
		Node node = (Node) event.getSource();
		Stage currentStage = (Stage) node.getScene().getWindow();
		return currentStage;
	}
	
	/**
	 * Extracts the scene from the event and returns it. This method helps using the DRY pattern.
	 * @param event
	 * @return the scene this event was triggered from
	 */
	public static Scene getSceneFromEvent(ActionEvent event) {
		Node node = (Node) event.getSource();
		Scene scene = node.getScene();
		return scene;
	}
	
	/**
	 * by quitting the application the microstream db will be shutdown
	 */
	public static void quit() {
		logger.info("Quitting application");
		PersistenceController.getInstance().shutdown();
		Platform.exit();
		System.exit(0);
	}

}
