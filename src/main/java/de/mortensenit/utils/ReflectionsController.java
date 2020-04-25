package de.mortensenit.utils;

/**
 * Simple helper tool for all kinds of reflections
 * 
 * @author frederik.mortensen
 *
 */
public class ReflectionsController {

	/**
	 * Removes the .class suffix and the class name and then returns the package
	 * name
	 * 
	 * @param className the fully qualified class name
	 * @return the extracted package name
	 */
	public static String extractPackage(String className) {
		className = className.substring(0, className.length() - 6);
		int lastIndex = className.lastIndexOf(".");
		return className.substring(0, lastIndex);
	}

}