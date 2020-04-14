package de.mortensenit;

import static de.mortensenit.persistence.Constants.APPLICATION_PROPERTIES;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper for the filesystem configuration.
 * 
 * @author frederik.mortensen
 *
 */
public class ApplicationConfiguration {

	private Logger logger = LoggerFactory.getLogger(getClass());

	// filesystem settings for the application
	private Properties applicationProperties;

	private static ApplicationConfiguration SINGLETON = null;

	/**
	 * singleton instance, so no public default constructor
	 */
	private ApplicationConfiguration() {
	}

	/**
	 * returns the singleton reference to this object
	 * 
	 * @return
	 */
	public static ApplicationConfiguration getInstance() throws IllegalArgumentException, IOException {
		if (SINGLETON == null) {
			SINGLETON = new ApplicationConfiguration();
			SINGLETON.loadConfiguration();
		}
		return SINGLETON;
	}

	/**
	 * load application configuration from property file
	 * 
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	private void loadConfiguration() throws IllegalArgumentException, IOException {

		logger.info("Loading application config...");

		applicationProperties = new Properties();
		URL url = ClassLoader.getSystemResource(APPLICATION_PROPERTIES);
		if (url == null)
			throw new IllegalArgumentException("Application properties are missing!");

		applicationProperties.load(url.openStream());

		logger.info("Application config loaded.");
	}

	/**
	 * get the value to the given key from the filesystem property file
	 * 
	 * @param key
	 * @return the value to the given key
	 */
	public String getProperty(String key) {
		return applicationProperties.getProperty(key);
	}

	/**
	 * load the property with the given type from the filesystem property
	 * 
	 * @param <T>   the type that the value needs to be casted to
	 * @param key   the search key in the properties
	 * @param clazz target type for the value
	 * @return the casted property value
	 */
	@SuppressWarnings("unchecked")
	public <T> T getProperty(String key, Class<T> clazz) {

		String value = getProperty(key);

		if (value == null) {
			throw new IllegalArgumentException("Value is null!");
		}

		if (clazz == Path.class) {
			return (T) Path.of(value);
		}

		throw new IllegalArgumentException("Class type is not supported!");
	}

}
