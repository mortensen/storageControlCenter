/**
 * Java jigsaw modularization
 */
module scc {

	// needed exports
	exports de.mortensenit;

	opens de.mortensenit to javafx.fxml;
	opens de.mortensenit.gui to javafx.fxml;

	// javafx
	requires transitive javafx.base;
	requires transitive javafx.controls;
	requires transitive javafx.fxml;
	requires transitive javafx.graphics;

	// log4j
	requires org.apache.logging.log4j;

	// microstream
	requires one.microstream.all;
	requires transitive storage.restservice;
	requires transitive storage.restservice.sparkjava;
	requires transitive spark.core;

}