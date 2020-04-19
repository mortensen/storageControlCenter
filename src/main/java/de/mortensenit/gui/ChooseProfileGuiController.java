package de.mortensenit.gui;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mortensenit.model.DataStorageProfile;
import de.mortensenit.persistence.ProfileController;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

/**
 * This dialogue shows a list of managed microstream profiles, that can be
 * created, edited, opened and deleted.
 * 
 * @author frederik.mortensen
 *
 */
public class ChooseProfileGuiController {

	private Logger logger = LoggerFactory.getLogger(getClass().getName());

	@FXML
	private Button newButton;

	@FXML
	private Button editButton;

	@FXML
	private Button openButton;

	@FXML
	private Button deleteButton;

	@FXML
	private Button closeButton;

	@FXML
	private MenuItem fileExitMenuItem;

	@FXML
	private MenuItem aboutMenuItem;

	private ProfileController profileController = new ProfileController();

	/**
	 * make the transistion from splash screen to profile chooser dialogue
	 * 
	 * @param splashStage the source screen
	 */
	public void transistToProfileChooserDialogue(Stage splashStage) {
		// Wait n seconds, then switch from splash screen to chooseProfile view
		PauseTransition pause = new PauseTransition(Duration.seconds(1));
		pause.setOnFinished(event -> {
			openProfileChooserDialogue(splashStage);
		});
		pause.play();
	}

	/**
	 * open the profile chooser dialogue
	 * 
	 * @param event triggered by button
	 */
	public void openProfileChooserDialogue(ActionEvent event) {
		Stage currentStage = JavaFXHelper.getStageFromEvent(event);
		openProfileChooserDialogue(currentStage);
	}

	/**
	 * open the profile chooser dialogue
	 * 
	 * @param currentStage the source stage to navigate away from
	 */
	public void openProfileChooserDialogue(Stage currentStage) {
		Stage chooseProfileStage = new Stage();
		Parent chooseProfileRoot = null;

		try {
			chooseProfileRoot = FXMLLoader.load(getClass().getResource(SceneConstants.CHOOSE_PROFILE));
		} catch (IOException e) {
			logger.error("This scene could not be loaded! " + SceneConstants.CHOOSE_PROFILE, e);
			JavaFXHelper.quit();
		}

		Scene chooseProfileScene = new Scene(chooseProfileRoot, 800, 600);
		addRepeatingSideImage(chooseProfileRoot);
		loadProfileList(chooseProfileRoot);
		chooseProfileStage.setScene(chooseProfileScene);
		chooseProfileStage.setTitle("Storage Control Center - Profil auswählen");

		chooseProfileStage.setOnCloseRequest(e -> {
			JavaFXHelper.quit();
		});

		chooseProfileStage.show();
		currentStage.close();
	}

	/**
	 * add repeatable graphic to the left
	 * 
	 * @param root the stage to add the image to
	 */
	private void addRepeatingSideImage(Parent root) {
		VBox leftVBox = (VBox) root.lookup("#leftVBox");
		Image sideLogoImage = new Image(getClass().getResourceAsStream(ResourceConstants.IMAGE_SIDELOGO));
		BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
		BackgroundImage backgroundImage = new BackgroundImage(sideLogoImage, BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
		leftVBox.setBackground(new Background(backgroundImage));
	}

	/**
	 * load profiles and add them to the list view
	 * 
	 * @param root the target stage where the data will get into
	 */
	private void loadProfileList(Parent root) {

		String[] profileNames = profileController.getProfileNames();

		ScrollPane scrollPane = (ScrollPane) root.lookup("#scrollPane");
		ListView<String> listView = new ListView<String>();
		listView.setId("profilesListView");

		if (profileNames != null) {
			// set items from database
			ObservableList<String> items = FXCollections.observableArrayList(profileNames);
			listView.setItems(items);
		}

		listView.setPadding(new Insets(5, 5, 5, 5));
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				logger.debug("Selected: " + newValue);
				// set selected item into window user data
				root.setUserData(newValue);
			}

		});
		VBox vbox = new VBox(listView);
		scrollPane.setContent(vbox);
	}

	/**
	 * open the new profile dialogue
	 * 
	 * @param event triggered by a button
	 */
	@FXML
	public void handleNewButton(ActionEvent event) {
		Stage newStage = new Stage();
		Parent newRoot = null;

		try {
			newRoot = FXMLLoader.load(getClass().getResource(SceneConstants.NEW_PROFILE));
		} catch (IOException e) {
			logger.error("This scene could not be loaded!" + SceneConstants.NEW_PROFILE, e);
			JavaFXHelper.quit();
		}

		Scene newScene = new Scene(newRoot, 800, 600);
		newStage.setScene(newScene);
		newStage.setTitle("Storage Control Center - Neues Profil anlegen");

		addRepeatingSideImage(newRoot);

		newStage.setOnCloseRequest(e -> {
			Stage stage = JavaFXHelper.getStageFromEvent(event);
			stage.show();
			newStage.hide();
		});

		newStage.show();
		hideCurrentWindow(event);
	}

	/**
	 * open the edit profile dialogue
	 * 
	 * @param event triggerd by a button
	 */
	@FXML
	public void handleEditButton(ActionEvent event) {
		Stage editStage = new Stage();
		Parent editRoot = null;
		EditProfileGuiController editProfileGuiController = null;

		try {
			// initalize the controller to be able to set data into the target view
			FXMLLoader loader = new FXMLLoader(getClass().getResource(SceneConstants.EDIT_PROFILE));
			editRoot = loader.load();
			editProfileGuiController = loader.getController();
		} catch (IOException e) {
			logger.error("This scene could not be loaded! " + SceneConstants.EDIT_PROFILE, e);
			JavaFXHelper.quit();
		}

		// find the list view to be able to get the selected item
		Scene scene = JavaFXHelper.getSceneFromEvent(event);
		ListView<?> listView = null;
		Node profilesListViewNode = scene.lookup("#profilesListView");
		if (profilesListViewNode instanceof ListView<?>) {
			listView = (ListView<?>) profilesListViewNode;
		} else {
			logger.error("Could not find profilesListView node. " + profilesListViewNode);
			JavaFXHelper.quit();
		}

		// generate a profile with the selected item and set it into the controller
		String profileName = (String) listView.getSelectionModel().getSelectedItem();

		DataStorageProfile profile = new DataStorageProfile();
		profile.setProfileName(profileName);
		profile.setPersistentProfileName(profileName);
		editProfileGuiController.setProfile(profile);

		Scene editScene = new Scene(editRoot, 800, 600);
		editStage.setScene(editScene);
		editStage.setTitle("Storage Control Center - Profil bearbeiten");

		addRepeatingSideImage(editRoot);

		editStage.setOnCloseRequest(e -> {
			Stage chooseProfileStage = JavaFXHelper.getStageFromEvent(event);
			chooseProfileStage.show();
			editStage.hide();
		});

		editStage.show();
		hideCurrentWindow(event);
	}

	/**
	 * open the main dialogue to show the datastore content
	 * 
	 * @param event triggered by a button
	 */
	@FXML
	public void handleOpenButton(ActionEvent event) {
		Stage showDataStoreContentStage = new Stage();
		Parent showDataStoreContentRoot = null;

		try {
			showDataStoreContentRoot = FXMLLoader.load(getClass().getResource(SceneConstants.SHOW_DATASTORE_CONTENT));
		} catch (IOException e) {
			logger.error("This scene could not be loaded! " + SceneConstants.SHOW_DATASTORE_CONTENT, e);
			JavaFXHelper.quit();
		}

		Scene showDataStoreContentScene = new Scene(showDataStoreContentRoot, 800, 600);
		showDataStoreContentStage.setScene(showDataStoreContentScene);
		// TODO: Fake Daten im Titel
		showDataStoreContentStage.setTitle("Storage Control Center - DEV");
		TreeView<String> treeView = generateTreeView();
		ScrollPane treeScrollPane = (ScrollPane) showDataStoreContentRoot.lookup("#treeScrollPane");
		treeScrollPane.setContent(treeView);

		showDataStoreContentStage.setOnCloseRequest(e -> {
			JavaFXHelper.quit();
		});

		showDataStoreContentStage.show();
		hideCurrentWindow(event);
	}

	/**
	 * 
	 * @return
	 */
	private TreeView<String> generateTreeView() {
		logger.debug("Loading tree...");
		// TODO: Fake Daten
		TreeItem<String> rootItem = new TreeItem<String>("DataStore", null);
		rootItem.setExpanded(true);

		TreeItem<String> item = new TreeItem<String>("Users");
		rootItem.getChildren().add(item);

		TreeItem<String> item2 = new TreeItem<String>("Contacts");
		rootItem.getChildren().add(item2);

		TreeItem<String> item3 = new TreeItem<String>("Addresses");
		rootItem.getChildren().add(item3);

		TreeView<String> treeView = new TreeView<String>();
		treeView.setRoot(rootItem);
		return treeView;
	}

	/**
	 * 
	 * @param event
	 */
	@FXML
	public void handleDeleteButton(ActionEvent event) {
		// TODO: add delete functionality
	}

	/**
	 * @see #quit()
	 * @param event triggered by a button
	 */
	@FXML
	public void handleCloseButton(ActionEvent event) {
		JavaFXHelper.quit();
	}

	/**
	 * @see #quit()
	 */
	@FXML
	public void handleFileExitMenuItem() {
		JavaFXHelper.quit();
	}

	/**
	 * shows the about software dialogue as modal
	 * 
	 * @param event triggered by a button
	 */
	@FXML
	public void handleAboutMenuItem(ActionEvent event) {
		Stage aboutStage = null;
		Parent aboutRoot = null;
		try {
			aboutStage = new Stage();
			aboutRoot = FXMLLoader.load(getClass().getResource(SceneConstants.ABOUT_DIALOGUE));
		} catch (IOException e) {
			logger.error("This scene could not be loaded! " + SceneConstants.ABOUT_DIALOGUE, e);
			JavaFXHelper.quit();
		}
		Scene aboutScene = new Scene(aboutRoot, 800, 400);
		aboutStage.setResizable(false);
		aboutStage.setAlwaysOnTop(true);
		aboutStage.setScene(aboutScene);
		aboutStage.setTitle("Über SCC");
		aboutStage.show();
	}

	/**
	 * used whenever a new scene is opened and the current stage should be hidden
	 * 
	 * @param event triggered by a button
	 */
	private void hideCurrentWindow(ActionEvent event) {
		Node node = (Node) (event.getSource());
		Window window = node.getScene().getWindow();
		window.hide();
	}

}