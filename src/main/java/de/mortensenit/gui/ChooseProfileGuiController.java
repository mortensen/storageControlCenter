package de.mortensenit.gui;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.mortensenit.model.DataStorageProfile;
import de.mortensenit.persistence.PersistenceController;
import de.mortensenit.persistence.ProfileController;
import de.mortensenit.utils.JarUtils;
import de.mortensenit.utils.StringUtils;
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

	private Logger logger = LogManager.getLogger(getClass());

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
		DataStorageProfile selectedProfile = fetchSelectedProfile(scene);
		editProfileGuiController.setProfile(selectedProfile);

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
		DataStoreContentGuiController dataStoreContentGuiController = null;

		try {
			// initalize the controller to be able to set data into the target view
			FXMLLoader loader = new FXMLLoader(getClass().getResource(SceneConstants.SHOW_DATASTORE_CONTENT));
			showDataStoreContentRoot = loader.load();
			dataStoreContentGuiController = loader.getController();
		} catch (IOException e) {
			logger.error("This scene could not be loaded! " + SceneConstants.SHOW_DATASTORE_CONTENT, e);
			JavaFXHelper.quit();
		}

		// find the list view to be able to get the selected item
		Scene scene = JavaFXHelper.getSceneFromEvent(event);
		DataStorageProfile selectedProfile = fetchSelectedProfile(scene);
		dataStoreContentGuiController.setDataStoreaDataStorageProfile(selectedProfile);

		Scene showDataStoreContentScene = new Scene(showDataStoreContentRoot, 800, 600);
		showDataStoreContentStage.setScene(showDataStoreContentScene);
		showDataStoreContentStage.setTitle("Storage Control Center - " + selectedProfile.getProfileName());

		// build tree by loading the jar content, finding the datastore and get the
		// child elements
		ScrollPane treeScrollPane = (ScrollPane) showDataStoreContentRoot.lookup("#treeScrollPane");
		TreeView<String> treeView = generateDataStoreTreeView(selectedProfile);
		if (treeView == null) {
			logger.error("Can not show an empty tree view!");
			return;
		}
		treeScrollPane.setContent(treeView);

		showDataStoreContentStage.setOnCloseRequest(e -> {
			JavaFXHelper.quit();
		});

		showDataStoreContentStage.show();
		hideCurrentWindow(event);
	}

	/**
	 * load the jar file from the given path, search for the datastore root class
	 * inside it and then build the tree view for this dataRoot. This is where the
	 * magic happens!
	 * 
	 * @param selectedProfile
	 * @return
	 */
	private TreeView<String> generateDataStoreTreeView(DataStorageProfile selectedProfile) {
		logger.debug("Loading tree content...");

		// First get persistent version of selected profile to have all data needed
		String profileName = selectedProfile.getProfileName();
		PersistenceController persistenceController = PersistenceController.getInstance();
		List<DataStorageProfile> profiles = persistenceController.root().getProfiles();

		for (DataStorageProfile profile : profiles) {
			if (profile.getProfileName().equals(profileName)) {
				selectedProfile = profile;
				break;
			}
		}

		String jarPath = selectedProfile.getJarPath();
		String dataRootClassName = selectedProfile.getDataRootClassName();
		logger.info("Loading dataRoot with name " + dataRootClassName + " from " + jarPath);

		// Next create a JavaFX tree root item
		TreeView<String> treeView = new TreeView<String>();
		TreeItem<String> rootItem = new TreeItem<String>(dataRootClassName, null);
		rootItem.setExpanded(true);

		// Now fill tree with items
		appendDataRootChildren(rootItem, jarPath, dataRootClassName);
		// appendAllJarItems(rootItem, jarPath);

		treeView.setRoot(rootItem);
		return treeView;
	}

	/**
	 * Now that we have a root element for our tree and the name of the jar file we
	 * can load and build our tree. In this case only the classes of the dataRoot
	 * will be added.
	 * 
	 * @param rootItem          the topmost element that was configured in the
	 *                          profile
	 * @param jarPath           the url to the file with classes that represent the
	 *                          app whose datastore content we will show
	 * @param dataRootClassName the fully qualified name of the class that should
	 *                          hold the application datastore model
	 */
	private void appendDataRootChildren(TreeItem<String> rootItem, String jarPath, String dataRootClassName) {
		Class<?> dataRootClass = findDataRootClass(jarPath, dataRootClassName);
		URLClassLoader urlClassLoader = JarUtils.buildURLClassLoader(jarPath);

		if (dataRootClass != null) {
			// subtrees aufbauen...
			List<TreeItem<String>> children = getTreeChildren(dataRootClass, urlClassLoader);
			rootItem.getChildren().addAll(children);
		} else {
			logger.error("Could not parse dataRoot class named " + dataRootClassName);
		}

	}

	/**
	 * 
	 * @param jarPath           the url to the file with classes that represent the
	 *                          app whose datastore content we will show
	 * @param dataRootClassName the fully qualified name of the class that should
	 *                          hold the application datastore model
	 * @return the class reference to the configured datastore root class
	 */
	private Class<?> findDataRootClass(String jarPath, String dataRootClassName) {

		// first load the full class tree of the jar
		// TODO: Performance Optimierung - nicht den ganzen Baum laden?
		TreeMap<String, List<Class<?>>> packageAndClassTree = JarUtils.loadFullClassTree(jarPath);
		if (packageAndClassTree == null) {
			logger.error("Could not load class tree! Check your jar path: " + jarPath);
			return null;
		}

		// get package name and class name
		String[] packageAndClass = StringUtils.splitPackageAndClass(dataRootClassName);
		if (packageAndClass == null) {
			logger.error("Could not split dataRootClassName into package and class: " + dataRootClassName);
			return null;
		}

		String packageName = packageAndClass[0];

		// now get the list of classes inside the package of the dataRootClassName
		List<Class<?>> classes = packageAndClassTree.get(packageName);

		if (classes == null) {
			logger.error("Could not find any classes inside package " + packageName);
			return null;
		}

		// filter dataRootClass
		for (Class<?> clazz : classes) {
			if (clazz.getName().equals(dataRootClassName)) {
				return clazz;
			}
		}

		logger.warn("Could not find dataRootClass " + dataRootClassName + " inside package " + jarPath);
		return null;
	}

	/**
	 * Beginning with the dataRoot class we will search for all fields and then loop
	 * over their recursive fields. Then the tree of all subentries will be
	 * returned.
	 * 
	 * @param clazz          the dataRoot parent class
	 * @param urlClassLoader helps instantiating classes inside the application jar
	 * @return the list of child entries and their recursive children e.G. Person ->
	 *         Address -> Street
	 */
	private List<TreeItem<String>> getTreeChildren(Class<?> clazz, URLClassLoader urlClassLoader) {
		List<TreeItem<String>> firstLevelEntries = new ArrayList<TreeItem<String>>();

		for (Field field : clazz.getDeclaredFields()) {
			TreeItem<String> firstLevelEntry = new TreeItem<String>(field.getName(), null);
			List<TreeItem<String>> children = getRecursiveFields(field, firstLevelEntry, urlClassLoader);
			if (children != null) {
				firstLevelEntry.getChildren().addAll(children);
			}
			firstLevelEntries.add(firstLevelEntry);
		}
		return firstLevelEntries;
	}

	/**
	 * recursive method to get all child tree elements to be able to build the tree
	 * 
	 * @param parent         the parent field whose children we will examine
	 * @param parentEntry    the parent tree node that will recieve the child
	 *                       entries
	 * @param urlClassLoader instantiate external classes
	 * @return the list of child elements that the given field holds
	 */
	private List<TreeItem<String>> getRecursiveFields(Field parent, TreeItem<String> parentEntry,
			URLClassLoader urlClassLoader) {
		// when we reach class level, we stop recursion
		if (parent.getName().equals("clazz") && parent.getType().equals(Class.class))
			return null;

		Field[] childFields = null;
		if (parent.getType().isInstance(new ArrayList<>())) {
			//in case of lists we need to get the parameterized type (e.g. List<Person>)
			ParameterizedType listType = (ParameterizedType) parent.getGenericType();
			Type type = listType.getActualTypeArguments()[0];
			try {
				Class<?> clazz = urlClassLoader.loadClass(type.getTypeName());
				childFields = clazz.getDeclaredFields();
			} catch (ClassNotFoundException e) {
				logger.error("Could not parse parameterized type and instantiate it! Type was: " + type.getTypeName(), e);
				return null;
			}
		} else {
			//simple objects
			childFields = parent.getType().getDeclaredFields();
		}
		// if there are no children, then there is nothing to do
		if (childFields.length == 0)
			return null;

		List<TreeItem<String>> childTreeItems = new ArrayList<>();
		for (Field childField : childFields) {
			TreeItem<String> childTreeItem = new TreeItem<>(childField.getName());
			childTreeItems.add(childTreeItem);
			List<TreeItem<String>> grandChildren = getRecursiveFields(childField, childTreeItem, urlClassLoader);
			if (grandChildren == null) {
				return childTreeItems;
			} else {
				childTreeItem.getChildren().addAll(grandChildren);
			}
		}
		return childTreeItems;
	}

	/**
	 * Now that we have a root element for our tree and the name of the jar file we
	 * can load and build our tree. In this case the whole jar content will be
	 * loaded, but only class files.
	 * 
	 * @param rootItem the topmost element that was configured in the profile
	 * @param jarPath  the url to the file with classes that represent the app whose
	 *                 datastore content we will show
	 */
	private void appendAllJarItems(TreeItem<String> rootItem, String jarPath) {
		// first load the full class tree of the jar
		TreeMap<String, List<Class<?>>> packageAndClassTree = JarUtils.loadFullClassTree(jarPath);
		if (packageAndClassTree == null) {
			logger.error("Could not load class tree! Check your jar path: " + jarPath);
			return;
		}

		// next loop over all packages
		Iterator<String> packagesIterator = packageAndClassTree.navigableKeySet().iterator();
		while (packagesIterator.hasNext()) {

			// add the package itself to the tree
			String packageName = packagesIterator.next();
			TreeItem<String> packageItem = new TreeItem<String>();
			packageItem.setValue(packageName);
			rootItem.getChildren().add(packageItem);

			// add the classes to the package
			List<Class<?>> clazzes = packageAndClassTree.get(packageName);
			for (Class<?> clazz : clazzes) {
				TreeItem<String> item = new TreeItem<String>(clazz.getSimpleName());
				packageItem.getChildren().add(item);
			}
		}
	}

	/**
	 * 
	 * @param event
	 */
	@FXML
	public void handleDeleteButton(ActionEvent event) {
		// find the list view to be able to get the selected item
		Scene scene = JavaFXHelper.getSceneFromEvent(event);
		DataStorageProfile selectedProfile = fetchSelectedProfile(scene);
		if (selectedProfile == null || selectedProfile.getProfileName() == null) {
			logger.info("Nothing to delete");
			return;
		}
		profileController.delete(selectedProfile);
		loadProfileList(scene.getRoot());
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

	/**
	 * check which element was selected and generate a profile that can be processed
	 * (like i.e. edited or opened)
	 * 
	 * @param scene
	 * @return
	 */
	private DataStorageProfile fetchSelectedProfile(Scene scene) {
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
		return profile;
	}

}