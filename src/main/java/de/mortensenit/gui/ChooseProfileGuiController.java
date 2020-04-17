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
	 * @param splashStage
	 */
	public void transistToProfileChooserDialogue(Stage splashStage) {
		// wait 3 seconds, then switch from splash to chooseProfile view
		PauseTransition pause = new PauseTransition(Duration.seconds(1));
		pause.setOnFinished(event -> {
			openProfileChooserDialogue(splashStage);
		});
		pause.play();
	}

	/**
	 * open the profile chooser dialogue
	 * 
	 * @param splashStage
	 */
	public void openProfileChooserDialogue(ActionEvent event) {
		Node node = (Node) (event.getSource());
		Stage currentStage = (Stage) node.getScene().getWindow();
		openProfileChooserDialogue(currentStage);
	}

	/**
	 * open the profile chooser dialogue
	 * 
	 * @param currentStage
	 */
	public void openProfileChooserDialogue(Stage currentStage) {
		try {
			Stage chooseProfileStage = new Stage();
			Parent chooseProfileRoot = FXMLLoader.load(getClass().getResource("/scenes/chooseProfile.fxml"));
			Scene chooseProfileScene = new Scene(chooseProfileRoot, 800, 600);
			addRepeatingSideImage(chooseProfileRoot);
			loadProfileList(chooseProfileRoot);
			chooseProfileStage.setScene(chooseProfileScene);
			chooseProfileStage.setTitle("Storage Control Center - Profil auswählen");

			chooseProfileStage.setOnCloseRequest(e -> {
				System.exit(0);
			});

			chooseProfileStage.show();
			currentStage.close();
		} catch (IOException e) {
			logger.error("", e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * add repeatable graphic to the left
	 * 
	 * @param root
	 */
	private void addRepeatingSideImage(Parent root) {
		VBox leftVBox = (VBox) root.lookup("#leftVBox");
		Image sideLogoImage = new Image(getClass().getResourceAsStream("/images/sidelogo.png"));
		BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
		BackgroundImage backgroundImage = new BackgroundImage(sideLogoImage, BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
		leftVBox.setBackground(new Background(backgroundImage));
	}

	/**
	 * load profiles and add them to the list
	 * 
	 * @param root
	 */
	private void loadProfileList(Parent root) {

		String[] profileNames = profileController.getProfileNames();

		ScrollPane scrollPane = (ScrollPane) root.lookup("#scrollPane");
		ListView<String> listView = new ListView<String>();
		listView.setId("profilesListView");
		if (profileNames != null) {
			ObservableList<String> items = FXCollections.observableArrayList(profileNames);
			listView.setItems(items);
		}
		listView.setPadding(new Insets(5, 5, 5, 5));
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				logger.info("Selected: " + newValue);
				root.setUserData(newValue);
			}
		});
		VBox vbox = new VBox(listView);
		scrollPane.setContent(vbox);
	}

	/**
	 * 
	 * @param event
	 */
	@FXML
	public void handleNewButton(ActionEvent event) {
		try {
			Stage newStage = new Stage();
			Parent newRoot = FXMLLoader.load(getClass().getResource("/scenes/newProfile.fxml"));
			Scene newScene = new Scene(newRoot, 800, 600);
			newStage.setScene(newScene);
			newStage.setTitle("Storage Control Center - Neues Profil anlegen");

			addRepeatingSideImage(newRoot);

			newStage.setOnCloseRequest(e -> {
				newStage.hide();
				Node node = (Node) (event.getSource());
				Stage stage = (Stage) node.getScene().getWindow();
				stage.show();
			});

			hideCurrentWindow(event);
			newStage.show();
		} catch (IOException e) {
			logger.error("", e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * 
	 * @param event
	 */
	@FXML
	public void handleEditButton(ActionEvent event) {
		try {
			Stage editStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/editProfile.fxml"));
			Parent editRoot = loader.load();
			
			EditProfileGuiController controller = loader.getController();
			DataStorageProfile profile = new DataStorageProfile();
			
			Node tempNode = (Node) (event.getSource());
			Scene scene = (Scene) tempNode.getScene();
			ListView<String> listView = (ListView<String>)scene.lookup("#profilesListView");
			String profileName = listView.getSelectionModel().getSelectedItem();

			profile.setProfileName(profileName);
			controller.setProfile(profile);

			Scene editScene = new Scene(editRoot, 800, 600);
			editStage.setScene(editScene);
			editStage.setTitle("Storage Control Center - Profil bearbeiten");

			addRepeatingSideImage(editRoot);

			editStage.setOnCloseRequest(e -> {
				editStage.hide();
				Node node = (Node) (event.getSource());
				Stage stage = (Stage) node.getScene().getWindow();
				stage.show();
			});

			hideCurrentWindow(event);
			editStage.show();
		} catch (IOException e) {
			logger.error("", e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * 
	 * @param event
	 */
	@FXML
	public void handleOpenButton(ActionEvent event) {
		try {
			Stage showDataStoreContentStage = new Stage();
			Parent showDataStoreContentRoot = FXMLLoader
					.load(getClass().getResource("/scenes/showDataStoreContent.fxml"));
			Scene showDataStoreContentScene = new Scene(showDataStoreContentRoot, 800, 600);
			showDataStoreContentStage.setScene(showDataStoreContentScene);
			// TODO: Dummy Wert
			showDataStoreContentStage.setTitle("Storage Control Center - DEV");

			// TODO: Fake Daten
			TreeItem<String> rootItem = new TreeItem<String>("DataStore", null);
			rootItem.setExpanded(true);

			TreeItem<String> item = new TreeItem<String>("Users");
			rootItem.getChildren().add(item);

			TreeItem<String> item2 = new TreeItem<String>("Contacts");
			rootItem.getChildren().add(item2);

			TreeItem<String> item3 = new TreeItem<String>("Addresses");
			rootItem.getChildren().add(item3);

			ScrollPane treeScrollPane = (ScrollPane) showDataStoreContentRoot.lookup("#treeScrollPane");
			TreeView<String> treeView = new TreeView<String>();
			treeView.setRoot(rootItem);
			treeScrollPane.setContent(treeView);

			showDataStoreContentStage.setOnCloseRequest(e -> {
				System.exit(0);
			});

			showDataStoreContentStage.show();
			hideCurrentWindow(event);
		} catch (IOException e) {
			logger.error("", e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * 
	 * @param event
	 */
	@FXML
	public void handleDeleteButton(ActionEvent event) {
	}

	/**
	 * 
	 * @param event
	 */
	@FXML
	public void handleCloseButton(ActionEvent event) {
		quit();
	}

	/**
	 * 
	 */
	@FXML
	public void handleFileExitMenuItem() {
		quit();
	}

	/**
	 * 
	 * @param event
	 */
	@FXML
	public void handleAboutMenuItem(ActionEvent event) {
		try {
			Stage aboutStage = new Stage();
			Parent aboutRoot = FXMLLoader.load(getClass().getResource("/scenes/about.fxml"));
			Scene aboutScene = new Scene(aboutRoot, 800, 400);
			aboutStage.setResizable(false);
			aboutStage.setAlwaysOnTop(true);
			aboutStage.setScene(aboutScene);
			aboutStage.setTitle("Über SCC");
			aboutStage.show();
		} catch (IOException e) {
			logger.error("", e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * 
	 * @param event
	 */
	private void hideCurrentWindow(ActionEvent event) {
		Node node = (Node) (event.getSource());
		Window window = node.getScene().getWindow();
		window.hide();
	}

	@FXML
	public void quit() {
		logger.info("System.exit(0);");
		System.exit(0);
	}

}
