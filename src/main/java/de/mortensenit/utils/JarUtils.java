package de.mortensenit.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Simple util class for everything java archive dependent. Searches for the
 * content of jars and returns lists of classes.
 * 
 * @author frederik.mortensen
 *
 */
public class JarUtils {

	private static Logger logger = LogManager.getLogger(JarUtils.class);

	/**
	 * 
	 * @param jarPath           the url to the file with classes that represent the
	 *                          app whose datastore content we will show
	 * @param dataRootClassName the fully qualified name of the class that should
	 *                          hold the application datastore model
	 * @return the class reference to the configured datastore root class
	 */
	public static Class<?> findDataRootClass(String jarPath, String dataRootClassName) {

		logger.info("Searching class for dataRoot name in external jar.");

		// TODO: Performance Optimierung - nicht den ganzen Baum laden?
		// first load the full class tree of the jar
		TreeMap<String, List<Class<?>>> packageAndClassTree = loadFullClassTree(jarPath);
		if (packageAndClassTree == null) {
			logger.error("Could not load class tree! Check your jar path: " + jarPath);
			return null;
		}

		// get package name and class name
		String[] packageAndClass = StringUtils.splitPackageAndClass(dataRootClassName);
		if (packageAndClass == null) {
			logger.error("Could not split dataRootClassName into package and class: " + dataRootClassName);
			return null;
		}

		String packageName = packageAndClass[0];

		// now get the list of classes inside the package of the dataRootClassName
		List<Class<?>> classes = packageAndClassTree.get(packageName);

		if (classes == null) {
			logger.error("Could not find any classes inside package " + packageName);
			return null;
		}

		// filter dataRootClass
		for (Class<?> clazz : classes) {
			if (clazz.getName().equals(dataRootClassName)) {
				return clazz;
			}
		}

		logger.warn("Could not find dataRootClass " + dataRootClassName + " inside package " + jarPath);
		return null;
	}

	/**
	 * Calls the method getRootPackageClasses to get all elements on the root level
	 * of the given jar. Then filters by name.
	 * 
	 * @param className the name of the class of interest to look after
	 * @param jarPath   the absolute path to the jar including .jar
	 * @return
	 */
	public static Class<?> getRootPackageClassByName(String className, String jarPath) {
		List<Class<?>> rootClassElements = getRootPackageClasses(jarPath);
		if (rootClassElements == null)
			return null;
		for (Class<?> clazz : rootClassElements) {
			if (clazz.getName().equals(className)) {
				return clazz;
			}
		}
		return null;
	}

	/**
	 * Get the top stack entries of the class tree inside the given jar. These are
	 * the classes that are inside the root package.
	 * 
	 * @param jarPath the filename of the jar that contains the classes, that will
	 *                be loaded into a class tree
	 * @return a list of classes on top of the packages
	 */
	public static List<Class<?>> getRootPackageClasses(String jarPath) {
		TreeMap<String, List<Class<?>>> classTree = loadFullClassTree(jarPath);
		if (classTree == null) {
			return null;
		}
		// return the classes inside the top package
		String rootPackage = classTree.firstKey();
		logger.info("Returning all classes inside the root package " + rootPackage);
		List<Class<?>> entryClasses = classTree.get(rootPackage);
		return entryClasses;
	}

	/**
	 * This method parses the jar path to an url, loads an url class loader and then
	 * loads all classes inside the jar into a class tree map which will be returned
	 * then.
	 * 
	 * @param jarPath the jar file containing the classes of interest
	 * @return the parsed class tree grouped by packages
	 */
	public static TreeMap<String, List<Class<?>>> loadFullClassTree(String jarPath) {

		// validate input parameters
		if (jarPath == null || !jarPath.endsWith(".jar") || jarPath.startsWith("file:")) {
			logger.info("Jar path value is invalid! " + jarPath);
			return null;
		}

		logger.info("Searching for jar: " + jarPath);

		URLClassLoader urlClassLoader = buildURLClassLoader(jarPath);
		TreeMap<String, List<Class<?>>> classTree = buildClassTree(jarPath, urlClassLoader);
		return classTree;

	}

	/**
	 * Generate a urlClassloader instance that will be able to instantiate classes
	 * from inside the jar file
	 * 
	 * @param jarPath the file system path to the application jar file
	 * @return urlClassLoader instance
	 */
	public static URLClassLoader buildURLClassLoader(String jarPath) {
		// load jar with classloader
		URL[] urls = new URL[1];
		URL url = null;
		try {
			url = new URL("file:" + jarPath);
		} catch (MalformedURLException e) {
			logger.error("The given url is invalid: " + jarPath);
			return null;
		}
		urls[0] = url;
		URLClassLoader urlClassloader = new URLClassLoader(urls);
		return urlClassloader;

	}

	/**
	 * First the jar file with the given path string is being loaded. Then we
	 * iterate over all entries. We filter only the classes of interest and build a
	 * tree map.
	 * 
	 * @param jarPath        the url to the file including .jar
	 * @param urlClassLoader the class loader that will be able to load the classes
	 *                       inside this jar. Those are classes that are NOT yet
	 *                       loaded as classes in this application, as they are not
	 *                       in our classpath yet!
	 * @return a tree of classes that were found inside the jar or null if something
	 *         went wrong
	 */
	private static TreeMap<String, List<Class<?>>> buildClassTree(String jarPath, URLClassLoader urlClassLoader) {

		// load the jar file from file system
		try (JarFile jar = new JarFile(jarPath)) {

			TreeMap<String, List<Class<?>>> classTreeMap = new TreeMap<>();

			// iterate over all stuff inside this jar
			Iterator<JarEntry> iterator = jar.entries().asIterator();

			while (iterator.hasNext()) {
				JarEntry jarEntry = iterator.next();
				String clazzName = jarEntry.getName();
				// we only care about .class files so far
				if (clazzName.endsWith(".class")) {
					if (!canClassBeSkipped(clazzName)) {
						// change path to package
						clazzName = clazzName.replaceAll("/", ".");
						// remove .class
						clazzName = clazzName.substring(0, clazzName.length() - 6);

						Class<?> clazz = null;
						try {
							// fully qualified class name
							clazz = urlClassLoader.loadClass(clazzName);
						} catch (ClassNotFoundException e) {
							logger.error("Could not find class named " + clazzName, e);
							return null;
						}

						// group classes by package
						String packageName = clazzName.substring(0, clazzName.lastIndexOf("."));
						List<Class<?>> classes = classTreeMap.get(packageName);
						if (classes == null) {
							classes = new ArrayList<>();
						}

						classes.add(clazz);
						classTreeMap.put(packageName, classes);
					}
				}
			}

			return classTreeMap;

		} catch (IOException e) {
			logger.error("The given jar path is invalid: " + jarPath);
			return null;
		} catch (SecurityException e) {
			logger.error("The jar is signed. Having trouble opening it!");
			return null;
		}
	}

	/**
	 * checks if this class file is not of interest
	 * 
	 * @param clazzName the name of the class including its package path
	 * @return true if its a class we don't care about
	 */
	private static boolean canClassBeSkipped(String clazzName) {
		if (StringUtils.isNullOrEmpty(clazzName)) {
			return true;
		}
		return clazzName.endsWith("module-info.class") || clazzName.endsWith("package-info.class");
	}

}