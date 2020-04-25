package de.mortensenit;

import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.mortensenit.gui.ChooseProfileGuiController;
import de.mortensenit.gui.SceneConstants;
import de.mortensenit.persistence.PersistenceController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Storage Control Center - Main Method / Starter class
 * 
 * @author frederik.mortensen
 *
 */
public class SCCFXStarter extends Application {

	private Logger logger = LogManager.getLogger(getClass());
//	private ApplicationConfiguration config;
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}

	/**
	 * Main entry point for the application
	 * 
	 * @param splashStage
	 */
	@Override
	public void start(Stage splashStage) throws Exception {
		Parent splashRoot = FXMLLoader.load(getClass().getResource(SceneConstants.SPLASH_DIALOGUE));
		Scene scene = new Scene(splashRoot, 800, 400);
		splashStage.setResizable(false);
		splashStage.setTitle("Storage Control Center");
		splashStage.setScene(scene);
		splashStage.show();

		startUpDataStore();

		new ChooseProfileGuiController().transistToProfileChooserDialogue(splashStage);
	}

	/**
	 * loading the configuration and persistence
	 * 
	 * @throws Exception
	 */
	private void startUpDataStore() throws Exception {

		logger.info("=== Starting datastore... ===");

//		config = ApplicationConfiguration.getInstance();

		PersistenceController.getInstance();

		logger.info("=== Datastore startup finished successfully: " + LocalDateTime.now() + " ===");
	}

}