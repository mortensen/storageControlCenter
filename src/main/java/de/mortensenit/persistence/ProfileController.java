package de.mortensenit.persistence;

/**
 * Manages the profiles in the datastore
 * 
 * @author frederik.mortensen
 *
 */
public class ProfileController {

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
		if (dataRoot != null && dataRoot.getProfiles() != null && dataRoot.getProfiles().size() > 0) {
			profileNames = new String[dataRoot.getProfiles().size()];
			for (int i = 0; i < dataRoot.getProfiles().size(); i++) {
				profileNames[i] = dataRoot.getProfiles().get(i).getProfileName();
			}
		}
		return profileNames;
	}

}
