package Modelo.Vista;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class MainView extends Application{

    public void start(Stage primaryStage) {
        // Crear botones del menú principal
        Button btnProductos = new Button("Productos");
        Button btnVentas = new Button("Ventas");
        Button btnInventario = new Button("Inventario");

        // Layout de la ventana principal
        VBox layout = new VBox(10);
        layout.getChildren().addAll(btnProductos, btnVentas, btnInventario);

        // Configurar escena
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setTitle("Menú Principal");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
