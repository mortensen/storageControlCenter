package de.mortensenit.persistence;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import one.microstream.reference.LazyReferenceManager;
import one.microstream.storage.configuration.Configuration;
import one.microstream.storage.restservice.StorageRestService;
import one.microstream.storage.restservice.StorageRestServiceResolver;
import one.microstream.storage.restservice.sparkjava.StorageRestServiceSparkJava;
import one.microstream.storage.types.EmbeddedStorageManager;
import spark.Service;

/**
 * This class loads, saves and updates all objects that need serialization from
 * and to the microstream datastore.
 * 
 * @author frederik.mortensen
 */
public class PersistenceController {

	private Logger logger = LogManager.getLogger(getClass());

	/**
	 * the microStream datastorage access manager
	 */
	private EmbeddedStorageManager storageManager;

	private static PersistenceController SINGLETON = null;

	/**
	 * private controller as this will be a singleton
	 */
	private PersistenceController() {

	}

	/**
	 * get the singleton reference for the persistence controller
	 * 
	 * @return
	 */
	public static PersistenceController getInstance() {
		if (SINGLETON == null) {
			SINGLETON = new PersistenceController();
			SINGLETON.initDatastore();
		}
		return SINGLETON;
	}

	/**
	 * load datastore and setup initial values if it's created the first time
	 */
	public void initDatastore() {
		logger.info("Initializing datastore...");
		initDatastore("microstream/scc");
	}

	/**
	 * load datastore and setup initial values if it's created the first time
	 * 
	 * @param datastorePath the filesystem path where the datastore is persisted
	 */
	public void initDatastore(String datastorePath) {

		if (datastorePath == null)
			throw new IllegalArgumentException("Datastore path not set! Check your configuration!");

		// Configure paths and root, then create a storage manager instance
		// This will start 1 thread per channel.
		storageManager = Configuration.Default().setBaseDirectoryInUserHome(datastorePath).setChannelCount(1)
				.createEmbeddedStorageFoundation().createEmbeddedStorageManager();

		// startup storage
		storageManager.start();

		DataRoot dataRoot = null;
		if (storageManager.root() instanceof DataRoot) {
			dataRoot = (DataRoot) storageManager.root();
		} else {
			dataRoot = new DataRoot();
			storageManager.setRoot(dataRoot);
			storageManager.storeRoot();
		}

		//configure REST interface with spark as mentioned in the docs
		StorageRestService service = StorageRestServiceResolver.resolve(storageManager);
		service.start();

		StorageRestServiceSparkJava sparkService = StorageRestServiceSparkJava.New(storageManager);
		sparkService.setSparkService(Service.ignite().port(8080));
		sparkService.setInstanceName("scc");
		sparkService.start();

		// Start lazy reference management with timeout after
		// 10 minutes without any access. This will start a new thread.
		LazyReferenceManager.set(LazyReferenceManager.New(Duration.ofMinutes(10).toMillis()).start());

	}

	/**
	 * fetch storage manager
	 * 
	 * @return the reference to the microStream datastorage manager
	 */
	public EmbeddedStorageManager getStorageManager() {
		return storageManager;
	}

	/**
	 * fetch main data container
	 * 
	 * @return the main data container object
	 */
	public DataRoot root() {
		return storageManager != null ? (DataRoot) storageManager.root() : null;
	}

	/**
	 * closing the dataStore
	 */
	public void shutdown() {
		storageManager.shutdown();
	}

}