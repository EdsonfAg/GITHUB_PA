/**
 * 
 */
/**
 * 
 */
module PA {
	requires java.desktop;
	requires com.google.gson;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    exports Modelo.Vista; 
    opens Modelo.Vista to javafx.graphics, javafx.fxml; 
    exports Modelo;
    opens Modelo to javafx.graphics, javafx.fxml;
}