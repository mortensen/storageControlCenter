package de.mortensenit.persistence;

import java.util.Date;
import java.util.List;

import de.mortensenit.model.DataStorageProfile;

/**
 * Root class for all objects that will be serialized with microstream.
 * 
 * @author frederik.mortensen
 */
public class DataRoot {

	private Date created;

	private List<DataStorageProfile> profiles;

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public List<DataStorageProfile> getProfiles() {
		return this.profiles;
	}

	public void setProfiles(List<DataStorageProfile> profiles) {
		this.profiles = profiles;
	}

}