/**
 * Java jigsaw modularization
 */
module scc {

	// needed exports
	exports de.mortensenit;

	opens de.mortensenit to javafx.fxml;
	opens de.mortensenit.gui to javafx.fxml;

	// javafx
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires transitive javafx.graphics;

	// log4j
	requires org.slf4j;

	// microstream
	requires one.microstream.all;

}