package de.mortensenit.utils;

/**
 * Basic helper class for string processing.
 * 
 * @author frederik.mortensen
 *
 */
public class StringUtils {

	/**
	 * Check if the given string(s) is/are null or empty.
	 * 
	 * @param strings vararg containing the strings that need to be checked
	 * @return true if no parameters were entered, one or more parameters are null
	 *         or empty.
	 */
	public static boolean isNullOrEmpty(String... strings) {
		if (strings == null) {
			return true;
		}
		for (String string : strings) {
			if (string == null)
				return true;
			if (string.trim().length() == 0)
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param fullClassName
	 * @return
	 */
	public static String[] splitPackageAndClass(String fullClassName) {
		if (StringUtils.isNullOrEmpty(fullClassName)) {
			return null;
		}
		
		String packageName = fullClassName.substring(0, fullClassName.lastIndexOf("."));
		String className = fullClassName.replace(packageName + ".", "");
		String[] packageAndClass = new String[2];
		packageAndClass[0] = packageName;
		packageAndClass[1] = className;
		return packageAndClass;
	}

}