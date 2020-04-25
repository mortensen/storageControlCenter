package de.mortensenit.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import one.microstream.collections.types.XSequence;
import one.microstream.storage.types.EmbeddedStorageManager;
import one.microstream.storage.types.StorageConnection;
import one.microstream.storage.types.StorageDataConverterCsvConfiguration;
import one.microstream.storage.types.StorageDataConverterTypeBinaryToCsv;
import one.microstream.storage.types.StorageEntityTypeConversionFileProvider;
import one.microstream.storage.types.StorageEntityTypeExportFileProvider;
import one.microstream.storage.types.StorageEntityTypeExportStatistics;
import one.microstream.storage.types.StorageLockedFile;
import one.microstream.util.cql.CQL;

/**
 * 
 * @author frederik.mortensen
 *
 */
public class CSVExport {

	private Logger logger = LogManager.getLogger(getClass());

	private EmbeddedStorageManager embeddedStorageManager;

	/**
	 * 
	 * @param embeddedStorageManager
	 */
	public CSVExport(EmbeddedStorageManager embeddedStorageManager) {
		this.embeddedStorageManager = embeddedStorageManager;
	}

	/**
	 * 
	 * @param binExportPath
	 * @param csvExportPath
	 * @throws IOException
	 */
	public void exportCSV(Path binExportPath, Path csvExportPath) throws IOException {
		logger.info("Starting bin exports...");
		XSequence<Path> binFiles = createSortedBinaries(binExportPath);
		logger.info("Starting csv exports...");
		exportFilesToCSV(binFiles, csvExportPath);
		logger.info("Export finished.");
	}

	/**
	 * 
	 * @param binExportPath
	 * @throws IOException
	 */
	private XSequence<Path> createSortedBinaries(Path binExportPath) throws IOException {
		String fileSuffix = "bin";

		// create directory if needed
		try {
			FileSystemUtils.deleteDirectory(binExportPath);
		} catch (IOException e) {
		}
		Files.createDirectory(binExportPath);

		StorageConnection connection = embeddedStorageManager.createConnection();
		StorageEntityTypeExportStatistics exportResult = connection.exportTypes(
				new StorageEntityTypeExportFileProvider.Default(binExportPath, fileSuffix), typeHandler -> true);
		XSequence<Path> exportFiles = CQL.from(exportResult.typeStatistics().values())
				.project(s -> Paths.get(s.file().identifier())).execute();
		return exportFiles;
	}

	/**
	 * 
	 * @param binPath
	 * @param csvExportPath
	 * @throws IOException
	 */
	private void exportFilesToCSV(XSequence<Path> binFiles, Path csvExportPath) throws IOException {

		// create directory if needed
		try {
			FileSystemUtils.deleteDirectory(csvExportPath);
		} catch (IOException e) {
		}
		Files.createDirectory(csvExportPath);

		if (binFiles != null && binFiles.iterator() != null) {
			Iterator<Path> iterator = binFiles.iterator();
			while (iterator.hasNext()) {
				Path binFilePath = iterator.next();
				exportFileToCSV(binFilePath, csvExportPath);
			}
		}
	}

	/**
	 * exports a single binary file into csv
	 * 
	 * @param binFilePath
	 * @param csvExportPath
	 * @throws IOException
	 */
	private void exportFileToCSV(Path binFilePath, Path csvExportPath) throws IOException {
		String fileSuffix = "csv";

		StorageDataConverterTypeBinaryToCsv converter = new StorageDataConverterTypeBinaryToCsv.UTF8(
				StorageDataConverterCsvConfiguration.defaultConfiguration(),
				new StorageEntityTypeConversionFileProvider.Default(csvExportPath, fileSuffix),
				embeddedStorageManager.typeDictionary(), null, 4096, 4096);
		StorageLockedFile storageFile = StorageLockedFile.openLockedFile(binFilePath);
		try {
			converter.convertDataFile(storageFile);
		} finally {
			storageFile.close();
		}
	}

}
