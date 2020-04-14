package de.mortensenit.persistence;

import java.util.List;

/**
 * Root class for all objects that will be serialized with microstream.
 * 
 * @author frederik.mortensen
 */
public class DataRoot {

	private List<DataStorageProfile> profiles = null;

	public List<DataStorageProfile> getProfiles() {
		return this.profiles;
	}

	public void setProfiles(List<DataStorageProfile> profiles) {
		this.profiles = profiles;
	}

}