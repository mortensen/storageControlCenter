package de.mortensenit.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple util class for fileystem IO stuff.
 * 
 * @author frederik.mortensen
 *
 */
public class FileSystemUtils {

	private static Logger logger = LoggerFactory.getLogger(FileSystemUtils.class.getName());

	/**
	 * Deletes a directory
	 * 
	 * @param file the directory to delete
	 * @throws IOException
	 */
	public static void deleteDirectory(File file) throws IOException {
		logger.info("Deleting directory " + file.getAbsolutePath());
		Path path = Path.of(file.getAbsolutePath());
		deleteDirectory(path);
	}

	/**
	 * Recursive deletion of path
	 * 
	 * @param path the path that should be deleted
	 * @throws IOException
	 */
	public static void deleteDirectory(Path path) throws IOException {
		Files.walk(path).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
	}

}