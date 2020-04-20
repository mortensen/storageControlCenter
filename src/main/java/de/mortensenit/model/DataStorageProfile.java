package de.mortensenit.model;

/**
 * Model representation for serialized microstream storage configurations.
 * 
 * @author frederik.mortensen
 *
 */
public class DataStorageProfile {

	private String profileName;

	private String jarPath;

	private String dataRootClassName;

	/**
	 * In case of a profile update we need the old name to find it in the database
	 * as primary key
	 */
	private transient String persistentProfileName;

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getJarPath() {
		return this.jarPath;
	}

	public void setJarPath(String jarPath) {
		this.jarPath = jarPath;
	}

	public String getDataRootClassName() {
		return this.dataRootClassName;
	}

	public void setDataRootClassName(String dataRootClassName) {
		this.dataRootClassName = dataRootClassName;
	}

	public String getPersistentProfileName() {
		return persistentProfileName;
	}

	public void setPersistentProfileName(String persistentProfileName) {
		this.persistentProfileName = persistentProfileName;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("ProfileName: ");
		stringBuffer.append(profileName);
		stringBuffer.append(", Jar Path: ");
		stringBuffer.append(jarPath);
		stringBuffer.append(", dataRootClassName: ");
		stringBuffer.append(dataRootClassName);
		return stringBuffer.toString();
	}

}