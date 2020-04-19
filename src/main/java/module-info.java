/**
 * Java jigsaw modularization
 */
module scc {

	// javafx
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;

	// log4j
	requires org.apache.logging.log4j.core;
	requires org.apache.logging.log4j;
	requires org.apache.logging.log4j.slf4j;
	requires junit;
	requires org.slf4j;

	// microstream
	requires one.microstream.all;

	//needed exports
	exports de.mortensenit to javafx.graphics;
	exports de.mortensenit.gui to javafx.fxml;
	opens de.mortensenit.gui to javafx.fxml;

}