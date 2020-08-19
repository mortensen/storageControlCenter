package de.mortensenit.persistence;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import one.microstream.reference.LazyReferenceManager;
import one.microstream.storage.configuration.Configuration;
import one.microstream.storage.types.EmbeddedStorageFoundation;
import one.microstream.storage.types.EmbeddedStorageManager;

/**
 * This class is used to connect to datastores of external applications and load
 * their data.
 * 
 * @author frederik.mortensen
 *
 */
public class MicroStreamController {

	private Logger logger = LogManager.getLogger(getClass());

	private EmbeddedStorageManager storageManager = null;

	private Object dataRoot = null;

	/**
	 * Loads a connection to an external microstream datastore
	 * 
	 * @param datastorePath this is where the channel files and TypeDescriptor are
	 * @param jarPath       the path to the application jar with the classes that
	 *                      are used with this external datastore
	 * @param packageName   the name of the packages that need to be loaded
	 *                      externally
	 */
	public void connect(String datastorePath, String jarPath, String packageName) {

		if (datastorePath == null)
			throw new IllegalArgumentException("Datastore path not set! Check your configuration!");

		// Configure paths and root, then create a storage manager instance
		// This will start 1 thread per channel.
		EmbeddedStorageFoundation<?> foundation = Configuration.Default().setBaseDirectory(datastorePath)
				.setChannelCount(1).createEmbeddedStorageFoundation();

		// This will fix the one.microstream.persistence.exceptions.PersistenceException
		// problem with missing runtime type

		URL[] urls = new URL[1];
		try {
			URI uri = URI.create("file://" + jarPath);
			urls[0] = uri.toURL();
		} catch (MalformedURLException e) {
			logger.error("Could not open jar at path " + jarPath, e);
			return;
		}

		foundation.getConnectionFoundation().setClassLoaderProvider(typeName -> {
			if (typeName.startsWith(packageName)) {
				return new URLClassLoader(urls);
			}
			return ClassLoader.getSystemClassLoader();
		});
		storageManager = foundation.createEmbeddedStorageManager();

		// startup storage
		storageManager.start();

		// Start lazy reference management with timeout after
		// 10 minutes without any access. This will start a new thread.
		LazyReferenceManager.set(LazyReferenceManager.New(Duration.ofMinutes(10).toMillis()).start());
	}

	/**
	 * shutdown the storage manager
	 */
	public void disconnect() {
		storageManager.shutdown();
	}

	public EmbeddedStorageManager getStorageManager() {
		return storageManager;
	}

	public void setStorageManager(EmbeddedStorageManager storageManager) {
		this.storageManager = storageManager;
	}

	public void setDataRoot(Object dataRoot) {
		this.dataRoot = dataRoot;
	}

	public Object getDataRoot() {
		return dataRoot;
	}

}