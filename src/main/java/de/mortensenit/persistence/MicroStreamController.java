package de.mortensenit.persistence;

import java.time.Duration;

import one.microstream.reference.LazyReferenceManager;
import one.microstream.storage.configuration.Configuration;
import one.microstream.storage.types.EmbeddedStorageManager;

/**
 * This class is used to connect to datastores of external applications and load
 * their data.
 * 
 * @author frederik.mortensen
 *
 */
public class MicroStreamController {

	private EmbeddedStorageManager storageManager = null;

	private Object dataRoot = null;

	/**
	 * Loads a connection to an external microstream datastore
	 * 
	 * @param datastorePath this is where the channel files and TypeDescriptor are
	 * @param jarPath       the path to the application jar with the classes that
	 *                      are used with this external datastore
	 */
	public void connect(String datastorePath, String jarPath) {

		if (datastorePath == null)
			throw new IllegalArgumentException("Datastore path not set! Check your configuration!");

		// Configure paths and root, then create a storage manager instance
		// This will start 1 thread per channel.
		storageManager = Configuration.Default().setBaseDirectory(datastorePath).setChannelCount(1)
				.createEmbeddedStorageFoundation().createEmbeddedStorageManager();

		// TODO: current issue:
		// one.microstream.persistence.exceptions.PersistenceException:
		// Missing runtime type for required type handler for
		// type: de.mortensenit.example.persistence.ExampleDataRoot

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