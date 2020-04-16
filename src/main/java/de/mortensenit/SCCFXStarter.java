package de.mortensenit;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mortensenit.gui.ChooseProfileGuiController;
import de.mortensenit.persistence.PersistenceController;
import javafx.application.Application;
import javafx.application.Platform;
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

	private Logger logger = LoggerFactory.getLogger(getClass().getName());

	private PersistenceController persistenceController;

//	private ApplicationConfiguration config;

	/**
	 * Main entry point for the application
	 * 
	 * @param splashStage
	 */
	@Override
	public void start(Stage splashStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/scenes/splash.fxml"));
		Scene scene = new Scene(root, 800, 400);
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

		persistenceController = PersistenceController.getInstance();

		logger.info("=== Datastore startup finished successfully: " + LocalDateTime.now() + " ===");
	}

}