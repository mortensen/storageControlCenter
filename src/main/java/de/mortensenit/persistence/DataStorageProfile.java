package de.mortensenit.persistence;

/**
 * Model representation for serialized microstream storage configurations.
 * 
 * @author frederik.mortensen
 *
 */
public class DataStorageProfile {

	private String name;

	private String jarPath;

	private String dataRootClassName;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return getName();
	}

}