package de.mortensenit.persistence;

/**
 * A collection of application wide constants
 * 
 * @author frederik.mortensen
 *
 */
public class Constants {

	/**
	 * name of the application's configuration file
	 */
	public static final String APPLICATION_PROPERTIES = "app.properties";

	/**
	 * The path the datastore files are persisted to (inside the user directory!).
	 */
	public static final String DATASTORE_DIRECTORY = "datastore.directory";

	/**
	 * Filesystem path where the datastore backup will be saved into. The backup is
	 * managed by MicroStream.
	 */
	public static final String BACKUP_DIRECTORY = "datastore.backup.directory";

	/**
	 * Filesystem path where the exported bin file with the datastore data will be
	 * saved into.
	 */
	public static final String BIN_EXPORT_DIRECTORY = "datastore.bin.export.directory";

	/**
	 * Filesystem path where the exported csv file with the datastore data will be
	 * saved into.
	 */
	public static final String CSV_EXPORT_DIRECTORY = "datastore.csv.export.directory";

}
