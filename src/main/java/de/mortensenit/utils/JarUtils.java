package de.mortensenit.utils;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: needs quality check
 * Simple util class for everything java archive dependent.
 * 
 * @author frederik.mortensen
 *
 */
public class JarUtils {

	private static Logger logger = LoggerFactory.getLogger(JarUtils.class.getName());

	/**
	 * Get the top stack entries of the class tree.
	 * 
	 * @param jarName the jar file containing the datastore classes
	 * @return
	 * @throws Exception
	 */
	public static List<Class<?>> getEntryClasses(String jarName) throws Exception {
		TreeMap<String, List<Class<?>>> classTree = loadJar(jarName);
		List<Class<?>> entryClasses = classTree.get(classTree.firstKey());
		return entryClasses;
	}

	/**
	 * TODO: documentation
	 * 
	 * @param jarName the jar file containing the datastore classes
	 * @return
	 * @throws Exception
	 */
	public static TreeMap<String, List<Class<?>>> loadJar(String jarName) throws Exception {

		// validate input parameters
		if (jarName == null || !jarName.endsWith(".jar") || jarName.startsWith("file:"))
			throw new IllegalArgumentException("Value is invalid!" + jarName);

		logger.info("Searching for jar: " + jarName);

		// load jar with classloader
		URL[] urls = new URL[1];
		URL url = new URL("file:" + jarName);
		urls[0] = url;
		URLClassLoader urlClassloader = new URLClassLoader(urls);

		TreeMap<String, List<Class<?>>> classTree = getClassTree(jarName, urlClassloader);
		urlClassloader.close();

		return classTree;
	}

	/**
	 * TODO: documentation
	 * 
	 * @param jarName
	 * @param urlClassLoader
	 * @return
	 * @throws Exception
	 */
	private static TreeMap<String, List<Class<?>>> getClassTree(String jarName, URLClassLoader urlClassLoader)
			throws Exception {

		// find jar content
		JarFile jar = new JarFile(jarName);
		Iterator<JarEntry> iterator = jar.entries().asIterator();

		TreeMap<String, List<Class<?>>> treeMap = new TreeMap<>();

		while (iterator.hasNext()) {
			JarEntry jarEntry = iterator.next();
			String clazzName = jarEntry.getName();
			if (clazzName.endsWith(".class")) {
				if (!(clazzName.endsWith("module-info.class") || clazzName.endsWith("package-info.class"))) {
					// change path to package
					clazzName = clazzName.replaceAll("/", ".");
					// remove .class
					clazzName = clazzName.substring(0, clazzName.length() - 6);
					String packageName = clazzName.substring(0, clazzName.lastIndexOf("."));
					List<Class<?>> classes = treeMap.get(packageName);
					if (classes == null) {
						classes = new ArrayList<>();
					}
					classes.add(urlClassLoader.loadClass(clazzName));
					treeMap.put(packageName, classes);
				}
			}
		}
		jar.close();

		return treeMap;
	}

}