package de.mortensenit.persistence;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	 * 
	 * @return
	 */
	public String[] getProfileNames() {
		String[] profileNames = null;
		DataRoot dataRoot = persistenceController.root();
		if (dataRoot != null) {
			if (dataRoot.getProfiles() != null) {
				if (dataRoot.getProfiles().size() > 0) {
					profileNames = new String[dataRoot.getProfiles().size()];
					for (int i = 0; i < dataRoot.getProfiles().size(); i++) {
						profileNames[i] = dataRoot.getProfiles().get(i).getProfileName();
					}
				} else {
					logger.error("Pofile waren leer");
				}
			} else {
				logger.error("Profile waren null");
			}
		} else {
			logger.error("DataRoot war null");
		}
		return profileNames;
	}

	/**
	 * 
	 * @param profile
	 */
	public void save(DataStorageProfile profile) {
		List<DataStorageProfile> profiles = persistenceController.root().getProfiles();
		if (profiles == null)
			profiles = new ArrayList<DataStorageProfile>();
		profiles.add(profile);
		persistenceController.root().setProfiles(profiles);
		persistenceController.getStorageManager().storeRoot();
	}

	/**
	 * 
	 * @param profile
	 */
	public void update(DataStorageProfile profile) {
		List<DataStorageProfile> profiles = persistenceController.root().getProfiles();
		if (profiles == null)
			profiles = new ArrayList<DataStorageProfile>();
		DataStorageProfile current = profiles.stream().filter(e -> e.getProfileName().equals(profile.getProfileName()))
				.findFirst().get();
		profiles.remove(current);
		profiles.add(profile);
		persistenceController.root().setProfiles(profiles);
		persistenceController.getStorageManager().storeRoot();
	}

}
