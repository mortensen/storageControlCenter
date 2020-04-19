/**
 * Java jigsaw modularization
 */
module scc {
	
	//needed exports
	exports de.mortensenit to javafx.graphics;
	exports de.mortensenit.gui to javafx.fxml;
	exports de.mortensenit.model to javafx.fxml;
	opens de.mortensenit.gui to javafx.fxml;

	// javafx
	requires transitive javafx.base;
	requires transitive javafx.controls;
	requires transitive javafx.fxml;
	requires transitive javafx.graphics;

	// log4j
	requires org.apache.logging.log4j.core;
	requires org.apache.logging.log4j;
	requires org.apache.logging.log4j.slf4j;
	requires junit;
	requires org.slf4j;

	// microstream
	requires one.microstream.all;

}