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
public class MicroStreamController<T> {

	private EmbeddedStorageManager storageManager = null;

	private T dataRoot = null;

	/**
	 * 
	 * @param datastorePath
	 */
	private void connect(String datastorePath) {

		if (datastorePath == null)
			throw new IllegalArgumentException("Datastore path not set! Check your configuration!");

		// Configure paths and root, then create a storage manager instance
		// This will start 1 thread per channel.
		storageManager = Configuration.Default().setBaseDirectoryInUserHome(datastorePath).setChannelCount(1)
				.createEmbeddedStorageFoundation().createEmbeddedStorageManager();

		// startup storage
		storageManager.start();

		// Start lazy reference management with timeout after
		// 10 minutes without any access. This will start a new thread.
		LazyReferenceManager.set(LazyReferenceManager.New(Duration.ofMinutes(10).toMillis()).start());
	}

	/**
	 * 
	 */
	public void disconnect() {
		// TODO:...
	}

	/**
	 * generate an instance of a microStream controller, connect to the configured
	 * external app dataStore and get the dataRoot
	 * 
	 * @param <T>
	 * @param dataRoot
	 * @param dataStorePath
	 * @return
	 */
	public static <T> MicroStreamController<T> generateMicroStreamController(String dataStorePath, T dataRoot) {
		MicroStreamController<T> microStreamController = new MicroStreamController<T>();
		microStreamController.connect(dataStorePath);
		microStreamController.setDataRoot((T)microStreamController.getStorageManager().root());
		return microStreamController;
	}

	public void setDataRoot(T dataRoot) {
		this.dataRoot = dataRoot;
	}

	public T getDataRoot() {
		return dataRoot;
	}

	public EmbeddedStorageManager getStorageManager() {
		return storageManager;
	}

	public void setStorageManager(EmbeddedStorageManager storageManager) {
		this.storageManager = storageManager;
	}

}
