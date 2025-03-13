module PA {
    requires java.desktop; // Para Swing
    requires com.google.gson; // Para JSON

    exports App;
   // exports Controlador;
    exports Modelo;
    exports Persistencia;
    exports Vista;
}