package de.mortensenit.utils;

import java.io.IOException;

/**
 * 
 * @author frederik.mortensen
 *
 */
public class DeepCloneService {

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		new DeepCloneService().start();
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void start() throws IOException {

//		// create datastore
//		DataRoot dataRoot = new DataRoot();
//		EmbeddedStorageManager storageManager = EmbeddedStorage.start(dataRoot);
//		storageManager.storeRoot();
//
//		// save object
//		Object toClone = new Object();
//		dataRoot.setObject(toClone);
//		storageManager.storeRoot();
//		storageManager.store(toClone);
//
//		// load clone
//		storageManager.shutdown();
//		dataRoot = new DataRoot();
//		storageManager = EmbeddedStorage.start(dataRoot);
//
//		Object cloned = (Object) dataRoot.getObject();
//
//		System.out.println("Object clone successful: " + toClone != cloned);
//
//		// delete db
//		File file = new File("storage");
//		FileSystemUtils.deleteDirectory(file);
//		System.exit(0);
	}

}
