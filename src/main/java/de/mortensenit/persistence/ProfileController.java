package de.mortensenit.persistence;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mortensenit.gui.JavaFXHelper;
import de.mortensenit.model.DataStorageProfile;

/**
 * Manages the profiles in the datastore
 * 
 * @author frederik.mortensen
 *
 */
public class ProfileController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private PersistenceController persistenceController;

	/**
	 * default constructor
	 */
	public ProfileController() {
		persistenceController = PersistenceController.getInstance();
	}

	/**
	 * Loads the persistent profiles from the datastore and returns a list of their
	 * profile names.
	 * 
	 * @return an arry of profileNames
	 */
	public String[] getProfileNames() {
		String[] profileNames = null;
		DataRoot dataRoot = persistenceController.root();
		if (dataRoot != null) {
			if (dataRoot.getProfiles() != null) {
				if (dataRoot.getProfiles().size() > 0) {
					logger.info("Profiles found:" + dataRoot.getProfiles().size());
					profileNames = new String[dataRoot.getProfiles().size()];
					for (int i = 0; i < dataRoot.getProfiles().size(); i++) {
						profileNames[i] = dataRoot.getProfiles().get(i).getProfileName();
					}
				} else {
					logger.info("Profiles list was empty.");
				}
			} else {
				logger.info("No profiles found.");
			}
		} else {
			logger.error("DataRoot was null!");
			JavaFXHelper.quit();
		}
		return profileNames;
	}

	/**
	 * Insert a new profile into the datastore
	 * 
	 * @param profile the datastore profile that needs to be persisted
	 */
	public void save(DataStorageProfile profile) {
		List<DataStorageProfile> profiles = persistenceController.root().getProfiles();
		if (profiles == null) {
			logger.info("Initializing new profile list as they were null");
			profiles = new ArrayList<DataStorageProfile>();
		}
		profiles.add(profile);
		persistenceController.root().setProfiles(profiles);
		persistenceController.getStorageManager().storeRoot();
		persistenceController.getStorageManager().store(profiles);
	}

	/**
	 * Update an existing profile in the datastore
	 * 
	 * @param profile the datastore profile that needs to be updated
	 */
	public void update(DataStorageProfile profile) {
		if (PersistenceController.getInstance().root() == null) {
			logger.error("Error while trying to retrieve dataRoot");
			JavaFXHelper.quit();
		}

		List<DataStorageProfile> profiles = PersistenceController.getInstance().root().getProfiles();
		
		if (profiles == null) {
			profiles = new ArrayList<DataStorageProfile>();
			logger.info("No entries yet. Creating new entry.");
			profiles.add(profile);
			persistenceController.root().setProfiles(profiles);
			persistenceController.getStorageManager().storeRoot();
			persistenceController.getStorageManager().store(profiles);
		} else {
			DataStorageProfile current = profiles.stream()
					.filter(e -> e.getProfileName().equals(profile.getProfileName())).findFirst().orElse(null);
			if (current != null) {
				logger.info("Entry for update found");
				profiles.remove(current);
				profiles.add(profile);
				persistenceController.root().setProfiles(profiles);
				persistenceController.getStorageManager().storeRoot();
				persistenceController.getStorageManager().store(profiles);
			} else {
				logger.info("Inserting a new entry, because I didn't find anything to update.");
				profiles.add(profile);
				persistenceController.root().setProfiles(profiles);
				persistenceController.getStorageManager().storeRoot();
				persistenceController.getStorageManager().store(profiles);
			}
		}
	}

}